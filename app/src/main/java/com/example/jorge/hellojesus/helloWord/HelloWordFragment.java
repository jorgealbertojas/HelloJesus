package com.example.jorge.hellojesus.helloWord;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jorge.hellojesus.Injection;
import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.data.local.helloWord.HelloWord;
import com.example.jorge.hellojesus.data.onLine.topic.model.Content;
import com.example.jorge.hellojesus.tipWord.TipWordActivity;
import com.example.jorge.hellojesus.word.WordFragment;

import java.util.List;
import java.util.Random;


public class HelloWordFragment extends Fragment implements HelloWordContract.View{

    public static String EXTRA_WORD = "WORD";
    public static String EXTRA_BUNDLE_WORD = "BUNDLE_WORD";

    private static HelloWordContract.UserActionsListener mActionsListener;

    private HelloWordFragment.HelloWordAdapter mListAdapter;

    private static final int PERMISSION_REQUEST_CODE = 1;

    private TextView mTotalDown;
    private TextView mTotalUp;

    private RecyclerView mRecyclerView;

    public static List<HelloWord> mWords;



    private static Context mContext;



    private static LinearLayout llcontainer;

    long pressTime = 0;
    long limit = 5000;

    public HelloWordFragment() {
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

    public static HelloWordFragment newInstance(List<HelloWord> stringList) {
        mWords = stringList;
        return new HelloWordFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionsListener = new HelloWordPresenter(this, Injection.provideWordsRepository(getActivity().getApplicationContext()));



        mListAdapter = new HelloWordFragment.HelloWordAdapter(mWords);

    }


    @Override
    public void onResume() {
        super.onResume();
        //   mActionsListener.loadingWords(mWords,getResources().getString(R.string.key_music_write),"true");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View root = inflater.inflate(R.layout.fragment_hello_word, container, false);

        mTotalDown = (TextView) root.findViewById(R.id.tv_down);
        mTotalUp = (TextView) root.findViewById(R.id.tv_up);

        llcontainer = (LinearLayout) root.findViewById(R.id.ll_container);





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

        return root;
    }




    @Override
    public void showHelloWords(List<Content> contentList) {

    }



    @Override
    public void showLoadingHelloWordError() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }


    private static class HelloWordAdapter extends RecyclerView.Adapter<HelloWordFragment.HelloWordAdapter.ViewHolder> {

        private List<HelloWord> mListString;


        public HelloWordAdapter(List<HelloWord> stringList) {
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
        public HelloWordFragment.HelloWordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mContext = parent.getContext();

            LayoutInflater inflater = LayoutInflater.from(mContext);
            View noteView = inflater.inflate(R.layout.item_hello_word, parent, false);

            return new HelloWordFragment.HelloWordAdapter.ViewHolder(noteView);
        }

        @Override
        public void onBindViewHolder(HelloWordFragment.HelloWordAdapter.ViewHolder viewHolder, int position) {
            HelloWord word = mListString.get(position);

            viewHolder.mWord.setTypeface(null, Typeface.BOLD);
            viewHolder.mWord.setText(word.getMwordName());

        }


        public void replaceData(List<HelloWord> notes) {
            setList(notes);
            notifyDataSetChanged();
        }

        private void setList(List<HelloWord> notes) {
            mListString = notes;
        }

        @Override
        public int getItemCount() {
            return mListString.size();
        }

        public HelloWord getItem(int position) {
            return mListString.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView mWord;
            public ImageView mOpenTip;
            public ImageView mWordOK;

            private int[] locationInScreen = new int[]{0,0};
            private int lastPositionX = 0;
            private int lastPositionY = 0;

            private String EXTRA_X = "EXTRA_X";
            private String EXTRA_Y = "EXTRA_Y";
            private String EXTRA_WIDTH = "EXTRA_WIDTH";
            private String EXTRA_HELLO_WORD = "EXTRA_HELLO_WORD";

            public ViewHolder(View itemView) {
                super(itemView);
                mWord = (TextView) itemView.findViewById(R.id.tv_word);
                mWord.setTypeface(null, Typeface.NORMAL);

                mWordOK = (ImageView) itemView.findViewById(R.id.iv_up);
                mWordOK.setTag("0");
                mWordOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getTag().equals("0")) {
                            v.setTag("1");
                            RotateAnimation rotate = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            rotate.setDuration(1000);
                            rotate.setInterpolator(new LinearInterpolator());
                            v.startAnimation(rotate);
                            ((ImageView) v).setImageResource(R.drawable.ic_thumb_down_white_24dp);
                            ((ImageView) v).setColorFilter(v.getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN);


                        }else{
                            v.setTag("0");
                            RotateAnimation rotate = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            rotate.setDuration(1000);
                            rotate.setInterpolator(new LinearInterpolator());
                            v.startAnimation(rotate);
                            ((ImageView) v).setImageResource(R.drawable.ic_thumb_up_white_24dp);
                            ((ImageView) v).setColorFilter(v.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                        }

                    }
                });


                mOpenTip = (ImageView) itemView.findViewById(R.id.iv_down);

                mOpenTip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), TipWordActivity.class);

                        if (v != null){
                            v.getLocationInWindow(locationInScreen);
                            lastPositionX = locationInScreen[0];
                            lastPositionY = locationInScreen[1];
                            locationInScreen[0] = locationInScreen[0] + v.getMeasuredWidth() / 2;
                            locationInScreen[1] = locationInScreen[1] + llcontainer.getMeasuredHeight() / 2;
                        }else{
                            locationInScreen[0] = lastPositionX;
                            locationInScreen[1] = lastPositionY;
                        }

                        intent.putExtra(EXTRA_X, locationInScreen[0]);
                        intent.putExtra(EXTRA_Y, locationInScreen[1]);
                        intent.putExtra(EXTRA_WIDTH,v.getWidth()/2);
                        intent.putExtra(EXTRA_HELLO_WORD,(Parcelable) mListString.get(getAdapterPosition()));
                        v.getContext().startActivity(intent);

                    }
                });



                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                HelloWord word = getItem(position);
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

            ShowInformationHelloWord();

        }
    };

    private void initRecyclerView(View root){
        mRecyclerView= (RecyclerView) root.findViewById(R.id.rv_content_list);
        mRecyclerView.setAdapter(mListAdapter);

        int numColumns = 1;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));

    }



    @Override
    public void ShowInformationHelloWord(){
        mTotalDown.setText(mListAdapter.getItemCount());
        mTotalUp.setText(mListAdapter.getItemCount());

    }

}
