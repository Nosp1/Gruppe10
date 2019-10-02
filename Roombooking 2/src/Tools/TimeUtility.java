package Tools;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
Kan kanskje også inkludere Dato-formatteringa?
 */
public class TimeUtility {

    private static Calendar calendar;

    public static int getDay(Timestamp timestamp) {
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDay(String timestamp) throws ParseException {
        return getDay(getTimestampFromString(timestamp));
    }

    public static Timestamp getTimestampFromString(String input) throws ParseException {
        input = input.replace("T", " ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date output = sdf.parse(input);
        return new Timestamp(output.getTime());
    }
}
