package schema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by willian on 19/06/2016.
 */
public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "HoraCerta.db";

    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS USUARIO (" +
                    "ID INTEGER PRIMARY KEY NOT NULL, " +
                    "EMAIL TEXT NOT NULL, "+
                    "NOME TEXT NOT NULL, "+
                    "SENHA TEXT NOT NULL);"+

            "CREATE TABLE IF NOT EXISTS MEDICAMENTO ("+
                    "ID INTEGER PRIMARY KEY NOT NULL, "+
                    "DESCRICAO_DO_USO TEXT, "+
                    "DT_INICIO TEXT NOT NULL, "+
                    "IMAGEM BLOB, "+
                    "INTERVALO_EM_MINUTOS INTEGER NOT NULL, "+
                    "NOME TEXT NOT NULL, "+
                    "USUARIO_ID INTEGER PRIMARY KEY NOT NULL);";


    private static final String SQL_DROP_TABLES =
            "DROP TABLE IF EXISTS USUARIO, MEDICAMENTO;";

    public Database (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLES);
        onCreate(db);
    }
}
