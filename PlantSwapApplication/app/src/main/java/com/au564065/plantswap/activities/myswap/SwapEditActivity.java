package com.au564065.plantswap.activities.myswap;

import android.content.Intent;
import android.os.Bundle;

import com.au564065.plantswap.models.Swap;
import com.au564065.plantswap.viewmodels.SwapEditViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.au564065.plantswap.R;

public class SwapEditActivity extends AppCompatActivity {
    public static final String SWAP_EDIT_NEW = "com.au564065.plantswap.activities.myswap.SWAP_EDIT_NEW";
    public static final String SWAP_EDIT_ID = "com.au564065.plantswap.activities.myswap.SWAP_EDIT_ID";

    private EditText plantName;
    private ImageView plantPhoto;
    private Spinner wish1, wish2, wish3, wish4;
    private SwapEditViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewModelProvider viewModelProvider= new ViewModelProvider(getViewModelStore(), ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        viewModel = viewModelProvider.get(SwapEditViewModel.class);
        viewModel.setSwap(getIntent().getStringExtra(SWAP_EDIT_ID));

        if(!getIntent().getBooleanExtra(SWAP_EDIT_NEW, false)){
            plantName = findViewById(R.id.my_swap_edit_plant_name);
            plantName.setText(viewModel.swap.getPlantName());

            Button btnDelete = findViewById(R.id.swap_edit_btn_delete);
            btnDelete.setOnClickListener(view -> {
                viewModel.deleteSwap();
                Intent intent = new Intent(SwapEditActivity.this, MySwapActivity.class);
                startActivity(intent);
            });
        } else {

        }
        initializeViews();
        initializeButtons();
    }

    private void initializeButtons() {
        Button btnPhoto = findViewById(R.id.swap_edit_add_photo);
        btnPhoto.setOnClickListener(view -> {
            // TODO
        });

        Button btnCancel = findViewById(R.id.swap_edit_btn_cancel);
        btnCancel.setOnClickListener(view -> {
            finish();
        });

        Button btnSave = findViewById(R.id.swap_edit_btn_save);
        btnSave.setOnClickListener(view -> {
            viewModel.swap.setPlantName(plantName.getText().toString());
            viewModel.saveSwap();
            Intent intent = new Intent(SwapEditActivity.this, MySwapActivity.class);
            startActivity(intent);
        });
    }

    private void initializeViews() {

    }
}