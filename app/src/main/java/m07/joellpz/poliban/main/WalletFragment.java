package m07.joellpz.poliban.main;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.databinding.FragmentWalletBinding;
import m07.joellpz.poliban.databinding.ViewholderWalletBinding;
import m07.joellpz.poliban.databinding.ViewholderWalletCardBinding;
import m07.joellpz.poliban.model.AppViewModel;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.model.Transaction;
import m07.joellpz.poliban.model.WalletCard;
import m07.joellpz.poliban.tools.ChargingImage;

public class WalletFragment extends Fragment {

    private FragmentWalletBinding binding;
    DecimalFormat df = new DecimalFormat("#.##");

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentWalletBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        df.setRoundingMode(RoundingMode.CEILING);

        binding.mainView.setVisibility(View.GONE);

        new ChargingImage(binding.customImageProgressBar, this);
        binding.customImageProgressBar.setVisibility(View.VISIBLE);

        AppViewModel appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);


        WalletAdapter walletAdapter = new WalletAdapter(appViewModel.getBankAccountList());
        binding.walletRecyclerView.setAdapter(walletAdapter);

        binding.mainView.setVisibility(View.VISIBLE);
        binding.customImageProgressBar.setVisibility(View.GONE);
    }

    class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletViewHolder> {

        private final List<BankAccount> bankAccounts;

        public WalletAdapter(List<BankAccount> exampleList) {
            bankAccounts = exampleList;
        }

        @NonNull
        @Override
        public WalletAdapter.WalletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new WalletAdapter.WalletViewHolder(ViewholderWalletBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public int getItemCount() {
            return bankAccounts.size();
        }


        @Override
        public void onBindViewHolder(@NonNull WalletAdapter.WalletViewHolder holder, int position) {
            BankAccount currentItem = bankAccounts.get(position);
            CardAdapter cardAdapter = new CardAdapter(currentItem.getWalletCardList());

            holder.bindingWallet.whoseAccountText.setText(currentItem.getOwner());
            holder.bindingWallet.numAccountText.setText(". . . .  " + currentItem.getIban().split(" ")[currentItem.getIban().split(" ").length - 1]);

            if (currentItem.getIban().split(" ")[1].equals("2100")) {
                holder.bindingWallet.bankLogoWallet.setImageResource(R.drawable.logo_lacaixa);
            } else if (currentItem.getIban().split(" ")[1].equals("0057")) {
                holder.bindingWallet.bankLogoWallet.setImageResource(R.drawable.logo_bbva);
            } else if (currentItem.getIban().split(" ")[1].equals("0049")) {
                holder.bindingWallet.bankLogoWallet.setImageResource(R.drawable.logo_santander);
            }

            holder.bindingWallet.cardRecyclerView.setAdapter(cardAdapter);

        }

        class WalletViewHolder extends RecyclerView.ViewHolder {
            private final ViewholderWalletBinding bindingWallet;

            public WalletViewHolder(ViewholderWalletBinding binding) {
                super(binding.getRoot());
                this.bindingWallet = binding;
            }
        }

    }

    //TO DO https://www.section.io/engineering-education/android-nested-recycler-view/
    class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {


        private final List<WalletCard> walletCardList;

        class CardViewHolder extends RecyclerView.ViewHolder {

            private final ViewholderWalletCardBinding bindingWalletCard;

            public CardViewHolder(ViewholderWalletCardBinding binding) {
                super(binding.getRoot());
                this.bindingWalletCard = binding;
            }
        }

        public CardAdapter(List<WalletCard> exampleList) {
            walletCardList = exampleList;
        }

        @NonNull
        @Override
        public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CardViewHolder(ViewholderWalletCardBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CardAdapter.CardViewHolder holder, int position) {
            WalletCard currentItem = walletCardList.get(position);

            holder.bindingWalletCard.deleteBtnCard.setOnClickListener(v -> removeAt(holder.getAbsoluteAdapterPosition()));
            holder.bindingWalletCard.whoseTextCard.setText(currentItem.getCardOwner());
            holder.bindingWalletCard.expDateInfoCard.setText(currentItem.getExpDateToCard());
            holder.bindingWalletCard.cardNumText.setText("* * * *  * * * *  * * * *  " + currentItem.getCardNum().split(" ")[currentItem.getCardNum().split(" ").length - 1]);
            holder.bindingWalletCard.cveInfoCard.setText("***");
            holder.bindingWalletCard.currentBalance.setText(df.format(currentItem.getBalance()) + " €");

            if (currentItem.getCardNum().startsWith("4")) {
                holder.bindingWalletCard.entityCardLogo.setImageResource(R.drawable.logo_visa);
                holder.bindingWalletCard.walletItem.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.visa, requireActivity().getTheme())));
            } else {
                holder.bindingWalletCard.entityCardLogo.setImageResource(R.drawable.logo_mastercard);
                holder.bindingWalletCard.walletItem.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.mastercard, requireActivity().getTheme())));
            }

            //Show Card Info
            holder.bindingWalletCard.infoBtnCard.setOnClickListener(l -> {
                if (currentItem.isActive()) {
                    if (holder.bindingWalletCard.cardNumText.getText().subSequence(0, 1).equals("*")) {
                        holder.bindingWalletCard.cardNumText.setText(currentItem.getCardNum());
                        holder.bindingWalletCard.cveInfoCard.setText(currentItem.getCvv() + "");
                    } else {
                        holder.bindingWalletCard.cardNumText.setText("* * * *  * * * *  * * * *  " + currentItem.getCardNum().split(" ")[currentItem.getCardNum().split(" ").length - 1]);
                        holder.bindingWalletCard.cveInfoCard.setText("***");
                    }
                }
            });

            //Pay ContactLess
            holder.bindingWalletCard.payBtnCard.setOnClickListener(l -> {
                if (currentItem.isActive()) {
                    holder.bindingWalletCard.contactLessPay.setVisibility(View.VISIBLE);
                }
            });
            holder.bindingWalletCard.contactLessBtnWallet.setOnClickListener(l -> holder.bindingWalletCard.contactLessPay.setVisibility(View.GONE));

            //Block Card
            holder.bindingWalletCard.blockBtnCard.setOnClickListener(l -> {
                holder.bindingWalletCard.blockedWallet.setVisibility(View.VISIBLE);
                holder.bindingWalletCard.blockBtnCard.setImageAlpha(0);
                currentItem.setActive(false);
            });


            //Reactivate Card
            holder.bindingWalletCard.tickBlockBtnWallet.setOnClickListener(l -> {
                holder.bindingWalletCard.blockedWallet.setVisibility(View.GONE);
                holder.bindingWalletCard.blockBtnCard.setImageAlpha(255);
                currentItem.setActive(true);

            });
        }

        @Override
        public int getItemCount() {
            return walletCardList.size();
        }

        private void removeAt(int position) {
            if (walletCardList.size() - 1 != 0) {
                walletCardList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, walletCardList.size());
            }
        }
    }
}