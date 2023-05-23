package m07.joellpz.poliban.model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class BankAccount {
    private String userId;
    private String iban;
    private String owner;
    private String cif;
    private float balance;


    public BankAccount() {
    }


    /**
     * Constructor to save and register an account to an user.
     *
     * @param userId Id of the owner
     * @param iban   IBAN of the account
     * @param owner  Name of the owner
     */
    public BankAccount(String userId, String iban, String owner) {
        this.userId = userId;
        this.iban = iban;
        this.owner = owner;
        List<String> subCollections = new ArrayList<>(Arrays.asList("transaction", "walletCard"));
        subCollections.forEach(this::createSubcollection);
    }

    public BankAccount(String userId, String iban, String owner, String cif, float balance) {
        this.userId = userId;
        this.iban = iban;
        this.owner = owner;
        this.cif = cif;
        this.balance = balance;
    }


    private void createSubcollection(String name) {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(this.getClass().getSimpleName().toLowerCase().charAt(0) + this.getClass().getSimpleName().substring(1))
                .document(getIban()).collection(name);

        if (name.equals("walletCard")) {
            //WalletCard walletCard = new WalletCard(this,(float) (Math.random() * 158), "4241 3373 0328 3409", "Joel Lopez", 739, "05/09", true);

            collectionReference.add(new WalletCard(this));
        } else {
            for (int i = 0; i < 25; i++) {
                Date randomDate = new Date(ThreadLocalRandom.current().nextLong(1681077600000L, 1686348000000L));
                Transaction transaction = new Transaction("Titus", false, (float) (Math.random() * 158) - 79, "La Fiesta", randomDate, this);
                collectionReference.add(transaction).addOnSuccessListener(docRef -> docRef.update("transactionId", docRef.getId()));
            }
        }
    }


    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public float getBalance() {
        return balance;
    }

    @Exclude
    public String getBalanceString() {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(balance);
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static void saveBankAccountToUser(BankAccount account, final Consumer<Boolean> callback) {
        FirebaseFirestore.getInstance().collection("users")
                .document(account.getUserId()).get().addOnSuccessListener(docSnap -> {
                    if (Objects.requireNonNull((List<String>) docSnap.get("bankAccounts")).contains(account.getIban())) {
                        callback.accept(false);
                    } else {
                        callback.accept(true);
                        System.out.println("Registered IBAN: " + account.getIban() + " --- Registered IBAN ----");
                        FirebaseFirestore.getInstance().collection("bankAccount").document(account.getIban()).set(account);
                        docSnap.getReference().update("bankAccounts", FieldValue.arrayUnion(account.getIban()));
                    }
                });
    }

    public void deleteAccount(){
        FirebaseFirestore.getInstance().collection("bankAccount").document(getIban()).delete();
        FirebaseFirestore.getInstance().collection("users").document(userId).update("bankAccounts",FieldValue.arrayRemove(getIban()));
        System.out.println("Cuenta: "+getIban()+" Eliminada!");
    }


    @NonNull
    @Override
    public String toString() {
        return iban;
    }

    @NonNull
    public String toStringComplete() {
        return "BankAccount{" +
                ", userId='" + getUserId() + '\'' +
                ", iban='" + getIban() + '\'' +
                ", owner='" + getOwner() + '\'' +
                ", cif='" + getCif() + '\'' +
                ", balance=" + getBalanceString() +
                '}';
    }
}
