# ProgressView
CircleProgressView 

自定义控件属性
<declare-styleable name="CircleProgressView">
    <!-- 底层进度条浅颜色 -->
    <attr name="base_progress_light_color" format="color" />
    <!-- 底层进度条深颜色 -->
    <attr name="base_progress_height_color" format="color" />
    <!-- 进度条浅颜色 -->
    <attr name="progress_light_color" format="color" />
    <!-- 进度条深颜色 -->
    <attr name="progress_height_color" format="color" />
    <!-- 进度条宽度 -->
    <attr name="progress_width" />
    <!-- 绘制半径 -->
    <attr name="progress_radius" format="dimension" />
    <!-- 顺时针转 -->
    <attr name="progress_clockwise" format="boolean" />
    <!-- 周期 -->
    <attr name="progress_period" format="integer" />
    <!-- 刷新间隔，最低应该是16.67ms，按1s 60帧来算 -->
    <attr name="progress_speed" format="integer" />
    <!-- 循环播放 -->
    <attr name="progress_recycler" format="boolean" />
</declare-styleable>


<!-- 背景颜色 -->
<attr name="background_color" format="color" />
<!-- 进度条背景颜色 -->
<attr name="progress_background_color" format="color" />
<!-- 进度条颜色 -->
<attr name="progress_color" format="color" />
<!-- 进度条宽度 -->
<attr name="progress_width" format="dimension" />
<!-- 最大进度 -->
<attr name="max_progress" format="integer" />
<!-- 进度 -->
<attr name="progress" format="integer" />
<!-- 分段数 -->
<attr name="arc_count" format="integer" />
<!-- 间隔角度 -->
<attr name="interval_angle" format="float" />
<!-- 等级 -->
<attr name="grade" format="integer"/>


xml设置属性
 <com.bao.circleprogressview.CircleProgressView
        android:id="@+id/circleProgressView"
        android:layout_width="200dp"
        android:layout_height="200dp"
        app:base_progress_height_color="@color/base_progress_height"
        app:base_progress_light_color="@color/base_progress_light"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:progress_clockwise="true"
        app:progress_height_color="@color/progress_height"
        app:progress_light_color="@color/progress_light"
        app:progress_period="3000"
        app:progress_radius="100dp"
        app:progress_recycler="true"
        app:progress_width="5dp" />

<com.bao.circleprogressview.GradeProgressView
    android:id="@+id/powerProgress"
    android:layout_width="70dp"
    android:layout_height="70dp"
    android:gravity="center"
    android:orientation="vertical"
    app:arc_count="6"
    app:interval_angle="20"
    app:layout_constraintBottom_toBottomOf="@id/circleProgressView3"
    app:layout_constraintLeft_toRightOf="@id/circleProgressView3">


    <TextView
        android:id="@+id/tvPower"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textColor="@color/colorPrimary"
        android:textSize="20sp" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="w"
        android:textColor="@color/colorAccent"
        android:textSize="12sp" />

</com.bao.circleprogressview.GradeProgressView>

<com.bao.circleprogressview.ProgressLinearLayout
    android:id="@+id/progressView"
    android:layout_width="70dp"
    android:layout_height="70dp"
    android:gravity="center"
    android:orientation="vertical"
    app:layout_constraintBottom_toBottomOf="@id/circleProgressView3"
    app:layout_constraintLeft_toRightOf="@id/powerProgress"
    app:progress_color="@color/colorAccent">

    <TextView
        android:id="@+id/tvDistance"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textColor="@color/colorPrimary"
        android:textSize="20sp" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="km"
        android:textColor="@color/colorAccent"
        android:textSize="12sp" />

</com.bao.circleprogressview.ProgressLinearLayout>
