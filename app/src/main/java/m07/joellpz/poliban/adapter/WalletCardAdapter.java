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

public class WalletCardAdapter extends FirestoreRecyclerAdapter<WalletCard, RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_ADD_WALLETCARD = 1;
    private final Fragment parentFragment;
    private final BankAccount bankAccount;

    public WalletCardAdapter(@NonNull FirestoreRecyclerOptions<WalletCard> options, Fragment parentFragment, BankAccount bankAccount) {
        super(options);
        this.parentFragment = parentFragment;
        this.bankAccount = bankAccount;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_ADD_WALLETCARD)
            return new RegisterWalletCardViewHolder(ViewholderWalletCardRegisterBinding.inflate(parentFragment.getLayoutInflater(), parent, false),parentFragment, bankAccount);
        else
            return new WalletCardViewHolder(ViewholderWalletCardBinding.inflate(parentFragment.getLayoutInflater(), parent, false), parentFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_ADD_WALLETCARD){
            ((RegisterWalletCardViewHolder) holder).bind();
            if (getItemCount()==1){
                parentFragment.requireView().findViewById(R.id.custom_imageProgressBar).setVisibility(View.GONE);
            }

        }
        else super.onBindViewHolder(holder, position);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull WalletCard walletCard) {
        ((WalletCardViewHolder) holder).bind(walletCard);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) return ITEM_TYPE_ADD_WALLETCARD;
        else return super.getItemViewType(position);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        ((RecyclerView) parentFragment.requireView().findViewById(R.id.cardRecyclerView)).scrollToPosition(0);
    }

}