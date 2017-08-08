package com.av.videoplayer;

import com.bumptech.glide.Glide;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeTransform;
import android.transition.Transition;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class PictureActivity extends AppCompatActivity {

    public static final String PICTURE_NAME = "picture_name";

    public static final String PICTURE_ID = "picture_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        Intent intent = getIntent();
        String pictureNmae = intent.getStringExtra(PICTURE_NAME);
        int pictureId = intent.getIntExtra(PICTURE_ID, 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView pictureImageView = (ImageView) findViewById(R.id.image_view);
        pictureImageView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                // set share element transition animation for current activity
                Transition ts = new ChangeTransform();
                ts.setDuration(3000);
                getWindow().setExitTransition(ts);
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(PictureActivity.this,
                        Pair.create(findViewById(R.id.floatingbtn), "floatingbtn")).toBundle();

                // start activity with share element transition
                Intent intent = new Intent(PictureActivity.this,AnimationTestActivity.class);
                startActivity(intent, bundle);
            }
        });

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(pictureNmae);
        Glide.with(this).load(pictureId).into(pictureImageView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
