package com.au564065.plantswap.activities.mywish;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.au564065.plantswap.GlobalConstants;
import com.au564065.plantswap.R;
import com.au564065.plantswap.activities.browseplant.BrowsePlant_AddingPlant_fragment;
import com.au564065.plantswap.activities.browseplant.BrowsePlant_List_fragment;
import com.au564065.plantswap.models.Plant;
import com.au564065.plantswap.models.Wish;
import com.au564065.plantswap.viewmodels.MyWishViewModel;
import com.au564065.plantswap.viewmodels.PlantViewModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class EditWish extends Fragment {

    private TextView name, plantName, distance;
    private EditText radius;
    private Button cancelBtn, saveBtn, deleteBtn;
    private Wish chosenWish;

    private MyWishViewModel wvm;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mywish_edit, container, false);
        findUiId(v);
        setListeners();
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wvm = new ViewModelProvider(getActivity()).get(MyWishViewModel.class);
        wvm.getWish().observe(getViewLifecycleOwner(), new Observer<Wish>() {
            @Override
            public void onChanged(Wish wish) {
                updateUI();
            }
        });
    }


    private void findUiId(View v){
        //textviews
        name = v.findViewById(R.id.wishPlantText);
        plantName = v.findViewById(R.id.wishPlantNameEdit);
        distance = v.findViewById(R.id.wishRadiusTextEdit);
        //spinners
        radius = v.findViewById(R.id.wishRadiusEdit);
        //buttons
        cancelBtn = v.findViewById(R.id.cancel_editwish);
        saveBtn = v.findViewById(R.id.save_editwish);
        deleteBtn = v.findViewById(R.id.delete_editwish);

        name.setText(R.string.notifyMe);
        distance.setText(R.string.distance);
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
               int temp;
               try {
                   temp = Integer.parseInt(radius.getText().toString());
               } catch (NumberFormatException e) {
                   temp = 0;
               }
               if (temp != 0) {
                   Toast.makeText(getActivity().getApplicationContext(), wvm.onClickedWish.getWishPlant().getCommonName() + " has been updated", Toast.LENGTH_SHORT).show();
                   wvm.updateWish(wvm.onClickedWish.getWishId(), wvm.onClickedWish);
                   m.beginTransaction()
                           .replace(R.id.WishPlantLayout, new MyWishList())
                           .commit();
               } else {
                   Toast.makeText(getActivity().getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show();
               }
           }
       });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wvm.deleteWish(wvm.onClickedWish.getWishId());
                m.beginTransaction()
                        .replace(R.id.WishPlantLayout, new MyWishList())
                        .commit();
            }
        });
    }

    private void updateUI(){
        plantName.setText(wvm.onClickedWish.getWishPlant().getCommonName());
        radius.setText(""+ (int) wvm.onClickedWish.getRadius());
    }
}