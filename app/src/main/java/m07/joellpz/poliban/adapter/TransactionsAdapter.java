package m07.joellpz.poliban.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import m07.joellpz.poliban.adapter.viewHolders.TransactionCardViewHolder;
import m07.joellpz.poliban.adapter.viewHolders.TransactionViewHolder;
import m07.joellpz.poliban.databinding.ViewholderTransactionBinding;
import m07.joellpz.poliban.databinding.ViewholderTransactionCardBinding;
import m07.joellpz.poliban.model.Transaction;

public class TransactionsAdapter extends FirestoreRecyclerAdapter<Transaction, RecyclerView.ViewHolder> {
    private final Fragment parentFragment;
    private final boolean isCard;

    public TransactionsAdapter(@NonNull FirestoreRecyclerOptions<Transaction> options, Fragment parentFragment,boolean isCard) {
        super(options);
        this.parentFragment = parentFragment;
        this.isCard = isCard;

    }
    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Transaction model) {
        if (isCard) ((TransactionCardViewHolder) holder).bind(model);
        else ((TransactionViewHolder) holder).bind(model);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isCard)
            return new TransactionCardViewHolder(ViewholderTransactionCardBinding.inflate(parentFragment.getLayoutInflater(), parent, false), parentFragment);
        else
            return new TransactionViewHolder(ViewholderTransactionBinding.inflate(parentFragment.getLayoutInflater(), parent, false), parentFragment);
    }
}