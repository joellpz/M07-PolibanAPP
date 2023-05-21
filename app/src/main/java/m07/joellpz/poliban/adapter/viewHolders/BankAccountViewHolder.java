package m07.joellpz.poliban.adapter.viewHolders;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.adapter.TransactionsAdapter;
import m07.joellpz.poliban.adapter.TransactionsCardAdapter;
import m07.joellpz.poliban.databinding.ActivityMainBinding;
import m07.joellpz.poliban.databinding.ViewholderBankAccountBinding;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.model.Transaction;

public class BankAccountViewHolder extends RecyclerView.ViewHolder {
    private final ViewholderBankAccountBinding binding;
    private BankAccount bankAccount;
    private final Fragment parentFragment;
    DecimalFormat df = new DecimalFormat("#.##");
    List<Transaction> transactionsToCards = new ArrayList<>();

    public BankAccountViewHolder(ViewholderBankAccountBinding binding,Fragment parentFragment) {
        super(binding.getRoot());
        this.binding = binding;
        this.parentFragment = parentFragment;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void bind(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        FirestoreRecyclerOptions<Transaction> options;

        binding.hScrollBA.setOnTouchListener((v, event) -> {
            binding.hScrollBA.getParent().getParent().requestDisallowInterceptTouchEvent(true);
            parentFragment.requireView().findViewById(R.id.recyclerViewHome).setNestedScrollingEnabled(false);
            return false;
        });

        binding.linearcalendarView.setOnTouchListener((v, event) -> {
            binding.hScrollBA.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });


        df.setRoundingMode(RoundingMode.CEILING);

        //TODO PONER MARCADORES DE COLORES EN EL MAPA

        //Bank info Introduce
        setMainInfo();
        Query qTransactionsAll = FirebaseFirestore.getInstance().collection("bankAccount").document(bankAccount.getIban()).collection("transaction").orderBy("date", Query.Direction.DESCENDING);
        options = new FirestoreRecyclerOptions.Builder<Transaction>()
                .setQuery(qTransactionsAll, Transaction.class)
                .setLifecycleOwner(parentFragment.getParentFragment())
                .build();

        binding.recyclerView.setAdapter(new TransactionsAdapter(options, parentFragment, false));

        setCalendarForMonth(null);

        Query qTransactionsFuture = Transaction.getQueryTransactions("month", null, bankAccount).whereEqualTo("future", true).orderBy("date");
        options = new FirestoreRecyclerOptions.Builder<Transaction>()
                .setQuery(qTransactionsFuture, Transaction.class)
                .setLifecycleOwner(parentFragment.getParentFragment())
                .build();
        binding.calendarExplicitIbanFragment.recyclerViewFutureCalendarExp.setAdapter(new TransactionsAdapter(options, parentFragment, false));

        //Sets Charts Info And Calendar Events
        setChartsInfo("month");
        setCalendarViewAppearance();

        binding.fragmentTransactionCards.goBackBtnCards.setOnClickListener(l -> binding.fragmentTransactionCards.getRoot().setVisibility(View.INVISIBLE));
        binding.mapImageContainer.setOnClickListener(l -> Navigation.findNavController(parentFragment.requireView()).navigate(R.id.mapsFragment));

        binding.bizumButton.setOnClickListener(l -> ActivityMainBinding.inflate(parentFragment.getLayoutInflater()).appBarMain.contentMain.bottomMainMenu.findViewById(R.id.payFragment).performClick());
        binding.creditButton.setOnClickListener(l -> ActivityMainBinding.inflate(parentFragment.getLayoutInflater()).appBarMain.contentMain.bottomMainMenu.findViewById(R.id.payFragment).performClick());
    }

    /**
     * Defines the main information of the Bank Account
     */
    private void setMainInfo() {
        // Aquí puedes enlazar los datos de la cuenta bancaria con las vistas del item
        binding.ibanNumber.setText(bankAccount.getIban());
        binding.moneyBankInfo.setText(bankAccount.getBalanceString());
        binding.ownerInfo.setText(bankAccount.getOwner());
        if (bankAccount.getCif() != null) {
            binding.cifInfo.setText(bankAccount.getCif());
            binding.investButton.setOnClickListener(l -> {
                AlertDialog ad = new AlertDialog.Builder(parentFragment.getContext()).create();
                ad.setMessage("Have not been implemented yet...");
                ad.setCancelable(true);
                ad.show();
            });
        } else {
            binding.cifInfo.setVisibility(View.INVISIBLE);
            binding.investButton.setVisibility(View.GONE);
        }
        if (bankAccount.getIban().split(" ").length > 2) {
            if (bankAccount.getIban().split(" ")[1].equals("2100")) {
                binding.bankData.setBackgroundTintList(ColorStateList.valueOf(parentFragment.getResources().getColor(R.color.lacaixa, parentFragment.requireActivity().getTheme())));
                binding.bankEntityLogo.setImageResource(R.drawable.logo_lacaixa);
            } else if (bankAccount.getIban().split(" ")[1].equals("0057")) {
                binding.bankData.setBackgroundTintList(ColorStateList.valueOf(parentFragment.getResources().getColor(R.color.bbva, parentFragment.requireActivity().getTheme())));
                binding.bankEntityLogo.setImageResource(R.drawable.logo_bbva);
            } else if (bankAccount.getIban().split(" ")[1].equals("0049")) {
                binding.bankData.setBackgroundTintList(ColorStateList.valueOf(parentFragment.getResources().getColor(R.color.santander, parentFragment.requireActivity().getTheme())));
                binding.bankEntityLogo.setImageResource(R.drawable.logo_santander);
            }
        }
    }

    /**
     * Lets define what information goes inside each Chart.
     *
     * @param time Time Filter
     */
    private void setChartsInfo(@NonNull String time) {
        Query qTransaction;

        setChartVisualitzation(binding.chartRevenue);
        setChartVisualitzation(binding.chartExped);

        if (time.equals("week")) {
            binding.revenueTimeLabel.setText(R.string.lastWeek);
            binding.expenditureTimeLabel.setText(R.string.lastWeek);
        } else {
            binding.revenueTimeLabel.setText(R.string.lastMonth);
            binding.expenditureTimeLabel.setText(R.string.lastMonth);
        }
        qTransaction = Transaction.getQueryTransactions(time, null, bankAccount);

        qTransaction.get().addOnSuccessListener(ts -> {
            List<Transaction> transactions = ts.toObjects(Transaction.class);
            int totalRev = 0, totalExp = 0;
            for (int i = 0; i < transactions.size(); i++) {
                if (transactions.get(i).getValue() > 0) totalRev += transactions.get(i).getValue();
                else totalExp += transactions.get(i).getValue();
            }

            binding.revenuePrice.setText(df.format(totalRev));
            setChartInformation("Revenue", transactions, binding.chartRevenue);
            binding.expenditurePrice.setText(df.format(totalExp));
            setChartInformation("Expenditure", transactions, binding.chartExped);
        });

        binding.chartRevenueLayout.setOnClickListener(l -> {
            if (time.equals("week")) setChartsInfo("month");
            else setChartsInfo("week");
        });

        binding.chartExpenditureLayout.setOnClickListener(l -> {
            if (time.equals("week")) setChartsInfo("month");
            else setChartsInfo("week");
        });
    }

    /**
     * Defines the information that goes inside the Linear Chart
     *
     * @param name      Name of the Graph
     * @param tList     List of Transactions
     * @param lineChart LineChart of the Graph
     */
    private void setChartInformation(String name, @NonNull List<Transaction> tList, LineChart lineChart) {
        List<Entry> tEntrys = new ArrayList<>();
        LineDataSet dataSet;

        for (Transaction transaction : tList) {
            tEntrys.add(new Entry(tEntrys.size() + 1, transaction.getValue() < 0 ? transaction.getValue() : transaction.getValue() * (-1)));
        }

        if (tList.size() == 0) {
            tEntrys.add(new Entry(tEntrys.size() + 1, 0));
        }

        dataSet = new LineDataSet(tEntrys, name);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setLineWidth(1.5f);
        dataSet.setDrawFilled(true);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);

        dataSet.setFillColor(ContextCompat.getColor(lineChart.getContext(),
                name.equals("Expenditure") ? R.color.red_light : R.color.green_light));
        dataSet.setColor(ContextCompat.getColor(lineChart.getContext(),
                name.equals("Expenditure") ? R.color.red_less : R.color.green));

        lineChart.setData(new LineData(dataSet));
    }

