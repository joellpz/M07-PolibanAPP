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
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatBotFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatBotFragment extends Fragment {
    //IA lINK: https://brainshop.ai/brain/172691/settingss

    private RecyclerView chatsRv;
    private EditText userMsgEdt;
    private final String BOT_KEY = "bot";
    private ArrayList<ChatsModal> ChatsModalArrayList;
    private ChatRVAdapter ChatRVAdapter;
    private NavController navController;

    public ChatBotFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        requireActivity().findViewById(R.id.bottomMainMenu).setVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_chat_bot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        view.findViewById(R.id.goBackBtn).setOnClickListener(l -> navController.popBackStack());

        chatsRv = view.findViewById(R.id.idRVChats);
        userMsgEdt = view.findViewById(R.id.idEdtMessage);
        FloatingActionButton sendMsgFAB = view.findViewById(R.id.idFABSend);

        ChatsModalArrayList = new ArrayList<>();
        ChatRVAdapter = new ChatRVAdapter(ChatsModalArrayList);

        // LinearLayoutManager -> Provides similar function to listView.
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        manager.setStackFromEnd(true);
        chatsRv.setLayoutManager(manager);
        chatsRv.setItemAnimator(new DefaultItemAnimator());
        chatsRv.setAdapter(ChatRVAdapter);

        sendMsgFAB.setOnClickListener(v -> {
            if (userMsgEdt.getText().toString().isEmpty()) {
                Toast.makeText(view.getContext(), "Please enter your message", Toast.LENGTH_SHORT).show();
                return;
            }
            getResponse(userMsgEdt.getText().toString());
            userMsgEdt.setText("");
        });

    }

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

