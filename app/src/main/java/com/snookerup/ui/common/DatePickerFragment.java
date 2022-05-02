package com.snookerup.ui.common;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * Allows the user to select a date, within a fragment dialog.
 *
 * @author Huwdunnit
 */
public class DatePickerFragment extends DialogFragment {

    /** A listener, to handle the date selected by the user. */
    DatePickerDialog.OnDateSetListener listener;

    /**
     * Constructor.
     * @param listener A listener, to receive the selected date
     */
    public DatePickerFragment(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }
}