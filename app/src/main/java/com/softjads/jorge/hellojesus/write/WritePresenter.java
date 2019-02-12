package com.softjads.jorge.hellojesus.write;



import com.softjads.jorge.hellojesus.data.local.WordsRepository;
import com.softjads.jorge.hellojesus.data.local.control.Control;
import com.softjads.jorge.hellojesus.data.onLine.topic.model.Content;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.List;



/**
 * Created by jorge on 06/04/2018.
 * Presenter implement contract Write
 */

public class WritePresenter implements WriteContract.UserActionsListener {


    private final WriteContract.View mWriteContractView;
    private final WordsRepository mWordsRepository;


    public WritePresenter(WriteContract.View writeContract_View, WordsRepository wordsRepository, String salveStatus, String nName) {
        this.mWordsRepository = wordsRepository;

        Control control = new Control(nName,"0","0","0","0");
        mWordsRepository.saveControl(control);
        mWordsRepository.updateControlStatus3(control,salveStatus);

        this.mWriteContractView = writeContract_View;
    }

    @Override
    public void loadingContent(List<Content> contents, int mTimeLast) {

        long[] listTime = new long[contents.size()];

        int second, minute, sum = 0;
        int secondNext, minuteNext, sumNext = 0;

        for (int i = 0; i < contents.size(); i++) {


            if (i < (contents.size() - 1)) {

                if (contents.get(i + 1).getTime() > 59) {
                    secondNext = Integer.parseInt(Integer.toString(contents.get(i + 1).getTime()).substring(1));
                    minuteNext = 60 * Integer.parseInt(Integer.toString(contents.get(i + 1).getTime()).substring(0, 1));
                    sumNext = 1000 * (minuteNext + secondNext);
                } else {
                    secondNext = 1000 * (contents.get(i + 1).getTime());
                    minuteNext = 0;
                    sumNext = minuteNext + secondNext;
                }

                if (contents.get(i).getTime() > 59) {
                    second = Integer.parseInt(Integer.toString(contents.get(i).getTime()).substring(1));
                    minute = 60 * Integer.parseInt(Integer.toString(contents.get(i).getTime()).substring(0, 1));
                    sum = 1000 * (minute + second);
                } else {
                    second = 1000 * (contents.get(i).getTime());
                    minute = 0;
                    sum = minute + second;
                }


                listTime[i] = sumNext - sum;
            } else {

                if (mTimeLast > 59) {
                    secondNext = Integer.parseInt(Integer.toString(mTimeLast).substring(1));
                    minuteNext = 60 * Integer.parseInt(Integer.toString(mTimeLast).substring(0, 1));
                    sumNext = 1000 * (minuteNext + secondNext);
                } else {
                    secondNext = 1000 * (mTimeLast);
                    minuteNext = 0;
                    sumNext = minuteNext + secondNext;
                }


                if (contents.get(i).getTime() > 59) {
                    second = Integer.parseInt(Integer.toString(contents.get(i).getTime()).substring(1));
                    minute = 60 * Integer.parseInt(Integer.toString(contents.get(i).getTime()).substring(0, 1));
                    sum = 1000 * (minute + second);
                } else {
                    second = 1000 * (contents.get(i).getTime());
                    minute = 0;
                    sum = minute + second;
                }
                if (sumNext > sum) {
                    listTime[i] = sumNext - sum;
                }
            }


        }

        mWriteContractView.setListTime(listTime);

        mWriteContractView.showContent(contents);


    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void playAudio(SimpleExoPlayer ExoPlayerAudio) {

            ExoPlayerAudio.setPlayWhenReady(true);

    }

    @Override
    public void pauseAudio(SimpleExoPlayer ExoPlayerAudio) {

            ExoPlayerAudio.setPlayWhenReady(false);


    }










}
