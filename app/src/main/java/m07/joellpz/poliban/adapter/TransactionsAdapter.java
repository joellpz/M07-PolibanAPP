package m07.joellpz.poliban.adapter;

import android.view.View;
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
import m07.joellpz.poliban.databinding.ViewholderTransactionBinding;
import m07.joellpz.poliban.model.Transaction;
import m07.joellpz.poliban.view.IbanMainFragment;
import m07.joellpz.poliban.viewHolders.RegisterBankAccountViewHolder;
import m07.joellpz.poliban.viewHolders.TransactionViewHolder;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionViewHolder> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
    private final List<Transaction> mExampleList;
    private Fragment parentFragment;
    TransactionsAdapter mainAdapter, explicitAdapter, explicitFutureAdapter;

    float totalBalanceMonth, totalComeMonth;
    List<Transaction> transactionsPerMonth = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("#.##");

    List<Transaction> transactionsToCards = new ArrayList<>();

    public TransactionsAdapter(List<Transaction> exampleList, Fragment parentFragment) {
        mExampleList = exampleList;
        this.parentFragment = parentFragment;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(ViewholderTransactionBinding.inflate(parentFragment.getLayoutInflater(), parent, false), parentFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        ((TransactionViewHolder) holder).bind(mExampleList.get(position));


    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }


}