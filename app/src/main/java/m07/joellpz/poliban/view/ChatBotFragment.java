package m07.joellpz.poliban.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.adapter.ChatRVAdapter;
import m07.joellpz.poliban.model.ChatsModal;
import m07.joellpz.poliban.model.MsgModal;
import m07.joellpz.poliban.tools.RetrofitAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The ChatBotFragment class represents a fragment that allows users to chat with a chatbot.
 * It sends user messages to a chatbot API and displays the responses in a RecyclerView.
 * Bot AI - <a href="https://brainshop.ai">Brainshop.ai</a>
 */
public class ChatBotFragment extends Fragment {

    /**
     * RecyclerView for displaying chat messages
     */
    private RecyclerView chatsRv;
    /**
     * EditText for entering user messages
     */
    private EditText userMsgEdt;
    /**
     * Key for identifying bot messages
     */
    private final String BOT_KEY = "bot";
    /**
     * ArrayList for storing chat messages
     */
    private ArrayList<ChatsModal> ChatsModalArrayList;
    /**
     * RecyclerView adapter for chat messages
     */
    private ChatRVAdapter ChatRVAdapter;
    /**
     * Navigation controller for fragment navigation
     */
    private NavController navController;

    /**
     * Constructs a new instance of ChatBotFragment.
     */
    public ChatBotFragment() {
        // Required empty public constructor
    }

    /**
     * Creates the view for the fragment.
     *
     * @param inflater           the layout inflater
     * @param container          the container for the fragment
     * @param savedInstanceState the saved instance state
     * @return the fragment view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        requireActivity().findViewById(R.id.bottomMainMenu).setVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_chat_bot, container, false);
    }

    /**
     * Called after the view has been created.
     *
     * @param view               the created view
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        view.findViewById(R.id.goBackBtn).setOnClickListener(l -> navController.popBackStack());

        chatsRv = view.findViewById(R.id.idRVChats); // Initialize the RecyclerView for chat messages
        userMsgEdt = view.findViewById(R.id.idEdtMessage); // Initialize the EditText for user messages
        FloatingActionButton sendMsgFAB = view.findViewById(R.id.idFABSend); // Initialize the send message FAB

        ChatsModalArrayList = new ArrayList<>(); // Create an empty ArrayList for chat messages
        ChatRVAdapter = new ChatRVAdapter(ChatsModalArrayList); // Create a new RecyclerView adapter

        // LinearLayoutManager -> Provides similar function to listView.
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        manager.setStackFromEnd(true);
        chatsRv.setLayoutManager(manager); // Set the layout manager for the RecyclerView
        chatsRv.setItemAnimator(new DefaultItemAnimator());
        chatsRv.setAdapter(ChatRVAdapter); // Set the adapter for the RecyclerView

        sendMsgFAB.setOnClickListener(v -> {
            if (userMsgEdt.getText().toString().isEmpty()) {
                Toast.makeText(view.getContext(), "Please enter your message", Toast.LENGTH_SHORT).show();
                return;
            }
            getResponse(userMsgEdt.getText().toString());
            userMsgEdt.setText("");
        });

    }

    /**
     * Sends the user message to the chatbot API and retrieves the response.
     *
     * @param message the user message
     */
    @SuppressLint("NotifyDataSetChanged")
    private void getResponse(String message) {
        String USER_KEY = "user";
        ChatsModalArrayList.add(new ChatsModal(message, USER_KEY)); // User message;
        ChatRVAdapter.notifyDataSetChanged(); // To Notify that the data is changed.

        String url = "http://api.brainshop.ai/get?bid=172691&key=0pGuSen9Tjrb1vhc&uid=[uid]&msg=" + message;
        String BASE_URL = "http://api.brainshop.ai/";

        // Retrofit is a kind of REST API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<MsgModal> call = retrofitAPI.getMessage(url); // request have been made.
        call.enqueue(new Callback<MsgModal>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<MsgModal> call, @NonNull Response<MsgModal> response) {
                if (response.isSuccessful()) {
                    MsgModal modal = response.body();
                    ChatsModalArrayList.add(new ChatsModal(Objects.requireNonNull(modal).getCnt(), BOT_KEY));
                    ChatRVAdapter.notifyDataSetChanged();
                    Objects.requireNonNull(chatsRv.getLayoutManager()).smoothScrollToPosition(chatsRv, null, ChatRVAdapter.getItemCount() - 1);
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onFailure(@NonNull Call<MsgModal> call, @NonNull Throwable t) {
                String errorMessage = t.getMessage();
                Log.e("API Error", errorMessage);
                ChatsModalArrayList.add(new ChatsModal("Please revert your question", BOT_KEY));
                ChatRVAdapter.notifyDataSetChanged();
            }
        });
    }
}
