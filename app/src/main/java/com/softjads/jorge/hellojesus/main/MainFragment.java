package com.softjads.jorge.hellojesus.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softjads.jorge.hellojesus.Injection;
import com.softjads.jorge.hellojesus.R;
import com.softjads.jorge.hellojesus.data.local.help.Help;
import com.softjads.jorge.hellojesus.data.onLine.main.MainServiceImpl;
import com.softjads.jorge.hellojesus.data.onLine.main.model.Main;
import com.softjads.jorge.hellojesus.helpApp.AppHelp;
import com.softjads.jorge.hellojesus.topic.TopicActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;

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

    private static MainContract.UserActionsListener mPresenter;

    private MainAdapter mListAdapter;
    public static Activity mActivity;

    private Main mMain;

    private static ShimmerLayout shimmerText;

    private int mPosition = 0;

    private View iconView;

    public static int[] locationInScreen = new int[]{0,0};

    private ViewGroup mContainer;

    private static int lastPositionX = 0;
    private static int lastPositionY = 0;
    private static int radius = 0;

    private static boolean helpBoolean = false;


    private static FloatingActionMenu fabMenu;

    private static FloatingActionButton fabCheck;
   // private static FloatingActionButton fabWord;
    private static FloatingActionButton fabNoCheck;
    private static FloatingActionButton fabHelp;


    //private static LinearLayout Llmain ;

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        final MainFragment mainFragment = new MainFragment();
        return mainFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new MainAdapter(new ArrayList<Main>(0), mItemListener);
        mPresenter = new MainPresenter(Injection.provideWordsRepository(this.getContext()), new MainServiceImpl(getActivity()), this);
        mActivity = getActivity();


    }

    @Override
    public void onResume() {
        super.onResume();
       // mPresenter.loadingMain();
        if (helpBoolean){
            ObjectAnimator animation = ObjectAnimator.ofFloat(fabMenu, "translationX", 0);
            animation.setDuration(2000);
            animation.start();
            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    fabMenu.close(false);
                }
            });


        }
        helpBoolean = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {



        final View root = inflater.inflate(R.layout.fragment_main, container, false);

        final LinearLayout Llmain = (LinearLayout) root.findViewById(R.id.ll_main);

        shimmerText = (ShimmerLayout) root.findViewById(R.id.shimmer_layout);

        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmerText.startShimmerAnimation();
                mPresenter.loadingMain(shimmerText);
            }
        });

        swipeRefreshLayout.setProgressViewEndTarget(false,0);

        final int newColorRed = getResources().getColor(R.color.red);
        // Set up floating action button

        fabMenu = (FloatingActionMenu) getActivity().findViewById(R.id.multiple_actions_down);

        fabHelp = (FloatingActionButton) getActivity().findViewById(R.id.fab_help);
        fabHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpBoolean = true;
                ObjectAnimator animation = ObjectAnimator.ofFloat(fabMenu, "translationX", fabMenu.getHeight() * 2);
                animation.setDuration(2000);
                animation.start();
                mPresenter.loadHelp(root,getContext());
            }
        });

      //  fabWord = (FloatingActionButton) getActivity().findViewById(R.id.fab_word);
     //   fabWord.setOnClickListener(new View.OnClickListener() {
     //       @Override
     //       public void onClick(View v) {
    //            Intent intent = new Intent(getActivity(), CycleActivity.class);
    //            getActivity().startActivity(intent);
    //            getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
   //             fabMenu.close(false);
   //         }
   //     });

        fabNoCheck = (FloatingActionButton) getActivity().findViewById(R.id.fab_no_check);
        fabNoCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadWordWrong(getContext());
                fabMenu.close(false);
            }
        });

        fabCheck = (FloatingActionButton) getActivity().findViewById(R.id.fab_check);
        fabCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadWordCorrect(getContext());
                fabMenu.close(false);
            }
        });

        mPresenter.loadingMain(shimmerText);
        // Insert HELP for all Screen
        loadSessionConfig();

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

            final int newColor = res.getColor(R.color.NoAccent2);



            viewHolder.mImage1.setImageResource(R.mipmap.ic_bible);
            viewHolder.mImage2.setImageResource(R.mipmap.ic_music);
            viewHolder.mImage3.setImageResource(R.mipmap.ic_write);
            viewHolder.mImage4.setImageResource(R.mipmap.ic_exercise);

            viewHolder.titleBible.setText(product.getTitle_bible());
            viewHolder.titleMusic.setText(product.getTitle_music());
            viewHolder.titleWrite.setText(product.getTitle_write());
            viewHolder.titleExercise.setText(product.getTitle_exercise());

            viewHolder.mImageList.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
            viewHolder.mImage1.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
            viewHolder.mImage2.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
            viewHolder.mImage3.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
            viewHolder.mImage4.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);


            mPresenter.getControlStatus1(product.getName(), viewHolder.mImage1,context,"1");
            mPresenter.getControlStatus1(product.getName(), viewHolder.mImage2,context,"2");
            mPresenter.getControlStatus1(product.getName(), viewHolder.mImage3,context,"3");
            mPresenter.getControlStatus1(product.getName(), viewHolder.mImage4,context,"4");

            mPresenter.getControlStatusAll(product.getName(), viewHolder.mImageList,context);
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

                titleBible = (TextView) itemView.findViewById(R.id.tv_title4);
                titleMusic = (TextView) itemView.findViewById(R.id.tv_title3);
                titleWrite = (TextView) itemView.findViewById(R.id.tv_title2);
                titleExercise = (TextView) itemView.findViewById(R.id.tv_title1);

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
        final RecyclerView mRecyclerView= (RecyclerView) root.findViewById(R.id.rv_main_list);
        mRecyclerView.setAdapter(mListAdapter);

        int numColumns = 1;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));
    }

    protected String readJsonFile(InputStream inputStream) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte bufferByte[] = new byte[1024];
        int length;
        try {
            while ((length = inputStream.read(bufferByte)) != -1) {
                outputStream.write(bufferByte, 0, length);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
        return outputStream.toString();
    }

    private void loadSessionConfig(){
        try {

            Gson gson = new Gson();
            InputStream inputStream = this.getActivity().getAssets().open("config_help.json");
            AppHelp appHelp = (gson.fromJson(readJsonFile(inputStream), AppHelp.class));

            for (int i = 0;  i < appHelp.getConfigHelp().size()  ;i ++ ){
                Help help = new Help(appHelp.getConfigHelp().get(i).getMkey(),appHelp.getConfigHelp().get(i).getMvalue(), appHelp.getConfigHelp().get(i).getMlast());
                mPresenter.saveHelp(help);
            }
        } catch (Exception e) {
            //   errorLog(e.toString());
        }
    }
}

