package m07.joellpz.poliban.adapter;


import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.adapter.viewHolders.BankAccountViewHolder;
import m07.joellpz.poliban.adapter.viewHolders.RegisterBankAccountViewHolder;
import m07.joellpz.poliban.adapter.viewHolders.WalletViewHolder;
import m07.joellpz.poliban.databinding.ViewholderBankAccountBinding;
import m07.joellpz.poliban.databinding.ViewholderBankAccountRegisterBinding;
import m07.joellpz.poliban.databinding.ViewholderWalletBinding;
import m07.joellpz.poliban.model.BankAccount;

public class BankAccountAdapter extends FirestoreRecyclerAdapter<BankAccount, RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_ADD_ACCOUNT = 1;
    private static final int ITEM_TYPE_WALLET = 2;
    private final Fragment parentFragment;
    private final FirebaseUser user;
    private final boolean isForWallet;

    public BankAccountAdapter(@NonNull FirestoreRecyclerOptions<BankAccount> options, Fragment parentFragment, FirebaseUser user, boolean isForWallet) {
        super(options);
        this.parentFragment = parentFragment;
        this.user = user;
        this.isForWallet = isForWallet;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_ADD_ACCOUNT)
            return new RegisterBankAccountViewHolder(ViewholderBankAccountRegisterBinding.inflate(parentFragment.getLayoutInflater(), parent, false), user);
        else if (isForWallet) {
            return new WalletViewHolder(ViewholderWalletBinding.inflate(parentFragment.getLayoutInflater(), parent, false), parentFragment);
        } else
            return new BankAccountViewHolder(ViewholderBankAccountBinding.inflate(parentFragment.getLayoutInflater(), parent, false), parentFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_ADD_ACCOUNT) {
            ((RegisterBankAccountViewHolder) holder).bind();
            if (getItemCount() == 1) {
                parentFragment.requireView().findViewById(R.id.custom_imageProgressBar).setVisibility(View.GONE);
            }
        } else super.onBindViewHolder(holder, position);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull BankAccount bankAccount) {
        if (isForWallet) ((WalletViewHolder) holder).bind(bankAccount);
        else ((BankAccountViewHolder) holder).bind(bankAccount);

    }

    @Override
    public int getItemCount() {
        if (isForWallet) return super.getItemCount();
        else return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && !isForWallet) return ITEM_TYPE_ADD_ACCOUNT;
        else return super.getItemViewType(position);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if (!isForWallet) {
            RecyclerView recyclerView = parentFragment.requireView().findViewById(R.id.recyclerViewHome);
            recyclerView.scrollToPosition(0);
        }
    }
}