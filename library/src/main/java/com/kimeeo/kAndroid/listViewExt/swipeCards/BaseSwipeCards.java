package com.kimeeo.kAndroid.listViewExt.swipeCards;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.kimeeo.kAndroid.listViewExt.R;
import com.kimeeo.kAndroid.listViews.listView.BaseListView;
import com.kimeeo.kAndroid.listViews.listView.BaseListViewAdapter;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;


import java.util.List;

/**
 * Created by bhavinpadhiyar on 1/27/16.
 */
abstract public class BaseSwipeCards extends BaseListView {

    private boolean firstItemIn = false;

    private ProgressBar mProgressBar;
    private SwipeFlingAdapterView mFlingAdapterView;
    protected void garbageCollectorCall()
    {
        super.garbageCollectorCall();
        mProgressBar=null;
        mFlingAdapterView=null;
    }

    protected View createRootView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        if(getDataProvider().getRefreshEnabled())
            return inflater.inflate(R.layout._fragment_swipe_card_view_with_swipe_refresh_layout, container, false);
        else
            return inflater.inflate(R.layout._fragment_swipe_card_view, container, false);
    }
    protected SwipeFlingAdapterView createFlingAdapterView(View rootView)
    {
        return (SwipeFlingAdapterView) rootView.findViewById(R.id.listView);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        configViewParam();
        getDataProvider().setRefreshEnabled(false);
        mRootView = createRootView(inflater, container, savedInstanceState);
        if(getDataProvider().getRefreshEnabled())
            configSwipeRefreshLayout(createSwipeRefreshLayout(mRootView));

        mFlingAdapterView = createFlingAdapterView(mRootView);
        mEmptyViewHelper = createEmptyViewHelper();
        mAdapter = createListViewAdapter();
        mAdapter.supportLoader=false;



        mFlingAdapterView.setAdapter(mAdapter);




        configFlingView(mFlingAdapterView, mAdapter);


        if(mRootView.findViewById(R.id.progressBar)!=null)
            mProgressBar= (ProgressBar)mRootView.findViewById(R.id.progressBar);


        mFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                getDataProvider().remove(0);
                getAdapter().notifyDataSetChanged();
            }
            @Override
            public void onLeftCardExit(Object dataObject) {
                leftCardExit(dataObject);
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                rightCardExit(dataObject);
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter)
            {
                if(getDataProvider().getCanLoadNext())
                    next();
                getAdapter().notifyDataSetChanged();
            }
            public void onScroll(float var1)
            {

            }
        });
        mFlingAdapterView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
               onItemClick(dataObject);
            }
        });

        next();
        onViewCreated(mRootView);
        return mRootView;
    }
    public void leftCardExit(Object dataObject) {

    }


    public void rightCardExit(Object dataObject) {

    }
    public void selectRight() {
        mFlingAdapterView.getTopCardListener().selectRight();
    }
    public void selectLeft() {
        mFlingAdapterView.getTopCardListener().selectLeft();
    }
    protected void configFlingView(SwipeFlingAdapterView mList,BaseListViewAdapter mAdapter)
    {

    }



    public void onFetchingStart(boolean isFetchingRefresh) {
        super.onFetchingStart(isFetchingRefresh);
        if(mProgressBar!=null && firstItemIn==false)
            mProgressBar.setVisibility(View.VISIBLE);
    }

    public void onFetchingError(Object error) {
        if(this.mEmptyViewHelper != null) {
            this.mEmptyViewHelper.updateView(this.getDataProvider());
        }

        this.updateSwipeRefreshLayout(false);
    }

    public void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh) {
        super.onFetchingEnd(dataList,isFetchingRefresh);
        updateProgress(isFetchingRefresh);
    }

    protected void updateProgress(boolean isRefreshData) {
        if(mProgressBar!=null)
            mProgressBar.setVisibility(View.GONE);
        firstItemIn = true;
    }
    public void onFetchingFinish(boolean isFetchingRefresh) {
        super.onFetchingFinish(isFetchingRefresh);
        if(mProgressBar!=null)
            mProgressBar.setVisibility(View.GONE);
        firstItemIn = true;
    }
}
