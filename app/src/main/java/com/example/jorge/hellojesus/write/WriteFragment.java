package com.example.jorge.hellojesus.write;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jorge.hellojesus.Injection;
import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.content.ContentPresenter;
import com.example.jorge.hellojesus.data.onLine.topic.model.Content;
import com.example.jorge.hellojesus.progress.ProgressActivity;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.jorge.hellojesus.content.ContentFragment.BASE_STORAGE;
import static com.example.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_CONTENT_LAST;
import static com.example.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_CONTENT_NAME;


/**
 * Created by jorge on 06/04/2018.
 *
 */

public class WriteFragment extends Fragment implements WriteContract.View, ExoPlayer.EventListener {

    public static final String EXTRA_LIST_CONTENT = "EXTRA_LIST_CONTENT";
    public static final String EXTRA_ARRAY_LIST_STRING = "EXTRA_ARRAY_LIST_STRING";
    public static final String EXTRA_SOURCE_NAME = "EXTRA_SOURCE_NAME";
    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final String EXTRA_BUNDLE = "EXTRA_BUNDLE";

    private static double mTimeElapsed = 0, mFinalTime = 0,  mTimeLast = 0;

    private ProgressBar mProgressBar;


    private static SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayerAudio;
    private PlaybackStateCompat.Builder mStateBuilder;
    private MediaSessionCompat mMediaSession;
    private Handler durationHandler = new Handler();

    private boolean libera = false;

    public static final boolean mIsFinal = true;

    private static final String STATE_RESULTS = "results";
    // Resource caches
    private int mColorHearing;
    private int mColorNotHearing;
    // View references
    private TextView mStatus;
    private ImageView mLoadingGif;
    private static EditText mText;
    private WriteFragment.ResultAdapter mAdapter;
    private RecyclerView mRecyclerViewSpeech;

    private static final int PERMISSION_REQUEST_CODE = 1;

    private static WriteContract.UserActionsListener mPresenter;

    private WriteFragment.ContentAdapter mListAdapter;
    private RecyclerView mRecyclerView;

    public static List<Content> mContents;
    public static int mTime;
    public static String mMp3;
    public static String mSourceName;
    public static String mType;

    private static final int PROGRESS_COUNT = 18;

    private static Animation mRotateFab;

    private static FloatingActionButton mFloatingActionButton;
    private static FloatingActionButton mFabNext;

    private static Boolean mFabMenuOpen = false;

    private static LinearLayout mLinearLayout;
    private static Context mContext;


    private int counter = 0;

    private long[] durations;

    long pressTime = 0;
    long limit = 5000;

    private static int mPosition = 0;

    int second;

    private TextView mValueStart;
    private TextView mValueEnd;

    private static boolean statusPlay = false;

    public static String mName;
    public static String mSaveStatus;

