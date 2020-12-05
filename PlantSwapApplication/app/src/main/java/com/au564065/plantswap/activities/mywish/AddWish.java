package com.au564065.plantswap.activities.mywish;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.au564065.plantswap.R;
import com.au564065.plantswap.activities.browseplant.BrowsePlant_AddingPlant_fragment;
import com.au564065.plantswap.activities.browseplant.BrowsePlant_List_fragment;
import com.au564065.plantswap.models.Plant;
import com.au564065.plantswap.models.Wish;
import com.au564065.plantswap.viewmodels.MyWishViewModel;
import com.au564065.plantswap.viewmodels.PlantViewModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class AddWish extends Fragment {

    private TextView name, distance;
    private Spinner spinnerName, spinnerDistance;
    private Button cancelBtn, saveBtn;

    private MyWishViewModel wvm;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mywish_add, container, false);
        findUiId(v);
        setListeners();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        wvm = new ViewModelProvider(getActivity()).get(MyWishViewModel.class);
        wvm.getAllWishes().observe(getViewLifecycleOwner(), new Observer<List<Wish>>() {
            @Override
            public void onChanged(List<Wish> wish) {
                updateUI();
            }
        });

    }

    private void findUiId(View v){
        //textviews
        name = v.findViewById(R.id.wishPlantNameText);
        distance = v.findViewById(R.id.wishPlantRadiusText);
        //spinners
        spinnerDistance = v.findViewById(R.id.wishPlantRadiusSpinner);
        spinnerName = v.findViewById(R.id.wishPlantNamepinner);
        //buttons
        cancelBtn = v.findViewById(R.id.cancel_addwish);
        saveBtn = v.findViewById(R.id.save_addwish);
    }
    private void setListeners(){

        FragmentManager m = getActivity().getSupportFragmentManager();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                m.beginTransaction()
                        .replace(R.id.WishPlantLayout, new MyWishList())
                        .commit();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.beginTransaction()
                        .replace(R.id.WishPlantLayout, new BrowsePlant_AddingPlant_fragment())
                        .commit();
            }
        });
    }

    private void updateUI(){
        name.setText(R.string.notifyMe);
        distance.setText(R.string.distance);
        // spinners
        // søgefunktion i stedet for spinner?
        String name = spinnerName.getSelectedItem().toString();
        Double distance = Double.valueOf(spinnerDistance.getSelectedItem().toString());
    }
}

