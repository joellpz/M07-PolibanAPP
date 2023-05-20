package m07.joellpz.poliban.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import m07.joellpz.poliban.adapter.viewHolders.BankAccountViewHolder;
import m07.joellpz.poliban.adapter.viewHolders.RegisterBankAccountViewHolder;
import m07.joellpz.poliban.adapter.viewHolders.TransactionCardViewHolder;
import m07.joellpz.poliban.adapter.viewHolders.TransactionViewHolder;
import m07.joellpz.poliban.databinding.ViewholderBankAccountBinding;
import m07.joellpz.poliban.databinding.ViewholderRegisterBankAccountBinding;
import m07.joellpz.poliban.databinding.ViewholderTransactionBinding;
import m07.joellpz.poliban.databinding.ViewholderTransactionCardBinding;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.model.Transaction;

public class TransactionsAdapter extends FirestoreRecyclerAdapter<Transaction, RecyclerView.ViewHolder> {
    private Fragment parentFragment;
    private final boolean isCard;

    public TransactionsAdapter(@NonNull FirestoreRecyclerOptions<Transaction> options, Fragment parentFragment,boolean isCard) {
        super(options);
        this.parentFragment = parentFragment;
        this.isCard = isCard;

    }
    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Transaction model) {
        super.onBindViewHolder(holder, position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isCard)
            return new TransactionCardViewHolder(ViewholderTransactionCardBinding.inflate(parentFragment.getLayoutInflater(), parent, false), parentFragment);
        else
            return new TransactionViewHolder(ViewholderTransactionBinding.inflate(parentFragment.getLayoutInflater(), parent, false), parentFragment);

    }


//    public TransactionsAdapter(List<Transaction> exampleList, Fragment parentFragment) {
//        mExampleList = exampleList;
//        this.parentFragment = parentFragment;
//    }
//
//    @NonNull
//    @Override
//    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new TransactionViewHolder(ViewholderTransactionBinding.inflate(parentFragment.getLayoutInflater(), parent, false), parentFragment);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
//        ((TransactionViewHolder) holder).bind(mExampleList.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return mExampleList.size();
//    }


}