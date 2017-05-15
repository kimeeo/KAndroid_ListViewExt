package com.kimeeo.kAndroid.listViewExtDemo;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kimeeo.kAndroid.listViewExt.flipView.HorizontalFlipView;
import com.kimeeo.kAndroid.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.listView.BaseItemHolder;

/**
 * Created by BhavinPadhiyar on 24/05/16.
 */
public class HFlipView extends HorizontalFlipView {
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
