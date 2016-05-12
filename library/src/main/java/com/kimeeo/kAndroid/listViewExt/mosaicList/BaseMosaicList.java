package com.kimeeo.kAndroid.listViewExt.mosaicList;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.adhamenaya.listeners.OnItemClickListener;
import com.adhamenaya.views.MosaicLayout;
import com.kimeeo.kAndroid.listViewExt.R;
import com.kimeeo.kAndroid.listViews.listView.BaseListView;
import com.kimeeo.kAndroid.listViews.listView.BaseListViewAdapter;


import java.util.List;

/**
 * Created by bhavinpadhiyar on 1/28/16.
 */
abstract public class BaseMosaicList extends BaseListView implements OnItemClickListener {

    private boolean firstItemIn = false;
    private boolean firstDataIn = true;
    private View mProgressBar;
    private MosaicLayout mMosaicLayout;

    public MosaicLayout getMosaicLayout() {
        return mMosaicLayout;
    }

    protected void garbageCollectorCall() {
        super.garbageCollectorCall();
        mProgressBar = null;
        mMosaicLayout = null;
    }

    protected View createRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        if(getDataProvider().getRefreshEnabled())
            rootView = inflater.inflate(R.layout._fragment_mocaic_page_view_with_swipe_refresh_layout, container, false);
        else
            rootView = inflater.inflate(R.layout._fragment_mocaic_page_view, container, false);
        return rootView;
    }

    protected MosaicLayout createMosaicLayout(View rootView) {
        return (MosaicLayout) rootView.findViewById(R.id.listView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        configViewParam();
        mRootView = createRootView(inflater, container, savedInstanceState);
        if(getDataProvider().getRefreshEnabled())
            configSwipeRefreshLayout(createSwipeRefreshLayout(mRootView));

        mMosaicLayout = createMosaicLayout(mRootView);
        mEmptyViewHelper = createEmptyViewHelper();
        mAdapter = createListViewAdapter();
        mAdapter.supportLoader = false;

        mMosaicLayout.setOnItemClickListener(this);
        setOnScrollListener(mMosaicLayout);
        mMosaicLayout.chooseRandomPattern(true);
        configMosaicLayout(mMosaicLayout, mAdapter);


        if (mRootView.findViewById(R.id.progressBar) != null)
            mProgressBar = mRootView.findViewById(R.id.progressBar);


        next();
        onViewCreated(mRootView);
        return mRootView;
    }
    public void onClick(int position)
    {
        onItemClick(getDataProvider().get(position));
    }


    protected void setOnScrollListener(MosaicLayout mList) {

        mList.setListener(new MosaicLayout.OnBottomReach() {
            public void onBottomReached(int l, int t, int oldl, int oldt) {
                if (getDataProvider().getCanLoadNext())
                    next();
                onDataScroll(getMosaicLayout(), l, t);
            }
        });
    }
    protected void onDataScroll(MosaicLayout listView, int dx, int dy)
    {

    }

    protected void configMosaicLayout(MosaicLayout mList,BaseListViewAdapter mAdapter)
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
        final Handler handler = new Handler();
        final Runnable runnablelocal = new Runnable() {
            @Override
            public void run() {

                mMosaicLayout.setAdapter(mAdapter);
                mMosaicLayout.invalidate();
                firstDataIn=false;
            }
        };
        handler.postDelayed(runnablelocal, 1000);

        if(mProgressBar!=null)
            mProgressBar.setVisibility(View.GONE);
        firstItemIn = true;
    }


    public void onFetchingFinish(boolean isFetchingRefresh) {
        super.onFetchingFinish(isFetchingRefresh);
    }
}
