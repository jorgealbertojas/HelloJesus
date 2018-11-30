package com.example.jorge.hellojesus.speech;

import android.Manifest;
import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jorge.hellojesus.Injection;
import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.data.onLine.topic.model.Content;
import com.example.jorge.hellojesus.progress.ProgressActivity;
import com.example.jorge.hellojesus.speech.support.MessageDialogFragment;
import com.example.jorge.hellojesus.speech.support.SpeechService;
import com.example.jorge.hellojesus.speech.support.VoiceRecorder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

import static android.content.Context.BIND_AUTO_CREATE;


/**
 * Created by jorge on 16/03/2018.
 * Fragment for speech when write the word the user Talk
 */

public class SpeechFragment extends Fragment implements SpeechContract.View, StoriesProgressView.StoriesListener, MessageDialogFragment.Listener {

    public static final String EXTRA_LIST_CONTENT = "EXTRA_LIST_CONTENT";
    public static final String EXTRA_ARRAY_LIST_STRING = "EXTRA_ARRAY_LIST_STRING";
    public static final String EXTRA_BUNDLE = "EXTRA_BUDLE";
    public static final String EXTRA_SOURCE_NAME = "EXTRA_SOURCE_NAME";
    public static final String EXTRA_TYPE = "EXTRA_TYPE";


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
    private static RecyclerView mRecyclerViewSpeech;

    private static final int PERMISSION_REQUEST_CODE = 1;

    private static SpeechContract.UserActionsListener mPresenter;

    private SpeechFragment.ContentAdapter mListAdapter;
    private RecyclerView mRecyclerView;

    public static List<Content> mContents;
    public static int mTime;
    public static String mMp3;

    private static final int PROGRESS_COUNT = 18;

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

    private static String mSourceName;

    private static boolean statusPlay = false;

    public static String mName;
    public static String mSaveStatus;

    private String mType;

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


    public static SpeechFragment newInstance(List<Content> contents, int time, String mp3, String saveStatus, String name, String sourceName) {
        mMp3 = mp3;
        mSourceName = sourceName;
        mTime = time;
        mContents = contents;
        mSaveStatus = saveStatus;
        mName = name;
        return new SpeechFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getActivity().getResources().getString(R.string.key_music_said);
        mPosition = 0;
        mListAdapter = new SpeechFragment.ContentAdapter(new ArrayList<Content>(0));
        mPresenter = new SpeechPresenter(this, Injection.provideWordsRepository(getActivity().getApplicationContext()),mSaveStatus, mName);

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
        View root = inflater.inflate(R.layout.fragment_speech, container, false);

        mLinearLayout = (LinearLayout) root.findViewById(R.id.fabContainerLayout);

        mFloatingActionButton = (FloatingActionButton) root.findViewById(R.id.fab_function);
        mFabNext = (FloatingActionButton) root.findViewById(R.id.fab_next);

        // Initialize the player view.
        mValueStart = (TextView) root.findViewById(R.id.tv_start);
        mValueEnd = (TextView) root.findViewById(R.id.tv_end);

        mValueStart.setText("1 / ");
        mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_content_list);

        mContext = getContext();

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

                }else{
                    startVoiceRecorder();

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


                        mPosition = mRecyclerViewSpeech.getAdapter().getItemCount();
                        mPosition++;
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        mRecyclerView.scrollToPosition(mPosition);
                        //mValueStart.setText(Integer.toString(mPosition + 1) + getResources().getString(R.string.format_until));
                        mRecyclerView.setSelected(true);

                       // if (mPosition == mListAdapter.mContent.size()) {

                            Intent intent = new Intent(getActivity(), ProgressActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putSerializable(EXTRA_LIST_CONTENT, (Serializable) mListAdapter.mContent);
                            bundle.putStringArrayList(EXTRA_ARRAY_LIST_STRING, mAdapter.getResults());
                            bundle.putString(EXTRA_SOURCE_NAME, mSourceName);
                            bundle.putString(EXTRA_TYPE, mType);

                            intent.putExtras(bundle);
                            startActivity(intent);

                            stopVoiceRecorder();
                            changeStatus(false);
                            getActivity().finish();

                      //  }


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
            viewHolder.mContentMyEnglish.setText(content.getContent_portuguese());

            if (position == mRecyclerViewSpeech.getAdapter().getItemCount()) {
                viewHolder.mContentEnglish.setTypeface(null, Typeface.BOLD);
                viewHolder.mContentEnglish.setTextSize(mContext.getResources().getDimension(R.dimen.text_big));
            } else {
                viewHolder.mContentEnglish.setTypeface(null, Typeface.NORMAL);
                viewHolder.mContentEnglish.setTextSize(mContext.getResources().getDimension(R.dimen.text_normal));
            }

            if (position >= mRecyclerViewSpeech.getAdapter().getItemCount()) {
                viewHolder.mContentMyEnglish.setText("");
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
                mContentMyEnglish = (TextView) itemView.findViewById(R.id.tv_content_my_english_said);
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
                                    if (mRecyclerViewSpeech.getAdapter().getItemCount() > 0) {
                                        if (mListAdapter.mContent.size() > mRecyclerViewSpeech.getAdapter().getItemCount()-1) {
                                            mListAdapter.mContent.get(mRecyclerViewSpeech.getAdapter().getItemCount() - 1).setContent_portuguese(text);
                                            mRecyclerView.smoothScrollToPosition(mRecyclerViewSpeech.getAdapter().getItemCount());
                                        }
                                    }
                                    mListAdapter.notifyDataSetChanged();
                                    mValueStart.setText(Integer.toString(mRecyclerViewSpeech.getAdapter().getItemCount() + 1) + getResources().getString(R.string.format_until));
                                    onNext();
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
        changeStatus(true);
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
                    changeStatus(false);
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


}

