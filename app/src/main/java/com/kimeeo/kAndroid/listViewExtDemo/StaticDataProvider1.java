package com.kimeeo.kAndroid.listViewExtDemo;

import android.os.Handler;

import com.kimeeo.kAndroid.listViews.dataProvider.StaticDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BhavinPadhiyar on 23/05/16.
 */
public class StaticDataProvider1 extends StaticDataProvider {
    public int pageSize=10;
    public int nextPageTotal=10;
    public int refreshPageTotal=3;
    int count = 1;
    Handler h = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            List list = new ArrayList();
            for (int i = 0; i < pageSize; i++) {
                list.add(new DataObject("Name " + count));
                count++;
            }
            addData(list);
        }
    };
    private int pageCount = 1;
    private int refreshPageCount = 1;

    public StaticDataProvider1(boolean nextEnabled,boolean  refreshEnabled,int pageSize) {
        //setRefreshItemPos(1);
        //setNextItemPos(1);
        setNextEnabled(nextEnabled);
        setRefreshEnabled(refreshEnabled);
        this.pageSize = pageSize;

    }

    @Override
    protected void invokeLoadNext() {
        if (pageCount != nextPageTotal) {
            h.postDelayed(r, 2000);


            pageCount += 1;
        } else {
            setCanLoadNext(false);
            dataLoadError(null);
        }
    }

    @Override
    protected void invokeLoadRefresh() {
        if (refreshPageCount != refreshPageTotal) {
            h.postDelayed(r, 2000);
            refreshPageCount += 1;
        } else {
            setCanLoadRefresh(false);
            dataLoadError(null);
        }
    }
}