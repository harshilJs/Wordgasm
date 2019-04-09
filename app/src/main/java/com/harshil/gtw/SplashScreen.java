package com.harshil.gtw;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private Animation animation_1;
    private Animation animation_3;
    private ImageView sp_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //App Name with Typeface
        TextView sp_name = findViewById(R.id.sp_name);
        sp_name.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/MontserratMedium.ttf"));

        sp_logo = findViewById(R.id.sp_logo);

        animation_1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.antirotate);
        animation_3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_out);

        sp_logo.startAnimation(animation_2);

        animation_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sp_logo.startAnimation(animation_1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation_1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sp_logo.startAnimation(animation_3);
                finish();
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
