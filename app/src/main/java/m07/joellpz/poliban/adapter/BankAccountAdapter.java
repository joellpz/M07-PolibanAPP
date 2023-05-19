package m07.joellpz.poliban.adapter;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;

import m07.joellpz.poliban.databinding.ViewholderBankAccountBinding;
import m07.joellpz.poliban.databinding.ViewholderRegisterBankAccountBinding;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.viewHolders.BankAccountViewHolder;
import m07.joellpz.poliban.viewHolders.RegisterBankAccountViewHolder;

public class BankAccountAdapter extends FirestoreRecyclerAdapter<BankAccount, RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_ADD_ACCOUNT = 1;
    private final Fragment parentFragment;
    private final FirebaseUser user;

    public BankAccountAdapter(@NonNull FirestoreRecyclerOptions<BankAccount> options, Fragment parentFragment, FirebaseUser user) {
        super(options);
        this.parentFragment = parentFragment;
        this.user = user;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_ADD_ACCOUNT)
            return new RegisterBankAccountViewHolder(ViewholderRegisterBankAccountBinding.inflate(parentFragment.getLayoutInflater(), parent, false), parentFragment, user);
        else
            return new BankAccountViewHolder(ViewholderBankAccountBinding.inflate(parentFragment.getLayoutInflater(), parent, false), parentFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @NonNull
    @Override
    public BankAccount getItem(int position) {
        if (position == getItemCount() - 1) return super.getItem(position - 1);
        else return super.getItem(position);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull BankAccount bankAccount) {
        System.out.println(getItemCount() + "+++++++++++++++++++++");
        if (getItemViewType(position) == ITEM_TYPE_ADD_ACCOUNT) {
            System.out.println(position + "Registrer");
            ((RegisterBankAccountViewHolder) holder).bind();
        } else {
            System.out.println(position + "BankAccounts");
            ((BankAccountViewHolder) holder).bind(bankAccount);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) return ITEM_TYPE_ADD_ACCOUNT;
        else return super.getItemViewType(position);
    }
}