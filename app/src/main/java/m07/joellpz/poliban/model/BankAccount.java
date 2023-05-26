package m07.joellpz.poliban.model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

/**
 * Represents a bank account.
 */
public class BankAccount implements Serializable {
    /**
     * The ID from the User Owner.
     */
    private String userId;
    /**
     * The IBAN from the account.
     */
    private String iban;
    /**
     * The name of the Owner.
     */
    private String owner;
    /**
     * The CIF from the owner that is a Business.
     */
    private String cif;
    /**
     * Balance of the account.
     */
    private float balance;


    /**
     * Default constructor required for Firestore deserialization.
     */
    public BankAccount() {
    }


    /**
     * Constructor to save and register an account to a user.
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

    /**
     * Constructor to create a bank account with additional details.
     *
     * @param userId  Id of the owner
     * @param iban    IBAN of the account
     * @param owner   Name of the owner
     * @param cif     CIF of the account
     * @param balance Initial balance of the account
     */
    public BankAccount(String userId, String iban, String owner, String cif, float balance) {
        this.userId = userId;
        this.iban = iban;
        this.owner = owner;
        this.cif = cif;
        this.balance = balance;
    }


    /**
     * Creates a subcollection for the bank account.
     *
     * @param name Name of the subcollection
     */
    private void createSubcollection(String name) {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(this.getClass().getSimpleName().toLowerCase().charAt(0) + this.getClass().getSimpleName().substring(1))
                .document(getIban()).collection(name);

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.MONTH, -2);

        if (name.equals("walletCard")) {
            collectionReference.add(new WalletCard(this)).addOnSuccessListener(docRef -> docRef.update("cardId", docRef.getId()));
        } else {
            for (int i = 0; i < 20; i++) {
                Date randomDate = new Date(ThreadLocalRandom.current().nextLong(calendar.getTimeInMillis(), today.getTime()));
                Transaction transaction = new Transaction("Cosas", false, (float) (Math.random() * 158) - 79, "La Fiesta", randomDate, this);
                System.out.println(transaction);
                collectionReference.add(transaction).addOnSuccessListener(docRef -> docRef.update("transactionId", docRef.getId()));
            }
            calendar.add(Calendar.MONTH, 3);
            Transaction t1 = new Transaction("El Puig SL", true, (float) (Math.random() * 158), "Nomina", calendar.getTime(), this);
            collectionReference.add(t1).addOnSuccessListener(docRef -> docRef.update("transactionId", docRef.getId()));
            calendar.add(Calendar.DAY_OF_WEEK, -8);
            Transaction t2 = new Transaction("Nedesa", true, (float) (Math.random() * 158) - 158, "Luh, agua y ga", calendar.getTime(), this);
            collectionReference.add(t2).addOnSuccessListener(docRef -> docRef.update("transactionId", docRef.getId()));
        }
    }


    /**
     * Retrieves the IBAN of the bank account.
     *
     * @return The IBAN
     */
    public String getIban() {
        return iban;
    }

    /**
     * Sets the IBAN of the bank account.
     *
     * @param iban The IBAN to set
     */
    public void setIban(String iban) {
        this.iban = iban;
    }

    /**
     * Retrieves the Owner of the bank account.
     *
     * @return The Owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the Owner of the bank account.
     *
     * @param owner The Owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Retrieves the CIF of the bank account.
     *
     * @return The CIF
     */
    public String getCif() {
        return cif;
    }

    /**
     * Sets the CIF of the bank account.
     *
     * @param cif The CIF to set
     */
    public void setCif(String cif) {
        this.cif = cif;
    }

    /**
     * Retrieves the Balance of the bank account.
     *
     * @return The Balance
     */
    public float getBalance() {
        return balance;
    }

    /**
     * Retrieves the Balance formatted to #.## of the bank account.
     *
     * @return The Balance Formatted
     */
    @Exclude
    public String getBalanceString() {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(balance);
    }

    /**
     * Sets the Balance of the bank account.
     *
     * @param balance The value to the Balance to set
     */
    public void setBalance(float balance) {
        this.balance = balance;
    }

    /**
     * Retrieves the UserID of the bank account.
     *
     * @return The UserID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the UserID of the bank account.
     *
     * @param userId The UserID to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Saves a bank account to a user and updates Firestore accordingly.
     *
     * @param account  The bank account to save
     * @param callback Callback function to handle the result (true if successful, false otherwise)
     */
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

    /**
     * Deletes the bank account and updates Firestore accordingly.
     */
    public void deleteAccount() {
        FirebaseFirestore.getInstance().collection("bankAccount").document(getIban()).delete();
        FirebaseFirestore.getInstance().collection("users").document(userId).update("bankAccounts", FieldValue.arrayRemove(getIban()));
        System.out.println("Cuenta: " + getIban() + " Eliminada!");
    }


    /**
     * Retrieves the IBAN value for the default toString.
     *
     * @return Iban to String
     */
    @NonNull
    @Override
    public String toString() {
        return iban;
    }

    /**
     * Retrieves the class toString
     *
     * @return All the class to String
     */
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
