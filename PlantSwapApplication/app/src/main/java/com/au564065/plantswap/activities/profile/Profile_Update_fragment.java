package com.au564065.plantswap.activities.profile;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.au564065.plantswap.R;
import com.au564065.plantswap.viewmodels.ProfileViewModel;

public class Profile_Update_fragment extends Fragment {

    private ProfileViewModel vm;
    private Button updatebtn, deletebtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_profile_update, container, false);

        updatebtn = v.findViewById(R.id.ProfileUpdate_UpdateButton);
        deletebtn = v.findViewById(R.id.ProfileWindow_EditButton);

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vm.UpdateUserData(); //skal updateuser til firebase
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vm.DeleteUserData(); //skal slet user fra firebase
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       /* vm = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);
        vm.getData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                //skal indsætte værdier for user i forvejen
            }
        });*/
    }


}