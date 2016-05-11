package com.kimeeo.kAndroid.listViewExt.swipeDeck;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kimeeo.kAndroid.listViews.listView.BaseItemHolder;
import com.kimeeo.kAndroid.listViews.listView.BaseListViewAdapter;
import com.kimeeo.kAndroid.listViews.listView.DefaultListViewAdapter;
import com.kimeeo.kAndroid.listViews.listView.IViewProvider;


/**
 * Created by bhavinpadhiyar on 2/4/16.
 */
abstract public class DefaultSwipeDeck extends BaseSwipeDeck implements IViewProvider {
    abstract public BaseItemHolder getItemHolder(int viewType, View view);
    abstract public int getTotalViewTypeCount();
    abstract public int getListItemViewType(int viewType,Object data);
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
