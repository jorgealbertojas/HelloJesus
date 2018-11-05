package com.example.jorge.hellojesus.main;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.data.onLine.main.MainServiceImpl;
import com.example.jorge.hellojesus.data.onLine.main.model.Main;
import com.example.jorge.hellojesus.topic.TopicActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 22/02/2018.
 *
 *
 *
 *
 */



public class MainFragment extends Fragment implements MainContract.View {

    public static String EXTRA_MAIN = "EXTRA_MAIN";
    public static String EXTRA_STRING_TITLE = "EXTRA_STRING_TITLE";
    public static String EXTRA_BUNDLE_MAIN = "EXTRA_BUNDLE_MAIN";

    private MainContract.UserActionsListener mPresenter;

    private MainAdapter mListAdapter;
    public RecyclerView mRecyclerView;
    public static Activity mActivity;

    private Main mMain;

    private int mPosition = 0;

    private View iconView;

    public static int[] locationInScreen = new int[]{0,0};

    private ViewGroup mContainer;

    private static int lastPositionX = 0;
    private static int lastPositionY = 0;
    private static int radius = 0;


    public MainFragment() {
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new MainAdapter(new ArrayList<Main>(0), mItemListener);
        mPresenter = new MainPresenter(new MainServiceImpl(), this);
        mActivity = getActivity();


    }

