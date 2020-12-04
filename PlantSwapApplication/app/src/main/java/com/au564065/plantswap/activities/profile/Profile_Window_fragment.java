package com.au564065.plantswap.activities.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.au564065.plantswap.R;
import com.au564065.plantswap.models.PlantSwapUser;
import com.au564065.plantswap.viewmodels.ProfileViewModel;

public class Profile_Window_fragment extends Fragment {

    private ProfileViewModel vm;
    private Button backBtn, editBtn;
    private TextView name, address, city, zipcode, mail, phone;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_profile_window, container, false);

        initUI(v);
        setListener();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vm = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);
        vm.getData().observe(getViewLifecycleOwner(), new Observer<PlantSwapUser>() {
            @Override
            public void onChanged(PlantSwapUser swapUser) {
                updateUI(swapUser);
            }
        });
    }

    private void changeToEdit(){
        FragmentManager m = getActivity().getSupportFragmentManager();
        m.beginTransaction()
                .replace(R.id.ProfileLayout, new Profile_Update_fragment())
                .commit();
    }

    private void initUI(View v){

        name = v.findViewById(R.id.Profile_Window_Name);
        address = v.findViewById(R.id.Profile_Window_Address);
        city = v.findViewById(R.id.Profile_Window_City);
        zipcode = v.findViewById(R.id.Profile_Window_Zipcode);
        mail = v.findViewById(R.id.Profile_Window_Mail);
        phone = v.findViewById(R.id.Profile_Window_PhoneNumber);

        backBtn = v.findViewById(R.id.Profile_Window_BackButton);
        editBtn = v.findViewById(R.id.Profile_Window_EditButton);
    }

    private void updateUI(PlantSwapUser user){
        name.setText(user.getName());
        address.setText(user.getAddress());
        city.setText(user.getCity());
        zipcode.setText(user.getZipCode());
        mail.setText(user.getEmail());
        phone.setText(user.getPhoneNumber());
    }

    private void setListener(){

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish(); //skal updateuser til firebase
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToEdit();
            }
        });
    }
}
