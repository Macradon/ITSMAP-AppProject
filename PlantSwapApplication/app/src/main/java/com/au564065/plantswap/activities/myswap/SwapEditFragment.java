package com.au564065.plantswap.activities.myswap;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.au564065.plantswap.R;
import com.au564065.plantswap.viewmodels.MySwapViewModel;

public class SwapEditFragment extends Fragment {

    private MySwapViewModel viewModel;

    public static SwapEditFragment newInstance() {
        return new SwapEditFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.swap_edit_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(MySwapViewModel.class);
        // TODO: Use the ViewModel
    }

}