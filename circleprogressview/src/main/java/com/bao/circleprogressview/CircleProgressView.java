package com.bao.circleprogressview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;


public class CircleProgressView extends View {
    //控件的宽
    private int width;
    //控件的高
    private int height;

    //底层画笔
    private Paint paint_base_progress;
    //进度条的画笔
    private Paint paint_progress;
    //画笔宽度
    private int strokeWidth = dp2px(5);
    //绘制半径
    private int radius;
    //区域
    private RectF rectF;
    //绘制标志
    private boolean drawing = false;


    //底层扫描渐变色
    private Shader shader_base_progress;
    //底层条浅的色
    private int base_progress_light_color;
    //底层条深的色
    private int base_progress_height_color;
    //底层颜色数组
    private int base_progress_colors[];
    //底层颜色分配
    private float base_progress_positions[];
    //进度条扫描渐变色
    private Shader shader_progress;
    //进度条浅的色
    private int progress_light_color;
    //进度条深的色
    private int progress_height_color;
    //底层颜色数组
    private int progress_colors[];
    //底层颜色分配
    private float progress_positions[];
    //进度条的当前角度
    private float progress_angle = 0f;
    //底层进度条的角度
    private float base_angle = 360f;
    //顺时针
    private boolean progress_clockwise = true;
    //速度,刷新频率最快60帧，参数为每次刷新的时长，单位：毫秒
    private long speed = 33;
    //30帧的绘制速度
    private int frame = 30;
    //转一圈的周期，单位：毫秒
    private long period = 3000;
    //循环
    private boolean progress_recycler = false;
    //暂停
    private boolean progress_pause = false;

    private OnProgressListener onProgressListener;

    //开始
    private static final int START = 1111;
    //结束
    private static final int COMPLETE = 1000;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case START:
                    //顺时针转
                    if (progress_clockwise) {
                        //每圈360°不变，共period辣么长的ms，360f/period算出每ms转的角度，再用结果*speed就是每次刷新转的角度
                        progress_angle += 360f / period * speed;
                    }
                    //逆时针转
                    else {
                        progress_angle -= 360f / period * speed;
                    }
                    //进度
                    if (onProgressListener != null) {
                        onProgressListener.onProgress((int) (Math.abs(progress_angle / 3.6f)), Math.abs(progress_angle / 3.6f));
                    }

