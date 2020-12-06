package com.au564065.plantswap.ui.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.au564065.plantswap.R;
import com.au564065.plantswap.database.Repository;
import com.au564065.plantswap.models.Chatroom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {

    final private static int USER_SIDE = 0;
    final private static int TARGET_SIDE = 1;

    private Chatroom chatroom;
    private Repository repo;
    private Context context;

    public ChatAdapter(Context context, Chatroom chatroom){
        repo = Repository.getInstance(context);
        this.chatroom = chatroom;
        this.context = context;

    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == USER_SIDE){
            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            ChatHolder holder = new ChatHolder(v);
            return holder;
        }else {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            ChatHolder holder = new ChatHolder(v);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {

        holder.name.setText(chatroom.getMessageList().get(position).getName());
        holder.message.setText(chatroom.getMessageList().get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return chatroom.getMessageList().size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder{

        private TextView name, message;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.user_name);
            message = itemView.findViewById(R.id.show_message);

        }
    }

    @Override
    public int getItemViewType(int position) {
        String sender = chatroom.getMessageList().get(position).getName();
        if(sender == repo.getCurrentUser().getValue().getName()){
            return USER_SIDE;
        } else{
            return TARGET_SIDE;
        }
    }
}
