package br.delonborges.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataUtils {

    public static String getDataAndAddDays(Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        return format.format(calendar.getTime());
    }
}
