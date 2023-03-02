package m07.joellpz.poliban.model;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WalletCard {
    private float balance;
    private String cardNum;
    private String cardOwner;
    private int cvv;
    private Date expDate;
    private boolean active;

    public WalletCard() {
        this.active = true;
    }

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

    public Date getExpDate() {
        return expDate;
    }

    public String getExpDateToCard() {

        return new SimpleDateFormat("MM/yy").format(expDate);
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
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
