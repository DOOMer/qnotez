package org.doomer.qnotez.db

import android.arch.persistence.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter  // Указать, что метод является конвертером
    fun toDate(timestamp: Long?) = timestamp?.let { Date(it) }

    @TypeConverter  // Указать, что метод является конвертером
    fun toTimestamp(date: Date?) = date?.time
}
