package m07.joellpz.poliban.view;

import static android.content.ContentValues.TAG;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.databinding.ViewholderTransactionBinding;
import m07.joellpz.poliban.databinding.ViewholderTransactionCardBinding;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.model.Transaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IbanMainFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class IbanMainFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    TextView ibanInfoNumCard, moneyBankInfo, ownerInfo, cifInfo;
    TextView expediturePrice, revenuePrice;
    LineChart chartWins, chartLoses;

    Button investButton;
    BankAccount bankAccount;

    CompactCalendarView compactCalendar, compactCalendarExplicit;
    RecyclerView recyclerView, recyclerViewExplicit, recyclerViewExplicitFuture, recyclerTransactionsCards;
    TransactionsAdapter mainAdapter, explicitAdapter, explicitFutureAdapter;

    TextView textBalanceCalendarExp, textFutureCalendarExp;
    float totalBalanceMonth, totalComeMonth;
    List<Transaction> transactionsPerMonth = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("#.##");

    List<Transaction> transactionsToCards = new ArrayList<>();

    private GoogleMap mMap;


    public IbanMainFragment() {
        // Required empty public constructor
    }

    public IbanMainFragment(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        df.setRoundingMode(RoundingMode.CEILING);
        //Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        //TODO PONER MARCADORES DE COLORES EN EL MAPA

        //Main Info
        ibanInfoNumCard = view.findViewById(R.id.ibanInfoNumCard);
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

        textBalanceCalendarExp = view.findViewById(R.id.textBalanceCalendarExp);
        textFutureCalendarExp = view.findViewById(R.id.textFutureCalendarExp);

        transactionsPerMonth.forEach(transaction -> totalBalanceMonth += transaction.getValue());
        textBalanceCalendarExp.setText(df.format(totalBalanceMonth));


        //Charts & Calendar
        expediturePrice = view.findViewById(R.id.expediturePrice);
        revenuePrice = view.findViewById(R.id.revenuePrice);
        chartLoses = view.findViewById(R.id.chartExped);
        chartWins = view.findViewById(R.id.chartRevenue);

        compactCalendar = view.findViewById(R.id.compactcalendar_view);
        compactCalendarExplicit = view.findViewById(R.id.compactcalendar_viewExplicit);


        //Sets Charts Info And Calendar Events
        setChartsInfo("");
        setCalendarViewAppearence(view);

        view.findViewById(R.id.goBackBtnCards).setOnClickListener(l -> view.findViewById(R.id.fragmentTransactionCards).setVisibility(View.INVISIBLE));
        view.findViewById(R.id.mapImageContainer).setOnClickListener(l -> Navigation.findNavController(view).navigate(R.id.mapsFragment));

        view.findViewById(R.id.bizumButton).setOnClickListener(l -> Navigation.findNavController(view));
        view.findViewById(R.id.creditButton).setOnClickListener(l -> Navigation.findNavController(view).navigate(R.id.payFragment));
    }


    private void setCalendarViewAppearence(@NonNull View view) {
        ScrollView calendarExplicitIbanFragment = view.findViewById(R.id.calendarExplicitIbanFragment);
        Date today = new Date();
        SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy");

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
                    calendarLinearView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    calendarLinearViewExplicit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

        LatLng badalona = new LatLng(41.455775193431435, 2.201906692392249);
        mMap.addMarker(new MarkerOptions().position(badalona).title("Badalona"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(badalona));
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        //txtLatitud.setText(String.valueOf(latLng.latitude));
        //txtLongitud.setText(String.valueOf(latLng.longitude));

        mMap.clear();
        LatLng mexico = new LatLng(latLng.latitude, latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(mexico).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mexico));
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
//        txtLatitud.setText(String.valueOf(latLng.latitude));
//        txtLongitud.setText(String.valueOf(latLng.longitude));

        mMap.clear();
        LatLng mexico = new LatLng(latLng.latitude, latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(mexico).title("AÑA"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mexico));
    }

    private void setChartsInfo(String time) {
        TextView revenueTimeLabel = getView().findViewById(R.id.revenueTimeLabel);
        TextView expeditureTimeLabel = getView().findViewById(R.id.expeditureTimeLabel);

        ArrayList<Entry> expeditureList = new ArrayList<>();
        ArrayList<Entry> revenueList = new ArrayList<>();
        List<Transaction> filteredTransactionList;
        int totalRevenue = 0, totalExpediture = 0;

        setChartPropieties(chartLoses);
        setChartPropieties(chartWins);

        if (time.equals("week")) {
            revenueTimeLabel.setText("Last Week");
            expeditureTimeLabel.setText("Last Week");
            filteredTransactionList = bankAccount.findTransactionPerWeek(new Date());
        } else {
            revenueTimeLabel.setText("Last Month");
            expeditureTimeLabel.setText("Last Month");
            filteredTransactionList = bankAccount.findTransactionPerMonth(new Date());
        }

        for (Transaction transaction : filteredTransactionList) {
            if (transaction.getValue() > 0) {
                revenueList.add(new Entry(revenueList.size() + 1, transaction.getValue()));
                totalRevenue += transaction.getValue();
            } else {
                expeditureList.add(new Entry(expeditureList.size() + 1, transaction.getValue() * (-1)));
                totalExpediture += transaction.getValue();
            }
        }


        expediturePrice.setText(df.format(totalExpediture));
        revenuePrice.setText(df.format(totalRevenue));

        LineDataSet expediture = new LineDataSet(expeditureList, "Expediture");
        LineDataSet revenue = new LineDataSet(revenueList, "Revenue");
        revenue.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        expediture.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        revenue.setLineWidth(1.5f);
        expediture.setLineWidth(1.5f);

        revenue.setDrawFilled(true);
        expediture.setDrawFilled(true);

        revenue.setDrawCircles(false);
        expediture.setDrawCircles(false);

        revenue.setDrawValues(false);
        expediture.setDrawValues(false);

        expediture.setFillColor(ContextCompat.getColor(chartLoses.getContext(), R.color.red_light));
        revenue.setFillColor(ContextCompat.getColor(chartWins.getContext(), R.color.green_light));
        expediture.setColor(ContextCompat.getColor(chartLoses.getContext(), R.color.red_less));
        revenue.setColor(ContextCompat.getColor(chartWins.getContext(), R.color.green));

        chartLoses.setData(new LineData(expediture));
        chartWins.setData(new LineData(revenue));

        getView().findViewById(R.id.chartRevenueLayout).setOnClickListener(l -> {
            if (time.equals("week")) setChartsInfo("month");
            else setChartsInfo("week");
        });

        getView().findViewById(R.id.chartExpeditureLayout).setOnClickListener(l -> {
            if (time.equals("week")) setChartsInfo("month");
            else setChartsInfo("week");
        });
    }

    private void setMainInfo() {
        ibanInfoNumCard.setText(bankAccount.getIban());
        moneyBankInfo.setText(bankAccount.getBalanceString());
        ownerInfo.setText(bankAccount.getOwner());
        if (bankAccount.getCif() != null) cifInfo.setText(bankAccount.getCif());
        else {
            cifInfo.setVisibility(View.INVISIBLE);
            investButton.setVisibility(View.GONE);
        }
    }

    private void setChartPropieties(LineChart chart) {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.bankAccount = (BankAccount) requireArguments().getSerializable("bank");
        return inflater.inflate(R.layout.fragment_iban_main, container, false);
    }


    //TODO Cambiar por FirestoreRecyclerAdapter el RecyclerView
    class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {

        private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        private List<Transaction> mExampleList;

        public TransactionsAdapter(List<Transaction> exampleList) {
            mExampleList = exampleList;
        }

        @NonNull
        @Override
        public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

        private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        private List<Transaction> mExampleList;

        public TransactionsCardAdapter(List<Transaction> exampleList) {
            mExampleList = exampleList;
        }

        @NonNull
        @Override
        public TransactionCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TransactionCardViewHolder(ViewholderTransactionCardBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TransactionCardViewHolder holder, int position) {
            Transaction currentItem = mExampleList.get(position);


            if (currentItem.getValue() > 0) {
                holder.binding.fromText.setText("From: ");
                holder.binding.euroBankInfo.setTextColor(getResources().getColor(R.color.green));
                holder.binding.priceTransaction.setTextColor(getResources().getColor(R.color.green));
            } else {
                holder.binding.fromText.setText("To: ");
                holder.binding.euroBankInfo.setTextColor(getResources().getColor(R.color.red_light));
                holder.binding.priceTransaction.setTextColor(getResources().getColor(R.color.red_light));
            }

            holder.binding.fromInfo.setText(currentItem.getFrom());
            holder.binding.subjectTransaction.setText(currentItem.getSubject());
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