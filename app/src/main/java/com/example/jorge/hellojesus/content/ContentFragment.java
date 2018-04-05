package com.example.jorge.hellojesus.content;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.data.onLine.main.MainServiceImpl;
import com.example.jorge.hellojesus.data.onLine.main.model.Main;
import com.example.jorge.hellojesus.data.onLine.topic.model.Content;
import com.example.jorge.hellojesus.main.MainContract;
import com.example.jorge.hellojesus.main.MainFragment;
import com.example.jorge.hellojesus.main.MainPresenter;
import com.example.jorge.hellojesus.topic.TopicActivity;
import com.example.jorge.hellojesus.util.Common;
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
import java.util.Random;
import java.util.concurrent.TimeUnit;

import jp.shts.android.storiesprogressview.StoriesProgressView;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 27/02/2018.
 */

public class ContentFragment extends Fragment implements ContentContract.View, ExoPlayer.EventListener, StoriesProgressView.StoriesListener {

    public static final String MESSAGE_PROGRESS = "message_progress";
    public static final String BASE_STORAGE =  Environment.DIRECTORY_DOWNLOADS;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private SimpleExoPlayer mExoPlayerAudio;
    private Handler durationHandler = new Handler();
    private SeekBar seekbar;

    private static double mTimeElapsed = 0, mFinalTime = 0,  mTimeLast = 0;

    private static SimpleExoPlayerView mPlayerView;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;


    private static ContentContract.UserActionsListener mPresenter;

    private ContentFragment.ContentAdapter mListAdapter;
    private RecyclerView mRecyclerView;

    public static List<Content> mContents;
    public static int mTime;
    public static String mMp3;

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

    private static Context mContext;

    private static Button mWord;

    private StoriesProgressView storiesProgressView;


    private int counter = 0;

    private ProgressBar mProgressBar;
    private ObjectAnimator mAnimation;

    private long[] durations;


    long pressTime = 0;
    long limit = 5000;

    private static int mPosition = 0;

    int second;

    private TextView mValueStart;
    private TextView mValueEnd;

    private int selected_item;

    private static android.support.constraint.ConstraintLayout mText;


    public ContentFragment() {
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    public static ContentFragment newInstance(List<Content> contents, int time, String mp3) {
        mMp3 = mp3;
        mTime = time;
        mContents = contents;
        return new ContentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListAdapter = new ContentFragment.ContentAdapter(new ArrayList<Content>(0), mItemListener);
        mPresenter = new ContentPresenter( this);

    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadingContent(mContents, second);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mShowFab = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab_show);
        mHideFab = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab_hide);
        mRotateFab = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab_rotate);

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View root = inflater.inflate(R.layout.fragment_content, container, false);

        mLinearLayout = (LinearLayout) root.findViewById(R.id.fabContainerLayout);

        mFloatingActionButton = (FloatingActionButton) root.findViewById(R.id.fab_function);
        mFabImage = (FloatingActionButton) root.findViewById(R.id.fab_image);
        mFabExplanation = (FloatingActionButton) root.findViewById(R.id.fab_explanation);
        mFabTranslate = (FloatingActionButton) root.findViewById(R.id.fab_translate);

        // Initialize the player view.
        mPlayerView = (SimpleExoPlayerView) root.findViewById(R.id.sep_playerView_Audio);
        seekbar = (SeekBar) root.findViewById(R.id.exo_progress);
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

                mPresenter.loadingContent(mContents,second);
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

        requestPermission();
        initializeMediaSession();
        initializePlayer(Uri.parse(Environment.getExternalStoragePublicDirectory(BASE_STORAGE).toString() + "/" + mMp3 + ".mp3"));


        mPresenter.loadingContent(mContents, mTime);

        showAnimation();
        showProgress(root);


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
            mExoPlayerAudio.stop();
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
                MediaButtonReceiver.buildMediaButtonPendingIntent(this.getActivity(),
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action restartAction = new android.support.v4.app.NotificationCompat
                .Action(R.drawable.exo_controls_previous, getString(R.string.restart),
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (this.getActivity(), PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this.getActivity(), 0, new Intent(this.getActivity(), ContentActivity.class), 0);


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

        ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }

    @Override
    public void showContent(List<Content> contents) {
        mListAdapter.replaceData(contents);
    }

    @Override
    public void showProgress(View root) {
        storiesProgressView = (StoriesProgressView) root.findViewById(R.id.stories);
        storiesProgressView.setStoriesCount(PROGRESS_COUNT);
        storiesProgressView.setStoriesCountWithDurations(durations);
        storiesProgressView.animate().translationX(20);
        storiesProgressView.setStoriesListener(this);
        storiesProgressView.startStories();
    }

    @Override
    public void showAnimation() {
        mAnimation = ObjectAnimator.ofInt (mProgressBar, "progress", 0, 500); // see this max value coming back here, we animate towards that value
        mAnimation.setDuration (durations[mPosition]); //in milliseconds
        mAnimation.setInterpolator (new DecelerateInterpolator());
        mAnimation.start();


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
            mPresenter.pauseAudio(mExoPlayerAudio, mAnimation, storiesProgressView);
        }else{
            mPresenter.playAudio(mExoPlayerAudio, mAnimation, storiesProgressView);

        }
        showNotification(mStateBuilder.build());
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
        mNotificationManager.cancelAll();
        mExoPlayerAudio.stop();
        mExoPlayerAudio.release();
        mExoPlayerAudio = null;

    }

    @Override
    public void onNext() {
        mProgressBar.clearAnimation();
        mPosition ++;
        mRecyclerView.scrollToPosition(mPosition);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mValueStart.setText(Integer.toString(mPosition + 1));
        showAnimation();



        //mRecyclerView.setFocusable(true);

     //   mText = (android.support.constraint.ConstraintLayout) mRecyclerView.findViewHolderForPosition(mPosition).itemView.findViewById(R.id.cl_content);
      //  mText.setBackground(getActivity().getResources().getDrawable(android.R.color.darker_gray));
       // mText.setTypeface(null, Typeface.BOLD);



      //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
       //     mText.setTextColor(getActivity().getResources().getColor(android.R.color.white, getActivity().getTheme()));
     //   }else {
       //     mText.setTextColor(getActivity().getResources().getColor(android.R.color.white));
    //    }

       // mText = mRecyclerView.findViewHolderForPosition(mPosition).itemView.findViewById(R.id.tv_content_english);
       // mText.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;

    }

    @Override
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

           // setAnimation(viewHolder.itemView, position);

            if(position == mPosition) {
                viewHolder.mContentEnglish.setTypeface(null, Typeface.BOLD);

            } else {
                viewHolder.mContentEnglish.setTypeface(null, Typeface.NORMAL);
            }

            viewHolder.mContentEnglish.setText(content.getContent_english());
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

}


