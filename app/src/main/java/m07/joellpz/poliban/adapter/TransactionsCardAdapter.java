package m07.joellpz.poliban.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.databinding.ViewholderTransactionCardBinding;
import m07.joellpz.poliban.model.Transaction;
import m07.joellpz.poliban.view.IbanMainFragment;
import m07.joellpz.poliban.viewHolders.TransactionCardViewHolder;
import m07.joellpz.poliban.viewHolders.TransactionViewHolder;

public class TransactionsCardAdapter extends RecyclerView.Adapter<TransactionCardViewHolder> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
    private final List<Transaction> mExampleList;
    private final Fragment parentFragment;
    TransactionsAdapter mainAdapter, explicitAdapter, explicitFutureAdapter;

    float totalBalanceMonth, totalComeMonth;
    List<Transaction> transactionsPerMonth = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("#.##");

    List<Transaction> transactionsToCards = new ArrayList<>();

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