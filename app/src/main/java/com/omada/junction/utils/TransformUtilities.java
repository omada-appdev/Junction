package com.omada.junction.utils;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class TransformUtilities {

    public static float DP_TO_PX(Context context, float dp_value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp_value, context.getResources().getDisplayMetrics());
    }

    public static String convertTimestampToHHMM(Timestamp timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp.toDate());
        String hh = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String mm = String.valueOf(cal.get(Calendar.MINUTE));

        if (hh.length() == 1) hh = "0" + hh;
        if (mm.length() == 1) mm = "0" + mm;

        return hh + ":" + mm;
    }

    public static String convertTimestampToDDMM(Timestamp timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp.toDate());
        return cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1);
    }

    public static String convertTimestampToDDMMYYYY(Timestamp timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp.toDate());
        return cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + (cal.get(Calendar.YEAR));
    }

    public static Date convertDDMMYYYYtoDate(String formattedDate, String separator) {

        Date date = null;
        try {
            date = new SimpleDateFormat("dd" + separator + "MM" + separator + "yyyy", Locale.US).parse(formattedDate);
        } catch (ParseException e) {
            Log.e("PARSE", "parse exception");
        }
        return date;
    }

    public static String convertMillisecondsToDDMMYYYY(long millis, String separator) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(millis);

        StringBuilder builder = new StringBuilder();

        builder.append(calendar.get(Calendar.DATE));
        builder.append(separator);
        builder.append(calendar.get(Calendar.MONTH) + 1); // The calendar month is zero indexed
        builder.append(separator);
        builder.append(calendar.get(Calendar.YEAR));

        return builder.toString();
    }

    public static ZonedDateTime convertUtcLocalDateTimeToSystemZone(LocalDateTime time) {
        return time.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault());
    }

    public static LocalDateTime convertSystemZoneLocalDateTimeToUtc(ZonedDateTime time) {
        return time.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
    }


    public static LocalDateTime convertTimestampToLocalDateTime(Timestamp timestamp) {
        return timestamp.toDate().toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    public static String convertUtcLocalDateTimeToHHMM(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = convertUtcLocalDateTimeToSystemZone(localDateTime);
        return convertSystemZoneDateTimeToHHMM(zonedDateTime);
    }

    public static String convertSystemZoneDateTimeToHHMM(ZonedDateTime zonedDateTime) {
        String res = zonedDateTime.getHour() + ":";
        res += String.valueOf(zonedDateTime.getMinute()).length() < 2 ? "0" : "";
        res += zonedDateTime.getMinute();
        return res;
    }

    public static String convertUtcLocalDateTimeToddDDMM(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = convertUtcLocalDateTimeToSystemZone(localDateTime);
        return convertSystemZoneDateTimeToddDDMM(zonedDateTime);
    }

    public static String convertSystemZoneDateTimeToddDDMM(ZonedDateTime zonedDateTime) {
        return String.format(Locale.ENGLISH, "%s %d %s",
                zonedDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                zonedDateTime.getDayOfMonth(),
                zonedDateTime.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
        );
    }

    public static Timestamp convertUtcLocalDateTimeToTimestamp(LocalDateTime localDateTime) {
        return new Timestamp(Date.from(localDateTime.atZone(ZoneId.of("UTC")).toInstant()));
    }

    public static String getUrlFromForm(Map<String, Map<String, Map<String, String>>> form) {
        if(form == null) {
            return null;
        } else if (form.equals("")) {
            return null;
        } else {
            String url;
            try {
                url = form.get("links").get("linkId").get("url");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            return url;
        }
    }

}
