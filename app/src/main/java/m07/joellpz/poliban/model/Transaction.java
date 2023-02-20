package m07.joellpz.poliban.model;

import java.util.Date;

public class Transaction {
    private int fromId;
    private int value;
    private String to;
    private Date date;

    public Transaction() {
    }

    public Transaction(int fromId, int value, String to, Date date) {
        this.fromId = fromId;
        this.value = value;
        this.to = to;
        this.date = date;
    }

    public int getFrom() {
        return fromId;
    }

    public void setFrom(int fromId) {
        this.fromId = fromId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
