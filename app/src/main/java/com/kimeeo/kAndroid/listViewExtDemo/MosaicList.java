package com.kimeeo.kAndroid.listViewExtDemo;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kimeeo.kAndroid.listViewExt.mosaicList.DefaultMosaicList;
import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.listView.BaseItemHolder;

/**
 * Created by BhavinPadhiyar on 24/05/16.
 */
public class MosaicList extends DefaultMosaicList {
    @Override
    public View getItemView(int viewType, LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.flip_item,container,false);
    }

    @Override
    public BaseItemHolder getItemHolder(int viewType, View view) {
        return new ListItemHolder(view);
    }

    @NonNull
    @Override
    protected DataProvider createDataProvider()
    {
        return new StaticDataProvider1(true,true,6);
    }
}
