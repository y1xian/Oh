package com.yyxnb.http.cache;


import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Long date2Long(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public static Date long2Date(Long data) {
        return new Date(data);
    }
}