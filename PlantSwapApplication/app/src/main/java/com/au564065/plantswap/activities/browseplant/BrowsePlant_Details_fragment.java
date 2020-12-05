package com.au564065.plantswap.activities.browseplant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.au564065.plantswap.R;
import com.au564065.plantswap.models.Plant;
import com.au564065.plantswap.ui.recyclerview.PlantAdapter;
import com.au564065.plantswap.viewmodels.PlantViewModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class BrowsePlant_Details_fragment extends Fragment {

    private TextView common, science, genus, family;
    private ImageView plantImg;
    private Button cancelBtn, addBtn;

    private PlantViewModel plantVM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_browse_plant_details, container, false);
        //initiate recyclerview
        findUiId(v);
        setListeners();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        plantVM = new ViewModelProvider(getActivity()).get(PlantViewModel.class);
        plantVM.getPlants().observe(getViewLifecycleOwner(), new Observer<List<Plant>>() {
            @Override
            public void onChanged(List<Plant> plants) {
                updateUI();
            }
        });

    }

    private void findUiId(View v){
        //textviews
        common = v.findViewById(R.id.BrowsePlant_Detail_CommonName);
        science = v.findViewById(R.id.BrowsePlant_Detail_Science);
        genus = v.findViewById(R.id.BrowsePlant_Detail_Genus);
        family = v.findViewById(R.id.BrowsePlant_Detail_Family);
        //imgview
        plantImg = v.findViewById(R.id.BrowsePlant_Detail_Img);
        //buttons
        cancelBtn = v.findViewById(R.id.BrowsePlant_Detail_Back);
        addBtn = v.findViewById(R.id.BrowsePlant_Detail_Add);
    }

    private void setListeners(){

        FragmentManager m = getActivity().getSupportFragmentManager();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                m.beginTransaction()
                        .replace(R.id.BrowsePlantLayout, new BrowsePlant_List_fragment())
                        .commit();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.beginTransaction()
                        .replace(R.id.BrowsePlantLayout, new BrowsePlant_AddingPlant_fragment())
                        .commit();
            }
        });
    }

    private void updateUI(){

        Glide.with(plantImg.getContext())
                .load(plantVM.onClickedPlant.getImageURL())
                .into(plantImg);

        common.setText(plantVM.onClickedPlant.getCommonName());
        science.setText(plantVM.onClickedPlant.getScientificName());
        genus.setText(plantVM.onClickedPlant.getGenus());
        family.setText(plantVM.onClickedPlant.getFamily());

    }
}
