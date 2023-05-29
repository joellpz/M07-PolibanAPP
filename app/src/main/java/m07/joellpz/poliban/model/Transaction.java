package m07.joellpz.poliban.model;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.maps.android.clustering.ClusterItem;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Class representing a transaction.
 */
public class Transaction implements ClusterItem {
    /**
     * The ID of the bank associated with the transaction.
     */
    private String bankId;

    /**
     * The ID of the transaction.
     */
    private String transactionId;

    /**
     * The sender/receiver of the transaction.
     */
    private String from;

    /**
     * The value of the transaction.
     */
    private float value;

    /**
     * The subject of the transaction.
     */
    private String subject;

    /**
     * The date of the transaction.
     */
    private Date date;

    /**
     * The opinion/rating of the transaction.
     */
    private float opinion;

    /**
     * Indicates if the transaction is a future transaction.
     */
    private boolean future;

    /**
     * The bank account associated with the transaction.
     */
    private BankAccount bankAccount;

    /**
     * The geographical location of the transaction.
     */
    private GeoPoint ubi;

    /**
     * Default constructor for the Transaction class.
     */
    public Transaction() {

    }

    /**
     * Constructor for the Transaction class.
     *
     * @param from        The sender/receiver of the transaction.
     * @param future      Indicates if the transaction is a future transaction.
     * @param value       The value of the transaction.
     * @param subject     The subject of the transaction.
     * @param date        The date of the transaction.
     * @param bankAccount The bank account associated with the transaction.
     */
    public Transaction(String from, boolean future, float value, String subject, Date date, BankAccount bankAccount) {
        this.from = from;
        this.future = future;
        this.value = value;
        this.subject = subject;
        this.date = date;
        this.bankAccount = bankAccount;
        this.bankId = bankAccount.getIban();
        this.ubi = getRandomPosition();
    }

    /**
     * Constructor for the Transaction class.
     *
     * @param from    The sender/receiver of the transaction.
     * @param future  Indicates if the transaction is a future transaction.
     * @param value   The value of the transaction.
     * @param subject The subject of the transaction.
     * @param date    The date of the transaction.
     * @param opinion The opinion/rating of the transaction.
     * @param ubi     The geographical location of the transaction.
     */
    public Transaction(String from, boolean future, float value, String subject, Date date, float opinion, GeoPoint ubi) {
        this.from = from;
        this.value = value;
        this.future = future;
        this.subject = subject;
        this.date = date;
        this.opinion = opinion;
        this.ubi = ubi;
    }

    /**
     * Retrieves the ID of the bank associated with the transaction.
     *
     * @return The ID of the bank.
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * Sets the ID of the bank associated with the transaction.
     *
     * @param bankId The ID of the bank.
     */
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    /**
     * Retrieves the ID of the transaction.
     *
     * @return The ID of the transaction.
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the ID of the transaction.
     *
     * @param transactionId The ID of the transaction.
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Retrieves the opinion/rating of the transaction.
     *
     * @return The opinion/rating of the transaction.
     */
    public float getOpinion() {
        return opinion;
    }

    /**
     * Sets the opinion/rating of the transaction.
     *
     * @param opinion The opinion/rating of the transaction.
     */
    public void setOpinion(float opinion) {
        this.opinion = opinion;
    }

    /**
     * Retrieves the sender/receiver of the transaction.
     *
     * @return The sender/receiver of the transaction.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the sender/receiver of the transaction.
     *
     * @param fromId The sender/receiver of the transaction.
     */
    public void setFrom(String fromId) {
        this.from = fromId;
    }

    /**
     * Checks if the transaction is a future transaction.
     *
     * @return True if the transaction is a future transaction, false otherwise.
     */
    public boolean isFuture() {
        return future;
    }

    /**
     * Sets whether the transaction is a future transaction or not.
     *
     * @param future True if the transaction is a future transaction, false otherwise.
     */
    public void setFuture(boolean future) {
        this.future = future;
    }

    /**
     * Retrieves the geographical location of the transaction.
     *
     * @return The geographical location of the transaction.
     */
    public GeoPoint getUbi() {
        return ubi;
    }

