package m07.joellpz.poliban.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.model.ChatsModal;

/**
 * The ChatRVAdapter class is a RecyclerView adapter responsible for displaying chat messages in a chat interface.
 * It binds chat message data to the corresponding view holders and determines the view type based on the sender.
 */
public class ChatRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<ChatsModal> chatsModalArrayList;

    /**
     * Constructs a new ChatRVAdapter with the provided chat message data.
     *
     * @param chatsModalArrayList The list of chat message data.
     */
    public ChatRVAdapter(ArrayList<ChatsModal> chatsModalArrayList) {
        this.chatsModalArrayList = chatsModalArrayList;
    }

    /**
     * Called when a new ViewHolder object should be created and initialized.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The type of the view.
     * @return The created ViewHolder object.
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_chatbot_user_msg, parent, false));
        else
            return new BotViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_chatbot_bot_msg, parent, false));
    }

    /**
     * Called to bind the data to the ViewHolder at the specified position.
     *
     * @param holder   The ViewHolder to bind the data to.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
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

    /**
     * Returns the view type of the item at the specified position.
     *
     * @param position The position of the item within the adapter's data set.
     * @return The view type of the item.
     */
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

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items.
     */
    @Override
    public int getItemCount() {
        return chatsModalArrayList.size();
    }

    /**
     * ViewHolder class for displaying user chat messages.
     */
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        final TextView userTv;

        /**
         * Constructs a new UserViewHolder with the provided itemView.
         *
         * @param itemView The user chat message view.
         */
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userTv = itemView.findViewById(R.id.idTVUser);
        }
    }

    /**
     * ViewHolder class for displaying bot chat messages.
     */
    public static class BotViewHolder extends RecyclerView.ViewHolder {
        final TextView botMsgTv;

        /**
         * Constructs a new BotViewHolder with the provided itemView.
         *
         * @param itemView The bot chat message view.
         */
        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            botMsgTv = itemView.findViewById(R.id.idTVBot);
        }
    }
}
