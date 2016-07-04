package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Medicamento;
import schema.Database;

/**
 * Created by willian on 02/07/2016.
 */
public class MedicamentoDAO  implements Persistencia<Medicamento>{
    private Context context;

    public MedicamentoDAO(Context context) {
        this.context = context;
    }

    @Override
    public void insert(Medicamento object) {
        String pattern = "MM-dd-yyyy HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(pattern);

        Database schema = new Database(this.context);
        SQLiteDatabase db = schema.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("ID",(findLastId()+1));
        values.put("NOME", object.getNome());
        values.put("IMAGEM", object.getImagem());
        values.put("DESCRICAO_DO_USO", object.getDescricaoDoUso());
        values.put("DT_INICIO", format.format(object.getDtInicio()));
        values.put("INTERVALO_EM_MINUTOS", object.getIntervaloEmMinutos());
        values.put("USUARIO_ID", object.getUsuario().getId());
        db.insert("MEDICAMENTO", null, values);

    }

    @Override
    public void update(Medicamento object) {

    }

    @Override
    public void delete(Medicamento object) {

    }

    @Override
    public List<Medicamento> findAll() {
        Database schema = new Database(this.context);
        SQLiteDatabase db = schema.getReadableDatabase();
        Cursor cursor = db.query("Medicamento",null, null, null, null, null, null);
        if(cursor.getCount() == 0){
            return null;
        }else {

            List<Medicamento> listaMedicamentosReturn = new ArrayList<Medicamento>();

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                Medicamento medicamento = new Medicamento();
                medicamento.setId(cursor.getLong(cursor.getColumnIndex("ID")));
                medicamento.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
                medicamento.setImagem(cursor.getBlob(cursor.getColumnIndex("IMAGEM")));
                medicamento.setDescricaoDoUso(cursor.getString(cursor.getColumnIndex("DESCRICAO_DO_USO")));
                medicamento.setIntervaloEmMinutos(cursor.getInt(cursor.getColumnIndex("INTERVALO_EM_MINUTOS")));

                String dataString = cursor.getString(cursor.getColumnIndex("DT_INICIO"));
                String pattern = "MM/dd/yyyy HH:mm:ss";
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                Date date = null;
                try {
                    date = format.parse(dataString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                medicamento.setDtInicio(date);

                listaMedicamentosReturn.add(medicamento);
            }

            return listaMedicamentosReturn;
        }
    }

    public long findLastId(){
        String sql = "SELECT ID FROM MEDICAMENTO ORDER BY ID DESC LIMIT 1";
        Database schema = new Database(this.context);
        SQLiteDatabase db = schema.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.getCount() == 0){
            cursor.close();
           return 0;
        }else {
            Long idReturn;
            cursor.moveToFirst();
            idReturn = cursor.getLong(cursor.getColumnIndex("ID"));
            cursor.close();
            return idReturn;
        }
    }
}