package com.kimeeo.kAndroid.listViewExt.carouselView;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
    private boolean firstItemIn = false;
    private boolean firstDataIn = true;
    ProgressBar mProgressBar;


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
            rootView= inflater.inflate(R.layout._fragment_cover_flow_page_view_with_swipe_refresh_layout, container, false);
        else
            rootView= inflater.inflate(R.layout._fragment_cover_flow_page_view, container, false);
        return rootView;
    }

    public CoverFlowCarousel getCoverFlow() {
        return mCoverFlow;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        configViewParam();
        getDataProvider().setRefreshEnabled(false);
        mRootView = createRootView(inflater, container, savedInstanceState);

        if(getDataProvider().getRefreshEnabled())
            configSwipeRefreshLayout(createSwipeRefreshLayout(mRootView));

        mCoverFlow = createCoverFlowCarouselView(mRootView);
        mEmptyViewHelper = createEmptyViewHelper();

        mAdapter = createListViewAdapter();
        mAdapter.supportLoader=false;

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

        if(mRootView.findViewById(R.id.progressBar)!=null)
            mProgressBar= (ProgressBar)mRootView.findViewById(R.id.progressBar);

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
        return (CoverFlowCarousel) rootView.findViewById(R.id.listView);
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
        updateProgress();
    }

    public void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh) {
        super.onFetchingEnd(dataList,isFetchingRefresh);
        updateProgress();
    }
    public void onFetchingFinish(boolean isFetchingRefresh) {
        super.onFetchingFinish(isFetchingRefresh);
        updateProgress();
    }
    protected void updateProgress() {
        if(firstDataIn==false) {
            final Handler handler = new Handler();
            final Runnable runnablelocal = new Runnable() {
                @Override
                public void run() {
                    if (firstDataIn) {
                        mCoverFlow.setAdapter(mAdapter);
                        mCoverFlow.invalidate();
                        firstDataIn = false;
                    }
                }
            };
            handler.postDelayed(runnablelocal, 1000);
        }

        if(mProgressBar!=null)
            mProgressBar.setVisibility(View.GONE);
        firstItemIn=true;
    }



}
