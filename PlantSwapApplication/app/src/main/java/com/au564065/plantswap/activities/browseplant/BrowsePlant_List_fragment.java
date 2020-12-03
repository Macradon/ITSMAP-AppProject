package com.au564065.plantswap.activities.browseplant;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.au564065.plantswap.R;
import com.au564065.plantswap.models.Plant;
import com.au564065.plantswap.ui.recyclerview.PlantAdapter;
import com.au564065.plantswap.viewmodels.PlantViewModel;

import java.util.List;


public class BrowsePlant_List_fragment extends Fragment implements PlantAdapter.IPlantItemClickedListener {

    private PlantViewModel plantVM;
    private RecyclerView plantCycler;
    private PlantAdapter adapter;
    private RecyclerView.LayoutManager layoutMan;
    private Button searchBtn;
    private EditText searchTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_browse_plant_list, container, false);
        //initiate recyclerview
        plantCycler = v.findViewById(R.id.browsePlantCycler);
        searchBtn = v.findViewById(R.id.BrowsePlant_SearchButton);
        searchTxt = v.findViewById(R.id.BrowsePlant_TextField);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new PlantAdapter(this);
        layoutMan = new LinearLayoutManager(getContext());
        plantCycler.setAdapter(adapter);
        plantCycler.setLayoutManager(layoutMan);

        setClickers();

        plantVM = new ViewModelProvider(getActivity()).get(PlantViewModel.class);
        plantVM.getPlants().observe(getViewLifecycleOwner(), new Observer<List<Plant>>() {
            @Override
            public void onChanged(List<Plant> plants) {
                adapter.updateList(plants);
            }
        });


    }

    @Override
    public void onItemClicked(int index) {
        //go to details??? i cannot see what details i should go to or stuff like that
    }

    public void setClickers(){
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plantVM.SearchPlant(searchTxt.getText().toString());
            }
        });
    }
}