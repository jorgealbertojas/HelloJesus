package com.softjads.jorge.hellojesus.progress;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softjads.jorge.hellojesus.Injection;
import com.softjads.jorge.hellojesus.R;
import com.softjads.jorge.hellojesus.data.onLine.topic.model.Content;
import com.softjads.jorge.hellojesus.util.Common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.softjads.jorge.hellojesus.speech.SpeechFragment.EXTRA_ARRAY_LIST_STRING;
import static com.softjads.jorge.hellojesus.speech.SpeechFragment.EXTRA_LIST_CONTENT;
import static com.softjads.jorge.hellojesus.write.WriteFragment.EXTRA_SOURCE_NAME;
import static com.softjads.jorge.hellojesus.write.WriteFragment.EXTRA_TYPE;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 06/04/2018.
 * Progress Activity for support all app with
 * Algorithm for count total word, mistake, missing, correct and word said
 */

public class ProgressActivity extends AppCompatActivity implements ProgressContract.View {

    private static ProgressContract.UserActionsListener mActionsListener;



    private static CountDownTimer mCountDownTimer;

    private LinearLayout mLinearLayoutResult;
    private TextView mCountSpeech;
    private TextView mCountMissing;
    private TextView mCountCorrect;
    private TextView mCountSaid;
    private TextView mCountMistake;

    private static List<String> mListCorrect;
    private static List<String> mListListen;

    private static List<Content> mListContent;
    private static ArrayList<String> mListArrayString;

    private static String mSourceName;
    private static String mType;

    private static int mTotal;

    public ImageView ic_close;

    private static String mTotalMissing, mTotalSaidCorrect, mTotalSaid, mTotalMistake;

    private static String CONST_TIME = "1";
    private static String CONST_SAID = "0";
    private static String CONST_WRITE = "0";

    private static CardView cardViewProgress;

    private  static int i=0;

    private  static int j=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

       i=0;

        j=5;

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        mListContent = (List<Content>) bundle.getSerializable(EXTRA_LIST_CONTENT);
        mListArrayString = (ArrayList<String>) bundle.getStringArrayList(EXTRA_ARRAY_LIST_STRING);

        mType = bundle.getString(EXTRA_TYPE);
        mSourceName = bundle.getString(EXTRA_SOURCE_NAME);

        mActionsListener = new ProgressPresenter(
                Injection.provideWordsRepository(getApplicationContext()), this);


        intCount(mListContent, mListArrayString);

        ic_close = (ImageView) findViewById(R.id.ic_close);
        ic_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        cardViewProgress = (CardView) findViewById(R.id.cardViewProgress);



        mLinearLayoutResult = (LinearLayout) findViewById(R.id.ll_result);
        mCountSpeech = (TextView) findViewById(R.id.tv_count_speech);
        mCountMissing = (TextView) findViewById(R.id.tv_count_missing);
        mCountCorrect = (TextView) findViewById(R.id.tv_count_correct);
        mCountSaid = (TextView) findViewById(R.id.tv_count_said);
        mCountMistake = (TextView) findViewById(R.id.tv_count_mistake);
        mCountSpeech = (TextView) findViewById(R.id.tv_count_speech);

        final int newColorBlue = getResources().getColor(R.color.colorPrimary);

        cardViewProgress.setBackgroundColor(Common.getColorWithAlpha(newColorBlue, 0.6f));

       // mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
       // mProgressBar.setProgress(i);
        mCountDownTimer = new CountDownTimer(3000, 20) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);

                j--;
               // mProgressBar.setProgress((int) i * 100 / (5000 / 1000));
                if (i <=  (mTotal)) {
                    mCountSpeech.setText(Integer.toString(i));
                }
                putValueInLabel(mTotalMissing, mTotalSaidCorrect, mTotalSaid, mTotalMistake, i);
                i++;
            }

            @Override
            public void onFinish() {
                //Do what you want
                i++;
               // mProgressBar.setProgress(100);
              //  mProgressBar.setVisibility(View.INVISIBLE);
                //mCountSpeech.setBackground(getDrawable(R.drawable.circle_text_view_border));
                mLinearLayoutResult.setVisibility(View.VISIBLE);
                mCountSpeech.setText(Integer.toString(mTotal));

                mCountMissing.setText(mTotalMissing);
                mCountCorrect.setText(mTotalSaidCorrect);
                mCountSaid.setText(mTotalSaid);
                mCountMistake.setText(mTotalMistake);


            }
        };
        mCountDownTimer.start();

    }


    private void intCount(List<Content> listContent, ArrayList<String> stringArrayList ){
        mListCorrect = verifyTheWord(changeForAdapter(listContent));

        mListListen = verifyTheWord(changeForAdapter(stringArrayList));

        mTotal = mListCorrect.size();
        mTotalSaid = Integer.toString(mListListen.size());

        List<String> result = new ArrayList<>();
        result = countPoint(mListCorrect,mListListen,0);

        mActionsListener.saveWord(result,mType,mSourceName,CONST_TIME, CONST_SAID, CONST_WRITE);

        mTotalSaidCorrect = Integer.toString(mTotal - result.size());
        mTotalMistake = Integer.toString(Integer.parseInt(mTotalSaid) - Integer.parseInt(mTotalSaidCorrect));
        mTotalMissing = Integer.toString((mTotal) - Integer.parseInt(mTotalSaidCorrect));
    }


    private void putValueInLabel(String countMissing, String countCorrect, String countSaid, String countMistake, Integer interval){

        if (interval <=  Integer.parseInt(countMissing)) {
            mCountMissing.setText(Integer.toString(interval));
        }
        if (interval <=  Integer.parseInt(countCorrect)) {
            mCountCorrect.setText(Integer.toString(interval));
        }
        if (interval <=  Integer.parseInt(countSaid)) {
            mCountSaid.setText(Integer.toString(interval));
        }
        if (interval <=  Integer.parseInt(countMistake)) {
            mCountMistake.setText(Integer.toString(interval));
        }
    }

    private static List<String> verifyTheWord(List<String> eeee) {

        List<String> listString = new ArrayList<String>();

        for (int i = 0; i < eeee.size(); i++) {
            listString.addAll(getWordInPhase(eeee.get(i)))
            ;
        }

        Collections.sort(listString);
        listString = EliminateDuplicate(listString);


        return listString;
    }


    private static List<String> getWordInPhase(String phrase) {
        List<String> mListResult = new ArrayList<String>();
        int index = 0;

        while (phrase.length() > 0) {


            index = phrase.toString().indexOf(" ");

            if ((index > 0)) {

                mListResult.add(phrase.substring(0, index).toUpperCase());
                phrase = phrase.toString().substring(index + 1,phrase.length());

            }else{
                mListResult.add(phrase.toString().toUpperCase());
                phrase = "";
            }
        }
        return mListResult;
    }

    private static  List<String> EliminateDuplicate(List<String> listString){
        int i = 0;
        while ( i < (listString.size() - 1)){
            if (listString.get(i).toString().equals(listString.get(i+1).toString())){
                listString.remove(i+1);
                i--;
            }
            i++;
        }

        return listString;
    }

    private static  List<String> countPoint(List<String> correctList, List<String> myList, int i) {

        if (correctList.size() == i) {
            return correctList;
        }
        else if (myList.size() == 0) {
            i ++;
            myList = mListListen;
            return countPoint(correctList, myList,i);
        } else if (correctList.get(i).toString().equals(myList.get(0).toString())) {
            correctList.remove(i);
            myList.remove(0);
            return countPoint(correctList, myList,i);
        } else if (myList.size() > 0) {
            return countPoint(correctList, myList.subList(1, myList.size()),i);
        } else {
            return countPoint(correctList, myList, i);

        }

    }

    public List<String> changeForAdapter(List<Content> contentList){
        List<String> stringList = new ArrayList<>();
        if (contentList != null) {
            for (int i = 0; i < contentList.size(); i++) {
                stringList.add(contentList.get(i).getContent_english());
            }
        }
        return stringList;
    }

    public List<String> changeForAdapter(ArrayList<String> stringArrayList){
        List<String> stringList = new ArrayList<>();
        if (stringArrayList != null) {
            for (int i = 0; i < stringArrayList.size(); i++){
                stringList.add(stringArrayList.get(i).toString());
            }
        }

        return stringList;
    }

    @Override
    public void setPresenter(ProgressContract.UserActionsListener presenter) {
        mActionsListener = checkNotNull(presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.start();

    }
}
