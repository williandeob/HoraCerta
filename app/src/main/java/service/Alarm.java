package service;

/**
 * Created by willian on 06/07/2016.
 */
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

        Log.i("Service", "Iníciando serviço de medicamento");

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
        Log.i("Service", "Alert Executado");
        Toast.makeText(context, "TOMAR O MEDICAMENTO : "+medicamento.getNome(), Toast.LENGTH_LONG).show();
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
}
