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
        binding.getRoot().setOnClickListener(l -> {
            System.out.println(currentItem.getTransactionId());
            Query qTransactionsDay = FirebaseFirestore.getInstance()
                    .collection("bankAccount")
                    .document(currentItem.getBankId())
                    .collection("transaction")
                    .whereEqualTo("transactionId", currentItem.getTransactionId());
            FirestoreRecyclerOptions<Transaction> options = new FirestoreRecyclerOptions.Builder<Transaction>().setQuery(qTransactionsDay, Transaction.class).setLifecycleOwner(parentFragment.getParentFragment()).build();
            parentFragment.requireView().findViewById(R.id.chatbotBtn).setVisibility(View.INVISIBLE);
            ((RecyclerView) parentFragment.requireView().findViewById(R.id.fragmentTransactionCardsBank).findViewById(R.id.recyclerview_transactionCards)).setAdapter(new TransactionAdapter(options, parentFragment.getParentFragment(), true));
            parentFragment.requireView().findViewById(R.id.fragmentTransactionCardsBank).setVisibility(View.VISIBLE);
        });
    }
}
