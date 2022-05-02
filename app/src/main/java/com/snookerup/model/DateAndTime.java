package com.snookerup.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Provides a two-way mapping between a standard Java Date object, and the individual user-friendly
 * fields for separate date and time.
 *
 * @author Huwdunnit
 */
public class DateAndTime {

    private final String date;

    private final String time;

    /**
     * Private constructor, to avoid instantiation. Use the provided builder instead
     * @param date User friendly date String, using the format provided in String resources
     * @param time User friendly time String, in format HH:MM
     */
    private DateAndTime(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    /**
     * Get a DateAndTime object from a standard Java Date object.
     * @param date The date to parse
     * @param dateFormat The format to display the date in
     * @return A DateAndTime object from the provided Date
     */
    public static DateAndTime fromDate(Date date, String dateFormat) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        SimpleDateFormat dateFormatObj = new SimpleDateFormat(dateFormat);
        String dateString = dateFormatObj.format(date);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        String timeString;
        if (minutes < 10) {
            timeString = hour + ":0" + minutes;
        } else {
            timeString = hour + ":" + minutes;
        }
        return new DateAndTimeBuilder().setDate(dateString).setTime(timeString).createDateAndTime();
    }

    /**
     * Combine the separate date and time Strings into a single Java Date object.
     * @param dateFormatString The format of the date
     * @return A standard Java Date object
     */
    public Date toDate(String dateFormatString) {
        final Calendar c = Calendar.getInstance();
        if (date == null) {
            //Do nothing, calendar instance will be set to today's date
        } else {
            //Set the selected date in the calendar
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
            try {
                Date dateObj = dateFormat.parse(date);
                c.setTime(dateObj);
            } catch (ParseException | NumberFormatException ex) {
                ex.printStackTrace();
            }
        }

        //Split the time String, then set the correct hours and minutes on the calendar
        String[] hoursAndMinutes = time.split(":");
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hoursAndMinutes[0]));
        c.set(Calendar.MINUTE, Integer.parseInt(hoursAndMinutes[1]));

        //Convert the calendar to a date
        return c.getTime();
    }
    /**
     * A Builder for creating DateAndTime objects.
     *
     * @author Huwdunnit
     */
    public static class DateAndTimeBuilder {
        private String date;
        private String time;

        public DateAndTime.DateAndTimeBuilder setDate(String date) {
            this.date = date;
            return this;
        }

        public DateAndTime.DateAndTimeBuilder setTime(String time) {
            this.time = time;
            return this;
        }

        public DateAndTime createDateAndTime() {
            return new DateAndTime(date, time);
        }
    }
}
