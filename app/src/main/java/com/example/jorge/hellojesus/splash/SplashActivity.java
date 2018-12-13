package com.example.jorge.hellojesus.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.login.LoginActivity;
import com.example.jorge.hellojesus.main.MainActivity;
import com.example.jorge.hellojesus.topic.TopicActivity;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

import java.io.Serializable;
import java.util.List;

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

        //Bitmap bMap = BitmapFactory.decodeFile(MyConstant.storageCliente+"cliente.png");
        //sspbr.setImageBitmap(bMap);

        kbv=(KenBurnsView) findViewById(R.id.fragmentloginKenBurnsView1);
      //  RandomTransitionGenerator generator = new RandomTransitionGenerator(7000, new AccelerateDecelerateInterpolator());


    //    kbv.setScrollBarFadeDuration(7000);
      //  kbv.set

        icon_main.animate().setStartDelay(3000).setDuration(2000).alpha(1).start();



/*        YoYo.with(Techniques.RotateOut)

                .duration(2000)
                .delay(4000)
                .playOn(sspbr);*/


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);

                Pair<View, String> p1 = Pair.create((View) icon_main, "icon_main");

                ActivityOptionsCompat options =

                        ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this,
                                p1);
                startActivity(i,options.toBundle());
                //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

                // Intent i2 = new Intent(SplashActivity.this, login.class);
                // startActivity(i2);

                // close this activity
                finish();







            }
        }, SPLASH_TIME_OUT);
    }
}
