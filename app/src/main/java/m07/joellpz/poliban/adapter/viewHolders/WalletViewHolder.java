package m07.joellpz.poliban.adapter.viewHolders;

import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.adapter.WalletCardAdapter;
import m07.joellpz.poliban.databinding.ViewholderWalletBinding;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.model.WalletCard;

public class WalletViewHolder extends RecyclerView.ViewHolder {
    private final ViewholderWalletBinding binding;
    private final Fragment parentFragment;

    public WalletViewHolder(ViewholderWalletBinding binding, Fragment parentFragment) {
        super(binding.getRoot());
        this.binding = binding;
        this.parentFragment = parentFragment;
    }

    @SuppressLint("SetTextI18n")
    public void bind(BankAccount bankAccount) {
        binding.whoseAccountText.setText(bankAccount.getOwner());
        binding.numAccountText.setText(". . . .  " + bankAccount.getIban().split(" ")[bankAccount.getIban().split(" ").length - 1]);

        if (bankAccount.getIban().split(" ")[1].equals("2100")) {
            binding.bankLogoWallet.setImageResource(R.drawable.logo_lacaixa);
        } else if (bankAccount.getIban().split(" ")[1].equals("0057")) {
            binding.bankLogoWallet.setImageResource(R.drawable.logo_bbva);
        } else if (bankAccount.getIban().split(" ")[1].equals("0049")) {
            binding.bankLogoWallet.setImageResource(R.drawable.logo_santander);
        }

        Query qWalletCard = FirebaseFirestore.getInstance().collection("bankAccount").document(bankAccount.getIban()).collection("walletCard");
        FirestoreRecyclerOptions<WalletCard> options = new FirestoreRecyclerOptions.Builder<WalletCard>().setQuery(qWalletCard, WalletCard.class).setLifecycleOwner(parentFragment.getParentFragment()).build();
        binding.cardRecyclerView.setAdapter(new WalletCardAdapter(options, parentFragment,bankAccount));
    }
}
