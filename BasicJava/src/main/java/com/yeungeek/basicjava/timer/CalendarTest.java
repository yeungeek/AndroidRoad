package com.yeungeek.basicjava.timer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarTest {
    public static void main(String[] args) {
        String start = "00:00:00";
        String end = "18:00:00";

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        try {
            System.out.println(belongCalender(new Date(), formatter.parse(start), formatter.parse(end)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static boolean belongCalender(final Date nowTime, final Date beginTime, final Date endTime) {
        Calendar date = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        date.setTime(nowTime);

        Calendar temp = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        temp.setTime(beginTime);

        Calendar begin = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        begin.setTime(nowTime);
        begin.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
        begin.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
        begin.set(Calendar.SECOND, temp.get(Calendar.SECOND));

        temp.setTime(endTime);

        Calendar end = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        end.setTime(nowTime);
        end.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
        end.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
        end.set(Calendar.SECOND, temp.get(Calendar.SECOND));

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
}
