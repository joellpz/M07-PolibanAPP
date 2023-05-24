package m07.joellpz.poliban.adapter.viewHolders;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
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
import m07.joellpz.poliban.adapter.TransactionAdapter;
import m07.joellpz.poliban.databinding.ViewholderBankAccountBinding;
import m07.joellpz.poliban.databinding.ViewholderWalletCardRegisterBinding;
import m07.joellpz.poliban.main.HomeFragment;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.model.Transaction;
import m07.joellpz.poliban.view.MapsFragment;

public class BankAccountViewHolder extends RecyclerView.ViewHolder {
    /**
     * View binding for the fragment.
     */
    private final ViewholderBankAccountBinding binding;
    /**
     * Parent Fragment
     */
    private final Fragment parentFragment;
    /**
     * Bank Account to define the Objects of the ViewHolder
     */
    private BankAccount bankAccount;
    /**
     * Decimal Format
     */
    final DecimalFormat df = new DecimalFormat("#.##");

    /**
     * Constructs a new BankAccountViewHolder.
     *
     * @param binding          The ViewholderBankAccountBinding object associated with this ViewHolder.
     * @param parentFragment   The parent Fragment that holds the RecyclerView.
     */
    public BankAccountViewHolder(ViewholderBankAccountBinding binding,Fragment parentFragment) {
        super(binding.getRoot());
        this.binding = binding;
        this.parentFragment = parentFragment;
    }

    /**
     * Binds the ViewHolder to the data.
     */
    @SuppressLint("ClickableViewAccessibility")
    public void bind(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        FirestoreRecyclerOptions<Transaction> options;
        parentFragment.requireView().findViewById(R.id.custom_imageProgressBar).setVisibility(View.VISIBLE);

        FragmentTransaction mapFragment = parentFragment.requireActivity().getSupportFragmentManager().beginTransaction();
        mapFragment.replace(R.id.map, new MapsFragment(bankAccount));
        mapFragment.commit();

        binding.deleteAcoountBtn.setOnClickListener(v -> new AlertDialog.Builder(parentFragment.getContext())
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, (dialog, which) -> bankAccount.deleteAccount())

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .show());

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
        Query qTransactionsAll = FirebaseFirestore.getInstance().collection("bankAccount").document(bankAccount.getIban()).collection("transaction").whereEqualTo("future", false).orderBy("date", Query.Direction.DESCENDING);
        options = new FirestoreRecyclerOptions.Builder<Transaction>().setQuery(qTransactionsAll, Transaction.class).setLifecycleOwner(parentFragment.getParentFragment()).build();

        binding.recyclerView.setAdapter(new TransactionAdapter(options, parentFragment, false));

        setCalendarForMonth(null);

        Query qTransactionsFuture = Transaction.getQueryTransactions("month", null, bankAccount).whereEqualTo("future", true).orderBy("date", Query.Direction.DESCENDING);
        options = new FirestoreRecyclerOptions.Builder<Transaction>().setQuery(qTransactionsFuture, Transaction.class).setLifecycleOwner(parentFragment.getParentFragment()).build();
        binding.calendarExplicitIbanFragment.recyclerViewFutureCalendarExp.setAdapter(new TransactionAdapter(options, parentFragment, false));

        //Sets Charts Info And Calendar Events
        setChartsInfo("month");
        setCalendarViewAppearance();

