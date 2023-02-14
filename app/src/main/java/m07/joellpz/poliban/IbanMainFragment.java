package m07.joellpz.poliban;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.Chart;
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