package com.example.jorge.hellojesus.progress;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.jorge.hellojesus.data.local.Word;
import com.example.jorge.hellojesus.data.local.WordsRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 11/04/2018.
 * Presenter for implements Contract Progress
 */

public class ProgressPresenter implements ProgressContract.UserActionsListener {

    private final WordsRepository mWordsRepository;
    private final ProgressContract.View mWordsView;

    public ProgressPresenter(@NonNull WordsRepository wordsRepository, @NonNull ProgressContract.View ProgressView) {
        mWordsRepository = checkNotNull(wordsRepository, "tasksRepository cannot be null");
        mWordsView = checkNotNull(ProgressView, "tasksView cannot be null!");

        mWordsView.setPresenter(this);
    }

    @Override
    public void saveWord(List<String> stringList, String type,  String sourceName, String countTime,  String statusSaid, String statusWrite) {

        for (int i = 0 ; i < stringList.size(); i++){
            Word word = new Word(stringList.get(i).toString(), type,  sourceName, countTime,  statusSaid, statusWrite);
            mWordsRepository.saveWord(word);
        }


    }

    @Override
    public void start() {

    }

    @Override
    public void loadHelp(View root, Context context) {

    }
}
