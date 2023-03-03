package m07.joellpz.poliban.view;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.databinding.FragmentHomeBinding;
import m07.joellpz.poliban.databinding.FragmentIbanMainBinding;
import m07.joellpz.poliban.databinding.FragmentPayBinding;
import m07.joellpz.poliban.databinding.ViewholderTransactionBinding;
import m07.joellpz.poliban.databinding.ViewholderTransactionCardBinding;
import m07.joellpz.poliban.main.HomeFragment;
import m07.joellpz.poliban.model.AppViewModel;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.model.Transaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IbanMainFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class IbanMainFragment extends Fragment {

    TextView ibanInfoNumCard, moneyBankInfo, ownerInfo, cifInfo;
    TextView expenditurePrice, revenuePrice;
    LineChart chartWins, chartLoses;

    Button investButton;
    BankAccount bankAccount;

    CompactCalendarView compactCalendar, compactCalendarExplicit;
    RecyclerView recyclerView, recyclerViewExplicit, recyclerViewExplicitFuture, recyclerTransactionsCards;
    TransactionsAdapter mainAdapter, explicitAdapter, explicitFutureAdapter;

    TextView textBalanceCalendarExp, textToComeCalendarExp;
    float totalBalanceMonth, totalComeMonth;
    List<Transaction> transactionsPerMonth = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("#.##");

    List<Transaction> transactionsToCards = new ArrayList<>();

    HomeFragment home;


    public IbanMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        df.setRoundingMode(RoundingMode.CEILING);

        //TODO PONER MARCADORES DE COLORES EN EL MAPA

        //Main Info
        ibanInfoNumCard = view.findViewById(R.id.currentBalance);
        moneyBankInfo = view.findViewById(R.id.moneyBankInfo);
        ownerInfo = view.findViewById(R.id.ownerInfo);
        cifInfo = view.findViewById(R.id.cifInfo);
        investButton = view.findViewById(R.id.investButton);
        //Bank info Introduce
        setMainInfo();


        /*RecyclerView Transactions*/
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerViewExplicit = view.findViewById(R.id.recycler_view_calendarExp);
        recyclerViewExplicitFuture = view.findViewById(R.id.recycler_view_future_calendarExp);
        recyclerTransactionsCards = view.findViewById(R.id.recyclerview_transactionCards);


        mainAdapter = new TransactionsAdapter(bankAccount.getTransactionList());
        recyclerView.setAdapter(mainAdapter);

        transactionsPerMonth = bankAccount.findTransactionPerMonth(new Date());


        explicitAdapter = new TransactionsAdapter(transactionsPerMonth);
        recyclerViewExplicit.setAdapter(explicitAdapter);

        explicitFutureAdapter = new TransactionsAdapter(bankAccount.getFutureTransactions());
        recyclerViewExplicitFuture.setAdapter(explicitFutureAdapter);

        textBalanceCalendarExp = view.findViewById(R.id.textBalanceCalendarExp);
        textToComeCalendarExp = view.findViewById(R.id.textToComeCalendarExp);

        transactionsPerMonth.forEach(transaction -> totalBalanceMonth += transaction.getValue());
        textBalanceCalendarExp.setText(df.format(totalBalanceMonth));


        bankAccount.getFutureTransactions().forEach(transaction -> totalComeMonth += transaction.getValue());
        textToComeCalendarExp.setText(df.format(totalComeMonth));


        //Charts & Calendar
        expenditurePrice = view.findViewById(R.id.expenditurePrice);
        revenuePrice = view.findViewById(R.id.revenuePrice);
        chartLoses = view.findViewById(R.id.chartExped);
        chartWins = view.findViewById(R.id.chartRevenue);

        compactCalendar = view.findViewById(R.id.compactcalendar_view);
        compactCalendarExplicit = view.findViewById(R.id.compactcalendar_viewExplicit);


        //Sets Charts Info And Calendar Events
        setChartsInfo("");
        setCalendarViewAppearance(view);

        view.findViewById(R.id.goBackBtnCards).setOnClickListener(l -> view.findViewById(R.id.fragmentTransactionCards).setVisibility(View.INVISIBLE));
        view.findViewById(R.id.mapImageContainer).setOnClickListener(l -> Navigation.findNavController(view).navigate(R.id.mapsFragment));

        view.findViewById(R.id.bizumButton).setOnClickListener(l -> getActivity().findViewById(R.id.bottomMainMenu).findViewById(R.id.payFragment).performClick());
        view.findViewById(R.id.creditButton).setOnClickListener(l -> getActivity().findViewById(R.id.bottomMainMenu).findViewById(R.id.payFragment).performClick());

        view.findViewById(R.id.deleteAcoountBtn).setOnClickListener(l -> {
            HomeFragment home = (HomeFragment) getParentFragment();
            Objects.requireNonNull(home).removeFragment();
        });
    }


    private void setCalendarViewAppearance(@NonNull View view) {
        ScrollView calendarExplicitIbanFragment = view.findViewById(R.id.calendarExplicitIbanFragment);
        Date today = new Date();
        SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", new Locale("es", "ES"));

        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendarExplicit.setUseThreeLetterAbbreviation(true);

        LinearLayout calendarLinearView = view.findViewById(R.id.linearcalendar_view);
        LinearLayout calendarLinearViewExplicit = view.findViewById(R.id.linearcalendar_viewExplicit);

        TextView monthText = view.findViewById(R.id.monthText);
        TextView monthTextExplicit = view.findViewById(R.id.monthTextExplicit);
        monthText.setText(dateFormatForMonth.format(compactCalendar.getFirstDayOfCurrentMonth()));
        monthTextExplicit.setText(dateFormatForMonth.format(compactCalendarExplicit.getFirstDayOfCurrentMonth()));

        for (Transaction transaction : bankAccount.getTransactionList()) {
            compactCalendar.addEvent(transaction.getTransactionEvent());
            compactCalendarExplicit.addEvent(transaction.getTransactionEvent());
        }

        for (Transaction transaction : bankAccount.getFutureTransactions()) {
            compactCalendar.addEvent(transaction.getTransactionEvent());
            compactCalendarExplicit.addEvent(transaction.getTransactionEvent());
        }
        CompactCalendarView.CompactCalendarViewListener compactCalendarViewListener = new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendar.getEvents(dateClicked);
                if (events.size() > 0) {
                    transactionsToCards.clear();
                    for (Event event : events) {
                        transactionsToCards.add((Transaction) event.getData());
                    }
                    TransactionsCardAdapter adapter = new TransactionsCardAdapter(transactionsToCards);
                    recyclerTransactionsCards.setAdapter(adapter);
                    view.findViewById(R.id.fragmentTransactionCards).setVisibility(View.VISIBLE);
                    Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                if (firstDayOfNewMonth.after(today)) {
                    calendarLinearView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B1B1B1")));
                    calendarLinearViewExplicit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B1B1B1")));
                } else {
                    calendarLinearView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white, requireActivity().getTheme())));
                    calendarLinearViewExplicit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white, requireActivity().getTheme())));
                }

                transactionsPerMonth = bankAccount.findTransactionPerMonth(firstDayOfNewMonth);
                explicitAdapter = new TransactionsAdapter(transactionsPerMonth);
                recyclerViewExplicit.setAdapter(explicitAdapter);

                totalComeMonth = 0;
                totalBalanceMonth = 0;
                transactionsPerMonth.forEach(transaction -> totalBalanceMonth += transaction.getValue());
                textBalanceCalendarExp.setText(df.format(totalBalanceMonth));

                compactCalendarExplicit.setCurrentDate(firstDayOfNewMonth);
                compactCalendar.setCurrentDate(firstDayOfNewMonth);
                monthText.setText(dateFormatForMonth.format(firstDayOfNewMonth));
                monthTextExplicit.setText(dateFormatForMonth.format(firstDayOfNewMonth));
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
            }
        };

        // events has size 2 with the 2 events inserted previously
