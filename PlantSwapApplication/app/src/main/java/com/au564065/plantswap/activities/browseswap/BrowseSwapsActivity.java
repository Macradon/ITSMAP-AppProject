package com.au564065.plantswap.activities.browseswap;

import android.os.Bundle;

import com.au564065.plantswap.activities.browseplant.BrowsePlant_List_fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.au564065.plantswap.R;

public class BrowseSwapsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_swaps);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.main_menu_btn_browse_swaps);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.BrowseSwapLayout,new BrowseSwaps_Search_fragment())
                    .commitNow();
        }

    }
}
