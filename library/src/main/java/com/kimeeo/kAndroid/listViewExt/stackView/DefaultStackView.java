package com.kimeeo.kAndroid.listViewExt.stackView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kimeeo.kAndroid.listViews.listView.IViewProvider;
import com.kimeeo.kAndroid.listViews.listView.BaseItemHolder;
import com.kimeeo.kAndroid.listViews.listView.BaseListViewAdapter;
import com.kimeeo.kAndroid.listViews.listView.DefaultListViewAdapter;
/**
 * Created by bhavinpadhiyar on 1/27/16.
 */
abstract public class DefaultStackView extends BaseStackView implements IViewProvider {
    abstract public BaseItemHolder getItemHolder(int viewType,View view);
    @Override
    public int getTotalViewTypeCount() {
        return 1;
    }
    @Override
    public int getListItemViewType(int viewType, Object data) {
        return 1;
    }

    abstract public int getItemViewRes(int viewType,LayoutInflater inflater,ViewGroup container);
    public View getItemView(int viewType,LayoutInflater inflater,ViewGroup container)
    {
        return inflater.inflate(getItemViewRes(viewType,inflater,container),container,false);
    }

    protected BaseListViewAdapter createListViewAdapter()
    {
        return new DefaultListViewAdapter(getDataProvider(),this);
    }
}
