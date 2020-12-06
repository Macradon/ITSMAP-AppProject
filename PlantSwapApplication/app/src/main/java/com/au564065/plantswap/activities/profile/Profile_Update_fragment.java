package com.au564065.plantswap.activities.profile;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.au564065.plantswap.R;
import com.au564065.plantswap.models.PlantSwapUser;
import com.au564065.plantswap.viewmodels.ProfileViewModel;

public class Profile_Update_fragment extends Fragment {

    private ProfileViewModel vm;
    private EditText name, address, city, zipcode, phone, mail;
    private Button updateBtn, deleteBtn, backBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_profile_update, container, false);

        initUI(v);
        setOnClickers();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vm = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);
        vm.getData().observe(getViewLifecycleOwner(), new Observer<PlantSwapUser>() {
            @Override
            public void onChanged(PlantSwapUser user) {
                updateUI(user);
            }
        });
    }

    private void initUI(View v){

        updateBtn = v.findViewById(R.id.Profile_Window_EditButton);
        deleteBtn = v.findViewById(R.id.Profile_Update_DeleteButton);
        backBtn = v.findViewById(R.id.Profile_Window_BackButton);

        name = v.findViewById(R.id.Profile_Update_edtTxtName);
        address = v.findViewById(R.id.Profile_Update_edtTxtAddress);
        city = v.findViewById(R.id.Profile_Update_edtTxtCity);
        zipcode = v.findViewById(R.id.Profile_Update_edtTxtZipCode);
        phone = v.findViewById(R.id.Profile_Update_edtTxtPhone);
        mail = v.findViewById(R.id.Profile_Update_edtTxtEmail);

    }

    private void updateUI(PlantSwapUser user){
        name.setText(user.getName());
        address.setText(user.getAddress());
        city.setText(user.getCity());
        zipcode.setText(user.getZipCode());
        mail.setText(user.getEmail());
        phone.setText(user.getPhoneNumber());
    }

    private PlantSwapUser makeTemp(){
        PlantSwapUser temp = new PlantSwapUser(name.getText().toString(),
                address.getText().toString(),
                zipcode.getText().toString(),
                city.getText().toString(),
                mail.getText().toString(),
                phone.getText().toString());

        return temp;
    }

    private void setOnClickers(){
        FragmentManager m = getActivity().getSupportFragmentManager();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vm.UpdateUserData(makeTemp()); //skal updateuser til firebase
                m.beginTransaction()
                        .replace(R.id.ProfileLayout, new Profile_Window_fragment())
                        .commit();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.beginTransaction()
                        .replace(R.id.ProfileLayout, new Profile_Window_fragment())
                        .commit();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vm.DeleteUserData(); //skal slet user fra firebase
            }
        });

    }
}