package cpviajes.modelo;

public class Eventos {

    public String idEventos;

    public String fecha;

    public String idCliente;

    public String idFormaPago;

    public Eventos(String idEventos, String fecha,
                          String idCliente, String idFormaPago) {
        this.idEventos = idEventos;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.idFormaPago = idFormaPago;
    }
}
//////
//
/*
db.execSQL(String.format("CREATE TABLE " +
        "%s Tablas.CABECERA_PEDIDO " +
        "(%s BaseColumns._ID INTEGER PRIMARY KEY AUTOINCREMENT," +
        "%s CabecerasPedido.ID_CABECERA_PEDIDO TEXT UNIQUE NOT NULL," +
        "%s CabecerasPedido.FECHA DATETIME NOT NULL," +
        "%s CabecerasPedido.ID_CLIENTE TEXT NOT NULL %s  Referencias.ID_CLIENTE," +
        "%s CabecerasPedido.ID_FORMA_PAGO TEXT NOT NULL %s Referencias.ID_FORMA_PAGO)",
        Tablas.CABECERA_PEDIDO, ,
        , ,
        ,,
        , ));


*/



////////////////
/*
private static final String E_TABLE =
        "create table " + TABLE_EVENT + " (_id integer primary key autoincrement, "
                + E_IDV + " integer, " + E_IDCGT + " integer, " + E_DATAH + " date not null, " + E_KMP + " integer, "
                + E_NOM + " text, " + E_DESC + " text, " + E_MPAG + " integer, "
                + E_MON +" text, " + E_TOT + " float, " + E_FOT1 + " text, " + E_FOT2 + " text, " + E_VAL + " text, "
                + E_DIR + " text, " + E_CP + " text, " + E_CIUD + " text, " + E_TEL + " text, " + E_MAIL + " text, "
                + E_WEB + " text, " + E_LON + " text, " + E_LAT + " text, " + E_ALT + " text, " + E_COM + " text);";
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


*/