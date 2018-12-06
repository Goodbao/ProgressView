package com.bao.circleprogressview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;

public class ProgressLinearLayout extends LinearLayout {
    //控件宽
    private int width;
    //控件高
    private int height;
    //背景画笔
    private Paint backgroundPaint;
    //背景颜色
    private int backgroundColor = Color.parseColor("#50FFFFFF");
    //进度条背景画笔
    private Paint progressBackgroundPaint;
    //进度条背景颜色
    private int progressBackgroundColor = Color.parseColor("#E7EAEE");
    //进度条画笔
    private Paint progressPaint;
    //进度条颜色
    private int progressColor = Color.parseColor("#7DB2F8");
    //进度条宽度
    private float progressWidth = dp2px(4);
    //绘制区域
    private RectF rectF;
    //进度条半径
    private int radius;

    //最大进度
    private int maxProgress = 100;
    //当前进度
    private int progress;

    public ProgressLinearLayout(Context context) {
        this(context, null);
    }

    public ProgressLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //ViewGroup要这样才onDraw
        setWillNotDraw(false);
        //获取自定义属性
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ProgressLinearLayout, defStyleAttr, 0);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            if (R.styleable.ProgressLinearLayout_background_color == attr) {
                backgroundColor = typedArray.getColor(attr, backgroundColor);
            } else if (R.styleable.ProgressLinearLayout_progress_background_color == attr) {
                progressBackgroundColor = typedArray.getColor(attr, progressBackgroundColor);
            } else if (R.styleable.ProgressLinearLayout_progress_color == attr) {
                progressColor = typedArray.getColor(attr, progressColor);
            } else if (R.styleable.ProgressLinearLayout_progress_width == attr) {
                progressWidth = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
            } else if (R.styleable.ProgressLinearLayout_max_progress == attr) {
                maxProgress = typedArray.getInteger(attr, maxProgress);
            } else if (R.styleable.ProgressLinearLayout_progress == attr) {
                progress = typedArray.getInteger(attr, progress);
            }
        }
        //用完回收
        typedArray.recycle();
        initPaint();
    }

    private void initPaint() {
        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStrokeWidth(progressWidth);

        progressBackgroundPaint = new Paint();
        progressBackgroundPaint.setStrokeWidth(progressWidth);
        progressBackgroundPaint.setColor(progressBackgroundColor);
        progressBackgroundPaint.setStyle(Paint.Style.STROKE);
        //圆润画笔
        progressBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        //抗锯齿
        progressBackgroundPaint.setAntiAlias(true);

        progressPaint = new Paint();
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.STROKE);
        //圆润画笔
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        //抗锯齿
        progressPaint.setAntiAlias(true);
    }

    private int dp2px(final float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int px2dp(final float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        if (null == rectF) {
            //控件范围
            radius = (width > height ? height : width) / 2;
            rectF = new RectF(width / 2 - radius + progressWidth / 2,
                    height / 2 - radius + progressWidth / 2,
                    width / 2 + radius - progressWidth / 2,
                    height / 2 + radius - progressWidth / 2);
        }
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(width / 2, height / 2, radius, backgroundPaint);
        canvas.drawArc(rectF, 0, 360, false, progressBackgroundPaint);
        canvas.drawArc(rectF, -90, (float) progress / maxProgress * 360f, false, progressPaint);
    }

    public void setProgress(int porgress) {
        this.progress = porgress;
        invalidate();
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        invalidate();
    }

    /**
     * 设置进度条宽度
     *
     * @param width
     */
    public void setProgressWidth(int width) {
        this.progressWidth = dp2px(width);
        invalidate();
    }

    /**
     * 设置进度条背景颜色
     */
    public void setProgressBackgroundColor(int color) {
        this.progressBackgroundColor = color;
        invalidate();
    }

    /**
     * 设置背景颜色
     */
    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
        invalidate();
    }
}
