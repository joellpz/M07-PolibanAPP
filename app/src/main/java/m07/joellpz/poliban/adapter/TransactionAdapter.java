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

/**
 * Adapter for displaying a list of transactions in a RecyclerView.
 */
public class TransactionAdapter extends FirestoreRecyclerAdapter<Transaction, RecyclerView.ViewHolder> {
    /**
     * Parent Fragment
     */
    private final Fragment parentFragment;
    /**
     * Check if it is a Card Mode or not
     */
    private final boolean isCard;

    /**
     * Constructs a new TransactionAdapter with the given options, parent fragment, and card layout flag.
     *
     * @param options         The FirestoreRecyclerOptions for transactions
     * @param parentFragment  The parent fragment
     * @param isCard          Flag indicating whether to use the card layout
     */
    public TransactionAdapter(@NonNull FirestoreRecyclerOptions<Transaction> options, Fragment parentFragment, boolean isCard) {
        super(options);
        this.parentFragment = parentFragment;
        this.isCard = isCard;
    }

    /**
     * Called to bind the data to the given ViewHolder at the specified position.
     *
     * @param holder   The ViewHolder to bind
     * @param position The position of the item
     * @param model    The transaction model
     */
    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Transaction model) {
        if (isCard) {
            ((TransactionCardViewHolder) holder).bind(model);
        } else {
            ((TransactionViewHolder) holder).bind(model);
        }
    }

    /**
     * Called when a new ViewHolder is needed for the given view type.
     *
     * @param parent   The parent ViewGroup
     * @param viewType The view type of the new ViewHolder
     * @return A new ViewHolder for the given view type
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isCard) {
            return new TransactionCardViewHolder(ViewholderTransactionCardBinding.inflate(parentFragment.getLayoutInflater(), parent, false), parentFragment);
        } else {
            return new TransactionViewHolder(ViewholderTransactionBinding.inflate(parentFragment.getLayoutInflater(), parent, false), parentFragment);
        }
    }
}