    @Override
    public void onResume() {
        super.onResume();
       // mPresenter.loadingMain();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {



        View root = inflater.inflate(R.layout.fragment_main, container, false);


        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mPresenter.loadingMain();
            }
        });


        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);

        // fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMain != null){
                    openViewTopic(mMain);


                }



            }
        });


        mPresenter.loadingMain();

        initRecyclerView(root);



        return root;
    }





    @Override
    public void openViewTopic(Main main) {
        Intent intent = new Intent(getActivity(), TopicActivity.class);

        List<Integer> ii = main.getTopics();

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_MAIN, (Serializable) main.getTopics());
        intent.putExtra(EXTRA_BUNDLE_MAIN, bundle);
        startActivity(intent);

    }


    @Override
    public void setPresenter(MainContract.UserActionsListener presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showMain(List<Main> mainList) {
        mListAdapter.replaceData(mainList);
    }


    private static class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

        private List<Main> mMains;
        private ItemListener mItemListener;
        Context context;

        public MainAdapter(List<Main> main, ItemListener itemListener) {
            setList(main);
            mItemListener = itemListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View noteView = inflater.inflate(R.layout.item_main, parent, false);

            return new ViewHolder(noteView, mItemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            Main product = mMains.get(position);

            viewHolder.mName.setText(product.getName());
            viewHolder.mTime.setText(product.getTime());
            viewHolder.mDescription.setText(product.getDescripition());

            viewHolder.mDescription.setText(product.getDescripition());
            viewHolder.mDescription.setText(product.getDescripition());
            viewHolder.mDescription.setText(product.getDescripition());
            viewHolder.mDescription.setText(product.getDescripition());

            viewHolder.mDescription.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Regular.ttf"));

            Resources res = context.getResources();

            viewHolder.mImage1.setTransitionName("imagem1");
            viewHolder.mImage2.setTransitionName("imagem2");
            viewHolder.mImage3.setTransitionName("imagem3");
            viewHolder.mImage4.setTransitionName("imagem4");
            viewHolder.mName.setTransitionName("mName");

            viewHolder.mImage1.setImageResource(R.mipmap.ic_bible);
            viewHolder.mImage2.setImageResource(R.mipmap.ic_music);
            viewHolder.mImage3.setImageResource(R.mipmap.ic_write);
            viewHolder.mImage4.setImageResource(R.mipmap.ic_exercise);

            viewHolder.titleBible.setText(product.getTitle_bible());
            viewHolder.titleMusic.setText(product.getTitle_music());
            viewHolder.titleWrite.setText(product.getTitle_write());
            viewHolder.titleExercise.setText(product.getTitle_exercise());


            final int newColor = res.getColor(R.color.NoAccent2);
            viewHolder.mImageList.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
            viewHolder.mImage1.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
            viewHolder.mImage2.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
            viewHolder.mImage3.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
            viewHolder.mImage4.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        }

        public void replaceData(List<Main> notes) {
            setList(notes);
            notifyDataSetChanged();
        }

        private void setList(List<Main> notes) {
            mMains = notes;
        }

        @Override
        public int getItemCount() {
            return mMains.size();
        }

        public Main getItem(int position) {
            return mMains.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public View mViewTitle;
            public TextView mName;
            public TextView mTime;
            public TextView mDescription;
            public ImageView mImageList;
            public ImageView mImage1;
            public ImageView mImage2;
            public ImageView mImage3;
            public ImageView mImage4;
            public CardView mCardView;
            private ItemListener mItemListener;
            public TextView titleBible;
            public TextView titleMusic;
            public TextView titleWrite;
            public TextView titleExercise;


            public ViewHolder(View itemView, ItemListener listener) {
                super(itemView);
                mItemListener = listener;
                mViewTitle = (View) itemView.findViewById(R.id.v_view_title);

                mName = (TextView) itemView.findViewById(R.id.tv_main_name);
                mTime = (TextView) itemView.findViewById(R.id.tv_main_time);
                mDescription = (TextView) itemView.findViewById(R.id.tv_main_description);

                mImageList = (ImageView) itemView.findViewById(R.id.iv_image_list);
                mImage1 = (ImageView) itemView.findViewById(R.id.imageView);
                mImage2 = (ImageView) itemView.findViewById(R.id.imageView2);
                mImage3 = (ImageView) itemView.findViewById(R.id.imageView3);
                mImage4 = (ImageView) itemView.findViewById(R.id.imageView4);

                titleBible = (TextView) itemView.findViewById(R.id.tv_title1);
                titleMusic = (TextView) itemView.findViewById(R.id.tv_title2);
                titleWrite = (TextView) itemView.findViewById(R.id.tv_title3);
                titleExercise = (TextView) itemView.findViewById(R.id.tv_title4);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Main main = getItem(position);
                mItemListener.onMainClick(main);

                //getPositionView(v);

                Intent intent = new Intent(context, TopicActivity.class);

                Bundle bundle =  new Bundle();

                // Define the view that the animation will start from
                View viewStart = mCardView;
                String transitionName = "teste";

                Pair<View, String> p1 = Pair.create((View) mImage1, "imagem1");
                Pair<View, String> p2 = Pair.create((View) mImage2, "imagem2");
                Pair<View, String> p3 = Pair.create((View) mImage3, "imagem3");
                Pair<View, String> p4 = Pair.create((View) mImage4, "imagem4");
                Pair<View, String> p6 = Pair.create((View) mName, "mName");

                ActivityOptionsCompat options =

                        ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                                p1,
                                p2,
                                p3,
                                p4,
                                p6);   // The String

                List<Integer> ii = main.getTopics();


                bundle.putSerializable(EXTRA_MAIN, (Serializable)   main.getTopics());
                intent.putExtra(EXTRA_STRING_TITLE,   main.getName());
                intent.putExtra(EXTRA_BUNDLE_MAIN, bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityCompat.startActivity(mActivity, intent,options.toBundle());
            }


            public void getPositionView(View iconView){

                if (iconView != null){
                    radius = iconView.getWidth() / 2;
                }

                iconView.getLocationInWindow(locationInScreen);
                lastPositionX = locationInScreen[0];
                lastPositionY = locationInScreen[1];
                iconView.getLocationInWindow(locationInScreen);


                if (iconView != null) {
                    locationInScreen[0] = locationInScreen[0] + iconView.getMeasuredWidth() / 2;
                    lastPositionX = locationInScreen[0];
                }else{
                    locationInScreen[0] = lastPositionX;
                    locationInScreen[1] = lastPositionY;
                }
            }
        }
    }

    public interface ItemListener {

        void onMainClick(Main clickedNote);
    }

    ItemListener mItemListener = new ItemListener() {
        @Override
        public void onMainClick(Main main) {

           mMain = main;
        }
    };

    private void initRecyclerView(View root){
        mRecyclerView= (RecyclerView) root.findViewById(R.id.rv_main_list);
        mRecyclerView.setAdapter(mListAdapter);

        int numColumns = 1;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));
    }
}

