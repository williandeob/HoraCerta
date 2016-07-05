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

    private static final String DICTIONARY_TABLE_CREATE_USUARIO =
            "CREATE TABLE IF NOT EXISTS USUARIO (" +
                    "ID INTEGER PRIMARY KEY NOT NULL, " +
                    "NOME TEXT NOT NULL, "+
                    "USERNAME TEXT NOT NULL, "+
                    "EMAIL TEXT NOT NULL, "+
                    "PASSWORD TEXT NOT NULL);";

    private static final String DICTIONARY_TABLE_CREATE_MEDICAMEENTO=
            "CREATE TABLE IF NOT EXISTS MEDICAMENTO ("+
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
                    "DESCRICAO_DO_USO TEXT, "+
                    "DT_INICIO DATETIME NOT NULL, "+
                    "IMAGEM BLOB, "+
                    "INTERVALO_EM_MINUTOS INTEGER NOT NULL, "+
                    "NOME TEXT NOT NULL, "+
                    "USUARIO_ID INTEGER NOT NULL," +
                    "FOREIGN KEY(USUARIO_ID) REFERENCES USUARIO(ID));";


    private static final String SQL_DROP_TABLES =
            "DROP TABLE IF EXISTS USUARIO;" +
                    "DROP TABLE IF EXISTS MEDICAMENTO;";

    public Database (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE_USUARIO);
        db.execSQL(DICTIONARY_TABLE_CREATE_MEDICAMEENTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLES);
        onCreate(db);
    }
}
