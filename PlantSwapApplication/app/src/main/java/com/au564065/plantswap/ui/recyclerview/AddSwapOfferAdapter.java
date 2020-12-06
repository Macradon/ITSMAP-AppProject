package com.au564065.plantswap.ui.recyclerview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.au564065.plantswap.R;
import com.au564065.plantswap.models.SwapOffer;

import java.util.ArrayList;
import java.util.List;

public class AddSwapOfferAdapter extends RecyclerView.Adapter<AddSwapOfferAdapter.SwapOfferHolder>{

    private List<String> offer;
    private List<Integer> theList;
    private List<String> savedStrings;


    public AddSwapOfferAdapter(List<String> offerList){
        offer = offerList;
        theList = new ArrayList<Integer>(1);
        savedStrings = new ArrayList<>(1);
    }

    public void updateList(int size){
        if(getItemCount() > size){
            for(int i = getItemCount(); i > size; i-- ){
                savedStrings.remove(getItemCount()-1);
                theList.remove(getItemCount()-1);
            }
        }else {
            for (int i = getItemCount(); i < size; i++) {
                savedStrings.add("");
                theList.add(1);
            }
        }
        notifyDataSetChanged();
    }

    public List<String> getSavedStrings(){
        return savedStrings;
    }

    @NonNull
    @Override
    public SwapOfferHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.swapoffer_item,parent,false);
        AddSwapOfferAdapter.SwapOfferHolder holder = new AddSwapOfferAdapter.SwapOfferHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SwapOfferHolder holder, int position) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(holder.itemView.getContext(),
                android.R.layout.simple_list_item_1,
                offer);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.wishes.setAdapter(adapter);

        holder.customWish.setText("Plant name here");


        holder.customWish.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (holder.wishes.getSelectedItem().equals("other")) {
                    savedStrings.set(position, holder.customWish.getText().toString());

                }
            }
        });

        holder.wishes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(holder.wishes.getSelectedItem() != "other"){
                    savedStrings.set(position, holder.wishes.getSelectedItem().toString());
                }else{
                    savedStrings.set(position, holder.customWish.getText().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return theList.size();
    }


    public class SwapOfferHolder extends RecyclerView.ViewHolder{

        private Spinner wishes;
        private EditText customWish;


        public SwapOfferHolder(@NonNull View itemView) {
            super(itemView);

            wishes = itemView.findViewById(R.id.swapOfferSpinner);
            customWish = itemView.findViewById(R.id.swapOffer_edittext);

            //placeholder = itemView.findViewById(R.id.swapItem_img);
        }

    }

}
