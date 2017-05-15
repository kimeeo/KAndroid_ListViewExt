package com.kimeeo.kAndroid.listViewExt.swipeDeck;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kimeeo.kAndroid.listViewExt.R;
import com.kimeeo.kAndroid.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.listView.BaseItemHolder;
import com.kimeeo.kAndroid.listViews.listView.BaseListViewAdapter;
import com.kimeeo.kAndroid.listViews.listView.DefaultListViewAdapter;
import com.kimeeo.kAndroid.listViews.listView.IViewProvider;


/**
 * Created by bhavinpadhiyar on 2/4/16.
 */
abstract public class DefaultSwipeDeck extends BaseSwipeDeck implements IViewProvider {
    public static class ViewType
    {
        public static final int FAKE_ITEM=1;
        public static final int ITEM=2;
    }
    public BaseItemHolder getItemHolder(int viewType, View view)
    {
        if(viewType==ViewType.FAKE_ITEM)
            return new FakeItemHolder(view);
        else
            return getCardHolder(viewType, view);
    }

    protected abstract BaseItemHolder getCardHolder(int viewType, View view);

    @Override
    public int getTotalViewTypeCount() {
        return 2;
    }
    @Override
    public int getListItemViewType(int viewType, Object data) {
        if(data instanceof FakeItem)
            return ViewType.FAKE_ITEM;
        else
            return ViewType.ITEM;
    }

    @LayoutRes
    abstract public int getItemViewRes(int viewType,LayoutInflater inflater,ViewGroup container);
    public View getItemView(int viewType,LayoutInflater inflater,ViewGroup container)
    {
        if(viewType==ViewType.FAKE_ITEM)
            return inflater.inflate(R.layout.fake_swipe_item,container,false);
        else
            return inflater.inflate(getItemViewRes(viewType,inflater,container),container,false);
    }

    protected BaseListViewAdapter createListViewAdapter()
    {
        DefaultListViewAdapter adapter =new DefaultListViewAdapter1(getDataProvider(),this);
        return adapter;
    }
    public class DefaultListViewAdapter1 extends DefaultListViewAdapter
    {
        public DefaultListViewAdapter1(DataProvider dataProvider, IViewProvider provider) {
            super(dataProvider,provider);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    public class FakeItemHolder extends BaseItemHolder {
        public FakeItemHolder(View itemView) {
            super(itemView);
        }
        @Override
        public void updateItemView(Object o, View view, int i) {

        }
    }

}
