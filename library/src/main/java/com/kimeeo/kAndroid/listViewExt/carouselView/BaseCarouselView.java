package com.kimeeo.kAndroid.listViewExt.carouselView;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kimeeo.kAndroid.listViewExt.R;
import com.kimeeo.kAndroid.listViews.listView.BaseListView;
import com.kimeeo.kAndroid.listViews.listView.BaseListViewAdapter;

import java.util.List;

/**
 * Created by bhavinpadhiyar on 1/22/16.
 */
abstract public class BaseCarouselView extends BaseListView
{
    private CoverFlowCarousel mCoverFlow;
    private boolean firstTimeSetupDone = false;
    View mProgressBar;


    protected void garbageCollectorCall() {
        super.garbageCollectorCall();
        mRootView=null;
        if(mAdapter!=null)
            mAdapter.garbageCollectorCall();
        mAdapter=null;
        mCoverFlow=null;
    }

    protected View createRootView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView;
        if(getDataProvider().getRefreshEnabled())
            rootView= inflater.inflate(getRootRefreshLayoutResID(), container, false);
        else
            rootView= inflater.inflate(getRootLayoutResID(), container, false);
        return rootView;
    }
    @Override
    @LayoutRes
    protected int getRootRefreshLayoutResID() {
        return R.layout._fragment_cover_flow_page_view_with_swipe_refresh_layout;
    }
    @Override
    @LayoutRes
    protected int getRootLayoutResID() {
        return R.layout._fragment_cover_flow_page_view;
    }
    @IdRes
    protected int getProgressBarResID() {
        return  R.id.progressBar;
    }


    public CoverFlowCarousel getCoverFlow() {
        return mCoverFlow;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        configViewParam();
        //getDataProvider().setRefreshEnabled(false);
        mRootView = createRootView(inflater, container, savedInstanceState);

        if(getDataProvider().getRefreshEnabled())
            configSwipeRefreshLayout(createSwipeRefreshLayout(mRootView));

        mCoverFlow = createCoverFlowCarouselView(mRootView);
        mEmptyViewHelper = createEmptyViewHelper();

        mAdapter = createListViewAdapter();
        mAdapter.setSupportLoader(false);

        mCoverFlow.setOnItemSelectedListener(new CoverFlowCarousel.OnItemSelectedListener() {
            public void onItemSelected(View child, int position) {
                Object iBaseObject = getDataProvider().get(position);
                onPageChange(iBaseObject, position);

                if (getDataProvider().getCanLoadNext() && position == getDataProvider().size() - 1)
                    next();

                if (getDataProvider().getCanLoadRefresh() && position == 0) {
                    if (getSwipeRefreshLayout() != null)
                        getSwipeRefreshLayout().setEnabled(true);
                    else
                        refresh();
                } else if (getSwipeRefreshLayout() != null)
                    getSwipeRefreshLayout().setEnabled(false);
            }
        });

        mCoverFlow.setSpacing(0.5f);

        if(mRootView.findViewById(getProgressBarResID())!=null)
            mProgressBar=mRootView.findViewById(getProgressBarResID());

        configListView(mCoverFlow, mAdapter);

        next();
        onViewCreated(mRootView);
        return mRootView;
    }
    protected void onPageChange(Object itemPosition,int position)
    {

    }
    protected void configListView(CoverFlowCarousel mList,BaseListViewAdapter mAdapter)
    {

    }

    protected CoverFlowCarousel createCoverFlowCarouselView(View rootView)
    {
        return (CoverFlowCarousel) rootView.findViewById(getListViewResID());
    }





    public void onFetchingStart(boolean isFetchingRefresh) {
        super.onFetchingStart(isFetchingRefresh);
        if(mProgressBar!=null && firstTimeSetupDone==false)
            mProgressBar.setVisibility(View.VISIBLE);
    }

    public void onFetchingError(Object error) {
        if(this.mEmptyViewHelper != null) {
            this.mEmptyViewHelper.updateView(this.getDataProvider());
        }

        this.updateSwipeRefreshLayout(false);
        updateProgress();
    }
    public void itemsAdded(int index, List items) {
        super.itemsAdded(index, items);
        mCoverFlow.setAdapter(mAdapter);
        mCoverFlow.invalidate();
        firstTimeSetupDone = true;
    }
    public void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh) {
        super.onFetchingEnd(dataList,isFetchingRefresh);

        if(firstTimeSetupDone==false) {
            final Handler handler = new Handler();
            final Runnable runnablelocal = new Runnable() {
                @Override
                public void run() {
                    mCoverFlow.setAdapter(mAdapter);
                    mCoverFlow.invalidate();
                    firstTimeSetupDone = true;
                }
            };
            handler.postDelayed(runnablelocal, 1000);
        }
        updateProgress();
    }
    public void onFetchingFinish(boolean isFetchingRefresh) {
        super.onFetchingFinish(isFetchingRefresh);
        updateProgress();
    }
    protected void updateProgress() {
        if(mProgressBar!=null)
            mProgressBar.setVisibility(View.GONE);
    }
}
