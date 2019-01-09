package com.example.jorge.hellojesus.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.login.LoginActivity;
import com.flaviofaria.kenburnsview.KenBurnsView;

public class SplashActivity extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 7000;
    KenBurnsView kbv;

    public ImageView icon_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        icon_main = (ImageView) findViewById(R.id.icon_main);

        kbv=(KenBurnsView) findViewById(R.id.fragmentloginKenBurnsView1);

        icon_main.animate().setStartDelay(3000).setDuration(2000).alpha(1).start();

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                try {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);

                    Pair<View, String> p1 = Pair.create((View) icon_main, "icon_main");

                    ActivityOptionsCompat options =

                            ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this,
                                    p1);
                    startActivity(i, options.toBundle());
                    // close this activity
                    finish();
                } catch (Exception e) {
                    finish();
                }







            }
        }, SPLASH_TIME_OUT);
    }
}
