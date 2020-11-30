package com.au564065.plantswap.viewmodels;

import androidx.recyclerview.widget.RecyclerView;

public interface ListViewModel {

    public RecyclerView.Adapter getAdapter();
    public RecyclerView.LayoutManager getLayoutManager();
}
