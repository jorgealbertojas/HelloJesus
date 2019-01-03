package com.example.jorge.hellojesus.util.about;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.util.Common;

public class AboutActivity extends AppCompatActivity {

    private TextView textView;
    private TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        textView = findViewById(R.id.textView);
        textView1 = findViewById(R.id.textView1);


        final int newColorRed = getResources().getColor(R.color.colorPrimaryDark);
        textView.setBackgroundColor(Common.getColorWithAlpha(newColorRed, 0.6f));
        textView1.setBackgroundColor(Common.getColorWithAlpha(newColorRed, 0.6f));
    }
}
