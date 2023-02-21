package m07.joellpz.poliban;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mrtyvz.archedimageprogress.ArchedImageProgressBar;

import java.util.ArrayList;
import java.util.List;

import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.model.WalletCard;
import m07.joellpz.poliban.tools.ChargingImage;

public class WalletFragment extends Fragment {

    private ArchedImageProgressBar polibanArcProgress;
    private ScrollView mainView;

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.walletRecyclerView);

        mainView = view.findViewById(R.id.mainView);
        mainView.setVisibility(View.GONE);
        polibanArcProgress = view.findViewById(R.id.custom_imageProgressBar);
        new ChargingImage(polibanArcProgress, this);
        polibanArcProgress.setVisibility(View.VISIBLE);


        List<BankAccount> bankAccounts = new ArrayList<>();
        List<WalletCard> walletCards = new ArrayList<>();
        BankAccount bankAccount = new BankAccount();

        walletCards.add(new WalletCard());
        walletCards.add(new WalletCard());
        walletCards.add(new WalletCard());

        bankAccount.setWalletCardList(walletCards);
        bankAccounts.add(bankAccount);
        bankAccounts.add(bankAccount);
        bankAccounts.add(bankAccount);


        WalletAdapter walletAdapter = new WalletAdapter(bankAccounts);
        recyclerView.setAdapter(walletAdapter);

        mainView.setVisibility(View.VISIBLE);
        polibanArcProgress.setVisibility(View.GONE);
    }

    static class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletViewHolder> {

        private List<BankAccount> bankAccounts;

        public WalletAdapter(List<BankAccount> exampleList) {
            bankAccounts = exampleList;
        }

        @NonNull
        @Override
        public WalletAdapter.WalletViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_wallet, parent, false);
            return new WalletAdapter.WalletViewHolder(v);
        }

        @Override
        public int getItemCount() {
            return bankAccounts.size();
        }


        @Override
        public void onBindViewHolder(@NonNull WalletAdapter.WalletViewHolder holder, int position) {
            BankAccount currentItem = bankAccounts.get(position);
            CardAdapter cardAdapter = new CardAdapter(currentItem.getWalletCardList());
            holder.cardRecyclerView.setAdapter(cardAdapter);

        }

        class WalletViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            private RecyclerView cardRecyclerView;

            public WalletViewHolder(View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.whoseAccountText);
                cardRecyclerView = itemView.findViewById(R.id.cardRecyclerView);
            }
        }

    }

    //TO DO https://www.section.io/engineering-education/android-nested-recycler-view/
    static class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {


        private List<WalletCard> walletCardList;

        class CardViewHolder extends RecyclerView.ViewHolder {

            public TextView balanceNumCard;
            public TextView cardNumText;
            public TextView whoseTextCard;
            public TextView cveInfoCard;
            public TextView expDateInfoCard;
            public ImageView entityCardLogo;

            public ImageView infoBtnCard;
            public ImageView deleteBtnCard;
            public ImageView payBtnCard;
            public ImageView blockBtnCard;
            public ImageView contactLessBtnWallet;

            public RelativeLayout blockedWallet;
            public ConstraintLayout walletItem;
            public LinearLayout contactLessPay;

            public ImageView tickBlockBtnWallet;

            public CardViewHolder(View itemView) {
                super(itemView);

                balanceNumCard = itemView.findViewById(R.id.balanceNumCard);
                cardNumText = itemView.findViewById(R.id.cardNumText);
                whoseTextCard = itemView.findViewById(R.id.whoseTextCard);
                cveInfoCard = itemView.findViewById(R.id.cveInfoCard);
                expDateInfoCard = itemView.findViewById(R.id.expDateInfoCard);

                entityCardLogo = itemView.findViewById(R.id.entityCardLogo);

                infoBtnCard = itemView.findViewById(R.id.infoBtnCard);
                deleteBtnCard = itemView.findViewById(R.id.deleteBtnCard);
                payBtnCard = itemView.findViewById(R.id.payBtnCard);
                blockBtnCard = itemView.findViewById(R.id.blockBtnCard);

                blockedWallet = itemView.findViewById(R.id.blockedWallet);
                walletItem = itemView.findViewById(R.id.walletItem);
                contactLessPay = itemView.findViewById(R.id.contactLessPay);

                tickBlockBtnWallet = itemView.findViewById(R.id.tickBlockBtnWallet);
                contactLessBtnWallet = itemView.findViewById(R.id.contactLessBtnWallet);

            }
        }

        public CardAdapter(List<WalletCard> exampleList) {
            walletCardList = exampleList;
        }

        @NonNull
        @Override
        public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_wallet_card, parent, false);
            return new CardViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CardAdapter.CardViewHolder holder, int position) {
            WalletCard currentItem = walletCardList.get(position);

            holder.deleteBtnCard.setOnClickListener(v -> removeAt(holder.getAbsoluteAdapterPosition()));


            //Show Card Info
            holder.infoBtnCard.setOnClickListener(l -> {
                if (currentItem.isActive()) {
                    if (holder.cardNumText.getText().subSequence(0, 1).equals("*")) {
                        holder.cardNumText.setText("1234  1234  1234  2230");
                        holder.cveInfoCard.setText("123");
                    } else {
                        holder.cardNumText.setText("* * * *  * * * *  * * * *  2230");
                        holder.cveInfoCard.setText("***");
                    }
                }
            });

            //Pay ContactLess
            holder.payBtnCard.setOnClickListener(l -> {
                if (currentItem.isActive()) {
                    holder.contactLessPay.setVisibility(View.VISIBLE);
                }
            });
            holder.contactLessBtnWallet.setOnClickListener(l -> holder.contactLessPay.setVisibility(View.GONE));

            //Block Card
            holder.blockBtnCard.setOnClickListener(l -> {
                holder.blockedWallet.setVisibility(View.VISIBLE);
                holder.blockBtnCard.setImageAlpha(0);
                currentItem.setActive(false);
            });


            //Reactivate Card
            holder.tickBlockBtnWallet.setOnClickListener(l -> {
                holder.blockedWallet.setVisibility(View.GONE);
                holder.blockBtnCard.setImageAlpha(255);
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