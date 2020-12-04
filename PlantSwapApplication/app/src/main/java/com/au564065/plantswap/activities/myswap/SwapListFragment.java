package com.au564065.plantswap.activities.myswap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.au564065.plantswap.R;
import com.au564065.plantswap.activities.MainMenuActivity;
import com.au564065.plantswap.ui.recyclerview.MySwapAdapter;
import com.au564065.plantswap.viewmodels.MySwapViewModel;

public class SwapListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private MySwapViewModel viewModel;

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
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// TODO remove
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View listView = inflater.inflate(R.layout.fragment_swap_list, container, false);
        Context context = listView.getContext();

        RecyclerView swapList = container.findViewById(R.id.mySwapList);
        swapList.setLayoutManager(new LinearLayoutManager(context));
        swapList.setAdapter(new MySwapAdapter(context, viewModel.swapList.getValue()));

        Button btnBack = container.findViewById(R.id.mySwap_list_btnBack);
        btnBack.setOnClickListener(view -> {
            getActivity().finish();
        });

        Button btnNew = container.findViewById(R.id.mySwap_list_btnNew);
        btnNew.setOnClickListener(view -> {
            FragmentManager m = getActivity().getSupportFragmentManager();
            m.beginTransaction()
                    .replace(R.id.mySwap_fragmentContainer, new Profile_Update_fragment())
                    .commit();
        });

        return listView;
    }
}