package com.snookerup.data;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Converters for types used in our model objects that are not supported by Room/SQLite.
 *
 * @author Huwdunnit
 */
public class Converters {

    private static final String STRING_LIST_DELIMITER = "\n\n";

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static List<String> fromString(String value) {
        return Arrays.asList(value.split(STRING_LIST_DELIMITER));
    }

    @TypeConverter
    public static String fromList(List<String> list) {
        return TextUtils.join(STRING_LIST_DELIMITER, list);
    }
}
