import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReminderManager {
    private Context context;

    public ReminderManager(Context context) {
        this.context = context;
    }

    public void setAlarm(Reminder reminder) throws ParseException {
        Intent intent = new Intent(context, Alarm.class);
        intent.putExtra(Constants.EXTRA_TIME, reminder.getTime());
        intent.putExtra(Constants.EXTRA_DATE, reminder.getDate());
        intent.putExtra(Constants.EXTRA_EVENT, reminder.getTitle());
        intent.putExtra(Constants.EXTRA_DESCRIPTION, reminder.getDescription());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                (int) System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        String dateTime = reminder.getDate() + " " + reminder.getTime();
        DateFormat format = new SimpleDateFormat("d-M-yyyy hh:mm");
        Date dateToSet = format.parse(dateTime);

        if (alarmManager != null && dateToSet != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, dateToSet.getTime(), pendingIntent);
            Toast.makeText(context, "Alarm set", Toast.LENGTH_SHORT).show();
        }
    }
}
