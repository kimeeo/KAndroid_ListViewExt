package com.kimeeo.kAndroid.listViewExt.carouselView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kimeeo.kAndroid.listViews.listView.BaseItemHolder;
import com.kimeeo.kAndroid.listViews.listView.BaseListViewAdapter;
import com.kimeeo.kAndroid.listViews.listView.DefaultListViewAdapter;
import com.kimeeo.kAndroid.listViews.listView.IViewProvider;


/**
 * Created by bhavinpadhiyar on 1/22/16.
 */
abstract public class DefaultCarouselView extends BaseCarouselView implements IViewProvider {
    abstract public View getItemView(int viewType,LayoutInflater inflater,ViewGroup container);
    abstract public BaseItemHolder getItemHolder(int viewType, View view);
    @Override
    public int getTotalViewTypeCount() {
        return 1;
    }
    @Override
    public int getListItemViewType(int viewType, Object data) {
        return 1;
    }
    protected BaseListViewAdapter createListViewAdapter()
    {
        return new DefaultListViewAdapter(getDataProvider(),this);
    }
}