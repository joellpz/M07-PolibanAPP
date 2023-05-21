package m07.joellpz.poliban.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

public class WalletCard {
    private String bankId;
    private String cardId;
    private float balance;
    private String cardNum;
    private String cardOwner;
    private int cvv;
    private String expDate;
    private boolean active;

    public WalletCard() {
    }

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

    public WalletCard(BankAccount bankAccount) {
        this.bankId = bankAccount.getIban();
        this.balance = (int) generateRandomNumber(3);
        this.cardNum = generateCardNumber();
        this.cardOwner = bankAccount.getOwner();
        this.cvv = (int) generateRandomNumber(3);
        this.expDate = generateRandomDate();
        this.active = true;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardOwner() {
        return cardOwner;
    }

    public void setCardOwner(String cardOwner) {
        this.cardOwner = cardOwner;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void changeActiveStatus(boolean active) {
        FirebaseFirestore.getInstance().collection("bankAccount").document(bankId).collection("walletCard").document(cardId).update("active", active);
        setActive(active);
    }

    @Exclude
    public String getBalanceString() {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(balance);
    }

    public static void generateNewCardToAccount(BankAccount bankAccount) {
        FirebaseFirestore.getInstance().collection("bankAccount").document(bankAccount.getIban())
                .collection("walletCard")
                .add(new WalletCard(bankAccount))
                .addOnSuccessListener(docRef -> {
                    System.out.println(docRef);
                    docRef.update("cardId", docRef.getId());
                });
    }

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

    @SuppressLint("DefaultLocale")
    private String generateRandomDate() {
        Random random = new Random();
        int month = random.nextInt(12) + 1; // Generar un número aleatorio entre 1 y 12 para el mes
        int day = random.nextInt(31) + 1; // Generar un número aleatorio entre 1 y 31 para el día

        return String.format("%02d/%02d", month, day);
    }

    public void deleteWalletCard(){
        FirebaseFirestore.getInstance().collection("bankAccount").document(getBankId()).collection("walletCard").document(getCardId()).delete();
    }

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
