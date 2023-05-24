package m07.joellpz.poliban.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Represents a wallet card associated with a bank account.
 */
public class WalletCard {

    /**
     * Bank ID
     */
    private String bankId;
    /**
     * Card ID
     */
    private String cardId;
    /**
     * Card balance
     */
    private float balance;
    /**
     * Card number
     */
    private String cardNum;
    /**
     * Card owner
     */
    private String cardOwner;
    /**
     * Card verification value
     */
    private int cvv;
    /**
     * Card expiration date
     */
    private String expDate;
    /**
     * Card activation status
     */
    private boolean active;


    /**
     * Default constructor required for Firestore deserialization.
     */
    public WalletCard() {
    }

    /**
     * Creates a new wallet card with the specified details.
     *
     * @param bankId     the bank ID associated with the card
     * @param cardId     the card ID
     * @param balance    the card balance
     * @param cardNum    the card number
     * @param cardOwner  the card owner
     * @param cvv        the card verification value
     * @param expDate    the card expiration date
     * @param active     the card activation status
     */
    public WalletCard(String bankId, String cardId, float balance, String cardNum, String cardOwner, int cvv, String expDate, boolean active) {
        this.bankId = bankId;
        this.cardId = cardId;
        this.balance = balance;
        this.cardNum = cardNum;
        this.cardOwner = cardOwner;
        this.cvv = cvv;
        this.expDate = expDate;
        this.active = active;
    }

    /**
     * Creates a new wallet card based on the given bank account.
     *
     * @param bankAccount the bank account to create the card from
     */
    public WalletCard(BankAccount bankAccount) {
        this.bankId = bankAccount.getIban();
        this.balance = (int) generateRandomNumber(3);
        this.cardNum = generateCardNumber();
        this.cardOwner = bankAccount.getOwner();
        this.cvv = (int) generateRandomNumber(3);
        this.expDate = generateRandomDate();
        this.active = true;
    }

    /**
     * Retrieves the bank ID associated with the card.
     *
     * @return the bank ID
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * Sets the bank ID associated with the card.
     *
     * @param bankId the bank ID to set
     */
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    /**
     * Retrieves the card ID.
     *
     * @return the card ID
     */
    public String getCardId() {
        return cardId;
    }

    /**
     * Sets the card ID.
     *
     * @param cardId the card ID to set
     */
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    /**
     * Retrieves the card balance.
     *
     * @return the card balance
     */
    public float getBalance() {
        return balance;
    }

    /**
     * Sets the card balance.
     *
     * @param balance the card balance to set
     */
    public void setBalance(float balance) {
        this.balance = balance;
    }

    /**
     * Retrieves the card number.
     *
     * @return the card number
     */
    public String getCardNum() {
        return cardNum;
    }

    /**
     * Sets the card number.
     *
     * @param cardNum the card number to set
     */
    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    /**
     * Retrieves the card owner.
     *
     * @return the card owner
     */
    public String getCardOwner() {
        return cardOwner;
    }

    /**
     * Sets the card owner.
     *
     * @param cardOwner the card owner to set
     */
    public void setCardOwner(String cardOwner) {
        this.cardOwner = cardOwner;
    }

    /**
     * Retrieves the card verification value (CVV).
     *
     * @return the card CVV
     */
    public int getCvv() {
        return cvv;
    }

    /**
     * Sets the card verification value (CVV).
     *
     * @param cvv the card CVV to set
     */
    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    /**
     * Retrieves the card expiration date.
     *
     * @return the card expiration date
     */
    public String getExpDate() {
        return expDate;
    }

    /**
     * Sets the card expiration date.
     *
     * @param expDate the card expiration date to set
     */
    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    /**
     * Checks if the card is active.
     *
     * @return true if the card is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the card activation status.
     *
     * @param active the card activation status to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Changes the active status of the card and updates it in the database.
     *
     * @param active the new active status
     */
    public void changeActiveStatus(boolean active) {
        FirebaseFirestore.getInstance().collection("bankAccount").document(bankId).collection("walletCard").document(cardId).update("active", active);
        setActive(active);
    }

    /**
     * Retrieves the card balance as a formatted string.
     *
     * @return the formatted card balance
     */
    @Exclude
    public String getBalanceString() {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(balance);
    }

    /**
     * Generates a new wallet card for the given bank account and adds it to the database.
     *
     * @param bankAccount the bank account to generate the card for
     */
    public static void generateNewCardToAccount(BankAccount bankAccount) {
        FirebaseFirestore.getInstance().collection("bankAccount").document(bankAccount.getIban())
                .collection("walletCard")
                .add(new WalletCard(bankAccount))
                .addOnSuccessListener(docRef -> {
                    System.out.println(docRef);
                    docRef.update("cardId", docRef.getId());
                });
    }

    /**
     * Generates the Card Number and Format it.
     * @return The Card Number in String
     */
    private String generateCardNumber() {
        StringBuilder result = new StringBuilder();
        String number = generateRandomNumber(16) + "";
        for (int i = 0; i < number.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                result.append(" ");
            }
            result.append(number.charAt(i));
        }
        return result.toString();
    }

    /**
     * Generates random number.
     * @param digits how long the number will be
     * @return the number
     */
    private long generateRandomNumber(int digits) {
        if (digits <= 0) {
            throw new IllegalArgumentException("El número de dígitos debe ser mayor que cero");
        }

        long min = (long) Math.pow(10, digits - 1);
        long max = (long) Math.pow(10, digits) - 1;

        Random random = new Random();
        long value = min + random.nextLong() % (max - min + 1);

        return value < 0 ? value * -1 : value;
    }

    /**
     * Returns the caducity in String and formatted
     * @return Date in String "%02d/%02d"
     */
    @SuppressLint("DefaultLocale")
    private String generateRandomDate() {
        Random random = new Random();
        int month = random.nextInt(12) + 1; // Generar un número aleatorio entre 1 y 12 para el mes
        int day = random.nextInt(31) + 1; // Generar un número aleatorio entre 1 y 31 para el día

        return String.format("%02d/%02d", month, day);
    }

    /**
     * Deletes the wallet card from the database.
     */
    public void deleteWalletCard(){
        FirebaseFirestore.getInstance().collection("bankAccount").document(getBankId()).collection("walletCard").document(getCardId()).delete();
    }

    /**
     * WalletCard ToString
     * @return Class toString
     */
    @NonNull
    @Override
    public String toString() {
        return "WalletCard{" +
                "balance=" + balance +
                ", cardNum=" + cardNum +
                ", cardOwner='" + cardOwner + '\'' +
                ", cvv=" + cvv +
                ", expDate=" + expDate +
                '}';
    }
}