    /**
     * Defines the visualization of the Linear Chart
     *
     * @param lineChart LineChart to Configure
     */
    private void setChartVisualitzation(@NonNull LineChart lineChart) {
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.getXAxis().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
    }


    private void setCalendarViewAppearance() {
        Date today = new Date();
        SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", new Locale("es", "ES"));

        binding.compactcalendarView.setUseThreeLetterAbbreviation(true);
        binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.setUseThreeLetterAbbreviation(true);

        binding.monthText.setText(dateFormatForMonth.format(binding.compactcalendarView.getFirstDayOfCurrentMonth()));
        binding.calendarExplicitIbanFragment.monthTextExplicit.setText(dateFormatForMonth.format(binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.getFirstDayOfCurrentMonth()));
        CompactCalendarView.CompactCalendarViewListener compactCalendarViewListener = new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = binding.compactcalendarView.getEvents(dateClicked);
                if (events.size() > 0) {
                    transactionsToCards.clear();
                    for (Event event : events) {
                        transactionsToCards.add((Transaction) event.getData());
                    }
                    //TODO CAMBIAR ESTE ADAPTADOR
                    TransactionsCardAdapter adapter = new TransactionsCardAdapter(transactionsToCards,parentFragment);
                    binding.fragmentTransactionCards.recyclerviewTransactionCards.setAdapter(adapter);
                    binding.fragmentTransactionCards.getRoot().setVisibility(View.VISIBLE);
                    Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                if (firstDayOfNewMonth.after(today)) {
                    binding.linearcalendarView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B1B1B1")));
                    binding.calendarExplicitIbanFragment.linearcalendarViewExplicit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B1B1B1")));
                } else {
                    binding.linearcalendarView.setBackgroundTintList(ColorStateList.valueOf(parentFragment.getResources().getColor(R.color.white, parentFragment.requireActivity().getTheme())));
                    binding.calendarExplicitIbanFragment.linearcalendarViewExplicit.setBackgroundTintList(ColorStateList.valueOf(parentFragment.getResources().getColor(R.color.white, parentFragment.requireActivity().getTheme())));
                }
                setCalendarForMonth(firstDayOfNewMonth);


                binding.monthText.setText(dateFormatForMonth.format(firstDayOfNewMonth));
                binding.calendarExplicitIbanFragment.monthTextExplicit.setText(dateFormatForMonth.format(firstDayOfNewMonth));
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
            }
        };

