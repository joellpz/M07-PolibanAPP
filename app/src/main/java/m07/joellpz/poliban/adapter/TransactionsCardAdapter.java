package m07.joellpz.poliban.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import m07.joellpz.poliban.databinding.ViewholderTransactionCardBinding;
import m07.joellpz.poliban.model.Transaction;
import m07.joellpz.poliban.adapter.viewHolders.TransactionCardViewHolder;

public class TransactionsCardAdapter extends RecyclerView.Adapter<TransactionCardViewHolder> {
    private final List<Transaction> mExampleList;
    private final Fragment parentFragment;
    public TransactionsCardAdapter(List<Transaction> exampleList, Fragment parentFragment) {
        mExampleList = exampleList;
        this.parentFragment = parentFragment;
    }

    @NonNull
    @Override
    public TransactionCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionCardViewHolder(ViewholderTransactionCardBinding.inflate(parentFragment.getLayoutInflater(), parent, false), parentFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionCardViewHolder holder, int position) {
        ((TransactionCardViewHolder) holder).bind(mExampleList.get(position));

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}