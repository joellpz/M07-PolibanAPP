package m07.joellpz.poliban.adapter.viewHolders;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.databinding.ViewholderTransactionCardBinding;
import m07.joellpz.poliban.model.Transaction;

/**
 * ViewHolder class for Transaction Card items in a RecyclerView.
 */
public class TransactionCardViewHolder extends RecyclerView.ViewHolder {
    /**
     * Date Formatter (dd MMMM yyyy)
     */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
    /**
     * View binding for the fragment.
     */
    private final ViewholderTransactionCardBinding binding;
    /**
     * Parent Fragment
     */
    private final Fragment parentFragment;

    /**
     * Constructs a new TransactionCardViewHolder.
     *
     * @param binding        The ViewholderTransactionCardBinding object associated with this ViewHolder.
     * @param parentFragment The parent Fragment that holds the RecyclerView.
     */
    public TransactionCardViewHolder(ViewholderTransactionCardBinding binding, Fragment parentFragment) {
        super(binding.getRoot());
        this.binding = binding;
        this.parentFragment = parentFragment;
    }

    /**
     * Binds a Transaction object to the ViewHolder.
     *
     * @param currentItem The Transaction object to bind.
     */
    public void bind(Transaction currentItem) {
        // Set transaction details
        if (currentItem.getValue() > 0) {
            binding.fromText.setText(R.string.fromText);
            binding.euroBankInfo.setTextColor(parentFragment.getResources().getColor(R.color.green, parentFragment.requireActivity().getTheme()));
            binding.priceTransaction.setTextColor(parentFragment.getResources().getColor(R.color.green, parentFragment.requireActivity().getTheme()));
        } else {
            binding.fromText.setText(R.string.toText);
            binding.euroBankInfo.setTextColor(parentFragment.getResources().getColor(R.color.red_light, parentFragment.requireActivity().getTheme()));
            binding.priceTransaction.setTextColor(parentFragment.getResources().getColor(R.color.red_light, parentFragment.requireActivity().getTheme()));
        }

        binding.fromInfo.setText(currentItem.getFrom());

        if (currentItem.isFuture())
            binding.subjectTransaction.setText(String.format("%s%s", parentFragment.getString(R.string.futureText), currentItem.getSubject()));
        else
            binding.subjectTransaction.setText(currentItem.getSubject());

        binding.dateTransaction.setText(dateFormat.format(currentItem.getDate()));
        binding.priceTransaction.setText(currentItem.getValueString());
        binding.opinion.setRating(currentItem.getOpinion());

        // Set RatingBar change listener to update transaction opinion
        binding.opinion.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                FirebaseFirestore.getInstance().collection("bankAccount")
                        .document(currentItem.getBankId())
                        .collection("transaction")
                        .document(currentItem.getTransactionId())
                        .update("opinion", rating);
                currentItem.setOpinion(rating);
            }
        });
    }
}
