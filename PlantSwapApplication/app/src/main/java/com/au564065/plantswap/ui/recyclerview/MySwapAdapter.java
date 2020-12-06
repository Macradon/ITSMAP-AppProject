package com.au564065.plantswap.ui.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.au564065.plantswap.R;
import com.au564065.plantswap.activities.myswap.SwapEditActivity;
import com.au564065.plantswap.models.Swap;

import java.util.List;

public class MySwapAdapter extends RecyclerView.Adapter<MySwapAdapter.ViewHolder> {
    private final Context context;
    private final LayoutInflater layoutInflater;
    private List<Swap> swapList;

    public MySwapAdapter(Context context, List<Swap> swaps) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        swapList = swaps;
    }

    public void setSwapList(List<Swap> swaps){
        swapList = swaps;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View swapItem = layoutInflater.inflate(R.layout.fragment_swap_list_item, parent,false);
        return new ViewHolder((swapItem));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Swap swap = swapList.get(position);
        holder.plantName.setText(swap.getPlantName());

        //needs updated db
     //   holder.swapId = swap.getId();
    }

    @Override
    public int getItemCount() {
        return swapList != null
                ? swapList.size()
                : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView plantName;
        public final ImageView photo;
        public String swapId = "";

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.swapListItemName);
            photo = itemView.findViewById(R.id.swapListItemPhoto);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, SwapEditActivity.class);
                intent.putExtra(SwapEditActivity.SWAP_EDIT_ID, swapId);
                context.startActivity(intent);
            });
        }
    }
}