                    //转满一圈就停止，并且恢复底色,绝对值＞360
                    if (Math.abs(progress_angle) > 360) {
                        drawing = false;
                        handler.sendEmptyMessage(COMPLETE);
                    } else {
                        drawing = true;
                        invalidate();
                        //我试过，1ms，10ms，速度都是一样，但是100ms，1000ms，就明显不一样
                        //打游戏打得多，我怀疑是刷新频率最快60帧，约16.67ms≈17ms，果然如此
                        handler.sendEmptyMessageDelayed(START, speed);
                    }
                    break;
                case COMPLETE:
                    //完成
                    if (onProgressListener != null) {
                        onProgressListener.onComplete();
                    }
                    invalidate();
                    if (progress_recycler) {
                        startProgress();
                    }
                    break;
            }
            return false;
        }
    });

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 由于要使用xml配置控件属性，所以要3个参数构造方法
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化颜色,如果不设置颜色参数，就会使用这个颜色参数
        base_progress_light_color = Color.WHITE;
        base_progress_height_color = Color.BLACK;

        progress_light_color = Color.WHITE;
        progress_height_color = Color.RED;

        //获取自定义样式的属性
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressView, defStyleAttr, 0);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.CircleProgressView_base_progress_light_color) {
                base_progress_light_color = typedArray.getColor(attr, Color.WHITE);

            } else if (attr == R.styleable.CircleProgressView_base_progress_height_color) {
                base_progress_height_color = typedArray.getColor(attr, Color.BLACK);

            } else if (attr == R.styleable.CircleProgressView_progress_light_color) {
                progress_light_color = typedArray.getColor(attr, Color.WHITE);

            } else if (attr == R.styleable.CircleProgressView_progress_height_color) {
                progress_height_color = typedArray.getColor(attr, Color.RED);

            } else if (attr == R.styleable.CircleProgressView_progress_width) {//转化为px,TypedValue也可以将DIP（DP）转PX
                strokeWidth = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                        , dp2px(7), getResources().getDisplayMetrics()));

            } else if (attr == R.styleable.CircleProgressView_progress_radius) {
                radius = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                        , 0, getResources().getDisplayMetrics()));

            } else if (attr == R.styleable.CircleProgressView_progress_clockwise) {
                progress_clockwise = typedArray.getBoolean(attr, true);

            } else if (attr == R.styleable.CircleProgressView_progress_period) {
                period = typedArray.getInteger(attr, 3000);

            } else if (attr == R.styleable.CircleProgressView_progress_frame) {//最低17ms
                frame = typedArray.getInteger(attr, 30);
                speed = 1000 / frame;
            } else if (attr == R.styleable.CircleProgressView_progress_recycler) {
                progress_recycler = typedArray.getBoolean(attr, false);

            }
        }
        typedArray.recycle();

        //初始化画笔
        paint_progress = initPaint();
        paint_base_progress = initPaint();
        base_progress_colors = new int[]{base_progress_light_color, base_progress_height_color, base_progress_light_color};
        //这个参数就是颜色的分配，第一种颜色到0.1的位置，第二种到0.9的位置，第三种到1的位置，不要超过1。
        base_progress_positions = new float[]{0.1f, 0.95f, 1f};

        progress_colors = new int[]{progress_light_color, progress_height_color, progress_light_color};
        progress_positions = new float[]{0.1f, 0.95f, 1f};
    }

    /**
     * 初始化画笔
     */
    private Paint initPaint() {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        //笔画圆润,Paint.Cap.ROUND
        paint.setStrokeCap(Paint.Cap.BUTT);
        //抗锯齿
        paint.setAntiAlias(true);
        return paint;
    }

    /**
     * 进度条开始
     */
    public void startProgress() {
        //开始
        if (onProgressListener != null) {
            onProgressListener.onStart();
        }
        progress_angle = 0f;
        //每次都清除
        handler.removeMessages(START);
        handler.sendEmptyMessageDelayed(START, speed);
    }

    /**
     * 暂停进度
     */
    public void pauseProgress() {
        if (handler.hasMessages(START) && !progress_pause) {
            progress_pause = true;
            handler.removeMessages(START);
        }
    }

    /**
     * 继续进度
     */
    public void continueProgress() {
        if (!handler.hasMessages(START) && progress_pause) {
            progress_pause = false;
            handler.sendEmptyMessageDelayed(START, speed);
        }
    }

    /**
     * 重置进度
     */
    public void resetProgress() {
        drawing = true;
        progress_angle = 0f;
        //每次都清除
        handler.removeMessages(START);
        invalidate();
    }

    /**
     * 设置进度
     */
    public void setProgress(int progress) {
        if (Math.abs(progress) > 100) {
            progress %= 100;
        }
        if (progress_clockwise) {
            progress_angle = progress * 3.6f;
        } else {
            progress_angle = -progress * 3.6f;
        }
        invalidate();
    }

    /**
     * 设置周期
     *
     * @param period
     */
    public void setPeriod(long period) {
        this.period = period;
    }

    /**
     * 设置进度条底色
     *
     * @param colors    颜色数组
     * @param positions 颜色的范围
     */
    public void setBase_progress_colors(int colors[], float positions[]) {
        this.base_progress_colors = colors;
        this.base_progress_positions = positions;
    }

    /**
     * 设置进度条颜色
     *
     * @param colors    颜色数组
     * @param positions 颜色的范围
     */
    public void setProgress_colors(int colors[], float positions[]) {
        this.progress_colors = colors;
        this.progress_positions = positions;
    }

    /**
     * 设置进度条宽度
     *
     * @param width 宽度，单位px
     */
    public void setStrokeWidth(int width) {
        this.strokeWidth = width;
        paint_base_progress.setStrokeWidth(width);
        paint_progress.setStrokeWidth(width);
        invalidate();
    }

    /**
     * 设置半径，默认控件大小一半
     *
     * @param radius 半径，单位px
     */
    public void setRadius(int radius) {
        this.radius = radius;
        invalidate();
    }

    /**
     * 是否循环，默认不循环
     */
    public void setProgress_recycler(boolean progress_recycler) {
        this.progress_recycler = progress_recycler;
    }

    /**
     * 是否顺时针，默认顺时针
     */
    public void setProgress_clockwise(boolean progress_clockwise) {
        this.progress_clockwise = progress_clockwise;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);


        if (shader_base_progress == null) {
            shader_base_progress = new SweepGradient(width / 2, height / 2
                    , base_progress_colors, base_progress_positions);
            paint_base_progress.setShader(shader_base_progress);
        }

        if (shader_progress == null) {
            //控件中心为渐变色中心
            shader_progress = new SweepGradient(width / 2, height / 2
                    , progress_colors, progress_positions);
            paint_progress.setShader(shader_progress);
        }


        if (rectF == null) {
            //半径
            if (radius == 0) {
                radius = (width > height ? height : width) / 2;
            }
            rectF = new RectF(width / 2 - radius + strokeWidth / 2
                    , height / 2 - radius + strokeWidth / 2
                    , width / 2 + radius - strokeWidth / 2
                    , height / 2 + radius - strokeWidth / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //因为圆开始角度是x轴，所以给他旋转270°
        canvas.rotate(270f, width / 2, height / 2);
        if (drawing) {
            canvas.drawArc(rectF, 0f, base_angle, false, paint_base_progress);
            canvas.drawArc(rectF, 0f, progress_angle, false, paint_progress);
        } else {
            canvas.drawArc(rectF, 0f, base_angle, false, paint_base_progress);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        onDestroy();
        super.onDetachedFromWindow();
    }

    /**
     * 注销
     */
    public void onDestroy() {
        handler.removeMessages(START);
        handler.removeMessages(COMPLETE);
    }

    /**
     * 进度条状态
     */
    public interface OnProgressListener {
        //开始
        void onStart();

        //过程
        void onProgress(int progress_int, float progress_float);

        //完成一圈
        void onComplete();
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    private int dp2px(final float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int px2dp(final float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
