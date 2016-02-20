package cpviajes.modelo;

public class Eventos {

    public int idEventos;
    public int idViaje;
    public int idCategoria;
    public String fechah;
    public int kmp;
    public String nom;
    public String descripcio;
    public int modpag;
    public int moneda;
    public float totaleur;
    public String foto1;
    public String foto2;
    public String valoracion;
    public String callenum;
    public String cp;
    public String ciudad;
    public String telef;
    public String mail;
    public String web;
    public String longitud;
    public String latitud;
    public String altitud;
    public String comentari;


    public Eventos (int idEventos, int idViaje, int idCategoria, String fechah,
                    int kmp, String nom, String descripcio, int modpag, int moneda,
                    float totaleur, String foto1, String foto2, String valoracion, String callenum,
                    String cp, String ciudad, String telef, String mail, String web, String longitud,
                    String latitud, String altitud, String comentari
    ) {
        this.idEventos = idEventos;
        this.idViaje = idViaje;
        this.idCategoria = idCategoria;
        this.fechah = fechah;
        this.kmp = kmp;
        this.nom = nom;
        this.descripcio = descripcio;
        this.modpag = modpag;
        this.moneda = moneda;
        this.totaleur = totaleur;
        this.foto1 = foto1;
        this.foto2 = foto2;
        this.valoracion = valoracion;
        this.callenum = callenum;
        this.cp = cp;
        this.ciudad = ciudad;
        this.telef = telef;
        this.mail = mail;
        this.web = web;
        this.longitud = longitud;
        this.latitud = latitud;
        this.altitud = altitud;
        this.comentari = comentari;
    }
}
//////
//
////////
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