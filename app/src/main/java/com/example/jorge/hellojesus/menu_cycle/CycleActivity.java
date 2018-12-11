package com.example.jorge.hellojesus.menu_cycle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
    public static String EXTRA_BUNDLE_HELLO_WORD = "BUNDLE_HELLO_WORD";

    private static WordsRepository mWordsRepository;
    private View view01;
    private View view19;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle);

        requestPermission();

        mWordsRepository = Injection.provideWordsRepository(getApplicationContext());

          CrunchifyJSONFileWrite CrunchifyJSONFileWrite = new CrunchifyJSONFileWrite();
           try {
                CrunchifyJSONFileWrite.main(this,mWordsRepository);
           } catch (IOException e) {
                e.printStackTrace();
        }

        view01 = (View) findViewById(R.id.bubble0_shape);
        view01.setTag("COLOR");
        view01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(view01.getTag().toString(),CycleActivity.this);
            }
        });

        view19 = (View) findViewById(R.id.bubble19);
        view19.setTag("FAMILY");
        view19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHelloWord(view19.getTag().toString(),CycleActivity.this);
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

                Intent intent = new Intent(CycleActivity.this, HelloWordActivity.class);

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(EXTRA_HELLO_WORD, (ArrayList<? extends Parcelable>) helloWordList);
                intent.putExtra(EXTRA_BUNDLE_HELLO_WORD, bundle);
                startActivity(intent);
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
