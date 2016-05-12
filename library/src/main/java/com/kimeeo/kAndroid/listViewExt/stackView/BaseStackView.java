package com.kimeeo.kAndroid.listViewExt.stackView;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.StackView;

import com.kimeeo.kAndroid.listViewExt.R;
import com.kimeeo.kAndroid.listViews.listView.BaseListView;
import com.kimeeo.kAndroid.listViews.listView.BaseListViewAdapter;
import java.util.List;

/**
 * Created by bhavinpadhiyar on 1/27/16.
 */
abstract public class BaseStackView extends BaseListView {

    private boolean firstItemIn = false;
    private boolean firstDataIn = true;


    private View mProgressBar;
    private StackView mStackView;
    protected void garbageCollectorCall()
    {
        super.garbageCollectorCall();
        mProgressBar=null;
        mStackView=null;
    }

    protected View createRootView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        if(getDataProvider().getRefreshEnabled())
            return inflater.inflate(R.layout._fragment_stack_view_with_swipe_refresh_layout, container, false);
        else
            return inflater.inflate(R.layout._fragment_stack_view, container, false);
    }
    protected StackView createStackView(View rootView)
    {
        return (StackView) rootView.findViewById(R.id.listView);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        configViewParam();
        mRootView = createRootView(inflater, container, savedInstanceState);
        if(getDataProvider().getRefreshEnabled())
            configSwipeRefreshLayout(createSwipeRefreshLayout(mRootView));

        mStackView = createStackView(mRootView);
        mEmptyViewHelper = createEmptyViewHelper();
        mAdapter = createListViewAdapter();
        mAdapter.supportLoader=false;
        getDataProvider().setRefreshEnabled(false);
        mStackView.setAdapter(mAdapter);

        mStackView.setOnItemSelectedListener(new StackView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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

            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        configmStackView(mStackView, mAdapter);


        if(mRootView.findViewById(R.id.progressBar)!=null)
            mProgressBar= mRootView.findViewById(R.id.progressBar);




        next();
        onViewCreated(mRootView);
        return mRootView;
    }
    protected void onPageChange(Object itemPosition,int position)
    {

    }

    protected void configmStackView(StackView mList,BaseListViewAdapter mAdapter)
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

        final Handler handler = new Handler();
        final Runnable runnablelocal = new Runnable() {
            @Override
            public void run() {
                if(firstDataIn) {

                    mStackView.invalidate();
                    firstDataIn=false;
                }
            }
        };
        handler.postDelayed(runnablelocal, 1000);

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

