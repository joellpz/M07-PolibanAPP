package m07.joellpz.poliban.model;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.model.Transaction;
import m07.joellpz.poliban.model.WalletCard;

public class AppViewModel extends AndroidViewModel {
    public List<BankAccount> bankAccountList;

    public static class Media {
        public Uri uri;
        public String tipo;

        public Media(Uri uri, String tipo) {
            this.uri = uri;
            this.tipo = tipo;
        }
    }

    public void createBankAccounts() {
        List<BankAccount> bankAccounts = new ArrayList<>();
        List<WalletCard> walletCards = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();
        List<Transaction> futureTransactions = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            Date randomDate = new Date(ThreadLocalRandom.current()
                    .nextLong(1669852148000L, 1677538800000L));
            Transaction transaction = new Transaction("Titus", false, (float) (Math.random() * 158) - 79, "La Fiesta", randomDate);
            transactions.add(transaction);
        }


        WalletCard walletCard = new WalletCard((float) (Math.random() * 158), "4241 3373 0328 3409", "Joel Lopez", 739, new Date(), true);
        WalletCard walletCard1 = new WalletCard((float) (Math.random() * 158), "5241 3373 0328 3409", "Joel Lopez", 739, new Date(), true);
        walletCards.add(walletCard);
        walletCards.add(walletCard1);


        for (int i = 1; i < 3; i++) {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(new Date());
            calendar.set(Calendar.DAY_OF_MONTH, 26 + i);
            futureTransactions.add(new Transaction("El PUIG", true, (float) (Math.random() * 158) - 79, "Nomina" + i, calendar.getTime()));
        }

        bankAccounts.add(new BankAccount("3kyRj9Nm5pTBZTvam3j91MxcklX2","ES54 0049 5178 7932 1818 3952", "Joel Lopez", null, (float) (Math.random() * 4380), transactions, futureTransactions, walletCards));
        bankAccounts.add(new BankAccount("3kyRj9Nm5pTBZTvam3j91MxcklX2","ES54 0057 5178 7932 1818 3952", "Joel Lopez", null, (float) (Math.random() * 4380), transactions, futureTransactions, walletCards));
        bankAccounts.add(new BankAccount("3kyRj9Nm5pTBZTvam3j91MxcklX2","ES54 2100 5178 7932 1818 3952", "Joel Lopez", null, (float) (Math.random() * 4380), transactions, futureTransactions, walletCards));

        setBankAccountList(bankAccounts);
    }

    public void getBankAccountsFirebase(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//        FirebaseFirestore.getInstance().collection("accounts").get().addOnSuccessListener()
//                .add(post)
//                .addOnSuccessListener(documentReference -> {
//                    documentReference.update("docid", documentReference.getId());
//                    navController.popBackStack();
//                    appViewModel.setMediaSeleccionado(null, null);
//                });
    }


    private void setBankAccountList(List<BankAccount> bankAccountList) {
        this.bankAccountList = bankAccountList;
    }

    public List<BankAccount> getBankAccountList() {
        return bankAccountList;
    }

    public BankAccount getBankAccountElement(int position) {
        return bankAccountList.get(position);
    }

    //public MutableLiveData<Post> postSeleccionado = new MutableLiveData<>();
    public MutableLiveData<Media> mediaSeleccionado = new MutableLiveData<>();

    public AppViewModel(@NonNull Application application) {
        super(application);
    }

    public void setMediaSeleccionado(Uri uri, String type) {
        mediaSeleccionado.setValue(new Media(uri, type));
    }
}