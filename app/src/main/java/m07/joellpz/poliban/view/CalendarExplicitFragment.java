package m07.joellpz.poliban.view;

import static android.content.ContentValues.TAG;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.model.BankAccount;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarExplicitFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarExplicitFragment extends Fragment {

    BankAccount bankAccount;

    public CalendarExplicitFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar_explicit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CompactCalendarView compactCalendar = view.findViewById(R.id.compactcalendar_viewExplicit);
        LinearLayout calendarLinearView = view.findViewById(R.id.linearcalendar_view);
        Date today = new Date();
        SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy");

        TextView monthText = view.findViewById(R.id.monthTextExplicit);
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
                if (firstDayOfNewMonth.after(today)) calendarLinearView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B1B1B1")));
                else calendarLinearView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                monthText.setText(dateFormatForMonth.format(firstDayOfNewMonth));
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });

    }
}