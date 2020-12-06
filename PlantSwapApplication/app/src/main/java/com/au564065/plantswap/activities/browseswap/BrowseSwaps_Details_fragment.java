package com.au564065.plantswap.activities.browseswap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.au564065.plantswap.R;
import com.au564065.plantswap.activities.browseplant.BrowsePlant_Details_fragment;
import com.au564065.plantswap.models.Swap;
import com.au564065.plantswap.ui.recyclerview.BrowseSwapAdapter;
import com.au564065.plantswap.viewmodels.BrowseSwapViewModel;

import java.util.List;

public class BrowseSwaps_Details_fragment extends Fragment {

    private BrowseSwapViewModel SwapVM;
    private TextView name, wishes;
    private ImageView img;
    private Button backBtn, addOfferBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_browse_swaps_details, container, false);

        initUI(v);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListeners();

        SwapVM = new ViewModelProvider(getActivity()).get(BrowseSwapViewModel.class);
        SwapVM.getSwapList().observe(getViewLifecycleOwner(), new Observer<List<Swap>>() {
            @Override
            public void onChanged(List<Swap> list) {
                updateUI();
            }
        });

    }

    private void initUI(View v){
        img = v.findViewById(R.id.Browse_Swaps_Details_Img);
        name = v.findViewById(R.id.Browse_Swaps_Details_Name);
        wishes = v.findViewById(R.id.Browse_Swaps_Details_Wishes);
        backBtn = v.findViewById(R.id.Browse_Swaps_Details_BackButton);
        addOfferBtn = v.findViewById(R.id.Browse_Swaps_Details_MakeOffer);
    }

    private void setListeners(){

        FragmentManager m = getActivity().getSupportFragmentManager();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.beginTransaction()
                        .replace(R.id.BrowseSwapLayout, new BrowseSwaps_Search_fragment())
                        .commit();
            }
        });

        addOfferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.beginTransaction()
                        .replace(R.id.BrowseSwapLayout, new BrowseSwaps_MakeOffer_fragment())
                        .commit();
            }
        });
    }

    private void updateUI(){
        //wishes.setText(SwapVM.getSwap().getWishPlants());
        //name.setText(SwapVM.getSwap().getPlantName());
    }
}
