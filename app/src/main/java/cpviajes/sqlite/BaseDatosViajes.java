package cpviajes.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.herprogramacion.CPViajes.sqlite.ContratoViajes.Categorias;
import com.herprogramacion.CPViajes.sqlite.ContratoViajes.MPago;
import com.herprogramacion.CPViajes.sqlite.ContratoViajes.Monedas;
import com.herprogramacion.CPViajes.sqlite.ContratoViajes.TipoV;
import com.herprogramacion.CPViajes.sqlite.ContratoViajes.Viajes;

/**
 * Clase que administra la conexión de la base de datos y su estructuración
 */
public class BaseDatosViajes extends SQLiteOpenHelper {

    private static final String NOMBRE_BASE_DATOS = "cpviajes.db";

    private static final int VERSION_ACTUAL = 1;

    private final Context contexto;

    interface Tablas {
        String EVENTOS = "eventos";
        String VIAJES = "viajes";
        String CATEGORIAS = "categorias";
        String MONEDAS = "monedas";
        String MPAGO = "mpago";
        String TIPOVIAJE = "tipov";
    }

    interface Referencias {

      //  String ID_EVENTOS = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
        //        Tablas.EVENTOS, Eventos.E_ID);

        String ID_CATEGORIAS = String.format("REFERENCES %s(%s)",
                Tablas.CATEGORIAS, Categorias.CAT_ID);

        String ID_MONEDAS = String.format("REFERENCES %s(%s)",
                Tablas.MONEDAS, Monedas.MON_ID);

        String ID_MPAGO = String.format("REFERENCES %s(%s)",
                Tablas.MPAGO, MPago.MPAG_ID);

        String ID_TIPOV = String.format("REFERENCES %s(%s)",
                Tablas.TIPOVIAJE, TipoV.TIPO_ID);
        String ID_VIAJES = String.format("REFERENCES %s(%s)",
                Tablas.VIAJES, Viajes.V_ID);
    }

    public BaseDatosViajes(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON"); //http://stackoverflow.com/questions/2545558/foreign-key-constraints-in-android-using-sqlite-on-delete-cascade
                                                //Foreign key constraints with on delete cascade are supported, but you need to enable them.
                                                // I just added the PRAGMA to my SQLOpenHelper, which seems to do the trick.
            }
        }
    }
