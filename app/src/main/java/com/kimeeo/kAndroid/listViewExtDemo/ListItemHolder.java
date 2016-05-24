package com.kimeeo.kAndroid.listViewExtDemo;

import android.view.View;
import android.widget.TextView;

import com.kimeeo.kAndroid.listViewExt.R;
import com.kimeeo.kAndroid.listViews.listView.BaseItemHolder;

/**
 * Created by BhavinPadhiyar on 24/05/16.
 */
public class ListItemHolder extends BaseItemHolder {
    public ListItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void updateItemView(Object o, View view, int i) {
        DataObject data = (DataObject) o;
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(i + ". " + data.name);
    }
}

