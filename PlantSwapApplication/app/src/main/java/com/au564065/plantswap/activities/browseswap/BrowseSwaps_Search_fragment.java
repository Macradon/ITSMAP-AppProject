package com.au564065.plantswap.activities.browseswap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.au564065.plantswap.R;
import com.au564065.plantswap.activities.browseplant.BrowsePlant_Details_fragment;
import com.au564065.plantswap.models.Swap;
import com.au564065.plantswap.ui.recyclerview.BrowseSwapAdapter;
import com.au564065.plantswap.viewmodels.BrowseSwapViewModel;


import java.util.List;

public class BrowseSwaps_Search_fragment extends Fragment implements BrowseSwapAdapter.ISwapClickListener {

    private BrowseSwapViewModel swapVM;
    private RecyclerView swapCycler;
    private BrowseSwapAdapter adapter;
    private RecyclerView.LayoutManager layoutMan;
    private Button backBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_browse_swaps_search, container, false);

        swapCycler = v.findViewById(R.id.Browse_Swap_Cycler);
        backBtn = v.findViewById(R.id.Browse_Swaps_BackButton);


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swapCycler.setAdapter(adapter);
        swapCycler.setLayoutManager(layoutMan);
        adapter = new BrowseSwapAdapter(this);
        layoutMan = new LinearLayoutManager(getContext());

        swapVM = new ViewModelProvider(getActivity()).get(BrowseSwapViewModel.class);
        swapVM.getSwapList().observe(getViewLifecycleOwner(), new Observer<List<Swap>>() {
            @Override
            public void onChanged(List<Swap> list) {
                swapVM.saveAdapterList(list);
                adapter.updateList(list);
            }
        });

    }


    @Override
    public void onSwapClicked(int index) {
        swapVM.getOnClickedSwap(index);

        FragmentManager m = getActivity().getSupportFragmentManager();
        m.beginTransaction()
                .replace(R.id.BrowseSwapLayout, new BrowseSwaps_Details_fragment())
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

    }

}
