package com.av.videoplayer;

import android.animation.Animator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

public class AnimationTestActivity extends AppCompatActivity {
    Button mTest3Btn;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(new Explode().setDuration(1000));
        setContentView(R.layout.activity_animation_test);
        mTest3Btn = (Button) findViewById(R.id.test3);
        mTest3Btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                //揭开动画
                Animator animator = ViewAnimationUtils.createCircularReveal(
                        mTest3Btn,
                        0,
                        0,
                        0,
                        (float) Math.hypot(mTest3Btn.getWidth(), mTest3Btn.getHeight()));
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(2000);
                animator.start();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAfterTransition();
    }
}
