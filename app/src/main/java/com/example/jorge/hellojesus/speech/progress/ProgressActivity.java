package com.example.jorge.hellojesus.speech.progress;


import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.jorge.hellojesus.R;
import static com.example.jorge.hellojesus.speech.SpeechFragment.EXTRA_TOTAL;
import static com.example.jorge.hellojesus.speech.SpeechFragment.EXTRA_TOTAL_CORRECT;
import static com.example.jorge.hellojesus.speech.SpeechFragment.EXTRA_TOTAL_MISSING;
import static com.example.jorge.hellojesus.speech.SpeechFragment.EXTRA_TOTAL_MISTAKE;
import static com.example.jorge.hellojesus.speech.SpeechFragment.EXTRA_TOTAL_SAID;


public class ProgressActivity extends AppCompatActivity {

    private static ProgressBar mProgressBar;
    private static CountDownTimer mCountDownTimer;

    private LinearLayout mLinearLayoutResult;
    private TextView mCountSpeech;
    private TextView mCountMissing;
    private TextView mCountCorrect;
    private TextView mCountSaid;
    private TextView mCountMistake;

    private  static int i=0;

    private  static int j=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        mLinearLayoutResult = (LinearLayout) findViewById(R.id.ll_result);
        mCountSpeech = (TextView) findViewById(R.id.tv_count_speech);
        mCountMissing = (TextView) findViewById(R.id.tv_count_missing);
        mCountCorrect = (TextView) findViewById(R.id.tv_count_correct);
        mCountSaid = (TextView) findViewById(R.id.tv_count_said);
        mCountMistake = (TextView) findViewById(R.id.tv_count_mistake);

        final String total = getIntent().getStringExtra(EXTRA_TOTAL);
        String totalMissing = getIntent().getStringExtra(EXTRA_TOTAL_MISSING);
        String totalSaidCorrect = getIntent().getStringExtra(EXTRA_TOTAL_CORRECT);
        String totalSaid = getIntent().getStringExtra(EXTRA_TOTAL_SAID);
        String totalMistake = getIntent().getStringExtra(EXTRA_TOTAL_MISTAKE);

        mCountSpeech = (TextView) findViewById(R.id.tv_count_speech);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mProgressBar.setProgress(i);
        mCountDownTimer = new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);
                i++;
                j--;
                mProgressBar.setProgress((int) i * 100 / (5000 / 1000));
                mCountSpeech.setText(Integer.toString(j));
            }

            @Override
            public void onFinish() {
                //Do what you want
                i++;
                mProgressBar.setProgress(100);
                mProgressBar.setVisibility(View.GONE);
                mCountSpeech.setBackground(getDrawable(R.drawable.circle_text_view_border));
                mLinearLayoutResult.setVisibility(View.VISIBLE);
                mCountSpeech.setText(total);

            }
        };
        mCountDownTimer.start();
        putValueInLabel(totalMissing, totalSaidCorrect, totalSaid, totalMistake);
    }


  private void putValueInLabel(String countMissing, String countCorrect, String countSaid, String countMistake ){

      mCountMissing.setText(countMissing);
      mCountCorrect.setText(countCorrect);
      mCountSaid.setText(countSaid);
      mCountMistake.setText(countMistake);
  }

}
