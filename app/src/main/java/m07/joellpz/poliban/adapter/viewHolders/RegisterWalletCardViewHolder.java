package m07.joellpz.poliban.adapter.viewHolders;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.databinding.ViewholderWalletCardRegisterBinding;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.model.WalletCard;

public class RegisterWalletCardViewHolder extends RecyclerView.ViewHolder {
    private final ViewholderWalletCardRegisterBinding binding;
    private final Fragment parentFragment;
    private final BankAccount bankAccount;
    public RegisterWalletCardViewHolder(ViewholderWalletCardRegisterBinding binding, Fragment parentFragment, BankAccount bankAccount) {
        super(binding.getRoot());
        this.binding = binding;
        this.parentFragment = parentFragment;
        this.bankAccount = bankAccount;
    }

    public void bind() {
        binding.addWalletCard.setOnClickListener(l -> {
            WalletCard.generateNewCardToAccount(bankAccount);
            ((RecyclerView) parentFragment.requireView().findViewById(R.id.cardRecyclerView)).scrollToPosition(0);
        });
    }
}
