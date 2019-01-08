package com.example.jorge.hellojesus.content;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jorge.hellojesus.Injection;
import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.data.onLine.topic.model.Content;
import com.example.jorge.hellojesus.speech.support.MessageDialogFragment;
import com.example.jorge.hellojesus.util.Common;
import com.example.jorge.hellojesus.util.download.Download;
import com.example.jorge.hellojesus.util.download.DownloadService;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.example.jorge.hellojesus.util.download.Utility.BASE_STORAGE;
import static com.example.jorge.hellojesus.util.download.Utility.EXTRA_DOWNLOAD;
import static com.example.jorge.hellojesus.util.download.Utility.EXTRA_POSITION;
import static com.example.jorge.hellojesus.util.download.Utility.EXTRA_POSITION_NUMBER;
import static com.example.jorge.hellojesus.util.download.Utility.FILE_DOWNLOAD_COMPLETE;

/**
 * Created by jorge on 27/02/2018.
 */

public class ContentFragment extends Fragment implements ContentContract.View, ExoPlayer.EventListener, MessageDialogFragment.Listener {

    public static final String MESSAGE_PROGRESS = "message_progress";

    public static LocalBroadcastManager bManager;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;

    private SimpleExoPlayer mExoPlayerAudio;
    private Handler durationHandler = new Handler();

    private static double mTimeElapsed = 0, mFinalTime = 0,  mTimeLast = 0;

    private static SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private static ObjectAnimator mAnimation;

    private static ContentContract.UserActionsListener mPresenter;

    private ContentFragment.ContentAdapter mListAdapter;
    private RecyclerView mRecyclerView;

    public static List<Content> mContents;
    public static int mTime;
    public static String mMp3;

    public static String mName;
    public static String mSaveStatus;
    public static String mStatus;

    private static final int PROGRESS_COUNT = 18;

    private static Animation mShowFab;
    private static Animation mHideFab;
    private static Animation mRotateFab;

    private static FloatingActionButton mFloatingActionButton;
    private static FloatingActionButton mFabImage;
    private static FloatingActionButton mFabExplanation;
    private static FloatingActionButton mFabTranslate;

    private static  Boolean mFabMenuOpen = false;

    private static LinearLayout mLinearLayout;
    private static LinearLayout mLinearLayout2;

    private static Context mContext;

    private static Button mWord;

    private int counter = 0;

    private ProgressBar mProgressBar;


    private long[] durations;


    long pressTime = 0;
    long limit = 5000;

    private static int mPosition = 0;

    private TextView mValueStart;
    private TextView mValueEnd;

    private int selected_item;

    private String mFileName;

    private static android.support.constraint.ConstraintLayout mText;

    private static TextView mProgressText;
    private static ProgressBar mProgressBarHorizontal;


    private static View root;

    private static final String FRAGMENT_MESSAGE_DIALOG = "message_dialog";

    private static String mShowTranslate;

    private ImageView fabNext;
    private ImageView fabBack;


    public ContentFragment() {
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    //storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    //storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    public static ContentFragment newInstance(List<Content> contents, int time, String mp3, String saveStatus, String name, String status, String showTranslate) {
        mMp3 = mp3;
        mTime = time;
        mContents = contents;
        mSaveStatus = saveStatus;
        mName = name;
        mStatus = status;
        mShowTranslate = showTranslate;
        return new ContentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            registerReceiver();
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }

