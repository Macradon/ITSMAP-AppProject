package com.au564065.plantswap.ui.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.au564065.plantswap.database.Repository;
import com.au564065.plantswap.models.Wish;
import com.au564065.plantswap.R;

import java.util.List;

public class MyWishAdapter extends RecyclerView.Adapter<MyWishAdapter.ViewHolder> {
    private Repository repos;
    private List<Wish> wishList;
    private Context context;

    public interface ItemClickedListener{
        void onWishClicked(int index);
    }

    private ItemClickedListener listener;

    public MyWishAdapter(ItemClickedListener listener) {
        this.listener = listener;
        }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtName;
        public TextView txtRadius;

        ItemClickedListener listener;

        public ViewHolder(@NonNull View itemView, ItemClickedListener clickedListener) {
            super(itemView);
            txtName = itemView.findViewById(R.id.plantName);
            txtRadius = itemView.findViewById(R.id.radius);
            listener = clickedListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onWishClicked(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyWishAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_my_wish, parent, false);
        ViewHolder vh = new ViewHolder(v, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Wish tempWish = wishList.get(position);
        for(int i = 0; i < wishList.size(); i++) {
            holder.txtName.setText(wishList.get(position).getPlantName());
            holder.txtRadius.setText(String.valueOf(tempWish.getRadius()));
        }
    }

    @Override
    public int getItemCount() {
        return wishList.size();
    }

}
