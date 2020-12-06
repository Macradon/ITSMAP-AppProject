package com.au564065.plantswap.activities.myswap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.au564065.plantswap.R;
import com.au564065.plantswap.activities.MainMenuActivity;
import com.au564065.plantswap.models.Swap;
import com.au564065.plantswap.ui.recyclerview.MySwapAdapter;
import com.au564065.plantswap.viewmodels.MySwapViewModel;

import java.util.ArrayList;
import java.util.List;

public class SwapListFragment extends Fragment {
    private MySwapViewModel viewModel;
    private MySwapAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SwapListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SwapListFragment newInstance(int columnCount) {
        SwapListFragment fragment = new SwapListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelProvider viewModelProvider= new ViewModelProvider(getActivity());
        viewModel = viewModelProvider.get(MySwapViewModel.class);
        adapter = new MySwapAdapter(getContext(), viewModel.swapList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View listView = inflater.inflate(R.layout.fragment_swap_list, container, false);
        Context context = listView.getContext();

        RecyclerView swapList = listView.findViewById(R.id.mySwapList);
        swapList.setLayoutManager(new LinearLayoutManager(context));
        swapList.setAdapter(adapter);

        Button btnBack = listView.findViewById(R.id.mySwap_list_btnBack);
        btnBack.setOnClickListener(view -> {
            getActivity().finish();
        });

        Button btnNew = listView.findViewById(R.id.mySwap_list_btnNew);
        btnNew.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SwapEditActivity.class);
            intent.putExtra(SwapEditActivity.SWAP_EDIT_NEW, true);
            startActivity(intent);
        });

        return listView;
    }
}