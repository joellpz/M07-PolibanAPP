package m07.joellpz.poliban.adapter;


import static androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.ArrayList;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.model.ChatsModal;

public class ChatRVAdapter extends Adapter<ViewHolder> {

    private final ArrayList<ChatsModal> chatsModalArrayList;

    public ChatRVAdapter(ArrayList<ChatsModal> chatsModalArrayList) {
        this.chatsModalArrayList = chatsModalArrayList;
    }

    // ViewHolder -> Describes an item view and metadata about its place within the RecyclerView.
    // To Create ViewHolder and initialize private fields.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 0)
            return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_chatbot_user_msg, parent, false));
        else
            return new BotViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_chatbot_bot_msg, parent, false));

    }

    // To Update ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatsModal chatsModal = chatsModalArrayList.get(position);
        switch (chatsModal.getSender()) {
            case "user":
                ((UserViewHolder) holder).userTv.setText(chatsModal.getMessage());
                ((UserViewHolder) holder).userTv.setVisibility(View.VISIBLE);
                break;
            case "bot":
                ((BotViewHolder) holder).botMsgTv.setText(chatsModal.getMessage());
                ((BotViewHolder) holder).botMsgTv.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (chatsModalArrayList.get(position).getSender()) {
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return chatsModalArrayList.size();
    }

    // ViewHolder -> Describes an item view and metadata about its place within the RecyclerView.
    public static class UserViewHolder extends ViewHolder {
        final TextView userTv;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userTv = itemView.findViewById(R.id.idTVUser);
        }
    }

    // ViewHolder -> Describes an item view and metadata about its place within the RecyclerView.
    public static class BotViewHolder extends ViewHolder {
        final TextView botMsgTv;

        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            botMsgTv = itemView.findViewById(R.id.idTVBot);
        }
    }
}
