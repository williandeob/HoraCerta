package service;

/**
 * Created by willian on 06/07/2016.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmAutoBoot extends BroadcastReceiver
{
    Alarm alarm = new Alarm();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            alarm.SetAlarm(context);
        }
    }
}
