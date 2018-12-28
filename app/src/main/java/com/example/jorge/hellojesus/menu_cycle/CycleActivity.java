package com.example.jorge.hellojesus.menu_cycle;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.jorge.hellojesus.Injection;
import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.data.local.WordsDataSource;
import com.example.jorge.hellojesus.data.local.WordsRepository;
import com.example.jorge.hellojesus.data.local.helloWord.HelloWord;
import com.example.jorge.hellojesus.helloWord.HelloWordActivity;
import com.example.jorge.hellojesus.helloWord.HelloWordContract;
import com.example.jorge.hellojesus.menuActivity.MenuWordActivity;
import com.example.jorge.hellojesus.util.CrunchifyJSONFileWrite;
import com.example.jorge.hellojesus.util.EspressoIdlingResource;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.jorge.hellojesus.word.WordFragment.EXTRA_BUNDLE_WORD;
import static com.example.jorge.hellojesus.word.WordFragment.EXTRA_WORD;

public class CycleActivity extends AppCompatActivity {

    public static String EXTRA_HELLO_WORD = "HELLO_WORD";
    public static String EXTRA_HELLO_WORD_TIP = "EXTRA_HELLO_WORD_TIP";
    public static String EXTRA_BUNDLE_HELLO_WORD = "BUNDLE_HELLO_WORD";

    private static WordsRepository mWordsRepository;
    private ImageView view01;
    private ImageView view02;
    private ImageView view03;
    private ImageView view04;
    private ImageView view05;
    private ImageView view06;
    private ImageView view07;
    private ImageView view08;
    private ImageView view09;
    private ImageView view10;
    private ImageView view11;
    private ImageView view12;
    private ImageView view13;
    private ImageView view14;
    private ImageView view15;
    private ImageView view16;
    private ImageView view17;
    private ImageView view18;
    private ImageView view19;
    private ImageView view20;
    private ImageView view21;
    private ImageView view22;
    private ImageView view23;
    private ImageView view24;
    private ImageView view25;


    private ImageView ivClose;

    public static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle);

        requestPermission();

        intent = null;

        mWordsRepository = Injection.provideWordsRepository(getApplicationContext());

          CrunchifyJSONFileWrite CrunchifyJSONFileWrite = new CrunchifyJSONFileWrite();
           try {
                CrunchifyJSONFileWrite.main(this,mWordsRepository);
           } catch (IOException e) {
                e.printStackTrace();
        }
        ivClose = (ImageView) findViewById(R.id.ic_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final int newColor = getResources().getColor(R.color.colorAccent);


        view01 = (ImageView) findViewById(R.id.bubble0_img);
        //view01.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view01.setTag("A");
        view01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });


        view02 = (ImageView) findViewById(R.id.bubble2);
        //view02.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view02.setTag("B");
        view02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });


        view03 = (ImageView) findViewById(R.id.bubble3);
        //view03.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view03.setTag("C");
        view03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });


        view04 = (ImageView) findViewById(R.id.bubble4);
        //view04.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view04.setTag("D");
        view04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });


        view05 = (ImageView) findViewById(R.id.bubble5);
        //view05.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view05.setTag("E");
        view05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });


        view06 = (ImageView) findViewById(R.id.bubble6);
        //view06.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view06.setTag("F");
        view06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });


        view07 = (ImageView) findViewById(R.id.bubble7);
        //view07.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view07.setTag("G");
        view07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });


        view08 = (ImageView) findViewById(R.id.bubble8);
        //view08.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view08.setTag("H");
        view08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });


        view09 = (ImageView) findViewById(R.id.bubble9);
        //view09.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view09.setTag("I");
        view09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });


        view10 = (ImageView) findViewById(R.id.bubble11);
        //view10.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view10.setTag("J");
        view10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });


        view11 = (ImageView) findViewById(R.id.bubble12);
        //view11.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view11.setTag("K");
        view11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });


        view12 = (ImageView) findViewById(R.id.bubble13);
        //view12.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view12.setTag("L");
        view12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });


        view13 = (ImageView) findViewById(R.id.bubble14);
        //view13.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view13.setTag("M");
        view13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });


        view14 = (ImageView) findViewById(R.id.bubble15);
        //view14.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view14.setTag("N");
        view14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });


        view15 = (ImageView) findViewById(R.id.bubble16);
        //view15.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view15.setTag("O");
        view15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });


        view16 = (ImageView) findViewById(R.id.bubble17);
        //view16.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view16.setTag("P");
        view16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });


        view17 = (ImageView) findViewById(R.id.bubble19);
        //view17.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view17.setTag("Q");
        view17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });


        view18 = (ImageView) findViewById(R.id.bubble20);
        //view18.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view18.setTag("R");
        view18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });


        view19 = (ImageView) findViewById(R.id.bubble21);
        //view19.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view19.setTag("S");
        view19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });

        view20 = (ImageView) findViewById(R.id.bubble22);
        //view20.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view20.setTag("T");
        view20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });

        view21 = (ImageView) findViewById(R.id.bubble23);
        //view20.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view21.setTag("U");
        view21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });

        view22 = (ImageView) findViewById(R.id.bubble24);
        //view20.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view22.setTag("V");
        view22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });

        view23 = (ImageView) findViewById(R.id.bubble25);
        //view20.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view23.setTag("X");
        view24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });

        view24 = (ImageView) findViewById(R.id.bubble26);
        //view20.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view24.setTag("Y");
        view24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });

        view25 = (ImageView) findViewById(R.id.bubble27);
        //view20.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        view25.setTag("Z");
        view25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(v.getTag().toString(),CycleActivity.this);
            }
        });




    }

    private void loadHelloWord(String tip1, Context context) {

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment();

        mWordsRepository.getHelloWord(new WordsDataSource.LoadHelloWordCallback() {

            @Override
            public void onHelloWordLoaded(List<HelloWord> helloWordList, String tip1, Context context) {


                    intent = new Intent(context, HelloWordActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(EXTRA_HELLO_WORD, (ArrayList<? extends Parcelable>) helloWordList);
                    bundle.putString(EXTRA_HELLO_WORD_TIP, tip1);
                    intent.putExtra(EXTRA_BUNDLE_HELLO_WORD, bundle);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
            }

        },tip1,context);
    }

    private void requestPermission(){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

    }
}
