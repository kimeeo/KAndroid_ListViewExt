package com.kimeeo.kAndroid.listViewExt.swipeDeck;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daprlabs.cardstack.SwipeDeck;
import com.kimeeo.kAndroid.listViewExt.R;
import com.kimeeo.kAndroid.dataProvider.DataProvider;
import com.kimeeo.kAndroid.dataProvider.StaticDataProvider;
import com.kimeeo.kAndroid.listViews.listView.BaseListView;
import com.kimeeo.kAndroid.listViews.listView.BaseListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavinpadhiyar on 2/4/16.
 */
abstract public class BaseSwipeDeck extends BaseListView {


    private View mProgressBar;
    private SwipeDeck swipeDeck;
    private List<Object> dataSwiped;

    public DataProvider getDataSwipedLeft() {
        return dataSwipedLeft;
    }

    public DataProvider getDataSwipedRight() {
        return dataSwipedRight;
    }

    private DataProvider dataSwipedLeft;
    private DataProvider dataSwipedRight;
    protected void garbageCollectorCall()
    {
        super.garbageCollectorCall();
        mProgressBar=null;
        swipeDeck=null;
        dataSwiped=null;
    }

    protected View createRootView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        if(getDataProvider().getRefreshEnabled())
            return inflater.inflate(getRootRefreshLayoutResID(), container, false);
        else
            return inflater.inflate(getRootLayoutResID(), container, false);
    }

    @Override
    @LayoutRes
    protected int getRootRefreshLayoutResID() {
        return R.layout._fragment_swipe_deck_view_with_swipe_refresh_layout;
    }
    @Override
    @LayoutRes
    protected int getRootLayoutResID() {
        return R.layout._fragment_swipe_deck_view;
    }
    @IdRes
    protected int getProgressBarResID() {
        return  R.id.progressBar;
    }


    protected com.daprlabs.cardstack.SwipeDeck createSwipeDeckView(View rootView)
    {
        return (SwipeDeck) rootView.findViewById(getListViewResID());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        configViewParam();
        mRootView = createRootView(inflater, container, savedInstanceState);
        if(getDataProvider().getRefreshEnabled())
            configSwipeRefreshLayout(createSwipeRefreshLayout(mRootView));

        swipeDeck = createSwipeDeckView(mRootView);
        mEmptyViewHelper = createEmptyViewHelper();
        mAdapter = createListViewAdapter();
        mAdapter.setSupportLoader(false);

        dataSwiped = new ArrayList<>();
        dataSwipedLeft= new StaticDataProvider();
        dataSwipedRight= new StaticDataProvider();

        swipeDeck.setAdapter(mAdapter);
        swipeDeck.setHardwareAccelerationEnabled(true);



        configSwipeDeck(swipeDeck, mAdapter);

        int rightHint=getRightHint();
        if(rightHint!=-1)
            swipeDeck.setRightImage(rightHint);

        int leftHint=getLeftHint();
        if(leftHint!=-1)
            swipeDeck.setLeftImage(leftHint);


        mProgressBar= mRootView.findViewById(getProgressBarResID());

        swipeDeck.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                dataSwiped.add(getDataProvider().get(position));
                dataSwipedLeft.add(getDataProvider().get(position));
                leftCardExit(getDataProvider().get(position));
                if(getDataProvider().size()==(dataSwiped.size()+2) && getDataProvider().getCanLoadNext())
                    emptyAndLoadNext();
            }

            @Override
            public void cardSwipedRight(int position) {
                dataSwiped.add(getDataProvider().get(position));
                dataSwipedRight.add(getDataProvider().get(position));
                rightCardExit(getDataProvider().get(position));
                if(getDataProvider().size()== (dataSwiped.size()+2) && getDataProvider().getCanLoadNext())
                    emptyAndLoadNext();
            }

            @Override
            public void cardsDepleted() {

            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });




        next();
        onViewCreated(mRootView);
        return mRootView;
    }

    private void emptyAndLoadNext() {
        getDataProvider().removeAll(getDataProvider());
        dataSwiped = new ArrayList<>();
        next();
    }

    protected int getLeftHint()
    {
        return -1;
    }

    protected int getRightHint() {
        return -1;
    }

    public void leftCardExit(Object dataObject) {

    }


    public void rightCardExit(Object dataObject) {

    }
    public void selectRight() {
        swipeDeck.swipeTopCardRight(2000);
    }
    public void selectLeft() {
        swipeDeck.swipeTopCardLeft(2000);
    }
    protected void configSwipeDeck(SwipeDeck mList,BaseListViewAdapter mAdapter)
    {

    }





    private boolean firstItemIn = false;
    public void onFetchingStart(boolean isFetchingRefresh) {
        super.onFetchingStart(isFetchingRefresh);
        if(mProgressBar!=null && isFetchingRefresh==false)
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
        if(isFetchingRefresh==false) {
            getDataProvider().add(new FakeItem());
            getDataProvider().add(new FakeItem());
        }
        swipeDeck.setAdapter(mAdapter);
    }
    public void itemsAdded(int index, List items) {
        super.itemsAdded(index, items);
        updateProgress(false);
        if(mAdapter!=null)
            mAdapter.notifyDataSetChanged();
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

    public class FakeItem {}
}
