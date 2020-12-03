package com.au564065.plantswap.activities.browseplant;

import android.os.Bundle;

import com.au564065.plantswap.activities.profile.Profile_Window_fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.au564065.plantswap.R;

public class BrowsePlantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_plant);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.BrowsePlantLayout,new BrowsePlant_List_fragment())
                    .commitNow();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}