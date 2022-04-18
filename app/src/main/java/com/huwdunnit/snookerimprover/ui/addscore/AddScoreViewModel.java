package com.huwdunnit.snookerimprover.ui.addscore;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huwdunnit.snookerimprover.model.Routine;
import com.huwdunnit.snookerimprover.ui.common.ChangeableRoutineViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddScoreViewModel extends ViewModel implements ChangeableRoutineViewModel,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private final MutableLiveData<String> date;

    private final MutableLiveData<String> time;

    private String dateFormatString;

    private String todayLabel;

    public AddScoreViewModel() {
        super();
        date = new MutableLiveData<>();
        time = new MutableLiveData<>();

        //Set the time to the current time
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        setTime(hour, minute);
    }

    public LiveData<String> getDate() {
        return date;
    }

    public LiveData<String> getTime() {
        return time;
    }

    public Date getFullDateAndTime() {
        final Calendar c = Calendar.getInstance();
        if (date.getValue().equals(todayLabel)) {
            //Do nothing, calendar instance will be set to today's date
        } else {
            //Set the selected date in the calendar
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
            try {
                Date dateObj = dateFormat.parse(date.getValue());
                c.setTime(dateObj);
            } catch (ParseException | NumberFormatException ex) {
                ex.printStackTrace();
            }
        }

        //Split the time String, then set the correct hours and minutes on the calendar
        String[] hoursAndMinutes = time.getValue().split(":");
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hoursAndMinutes[0]));
        c.set(Calendar.MINUTE, Integer.parseInt(hoursAndMinutes[1]));

        //Convert the calendar to a date
        return c.getTime();
    }

    @Override
    public void setRoutine(Routine routine, Context context) {
        //Do nothing - changing routine shouldn't change any values
    }

    void setDateFormat(String dateFormatString) {
        this.dateFormatString = dateFormatString;
    }

    void setTodayLabel(String todayLabel) {
        this.todayLabel = todayLabel;
        this.date.setValue(todayLabel);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        final Calendar c = Calendar.getInstance();
        int yearNow = c.get(Calendar.YEAR);
        int monthNow = c.get(Calendar.MONTH);
        int dayNow = c.get(Calendar.DAY_OF_MONTH);
        if (year == yearNow && month == monthNow && day == dayNow) {
            date.setValue(todayLabel);
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            dateFormat.setCalendar(c);
            date.setValue(dateFormat.format(c.getTime()));
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        setTime(hourOfDay, minute);
    }

    private void setTime(int hourOfDay, int minute) {
        if (minute < 10) {
            time.setValue(hourOfDay + ":0" + minute);
        } else {
            time.setValue(hourOfDay + ":" + minute);
        }
    }
}