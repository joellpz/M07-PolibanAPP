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
import m07.joellpz.poliban.databinding.ActivityMainBinding;
import m07.joellpz.poliban.databinding.FragmentIbanMainBinding;
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

    private FragmentIbanMainBinding binding;
    private HomeFragment home;
    private BankAccount bankAccount;

    TransactionsAdapter mainAdapter, explicitAdapter, explicitFutureAdapter;

    float totalBalanceMonth, totalComeMonth;
    List<Transaction> transactionsPerMonth = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("#.##");

    List<Transaction> transactionsToCards = new ArrayList<>();


    public IbanMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        df.setRoundingMode(RoundingMode.CEILING);

        //TODO PONER MARCADORES DE COLORES EN EL MAPA

        //Bank info Introduce
        setMainInfo();


        mainAdapter = new TransactionsAdapter(bankAccount.getTransactionList());
        binding.recyclerView.setAdapter(mainAdapter);

        transactionsPerMonth = bankAccount.findTransactionPerMonth(new Date(),2);


        explicitAdapter = new TransactionsAdapter(transactionsPerMonth);
        binding.calendarExplicitIbanFragment.recyclerViewCalendarExp.setAdapter(explicitAdapter);

        explicitFutureAdapter = new TransactionsAdapter(bankAccount.getFutureTransactions());
        binding.calendarExplicitIbanFragment.recyclerViewFutureCalendarExp.setAdapter(explicitFutureAdapter);

        transactionsPerMonth.forEach(transaction -> totalBalanceMonth += transaction.getValue());
        binding.calendarExplicitIbanFragment.textBalanceCalendarExp.setText(df.format(totalBalanceMonth));


        bankAccount.getFutureTransactions().forEach(transaction -> totalComeMonth += transaction.getValue());
        binding.calendarExplicitIbanFragment.textToComeCalendarExp.setText(df.format(totalComeMonth));


        //Sets Charts Info And Calendar Events
        setChartsInfo("");
        setCalendarViewAppearance(view);

        binding.fragmentTransactionCards.goBackBtnCards.setOnClickListener(l -> binding.fragmentTransactionCards.getRoot().setVisibility(View.INVISIBLE));
        binding.mapImageContainer.setOnClickListener(l -> Navigation.findNavController(view).navigate(R.id.mapsFragment));

        binding.bizumButton.setOnClickListener(l -> ActivityMainBinding.inflate(getLayoutInflater()).appBarMain.contentMain.bottomMainMenu.findViewById(R.id.payFragment).performClick());
        binding.creditButton.setOnClickListener(l -> ActivityMainBinding.inflate(getLayoutInflater()).appBarMain.contentMain.bottomMainMenu.findViewById(R.id.payFragment).performClick());

//        binding.deleteAcoountBtn.setOnClickListener(l -> {
//            Objects.requireNonNull(home).removeFragment();
//        });
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
        return (binding = FragmentIbanMainBinding.inflate(inflater, container, false)).getRoot();
    }

    private void setMainInfo() {
        binding.ibanNumber.setText(bankAccount.getIban());
        binding.moneyBankInfo.setText(bankAccount.getBalanceString());
        binding.ownerInfo.setText(bankAccount.getOwner());
        if (bankAccount.getCif() != null) {
            binding.cifInfo.setText(bankAccount.getCif());
            binding.investButton.setOnClickListener(l -> {
                AlertDialog ad = new AlertDialog.Builder(getContext()).create();
                ad.setMessage("Have not been implemented yet...");
                ad.setCancelable(true);
                ad.show();
            });
        } else {
            binding.cifInfo.setVisibility(View.INVISIBLE);
            binding.investButton.setVisibility(View.GONE);
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

    private void setChartsInfo(String time) {
        TextView revenueTimeLabel = getView().findViewById(R.id.revenueTimeLabel);
        TextView expenditureTimeLabel = getView().findViewById(R.id.expenditureTimeLabel);

        ArrayList<Entry> expenditureList = new ArrayList<>();
        ArrayList<Entry> revenueList = new ArrayList<>();
        List<Transaction> filteredTransactionList;
        int totalRevenue = 0, totalExpenditure = 0;

        setChartProperties(binding.chartExped);
        setChartProperties(binding.chartRevenue);

        if (time.equals("week")) {
            revenueTimeLabel.setText(R.string.lastWeek);
            expenditureTimeLabel.setText(R.string.lastWeek);
            filteredTransactionList = bankAccount.findTransactionPerWeek(new Date());
        } else {
            revenueTimeLabel.setText(R.string.lastMonth);
            expenditureTimeLabel.setText(R.string.lastMonth);
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

        getView().findViewById(R.id.chartRevenueLayout).setOnClickListener(l -> {
            if (time.equals("week")) setChartsInfo("month");
            else setChartsInfo("week");
        });

        getView().findViewById(R.id.chartExpenditureLayout).setOnClickListener(l -> {
            if (time.equals("week")) setChartsInfo("month");
            else setChartsInfo("week");
        });
    }

    private void setCalendarViewAppearance(@NonNull View view) {
        Date today = new Date();
        SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", new Locale("es", "ES"));

        binding.compactcalendarView.setUseThreeLetterAbbreviation(true);
        binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.setUseThreeLetterAbbreviation(true);

        binding.monthText.setText(dateFormatForMonth.format(binding.compactcalendarView.getFirstDayOfCurrentMonth()));
        binding.calendarExplicitIbanFragment.monthTextExplicit.setText(dateFormatForMonth.format(binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.getFirstDayOfCurrentMonth()));

        for (Transaction transaction : bankAccount.getTransactionList()) {
            binding.compactcalendarView.addEvent(transaction.getTransactionEvent());
            binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.addEvent(transaction.getTransactionEvent());
        }

        for (Transaction transaction : bankAccount.getFutureTransactions()) {
            binding.compactcalendarView.addEvent(transaction.getTransactionEvent());
            binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.addEvent(transaction.getTransactionEvent());
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
                    TransactionsCardAdapter adapter = new TransactionsCardAdapter(transactionsToCards);
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
                    binding.linearcalendarView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white, requireActivity().getTheme())));
                    binding.calendarExplicitIbanFragment.linearcalendarViewExplicit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white, requireActivity().getTheme())));
                }

                transactionsPerMonth = bankAccount.findTransactionPerMonth(firstDayOfNewMonth,2);
                explicitAdapter = new TransactionsAdapter(transactionsPerMonth);
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

        // events has size 2 with the 2 events inserted previously
//        List<Event> events = compactCalendar.getEvents(1676329200000L); // can also take a Date object
//        Log.d(TAG, "Events: " + events);

        // define a listener to receive callbacks when certain events happen.
        binding.compactcalendarView.setListener(compactCalendarViewListener);
        binding.calendarExplicitIbanFragment.compactcalendarViewExplicit.setListener(compactCalendarViewListener);

        binding.linearcalendarView.setOnClickListener(l -> binding.calendarExplicitIbanFragment.getRoot().setVisibility(View.VISIBLE));
        binding.calendarExplicitIbanFragment.goBackBtn.setOnClickListener(l -> binding.calendarExplicitIbanFragment.getRoot().setVisibility(View.INVISIBLE));
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
                        binding.fragmentTransactionCards.recyclerviewTransactionCards.setAdapter(adapter);
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