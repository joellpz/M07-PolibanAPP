package m07.joellpz.poliban.adapter.viewHolders;

import android.view.View;

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
import m07.joellpz.poliban.databinding.ViewholderTransactionBinding;
import m07.joellpz.poliban.model.Transaction;

public class TransactionViewHolder extends RecyclerView.ViewHolder {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
    private final ViewholderTransactionBinding binding;
    private final Fragment parentFragment;
    TransactionsAdapter mainAdapter, explicitAdapter, explicitFutureAdapter;

    float totalBalanceMonth, totalComeMonth;
    List<Transaction> transactionsPerMonth = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("#.##");

    List<Transaction> transactionsToCards = new ArrayList<>();

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
        binding.dateTransaction.setText(dateFormat.format(currentItem.getDate()));
        binding.priceTransaction.setText(currentItem.getValueString());

        binding.mainTransactionLayout.setOnClickListener(l -> {
                    transactionsToCards.clear();
                    transactionsToCards.add(currentItem);
                    TransactionsCardAdapter adapter = new TransactionsCardAdapter(transactionsToCards, parentFragment);
                    RecyclerView rvTransactionCards = parentFragment.getView().findViewById(R.id.recyclerview_transactionCards);
                    rvTransactionCards.setAdapter(adapter);
                    parentFragment.getView().findViewById(R.id.fragmentTransactionCards).setVisibility(View.VISIBLE);
                }
        );
    }
}