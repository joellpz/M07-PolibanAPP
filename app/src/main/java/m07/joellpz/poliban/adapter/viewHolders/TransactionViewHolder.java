package m07.joellpz.poliban.adapter.viewHolders;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.adapter.TransactionAdapter;
import m07.joellpz.poliban.databinding.ViewholderTransactionBinding;
import m07.joellpz.poliban.model.Transaction;

/**
 * ViewHolder class for Transaction items in a RecyclerView.
 */
public class TransactionViewHolder extends RecyclerView.ViewHolder {
    /**
     * View binding for the fragment.
     */
    private final ViewholderTransactionBinding binding;
    /**
     * Parent Fragment
     */
    private final Fragment parentFragment;

    /**
     * Constructs a new TransactionViewHolder.
     *
     * @param binding          The ViewholderTransactionBinding object associated with this ViewHolder.
     * @param parentFragment   The parent Fragment that holds the RecyclerView.
     */
    public TransactionViewHolder(ViewholderTransactionBinding binding, Fragment parentFragment) {
        super(binding.getRoot());
        this.binding = binding;
        this.parentFragment = parentFragment;
    }

    /**
     * Binds a Transaction object to the ViewHolder.
     *
     * @param currentItem  The Transaction object to bind.
     */
    public void bind(Transaction currentItem) {
        // Set transaction details
        if (currentItem.getValue() > 0)
            binding.imageTransaction.setImageResource(R.drawable.money_in);
        else
            binding.imageTransaction.setImageResource(R.drawable.money_out);
        binding.subjectTransaction.setText(currentItem.getSubject());
        binding.dateTransaction.setText(currentItem.getDateFormatted());
        binding.priceTransaction.setText(currentItem.getValueString());

        // Set click listener to show transaction details
        binding.mainTransactionLayout.setOnClickListener(l -> {
            // Query Firestore for transaction details
            Query qTransactionsOne = FirebaseFirestore.getInstance()
                    .collection("bankAccount")
                    .document(currentItem.getBankId())
                    .collection("transaction")
                    .whereEqualTo("transactionId", currentItem.getTransactionId());
            FirestoreRecyclerOptions<Transaction> options = new FirestoreRecyclerOptions.Builder<Transaction>()
                    .setQuery(qTransactionsOne, Transaction.class)
                    .setLifecycleOwner(parentFragment.getParentFragment())
                    .build();

            // Set TransactionAdapter for the RecyclerView
            RecyclerView rvTransactionCards = parentFragment.requireView().findViewById(R.id.recyclerview_transactionCards);
            rvTransactionCards.setAdapter(new TransactionAdapter(options, parentFragment, true));

            // Show the transaction details fragment
            parentFragment.requireView().findViewById(R.id.fragmentTransactionCards).setVisibility(View.VISIBLE);
        });
    }
}
