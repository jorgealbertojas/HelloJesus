package com.example.jorge.hellojesus.content;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.data.onLine.main.MainServiceImpl;
import com.example.jorge.hellojesus.data.onLine.main.model.Main;
import com.example.jorge.hellojesus.data.onLine.topic.model.Content;
import com.example.jorge.hellojesus.main.MainContract;
import com.example.jorge.hellojesus.main.MainFragment;
import com.example.jorge.hellojesus.main.MainPresenter;
import com.example.jorge.hellojesus.topic.TopicActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 27/02/2018.
 */

public class ContentFragment extends Fragment implements ContentContract.View {

    public static String EXTRA_MAIN = "EXTRA_MAIN";
    public static String EXTRA_BUNDLE_MAIN = "EXTRA_BUNDLE_MAIN";


    private ContentContract.UserActionsListener mPresenter;

    private ContentFragment.ContentAdapter mListAdapter;
    private RecyclerView mRecyclerView;

    public static List<Content> mContents;

    private int mPosition = 0;

    private Animation mShowFab;
    private Animation mHideFab;
    private FloatingActionButton mFloatingActionButton;

    private Boolean mFabMenuOpen = false;

    private LinearLayout mLinearLayout;



    public ContentFragment() {
    }

    public static ContentFragment newInstance(List<Content> contents) {
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
        mPresenter.loadingContent(mContents);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mShowFab = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab_show);
        mHideFab = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab_hide);


        View root = inflater.inflate(R.layout.fragment_content, container, false);

        mLinearLayout = (LinearLayout) root.findViewById(R.id.fabContainerLayout);

        mFloatingActionButton = (FloatingActionButton) root.findViewById(R.id.fab_function);

        mPresenter.ShowFabButton(mFloatingActionButton, mShowFab);

      //  mPresenter.HideFabButton(mFloatingActionButton, mHideFab);




        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mPresenter.loadingContent(mContents);
            }
        });


        // Set up floating action button
   //     FloatingActionButton fab =
       //         (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);

        mFloatingActionButton.setImageResource(R.drawable.ic_chevron_right_white_24dp);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMenu();



            }
        });


        mPresenter.loadingContent(mContents);

        initRecyclerView(root);


        return root;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void toggleFabMenu() {
        if (!mFabMenuOpen) {
            mFloatingActionButton.setImageResource(R.drawable.ic_forward_30_white_24dp);
            int centerX = mLinearLayout.getWidth() / 2;
            int centerY = mLinearLayout.getHeight() / 2;
            int startRadius = 0;
            int endRadius = (int) Math.hypot(mLinearLayout.getWidth(), mLinearLayout.getHeight()) / 2;

            mLinearLayout.setVisibility(View.VISIBLE);
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
            mFloatingActionButton.setImageResource(R.drawable.ic_chevron_right_white_24dp);
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






    @Override
    public void showContent(List<Content> contents) {
        mListAdapter.replaceData(contents);
    }

    @Override
    public void showAllContent() {

    }


    private static class ContentAdapter extends RecyclerView.Adapter<ContentFragment.ContentAdapter.ViewHolder> {

        private List<Content> mContent;
        private ContentFragment.ItemListener mItemListener;

        public ContentAdapter(List<Content> contents, ContentFragment.ItemListener itemListener) {
            setList(contents);
            mItemListener = itemListener;
        }

        @Override
        public ContentFragment.ContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View noteView = inflater.inflate(R.layout.item_content, parent, false);

            return new ContentFragment.ContentAdapter.ViewHolder(noteView, mItemListener);
        }

        @Override
        public void onBindViewHolder(ContentFragment.ContentAdapter.ViewHolder viewHolder, int position) {
            Content content = mContent.get(position);

            viewHolder.mContentEnglish.setText(content.getContent_english());
            viewHolder.mContentPortuguese.setText(content.getContent_portuguese());

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


}


