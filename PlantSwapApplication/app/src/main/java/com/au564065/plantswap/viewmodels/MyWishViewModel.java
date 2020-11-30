package com.au564065.plantswap.viewmodels;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.au564065.plantswap.ui.recyclerview.MyWishAdapter;

public class MyWishViewModel implements ListViewModel{

    public MyWishAdapter listAdapter;
    public androidx.recyclerview.widget.LinearLayoutManager layoutManager;

    @Override
    public RecyclerView.Adapter getAdapter() {
        return listAdapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }
}
