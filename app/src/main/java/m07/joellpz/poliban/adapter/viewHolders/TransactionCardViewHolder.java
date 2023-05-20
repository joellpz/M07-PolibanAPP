package m07.joellpz.poliban.adapter.viewHolders;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.adapter.TransactionsAdapter;
import m07.joellpz.poliban.adapter.TransactionsCardAdapter;
import m07.joellpz.poliban.databinding.ViewholderTransactionCardBinding;
import m07.joellpz.poliban.model.Transaction;

public class TransactionCardViewHolder extends RecyclerView.ViewHolder {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
    private final ViewholderTransactionCardBinding binding;
    private final Fragment parentFragment;
    TransactionsAdapter mainAdapter, explicitAdapter, explicitFutureAdapter;

    float totalBalanceMonth, totalComeMonth;
    List<Transaction> transactionsPerMonth = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("#.##");

    List<Transaction> transactionsToCards = new ArrayList<>();
    public TransactionCardViewHolder(ViewholderTransactionCardBinding binding,Fragment parentFragment) {
        super(binding.getRoot());
        this.binding = binding;
        this.parentFragment = parentFragment;
    }
    public void bind(Transaction currentItem){
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
        else binding.subjectTransaction.setText(currentItem.getSubject());


        binding.dateTransaction.setText(dateFormat.format(currentItem.getDate()));
        binding.priceTransaction.setText(currentItem.getValueString());
        binding.valoracion.setRating(currentItem.getOpinion());

        binding.valoracion.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                currentItem.setOpinion(rating);
            }
        });
    }
}
