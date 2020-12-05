package com.omada.junction.utils.transform;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TransformUtilities {

    public static float DP_TO_PX(Context context, float dp_value){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp_value, context.getResources().getDisplayMetrics());
    }

    public static String convertTimestampToHHMM(Timestamp timestamp){
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp.toDate());
        String hh = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String mm = String.valueOf(cal.get(Calendar.MINUTE));

        if(hh.length()==1) hh = "0" + hh;
        if(mm.length()==1) mm = "0" + mm;

        return hh + ":" + mm;
    }

    public static String convertTimestampToDDMM(Timestamp timestamp){
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp.toDate());
        return cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1);
    }

    public static String convertTimestampToDDMMYYYY(Timestamp timestamp){
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp.toDate());
        return cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + (cal.get(Calendar.YEAR));
    }

    public static Date convertDDMMYYYYtoDate(String formattedDate, String separator){

        Date date = null;
        try {
            date = new SimpleDateFormat("dd"+separator+"MM"+separator+"yyyy", Locale.US).parse(formattedDate);
        }catch (ParseException e){
            Log.e("PARSE", "parse exception");
        }
        return date;
    }

    public static String convertMillisecondsToDDMMYYYY(long millis, String separator){

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
}
