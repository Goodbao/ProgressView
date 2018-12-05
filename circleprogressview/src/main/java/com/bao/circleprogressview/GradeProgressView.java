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

import java.util.ArrayList;
import java.util.List;

/**
 * 等级进度条
 */
public class GradeProgressView extends LinearLayout {
    //控件宽
    private int width;
    //控件高
    private int height;
    //背景画笔
    private Paint backgroundPaint;
    //背景颜色
    private int backgroundColor = Color.parseColor("#50FFFFFF");
    //进度条画笔
    private Paint progressPaint;
    //进度条宽度
    private float progressWidth = dp2px(4);
    //进度条背景色
    private int progressBackgroundColor = Color.parseColor("#E7EAEE");
    //多段颜色
    private List<Integer> colorList = new ArrayList();
    //绘制区域
    private RectF rectF;
    //进度条半径
    private int radius;
    //间隔角度
    private float intervalAngle = 10;
    //弧度段
    private int arcCount = 6;
    //第几等级
    private int grade = 0;

    public GradeProgressView(Context context) {
        this(context, null);
    }

    public GradeProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradeProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //ViewGroup要这样才onDraw
        setWillNotDraw(false);
        //获取自定义属性
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GradeProgressView, defStyleAttr, 0);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            if (R.styleable.GradeProgressView_background_color == attr) {
                backgroundColor = typedArray.getColor(attr, backgroundColor);
            } else if (R.styleable.GradeProgressView_progress_background_color == attr) {
                progressBackgroundColor = typedArray.getColor(attr, progressBackgroundColor);
            } else if (R.styleable.GradeProgressView_progress_width == attr) {
                progressWidth = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
            } else if (R.styleable.GradeProgressView_arc_count == attr) {
                arcCount = typedArray.getInteger(attr, 6);
            } else if (R.styleable.GradeProgressView_interval_angle == attr) {
                intervalAngle = typedArray.getFloat(attr, 10f);
            } else if (R.styleable.GradeProgressView_grade == attr) {
                grade = typedArray.getInteger(attr, 0);
            }
        }
        //用完回收
        typedArray.recycle();
        initColorList();
        initPaint();
    }

    private void initColorList() {
        //默认6段，6种颜色
        colorList.add(Color.parseColor("#CFCFCF"));
        colorList.add(Color.parseColor("#7DB2F8"));
        colorList.add(Color.parseColor("#4DD874"));
        colorList.add(Color.parseColor("#FFBE5F"));
        colorList.add(Color.parseColor("#FF766F"));
        colorList.add(Color.parseColor("#D26969"));
    }

    private void initPaint() {
        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStrokeWidth(progressWidth);

        progressPaint = new Paint();
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(progressBackgroundColor);
        progressPaint.setStyle(Paint.Style.STROKE);
        //圆润画笔
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        //抗锯齿
        progressPaint.setAntiAlias(true);
    }

    /**
     * 设置颜色
     *
     * @param colorList
     */
    public void setColorList(List<Integer> colorList) {
        this.colorList = colorList;
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

    /**
     * 设置间隔角度
     */
    public void setIntervalAngle(int angle) {
        this.intervalAngle = angle;
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
        canvas.drawCircle(width / 2, height / 2, radius, backgroundPaint);
        for (int i = 0; i < arcCount; i++) {
            if (i == grade - 1) {
                progressPaint.setColor(colorList.get((grade - 1) % colorList.size()));
            } else {
                progressPaint.setColor(progressBackgroundColor);
            }
            canvas.drawArc(rectF
                    , 90 - (360 / arcCount - intervalAngle) / 2 + i * (360 / arcCount)
                    , 360 / arcCount - intervalAngle
                    , false, progressPaint);
        }

        super.onDraw(canvas);
    }

    public void setGrade(int grade) {
        this.grade = grade;
        invalidate();
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