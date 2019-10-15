package Tools;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/*
Kan kanskje også inkludere Dato-formatteringa?
 */
public class TimeUtility {

    private static Calendar calendar;

    public static int getYear(Timestamp timestamp) {
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());

        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(Timestamp timestamp) {
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());

        return calendar.get(Calendar.MONTH);
    }

    public static int getDay(Timestamp timestamp) {
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static LocalDate getDate(Timestamp timestamp) {
        int year = getYear(timestamp);
        int month = getMonth(timestamp);
        int day = getDay(timestamp);

        return LocalDate.of(year, month, day);
    }

    public static int getDay(String timestamp) throws ParseException {
        return getDay(getTimestampFromString(timestamp));
    }

    public static String format(String input, String format) throws ParseException {
        input = input.replace("T", " ");
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date output = sdf.parse(input);
        return output.toString();
    }

    public static Timestamp getTimestampFromString(String input) throws ParseException {
        input = input.replace("T", " ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date output = sdf.parse(input);
        return new Timestamp(output.getTime());
    }

    public static String getDateAsStringFromString(String input) throws ParseException {
        return getDate(getTimestampFromString(input)).toString();
    }
}
