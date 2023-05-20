package m07.joellpz.poliban.adapter.viewHolders;

import static android.content.ContentValues.TAG;

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
import m07.joellpz.poliban.adapter.BankAccountAdapter;
import m07.joellpz.poliban.adapter.TransactionsAdapter;
import m07.joellpz.poliban.adapter.TransactionsCardAdapter;
import m07.joellpz.poliban.databinding.ActivityMainBinding;
import m07.joellpz.poliban.databinding.ViewholderBankAccountBinding;
import m07.joellpz.poliban.main.HomeFragment;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.model.Transaction;

public class BankAccountViewHolder extends RecyclerView.ViewHolder {
    private final ViewholderBankAccountBinding binding;
    private BankAccount bankAccount;
    private Fragment parentFragment;

    TransactionsAdapter mainAdapter, explicitAdapter, explicitFutureAdapter;

    float totalBalanceMonth, totalComeMonth;
    List<Transaction> transactionsPerMonth = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("#.##");

    List<Transaction> transactionsToCards = new ArrayList<>();
    public BankAccountViewHolder(ViewholderBankAccountBinding binding,Fragment parentFragment) {
        super(binding.getRoot());
        this.binding = binding;
        this.parentFragment = parentFragment;
    }

    public void bind(BankAccount bankAccount) {
        this.bankAccount = bankAccount;

        df.setRoundingMode(RoundingMode.CEILING);

        //TODO PONER MARCADORES DE COLORES EN EL MAPA

        //Bank info Introduce
        setMainInfo();
        Query qTransactions = FirebaseFirestore.getInstance().collection("bankAccount").document(bankAccount.getIban()).collection("transaction").orderBy("date");
        FirestoreRecyclerOptions<Transaction> options = new FirestoreRecyclerOptions.Builder<Transaction>()
                .setQuery(qTransactions, Transaction.class)
                .setLifecycleOwner(parentFragment.getParentFragment())
                .build();

        binding.recyclerView.setAdapter(new TransactionsAdapter(options, parentFragment,false));



        //transactionsPerMonth = bankAccount.findTransactionPerMonth(new Date(), 2);


        //explicitAdapter = new TransactionsAdapter(transactionsPerMonth, parentFragment);
        binding.calendarExplicitIbanFragment.recyclerViewCalendarExp.setAdapter(explicitAdapter);

        //explicitFutureAdapter = new TransactionsAdapter(bankAccount.getFutureTransactions(), parentFragment);
        binding.calendarExplicitIbanFragment.recyclerViewFutureCalendarExp.setAdapter(explicitFutureAdapter);

        transactionsPerMonth.forEach(transaction -> totalBalanceMonth += transaction.getValue());
        binding.calendarExplicitIbanFragment.textBalanceCalendarExp.setText(df.format(totalBalanceMonth));


        bankAccount.getFutureTransactions().forEach(transaction -> totalComeMonth += transaction.getValue());
        binding.calendarExplicitIbanFragment.textToComeCalendarExp.setText(df.format(totalComeMonth));


        //Sets Charts Info And Calendar Events
        setChartsInfo("");
        setCalendarViewAppearance(parentFragment.getView());

        binding.fragmentTransactionCards.goBackBtnCards.setOnClickListener(l -> binding.fragmentTransactionCards.getRoot().setVisibility(View.INVISIBLE));
        binding.mapImageContainer.setOnClickListener(l -> Navigation.findNavController(parentFragment.getView()).navigate(R.id.mapsFragment));

        binding.bizumButton.setOnClickListener(l -> ActivityMainBinding.inflate(parentFragment.getLayoutInflater()).appBarMain.contentMain.bottomMainMenu.findViewById(R.id.payFragment).performClick());
        binding.creditButton.setOnClickListener(l -> ActivityMainBinding.inflate(parentFragment.getLayoutInflater()).appBarMain.contentMain.bottomMainMenu.findViewById(R.id.payFragment).performClick());
    }

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
    private void setChartsInfo(String time) {
        ArrayList<Entry> expenditureList = new ArrayList<>();
        ArrayList<Entry> revenueList = new ArrayList<>();
        List<Transaction> filteredTransactionList;
        int totalRevenue = 0, totalExpenditure = 0;

        setChartProperties(binding.chartExped);
        setChartProperties(binding.chartRevenue);

        if (time.equals("week")) {
            binding.revenueTimeLabel.setText(R.string.lastWeek);
            binding.expenditureTimeLabel.setText(R.string.lastWeek);
            filteredTransactionList = bankAccount.findTransactionPerWeek(new Date());
        } else {
            binding.revenueTimeLabel.setText(R.string.lastMonth);
            binding.expenditureTimeLabel.setText(R.string.lastMonth);
            filteredTransactionList = bankAccount.findTransactionPerMonth(new Date(),2);
        }

        for (Transaction transaction : filteredTransactionList) {
            if (transaction.getValue() > 0) {
                revenueList.add(new Entry(revenueList.size() + 1, transaction.getValue()));
                totalRevenue += transaction.getValue();
            } else {
                expenditureList.add(new Entry(expenditureList.size() + 1, transaction.getValue() * (-1)));
                totalExpenditure += transaction.getValue();
            }
        }


        binding.expenditurePrice.setText(df.format(totalExpenditure));
        binding.revenuePrice.setText(df.format(totalRevenue));

        LineDataSet expenditure = new LineDataSet(expenditureList, "Expenditure");
        LineDataSet revenue = new LineDataSet(revenueList, "Revenue");
        revenue.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        expenditure.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        revenue.setLineWidth(1.5f);
        revenue.setDrawFilled(true);
        revenue.setDrawCircles(false);
        revenue.setDrawValues(false);

        expenditure.setLineWidth(1.5f);
        expenditure.setDrawFilled(true);
        expenditure.setDrawCircles(false);
        expenditure.setDrawValues(false);

        expenditure.setFillColor(ContextCompat.getColor(binding.chartExped.getContext(), R.color.red_light));
        expenditure.setColor(ContextCompat.getColor(binding.chartExped.getContext(), R.color.red_less));

        revenue.setFillColor(ContextCompat.getColor(binding.chartRevenue.getContext(), R.color.green_light));
        revenue.setColor(ContextCompat.getColor(binding.chartRevenue.getContext(), R.color.green));

        binding.chartExped.setData(new LineData(expenditure));
        binding.chartRevenue.setData(new LineData(revenue));

        binding.chartRevenueLayout.setOnClickListener(l -> {
            if (time.equals("week")) setChartsInfo("month");
            else setChartsInfo("week");
        });

        binding.chartExpenditureLayout.setOnClickListener(l -> {
            if (time.equals("week")) setChartsInfo("month");
            else setChartsInfo("week");
        });
    }
    private void setChartProperties(LineChart chart) {
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(false);
        chart.setDrawGridBackground(false);
        chart.getXAxis().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
    }

