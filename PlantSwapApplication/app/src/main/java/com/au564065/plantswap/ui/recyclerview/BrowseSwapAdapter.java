package com.au564065.plantswap.ui.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.au564065.plantswap.R;
import com.au564065.plantswap.models.Swap;

import java.util.ArrayList;
import java.util.List;

public class BrowseSwapAdapter extends RecyclerView.Adapter<BrowseSwapAdapter.SwapsHolder> implements Filterable {

    private List<Swap> swaps = new ArrayList<>();
    public List<Swap> SwapsCopy;

    public interface ISwapClickListener{
        void onSwapClicked(int index);
    }

    ISwapClickListener listener;

    public BrowseSwapAdapter(ISwapClickListener listener){

        this.listener = listener;

    }

    public void updateList(List<Swap> swaps){
        this.swaps = swaps;
        SwapsCopy = new ArrayList<>(swaps);
        notifyDataSetChanged();
    }

    private boolean checkSwapsCopy(){
        if(SwapsCopy != null){
            return true;
        }else{
            return false;
        }
    }


    @NonNull
    @Override
    public SwapsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse_swap_item,parent,false);
        SwapsHolder holder = new SwapsHolder(v, listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SwapsHolder holder, int position) {
        Swap currentSwap = swaps.get(position);
        holder.name.setText(currentSwap.getPlantName());
    }

    @Override
    public int getItemCount() {
        if(swaps == null){
            return 0;
        }else {
            return swaps.size();
        }
    }

    class SwapsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private ImageView placeholder;
        private ISwapClickListener listener;

        public SwapsHolder(@NonNull View itemView, ISwapClickListener itemClickedListener) {
            super(itemView);

            name = itemView.findViewById(R.id.swapItem_name);
            //placeholder = itemView.findViewById(R.id.swapItem_img);
            listener = itemClickedListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            listener.onSwapClicked(getAdapterPosition());
        }
    }

    public Filter getFilter() {
        return SwapFilter;
    }

    private Filter SwapFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Swap> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(SwapsCopy);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Swap item : SwapsCopy){
                    if (item.getPlantName().toLowerCase().startsWith(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if(checkSwapsCopy()) {
                swaps.clear();
                swaps.addAll((List) filterResults.values);
                notifyDataSetChanged();
            }else{

            }
        }
    };

}
