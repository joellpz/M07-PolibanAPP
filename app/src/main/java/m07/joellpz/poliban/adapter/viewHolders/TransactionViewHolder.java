package m07.joellpz.poliban.adapter.viewHolders;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.adapter.TransactionsAdapter;
import m07.joellpz.poliban.databinding.ViewholderTransactionBinding;
import m07.joellpz.poliban.model.Transaction;

public class TransactionViewHolder extends RecyclerView.ViewHolder {

    private final ViewholderTransactionBinding binding;
    private final Fragment parentFragment;

    public TransactionViewHolder(ViewholderTransactionBinding binding, Fragment parentFragment) {
        super(binding.getRoot());
        this.binding = binding;
        this.parentFragment = parentFragment;
    }

    public void bind(Transaction currentItem) {
        if (currentItem.getValue() > 0)
            binding.imageTransaction.setImageResource(R.drawable.money_in);
        else binding.imageTransaction.setImageResource(R.drawable.money_out);

        binding.subjectTransaction.setText(currentItem.getSubject());
        binding.dateTransaction.setText(currentItem.getDateFormatted());
        binding.priceTransaction.setText(currentItem.getValueString());

        binding.mainTransactionLayout.setOnClickListener(l -> {
                    Query qTransactionsOne = FirebaseFirestore.getInstance()
                            .collection("bankAccount")
                            .document(currentItem.getBankId())
                            .collection("transaction")
                            .whereEqualTo("transactionId", currentItem.getTransactionId());
                    FirestoreRecyclerOptions<Transaction> options = new FirestoreRecyclerOptions.Builder<Transaction>()
                            .setQuery(qTransactionsOne, Transaction.class)
                            .setLifecycleOwner(parentFragment.getParentFragment())
                            .build();

                    RecyclerView rvTransactionCards = parentFragment.requireView().findViewById(R.id.recyclerview_transactionCards);
                    rvTransactionCards.setAdapter(new TransactionsAdapter(options, parentFragment, true));

                    parentFragment.requireView().findViewById(R.id.fragmentTransactionCards).setVisibility(View.VISIBLE);
                }
        );
    }
}