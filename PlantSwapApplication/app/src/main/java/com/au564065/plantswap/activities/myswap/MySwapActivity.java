package com.au564065.plantswap.activities.myswap;

import android.os.Bundle;

import com.au564065.plantswap.models.Swap;
import com.au564065.plantswap.viewmodels.MySwapViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;

import com.au564065.plantswap.R;

import java.util.List;

public class MySwapActivity extends AppCompatActivity {
    private MySwapViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_swap);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewModelProvider viewModelProvider= new ViewModelProvider(getViewModelStore(), ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        viewModel =  viewModelProvider.get(MySwapViewModel.class);

        viewModel.getSwaps().observe(this, swaps -> {
            viewModel.swapList = swaps;
            FragmentManager m = getSupportFragmentManager();
            m.beginTransaction()
                    .add(R.id.mySwap_fragmentContainer, new SwapListFragment())
                    .commit();
        });
    }
    public MySwapViewModel getViewModel(){
        return viewModel;
    }
}