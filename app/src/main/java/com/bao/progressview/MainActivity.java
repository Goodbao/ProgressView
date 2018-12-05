package com.bao.progressview;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bao.circleprogressview.CircleProgressView;
import com.bao.circleprogressview.GradeProgressView;
import com.bao.circleprogressview.ProgressLinearLayout;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private CircleProgressView circleProgressView;
    private CircleProgressView circleProgressView2;
    private EditText et_progress;
    private GradeProgressView gradeProgressView;
    private TextView tvPower;
    private ProgressLinearLayout progressLinearLayout;
    private TextView tvDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogUtils.getConfig().setGlobalTag("progress");

        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_pause).setOnClickListener(this);
        findViewById(R.id.btn_continue).setOnClickListener(this);
        findViewById(R.id.btn_reset).setOnClickListener(this);

        circleProgressView = findViewById(R.id.circleProgressView);
        gradeProgressView = findViewById(R.id.powerProgress);
        gradeProgressView = findViewById(R.id.powerProgress);
        tvPower = findViewById(R.id.tvPower);

        progressLinearLayout = findViewById(R.id.progressView);
        //设置最大进度
        progressLinearLayout.setMaxProgress(100);
        tvDistance = findViewById(R.id.tvDistance);

        //进度条状态
        circleProgressView.setOnProgressListener(new CircleProgressView.OnProgressListener() {
            @Override
            public void onStart() {
                LogUtils.e("progress onStart");
            }

            @Override
            public void onProgress(int progress_int, float progress_float) {
                LogUtils.d("progress_int:" + progress_int + " + progress_float:" + progress_float);

                if (progress_int >= 0 && progress_int < 20) {
                    gradeProgressView.setGrade(1);
                } else if (progress_int >= 20 && progress_int < 35) {
                    gradeProgressView.setGrade(2);
                } else if (progress_int >= 35 && progress_int < 50) {
                    gradeProgressView.setGrade(3);
                } else if (progress_int >= 50 && progress_int < 70) {
                    gradeProgressView.setGrade(4);
                } else if (progress_int >= 70 && progress_int < 85) {
                    gradeProgressView.setGrade(5);
                } else if (progress_int >= 85) {
                    gradeProgressView.setGrade(6);
                }
                tvPower.setText(String.valueOf(progress_int));

                progressLinearLayout.setProgress(progress_int);
                tvDistance.setText(String.valueOf(progress_int));
            }

            @Override
            public void onComplete() {
                LogUtils.e("progress onComplete");
            }
        });

        et_progress = findViewById(R.id.et_progress);
        et_progress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    //下载进度条之类的可以用用
                    circleProgressView.setProgress(Integer.valueOf(s.toString()));
                }
            }
        });


        circleProgressView2 = findViewById(R.id.circleProgressView2);
        //设置半径
        circleProgressView2.setRadius(ConvertUtils.dp2px(55));
        //设置进度条宽度
        circleProgressView2.setStrokeWidth(ConvertUtils.dp2px(4));
        //逆时针
        circleProgressView2.setProgress_clockwise(false);
        //设置底色
        int base_colors[] = {Color.WHITE, Color.BLACK, Color.WHITE};
        float base_position[] = {0.2f, 0.9f, 1f};
        circleProgressView2.setBase_progress_colors(base_colors, base_position);
        //设置进度条颜色
        final int colors[] = {ContextCompat.getColor(this, R.color.progress_1)
                , ContextCompat.getColor(this, R.color.progress_2)
                , ContextCompat.getColor(this, R.color.progress_3)
                , ContextCompat.getColor(this, R.color.progress_4)};
        //这个参数就是颜色的分配，第一种颜色到0.25的位置，第二种到0.5的位置，第三种到0.75的位置，第四种1，不要超过1。
        float position[] = {0.25f, 0.5f, 0.75f, 1f};
        circleProgressView2.setProgress_colors(colors, position);
        //周期5s
        circleProgressView2.setPeriod(5000);
        //不循环
        circleProgressView2.setProgress_recycler(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                circleProgressView.startProgress();
                circleProgressView2.startProgress();
                break;
            case R.id.btn_pause:
                circleProgressView.pauseProgress();
                circleProgressView2.pauseProgress();
                break;
            case R.id.btn_continue:
                circleProgressView.continueProgress();
                circleProgressView2.continueProgress();
                break;
            case R.id.btn_reset:
                circleProgressView.resetProgress();
                circleProgressView2.resetProgress();
                break;
        }
    }
}
