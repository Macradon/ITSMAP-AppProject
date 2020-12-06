package com.au564065.plantswap.activities.profile;

import android.os.Bundle;

import com.au564065.plantswap.activities.browseplant.BrowsePlant_List_fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.au564065.plantswap.R;

import java.lang.reflect.Array;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ProfileLayout,new Profile_Window_fragment())
                    .commitNow();
        }
    }
}