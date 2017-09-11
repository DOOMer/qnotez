package org.doomer.qnotez.db;

import android.arch.persistence.room.TypeConverter;
import java.util.Date;

public class DateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? 0 : date.getTime();
    }
}