//        List<Event> events = compactCalendar.getEvents(1676329200000L); // can also take a Date object
//        Log.d(TAG, "Events: " + events);

        // define a listener to receive callbacks when certain events happen.
        compactCalendar.setListener(compactCalendarViewListener);
        compactCalendarExplicit.setListener(compactCalendarViewListener);

        calendarLinearView.setOnClickListener(l -> calendarExplicitIbanFragment.setVisibility(View.VISIBLE));
        view.findViewById(R.id.goBackBtn).setOnClickListener(l -> calendarExplicitIbanFragment.setVisibility(View.INVISIBLE));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        home = (HomeFragment) getParentFragment();
        AppViewModel appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        this.bankAccount = appViewModel.getBankAccountElement(home.getFragmentPosition());
        return inflater.inflate(R.layout.fragment_iban_main, container, false);
    }

    private void setChartsInfo(String time) {
        TextView revenueTimeLabel = getView().findViewById(R.id.revenueTimeLabel);
        TextView expenditureTimeLabel = getView().findViewById(R.id.expenditureTimeLabel);

        ArrayList<Entry> expenditureList = new ArrayList<>();
        ArrayList<Entry> revenueList = new ArrayList<>();
        List<Transaction> filteredTransactionList;
        int totalRevenue = 0, totalExpenditure = 0;

        setChartProperties(chartLoses);
        setChartProperties(chartWins);

        if (time.equals("week")) {
            revenueTimeLabel.setText(R.string.lastWeek);
            expenditureTimeLabel.setText(R.string.lastWeek);
            filteredTransactionList = bankAccount.findTransactionPerWeek(new Date());
        } else {
            revenueTimeLabel.setText(R.string.lastMonth);
            expenditureTimeLabel.setText(R.string.lastMonth);
            filteredTransactionList = bankAccount.findTransactionPerMonth(new Date());
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


        expenditurePrice.setText(df.format(totalExpenditure));
        revenuePrice.setText(df.format(totalRevenue));

        LineDataSet expenditure = new LineDataSet(expenditureList, "Expenditure");
        LineDataSet revenue = new LineDataSet(revenueList, "Revenue");
        revenue.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        expenditure.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        revenue.setLineWidth(1.5f);
        expenditure.setLineWidth(1.5f);

        revenue.setDrawFilled(true);
        expenditure.setDrawFilled(true);

        revenue.setDrawCircles(false);
        expenditure.setDrawCircles(false);

        revenue.setDrawValues(false);
        expenditure.setDrawValues(false);

        expenditure.setFillColor(ContextCompat.getColor(chartLoses.getContext(), R.color.red_light));
        revenue.setFillColor(ContextCompat.getColor(chartWins.getContext(), R.color.green_light));
        expenditure.setColor(ContextCompat.getColor(chartLoses.getContext(), R.color.red_less));
        revenue.setColor(ContextCompat.getColor(chartWins.getContext(), R.color.green));

        chartLoses.setData(new LineData(expenditure));
        chartWins.setData(new LineData(revenue));

        getView().findViewById(R.id.chartRevenueLayout).setOnClickListener(l -> {
            if (time.equals("week")) setChartsInfo("month");
            else setChartsInfo("week");
        });

        getView().findViewById(R.id.chartExpenditureLayout).setOnClickListener(l -> {
            if (time.equals("week")) setChartsInfo("month");
            else setChartsInfo("week");
        });
    }

    private void setMainInfo() {
        ibanInfoNumCard.setText(bankAccount.getIban());
        moneyBankInfo.setText(bankAccount.getBalanceString());
        ownerInfo.setText(bankAccount.getOwner());
        if (bankAccount.getCif() != null) {
            cifInfo.setText(bankAccount.getCif());
            investButton.setOnClickListener(l -> {
                AlertDialog ad = new AlertDialog.Builder(getContext()).create();
                ad.setMessage("Have not been implemented yet...");
                ad.setCancelable(true);
                ad.show();
            });
        } else {
            cifInfo.setVisibility(View.INVISIBLE);
            investButton.setVisibility(View.GONE);
        }
        ImageView image;
        if (bankAccount.getIban().split(" ")[1].equals("2100")) {
            getView().findViewById(R.id.bank_data).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.lacaixa, requireActivity().getTheme())));
            image = getView().findViewById(R.id.bankEntityLogo);
            image.setImageResource(R.drawable.logo_lacaixa);
        } else if (bankAccount.getIban().split(" ")[1].equals("0057")) {
            getView().findViewById(R.id.bank_data).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bbva, requireActivity().getTheme())));
            image = getView().findViewById(R.id.bankEntityLogo);
            image.setImageResource(R.drawable.logo_bbva);
        } else if (bankAccount.getIban().split(" ")[1].equals("0049")) {
            getView().findViewById(R.id.bank_data).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.santander, requireActivity().getTheme())));
            image = getView().findViewById(R.id.bankEntityLogo);
            image.setImageResource(R.drawable.logo_santander);
        }
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


    //TODO Cambiar por FirestoreRecyclerAdapter el RecyclerView
    class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {

        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
        private final List<Transaction> mExampleList;

        public TransactionsAdapter(List<Transaction> exampleList) {
            mExampleList = exampleList;
        }

        @NonNull
        @Override
        public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TransactionViewHolder(ViewholderTransactionBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
            Transaction currentItem = mExampleList.get(position);

            if (currentItem.getValue() > 0)
                holder.binding.imageTransaction.setImageResource(R.drawable.money_in);
            else holder.binding.imageTransaction.setImageResource(R.drawable.money_out);

            holder.binding.subjectTransaction.setText(currentItem.getSubject());
            holder.binding.dateTransaction.setText(dateFormat.format(currentItem.getDate()));
            holder.binding.priceTransaction.setText(currentItem.getValueString());

            holder.binding.mainTransactionLayout.setOnClickListener(l -> {
                        transactionsToCards.clear();
                        transactionsToCards.add(currentItem);
                        TransactionsCardAdapter adapter = new TransactionsCardAdapter(transactionsToCards);
                        recyclerTransactionsCards.setAdapter(adapter);
                        getView().findViewById(R.id.fragmentTransactionCards).setVisibility(View.VISIBLE);
                    }
            );
        }

        @Override
        public int getItemCount() {
            return mExampleList.size();
        }

        class TransactionViewHolder extends RecyclerView.ViewHolder {
            private final ViewholderTransactionBinding binding;

            public TransactionViewHolder(ViewholderTransactionBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    class TransactionsCardAdapter extends RecyclerView.Adapter<TransactionsCardAdapter.TransactionCardViewHolder> {

        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
        private final List<Transaction> mExampleList;

        public TransactionsCardAdapter(List<Transaction> exampleList) {
            mExampleList = exampleList;
        }

        @NonNull
        @Override
        public TransactionCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TransactionCardViewHolder(ViewholderTransactionCardBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TransactionCardViewHolder holder, int position) {
            Transaction currentItem = mExampleList.get(position);


            if (currentItem.getValue() > 0) {
                holder.binding.fromText.setText(R.string.fromText);
                holder.binding.euroBankInfo.setTextColor(getResources().getColor(R.color.green, requireActivity().getTheme()));
                holder.binding.priceTransaction.setTextColor(getResources().getColor(R.color.green, requireActivity().getTheme()));
            } else {
                holder.binding.fromText.setText(R.string.toText);
                holder.binding.euroBankInfo.setTextColor(getResources().getColor(R.color.red_light, requireActivity().getTheme()));
                holder.binding.priceTransaction.setTextColor(getResources().getColor(R.color.red_light, requireActivity().getTheme()));
            }

            holder.binding.fromInfo.setText(currentItem.getFrom());

            if (currentItem.isFuture())
                holder.binding.subjectTransaction.setText(String.format("%s%s", getString(R.string.futureText), currentItem.getSubject()));
            else holder.binding.subjectTransaction.setText(currentItem.getSubject());


            holder.binding.dateTransaction.setText(dateFormat.format(currentItem.getDate()));
            holder.binding.priceTransaction.setText(currentItem.getValueString());
            holder.binding.valoracion.setRating(currentItem.getOpinion());

            holder.binding.valoracion.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
                if (fromUser) {
                    currentItem.setOpinion(rating);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mExampleList.size();
        }

        class TransactionCardViewHolder extends RecyclerView.ViewHolder {
            private final ViewholderTransactionCardBinding binding;

            public TransactionCardViewHolder(ViewholderTransactionCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

}