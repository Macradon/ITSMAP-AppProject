package com.au564065.plantswap.activities.browseswap;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.au564065.plantswap.R;
import com.au564065.plantswap.activities.browseplant.BrowsePlant_Details_fragment;
import com.au564065.plantswap.models.Swap;
import com.au564065.plantswap.ui.recyclerview.AddSwapOfferAdapter;
import com.au564065.plantswap.ui.recyclerview.BrowseSwapAdapter;
import com.au564065.plantswap.viewmodels.BrowseSwapViewModel;
import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BrowseSwaps_MakeOffer_fragment extends Fragment {

    private BrowseSwapViewModel SwapVM;
    private RecyclerView swapOfferOffers;
    private AddSwapOfferAdapter adapter;
    private RecyclerView.LayoutManager layoutMan;
    private Button backBtn, addMoreBtn, sendBtn, deleteBtn;
    private TextView name;
    private ImageView img;
    private List<String> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_browse_swaps_makeoffer, container, false);

        initUI(v);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swapOfferOffers.setAdapter(adapter);
        swapOfferOffers.setLayoutManager(layoutMan);

        setListeners();

        SwapVM.getCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                adapter.updateList(integer);
                updateUI();
            }
        });

    }

    private void initUI(View v){
        backBtn = v.findViewById(R.id.Browse_Swaps_Offer_Back);
        addMoreBtn = v.findViewById(R.id.Browse_Swaps_Offer_More);
        sendBtn = v.findViewById(R.id.Browse_Swaps_Offer_Send);
        deleteBtn = v.findViewById(R.id.Browse_Swaps_Offer_Delete);
        name = v.findViewById(R.id.Browse_Swaps_Offer_Name);
        img = v.findViewById(R.id.Browse_Swaps_Offer_Img);

        swapOfferOffers = v.findViewById(R.id.Browse_Swaps_Offer_Cycler);

        SwapVM = new ViewModelProvider(getActivity()).get(BrowseSwapViewModel.class);
        list = new ArrayList<String>();
        list.add("Select Plant");
        List<String> temp = Arrays.asList(SwapVM.getSwap().getSwapWishes().split(","));
        for (String s : temp){
            list.add(s);
        }
        list.add("other");

        adapter = new AddSwapOfferAdapter(list);
        layoutMan = new LinearLayoutManager(getContext());


    }

    private void updateUI(){
        name.setText(SwapVM.getSwap().getPlantName());
        Glide.with(img).load(SwapVM.getSwap().getImageURL()).into(img);
    }

    private void setListeners(){
        FragmentManager m = getActivity().getSupportFragmentManager();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.beginTransaction()
                        .replace(R.id.BrowseSwapLayout, new BrowseSwaps_Details_fragment())
                        .commit();
                /*String s = "didnt work";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    s = String.join(",",adapter.getSavedStrings());
                }

                Log.d("From adding offer:", s);*/
            }
        });

        addMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwapVM.addMoreSpinners();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwapVM.deleteSpinner();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

}
