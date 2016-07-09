package service;

/**
 * Created by willian on 06/07/2016.
 */
import android.app.AlarmManager;

import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.ufg.inf.horacerta.LoginActivity;
import br.com.ufg.inf.horacerta.R;
import dao.MedicamentoDAO;
import model.Medicamento;

public class Alarm extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        MedicamentoDAO medicamentoDAO = new MedicamentoDAO(context);
        SimpleDateFormat parse = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date data = new Date();
        List<Medicamento> listaDeMedicamentos = medicamentoDAO.findLimitData(parse.format(data));
        separaMEdicamentosParaAlerta(context, listaDeMedicamentos, data);


        wl.release();
    }

    private void separaMEdicamentosParaAlerta(Context context, List<Medicamento> listaDeMedicamentos, Date data){
        for (Medicamento medicamento : listaDeMedicamentos){
            if(isTime(context, medicamento.getDtInicio(), data, medicamento.getIntervaloEmMinutos())){
                alertUser(context, medicamento);
            }
        }
    }

    private boolean isTime(Context context, Date dtInicio, Date data, int intervalo) {
        SimpleDateFormat parse = new SimpleDateFormat("mm");
        int minutosDtInicio  = Integer.parseInt(parse.format((dtInicio)));
        int minutosData = Integer.parseInt(parse.format((data)));

        if((minutosDtInicio-minutosData)%intervalo == 0){
            return true;
        }else{
            return false;
        }

    }

    private void alertUser(Context context, Medicamento medicamento) {
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeByteArray(medicamento.getImagem(), 0, medicamento.getImagem().length);
        }catch (Exception e){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_photo);
        }

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, LoginActivity.class),0);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notifications_white_48dp)
                        .setLargeIcon(bitmap)
                        .setTicker("Notificação HoraCerta")
                        .setContentTitle("HoraCerta informa")
                        .setContentText("Tomar o medicamento "+medicamento.getNome()+"!")
                        .setContentIntent(resultPendingIntent)
                        .setFullScreenIntent(resultPendingIntent, true)
                        .setOngoing(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                        .setVibrate( new long[]{150, 300, 150, 600, 150, 300, 150, 600, 150, 300, 150, 600,
                                150, 300, 150, 600, 150, 300, 150, 600, 150, 300, 150, 600,
                                150, 300, 150, 600, 150, 300, 150, 600, 150, 300, 150, 600,
                                150, 300, 150, 600, 150, 300, 150, 600, 150, 300, 150, 600,
                                150, 300, 150, 600, 150, 300, 150, 600, 150, 300, 150, 600,
                                150, 300, 150, 600, 150, 300, 150, 600})
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                        .setAutoCancel(true);

        mNotificationManager.notify(Integer.parseInt(""+medicamento.getId()), mBuilder.build());

    }

    public void SetAlarm(Context context)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60 * 1000, pi); // Millisec * Second * Minute
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private void playSound(Context context,Uri alert){
        MediaPlayer mPlayer;

        try{
            mPlayer=new MediaPlayer();
            mPlayer.setDataSource(context,alert);
            final AudioManager am=(AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if(am.getStreamVolume(AudioManager.STREAM_ALARM)!=0);
            {
                mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mPlayer.prepare();
                mPlayer.setLooping(true);
                mPlayer.start();
            }

        }catch(IOException e) {
            Log.i("AlaramReciever", "no audio file");
        }
    }
}
