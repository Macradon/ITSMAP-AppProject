package com.au564065.plantswap.activities.mywish;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.au564065.plantswap.R;
import com.au564065.plantswap.activities.browseplant.BrowsePlant_Details_fragment;
import com.au564065.plantswap.models.Plant;
import com.au564065.plantswap.models.Wish;
import com.au564065.plantswap.ui.recyclerview.MyWishAdapter;
import com.au564065.plantswap.ui.recyclerview.PlantAdapter;
import com.au564065.plantswap.viewmodels.MyWishViewModel;
import com.au564065.plantswap.viewmodels.PlantViewModel;

import java.util.List;

public class MyWishList extends Fragment implements MyWishAdapter.ItemClickedListener {

    private MyWishViewModel wvm;
    private RecyclerView listRecycler;
    private MyWishAdapter adapter;
    private RecyclerView.LayoutManager layoutMan;
    private Button exitBtn;
    private Button addBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wishlist, container, false);
        listRecycler = v.findViewById(R.id.wishList);
        addBtn = v.findViewById(R.id.addWishButton);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddClicked();
            }
        });
        exitBtn = v.findViewById(R.id.exitWishButton);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new MyWishAdapter(this);
        layoutMan = new LinearLayoutManager(getContext());
        listRecycler.setAdapter(adapter);
        listRecycler.setLayoutManager(layoutMan);
        wvm = new ViewModelProvider(getActivity()).get(MyWishViewModel.class);
        wvm.getAllWishes().observe(getViewLifecycleOwner(), new Observer<List<Wish>>() {
            @Override
            public void onChanged(List<Wish> wishes) {
                adapter.updateList(wishes);
                wvm.saveList(wishes);
            }
        });
    }



    @Override
    public void onWishClicked(int index) {
        wvm.OnClicked(index);

        FragmentManager m = getActivity().getSupportFragmentManager();
        m.beginTransaction()
                .replace(R.id.wishList, new EditWish())
                .commit();


    }


    public void onAddClicked() {
        FragmentManager m = getActivity().getSupportFragmentManager();
        m.beginTransaction()
                .replace(R.id.wishList, new AddWish())
                .commit();
    }

}

