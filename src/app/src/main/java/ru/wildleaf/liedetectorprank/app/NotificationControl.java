package ru.wildleaf.liedetectorprank.app;

import java.util.Calendar;
import java.util.Random;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationControl {
    private Context context;
    private AlarmManager alarmManager = null;
    private PendingIntent pendingIntent;
    private Boolean status = false;

    public NotificationControl(Context context) {
        this.context = context;
    }

    private void setNotification(Boolean statusOn, Boolean showMessage) {
        this.status = statusOn;
        Integer outMessage = null;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (status == true) {
            Calendar calendar = Calendar.getInstance();
            Random rn = new Random();
            Integer hours = rn.nextInt(12);
            Integer minute = rn.nextInt(60);
            Integer seconds = rn.nextInt(60);
            calendar.set(Calendar.HOUR_OF_DAY, hours);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, seconds);
            calendar.add(Calendar.DATE, 1);
            Intent intentAlarm = new Intent(context, NotificationActivity.class);
            pendingIntent = PendingIntent.getBroadcast(context, 415, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 3, pendingIntent);
        } else {
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        }
    }

    public Boolean getStatus() {
        return status;
    }

    // включение ежедневнего уведомления
    public void switchOn() {
        this.switchOn(false);
    }

    public void switchOn(Boolean showMessage) {
        setNotification(true, showMessage);
    }

    // выключение ежедневнего уведомления
    public void switchOff() {
        this.switchOff(false);
    }

    public void switchOff(Boolean showMessage) {
        setNotification(false, showMessage);
    }
}
