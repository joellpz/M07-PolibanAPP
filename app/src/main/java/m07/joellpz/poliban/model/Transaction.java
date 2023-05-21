package m07.joellpz.poliban.model;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Transaction {
    private String bankId;
    private String transactionId;
    private String from;
    private float value;
    private String subject;
    private Date date;
    private float opinion;
    private boolean future;

    public Transaction() {
    }

    public Transaction(String from, boolean future, float value, String subject, Date date, BankAccount bankAccount) {
        this.from = from;
        this.future = future;
        this.value = value;
        this.subject = subject;
        this.date = date;
        this.bankId = bankAccount.getIban();
    }

    public Transaction(String from, boolean future, float value, String subject, Date date, float opinion) {
        this.from = from;
        this.value = value;
        this.future = future;
        this.subject = subject;
        this.date = date;
        this.opinion = opinion;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

    @Exclude
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

    @Exclude
    public Event getTransactionEvent() {
        if (getValue() > 0) {
            if (isFuture())
                return new Event(Color.parseColor("#502EAB15"), getDate().getTime(), this);
            else return new Event(Color.GREEN, getDate().getTime(), this);
        } else {
            if (isFuture())
                return new Event(Color.parseColor("#50D40000"), getDate().getTime(), this);
            else return new Event(Color.RED, getDate().getTime(), this);
        }
    }

    @Exclude
    public String getDateFormatted(){
        return new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES")).format(getDate());
    }

    @Exclude
    public static Query getQueryTransactions(String time, Date firstDayOfNewMonth, BankAccount bankAccount) {
        Calendar calendar = Calendar.getInstance();
        Date firstDay;

        if (time.equals("day")) {
            firstDay = firstDayOfNewMonth;
            calendar.setTime(firstDay);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } else if (time.equals("week")) {
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.add(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() - calendar.get(Calendar.DAY_OF_WEEK) - 8);
            firstDay = calendar.getTime();
            calendar.add(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() - calendar.get(Calendar.DAY_OF_WEEK) + 6);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            if (firstDayOfNewMonth == null) firstDay = calendar.getTime();
            else {
                firstDay = firstDayOfNewMonth;
                calendar.setTime(firstDayOfNewMonth);
            }
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        System.out.println(calendar.getTime());
        return FirebaseFirestore.getInstance()
                .collection("bankAccount")
                .document(bankAccount.getIban())
                .collection("transaction")
                .whereGreaterThanOrEqualTo("date", firstDay)
                .whereLessThanOrEqualTo("date", calendar.getTime());
    }

    @NonNull
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
