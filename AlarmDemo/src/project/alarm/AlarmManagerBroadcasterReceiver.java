package project.alarm;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class AlarmManagerBroadcasterReceiver extends BroadcastReceiver {

	final public static String ONE_TIME = "onetime";
	final public static String REPEAT = "repeat";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();

        //You can do the processing here.
        Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();
         
       /* if(extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)){
         //Make sure this intent has been sent by the one-time timer button.
         msgStr.append("One time Timer : ");
        }*/
        Format formatter = new SimpleDateFormat("hh:mm:ss a");
        msgStr.append(formatter.format(new Date()));

        
        Vibrator v;
        v=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(3000);
        
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
        Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();
         
        //Release the lock
        wl.release();
	}

	public void SetAlarm(Context context,Long alarmtime)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcasterReceiver.class);
//        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        Log.d("Alarm time inMiliseconds -->", alarmtime+"");
        am.setRepeating(AlarmManager.RTC_WAKEUP, alarmtime, AlarmManager.INTERVAL_DAY * 7 , pi); 
        Toast.makeText(context, "Alarm has been set", Toast.LENGTH_SHORT).show();
    }
 
    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmManagerBroadcasterReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        Toast.makeText(context, "Alarm has been cancelled", Toast.LENGTH_SHORT).show();
    }
 
    public void setOnetimeTimer(Context context){
     AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcasterReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }
    
}
