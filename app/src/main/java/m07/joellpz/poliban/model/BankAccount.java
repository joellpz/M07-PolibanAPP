package m07.joellpz.poliban.model;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class BankAccount implements Serializable {
    private String iban;
    private String owner;
    private String cif;
    private float balance;
    private List<Transaction> transactionList;
    private List<Transaction> futureTransactions;
    private List<WalletCard> walletCardList;

    public BankAccount() {
    }

    public BankAccount(String iban, String owner, String cif, float balance, List<Transaction> transactionList, List<WalletCard> walletCardList) {
        this.iban = iban;
        this.owner = owner;
        this.cif = cif;
        this.balance = balance;
        this.transactionList = transactionList;
        this.walletCardList = walletCardList;
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

    public String getBalanceString() {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(balance);
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactionList() {

        transactionList.sort(new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public List<WalletCard> getWalletCardList() {
        return walletCardList;
    }

    public void setWalletCardList(List<WalletCard> walletCardList) {
        this.walletCardList = walletCardList;
    }

    public List<Transaction> findTransactionPerMonth(Date firstDayOfNewMonth) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(firstDayOfNewMonth);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        Date firstDay = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.getDate().after(firstDay) && transaction.getDate().before(calendar.getTime()))
                filteredTransactions.add(transaction);
        }
        return filteredTransactions;
    }

    public List<Transaction> findTransactionPerWeek(Date firstDayOfNewMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(firstDayOfNewMonth);
        calendar.add(Calendar.DAY_OF_WEEK,calendar.getFirstDayOfWeek()-calendar.get(Calendar.DAY_OF_WEEK)-8);
        Date firstDay = calendar.getTime();

        //calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_WEEK));

        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.getDate().after(firstDay) && transaction.getDate().before(firstDayOfNewMonth))
                filteredTransactions.add(transaction);
        }
        return filteredTransactions;
    }


    @Override
    public String toString() {
        return "BankAccount{" +
                "iban='" + iban + '\'' +
                ", owner='" + owner + '\'' +
                ", cif='" + cif + '\'' +
                ", balance=" + balance +
                ", transactionList=" + transactionList +
                '}';
    }
}
