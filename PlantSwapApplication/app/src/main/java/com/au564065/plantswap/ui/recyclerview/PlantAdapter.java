package com.au564065.plantswap.ui.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.au564065.plantswap.R;
import com.au564065.plantswap.models.Plant;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantItemViewHolder>{

    public interface IPlantItemClickedListener{
        void onItemClicked(int index);
    }

    private List<Plant> plants;
    private IPlantItemClickedListener itemClickedListener;

    public PlantAdapter(IPlantItemClickedListener listener){
        itemClickedListener = listener;
    }

    public void updateList(List<Plant> plants){
        this.plants = plants;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlantItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plant_item, parent, false);

        PlantItemViewHolder vh = new PlantItemViewHolder(v, itemClickedListener);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlantItemViewHolder holder, int position) {

        holder.common.setText(plants.get(position).getCommonName());
        Glide.with(holder.imgurl.getContext())
                .load(plants.get(position).getImageURL())
                .centerCrop()
                .into(holder.imgurl);
    }

    @Override
    public int getItemCount() {
        if(plants == null){
            return 0;
        }
        else {
            return plants.size();
        }
    }


    public class PlantItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView common;
        ImageView imgurl; // temp
        IPlantItemClickedListener list;

        public PlantItemViewHolder(@NonNull View itemView, IPlantItemClickedListener listener) {
            super(itemView);

            common = itemView.findViewById(R.id.plantItem_common);
            imgurl = itemView.findViewById(R.id.plantItem_image);

            list = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            list.onItemClicked(getAdapterPosition());
        }
    }
}