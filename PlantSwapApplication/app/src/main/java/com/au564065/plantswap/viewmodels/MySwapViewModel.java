package com.au564065.plantswap.viewmodels;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.au564065.plantswap.ui.recyclerview.MySwapAdapter;

public class MySwapViewModel implements ListViewModel{

    public MySwapAdapter listAdapter;
    public LinearLayoutManager layoutManager;

    @Override
    public RecyclerView.Adapter getAdapter() {
        return listAdapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }
}