    private void setCalendarViewAppearance(@NonNull View view) {
        Date today = new Date();
        SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", new Locale("es", "ES"));

        binding.compactcalendarView.setUseThreeLetterAbbreviation(true);
        binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.setUseThreeLetterAbbreviation(true);

        binding.monthText.setText(dateFormatForMonth.format(binding.compactcalendarView.getFirstDayOfCurrentMonth()));
        binding.calendarExplicitIbanFragment.monthTextExplicit.setText(dateFormatForMonth.format(binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.getFirstDayOfCurrentMonth()));

        for (Transaction transaction : bankAccount.getTransactionList()) {
            binding.compactcalendarView.addEvent(transaction.obtenerTransactionEvent());
            binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.addEvent(transaction.obtenerTransactionEvent());
        }

        for (Transaction transaction : bankAccount.getFutureTransactions()) {
            binding.compactcalendarView.addEvent(transaction.obtenerTransactionEvent());
            binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.addEvent(transaction.obtenerTransactionEvent());
        }
        CompactCalendarView.CompactCalendarViewListener compactCalendarViewListener = new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = binding.compactcalendarView.getEvents(dateClicked);
                if (events.size() > 0) {
                    transactionsToCards.clear();
                    for (Event event : events) {
                        transactionsToCards.add((Transaction) event.getData());
                    }
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

                transactionsPerMonth = bankAccount.findTransactionPerMonth(firstDayOfNewMonth, 2);
                explicitAdapter = new TransactionsAdapter(transactionsPerMonth,parentFragment);
                binding.calendarExplicitIbanFragment.recyclerViewCalendarExp.setAdapter(explicitAdapter);

                totalComeMonth = 0;
                totalBalanceMonth = 0;
                transactionsPerMonth.forEach(transaction -> totalBalanceMonth += transaction.getValue());
                binding.calendarExplicitIbanFragment.textBalanceCalendarExp.setText(df.format(totalBalanceMonth));

                binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.setCurrentDate(firstDayOfNewMonth);
                binding.compactcalendarView.setCurrentDate(firstDayOfNewMonth);
                binding.monthText.setText(dateFormatForMonth.format(firstDayOfNewMonth));
                binding.calendarExplicitIbanFragment.monthTextExplicit.setText(dateFormatForMonth.format(firstDayOfNewMonth));
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
            }
        };
    }
}