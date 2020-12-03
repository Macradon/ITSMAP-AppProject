package com.au564065.plantswap.activities.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.au564065.plantswap.R;
import com.au564065.plantswap.viewmodels.ProfileViewModel;

public class Profile_Window_fragment extends Fragment {

    private ProfileViewModel vm;
    private Button backbtn, editbtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_profile_window, container, false);

        backbtn = v.findViewById(R.id.ProfileWindow_BackButton);
        editbtn = v.findViewById(R.id.ProfileWindow_EditButton);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish(); //skal updateuser til firebase
            }
        });

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToEdit();
            }
        });

        return v;
    }

    public void changeToEdit(){
        FragmentManager m = getActivity().getSupportFragmentManager();
        m.beginTransaction()
                .replace(R.id.ProfileLayout, new Profile_Update_fragment())
                .commit();
    }
}
