package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import model.Usuario;
import schema.Database;

/**
 * Created by willian on 19/06/2016.
 */
public class UsuarioDAO implements Persistencia<Usuario> {
    private Context context;

    public UsuarioDAO(Context context){
        this.context = context;
    }

    @Override
    public void insert(Usuario object) {
        Database schema = new Database(this.context);
        SQLiteDatabase db = schema.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID", object.getId());
        values.put("NOME", object.getNome());
        values.put("USERNAME", object.getUserName());
        values.put("EMAIL", object.getEmail());
        values.put("PASSWORD", object.getPassword());
        db.insert("USUARIO", null, values);

        Usuario usuario = Usuario.getUsuarioInstance();
        usuario.setId(object.getId());
        usuario.setNome(object.getNome());
        usuario.setUserName(object.getUserName());
        usuario.setEmail(object.getEmail());
        usuario.setPassword(object.getPassword());
    }

    @Override
    public void update(Usuario object) {

    }

    @Override
    public void delete(Usuario object) {

    }

    @Override
    public List<Usuario> findAll() {
        return null;
    }

    public Usuario getUser(){
        Usuario usuario = Usuario.getUsuarioInstance();
        Database schema = new Database(this.context);
        SQLiteDatabase db = schema.getReadableDatabase();
        Cursor cursor = db.query("USUARIO",null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            usuario.setId(cursor.getLong(cursor.getColumnIndexOrThrow("ID")));
            usuario.setNome(cursor.getString(cursor.getColumnIndexOrThrow("NOME")));
            usuario.setUserName(cursor.getString(cursor.getColumnIndexOrThrow("USERNAME")));
            usuario.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("EMAIL")));
            usuario.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("PASSWORD")));
        }

        cursor.close();
        return usuario;

    }
}
