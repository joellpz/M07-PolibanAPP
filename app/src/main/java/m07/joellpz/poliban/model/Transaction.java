package m07.joellpz.poliban.model;

import android.graphics.Color;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;

public class Transaction{

    private int bankId;
    private String from;
    private float value;
    private String subject;
    private Date date;
    private float opinion;
    private boolean future;

    public Transaction(String from, boolean future, float value, String subject, Date date) {
        this.from = from;
        this.future = future;
        this.value = value;
        this.subject = subject;
        this.date = date;
    }

    public Transaction(String from, boolean future, float value, String subject, Date date, float opinion) {
        this.from = from;
        this.value = value;
        this.future = future;
        this.subject = subject;
        this.date = date;
        this.opinion = opinion;
    }

    public float getOpinion() {
        return opinion;
    }

    public void setOpinion(float opinion) {
        this.opinion = opinion;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String fromId) {
        this.from = fromId;
    }

    public boolean isFuture() {
        return future;
    }

    public void setFuture(boolean future) {
        this.future = future;
    }

    public float getValue() {
        return value;
    }

    public String getValueString() {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(value);
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Event getTransactionEvent() {
        if (getValue() > 0) {
            if (isFuture()) return new Event(Color.parseColor("#502EAB15"), getDate().getTime(), this);
            else return new Event(Color.GREEN, getDate().getTime(), this);
        } else {
            if (isFuture()) return new Event(Color.parseColor("#50D40000"), getDate().getTime(), this);
            else return new Event(Color.RED, getDate().getTime(), this);
        }
    }
    @Override
    public String toString() {
        return "Transaction{" +
                "fromId=" + from +
                ", value=" + value +
                ", to='" + subject + '\'' +
                ", date=" + date +
                '}';
    }
}
