package m07.joellpz.poliban;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatBotFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatBotFragment extends Fragment {
    //IA lINK: https://brainshop.ai/brain/172691/settingss

    private EditText userMsgEdt;
    private final String BOT_KEY = "bot";

    // creating a variable for array list and adapter class.
    private ArrayList<MessageModal> messageModalArrayList;
    private MessageRVAdapter messageRVAdapter;

    public ChatBotFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("GOLA");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_bot, container, false);
        RecyclerView chatsRV = view.findViewById(R.id.idRVChats);
        ImageButton sendMsgIB = view.findViewById(R.id.idIBSend);
        userMsgEdt = view.findViewById(R.id.idEdtMessage);

        // below line is to initialize our request queue.
        // creating a variable for
        // our volley request queue.
        RequestQueue mRequestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
        mRequestQueue.getCache().clear();

        // creating a new array list
        messageModalArrayList = new ArrayList<>();

        // adding on click listener for send message button.
        sendMsgIB.setOnClickListener(v -> {
            // checking if the message entered
            // by user is empty or not.
            if (userMsgEdt.getText().toString().isEmpty()) {
                // if the edit text is empty display a toast message.
                Toast.makeText(getActivity().getApplicationContext(), "Introduce tu mensaje", Toast.LENGTH_SHORT).show();
                return;
            }

            // calling a method to send message
            // to our bot to get response.
            sendMessage(userMsgEdt.getText().toString());

            // below line we are setting text in our edit text as empty
            userMsgEdt.setText("");
        });

        // on below line we are initializing our adapter class and passing our array list to it.
        messageRVAdapter = new MessageRVAdapter(messageModalArrayList, getActivity().getApplicationContext());

        // below line we are creating a variable for our linear layout manager.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false);

        // below line is to set layout
        // manager to our recycler view.
        chatsRV.setLayoutManager(linearLayoutManager);

        // below line we are setting
        // adapter to our recycler view.
        chatsRV.setAdapter(messageRVAdapter);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void sendMessage(String userMsg) {
        // below line is to pass message to our
        // array list which is entered by the user.
        String USER_KEY = "user";
        messageModalArrayList.add(new MessageModal(userMsg, USER_KEY));
        messageRVAdapter.notifyDataSetChanged();

        // url for our brain
        // make sure to add mshape for uid.
        // make sure to add your url.
        String url = "http://api.brainshop.ai/get?bid=172691&key=0pGuSen9Tjrb1vhc&uid=[uid]&msg=" + userMsg;

        // creating a variable for our request queue.
        RequestQueue queue = Volley.newRequestQueue(requireActivity().getApplicationContext());

        // on below line we are making a json object request for a get request and passing our url .
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                // in on response method we are extracting data
                // from json response and adding this response to our array list.
                String botResponse = response.getString("cnt");
                messageModalArrayList.add(new MessageModal(botResponse, BOT_KEY));

                // notifying our adapter as data changed.
                messageRVAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();

                // handling error response from bot.
                messageModalArrayList.add(new MessageModal("No response", BOT_KEY));
                messageRVAdapter.notifyDataSetChanged();
            }
        }, error -> {
            // error handling.
            messageModalArrayList.add(new MessageModal("Sorry no response found", BOT_KEY));
            Toast.makeText(requireActivity().getApplicationContext(), "No response from the bot..", Toast.LENGTH_SHORT).show();
        });

        // at last adding json object
        // request to our queue.
        queue.add(jsonObjectRequest);
    }

    static class MessageModal {

        // string to store our message and sender
        private String message;
        private String sender;

        // constructor.
        public MessageModal(String message, String sender) {
            this.message = message;
            this.sender = sender;
        }

        // getter and setter methods.
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }
    }

    static class MessageRVAdapter extends RecyclerView.Adapter {

        // variable for our array list and context.
        private final ArrayList<MessageModal> messageModalArrayList;
        private final Context context;

        // constructor class.
        public MessageRVAdapter(ArrayList<MessageModal> messageModalArrayList, Context context) {
            this.messageModalArrayList = messageModalArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            // below code is to switch our
            // layout type along with view holder.
            switch (viewType) {
                case 0:
                    // below line we are inflating user message layout.
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg_chatbot, parent, false);
                    return new UserViewHolder(view);
                case 1:
                    // below line we are inflating bot message layout.
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_msg_chatbot, parent, false);
                    return new BotViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            // this method is use to set data to our layout file.
            MessageModal modal = messageModalArrayList.get(position);
            switch (modal.getSender()) {
                case "user":
                    // below line is to set the text to our text view of user layout
                    ((UserViewHolder) holder).userTV.setText(modal.getMessage());
                    break;
                case "bot":
                    // below line is to set the text to our text view of bot layout
                    ((BotViewHolder) holder).botTV.setText(modal.getMessage());
                    break;
            }
        }

        @Override
        public int getItemCount() {
            // return the size of array list
            return messageModalArrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            // below line of code is to set position.
            switch (messageModalArrayList.get(position).getSender()) {
                case "user":
                    return 0;
                case "bot":
                    return 1;
                default:
                    return -1;
            }
        }

        public class UserViewHolder extends RecyclerView.ViewHolder {

            // creating a variable
            // for our text view.
            TextView userTV;

            public UserViewHolder(@NonNull View itemView) {
                super(itemView);
                // initializing with id.
                userTV = itemView.findViewById(R.id.idTVUser);
            }
        }

        public class BotViewHolder extends RecyclerView.ViewHolder {

            // creating a variable
            // for our text view.
            TextView botTV;

            public BotViewHolder(@NonNull View itemView) {
                super(itemView);
                // initializing with id.
                botTV = itemView.findViewById(R.id.idTVBot);
            }
        }
    }
}

