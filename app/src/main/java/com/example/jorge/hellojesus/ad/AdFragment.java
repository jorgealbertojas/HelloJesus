package com.example.jorge.hellojesus.ad;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jorge.hellojesus.Injection;
import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.data.onLine.ad.AdServiceImpl;
import com.example.jorge.hellojesus.data.onLine.ad.model.Ad;

import com.example.jorge.hellojesus.main.MainContract;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;

import static com.google.common.base.Preconditions.checkNotNull;

public class AdFragment extends Fragment implements AdContract.View {


    private static AdContract.UserActionsListener mPresenter;

    private AdFragment.AdAdapter mListAdapter;
    public RecyclerView mRecyclerView;
    public static Activity mActivity;

    private Ad mAd;

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

    private static ShimmerLayout shimmerText;


    private static LinearLayout Llad ;

    public AdFragment() {
    }

    public static AdFragment newInstance() {
        return new AdFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new AdFragment.AdAdapter(new ArrayList<Ad>(0), mItemListener);
        mPresenter = new AdPresenter(Injection.provideWordsRepository(this.getContext()),new AdServiceImpl(), this);
        mActivity = getActivity();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {



        final View root = inflater.inflate(R.layout.fragment_ad, container, false);

        Llad = (LinearLayout) root.findViewById(R.id.ll_ad);

        shimmerText = (ShimmerLayout) root.findViewById(R.id.shimmer_layout);


        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmerText.startShimmerAnimation();
                mPresenter.loadingAd(shimmerText);
            }
        });



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



        mPresenter.loadingAd(shimmerText);

        initRecyclerView(root);

        return root;
    }

    @Override
    public void setPresenter(AdContract.UserActionsListener presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showAd(List<Ad> adList) {

        mListAdapter.replaceData(adList);
    }




    private static class AdAdapter extends RecyclerView.Adapter<AdFragment.AdAdapter.ViewHolder> {

        private List<Ad> mAds;
        private AdFragment.ItemListener mItemListener;
        Context context;

        public AdAdapter(List<Ad> ad, AdFragment.ItemListener itemListener) {
            setList(ad);
            mItemListener = itemListener;
        }

        @Override
        public AdFragment.AdAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View noteView = inflater.inflate(R.layout.item_ad, parent, false);

            return new AdFragment.AdAdapter.ViewHolder(noteView, mItemListener);
        }

        @Override
        public void onBindViewHolder(AdFragment.AdAdapter.ViewHolder viewHolder, int position) {
            Ad ad = mAds.get(position);

            viewHolder.mName.setText(ad.getName());
            viewHolder.mDescription.setText(ad.getText());
            viewHolder.mDataStart.setText(context.getResources().getString(R.string.ad_start) + ad.getStar_date());
            viewHolder.mDataEnd.setText(context.getResources().getString(R.string.ad_start) + ad.getExpiration_date());
            viewHolder.mTitle4.setText(ad.getDescription());

            if (!ad.getLink().equals("")) {
                viewHolder.mCardView.setTag(ad.getLink());
            }else{
                viewHolder.mCardView.setTag("0");
            }


            viewHolder.mDescription.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Regular.ttf"));

            if (ad.getUrl_Image() != null){
                Picasso.with(context)
                        .load(ad.getUrl_Image())
                        .resize(600,600)
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .into(viewHolder.mImage4);
            }else{
                viewHolder.mImage4.setVisibility(View.GONE);
            }

            Resources res = context.getResources();


        }



        public void replaceData(List<Ad> notes) {
            setList(notes);
            notifyDataSetChanged();
        }



        private void setList(List<Ad> notes) {
            mAds = notes;
        }

        @Override
        public int getItemCount() {
            return mAds.size();
        }

        public Ad getItem(int position) {
            return mAds.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public View mViewTitle;
            public TextView mName;
            public TextView mDataStart;
            public TextView mDataEnd;
            public TextView mDescription;
            public ImageView mImageList;
            public ImageView mImage1;
            public ImageView mImage4;

            public CardView mCardView;
            private AdFragment.ItemListener mItemListener;
            public TextView mTitle4;



            public ViewHolder(View itemView, AdFragment.ItemListener listener) {
                super(itemView);
                mItemListener = listener;
                mViewTitle = (View) itemView.findViewById(R.id.v_view_title);

                mName = (TextView) itemView.findViewById(R.id.tv_main_nameAd);
                mDataStart = (TextView) itemView.findViewById(R.id.tv_date_start);
                mDataEnd = (TextView) itemView.findViewById(R.id.tv_date_end);
                mDescription = (TextView) itemView.findViewById(R.id.tv_main_descriptionAd);

                mImageList = (ImageView) itemView.findViewById(R.id.iv_image_list);
                mImage1 = (ImageView) itemView.findViewById(R.id.imageView);
                mImage4 = (ImageView) itemView.findViewById(R.id.imageViewAd);

                mCardView = (CardView) itemView.findViewById(R.id.card_view);


                mTitle4 = (TextView) itemView.findViewById(R.id.tv_title4);


                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                if (!((CardView) v).getTag().toString().equals("0")){
                    openBrowserLink(context,(((CardView) v).getTag().toString()));
                }




            }

        }
    }

    public interface ItemListener {
        void onAdClick(Ad clickedNote);
    }

    AdFragment.ItemListener mItemListener = new AdFragment.ItemListener() {
        @Override
        public void onAdClick(Ad ad) {

            mAd = ad;
        }
    };

    private void initRecyclerView(View root){
        mRecyclerView= (RecyclerView) root.findViewById(R.id.rv_ad_list);
        mRecyclerView.setAdapter(mListAdapter);

        int numColumns = 1;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));
    }

    public static void openBrowserLink(Context context, String link) {
        String url = link;

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.ic_arrow_back_white_24dp));

        builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        builder.setShowTitle(true);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl((Activity) context, Uri.parse(url));
    }



}


