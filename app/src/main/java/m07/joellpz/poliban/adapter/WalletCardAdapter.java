package m07.joellpz.poliban.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.adapter.viewHolders.RegisterWalletCardViewHolder;
import m07.joellpz.poliban.adapter.viewHolders.WalletCardViewHolder;
import m07.joellpz.poliban.databinding.ViewholderWalletCardBinding;
import m07.joellpz.poliban.databinding.ViewholderWalletCardRegisterBinding;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.model.WalletCard;

/**
 * Adapter for displaying a list of wallet cards in a RecyclerView.
 */
public class WalletCardAdapter extends FirestoreRecyclerAdapter<WalletCard, RecyclerView.ViewHolder> {
    /**
     * ITEM ID for WalletCard.
     */
    private static final int ITEM_TYPE_ADD_WALLETCARD = 1;
    /**
     * Parent Fragment.
     */
    private final Fragment parentFragment;
    /**
     * Base Bank Account to retrieve the info.
     */
    private final BankAccount bankAccount;

    /**
     * Constructs a new WalletCardAdapter with the given options, parent fragment, and bank account.
     *
     * @param options         The FirestoreRecyclerOptions for wallet cards
     * @param parentFragment  The parent fragment
     * @param bankAccount     The bank account associated with the wallet cards
     */
    public WalletCardAdapter(@NonNull FirestoreRecyclerOptions<WalletCard> options, Fragment parentFragment, BankAccount bankAccount) {
        super(options);
        this.parentFragment = parentFragment;
        this.bankAccount = bankAccount;
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
        if (viewType == ITEM_TYPE_ADD_WALLETCARD)
            return new RegisterWalletCardViewHolder(ViewholderWalletCardRegisterBinding.inflate(parentFragment.getLayoutInflater(), parent, false), parentFragment, bankAccount);
        else
            return new WalletCardViewHolder(ViewholderWalletCardBinding.inflate(parentFragment.getLayoutInflater(), parent, false), parentFragment);
    }

    /**
     * Called to bind the data to the given ViewHolder at the specified position.
     *
     * @param holder   The ViewHolder to bind
     * @param position The position of the item
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_ADD_WALLETCARD) {
            ((RegisterWalletCardViewHolder) holder).bind();
            if (getItemCount() == 1) {
                parentFragment.requireView().findViewById(R.id.custom_imageProgressBar).setVisibility(View.GONE);
            }
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    /**
     * Called to bind the data to the given ViewHolder at the specified position.
     *
     * @param holder     The ViewHolder to bind
     * @param position   The position of the item
     * @param walletCard The wallet card item
     */
    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull WalletCard walletCard) {
        ((WalletCardViewHolder) holder).bind(walletCard);
    }

    /**
     * Returns the total number of items in the data set, including the additional item for adding a new wallet card.
     *
     * @return The total number of items in the data set
     */
    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    /**
     * Returns the view type of the item at the specified position.
     *
     * @param position The position of the item
     * @return The view type of the item
     */
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return ITEM_TYPE_ADD_WALLETCARD;
        } else {
            return super.getItemViewType(position);
        }
    }

    /**
     * Called when the data set has changed.
     */
    @Override
    public void onDataChanged() {
        super.onDataChanged();
        ((RecyclerView) parentFragment.requireView().findViewById(R.id.cardRecyclerView)).scrollToPosition(0);
    }
}
