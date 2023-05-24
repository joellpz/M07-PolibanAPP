package m07.joellpz.poliban.adapter.viewHolders;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.databinding.ViewholderWalletCardRegisterBinding;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.model.WalletCard;

/**
 * ViewHolder class for the "Register Wallet Card" item in a RecyclerView.
 */
public class RegisterWalletCardViewHolder extends RecyclerView.ViewHolder {
    /**
     * View binding for the fragment.
     */
    private final ViewholderWalletCardRegisterBinding binding;
    /**
     * Parent Fragment
     */
    private final Fragment parentFragment;
    /**
     * Bank Account to define the Objects of the ViewHolder
     */
    private final BankAccount bankAccount;

    /**
     * Constructs a new RegisterWalletCardViewHolder.
     *
     * @param binding          The ViewholderWalletCardRegisterBinding object associated with this ViewHolder.
     * @param parentFragment   The parent Fragment that holds the RecyclerView.
     * @param bankAccount      The BankAccount object associated with this ViewHolder.
     */
    public RegisterWalletCardViewHolder(ViewholderWalletCardRegisterBinding binding, Fragment parentFragment, BankAccount bankAccount) {
        super(binding.getRoot());
        this.binding = binding;
        this.parentFragment = parentFragment;
        this.bankAccount = bankAccount;
    }

    /**
     * Binds the ViewHolder to the data.
     */
    public void bind() {
        // Set click listener to add a new Wallet Card to the Bank Account
        binding.addWalletCard.setOnClickListener(l -> {
            WalletCard.generateNewCardToAccount(bankAccount);
            ((RecyclerView) parentFragment.requireView().findViewById(R.id.cardRecyclerView)).scrollToPosition(0);
        });
    }
}
