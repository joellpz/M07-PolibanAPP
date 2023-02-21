package m07.joellpz.poliban;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.model.Transaction;
import m07.joellpz.poliban.model.WalletCard;

public class WalletFragment extends Fragment {

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    static class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletViewHolder> {

        private ArrayList<BankAccount> mExampleList;

        public WalletAdapter(ArrayList<BankAccount> exampleList) {
            mExampleList = exampleList;
        }

        @NonNull
        @Override
        public WalletAdapter.WalletViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_wallet, parent, false);
            return new WalletAdapter.WalletViewHolder(v);
        }
        //TODO https://www.section.io/engineering-education/android-nested-recycler-view/
        @Override
        public void onBindViewHolder(@NonNull WalletAdapter.WalletViewHolder holder, int position) {
            BankAccount currentItem = mExampleList.get(position);
        }

        @Override
        public int getItemCount() {
            return mExampleList.size();
        }

        class WalletViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;

            public WalletViewHolder(View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.whoseAccountText);
            }
        }

    }

    static class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

        private ArrayList<WalletCard> mExampleList;

        public CardAdapter(ArrayList<WalletCard> exampleList) {
            mExampleList = exampleList;
        }

        @NonNull
        @Override
        public CardAdapter.CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_wallet_card, parent, false);
            return new CardAdapter.CardViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CardAdapter.CardViewHolder holder, int position) {
            WalletCard currentItem = mExampleList.get(position);
        }

        @Override
        public int getItemCount() {
            return mExampleList.size();
        }

        class CardViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;

            public CardViewHolder(View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.whoseTextCard);
            }
        }
    }
}