    public WriteFragment() {
    }


    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    return limit < now - pressTime;
            }
            return false;
        }
    };


    public static WriteFragment newInstance(List<Content> contents, int time, String mp3, String sourceName, String saveStatus, String name) {

        mSourceName = sourceName;
        mMp3 = mp3;
        mTime = time;
        mContents = contents;
        mSaveStatus = saveStatus;
        mName = name;
        return new WriteFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = 0;

        mType = getActivity().getResources().getString(R.string.key_music_write);

        mListAdapter = new WriteFragment.ContentAdapter(new ArrayList<Content>(0));
        mPresenter = new WritePresenter(this,  Injection.provideWordsRepository(getActivity().getApplicationContext()),mSaveStatus, mName);



    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadingContent(mContents, second);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRotateFab = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab_rotate);

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View root = inflater.inflate(R.layout.fragment_write, container, false);

        mLinearLayout = (LinearLayout) root.findViewById(R.id.fabContainerLayout);

        mFloatingActionButton = (FloatingActionButton) root.findViewById(R.id.fab_function);
        mFabNext = (FloatingActionButton) root.findViewById(R.id.fab_next);

        // Initialize the player view.
        mPlayerView = (SimpleExoPlayerView) root.findViewById(R.id.sep_playerView_Audio);

        mValueStart = (TextView) root.findViewById(R.id.tv_start);
        mValueEnd = (TextView) root.findViewById(R.id.tv_end);

        mValueStart.setText("1 / ");
        mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_content_list);

        mContext = getContext();

        mProgressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mPresenter.loadingContent(mContents, second);
            }
        });


        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayerStateChangedDuration(true);

            }
        });

        mFabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayerStateChanged(true,3);
                onNextPart();
                putTextWrite(mText.getText().toString());


            }
        });

        final Resources resources = getResources();
        final Resources.Theme theme = getActivity().getTheme();
        mColorHearing = ResourcesCompat.getColor(resources, R.color.colorAccent, theme);
        mColorNotHearing = ResourcesCompat.getColor(resources, R.color.colorAccent, theme);

        mStatus = (TextView)  root.findViewById(R.id.status);
        mLoadingGif = (ImageView) root.findViewById(R.id.iv_loading_gif);
        mText = (EditText) root.findViewById(R.id.tv_enter_text);

        mRecyclerViewSpeech = (RecyclerView) root.findViewById(R.id.recycler_view);
        mRecyclerViewSpeech.setLayoutManager(new LinearLayoutManager(getContext()));
        final ArrayList<String> results = savedInstanceState == null ? null :
                savedInstanceState.getStringArrayList(STATE_RESULTS);
        mAdapter = new WriteFragment.ResultAdapter(results);
        mRecyclerViewSpeech.setAdapter(mAdapter);

        initRecyclerView();

        requestPermission();
        initializeMediaSession();
        initializePlayer(Uri.parse(Environment.getExternalStoragePublicDirectory(BASE_STORAGE).toString() + "/" + mMp3 + ".mp3"));


        mPresenter.loadingContent(mContents, mTime);

        return root;
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void toggleFabMenu() {
        mFloatingActionButton.startAnimation(mRotateFab);
        if (!mFabMenuOpen) {
            mFloatingActionButton.setImageResource(R.drawable.ic_remove_circle_outline_white_24dp);
            mLinearLayout.setVisibility(View.VISIBLE);
            int centerX = mLinearLayout.getWidth() / 2;
            int centerY = mLinearLayout.getHeight() / 2;
            int startRadius = 0;
            int endRadius = (int) Math.hypot(mLinearLayout.getWidth(), mLinearLayout.getHeight()) / 2;

            // mLinearLayout.setVisibility(View.VISIBLE);
            ViewAnimationUtils
                    .createCircularReveal(
                            mLinearLayout,
                            centerX,
                            centerY,
                            startRadius,
                            endRadius
                    )
                    .setDuration(1000)
                    .start();
        } else {
            mFloatingActionButton.setImageResource(R.drawable.ic_add_circle_outline_white_24dp);
            int centerX = mLinearLayout.getWidth() / 2;
            int centerY = mLinearLayout.getHeight() / 2;
            int startRadius = (int) Math.hypot(mLinearLayout.getWidth(), mLinearLayout.getHeight()) / 2;
            int endRadius = 0;

            Animator animator = ViewAnimationUtils
                    .createCircularReveal(
                            mLinearLayout,
                            centerX,
                            centerY,
                            startRadius,
                            endRadius
                    );
            animator.setDuration(1000);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mLinearLayout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animator.start();
        }
        mFabMenuOpen = !mFabMenuOpen;
    }

    /**
     * Request Permission download for the user .
     */
    private void requestPermission() {

        ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void showContent(List<Content> contents) {
        mListAdapter.replaceData(contents);
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    @Override
    public void initializeMediaSession() {
        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(this.getActivity(), "BEBETO");

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new WriteFragment.MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
    }

    @Override
    public void setListTime(long[] listTime) {
        mValueEnd.setText(Integer.toString(listTime.length));
        durations = listTime;

    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayerAudio.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayerAudio.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());


        if (!playWhenReady){
            mPresenter.pauseAudio(mExoPlayerAudio);
        }else{
            mPresenter.playAudio(mExoPlayerAudio);

        }

    }


    public void onPlayerStateChangedDuration(boolean playWhenReady) {

        if (mPosition == 0) {
            mExoPlayerAudio.seekTo(0);
        }else if (mPosition == durations.length) {
            mExoPlayerAudio.seekTo(mListAdapter.getItem(mPosition).getTime() * 100, mPosition - 1);
        }else{
            mExoPlayerAudio.seekTo(getSumList(durations, mPosition - 1));
        }

        if (!playWhenReady){
            mPresenter.pauseAudio(mExoPlayerAudio);
        }else{
            mPresenter.playAudio(mExoPlayerAudio);

        }

    }


    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }


    private static class ContentAdapter extends RecyclerView.Adapter<WriteFragment.ContentAdapter.ViewHolder> {

        private List<Content> mContent;

        public ContentAdapter(List<Content> contents) {
            setList(contents);
        }


        @Override
        public WriteFragment.ContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mContext = parent.getContext();


            LayoutInflater inflater = LayoutInflater.from(mContext);
            View noteView = inflater.inflate(R.layout.item_write, parent, false);

            return new WriteFragment.ContentAdapter.ViewHolder(noteView);
        }

        @Override
        public void onBindViewHolder(WriteFragment.ContentAdapter.ViewHolder viewHolder, int position) {
            Content content = mContent.get(position);

            viewHolder.mContentEnglish.setText(content.getContent_english());
            viewHolder.mContentMyEnglish.setText(content.getContent_portuguese());

            if (position == mPosition) {
                viewHolder.mContentEnglish.setTypeface(null, Typeface.BOLD);
                viewHolder.mContentEnglish.setTextSize(mContext.getResources().getDimension(R.dimen.text_big));
            } else {
                viewHolder.mContentEnglish.setTypeface(null, Typeface.NORMAL);
                viewHolder.mContentEnglish.setTextSize(mContext.getResources().getDimension(R.dimen.text_normal));
            }

            if (mPosition > position){
                viewHolder.mContentEnglish.setVisibility(View.VISIBLE);
                viewHolder.mContentMyEnglish.setVisibility(View.VISIBLE);
            }else{
                viewHolder.mContentEnglish.setVisibility(View.INVISIBLE);
                viewHolder.mContentMyEnglish.setVisibility(View.INVISIBLE);
            }

        }


        public void replaceData(List<Content> notes) {
            setList(notes);
            notifyDataSetChanged();
        }

        private void setList(List<Content> notes) {
            mContent = notes;
        }

        @Override
        public int getItemCount() {
            return mContent.size();
        }

        public Content getItem(int position) {
            return mContent.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView mContentEnglish;
            public TextView mContentMyEnglish;


            public ViewHolder(View itemView) {
                super(itemView);
                mContentEnglish = (TextView) itemView.findViewById(R.id.tv_content_english);
                mContentMyEnglish = (TextView) itemView.findViewById(R.id.tv_content_my_english);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Content content = getItem(position);
            }
        }
    }


    private void initRecyclerView() {
        mRecyclerView.setAdapter(mListAdapter);

        int numColumns = 1;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));
    }




    private void putTextWrite(final String text){
        if (mText != null && !TextUtils.isEmpty(text)) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mIsFinal) {
                        mText.setText(null);
                        mAdapter.addResult(text);
                        mRecyclerViewSpeech.smoothScrollToPosition(0);
                        if (mRecyclerViewSpeech.getAdapter().getItemCount() > 0) {
                            mListAdapter.mContent.get(mRecyclerViewSpeech.getAdapter().getItemCount()-1).setContent_portuguese(text);
                            mRecyclerView.smoothScrollToPosition(mRecyclerViewSpeech.getAdapter().getItemCount()-1);
                        }

                    } else {
                        mText.setText(text);
                    }
                }
            });
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        EditText text;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_result_write, parent, false));
            text = (EditText) itemView.findViewById(R.id.tv_enter_text);
        }

    }

    private static class ResultAdapter extends RecyclerView.Adapter<WriteFragment.ViewHolder> {

        private final ArrayList<String> mResults = new ArrayList<>();

        ResultAdapter(ArrayList<String> results) {
            if (results != null) {
                mResults.addAll(results);
            }
        }

        @Override
        public WriteFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WriteFragment.ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(WriteFragment.ViewHolder holder, int position) {
            holder.text.setText(mResults.get(position));
        }

        @Override
        public int getItemCount() {
            return mResults.size();
        }

        void addResult(String result) {
            mResults.add(0, result);
            notifyItemInserted(0);
        }

        public ArrayList<String> getResults() {
            return mResults;
        }

    }


    @Override
    public void onStart() {
        super.onStart();



    }

    @Override
    public void onStop() {

        super.onStop();
    }

    private void startWrite() {

    }

    private void stopWrite() {


    }


    private void showStatus(final boolean hearingVoice) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatus.setTextColor(hearingVoice ? mColorHearing : mColorNotHearing);
                if (hearingVoice) {
                    changeStatus(true);

                }else{

                }

            }
        });
    }

    private void changeStatus(boolean status ){
        if (status) {
            mLoadingGif.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(R.drawable.sound_image).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(mLoadingGif);
            mFloatingActionButton.setImageResource(R.drawable.exo_controls_pause);
            mStatus.setVisibility(View.GONE);
          //  statusPlay = true;

           // onPlayerStateChanged(false,3);
            mPresenter.playAudio(mExoPlayerAudio);

            mPosition++;
            mRecyclerView.scrollToPosition(mPosition);
            mRecyclerView.getAdapter().notifyDataSetChanged();
            mValueStart.setText(Integer.toString(mPosition + 1) + " / ");

        }else{
            mLoadingGif.setVisibility(View.GONE);
            mFloatingActionButton.setImageResource(R.drawable.exo_controls_play);
            mStatus.setVisibility(View.VISIBLE);
          //  statusPlay = false;
          //  onPlayerStateChanged(true,3);
            mPresenter.pauseAudio(mExoPlayerAudio);

        }
    }

    /**
     * Initialize ExoPlayer.
     */
    @Override
    public void initializePlayer(Uri mediaUriAudio) {
        if (mExoPlayerAudio == null) {

            /**
             * Create Audio.
             */
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayerAudio = ExoPlayerFactory.newSimpleInstance(this.getActivity(), trackSelector, loadControl);


            mPlayerView.setPlayer(mExoPlayerAudio);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayerAudio.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this.getActivity(), "ClassicalMusicQuiz");
            MediaSource mediaSourceAudio = new ExtractorMediaSource(mediaUriAudio, new DefaultDataSourceFactory(
                    this.getActivity(), userAgent), new DefaultExtractorsFactory(), null,null);


            mExoPlayerAudio.prepare(mediaSourceAudio);


            durationHandler.postDelayed(updateSeekBarTime, 1);
            mExoPlayerAudio.setPlayWhenReady(true);

        }
    }


    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayerAudio.setPlayWhenReady(true);

        }

        @Override
        public void onPause() {
            mExoPlayerAudio.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayerAudio.seekTo(0);
        }
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }

    public void onNextPart() {
        mPosition ++;
        mRecyclerView.scrollToPosition(mPosition);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mValueStart.setText(Integer.toString(mPosition + 1) + " / ");

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mPosition == mListAdapter.mContent.size()){

                    Intent intent = new Intent(getActivity(), ProgressActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(EXTRA_LIST_CONTENT, (Serializable) mListAdapter.mContent);
                    bundle.putStringArrayList(EXTRA_ARRAY_LIST_STRING, mAdapter.getResults());
                    bundle.putString(EXTRA_SOURCE_NAME,mSourceName );
                    bundle.putString(EXTRA_TYPE,mType );

                    intent.putExtras(bundle);
                    startActivity(intent);

                    changeStatus(false);
                    getActivity().finish();

                }
            }
        });
    }


    /**
     * Control the time for Put TXTs in TextView with this information.
     */
    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            if (mExoPlayerAudio != null) {
                //get current position
                mTimeElapsed = mExoPlayerAudio.getCurrentPosition();
                mTimeLast = mExoPlayerAudio.getDuration();

                double timeRemaining = mFinalTime - mTimeElapsed;
                String second = Float.toString(1000 * (-1 * TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining)));

                Double timeSound = Double.parseDouble(second) + 2;
                long timeForStop = getSumList(durations,mPosition);

                if (timeSound > timeForStop) {
                    onPlayerStateChanged(false,3);
                }

                //repeat yourself that again in 100 milliseconds
                durationHandler.postDelayed(this, 100);
            }
        }
    };



    private long getSumList(long[] durations, int position){
        int i = 0;
        long sum = 0;
        while (i <= position){
            if (i < durations.length ) {
                sum = sum + durations[i];
            }else{
                sum = mTime * 100;
            }
            i++;
        }
        return sum;
    }


}

