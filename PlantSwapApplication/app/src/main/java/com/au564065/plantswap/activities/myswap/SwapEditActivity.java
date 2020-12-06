package com.au564065.plantswap.activities.myswap;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.au564065.plantswap.models.Swap;
import com.au564065.plantswap.models.Wish;
import com.au564065.plantswap.viewmodels.SwapEditViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.au564065.plantswap.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SwapEditActivity extends AppCompatActivity {
    public static final String SWAP_EDIT_NEW = "com.au564065.plantswap.activities.myswap.SWAP_EDIT_NEW";
    public static final String SWAP_EDIT_ID = "com.au564065.plantswap.activities.myswap.SWAP_EDIT_ID";
    static final int REQUEST_IMAGE_CAPTURE = 1;

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

        initializeViews();
        setupObservers();
        initializeButtons();

        if(!getIntent().getBooleanExtra(SWAP_EDIT_NEW, false)){

            viewModel.getSwap(getIntent().getStringExtra(SWAP_EDIT_ID)).observe(this, swap -> {
                updateViews(swap);
                viewModel.swap = swap;
                String[] wishes = swap.getSwapWishes().split(",");
                ArrayAdapter<String> adapter;

                adapter = (ArrayAdapter<String>) wish1.getAdapter();
                wish1.setSelection(adapter.getPosition(wishes[0]));

                adapter = (ArrayAdapter<String>) wish2.getAdapter();
                wish1.setSelection(adapter.getPosition(wishes[1]));

                adapter = (ArrayAdapter<String>) wish3.getAdapter();
                wish1.setSelection(adapter.getPosition(wishes[2]));

                adapter = (ArrayAdapter<String>) wish4.getAdapter();
                wish1.setSelection(adapter.getPosition(wishes[3]));


            });
            Button btnPhoto = findViewById(R.id.swap_edit_add_photo);
            btnPhoto.setVisibility(View.INVISIBLE);
        } else {
            viewModel.isNew = true;

            Button btnDelete = findViewById(R.id.swap_edit_btn_delete);
            btnDelete.setVisibility(View.INVISIBLE);
        }
    }

    private void setupObservers(){

        viewModel.getWishes().observe(this, wishes -> {
            List<String> names = new ArrayList<>();
            names.add("Select Plant");

            for (Wish wish : wishes) {
                names.add(wish.getWishPlant().getCommonName());
            }
            initializeSpinners(names);
        });
    }

    private void initializeButtons() {
        Button btnPhoto = findViewById(R.id.swap_edit_add_photo);
        btnPhoto.setOnClickListener(view -> {
            dispatchTakePictureIntent();
        });

        Button btnCancel = findViewById(R.id.swap_edit_btn_cancel);
        btnCancel.setOnClickListener(view -> {
            finish();
        });

        Button btnDelete = findViewById(R.id.swap_edit_btn_delete);
        btnDelete.setOnClickListener(view -> {
            viewModel.deleteSwap();
            finish();
        });

        Button btnSave = findViewById(R.id.swap_edit_btn_save);
        btnSave.setOnClickListener(view -> {
            if(!viewModel.swap.getImageURL().isEmpty() || (viewModel.isNew && viewModel.photoURI != null)) {
                viewModel.swap.setPlantName(plantName.getText().toString());
                String wishString = String.format("%s,%s,%s,%s",
                        wish1.getSelectedItem(),
                        wish2.getSelectedItem(),
                        wish3.getSelectedItem(),
                        wish4.getSelectedItem());
                viewModel.swap.setSwapWishes(wishString);
                viewModel.saveSwap();
                finish();
            } else {
                View rootView = findViewById(android.R.id.content);
                Snackbar.make(rootView, "Must add a photo", Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void initializeViews(){
        plantName = findViewById(R.id.my_swap_edit_plant_name);
        plantPhoto = findViewById(R.id.swap_edit_photo);
    }

    private void updateViews(Swap swap){
        plantName.setText(swap.getPlantName());
        Glide.with(plantPhoto.getContext()).load(swap.getImageURL()).into(plantPhoto);
    }

    private void initializeSpinners(List<String> list) {

        ArrayAdapter<String> adapter;

        wish1 = findViewById(R.id.swap_edit_spinner_wish1);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wish1.setAdapter(adapter);

        wish2 = findViewById(R.id.swap_edit_spinner_wish2);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wish2.setAdapter(adapter);

        wish3 = findViewById(R.id.swap_edit_spinner_wish3);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wish3.setAdapter(adapter);

        wish4 = findViewById(R.id.swap_edit_spinner_wish4);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wish4.setAdapter(adapter);

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.au564065.plantswap.fileprovider",
                        photoFile);
                viewModel.photoURI = photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File file = new File(viewModel.photoPath);
            try {
                Bitmap bitmap = MediaStore.Images.Media
                        .getBitmap(this.getContentResolver(), Uri.fromFile(file));
                plantPhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        viewModel.photoPath = image.getAbsolutePath();
        return image;
    }



}