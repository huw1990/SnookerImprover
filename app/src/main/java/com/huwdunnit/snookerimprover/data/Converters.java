package com.huwdunnit.snookerimprover.data;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Converters for types used in our model objects that are not supported by Room/SQLite.
 *
 * @author Huwdunnit
 */
public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
