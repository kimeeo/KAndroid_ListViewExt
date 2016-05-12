package com.kimeeo.kAndroid.listViewExt.flipView;

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

import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;

/**
 * Created by bhavinpadhiyar on 1/27/16.
 */
abstract public class BaseFlipView extends BaseListView {

    public static final String ORIENTATION_VERTICAL = "vertical";
    public static final String ORIENTATION_HORIZONTAL = "horizontal";
    private boolean loadingRefreshData = false;
    private boolean loadingNextData = false;
    private boolean firstItemIn = false;

    private View mProgressBar;
    private View mProgressBarBottom;
    private View mProgressBarTop;
    private FlipView mFlipView;

    public String getOrientation() {
        return ORIENTATION_VERTICAL;
    }

    public FlipView getFlipView() {
        return mFlipView;
    }


    protected void garbageCollectorCall()
    {
        super.garbageCollectorCall();
        mProgressBar=null;
        mProgressBarBottom=null;
        mProgressBarTop=null;
        mFlipView=null;
    }

    protected View createRootView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View rootView;
        if(getOrientation()==ORIENTATION_HORIZONTAL)
            rootView = inflater.inflate(R.layout._fragment_flip_horizontal_page_view, container, false);
        else
            rootView = inflater.inflate(R.layout._fragment_flip_vertical_page_view, container, false);
        return rootView;
    }
    protected FlipView createFlipView(View rootView)
    {
        return (FlipView) rootView.findViewById(R.id.listView);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        configViewParam();
        mRootView = createRootView(inflater, container, savedInstanceState);

        mFlipView = createFlipView(mRootView);
        mEmptyViewHelper = createEmptyViewHelper();
        mAdapter = createListViewAdapter();
        mFlipView.setAdapter(mAdapter);

        mFlipView.setOnFlipListener(new FlipView.OnFlipListener() {
            public void onFlippedToPage(FlipView var1, int position, long var3) {

            }
        });


        configFlipView(mFlipView, mAdapter);
        //setOnScrollListener(recyclerView);


        if(mRootView.findViewById(R.id.progressBar)!=null)
            mProgressBar= mRootView.findViewById(R.id.progressBar);

        if(mRootView.findViewById(R.id.progressBarTop)!=null)
            mProgressBarTop= mRootView.findViewById(R.id.progressBarTop);

        if(mRootView.findViewById(R.id.progressBarBottom)!=null)
            mProgressBarBottom= mRootView.findViewById(R.id.progressBarBottom);

        if(getDataProvider().getRefreshEnabled())
        {
            mFlipView.setOnOverFlipListener(new FlipView.OnOverFlipListener() {
                @Override
                public void onOverFlip(FlipView flipView, OverFlipMode overFlipMode, boolean overFlippingPrevious, float overFlipDistance, float flipDistancePerPage) {
                    int position = getCurrentPage();
                    if(getDataProvider().isFetching()==false && getDataProvider().getCanLoadRefresh() && position==0) {
                        loadingRefreshData = true;
                        loadingNextData = false;
                        refresh();
                    }
                    else if(getDataProvider().isFetching()==false && getDataProvider().getCanLoadNext() && position==getDataProvider().size()-1) {
                        loadingRefreshData = false;
                        loadingNextData = true;
                        next();
                    }
                }
            });
        }

        next();
        onViewCreated(mRootView);
        return mRootView;
    }
    public int getPageCount()
    {
        return  mFlipView.getPageCount();
    }
    public void flipTo(int page)
    {
        mFlipView.flipTo(page);
    }
    public void smoothFlipTo(int page)
    {
        mFlipView.smoothFlipTo(page);
    }
    public void flipBy(int pageDelta)
    {
        mFlipView.flipBy(pageDelta);
    }
    public void smoothFlipBy(int pageDelta)
    {
        mFlipView.smoothFlipBy(pageDelta);
    }
    public void peakNext(boolean once)
    {
        mFlipView.peakNext(once);
    }
    public void peakPrevious(boolean once)
    {
        mFlipView.peakPrevious(once);
    }
    public boolean isFlippingVertically()
    {
        return  mFlipView.isFlippingVertically();
    }

    protected void configFlipView(FlipView mList,BaseListViewAdapter mAdapter)
    {

    }
    public int getCurrentPage()
    {
        return  mFlipView.getCurrentPage();
    }





    public void onFetchingStart(boolean isFetchingRefresh) {
        super.onFetchingStart(isFetchingRefresh);
        if(mProgressBar!=null && firstItemIn==false)
            mProgressBar.setVisibility(View.VISIBLE);

        if(mProgressBarTop!=null && loadingRefreshData)
            mProgressBarTop.setVisibility(View.VISIBLE);

        if(mProgressBarBottom!=null && loadingNextData)
            mProgressBarBottom.setVisibility(View.VISIBLE);
    }

    public void onFetchingError(Object error) {
        if(this.mEmptyViewHelper != null) {
            this.mEmptyViewHelper.updateView(this.getDataProvider());
        }
        this.updateSwipeRefreshLayout(false);
        updateProgress(false);
    }

    public void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh) {
        super.onFetchingEnd(dataList,isFetchingRefresh);
        updateProgress(isFetchingRefresh);
    }


    public void onFetchingFinish(boolean isFetchingRefresh) {
        super.onFetchingFinish(isFetchingRefresh);
        updateProgress(isFetchingRefresh);
    }
    protected void updateProgress(boolean isRefreshData) {
        if (isRefreshData && mFlipView!=null)
            smoothFlipTo(0);

        if(mProgressBar!=null)
            mProgressBar.setVisibility(View.GONE);
        firstItemIn = true;

        loadingRefreshData = false;
        loadingNextData = false;

        if(mProgressBarTop!=null)
            mProgressBarTop.setVisibility(View.GONE);

        if(mProgressBarBottom!=null)
            mProgressBarBottom.setVisibility(View.GONE);
    }
}
