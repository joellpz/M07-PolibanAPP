package m07.joellpz.poliban.model;

import java.util.List;

public class BankAccount {
    private String iban;
    private String owner;
    private String cif;
    private int balance;
    private List<Transaction> transactionList;

    public BankAccount() {
    }

    public BankAccount(String iban, String owner, String cif, int balance, List<Transaction> transactionList) {
        this.iban = iban;
        this.owner = owner;
        this.cif = cif;
        this.balance = balance;
        this.transactionList = transactionList;
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
}
