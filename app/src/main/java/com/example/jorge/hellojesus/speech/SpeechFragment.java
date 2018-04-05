package com.example.jorge.hellojesus.speech;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
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
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.data.onLine.topic.model.Content;
import com.example.jorge.hellojesus.speech.progress.ProgressActivity;
import com.example.jorge.hellojesus.speech.support.MessageDialogFragment;
import com.example.jorge.hellojesus.speech.support.SpeechService;
import com.example.jorge.hellojesus.speech.support.VoiceRecorder;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

import static android.content.Context.BIND_AUTO_CREATE;


/**
 * Created by jorge on 16/03/2018.
 */

public class SpeechFragment extends Fragment implements SpeechContract.View, StoriesProgressView.StoriesListener, MessageDialogFragment.Listener {

    public static final String EXTRA_TOTAL = "EXTRA_TOTAL";
    public static final String EXTRA_TOTAL_MISTAKE = "EXTRA_TOTAL_MISTAKE";
    public static final String EXTRA_TOTAL_MISSING = "EXTRA_TOTAL_MISSING";
    public static final String EXTRA_TOTAL_SAID = "EXTRA_TOTAL_SAID";
    public static final String EXTRA_TOTAL_CORRECT = "EXTRA_TOTAL_CORRECT";

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;
    private static final String FRAGMENT_MESSAGE_DIALOG = "message_dialog";
    private static final String STATE_RESULTS = "results";
    private SpeechService mSpeechService;
    // Resource caches
    private int mColorHearing;
    private int mColorNotHearing;
    // View references
    private TextView mStatus;
    private ImageView mLoadingGif;
    private TextView mText;
    private ResultAdapter mAdapter;
    private RecyclerView mRecyclerViewSpeech;

    private static List<String> ListCorrect;
    private static List<String> ListListen;







    public static final String MESSAGE_PROGRESS = "message_progress";
    public static final String BASE_STORAGE = Environment.DIRECTORY_DOWNLOADS;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private SimpleExoPlayer mExoPlayerAudio;
    private Handler durationHandler = new Handler();
    private SeekBar seekbar;

    private static double mTimeElapsed = 0, mFinalTime = 0, mTimeLast = 0;

    private static SimpleExoPlayerView mPlayerView;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;


    private static SpeechContract.UserActionsListener mPresenter;

    private SpeechFragment.ContentAdapter mListAdapter;
    private RecyclerView mRecyclerView;

    public static List<Content> mContents;
    public static int mTime;
    public static String mMp3;

    private static final int PROGRESS_COUNT = 18;

    private static Animation mShowFab;
    private static Animation mHideFab;
    private static Animation mRotateFab;




    private static FloatingActionButton mFloatingActionButton;
    private static FloatingActionButton mFabNext;

    private static Boolean mFabMenuOpen = false;

    private static LinearLayout mLinearLayout;

    private static Context mContext;

    private StoriesProgressView storiesProgressView;


    private int counter = 0;

    private long[] durations;


    long pressTime = 0;
    long limit = 5000;

    private static int mPosition = 0;

    int second;

    private TextView mValueStart;
    private TextView mValueEnd;
    private TextView mResultSpeech;

    private static boolean statusPlay = false;


    public SpeechFragment() {
    }

