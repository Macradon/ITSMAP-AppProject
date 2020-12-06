package com.au564065.plantswap.activities.browseplant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class BrowsePlant_AddingPlant_fragment extends Fragment {

    private PlantViewModel plantVM;
    private EditText radius;
    private TextView common;
    private ImageView img;
    private Button backBtn,addBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_browse_plant_add, container, false);

        initUI(v);
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

    private void initUI(View v){
        radius = v.findViewById(R.id.BrowsePlant_Adding_Radius);
        common = v.findViewById(R.id.BrowsePlant_Adding_CommonName);
        backBtn = v.findViewById(R.id.BrowsePlant_Adding_Back);
        addBtn = v.findViewById(R.id.BrowsePlant_Adding_Add);
        img = v.findViewById(R.id.BrowsePlant_Adding_Img);
    }

    private void updateUI(){

        Glide.with(getActivity().getApplicationContext())
                .load(plantVM.getOnClickedPlant().getImageURL())
                .centerCrop()
                .into(img);

        common.setText(plantVM.getOnClickedPlant().getCommonName());

    }

    private void setListeners(){

        FragmentManager m = getActivity().getSupportFragmentManager();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.beginTransaction()
                        .replace(R.id.BrowsePlantLayout, new BrowsePlant_Details_fragment())
                        .commit();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp;
                try {
                    temp = Integer.parseInt(radius.getText().toString());
                } catch (NumberFormatException e){
                    temp = 0;
                }
                //Toast.makeText(getActivity().getApplicationContext(),plantVM.onClickedPlant.getCommonName() + " has been added to your wishlist", Toast.LENGTH_SHORT).show();
                if(temp != 0){
                    Toast.makeText(getActivity().getApplicationContext(),plantVM.getOnClickedPlant().getCommonName() + " has been added to your wishlist", Toast.LENGTH_SHORT).show();
                    plantVM.addToWish(temp);
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"A number was not input or it was 0", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