        binding.compactcalendarView.setListener(compactCalendarViewListener);
        binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.setListener(compactCalendarViewListener);

        binding.linearcalendarView.setOnClickListener(l -> {
            binding.calendarExplicitIbanFragment.getRoot().setVisibility(View.VISIBLE);
            binding.hScrollBA.getParent().getParent().requestDisallowInterceptTouchEvent(true);
        });
        binding.calendarExplicitIbanFragment.goBackBtn.setOnClickListener(l -> binding.calendarExplicitIbanFragment.getRoot().setVisibility(View.INVISIBLE));
    }

    private void setCalendarForMonth(Date firstDayOfNewMonth) {
        Query qTransactionsMonth = Transaction.getQueryTransactions("month", firstDayOfNewMonth, bankAccount).orderBy("date", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Transaction> options = new FirestoreRecyclerOptions.Builder<Transaction>()
                .setQuery(qTransactionsMonth, Transaction.class)
                .setLifecycleOwner(parentFragment.getParentFragment())
                .build();
        binding.calendarExplicitIbanFragment.recyclerViewCalendarExp.setAdapter(new TransactionsAdapter(options, parentFragment, false));

        qTransactionsMonth.get().addOnSuccessListener(ts -> {
            List<Transaction> transactions = ts.toObjects(Transaction.class);
            int future = 0, balance = 0;
            for (Transaction t : transactions) {
                if (t.isFuture()) future += t.getValue();
                else balance += t.getValue();
                binding.compactcalendarView.addEvent(t.getTransactionEvent());
                binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.addEvent(t.getTransactionEvent());
            }
            binding.calendarExplicitIbanFragment.textBalanceCalendarExp.setText(df.format(balance));
            binding.calendarExplicitIbanFragment.textToComeCalendarExp.setText(df.format(future));
        });

        binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.setCurrentDate(firstDayOfNewMonth == null ? binding.compactcalendarView.getFirstDayOfCurrentMonth() : firstDayOfNewMonth);
        binding.compactcalendarView.setCurrentDate(firstDayOfNewMonth == null ? binding.compactcalendarView.getFirstDayOfCurrentMonth() : firstDayOfNewMonth);
    }

    //TODO TAMBIEN AL CAMBIAR DE MES NO SE PONEN LOS PUNTITOS PERTINENTES

}