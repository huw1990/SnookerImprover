package com.snookerup.ui.common;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * Allows the user to select a date, within a fragment dialog.
 *
 * @author Huwdunnit
 */
public class TimePickerFragment extends DialogFragment {

    /** A listener, to handle the time selected by the user. */
    TimePickerDialog.OnTimeSetListener listener;

    /**
     * Constructor.
     * @param listener A listener, to receive the selected time
     */
    public TimePickerFragment(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), listener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
}
