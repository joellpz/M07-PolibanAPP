package m07.joellpz.poliban;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

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

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chartLoses = (LineChart) view.findViewById(R.id.chartExped);
        chartWins = (LineChart) view.findViewById(R.id.chartRevenue);
        chartLoses.setDragEnabled(false);
        chartLoses.setScaleEnabled(false);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0,60f));
        entries.add(new Entry(1,30f));
        entries.add(new Entry(2,80f));
        entries.add(new Entry(3,65f));
        entries.add(new Entry(4,60f));
        entries.add(new Entry(6,70f));
        LineDataSet set1 = new LineDataSet(entries,"Data Set 1");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        chartLoses.setData(data);
        chartWins.setData(data);

        CalendarView simpleCalendarView = (CalendarView) view.findViewById(R.id.calendarView); // get the reference of CalendarView
        simpleCalendarView.setDateTextAppearance(R.color.orange_poliban);
        simpleCalendarView.setFocusedMonthDateColor(getContext().getColor(R.color.orange_poliban)); // set the red color for the dates of  focused month
        simpleCalendarView.setUnfocusedMonthDateColor(getContext().getColor(R.color.orange_poliban)); // set the yellow color for the dates of an unfocused month
        simpleCalendarView.setSelectedWeekBackgroundColor(getContext().getColor(R.color.orange_poliban)); // red color for the selected week's background
        simpleCalendarView.setWeekSeparatorLineColor(getContext().getColor(R.color.orange_poliban));; // green color for the week separator line
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
}