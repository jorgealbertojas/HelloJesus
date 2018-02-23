package com.example.jorge.hellojesus.topic.fragmentTab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.data.onLine.topic.TopicServiceImpl;
import com.example.jorge.hellojesus.data.onLine.topic.model.Topic;
import com.example.jorge.hellojesus.topic.TopicActivity;
import com.example.jorge.hellojesus.topic.TopicContract;
import com.example.jorge.hellojesus.topic.TopicPresenter;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorge on 23/02/2018.
 */

public class BibleFragment extends Fragment implements TopicContract.View {

    public static String EXTRA_PRODUCT = "PRODUCT";
    public static String EXTRA_BUNDLE_PRODUCT = "BUNDLE_PRODUCT";

    private TopicContract.UserActionsListener mActionsListener;

    private BibleFragment.TopicsAdapter mListAdapter;
    private RecyclerView mRecyclerView;

    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "RECYCLER_VIEW_STATE";
    private Parcelable mListState;

    private TopicContract.UserActionsListener mPresenter;

    public BibleFragment() {
    }

    public static BibleFragment newInstance() {
        return new BibleFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new BibleFragment.TopicsAdapter(new ArrayList<Topic>(0), mItemListener);
        mActionsListener = new TopicPresenter(new TopicServiceImpl(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.loadingTopic();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View root = inflater.inflate(R.layout.fragment_topics, container, false);

        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mActionsListener.loadingTopic();
            }
        });






        return root;
    }




    BibleFragment.ItemListener mItemListener = new BibleFragment.ItemListener() {
        @Override
        public void onTopicClick(Topic product) {

            mActionsListener.openDetail();
        }
    };

    @Override
    public void setLoading(final boolean isActive) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(isActive);
            }
        });
    }

    @Override
    public void showTopic(List<Topic> productList) {
        mListAdapter.replaceData(productList);
    }

    @Override
    public void showAllTopics() {
        Intent intent = new Intent(getActivity(), TopicActivity.class);

        //Bundle bundle = new Bundle();
        // bundle.putSerializable(EXTRA_PRODUCT, null);

        //intent.putExtra(EXTRA_BUNDLE_PRODUCT, bundle);
        startActivity(intent);
    }



    private static class TopicsAdapter extends RecyclerView.Adapter<BibleFragment.TopicsAdapter.ViewHolder> {

        private List<Topic> mTopics;
        private BibleFragment.ItemListener mItemListener;

        public TopicsAdapter(List<Topic> products, BibleFragment.ItemListener itemListener) {
            setList(products);
            mItemListener = itemListener;
        }

        @Override
        public BibleFragment.TopicsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View noteView = inflater.inflate(R.layout.item_topic, parent, false);

            return new BibleFragment.TopicsAdapter.ViewHolder(noteView, mItemListener);
        }

        @Override
        public void onBindViewHolder(BibleFragment.TopicsAdapter.ViewHolder viewHolder, int position) {
            Topic product = mTopics.get(position);

          /*  Picasso.with(viewHolder.productImage.getContext())
                    .load(product.getUrl_image_small())
                    .fit().centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(viewHolder.productImage);*/

            viewHolder.productName.setText(product.getName());
        }

        public void replaceData(List<Topic> notes) {
            setList(notes);
            notifyDataSetChanged();
        }

        private void setList(List<Topic> notes) {
            mTopics = notes;
        }

        @Override
        public int getItemCount() {
            return mTopics.size();
        }

        public Topic getItem(int position) {
            return mTopics.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView productImage;
            public TextView productName;
            private BibleFragment.ItemListener mItemListener;

            public ViewHolder(View itemView, BibleFragment.ItemListener listener) {
                super(itemView);
                mItemListener = listener;
                // productName= (TextView) itemView.findViewById(R.id.tv_topic_name);
                //productImage = (ImageView) itemView.findViewById(R.id.im_product_image);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                //   Topic product = getItem(position);
                //   mItemListener.onTopicClick(product);

                //   Intent intent = new Intent(v.getContext(), ShoppingActivity.class);

                //   Bundle bundle = new Bundle();
                //   bundle.putSerializable(EXTRA_PRODUCT, product );

                //   intent.putExtra(EXTRA_BUNDLE_PRODUCT, bundle);
                //   v.getContext().startActivity(intent);



            }
        }
    }

    public interface ItemListener {

        void onTopicClick(Topic clickedNote);
    }
}

