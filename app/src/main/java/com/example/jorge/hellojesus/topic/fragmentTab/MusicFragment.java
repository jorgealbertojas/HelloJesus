package com.example.jorge.hellojesus.topic.fragmentTab;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.content.ContentActivity;
import com.example.jorge.hellojesus.data.onLine.topic.TopicServiceImpl;
import com.example.jorge.hellojesus.data.onLine.topic.model.Content;
import com.example.jorge.hellojesus.data.onLine.topic.model.Topic;
import com.example.jorge.hellojesus.speech.SpeechActivity;
import com.example.jorge.hellojesus.topic.TopicActivity;
import com.example.jorge.hellojesus.topic.TopicContract;
import com.example.jorge.hellojesus.topic.TopicPresenter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_CONTENT_MP3;
import static com.example.jorge.hellojesus.util.KeyVar.KEY_SING;

/**
 * Created by jorge on 23/02/2018.
 */

public class MusicFragment  extends Fragment implements TopicContract.View {

    public static String EXTRA_CONTENT_TIME = "CONTENT_TIME";
    public static String EXTRA_CONTENT = "CONTENT";
    public static String EXTRA_BUNDLE_CONTENT = "BUNDLE_CONTENT";

    private TopicContract.UserActionsListener mActionsListener;

    private MusicFragment.TopicsAdapter mListAdapter;
    private RecyclerView mRecyclerView;

    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "RECYCLER_VIEW_STATE";
    private Parcelable mListState;

    private TopicContract.UserActionsListener mPresenter;

    private static List<Integer> mIdTopics;

    public MusicFragment() {
    }

    public static BibleFragment newInstance(List<Integer> topicList) {
        mIdTopics = topicList;
        return new BibleFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new MusicFragment.TopicsAdapter(new ArrayList<Topic>(0), mItemListener);
        mActionsListener = new TopicPresenter(new TopicServiceImpl(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.loadingTopic();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



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

                mActionsListener.loadingTopic();
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
        mListAdapter.replaceData(topics);
    }

    @Override
    public void showTopicExercise(List<Topic> topics) {

    }

    @Override
    public void showTopicQuestion(List<Topic> topics) {

    }

    @Override
    public void showAllTopics() {
        Intent intent = new Intent(getActivity(), TopicActivity.class);

        //Bundle bundle = new Bundle();
        // bundle.putSerializable(EXTRA_PRODUCT, null);

        //intent.putExtra(EXTRA_BUNDLE_PRODUCT, bundle);
        startActivity(intent);
    }



    private static class TopicsAdapter extends RecyclerView.Adapter<MusicFragment.TopicsAdapter.ViewHolder> {

        private List<Topic> mTopics;
        private BibleFragment.ItemListener mItemListener;

        public TopicsAdapter(List<Topic> topicList, BibleFragment.ItemListener itemListener) {
            setList(topicList);
            mItemListener = itemListener;
        }

        @Override
        public MusicFragment.TopicsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View noteView = inflater.inflate(R.layout.item_topic, parent, false);

            return new MusicFragment.TopicsAdapter.ViewHolder(noteView, mItemListener);
        }

        @Override
        public void onBindViewHolder(MusicFragment.TopicsAdapter.ViewHolder viewHolder, int position) {
            Topic topic = mTopics.get(position);

            viewHolder.topicName.setText(topic.getName());
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
            private BibleFragment.ItemListener mItemListener;

            public ViewHolder(View itemView, BibleFragment.ItemListener listener) {
                super(itemView);
                mItemListener = listener;
                topicName = (TextView) itemView.findViewById(R.id.tv_topic_name);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                List<Content> contents = getItem(position).getContent();
                int time = Integer.parseInt(getItem(position).getTime());
                String mp3 = (getItem(position).getAudio());

                if (mp3.toString().equals(KEY_SING)){
                    mItemListener.onTopicClick(contents);
                    Intent intent = new Intent(v.getContext(), SpeechActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(EXTRA_CONTENT, (Serializable) contents);
                    bundle.putInt(EXTRA_CONTENT_TIME, (int) time);
                    bundle.putString(EXTRA_CONTENT_MP3,  mp3);
                    intent.putExtra(EXTRA_BUNDLE_CONTENT, bundle);
                    v.getContext().startActivity(intent);
                }else{
                    mItemListener.onTopicClick(contents);
                    Intent intent = new Intent(v.getContext(), ContentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(EXTRA_CONTENT, (Serializable) contents);
                    bundle.putInt(EXTRA_CONTENT_TIME, (int) time);
                    bundle.putString(EXTRA_CONTENT_MP3,  mp3);
                    intent.putExtra(EXTRA_BUNDLE_CONTENT, bundle);
                    v.getContext().startActivity(intent);
                }
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