package m07.joellpz.poliban.model;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WalletCard {
    private float balance;
    private final String cardNum;
    private final String cardOwner;
    private final int cvv;
    private final Date expDate;
    private boolean active;

    public WalletCard(float balance, String cardNum, String cardOwner, int cvv, Date expDate, boolean active) {
        this.balance = balance;
        this.cardNum = cardNum;
        this.cardOwner = cardOwner;
        this.cvv = cvv;
        this.expDate = expDate;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public float getBalance() {
        return balance;
    }

    public String getCardNum() {
        return cardNum;
    }

    public String getCardOwner() {
        return cardOwner;
    }

    public int getCvv() {
        return cvv;
    }

    public String getExpDateToCard() {

        return new SimpleDateFormat("MM/yy", Locale.FRANCE).format(expDate);
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
