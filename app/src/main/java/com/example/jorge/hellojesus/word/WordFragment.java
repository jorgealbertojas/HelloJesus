package com.example.jorge.hellojesus.word;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.jorge.hellojesus.Injection;
import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.data.onLine.topic.model.Content;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import jp.shts.android.storiesprogressview.StoriesProgressView;

/**
 * Created by jorge on 21/04/2018.
 */




public class WordFragment extends Fragment implements WordContract.View {

    public static String EXTRA_WORD = "WORD";
    public static String EXTRA_BUNDLE_WORD = "BUNDLE_WORD";

    private static WordContract.UserActionsListener mActionsListener;

    private WordFragment.WordAdapter mListAdapter;

    private static final int PERMISSION_REQUEST_CODE = 1;

    private TextView mTotalDown;
    private TextView mTotalUp;




    private RecyclerView mRecyclerView;

    public static List<String> mWords;
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





    long pressTime = 0;
    long limit = 5000;




    public WordFragment() {
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

    public static WordFragment newInstance(List<String> stringList) {
        mWords = stringList;
        return new WordFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionsListener = new WordPresenter(this, Injection.provideWordsRepository(getActivity().getApplicationContext()));

        mListAdapter = new WordFragment.WordAdapter(mWords);

    }


    @Override
    public void onResume() {
        super.onResume();
     //   mActionsListener.loadingWords(mWords,getResources().getString(R.string.key_music_write),"true");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mShowFab = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab_show);
        mHideFab = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab_hide);
        mRotateFab = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab_rotate);

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View root = inflater.inflate(R.layout.fragment_word, container, false);

        mLinearLayout = (LinearLayout) root.findViewById(R.id.fabContainerLayout);

        mFloatingActionButton = (FloatingActionButton) root.findViewById(R.id.fab_function);
        mFabImage = (FloatingActionButton) root.findViewById(R.id.fab_image);
        mFabExplanation = (FloatingActionButton) root.findViewById(R.id.fab_explanation);
        mFabTranslate = (FloatingActionButton) root.findViewById(R.id.fab_translate);

        mTotalDown = (TextView) root.findViewById(R.id.tv_down);
        mTotalUp = (TextView) root.findViewById(R.id.tv_up);



        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

               // mActionsListener.loadingWords(mWords,getResources().getString(R.string.key_music_write),"false");
            }
        });






        initRecyclerView(root);

        requestPermission();


        return root;
    }






    /**
     * Request Permission download for the user .
     */
    private void requestPermission(){

        ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }

    @Override
    public void showWord(List<String> stringList) {
        mListAdapter.replaceData(stringList);
    }


    @Override
    public void showAllWord() {

    }

    @Override
    public void showWords(List<Content> contentList) {

    }

     @Override
    public void showLoadingWordError() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }


    private static class WordAdapter extends RecyclerView.Adapter<WordFragment.WordAdapter.ViewHolder> {

        private List<String> mListString;


        public WordAdapter(List<String> stringList) {
            setList(stringList);

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
        public WordFragment.WordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mContext = parent.getContext();

            LayoutInflater inflater = LayoutInflater.from(mContext);
            View noteView = inflater.inflate(R.layout.item_word, parent, false);

            return new WordFragment.WordAdapter.ViewHolder(noteView);
        }

        @Override
        public void onBindViewHolder(WordFragment.WordAdapter.ViewHolder viewHolder, int position) {
            String word = mListString.get(position);

            viewHolder.mWord.setTypeface(null, Typeface.BOLD);
            viewHolder.mWord.setText(word);

            viewHolder.mWord.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {

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
                                mWord.setText(phase.toString().substring(index2, phase.length()));
                                String word = phase.toString().substring(index2, index1 + offset);
                                mWord.setText(word.replace(",",""));
                                mActionsListener.ShowFabButton(mFloatingActionButton, mShowFab, mWord);
                            }else{
                                mWord.setText("");
                                mActionsListener.HideFabButton(mFloatingActionButton, mHideFab, mWord);
                                mFabMenuOpen = true;

                            }
                        }
                    }
                    return true;
                }
            });


            String s = word;

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



        public void replaceData(List<String> notes) {
            setList(notes);
            notifyDataSetChanged();
        }

        private void setList(List<String> notes) {
            mListString = notes;
        }

        @Override
        public int getItemCount() {
            return mListString.size();
        }

        public String getItem(int position) {
            return mListString.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView mWord;

            public ViewHolder(View itemView) {
                super(itemView);
                mWord = (TextView) itemView.findViewById(R.id.tv_word);
                mWord.setTypeface(null, Typeface.NORMAL);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                String word = getItem(position);
              //  mItemListener.onMainClick(word);

            }
        }
    }

    public interface ItemListener {

        void onMainClick(String clickedNote);

        void onRemoveWordClick(String activatedPurchaseId, String sourceName, String write);
    }

    WordFragment.ItemListener mItemListener = new WordFragment.ItemListener() {
        @Override
        public void onMainClick(String content) {

        }

        @Override
        public void onRemoveWordClick(String activatedWordId, String sourceName, String write) {
          //  mActionsListener.removeItemWord(activatedWordId, sourceName, write);

            ShowInformationWord();

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
    public void ShowInformationWord(){
        mTotalDown.setText(mListAdapter.getItemCount());
        mTotalUp.setText(mListAdapter.getItemCount());

    }

}