    /**
     * Sets the geographical location of the transaction.
     *
     * @param ubi The geographical location of the transaction.
     */
    public void setUbi(GeoPoint ubi) {
        this.ubi = ubi;
    }

    /**
     * Retrieves the value of the transaction.
     *
     * @return The value of the transaction.
     */
    public float getValue() {
        return value;
    }

    /**
     * Retrieves the value of the transaction as a formatted string.
     *
     * @return The value of the transaction as a formatted string.
     */
    @Exclude
    public String getValueString() {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(value);
    }

    /**
     * Sets the value of the transaction.
     *
     * @param value The value of the transaction.
     */
    public void setValue(float value) {
        this.value = value;
    }

    /**
     * Retrieves the subject of the transaction.
     *
     * @return The subject of the transaction.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject of the transaction.
     *
     * @param subject The subject of the transaction.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Retrieves the date of the transaction.
     *
     * @return The date of the transaction.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date of the transaction.
     *
     * @param date The date of the transaction.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Retrieves the bank account associated with the transaction.
     *
     * @return The bank account associated with the transaction.
     */
    @Exclude
    public BankAccount getBankAccount() {
        return bankAccount;
    }

    /**
     * Retrieves the transaction event for the CompactCalendarView library.
     *
     * @return The transaction event for the CompactCalendarView.
     */
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

    /**
     * Retrieves the formatted date of the transaction.
     *
     * @return The formatted date of the transaction.
     */
    @Exclude
    public String getDateFormatted() {
        return new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES")).format(getDate());
    }

    /**
     * Retrieves the Firestore query for retrieving transactions based on a given time period.
     *
     * @param time               The time period ("day", "week", or "month").
     * @param firstDayOfNewMonth The first day of the new month.
     * @param bankAccount        The bank account associated with the transactions.
     * @return The Firestore query for retrieving transactions.
     */
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

    /**
     * Creates a transaction and stores it in Firestore.
     *
     * @param t The transaction to be created.
     * @return A Task representing the creation of the transaction.
     */
    public static Task<?> makeTransaction(Transaction t) {
        return FirebaseFirestore.getInstance().collection("bankAccount")
                .document(t.getBankId())
                .collection("transaction")
                .add(t)
                .addOnSuccessListener( docRef -> {
                    docRef.update("transactionId", docRef.getId());

                    FirebaseFirestore.getInstance().collection("bankAccount")
                            .document(t.getBankId())
                            .update("balance",t.getBankAccount().getBalance()+t.value);
                });


    }

    /**
     * Generates a random geographical position within a specific range.
     *
     * @return The generated geographical position.
     */
    @Exclude
    private static GeoPoint getRandomPosition() {
        double minLat = 41.3879;
        double maxLat = 41.4655;
        double minLng = 2.1301;
        double maxLng = 2.2522;

        Random random = new Random();
        double lat = minLat + (maxLat - minLat) * random.nextDouble();
        double lng = minLng + (maxLng - minLng) * random.nextDouble();

        return new GeoPoint(lat, lng);
    }

    /**
     * Retrieves the position of the transaction for the ClusterManager.
     *
     * @return The position of the transaction.
     */
    @NonNull
    @Override
    @Exclude
    public LatLng getPosition() {
        return new LatLng(getUbi().getLatitude(), getUbi().getLongitude());
    }

    /**
     * Retrieves the title of the transaction for the ClusterManager.
     *
     * @return The title of the transaction.
     */
    @Override
    @Exclude
    public String getTitle() {
        return getSubject();
    }

    /**
     * Retrieves the snippet of the transaction for the ClusterManager.
     *
     * @return The snippet of the transaction.
     */
    @Override
    @Exclude
    public String getSnippet() {
        return getValueString();
    }

    /**
     * Retrieves the ZIndex of the transaction for the ClusterManager.
     *
     * @return The ZIndex of the transaction.
     */
    @Nullable
    @Override
    public Float getZIndex() {
        return null;
    }
}