        mPosition = 0;
        mListAdapter = new ContentFragment.ContentAdapter(new ArrayList<Content>(0), mItemListener);
        mPresenter = new ContentPresenter( this, Injection.provideWordsRepository(getActivity().getApplicationContext()),mSaveStatus, mName,mStatus);
      //  requestPermission();
    }


    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        if (mPresenter != null){
            mPresenter.pauseAudio(mExoPlayerAudio);
            mPresenter = null;
        }

        if (mStateBuilder != null){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    0, 1f);
            mMediaSession.setPlaybackState(mStateBuilder.build());
            mMediaSession.setCallback(new MySessionCallback());
            mMediaSession.setActive(false);

            mStateBuilder = null;
        }

        resetSession();
        super.onDestroy();
        releasePlayer();
        if (mMediaSession != null) {

            mMediaSession.setActive(false);
        }

        if (bManager != null) {
            bManager.unregisterReceiver(broadcastReceiver);
        }

        if (mAnimation != null) {
            mAnimation.start();
            mAnimation.pause();
            mAnimation.removeAllListeners();
        }

        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(broadcastReceiver);

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadingContent(mContents, mTime);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mShowFab = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab_show);
        mHideFab = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab_hide);
        mRotateFab = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab_rotate);

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        root = inflater.inflate(R.layout.fragment_content, container, false);

        mLinearLayout = (LinearLayout) root.findViewById(R.id.fabContainerLayout);

        mLinearLayout2 = (LinearLayout) root.findViewById(R.id.linearLayout);
        final int newColorRed = getResources().getColor(R.color.red);
        mLinearLayout2.setBackgroundColor(Common.getColorWithAlpha(newColorRed, 0.6f));

        mFloatingActionButton = (FloatingActionButton) root.findViewById(R.id.fab_function);
        mFabImage = (FloatingActionButton) root.findViewById(R.id.fab_image);
        mFabExplanation = (FloatingActionButton) root.findViewById(R.id.fab_explanation);
        mFabTranslate = (FloatingActionButton) root.findViewById(R.id.fab_translate);

        mProgressText = (TextView) root.findViewById(R.id.tv_progress_text);
        mProgressBarHorizontal = (ProgressBar) root.findViewById(R.id.pb_progress);

        mProgressBarHorizontal.setVisibility(View.GONE);
        mProgressText.setVisibility(View.GONE);

        // Initialize the player view.
        mPlayerView = (SimpleExoPlayerView) root.findViewById(R.id.sep_playerView_Audio);

        mProgressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        mWord = (Button) root.findViewById(R.id.tv_word);
        mValueStart = (TextView) root.findViewById(R.id.tv_start);
        mValueEnd = (TextView) root.findViewById(R.id.tv_end);



        mWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((TextView) v).getTag().toString().equals("0")){
                    ((TextView) v).setText(nextSpace(((TextView) v).getTag().toString(),((TextView) v)));
                }

            }
        });


        fabNext = root.findViewById(R.id.fab_next);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onNextStep();
            }
        });

        fabBack = root.findViewById(R.id.fab_back);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onPriorStep();
            }
        });


       // mPresenter.HideFabButton(mFloatingActionButton, mHideFab);



        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mPresenter.loadingContent(mContents,mTime);
            }
        });



        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMenu();

            }
        });

        mFabImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMenu();
                mPresenter.openBrowserImage(mContext,mWord);

            }
        });

        mFabExplanation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMenu();
                mPresenter.openBrowserExplanation(mContext,mWord);

            }
        });

        mFabTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMenu();
                mPresenter.openBrowserTranslate(mContext,mWord);

            }
        });



        initRecyclerView(root);




        mFileName = mMp3;
        if (!verifyExistFiles(mFileName)){
            mProgressBarHorizontal.setVisibility(View.VISIBLE);
            mProgressText.setVisibility(View.VISIBLE);
            allStarDownload(mFileName);
        }else{
           // requestPermission();
            initializeMediaSession();
            initializePlayer(Uri.parse(BASE_STORAGE + "/" + mMp3 + ".mp3"));

            mPresenter.loadingContent(mContents, mTime);

            showAnimation();
        }


        return root;
    }


    public void onPriorStep() {
        if (mPosition > 0){
            mPosition --;
        }
        mProgressBar.clearAnimation();
        mAnimation.removeAllListeners();
        mValueStart.setText(Integer.toString(mPosition +1));
        mRecyclerView.scrollToPosition(mPosition);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mRecyclerView.setFocusable(true);
        onPlayerStateChangedDuration(true);
        showAnimation();
        mFabMenuOpen = true;
        toggleFabMenu();
    }

    public void onNextStep() {

        if (mPosition < mContents.size()){
            mPosition ++;
        }
        mAnimation.removeAllListeners();
        mProgressBar.clearAnimation();
        mValueStart.setText(Integer.toString(mPosition +1));
        mRecyclerView.scrollToPosition(mPosition);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mRecyclerView.setFocusable(true);
        onPlayerStateChangedDuration(true);
        showAnimation();
        mFabMenuOpen = true;
        toggleFabMenu();
    }

    //@Override
    public void onNext() {
        mProgressBar.clearAnimation();
        mPosition ++;
        mAnimation.removeAllListeners();
        mRecyclerView.scrollToPosition(mPosition);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mValueStart.setText(Integer.toString(mPosition + 1));
        showAnimation();
        mFabMenuOpen = true;
        toggleFabMenu();
        mRecyclerView.setFocusable(true);
    }

    public void onPlayerStateChangedDuration(boolean playWhenReady) {
        mPresenter.pauseAudio(mExoPlayerAudio);
        if (mPosition == 0) {
            mExoPlayerAudio.seekTo(0);
        } else if (mPosition == durations.length) {
            //mExoPlayerAudio.seekTo(mListAdapter.getItem(mPosition).getTime() * 100, mPosition - 1);
        } else {
            mExoPlayerAudio.seekTo(getSumList(durations, mPosition-1));
        }

       // if (!playWhenReady) {
       //     mPresenter.pauseAudio(mExoPlayerAudio, mAnimation, storiesProgressView);
        //} else {
        mPresenter.playAudio(mExoPlayerAudio);

       // }
    }

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
            try {


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

            }catch (Exception e) {
                Log.wtf("DO THIS", " WHEN SAVE() FAILS");
            }
        }
        mFabMenuOpen = !mFabMenuOpen;

    }

   // @Override
  //  public void openViewContent(Content content) {
    //    Intent intent = new Intent(getActivity(), TopicActivity.class);

   //     List<Integer> ii = content.getTopics();

    //    Bundle bundle = new Bundle();
   //     bundle.putSerializable(EXTRA_MAIN, (Serializable) main.getTopics());
   //     intent.putExtra(EXTRA_BUNDLE_MAIN, bundle);
  //      startActivity(intent);

  //  }




    /**
     * Reset Session the Audio and the Video
     */
    private void resetSession() {

        if (mExoPlayerAudio != null) {
            mExoPlayerAudio.setPlayWhenReady(false);
        }

        mExoPlayerAudio = null;

    }

    /**
     * Shows Media Style notification, with actions that depend on the current MediaSession
     * PlaybackState.
     */
    private void showNotification(PlaybackStateCompat state) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        int icon;
        String play_pause;
        if(state.getState() == PlaybackStateCompat.STATE_PLAYING){
            icon = R.drawable.exo_controls_pause;
            play_pause = getString(R.string.pause);
        } else {
            icon = R.drawable.exo_controls_play;
            play_pause = getString(R.string.play);
        }


        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, play_pause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(mContext,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action restartAction = new android.support.v4.app.NotificationCompat
                .Action(R.drawable.exo_controls_previous, getString(R.string.restart),
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (mContext, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (mContext, 0, new Intent(mContext, ContentActivity.class), 0);


        builder.setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(restartAction)
                .addAction(playPauseAction)
                .setStyle(new NotificationCompat.MediaStyle()
                        .setMediaSession(mMediaSession.getSessionToken())
                        .setShowActionsInCompactView(0,1));

//        mNotificationManager = (NotificationManager)  this.getActivity().getSystemService(NOTIFICATION_SERVICE);
//        mNotificationManager.notify(0, builder.build());


    }

    /**
     * Request Permission download for the user .
     */
    private void requestPermission(){

        if (this.getActivity() != null) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_RECORD_AUDIO_PERMISSION);
        }


    }



    @Override
    public void onMessageDialogDismissed() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_RECORD_AUDIO_PERMISSION);
    }

    private void showPermissionMessageDialog() {


        LayoutInflater factory2 = LayoutInflater.from(getActivity());
        final View deleteDialogView2 = factory2.inflate(
                R.layout.dialog_permission, null);
        final AlertDialog deleteDialog2 = new AlertDialog.Builder(getActivity()).create();
        deleteDialog2.setView(deleteDialogView2);

        TextView nTextView2 = (TextView) deleteDialogView2.findViewById(R.id.txt_dia);
        nTextView2.setText(getString(R.string.permission_message));

        deleteDialogView2.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteDialog2.dismiss();
            }
        });

        deleteDialog2.show();
       /* MessageDialogFragment
                .newInstance(getString(R.string.permission_message))
                .show(getActivity().getSupportFragmentManager(), FRAGMENT_MESSAGE_DIALOG);*/
    }

    @Override
    public void showContent(List<Content> contents) {
        mListAdapter.replaceData(contents);
    }



    @Override
    public void showAnimation() {
        if (mPosition < durations.length){
          //
           // if (mAnimation == null){
                mAnimation = ObjectAnimator.ofInt (mProgressBar, "progress", 0, 500); // see this max value coming back here, we animate towards that value
           // }

            mAnimation.setDuration (durations[mPosition]); //in milliseconds
            mAnimation.setInterpolator (new DecelerateInterpolator());
            mAnimation.start();
            mAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }
                @Override
                public void onAnimationEnd(Animator animation) {
                    onNext();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }else{

        }


    }

    @Override
    public void showAllContent() {

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
            if (mPresenter != null) {
                mPresenter.pauseAudio(mExoPlayerAudio);
                if (mAnimation != null && mAnimation.isStarted()) {
                    mAnimation.pause();
                }
            }
        }else{
            if (mPresenter != null) {
                mPresenter.playAudio(mExoPlayerAudio);
                if (mAnimation != null && mAnimation.isPaused()) {
                    mAnimation.resume();
                }
            }

        }
       // showNotification(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    @Override
    public void initializeMediaSession() {

        if (mContext == null){
            mContext = this.getActivity();
        }

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(mContext, "BEBETO");

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
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);



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
            mExoPlayerAudio = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);


            mPlayerView.setPlayer(mExoPlayerAudio);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayerAudio.addListener(this);


            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(mContext, "ClassicalMusicQuiz");
            MediaSource mediaSourceAudio = new ExtractorMediaSource(mediaUriAudio, new DefaultDataSourceFactory(
                    mContext, userAgent), new DefaultExtractorsFactory(), null,null);


            mExoPlayerAudio.prepare(mediaSourceAudio);


            durationHandler.postDelayed(updateSeekBarTime, 1);
            mExoPlayerAudio.setPlayWhenReady(true);

        }
    }

    @Override
    public void setListTime(long[] listTime) {
        mValueEnd.setText(Integer.toString(listTime.length));
        durations = listTime;


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


                //repeat yourself that again in 100 milliseconds
                durationHandler.postDelayed(this, 100);
            }
        }
    };

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        //mNotificationManager.cancelAll();
        if (mExoPlayerAudio != null) {
            mExoPlayerAudio.stop();
            mExoPlayerAudio.release();
            mExoPlayerAudio.setPlayWhenReady(false);
            mExoPlayerAudio.removeListener(this);
            mExoPlayerAudio = null;
        }

    }



    //@Override
    public void onPrev() {
        if ((counter - 1) < 0) return;

    }

    //@Override
    public void onComplete() {

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



    private static class ContentAdapter extends RecyclerView.Adapter<ContentFragment.ContentAdapter.ViewHolder> {

        private List<Content> mContent;
        private ContentFragment.ItemListener mItemListener;

        public ContentAdapter(List<Content> contents, ContentFragment.ItemListener itemListener) {
            setList(contents);
            mItemListener = itemListener;
        }

        private void setAnimation(View viewToAnimate, int position)
        {
            // If the bound view wasn't previously displayed on screen, it's animated
          //  if (position > lastPosition)
           // {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
             //   lastPosition = position;
         //   }
        }

        @Override
        public ContentFragment.ContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mContext = parent.getContext();

            LayoutInflater inflater = LayoutInflater.from(mContext);
            View noteView = inflater.inflate(R.layout.item_content, parent, false);

            return new ContentFragment.ContentAdapter.ViewHolder(noteView, mItemListener);
        }

        @Override
        public void onBindViewHolder(ContentFragment.ContentAdapter.ViewHolder viewHolder, int position) {
            Content content = mContent.get(position);

            setAnimation(viewHolder.itemView, position);

            if (position == mPosition) {
                viewHolder.mContentEnglish.setTypeface(null, Typeface.BOLD);
                viewHolder.mContentEnglish.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));

            } else {
                viewHolder.mContentEnglish.setTypeface(null, Typeface.NORMAL);
            }

            viewHolder.mContentEnglish.setText(content.getContent_english());

            if (mShowTranslate != null){
                if (mShowTranslate.equals("Translate")) {
                    viewHolder.mContentPortuguese.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.mContentPortuguese.setVisibility(View.GONE);
                }
            }
            viewHolder.mContentPortuguese.setText(content.getContent_portuguese());

            viewHolder.mContentEnglish.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                   mPresenter.ShowControllerAudio(mPlayerView);
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Layout layout = ((TextView) v).getLayout();
                        int x = (int)event.getX();
                        int y = (int)event.getY();
                        if (layout!=null) {
                            int line = layout.getLineForVertical(y);
                            int offset = layout.getOffsetForHorizontal(line, x);

                            String phase = ((TextView) v).getText().toString();
                            String phaseRight = phase.toString().substring(offset, phase.length());

                            int index1 = firstSpace(phaseRight);
                            if (index1 < 0){
                                index1 = phaseRight.length();
                            }

                            int index2 = 0;
                            if (offset < phase.length()){
                                index2 = priorSpace(phase, offset);
                            }else{
                                offset = phase.length() - 1;
                                index2 = priorSpace(phase, offset);
                            }

                            if (index2 + 1 < index1 + offset) {
                                mWord.setTag(phase.toString().substring(index2 + 1, phase.length()));
                                String word = phase.toString().substring(index2 + 1, index1 + offset);
                                mWord.setText(word.replace(",",""));
                                mPresenter.ShowFabButton(mFloatingActionButton, mShowFab, mWord);
                            }else{
                                mWord.setText("");
                                mPresenter.HideFabButton(mFloatingActionButton, mHideFab, mWord);
                                mFabMenuOpen = true;
                                toggleFabMenu();
                            }
                        }
                    }
                    return true;
                }
            });


            String s = content.getContent_english();

            String[] arr = s.split(" ");

            for ( String ss : arr) {

                System.out.println(ss);
            }


            //viewHolder.mRelativeLayout = Common.createTagDynamic(viewHolder.mRelativeLayout,arr, mContext, true);


        }

        private int firstSpace(String textView){

            int i = textView.indexOf(" ");

            return i;

        }


        private int priorSpace(String textView, int off){
            int i = off;
            while (i > 0){

                if ((textView.toString().substring(i,i+1).indexOf(" ")==0)){
                    return i;
                }
                i--;
            }
            return i;

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
            public TextView mContentPortuguese;

            private ContentFragment.ItemListener mItemListener;

            public ViewHolder(View itemView, ContentFragment.ItemListener listener) {
                super(itemView);
                mItemListener = listener;
                mContentEnglish = (TextView) itemView.findViewById(R.id.tv_content_english);
                mContentPortuguese = (TextView) itemView.findViewById(R.id.tv_content_portuguese);

                mContentEnglish.setTypeface(null, Typeface.NORMAL);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Content content = getItem(position);
                mItemListener.onMainClick(content);

            }
        }
    }

    public interface ItemListener {

        void onMainClick(Content clickedNote);
    }

    ContentFragment.ItemListener mItemListener = new ContentFragment.ItemListener() {
        @Override
        public void onMainClick(Content content) {

            //mContent = content;
        }
    };

    private void initRecyclerView(View root){
        mRecyclerView= (RecyclerView) root.findViewById(R.id.rv_content_list);
        mRecyclerView.setAdapter(mListAdapter);

        int numColumns = 1;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));

    }

    private String nextSpace(String phrase, TextView TextView){
        int i = 0;
        int count = 0;
        int countWord = 1;


        while (i < TextView.getText().toString().length()){

            if ((TextView.getText().toString().toString().substring(i,i+1).indexOf(" ")==0)){
                countWord ++;
            }
            i++;
        }

        i = 0;
        while (i < phrase.length()){

            if ((phrase.toString().substring(i,i+1).indexOf(" ")==0)){
                count ++;
                if (count > countWord) {
                    return phrase.substring(0,i);
                }
            }
            i++;
        }
        return phrase.substring(0,i);

    }


    @Override
    public void onStart() {
        super.onStart();



        // Start listening to voices
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            if (!verifyExistFiles(mFileName)) {
                mProgressBarHorizontal.setVisibility(View.VISIBLE);
                mProgressText.setVisibility(View.VISIBLE);
                allStarDownload(mFileName);
            }
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.RECORD_AUDIO)) {
            showPermissionMessageDialog();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }

        // Start listening to voices
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.RECORD_AUDIO)) {
            showPermissionMessageDialog();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }


    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (permissions.length == 1 && grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!verifyExistFiles(mFileName)) {
                    mProgressBarHorizontal.setVisibility(View.VISIBLE);
                    mProgressText.setVisibility(View.VISIBLE);
                    allStarDownload(mFileName);
                }
            } else {
                showPermissionMessageDialog();
            }
        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (permissions.length == 1 && grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                showPermissionMessageDialog();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    /**
     * Verify if this files BG and SG they're gone
     */
    private boolean verifyExistFiles(String fileName) {
        if (fileName != null){
            fileName = (Common.returnFile(fileName));
            String fileNameWithPathBg = BASE_STORAGE + fileName;
            File fileBg = new File(fileNameWithPathBg);

            if (!fileBg.exists()) {
                return false;
            } else {
                return true;
            }
        }else{
            return false;
        }


    }




    /**
     * Call intent the Download with put extra
     */
    private void startDownload(String fileName, String position){

        Intent intent = new Intent(getActivity(), DownloadService.class);
        intent.putExtra(EXTRA_POSITION,fileName);
        intent.putExtra(EXTRA_POSITION_NUMBER, position);
        if (mContext == null){
            mContext = this.getActivity();
        }
        mContext.startService(intent);


    }

    /**
     * Manager the receiver for support return when finished.
     */
    private void registerReceiver() throws IntentFilter.MalformedMimeTypeException {

        Bundle extras = getActivity().getIntent().getExtras();
       // mPosition =  Integer.parseInt(extras.getString(EXTRA_POSITION));

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);






    }

    /**
     * BroadcastReceiver for wait when download finished.
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            if(intent.getAction().equals(MESSAGE_PROGRESS)){



                Download download = intent.getParcelableExtra(EXTRA_DOWNLOAD);
                mProgressBarHorizontal.setProgress(download.getProgress());
                if(download.getProgress() == 100){



                    // Initialize the player.
                    if (verifyExistFiles(mFileName) ){
                        mProgressText.setText(FILE_DOWNLOAD_COMPLETE);
                        mProgressBarHorizontal.setVisibility(View.GONE);
                        mProgressText.setVisibility(View.GONE);

                        mContext = context;
                        //requestPermission();
                        initializeMediaSession();
                        initializePlayer(Uri.parse(BASE_STORAGE + "/" + mMp3 + ".mp3"));

                        if (mPresenter != null) {
                            mPresenter.loadingContent(mContents, mTime);
                        }

                        showAnimation();

                    }

                } else {

                    mProgressText.setText(String.format("Downloaded (%d/%d) MB",download.getCurrentFileSize(),download.getTotalFileSize()));

                }
            }
        }
    };

    /**
     * Start download all file SG and BG.
     */
    private void allStarDownload(String position) {


        if (Common.checkPermission(getActivity())) {
            if (!position.equals("")){
                if (mFileName != null) {
                    mProgressText.setText("");
                    mProgressBarHorizontal.setVisibility(View.VISIBLE);
                    startDownload(mFileName, position);
                }
            }
        } else {
            //requestPermission();

        }

    }

}


