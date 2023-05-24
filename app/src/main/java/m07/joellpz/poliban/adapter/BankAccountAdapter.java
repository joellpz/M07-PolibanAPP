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

/**
 * The BankAccountAdapter class is a FirestoreRecyclerAdapter responsible for displaying bank account data in a RecyclerView.
 * It supports different view types based on whether it is for a wallet or not, and provides functionality for adding a new bank account.
 */
public class BankAccountAdapter extends FirestoreRecyclerAdapter<BankAccount, RecyclerView.ViewHolder> {
    /**
     * ViewType to define the add Account View Holder
     */
    private static final int ITEM_TYPE_ADD_ACCOUNT = 1;
    /**
     * Parent Fragment
     */
    private final Fragment parentFragment;
    /**
     * The User from the info is retrieved.
     */
    private final FirebaseUser user;
    /**
     * Boolean to know if the query is for the Wallet Fragment
     */
    private final boolean isForWallet;

    /**
     * Constructs a new BankAccountAdapter with the provided options and parameters.
     *
     * @param options         The FirestoreRecyclerOptions for the bank account data.
     * @param parentFragment  The parent fragment.
     * @param user            The FirebaseUser object representing the currently logged-in user.
     * @param isForWallet     A boolean indicating whether the adapter is for a wallet or not.
     */
    public BankAccountAdapter(@NonNull FirestoreRecyclerOptions<BankAccount> options, Fragment parentFragment, FirebaseUser user, boolean isForWallet) {
        super(options);
        this.parentFragment = parentFragment;
        this.user = user;
        this.isForWallet = isForWallet;
    }

    /**
     * Called when a new ViewHolder object should be created and initialized.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The type of the view.
     * @return The created ViewHolder object.
     */
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

    /**
     * Called to bind the data to the ViewHolder at the specified position.
     *
     * @param holder   The ViewHolder to bind the data to.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_ADD_ACCOUNT) {
            ((RegisterBankAccountViewHolder) holder).bind();
            if (getItemCount() == 1) {
                parentFragment.requireView().findViewById(R.id.custom_imageProgressBar).setVisibility(View.GONE);
            }
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    /**
     * Called to bind the data to the ViewHolder at the specified position.
     *
     * @param holder      The ViewHolder to bind the data to.
     * @param position    The position of the item within the adapter's data set.
     * @param bankAccount The bank account data to bind.
     */
    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull BankAccount bankAccount) {
        if (isForWallet) {
            ((WalletViewHolder) holder).bind(bankAccount);
        } else {
            ((BankAccountViewHolder) holder).bind(bankAccount);
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items.
     */
    @Override
    public int getItemCount() {
        if (isForWallet) {
            return super.getItemCount();
        } else {
            return super.getItemCount() + 1;
        }
    }

    /**
     * Returns the view type of the item at the specified position.
     *
     * @param position The position of the item in the adapter's data set.
     * @return The view type of the item at the specified position.
     */
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && !isForWallet) {
            return ITEM_TYPE_ADD_ACCOUNT;
        } else {
            return super.getItemViewType(position);
        }
    }

    /**
     * Called when the data set has been changed.
     * Scrolls the RecyclerView to the top position.
     */
    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if (!isForWallet) {
            RecyclerView recyclerView = parentFragment.requireView().findViewById(R.id.recyclerViewHome);
            recyclerView.scrollToPosition(0);
        }
    }
}
