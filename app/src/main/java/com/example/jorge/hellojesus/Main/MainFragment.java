package com.example.jorge.hellojesus.Main;

import android.content.Context;
import android.content.Intent;
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
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.data.onLine.main.MainServiceImpl;
import com.example.jorge.hellojesus.data.onLine.main.model.Main;
import com.example.jorge.hellojesus.data.onLine.topic.model.Topic;
import com.example.jorge.hellojesus.topic.TopicActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 22/02/2018.
 */

public class MainFragment extends Fragment implements MainContract.View {

    public static String EXTRA_MAIN = "EXTRA_MAIN";
    public static String EXTRA_BUNDLE_MAIN = "EXTRA_BUNDLE_MAIN";


    private MainContract.UserActionsListener mPresenter;

    private MainAdapter mListAdapter;
    private RecyclerView mRecyclerView;

    private Main mMain;

    private int mPosition = 0;


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


    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadingMain();
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

        public MainAdapter(List<Main> main, ItemListener itemListener) {
            setList(main);
            mItemListener = itemListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
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

            public TextView mName;
            public TextView mTime;
            public TextView mDescription;


            private ItemListener mItemListener;

            public ViewHolder(View itemView, ItemListener listener) {
                super(itemView);
                mItemListener = listener;
                mName = (TextView) itemView.findViewById(R.id.tv_main_name);
                mTime = (TextView) itemView.findViewById(R.id.tv_main_time);
                mDescription = (TextView) itemView.findViewById(R.id.tv_main_description);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Main product = getItem(position);
                mItemListener.onMainClick(product);

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

