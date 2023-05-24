package m07.joellpz.poliban.adapter.viewHolders;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.databinding.ViewholderWalletCardBinding;
import m07.joellpz.poliban.model.WalletCard;

/**
 * ViewHolder class for WalletCard items in a RecyclerView.
 */
public class WalletCardViewHolder extends RecyclerView.ViewHolder {
    /**
     * View binding for the fragment.
     */
    private final ViewholderWalletCardBinding binding;
    /**
     * Parent Fragment
     */
    private final Fragment parentFragment;

    /**
     * Constructs a new WalletCardViewHolder.
     *
     * @param binding          The ViewholderWalletCardBinding object associated with this ViewHolder.
     * @param parentFragment   The parent Fragment that holds the RecyclerView.
     */
    public WalletCardViewHolder(ViewholderWalletCardBinding binding, Fragment parentFragment) {
        super(binding.getRoot());
        this.binding = binding;
        this.parentFragment = parentFragment;
    }

    /**
     * Binds a WalletCard object to the ViewHolder.
     *
     * @param walletCard  The WalletCard object to bind.
     */
    @SuppressLint("SetTextI18n")
    public void bind(WalletCard walletCard) {
        // Set card details
        binding.whoseTextCard.setText(walletCard.getCardOwner());
        binding.expDateInfoCard.setText(walletCard.getExpDate());
        binding.cardNumText.setText("* * * *  * * * *  * * * *  " + walletCard.getCardNum().split(" ")[walletCard.getCardNum().split(" ").length - 1]);
        binding.cveInfoCard.setText("***");
        binding.currentBalance.setText(walletCard.getBalanceString() + " €");

        // Set card logo and background color based on card number
        if (walletCard.getCardNum().startsWith("4")) {
            binding.entityCardLogo.setImageResource(R.drawable.logo_visa);
            binding.walletItem.setBackgroundTintList(ColorStateList.valueOf(parentFragment.requireActivity().getResources().getColor(R.color.visa, parentFragment.requireActivity().getTheme())));
        } else {
            binding.entityCardLogo.setImageResource(R.drawable.logo_mastercard);
            binding.walletItem.setBackgroundTintList(ColorStateList.valueOf(parentFragment.requireActivity().getResources().getColor(R.color.mastercard, parentFragment.requireActivity().getTheme())));
        }

        // Delete Card button
        binding.deleteBtnCard.setOnClickListener(v -> walletCard.deleteWalletCard());

        // Show Card Info button
        binding.infoBtnCard.setOnClickListener(l -> {
            if (walletCard.isActive()) {
                if (binding.cardNumText.getText().subSequence(0, 1).equals("*")) {
                    binding.cardNumText.setText(walletCard.getCardNum());
                    binding.cveInfoCard.setText(walletCard.getCvv() + "");
                } else {
                    binding.cardNumText.setText("* * * *  * * * *  * * * *  " + walletCard.getCardNum().split(" ")[walletCard.getCardNum().split(" ").length - 1]);
                    binding.cveInfoCard.setText("***");
                }
            }
        });

        // Pay ContactLess button
        binding.payBtnCard.setOnClickListener(l -> {
            if (walletCard.isActive()) {
                binding.contactLessPay.setVisibility(View.VISIBLE);
            }
        });
        binding.contactLessBtnWallet.setOnClickListener(l -> binding.contactLessPay.setVisibility(View.GONE));

        // Block Card button
        binding.blockBtnCard.setOnClickListener(l -> {
            binding.blockedWallet.setVisibility(View.VISIBLE);
            binding.blockBtnCard.setImageAlpha(0);
            walletCard.changeActiveStatus(false);
        });

        // Reactivate Card button
        binding.tickBlockBtnWallet.setOnClickListener(l -> {
            binding.blockedWallet.setVisibility(View.GONE);
            binding.blockBtnCard.setImageAlpha(255);
            walletCard.changeActiveStatus(true);
        });
    }
}
