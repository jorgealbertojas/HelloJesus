package com.softjads.jorge.hellojesus.topic.fragmentTab;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.softjads.jorge.hellojesus.Injection;
import com.softjads.jorge.hellojesus.R;
import com.softjads.jorge.hellojesus.data.local.Word;
import com.softjads.jorge.hellojesus.data.onLine.topic.TopicServiceImpl;
import com.softjads.jorge.hellojesus.data.onLine.topic.model.Content;
import com.softjads.jorge.hellojesus.data.onLine.topic.model.Topic;
import com.softjads.jorge.hellojesus.topic.TopicActivity;
import com.softjads.jorge.hellojesus.topic.TopicContract;
import com.softjads.jorge.hellojesus.topic.TopicPresenter;
import com.softjads.jorge.hellojesus.word.WordActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.softjads.jorge.hellojesus.word.WordFragment.EXTRA_BUNDLE_WORD;
import static com.softjads.jorge.hellojesus.word.WordFragment.EXTRA_WORD;
import static com.softjads.jorge.hellojesus.word.WordFragment.EXTRA_WORD_CHECK;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 23/02/2018.
 */

public class QuestionFragment extends Fragment implements TopicContract.View {


    private TopicContract.UserActionsListener mActionsListener;

    private static TopicsAdapter mListAdapter;
    private RecyclerView mRecyclerView;

    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "RECYCLER_VIEW_STATE";
    private Parcelable mListState;

    private TopicContract.UserActionsListener mPresenter;

    //private static List<Integer> mIdTopics;

    private static List<Content> mListContentBible;
    private static List<Content> mListContentMusicWrite;
    private static List<Content> mListContentMusicSaid;

    private static Context mContext;

    private static String mNameTitle = "";

    public QuestionFragment() {
    }

    public static QuestionFragment newInstance() {
        //mIdTopics = topicList;
        return new QuestionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionsListener = new TopicPresenter(Injection.provideWordsRepository(getActivity().getApplicationContext()), new TopicServiceImpl(getActivity()), this);
        mActionsListener.loadingWords("QQQ");
        mListAdapter = new TopicsAdapter(new ArrayList<Topic>(0));

    }

    @Override
    public void showWords(List<Content> listWord) {
        mListContentBible = new ArrayList<Content>();
        mListContentMusicWrite = new ArrayList<Content>();
        mListContentMusicSaid = new ArrayList<Content>();

        for (int i = 0; i < listWord.size(); i++){
            if (listWord.get(i).getCorret_option().toString().equals(getActivity().getResources().getString(R.string.key_bible))){
                mListContentBible.add(listWord.get(i));

            }else if (listWord.get(i).getCorret_option().toString().equals(getActivity().getResources().getString(R.string.key_music_said))){
                mListContentMusicSaid.add(listWord.get(i));
            }else{
                mListContentMusicWrite.add(listWord.get(i));
            }

        }


        List<Topic> topicList = new ArrayList<Topic>();

        for (int j = 0; j < 3 ; j++){

            Topic topic = new Topic();
            topic.setTime("");
            topic.setType(getActivity().getResources().getString(R.string.words));
            if (j == 0) {
                topic.setName(getActivity().getResources().getString(R.string.key_bible));
                topic.setContent(mListContentBible);
            }else if (j == 1) {
                topic.setName(getActivity().getResources().getString(R.string.key_music_said));
                topic.setContent(mListContentMusicSaid);
            }else{
                topic.setName(getActivity().getResources().getString(R.string.key_music_write));
                topic.setContent(mListContentMusicWrite);
            }
            topicList.add(topic);
        }

        mListAdapter.setList(topicList);
        mListAdapter.notifyDataSetChanged();



    }


