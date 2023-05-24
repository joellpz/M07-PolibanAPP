package m07.joellpz.poliban.adapter.viewHolders;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.adapter.WalletCardAdapter;
import m07.joellpz.poliban.databinding.ViewholderWalletBinding;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.model.WalletCard;

/**
 * ViewHolder class for Wallet items in a RecyclerView.
 */
public class WalletViewHolder extends RecyclerView.ViewHolder {
    /**
     * View binding for the fragment.
     */
    private final ViewholderWalletBinding binding;
    /**
     * Parent Fragment
     */
    private final Fragment parentFragment;

    /**
     * Constructs a new WalletViewHolder.
     *
     * @param binding        The ViewholderWalletBinding object associated with this ViewHolder.
     * @param parentFragment The parent Fragment that holds the RecyclerView.
     */
    public WalletViewHolder(ViewholderWalletBinding binding, Fragment parentFragment) {
        super(binding.getRoot());
        this.binding = binding;
        this.parentFragment = parentFragment;
    }

    /**
     * Binds a BankAccount object to the ViewHolder.
     *
     * @param bankAccount The BankAccount object to bind.
     */
    @SuppressLint("SetTextI18n")
    public void bind(BankAccount bankAccount) {
        binding.whoseAccountText.setText(bankAccount.getOwner());
        binding.numAccountText.setText(". . . .  " + bankAccount.getIban().split(" ")[bankAccount.getIban().split(" ").length - 1]);

        // Set the bank logo based on the IBAN number
        if (bankAccount.getIban().split(" ")[1].equals("2100")) {
            binding.bankLogoWallet.setImageResource(R.drawable.logo_lacaixa);
        } else if (bankAccount.getIban().split(" ")[1].equals("0057")) {
            binding.bankLogoWallet.setImageResource(R.drawable.logo_bbva);
        } else if (bankAccount.getIban().split(" ")[1].equals("0049")) {
            binding.bankLogoWallet.setImageResource(R.drawable.logo_santander);
        }

        // Set up the RecyclerView for WalletCard items
        binding.cardRecyclerView.setLayoutManager(new LinearLayoutManager(parentFragment.requireContext(), RecyclerView.HORIZONTAL, false));
        binding.cardRecyclerView.setHasFixedSize(true);

        // Attach a LinearSnapHelper to enable snapping behavior in the RecyclerView
        LinearSnapHelper snapHelper = new LinearSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager lm, int velocityX, int velocityY) {
                View centerView = findSnapView(lm);
                if (centerView == null)
                    return RecyclerView.NO_POSITION;

                int position = lm.getPosition(centerView);
                int targetPosition = -1;
                if (lm.canScrollHorizontally()) {
                    if (velocityX < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                if (lm.canScrollVertically()) {
                    if (velocityY < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                final int firstItem = 0;
                final int lastItem = lm.getItemCount() - 1;
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
                return targetPosition;
            }
        };
        snapHelper.attachToRecyclerView(binding.cardRecyclerView);
        binding.cardRecyclerView.setNestedScrollingEnabled(false);

        // Set up FirestoreRecyclerOptions for querying WalletCard items from Firestore
        Query qWalletCard = FirebaseFirestore.getInstance().collection("bankAccount").document(bankAccount.getIban()).collection("walletCard");
        FirestoreRecyclerOptions<WalletCard> options = new FirestoreRecyclerOptions.Builder<WalletCard>()
                .setQuery(qWalletCard, WalletCard.class)
                .setLifecycleOwner(parentFragment.getParentFragment())
                .build();

        // Set the WalletCardAdapter for the RecyclerView
        binding.cardRecyclerView.setAdapter(new WalletCardAdapter(options, parentFragment, bankAccount));
    }
}