    @Override
    public void onMessageDialogDismissed() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_RECORD_AUDIO_PERMISSION);
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



    public static SpeechFragment newInstance(List<Content> contents, int time, String mp3) {
        mMp3 = mp3;
        mTime = time;
        mContents = contents;
        return new SpeechFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListAdapter = new SpeechFragment.ContentAdapter(new ArrayList<Content>(0));
        mPresenter = new SpeechPresenter(this);

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
        View root = inflater.inflate(R.layout.fragment_speech, container, false);

        mLinearLayout = (LinearLayout) root.findViewById(R.id.fabContainerLayout);

        mFloatingActionButton = (FloatingActionButton) root.findViewById(R.id.fab_function);
        mFabNext = (FloatingActionButton) root.findViewById(R.id.fab_next);

        // Initialize the player view.
        mPlayerView = (SimpleExoPlayerView) root.findViewById(R.id.sep_playerView_Audio);
        seekbar = (SeekBar) root.findViewById(R.id.exo_progress);
        mValueStart = (TextView) root.findViewById(R.id.tv_start);
        mValueEnd = (TextView) root.findViewById(R.id.tv_end);

        mValueStart.setText("1 / ");

        mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_content_list);

        mResultSpeech = (TextView) root.findViewById(R.id.tv_result_speech);


        mContext = getContext();




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

                mPresenter.loadingContent(mContents, second);
            }
        });


        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if ()
                mSpeechService.recognizeInputStream(getResources().openRawResource(R.raw.audio));
                if (statusPlay){
                    stopVoiceRecorder();
                    changeStatus(false);
                }else{
                    startVoiceRecorder();
                    changeStatus(true);
                }



            }
        });

        mFabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if ()
               onNext();



            }
        });

        final Resources resources = getResources();
        final Resources.Theme theme = getActivity().getTheme();
        mColorHearing = ResourcesCompat.getColor(resources, R.color.colorAccent, theme);
        mColorNotHearing = ResourcesCompat.getColor(resources, R.color.colorAccent, theme);

        mStatus = (TextView)  root.findViewById(R.id.status);
        mLoadingGif = (ImageView) root.findViewById(R.id.iv_loading_gif);
        mText = (TextView) root.findViewById(R.id.text);

        mRecyclerViewSpeech = (RecyclerView) root.findViewById(R.id.recycler_view);
        mRecyclerViewSpeech.setLayoutManager(new LinearLayoutManager(getContext()));
        final ArrayList<String> results = savedInstanceState == null ? null :
                savedInstanceState.getStringArrayList(STATE_RESULTS);
        mAdapter = new ResultAdapter(results);
        mRecyclerViewSpeech.setAdapter(mAdapter);


        initRecyclerView();

        requestPermission();
        initializeMediaSession();
        //  initializePlayer(Uri.parse(Environment.getExternalStoragePublicDirectory(BASE_STORAGE).toString() + "/" + mMp3 + ".mp3"));


        mPresenter.loadingContent(mContents, mTime);

        showProgress(root);

        mPresenter.pauseAudio(storiesProgressView);





        onStart();

        changeStatus(false);


        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (permissions.length == 1 && grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startVoiceRecorder();
            } else {
                showPermissionMessageDialog();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
    public void showAllContent() {
    }


    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    @Override
    public void initializeMediaSession() {
    }

    @Override
    public void initializePlayer(Uri mediaUriAudio) {

    }


    @Override
    public void setListTime(long[] listTime) {
        mValueEnd.setText(Integer.toString(listTime.length));
        durations = listTime;


    }

    @Override
    public void onNext() {
        if (statusPlay) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPosition++;
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(mPosition);
                    mValueStart.setText(Integer.toString(mPosition + 1) + getResources().getString(R.string.format_until));
                    mRecyclerView.setSelected(true);

                    if (mPosition == mListAdapter.mContent.size()){

                        ListCorrect = verifyTheWord(changeForAdpter(mListAdapter.mContent));

                        ListListen = verifyTheWord(changeForAdpter(mAdapter.getResults()));

                        int total = ListCorrect.size();
                        int totalSaid = ListListen.size();

                        List<String> result = new ArrayList<>();
                        result = countPoint(ListCorrect,ListListen,0);

                        int totalSaidCorrect = total - result.size();
                        int totalMistake = totalSaid - totalSaidCorrect;
                        int totalMissing = total - totalSaidCorrect;


                        Intent intent = new Intent(getActivity(), ProgressActivity.class);
                        intent.putExtra(EXTRA_TOTAL, Integer.toString(total));
                        intent.putExtra(EXTRA_TOTAL_CORRECT, Integer.toString(totalSaidCorrect) );
                        intent.putExtra(EXTRA_TOTAL_MISSING, Integer.toString(totalMissing) );
                        intent.putExtra(EXTRA_TOTAL_MISTAKE, Integer.toString(totalMistake) );
                        intent.putExtra(EXTRA_TOTAL_SAID, Integer.toString(totalSaid) );
                        startActivity(intent);


                        stopVoiceRecorder();
                        changeStatus(false);



                    }
                }
            });

        }

    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;

    }

    @Override
    public void onComplete() {

    }

    public List<String> changeForAdpter(List<Content> contentList){
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < contentList.size(); i++){
            stringList.add(contentList.get(i).getContent_english());
        }
        return stringList;
    }

    public List<String> changeForAdpter(ArrayList<String> stringArrayList){
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < stringArrayList.size(); i++){
            stringList.add(stringArrayList.get(i).toString());
        }
        return stringList;
    }



    private static class ContentAdapter extends RecyclerView.Adapter<SpeechFragment.ContentAdapter.ViewHolder> {

        private List<Content> mContent;

        public ContentAdapter(List<Content> contents) {
            setList(contents);
        }


        @Override
        public SpeechFragment.ContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mContext = parent.getContext();


            LayoutInflater inflater = LayoutInflater.from(mContext);
            View noteView = inflater.inflate(R.layout.item_speech, parent, false);

            return new SpeechFragment.ContentAdapter.ViewHolder(noteView);
        }

        @Override
        public void onBindViewHolder(SpeechFragment.ContentAdapter.ViewHolder viewHolder, int position) {
            Content content = mContent.get(position);

            viewHolder.mContentEnglish.setText(content.getContent_english());

            if (position == mPosition) {
                viewHolder.mContentEnglish.setTypeface(null, Typeface.BOLD);
                viewHolder.mContentEnglish.setTextSize(mContext.getResources().getDimension(R.dimen.text_big));
            } else {
                viewHolder.mContentEnglish.setTypeface(null, Typeface.NORMAL);
                viewHolder.mContentEnglish.setTextSize(mContext.getResources().getDimension(R.dimen.text_normal));
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


            public ViewHolder(View itemView) {
                super(itemView);
                mContentEnglish = (TextView) itemView.findViewById(R.id.tv_content_english);
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

    private String nextSpace(String phrase, TextView TextView) {
        int i = 0;
        int count = 0;
        int countWord = 1;


        while (i < TextView.getText().toString().length()) {

            if ((TextView.getText().toString().toString().substring(i, i + 1).indexOf(" ") == 0)) {
                countWord++;
            }
            i++;
        }

        i = 0;
        while (i < phrase.length()) {

            if ((phrase.toString().substring(i, i + 1).indexOf(" ") == 0)) {
                count++;
                if (count > countWord) {
                    return phrase.substring(0, i);
                }
            }
            i++;
        }
        return phrase.substring(0, i);

    }

    private VoiceRecorder mVoiceRecorder;
    private final VoiceRecorder.Callback mVoiceCallback = new VoiceRecorder.Callback() {

        @Override
        public void onVoiceStart() {
            if (mSpeechService != null) {
                mSpeechService.startRecognizing(mVoiceRecorder.getSampleRate());
                showStatus(true);
            }
        }

        @Override
        public void onVoice(byte[] data, int size) {
            if (mSpeechService != null) {
                mSpeechService.recognize(data, size);
            }
        }

        @Override
        public void onVoiceEnd() {
            showStatus(false);
            if (mSpeechService != null) {
                mSpeechService.finishRecognizing();
                onNext();
            }
        }

    };

    private final SpeechService.Listener mSpeechServiceListener =
            new SpeechService.Listener() {
                @Override
                public void onSpeechRecognized(final String text, final boolean isFinal) {
                    if (isFinal) {
                        if(mVoiceRecorder != null) {
                            mVoiceRecorder.dismiss();
                        }
                    }
                    if (mText != null && !TextUtils.isEmpty(text)) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isFinal) {
                                    mText.setText(null);
                                    mAdapter.addResult(text);
                                    mRecyclerViewSpeech.smoothScrollToPosition(0);
                                } else {
                                    mText.setText(text);
                                }
                            }
                        });
                    }
                }
            };

    private static class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_result, parent, false));
            text = (TextView) itemView.findViewById(R.id.text);
        }

    }

    private static class ResultAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final ArrayList<String> mResults = new ArrayList<>();

        ResultAdapter(ArrayList<String> results) {
            if (results != null) {
                mResults.addAll(results);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
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

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            mSpeechService = SpeechService.from(binder);
            mSpeechService.addListener(mSpeechServiceListener);
            mStatus.setVisibility(View.VISIBLE);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mSpeechService = null;
        }

    };

    @Override
    public void onStart() {
        super.onStart();


        // Prepare Cloud Speech API
        this.getActivity().bindService(new Intent(this.getActivity(), SpeechService.class), mServiceConnection, BIND_AUTO_CREATE);

        // Start listening to voices
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            startVoiceRecorder();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.RECORD_AUDIO)) {
            showPermissionMessageDialog();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }

    @Override
    public void onStop() {
        // Stop listening to voice
        stopVoiceRecorder();

        // Stop Cloud Speech API
        mSpeechService.removeListener(mSpeechServiceListener);
        getActivity().unbindService(mServiceConnection);
        mSpeechService = null;

        super.onStop();
    }

    private void startVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
        }
        mVoiceRecorder = new VoiceRecorder(mVoiceCallback);
        mVoiceRecorder.start();


    }

    private void stopVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
            mVoiceRecorder = null;
        }
        changeStatus(false);

    }

    private void showPermissionMessageDialog() {
        MessageDialogFragment
                .newInstance(getString(R.string.permission_message))
                .show(getActivity().getSupportFragmentManager(), FRAGMENT_MESSAGE_DIALOG);
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
            mFloatingActionButton.setImageResource(R.drawable.ic_mic_white_24dp);
            mStatus.setVisibility(View.GONE);
            statusPlay = true;
            mPresenter.playAudio(storiesProgressView);
        }else{
            mLoadingGif.setVisibility(View.GONE);
            mFloatingActionButton.setImageResource(R.drawable.ic_mic_off_white_24dp);
            mStatus.setVisibility(View.VISIBLE);
            statusPlay = false;
            mPresenter.pauseAudio(storiesProgressView);
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
            myList = ListListen;
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



}