        binding.fragmentTransactionCards.goBackBtnCards.setOnClickListener(l -> binding.fragmentTransactionCards.getRoot().setVisibility(View.INVISIBLE));
        binding.mapImageContainer.setOnClickListener(l -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("bankAccount", bankAccount);
            Navigation.findNavController(parentFragment.requireView()).navigate(R.id.mapsFragment, bundle);
        });

        binding.bizumButton.setOnClickListener(l -> ((HomeFragment) parentFragment).navController.navigate(R.id.payFragment));
        binding.creditButton.setOnClickListener(l -> ((HomeFragment) parentFragment).navController.navigate(R.id.payFragment));
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
            setChartData("Revenue", transactions, binding.chartRevenue);
            binding.expenditurePrice.setText(df.format(totalExp));
            setChartData("Expenditure", transactions, binding.chartExped);
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
    private void setChartData(String name, @NonNull List<Transaction> tList, LineChart lineChart) {
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
        System.out.println("Cambio de Graph ***************************");
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


    /**
     * Configures and Defines the visualization of the Calendars from the main view.
     */
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
                if (binding.compactcalendarView.getEvents(dateClicked).size() != 0) {
                    Query qTransactionsDay = Transaction.getQueryTransactions("day", dateClicked, bankAccount).orderBy("date", Query.Direction.DESCENDING);
                    FirestoreRecyclerOptions<Transaction> options = new FirestoreRecyclerOptions.Builder<Transaction>().setQuery(qTransactionsDay, Transaction.class).setLifecycleOwner(parentFragment.getParentFragment()).build();
                    binding.fragmentTransactionCards.recyclerviewTransactionCards.setAdapter(new TransactionAdapter(options, parentFragment, true));
                    binding.fragmentTransactionCards.getRoot().setVisibility(View.VISIBLE);
                    Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + binding.compactcalendarView.getEvents(dateClicked));
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

        binding.calArrowBack.setOnClickListener(l -> binding.compactcalendarView.scrollLeft());
        binding.calendarExplicitIbanFragment.calExpArrowBack.setOnClickListener(l -> {
            binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.scrollLeft();
            parentFragment.requireView().findViewById(R.id.custom_imageProgressBar).setVisibility(View.VISIBLE);
        });

        binding.calArrowNext.setOnClickListener(l -> binding.compactcalendarView.scrollRight());
        binding.calendarExplicitIbanFragment.calExpArrowNext.setOnClickListener(l -> {
            binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.scrollRight();
            parentFragment.requireView().findViewById(R.id.custom_imageProgressBar).setVisibility(View.VISIBLE);
        });

        binding.linearcalendarView.setOnClickListener(l -> {
            binding.calendarExplicitIbanFragment.getRoot().setVisibility(View.VISIBLE);
            RecyclerView recyclerView = parentFragment.requireView().findViewById(R.id.recyclerViewHome);
            recyclerView.setNestedScrollingEnabled(true);

        });
        binding.calendarExplicitIbanFragment.goBackBtn.setOnClickListener(l -> binding.calendarExplicitIbanFragment.getRoot().setVisibility(View.INVISIBLE));
    }

    /**
     * Makes a query to define the Transactions inside the calendar per Month,
     * @param firstDayOfNewMonth First day of the Month we search for
     */
    private void setCalendarForMonth(Date firstDayOfNewMonth) {
        Query qTransactionsMonth = Transaction.getQueryTransactions("month", firstDayOfNewMonth, bankAccount).orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Transaction> options = new FirestoreRecyclerOptions.Builder<Transaction>().setQuery(qTransactionsMonth, Transaction.class).setLifecycleOwner(parentFragment.getParentFragment()).build();
        binding.calendarExplicitIbanFragment.recyclerViewCalendarExp.setAdapter(new TransactionAdapter(options, parentFragment, false));

        qTransactionsMonth.get().addOnSuccessListener(ts -> {
            List<Transaction> transactions = ts.toObjects(Transaction.class);
            binding.compactcalendarView.removeAllEvents();
            binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.removeAllEvents();
            int future = 0, balance = 0;
            for (Transaction t : transactions) {
                if (t.isFuture()) future += t.getValue();
                else balance += t.getValue();
                binding.compactcalendarView.addEvent(t.getTransactionEvent());
                binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.addEvent(t.getTransactionEvent());
            }
            binding.calendarExplicitIbanFragment.textBalanceCalendarExp.setText(df.format(balance));
            binding.calendarExplicitIbanFragment.textToComeCalendarExp.setText(df.format(future));

            parentFragment.requireView().findViewById(R.id.custom_imageProgressBar).setVisibility(View.GONE);
        });

        binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.setCurrentDate(firstDayOfNewMonth == null ? binding.compactcalendarView.getFirstDayOfCurrentMonth() : firstDayOfNewMonth);
        binding.compactcalendarView.setCurrentDate(firstDayOfNewMonth == null ? binding.compactcalendarView.getFirstDayOfCurrentMonth() : firstDayOfNewMonth);
    }
}