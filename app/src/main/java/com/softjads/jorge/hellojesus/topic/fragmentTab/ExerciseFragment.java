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

import com.softjads.jorge.hellojesus.R;
import com.softjads.jorge.hellojesus.data.onLine.topic.TopicServiceImpl;
import com.softjads.jorge.hellojesus.data.onLine.topic.model.Content;
import com.softjads.jorge.hellojesus.data.onLine.topic.model.Topic;
import com.softjads.jorge.hellojesus.topic.TopicActivity;
import com.softjads.jorge.hellojesus.topic.TopicContract;
import com.softjads.jorge.hellojesus.topic.TopicPresenter;
import com.softjads.jorge.hellojesus.write.WriteActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.softjads.jorge.hellojesus.main.MainFragment.EXTRA_BUNDLE_MAIN;
import static com.softjads.jorge.hellojesus.main.MainFragment.EXTRA_MAIN;
import static com.softjads.jorge.hellojesus.main.MainFragment.EXTRA_STRING_TITLE;
import static com.softjads.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_CONTENT_SOURCE_NAME;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 23/02/2018.
 */

public class ExerciseFragment extends Fragment implements TopicContract.View {

    public static String EXTRA_CONTENT_TIME = "CONTENT_TIME";
    public static String EXTRA_CONTENT_MP3 = "CONTENT_MP3";
    public static String EXTRA_CONTENT = "CONTENT";
    public static String EXTRA_BUNDLE_CONTENT = "BUNDLE_CONTENT";
    public static String EXTRA_CONTENT_LAST = "CONTENT_LAST";
    public static String EXTRA_CONTENT_NAME = "CONTENT_NAME";
    public static String EXTRA_SOURCE_NAME = "CONTENT_SOURCE_NAME";

    private TopicContract.UserActionsListener mActionsListener;

    private static ExerciseFragment.TopicsAdapter mListAdapter;
    private RecyclerView mRecyclerView;

    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "RECYCLER_VIEW_STATE";
    private Parcelable mListState;

    private TopicContract.UserActionsListener mPresenter;

    private static List<Integer> mIdTopics;

    private static String mSourceName;

    private static Context mContext;

    private static String mNameTitle = "";

    public ExerciseFragment() {
    }

    public static ExerciseFragment newInstance(List<Integer> topicList, String sourceName) {
        mIdTopics = topicList;
        mSourceName = sourceName;
        return new ExerciseFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new ExerciseFragment.TopicsAdapter(new ArrayList<Topic>(0), mItemListener);
        mActionsListener = new TopicPresenter(new TopicServiceImpl(getActivity()), this);
        mNameTitle = getActivity().getIntent().getStringExtra(EXTRA_STRING_TITLE);
        Bundle mBundle = getActivity().getIntent().getBundleExtra(EXTRA_BUNDLE_MAIN);
        mIdTopics = (List<Integer>) mBundle.getSerializable(EXTRA_MAIN);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.loadingTopic(mIdTopics);
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

                mActionsListener.loadingTopic(mIdTopics);
            }
        });


        if (savedInstanceState== null){
            initRecyclerView(root);
            mBundleRecyclerViewState = new Bundle();
            Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
            mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        }else {
            initRecyclerView(root);
            if (mBundleRecyclerViewState != null){
                mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
            }else{
                mBundleRecyclerViewState = new Bundle();
                Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
                mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
            }
        }



        return root;
    }




    BibleFragment.ItemListener mItemListener = new BibleFragment.ItemListener() {
        @Override
        public void onTopicClick(List<Content> clickedNote) {
            mActionsListener.openDetail();
        }


    };

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
        mListAdapter.replaceData(topics);
    }

    @Override
    public void showTopicQuestion(List<Topic> topics) {

    }

    @Override
    public void showWords(List<Content> contentList) {

    }

    @Override
    public void showLoadingShoppingError() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showAllTopics() {
        Intent intent = new Intent(getActivity(), TopicActivity.class);

        startActivity(intent);
    }

    @Override
    public void setPresenter(TopicContract.UserActionsListener presenter) {
        mPresenter = checkNotNull(presenter);
    }


    private static class TopicsAdapter extends RecyclerView.Adapter<ExerciseFragment.TopicsAdapter.ViewHolder> {

        private List<Topic> mTopics;
        private BibleFragment.ItemListener mItemListener;

        public TopicsAdapter(List<Topic> topicList, BibleFragment.ItemListener itemListener) {
            setList(topicList);
            mItemListener = itemListener;
        }

        @Override
        public ExerciseFragment.TopicsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View noteView = inflater.inflate(R.layout.item_topic, parent, false);

            return new ExerciseFragment.TopicsAdapter.ViewHolder(noteView, mItemListener);
        }

        @Override
        public void onBindViewHolder(ExerciseFragment.TopicsAdapter.ViewHolder viewHolder, int position) {
            Topic topic = mTopics.get(position);

            viewHolder.topicName.setText(topic.getName());
            viewHolder.topicTime.setText("");
            viewHolder.topicGlossary.setText(Integer.toString(topic.getContent().size()));
            viewHolder.topicPhases.setText(mContext.getResources().getString(R.string.exercises));

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
            private BibleFragment.ItemListener mItemListener;

            public ViewHolder(View itemView, BibleFragment.ItemListener listener) {
                super(itemView);
                mItemListener = listener;
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
                List<Content> contents = getItem(position).getContent();
                int time = Integer.parseInt(getItem(position).getTime());
                String mp3 = (getItem(position).getAudio());

                mItemListener.onTopicClick(contents);

                Intent intent = new Intent(v.getContext(), WriteActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_CONTENT, (Serializable) contents);
                bundle.putInt(EXTRA_CONTENT_TIME, (int) time);
                bundle.putString(EXTRA_CONTENT_MP3, mp3);
                bundle.putString(EXTRA_CONTENT_SOURCE_NAME, mListAdapter.getItem(0).getName());

                if (mListAdapter.mTopics.size() == (position + 1) ){
                    bundle.putString(EXTRA_CONTENT_LAST, "1");
                }else{
                    bundle.putString(EXTRA_CONTENT_LAST, "0");
                }

                bundle.putString(EXTRA_CONTENT_NAME, mNameTitle);

                intent.putExtra(EXTRA_BUNDLE_CONTENT, bundle);

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