//////
public static final String V_ID = "_id";
    public static final String V_NOM = "nom";
    public static final String V_DATAIN = "datain";
    public static final String V_DATAFI = "datafi";
    public static final String V_KMIN = "kmin";
    public static final String V_KMFI = "kmfi";
    public static final String V_TIPO = "tipo";
    public static final String V_DESC = "descrip";
    public static final String V_TGAST = "tgast";
    public static final String V_TKM = "tkm";

    public static final String CAT_ID = "_id";
    public static final String CAT_CGT = "categoria";

    public static final String MPAG_ID = "_id";
    public static final String MPAG_MP = "mpago";

    public static final String TIPO_ID = "_id";
    public static final String TIPO_TIPO = "tipov";

    public static final String MON_ID = "_id";
    public static final String MON_NOM = "nom";
    public static final String MON_VAL = "valor";

    public static final String E_ID = "_id";
    public static final String E_IDV = "idviaje";
    public static final String E_IDCGT = "idcateg";
    public static final String E_DATAH = "fechah";
    public static final String E_KMP = "kmp";
    public static final String E_NOM = "nom";
    public static final String E_DESC = "descripcio";
    public static final String E_MPAG = "modpag";
    public static final String E_MON = "moneda";
    public static final String E_TOT = "totaleur";
    public static final String E_FOT1 = "foto1";
    public static final String E_FOT2 = "foto2";
    public static final String E_VAL = "valoracion";
    public static final String E_DIR = "callenum";
    public static final String E_CP = "cp";
    public static final String E_CIUD = "ciudad";
    public static final String E_TEL = "telef";
    public static final String E_MAIL = "mail";
    public static final String E_WEB = "web";
    public static final String E_LON = "longitud";
    public static final String E_LAT = "latitud";
    public static final String E_ALT = "altitud";
    public static final String E_COM = "comentari";

    public static Integer idViajeA;
    public static String nomViajeA;
    public static String nomViaje;
    public static Integer sumaTgasto;
    //public static final String NOU_TIPO = "@string/nou_modo";  tengo que arreglar esto, para las traducciones

    private static final String TAG = "En ViajeDatabase";

    public static final String TABLE_V = "viajes";
    public static final String TABLE_EVENT = "eventos";
    public static final String TABLE_MON = "monedas";
    public static final String TABLE_TIPO = "tipov";
    public static final String TABLE_CAT = "categorias";
    public static final String TABLE_MPAG = "mpago";

    private static final String V_TABLE =
            "create table " + TABLE_V + " (_id integer primary key autoincrement, "
                    + V_NOM + " text not null, " + V_DATAIN + " date not null, " + V_DATAFI + " date, " + V_KMIN + " integer, "
                    + V_KMFI + " integer, " + V_TIPO + " text, " + V_DESC + " text, " + V_TGAST + " integer, " + V_TKM + " integer);";

    private static final String E_TABLE =
            "create table " + TABLE_EVENT + " (_id integer primary key autoincrement, "
                    + E_IDV + " integer, " + E_IDCGT + " integer, " + E_DATAH + " date not null, " + E_KMP + " integer, "
                    + E_NOM + " text, " + E_DESC + " text, " + E_MPAG + " integer, "
                    + E_MON +" integer, " + E_TOT + " float, " + E_FOT1 + " text, " + E_FOT2 + " text, " + E_VAL + " text, "
                    + E_DIR + " text, " + E_CP + " text, " + E_CIUD + " text, " + E_TEL + " text, " + E_MAIL + " text, "
                    + E_WEB + " text, " + E_LON + " text, " + E_LAT + " text, " + E_ALT + " text, " + E_COM + " text);";

    private static final String TIPO_TABLE =
            "create table " + TABLE_TIPO +" (_id integer primary key autoincrement, "
                    + TIPO_TIPO + " text);";

    private static final String CAT_TABLE =
            "create table " + TABLE_CAT + " (_id integer primary key autoincrement, "
                    + CAT_CGT + " text);";

    private static final String MPAG_TABLE =
            "create table " + TABLE_MPAG + " (_id integer primary key autoincrement, "
                    + MPAG_MP + " text);";

    private static final String MON_TABLE =
            "create table " + TABLE_MON + " (_id integer primary key autoincrement, "
                    + MON_NOM + " text, " + MON_VAL + " float);";





    //////
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Ahora onCreate(SQLiteDatabase db) ");
        db.execSQL(V_TABLE);
        Log.i(TAG, "creada la tabla " + TABLE_V);
        db.execSQL(E_TABLE);
        Log.i(TAG, "creada la tabla " + TABLE_EVENT);
        db.execSQL(MON_TABLE);
        Log.i(TAG, "creada la tabla " + TABLE_MON);
        db.execSQL(TIPO_TABLE);
        Log.i(TAG, "creada la tabla " + TABLE_TIPO);
        db.execSQL(CAT_TABLE);
        Log.i(TAG, "creada la tabla " + TABLE_CAT);
        db.execSQL(MPAG_TABLE);
        Log.i(TAG, "creada la tabla " + TABLE_MPAG);

        //  valores iniciales de los spinners:
        db.execSQL("INSERT INTO tipov (tipov) VALUES ('Autocaravana')");
        db.execSQL("INSERT INTO tipov (tipov) VALUES ('coche')");
        db.execSQL("INSERT INTO tipov (tipov) VALUES ('avión')");
        db.execSQL("INSERT INTO tipov (tipov) VALUES ('tren')");
        db.execSQL("INSERT INTO tipov (tipov) VALUES ('barco')");
        Log.i(TAG, "insertado en la tabla " + TABLE_TIPO);

        db.execSQL("INSERT INTO categorias (categoria) VALUES ('desplazamientos')");
        db.execSQL("INSERT INTO categorias (categoria) VALUES ('alojamiento')");
        db.execSQL("INSERT INTO categorias (categoria) VALUES ('restaurantes')");
        db.execSQL("INSERT INTO categorias (categoria) VALUES ('comida')");
        db.execSQL("INSERT INTO categorias (categoria) VALUES ('regalos')");
        Log.i(TAG, "insertado en la tabla " + TABLE_CAT);

        db.execSQL("INSERT INTO mpago (mpago) VALUES ('efectivo')");
        db.execSQL("INSERT INTO mpago (mpago) VALUES ('ing')");
        db.execSQL("INSERT INTO mpago (mpago) VALUES ('lcaixa')");
        db.execSQL("INSERT INTO mpago (mpago) VALUES ('visalc')");
        db.execSQL("INSERT INTO mpago (mpago) VALUES ('visaing')");
        Log.i(TAG, "insertado en la tabla " + TABLE_MPAG);

        db.execSQL("INSERT INTO monedas (nom, valor) VALUES ('GBP', '1,4')");
        db.execSQL("INSERT INTO monedas (nom, valor) VALUES ('NOK', '1,6')");
        db.execSQL("INSERT INTO monedas (nom, valor) VALUES ('SEK', '1,2')");
        db.execSQL("INSERT INTO monedas (nom, valor) VALUES ('DNK', '0,4')");
        Log.i(TAG, "insertado en la tabla " + TABLE_MON);
    /*
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL,%s DATETIME NOT NULL,%s TEXT NOT NULL %s," +
                        "%s TEXT NOT NULL %s)",
                Tablas.EVENTOS, BaseColumns._ID,
                Eventos.E_ID,
                Eventos.E_IDV, Referencias.ID_VIAJES,
                Eventos.E_IDCGT, Referencias.ID_CATEGORIAS,
                Eventos.E_DATAH,
                Eventos.E_KMP,
                Eventos.E_NOM,
                Eventos.E_DESC,
                Eventos.E_MPAG,Referencias.ID_MPAG,
                Eventos.E_MON, Referencias.ID_MONEDAS,
                Eventos.E_TOT,
                Eventos.E_FOT1,
                Eventos.E_FOT2,
                Eventos.E_VAL,
                Eventos.E_DIR,
                Eventos.E_CP,
                Eventos.E_CIUD,
                Eventos.E_TEL,
                Eventos.E_MAIL,
                Eventos.E_WEB,
                Eventos.E_LON,
                Eventos.E_LAT,
                Eventos.E_ALT,
                Eventos.E_COM));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL %s,%s INTEGER NOT NULL CHECK (%s>0),%s INTEGER NOT NULL %s," +
                        "%s INTEGER NOT NULL,%s REAL NOT NULL,UNIQUE (%s,%s) )",
                Tablas.VIAJES, BaseColumns._ID,
                Viajes.V_NOM,
                Viajes.V_DATAIN,
                Viajes.V_DATAFI,
                Viajes.V_KMIN,
                Viajes.V_KMFI,
                Viajes.V_TIPO, Referencias.ID_TIPOV,
                Viajes.V_DESC,
                Viajes.V_TGAST,
                Viajes.V_TKM));

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s REAL NOT NULL," +
                        "%s INTEGER NOT NULL CHECK(%s>=0) )",
                Tablas.CATEGORIAS, BaseColumns._ID,
                Categorias.CAT_ID,
                Categorias.CAT_CGT));

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s TEXT NOT NULL,%s )",
                Tablas.MONEDAS, BaseColumns._ID,
                Monedas.MON_ID,
                Monedas.MON_NOM,
                Monedas.MON_VAL));

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL )",
                Tablas.MPAGO, BaseColumns._ID,
                MPago.MPAG_ID,
                MPago.MPAG_MP));
        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL )",
                Tablas.TIPOVIAJE, BaseColumns._ID,
                TipoV.TIPO_ID,
                TipoV.TIPO_TIPO));
*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //esborrar TOTES les taules
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.EVENTOS);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.VIAJES);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.CATEGORIAS);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.MONEDAS);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.MPAGO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.TIPOVIAJE);

        onCreate(db);   //crearlas de nuevo
    }


}