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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IbanMainFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class IbanMainFragment extends Fragment {

    LineChart chartWins, chartLoses;

    public IbanMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chartLoses = (LineChart) view.findViewById(R.id.chartExped);
        chartWins = (LineChart) view.findViewById(R.id.chartRevenue);
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

        CompactCalendarView compactCalendar = (CompactCalendarView) view.findViewById(R.id.compactcalendar_view);
        SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy");

        TextView monthText = (TextView) view.findViewById(R.id.monthText);
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
}