    /**
     * Listener for clicks on shopping in the ListView.
     */
    WordsItemListener mItemListener = new WordsItemListener() {
        @Override
        public void onWordClick(Word clickedWord) {

        }

        @Override
        public void onCompleteWordsClick(Word completedPurchase) {
          //  mPresenter.completePurchase(completedPurchase);
        }

        @Override
        public void onActivateWordClick(Word activatedPurchase) {
            mPresenter.loadingWords(activatedPurchase.getType(),getContext().getResources().getString(R.string.key_music_write),"false");
        }

        @Override
        public void onRemoveWordClick(String word) {

        }

        @Override
        public void onFinalizeWordClick(String date) {

        }


    };


    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.loadingTopic(null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getContext();

        View root = inflater.inflate(R.layout.fragment_topics, container, false);

        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mActionsListener.loadingTopic(null);
            }
        });


        if (savedInstanceState== null){
            initRecyclerView(root);
            mBundleRecyclerViewState = new Bundle();
            Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
            mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        }else{
            initRecyclerView(root);
            mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
        }



        return root;
    }

    @Override
    public void setPresenter(TopicContract.UserActionsListener presenter) {
        mPresenter = checkNotNull(presenter);
    }


    public interface WordsItemListener {

        void onWordClick(Word clickedWord);

        void onCompleteWordsClick(Word completedWord);

        void onActivateWordClick(Word activatedWord);

        void onRemoveWordClick(String word);

        void onFinalizeWordClick(String date);
    }

    @Override
    public void showTopicBible(List<Topic> topics) {

    }

    @Override
    public void showTopicMusic(List<Topic> topics) {

    }

    @Override
    public void showTopicMusicSing(List<Topic> topics) {

    }

    @Override
    public void showTopicExercise(List<Topic> topics) {

    }

    @Override
    public void showTopicQuestion(List<Topic> topics) {

    }

    @Override
    public void showLoadingShoppingError() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showAllTopics() {
        Intent intent = new Intent(getActivity(), TopicActivity.class);

        startActivity(intent);
    }



    private static class TopicsAdapter extends RecyclerView.Adapter<QuestionFragment.TopicsAdapter.ViewHolder> {

        private List<Topic> mTopics;
     //   private QuestionFragment.ItemListener mItemListener;

        public TopicsAdapter(List<Topic> topicList) {
            setList(topicList);
        }

        @Override
        public QuestionFragment.TopicsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View noteView = inflater.inflate(R.layout.item_topic, parent, false);

            return new QuestionFragment.TopicsAdapter.ViewHolder(noteView);
        }

        @Override
        public void onBindViewHolder(QuestionFragment.TopicsAdapter.ViewHolder viewHolder, int position) {
            Topic topic = mTopics.get(position);

            viewHolder.topicName.setText(topic.getName());
            viewHolder.topicTime.setText(topic.getTime() + " " + mContext.getResources().getString(R.string.seconds) );
            viewHolder.topicGlossary.setText(Integer.toString(topic.getContent().size()));
            viewHolder.topicPhases.setText(mContext.getResources().getString(R.string.words));

            // topic.getContent().size();

            Resources res = mContext.getResources();
            final int newColor = res.getColor(R.color.colorAccent);
            viewHolder.topicImage.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        }

        public void replaceData(List<Topic> notes) {
            setList(notes);
            notifyDataSetChanged();
        }

        private void setList(List<Topic> notes) {
            mTopics = notes;
        }

        @Override
        public int getItemCount() {
            return mTopics.size();
        }

        public Topic getItem(int position) {
            return mTopics.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView topicName;
            public TextView topicGlossary;
            public TextView topicTime;
            public ImageView topicImage;
            public TextView topicPhases;
            private QuestionFragment.ItemListener mItemListener;

            public ViewHolder(View itemView) {
                super(itemView);
                //mItemListener = listener;
                topicName = (TextView) itemView.findViewById(R.id.tv_topic_name);
                topicGlossary = (TextView) itemView.findViewById(R.id.tv_topic_glossary);
                topicTime = (TextView) itemView.findViewById(R.id.tv_topic_time);
                topicImage = (ImageView) itemView.findViewById(R.id.iv_topic);
                topicPhases = (TextView) itemView.findViewById(R.id.tv_phases);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();



                   List<String> stringList = new ArrayList<String>();

                   for (int i = 0; i < mListAdapter.mTopics.get(position).getContent().size() ; i++){
                       stringList.add(mListAdapter.mTopics.get(position).getContent().get(i).getContent_english());
                   }


                   Intent intent = new Intent(v.getContext(), WordActivity.class);

                   Bundle bundle = new Bundle();
                   bundle.putSerializable(EXTRA_WORD, (Serializable) stringList);
                   bundle.putString(EXTRA_WORD_CHECK, "1");
                   intent.putExtra(EXTRA_BUNDLE_WORD, bundle);
                   v.getContext().startActivity(intent);



            }
        }
    }

    public interface ItemListener {

        void onTopicClick(Topic clickedNote);
    }

    private void initRecyclerView(View root){
        mRecyclerView= (RecyclerView) root.findViewById(R.id.rv_topic_list);
        mRecyclerView.setAdapter(mListAdapter);

        int numColumns = 1;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));
    }






}

