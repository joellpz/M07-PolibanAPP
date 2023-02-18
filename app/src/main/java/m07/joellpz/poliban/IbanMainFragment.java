package m07.joellpz.poliban;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IbanMainFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class IbanMainFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    LineChart chartWins, chartLoses;
    private GoogleMap mMap;

    public IbanMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        //TODO PONER MARCADORES DE COLORES EN EL MAPA


        chartLoses = view.findViewById(R.id.chartExped);
        chartWins = view.findViewById(R.id.chartRevenue);
        setChartPropieties(chartLoses);
        setChartPropieties(chartWins);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 60f));
        entries.add(new Entry(1, 30f));
        entries.add(new Entry(2, 80f));
        entries.add(new Entry(3, 65f));
        entries.add(new Entry(4, 60f));
        entries.add(new Entry(6, 70f));
        LineDataSet expediture = new LineDataSet(entries, "Expediture");
        LineDataSet revenue = new LineDataSet(entries, "Revenue");
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
        //ArrayList<ILineDataSet> dataSets = new ArrayList<>();


        chartLoses.setData(new LineData(expediture));
        chartWins.setData(new LineData(revenue));

        CompactCalendarView compactCalendar = view.findViewById(R.id.compactcalendar_view);
        SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy");

        TextView monthText = view.findViewById(R.id.monthText);
        monthText.setText(dateFormatForMonth.format(compactCalendar.getFirstDayOfCurrentMonth()));

        compactCalendar.setUseThreeLetterAbbreviation(true);

        Event ev1 = new Event(Color.GREEN, 1676329200000L, "Some extra data that I want to store.");
        compactCalendar.addEvent(ev1);

        // Added event 2 GMT: Sun, 07 Jun 2015 19:10:51 GMT
        Event ev2 = new Event(Color.RED, 1676329200000L);
        compactCalendar.addEvent(ev2);

        // Query for events on Sun, 07 Jun 2015 GMT.
        // Time is not relevant when querying for events, since events are returned by day.
        // So you can pass in any arbitary DateTime and you will receive all events for that day.
        List<Event> events = compactCalendar.getEvents(1676329200000L); // can also take a Date object

        // events has size 2 with the 2 events inserted previously
        Log.d(TAG, "Events: " + events);


        // define a listener to receive callbacks when certain events happen.
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendar.getEvents(dateClicked);
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                monthText.setText(dateFormatForMonth.format(firstDayOfNewMonth));
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });

        /*RecyclerView Transactions*/

        ArrayList<TransactionsAdapter.TransactionItem> exampleList = new ArrayList<>();
        exampleList.add(new TransactionsAdapter.TransactionItem("Item 1"));
        exampleList.add(new TransactionsAdapter.TransactionItem("Item 2"));
        exampleList.add(new TransactionsAdapter.TransactionItem("Item 3"));
        exampleList.add(new TransactionsAdapter.TransactionItem("Item 4"));
        exampleList.add(new TransactionsAdapter.TransactionItem("Item 5"));
        RecyclerView mRecyclerView;
        TransactionsAdapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;


        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mAdapter = new TransactionsAdapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_iban_main, container, false);
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

    //TODO Cambiar por FirestoreRecyclerAdapter el RecyclerView
    static class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {

        private ArrayList<TransactionItem> mExampleList;

        public TransactionsAdapter(ArrayList<TransactionItem> exampleList) {
            mExampleList = exampleList;
        }

        @NonNull
        @Override
        public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_money_transit, parent, false);
            return new TransactionViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
            TransactionItem currentItem = mExampleList.get(position);
        }

        @Override
        public int getItemCount() {
            return mExampleList.size();
        }

        class TransactionViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;

            public TransactionViewHolder(View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.subject_transaction);
            }
        }

        static class TransactionItem {

            private String mText;

            public TransactionItem(String text) {
                mText = text;
            }

            public String getText() {
                return mText;
            }
        }
    }


}