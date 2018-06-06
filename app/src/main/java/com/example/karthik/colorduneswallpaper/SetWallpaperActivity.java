package com.example.karthik.colorduneswallpaper;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class SetWallpaperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_wallpaper);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"Raleway-Light.ttf");

        final Button button = findViewById(R.id.button);
        button.setTypeface(typeface);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,new ComponentName(SetWallpaperActivity.this,LiveTimeWallpaperService.class));
                startActivity(intent);
            }
        });

        TextView textView = findViewById(R.id.textView);

        textView.setTypeface(typeface);

        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);
        final Animation fade_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        textView.setVisibility(View.VISIBLE);
        textView.startAnimation(slide_down);

        slide_down.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                button.setVisibility(View.VISIBLE);
                button.startAnimation(fade_in);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });




    }

}
