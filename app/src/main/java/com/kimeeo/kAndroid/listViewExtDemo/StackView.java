package com.kimeeo.kAndroid.listViewExtDemo;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kimeeo.kAndroid.listViewExt.stackView.DefaultStackView;
import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.listView.BaseItemHolder;

/**
 * Created by BhavinPadhiyar on 24/05/16.
 */
public class StackView extends DefaultStackView {
    @Override
    public int getItemViewRes(int viewType, LayoutInflater inflater, ViewGroup container) {
        return R.layout.flip_item;
    }
    @Override
    public BaseItemHolder getItemHolder(int viewType, View view) {
        return new ListItemHolder(view);
    }
    @NonNull
    @Override
    protected DataProvider createDataProvider()
    {
        return new StaticDataProvider1(true,false,4);
    }
}
