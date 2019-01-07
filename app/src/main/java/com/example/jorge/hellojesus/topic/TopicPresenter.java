package com.example.jorge.hellojesus.topic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.jorge.hellojesus.data.local.Word;
import com.example.jorge.hellojesus.data.local.WordsDataSource;
import com.example.jorge.hellojesus.data.local.WordsRepository;
import com.example.jorge.hellojesus.data.local.control.Control;
import com.example.jorge.hellojesus.data.onLine.topic.TopicServiceApi;
import com.example.jorge.hellojesus.data.onLine.topic.model.Content;
import com.example.jorge.hellojesus.data.onLine.topic.model.ListTopic;
import com.example.jorge.hellojesus.data.onLine.topic.model.Topic;
import com.example.jorge.hellojesus.util.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.List;

import static com.example.jorge.hellojesus.util.KeyVar.KEY_SING;

/**
 * Created by jorge on 21/02/2018.
 * Presenter
 */

public class TopicPresenter  implements TopicContract.UserActionsListener {

    private final TopicServiceApi mTopicServiceApi;

    private final WordsRepository mWordsRepository;

    private final TopicContract.View mTopicContractView;

    private static String mNameTitle = "";

    public TopicPresenter(WordsRepository wordsRepository, @NonNull TopicServiceApi topicServiceApi, TopicContract.View topicContract_View) {
        this.mWordsRepository = wordsRepository;

        this.mTopicContractView = topicContract_View;
        this.mTopicServiceApi = topicServiceApi;

        mTopicContractView.setPresenter(this);
    }

    public TopicPresenter(@NonNull TopicServiceApi topicServiceApi, TopicContract.View topicContract_View) {
        mWordsRepository = null;
        this.mTopicContractView = topicContract_View;
        this.mTopicServiceApi = topicServiceApi;

    }


    @Override
    public void loadingTopic(final List<Integer> lisTopic) {
        mTopicServiceApi.getTopics(new TopicServiceApi.TopicServiceCallback<ListTopic<Topic>>(){
            @Override
            public void onLoaded(ListTopic listTopic) {



                List<Topic>  topicListBible = new ArrayList<>();
                List<Topic>  topicListMusic = new ArrayList<>();
                List<Topic>  topicListMusicSing = new ArrayList<>();
                List<Topic>  topicListQuestion = new ArrayList<>();
                List<Topic>  topicListExercise = new ArrayList<>();

                if (listTopic != null){
                    List<Topic>  topicListtemp = (List<Topic>) listTopic.items;
                    List<Topic>  topicList = new ArrayList<Topic>();

                    for (int i = 0; i < topicListtemp.size(); i++){
                        if (haveIdinThisTopic( topicListtemp.get(i).getId(), lisTopic)){
                            topicList.add(topicListtemp.get(i));
                        }
                    }



                    for (int i = 0 ; i < topicList.size(); i++){
                        if (topicList.get(i).getType().equals("bible")){
                            topicListBible.add(topicList.get(i));

                        }else if (topicList.get(i).getType().equals("music")){
                            if (topicList.get(i).getAudio().toString().equals(KEY_SING)) {
                                topicListMusicSing.add(topicList.get(i));
                            }else{
                                topicListMusic.add(topicList.get(i));
                            }

                        }else if (topicList.get(i).getType().equals("question")){
                            topicListQuestion.add(topicList.get(i));

                        }else if (topicList.get(i).getType().equals("exercise")){
                            topicListExercise.add(topicList.get(i));

                        }
                    }
                }

                mTopicContractView.showTopicBible(topicListBible);
                mTopicContractView.showTopicMusic(topicListMusic);
                mTopicContractView.showTopicMusicSing(topicListMusicSing);
                mTopicContractView.showTopicQuestion(topicListQuestion);
                mTopicContractView.showTopicExercise(topicListExercise);
            }
        });
    }

    private boolean haveIdinThisTopic(int id, List<Integer> lisTopic) {
        int i = 0;
        while (i < lisTopic.size()){
            if (id == lisTopic.get(i)){
                return true;
            }
            i++;
        }
        return false;


    }

    @Override
    public void saveControl(Control control) {

              mWordsRepository.saveControl(control);

    }

    @Override
    public String getNameTitle() {
        return this.mNameTitle;
    }


    private void loadWord() {

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment();

        mWordsRepository.getWords(new WordsDataSource.LoadWordCallback() {

            @Override
            public void onWordLoaded(List<Word> wordList) {
                List<Content> arrayList = new ArrayList<Content>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }

                // We filter the tasks based on the requestType
                int i = 0;
                for (Word word : wordList) {
                    Content content = new Content();
                    content.setContent_english(word.getWord());
                    content.setContent_portuguese(word.getWord());
                    content.setCorret_option(word.getType());
                    content.setId_content(1);
                    content.setTime(1);
                    arrayList.add(i,content);
                    i++;
                }

                // The view may not be able to handle UI updates anymore
                if (!mTopicContractView.isActive()) {
                    return;
                }
                //if (showLoadingUI) {
                if (true) {
                    mTopicContractView.setLoadingIndicator(false);
                }

                processWords(arrayList);
            }


            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mTopicContractView.isActive()) {
                    return;
                }
                mTopicContractView.showLoadingShoppingError();
            }

        });
    }



    @Override
    public void loadingWords(String type, String sourceName, String write) {
        loadWord();
    }

    @Override
    public void loadingWords(String type) {
        loadWord();
    }

    @Override
    public void openDetail() {

    }





    private void processWords(List<Content> wordList) {

        if (wordList.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            //processEmptyTasks();
        } else {
            // Show the list of tasks
            mTopicContractView.showWords(wordList);
            // Set the filter label's text.
           // showFilterLabel();
        }
    }


    @Override
    public void start() {

    }

    @Override
    public void loadHelp(View root, Context context) {

    }
}
