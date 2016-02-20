package cpviajes.sqlite;

import android.net.Uri;

import java.util.UUID;

/**
 * Clase que establece los nombres a usar en la base de datos
 */
public class ContratoViajes {


    interface ColumnasEventos {
        String E_ID = "id";
        String E_IDV = "idviaje";
        String E_IDCGT = "idcateg";
        String E_DATAH = "fechah";
        String E_KMP = "kmp";
        String E_NOM = "nom";
        String E_DESC = "descripcio";
        String E_MPAG = "modpag";
        String E_MON = "moneda";
        String E_TOT = "totaleur";
        String E_FOT1 = "foto1";
        String E_FOT2 = "foto2";
        String E_VAL = "valoracion";
        String E_DIR = "callenum";
        String E_CP = "cp";
        String E_CIUD = "ciudad";
        String E_TEL = "telef";
        String E_MAIL = "mail";
        String E_WEB = "web";
        String E_LON = "longitud";
        String E_LAT = "latitud";
        String E_ALT = "altitud";
        String E_COM = "comentari";
    }

    interface ColumnasViajes {
        String V_ID = "id";
        String V_NOM = "nom";
        String V_DATAIN = "datain";
        String V_DATAFI = "datafi";
        String V_KMIN = "kmin";
        String V_KMFI = "kmfi";
        String V_TIPO = "tipo";
        String V_DESC = "descrip";
        String V_TGAST = "tgast";
        String V_TKM = "tkm";;
    }

    interface ColumnasCategorias {
        String CAT_ID = "id";
        String CAT_CGT = "categoria";
    }

    interface ColumnasMonedas {
        String MON_ID = "id";
        String MON_NOM = "nom";
        String MON_VAL = "valor";
    }

    interface ColumnasMPago {
        String MPAG_ID = "id";
        String MPAG_MP = "mpago";
    }

    interface ColumnasTipoV {
        String TIPO_ID = "id";
        String TIPO_TIPO = "tipov";
    }

    // [URIS]
    public static final String AUTORIDAD = "cpviajes";

    public static final Uri URI_BASE = Uri.parse("content://" + AUTORIDAD);

    private static final String RUTA_EVENTOS = "eventos";
    private static final String RUTA_VIAJES = "viajes";
    private static final String RUTA_CATEGORIAS = "categorias";
    private static final String RUTA_MONEDAS = "monedas";
    private static final String RUTA_MPAGO = "mpago";
    private static final String RUTA_TIPOV = "tipov";
    // [/URIS]


    // [TIPOS_MIME]
    public static final String BASE_CONTENIDOS = "viajes.";

    public static final String TIPO_CONTENIDO = "vnd.android.cursor.dir/vnd."
            + BASE_CONTENIDOS;

    public static final String TIPO_CONTENIDO_ITEM = "vnd.android.cursor.item/vnd."
            + BASE_CONTENIDOS;

    public static String generarMime(String id) {
        if (id != null) {
            return TIPO_CONTENIDO + id;
        } else {
            return null;
        }
    }

    public static String generarMimeItem(String id) {
        if (id != null) {
            return TIPO_CONTENIDO_ITEM + id;
        } else {
            return null;
        }
    }
    // [/TIPOS_MIME]


    public static class Eventos implements ColumnasEventos {

        public static final Uri URI_CONTENIDO =
                URI_BASE.buildUpon().appendPath(RUTA_EVENTOS).build();

        public static final String PARAMETRO_FILTRO = "filtro";
        public static final String FILTRO_VIAJE = "viaje";
        public static final String FILTRO_TOTAL_G = "totalg";
        public static final String FILTRO_FECHA = "fecha";

         public static String obtenerIdEvento(Uri uri) {
            return uri.getPathSegments().get(1);         } // lo cambio por el siguiente:
        //public static String obtenerIdEvento(Uri uri) {
        //    return uri.getLastPathSegment();
        //}

        public static Uri crearUriEvento(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static Uri crearUriParaViajes(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).appendPath("viajes").build();
        }

        public static boolean tieneFiltro(Uri uri) {
            return uri != null && uri.getQueryParameter(PARAMETRO_FILTRO) != null;
        }

        public static String generarIdEvento() {
            return "EV-" + UUID.randomUUID().toString();
        }
    }


    public static class Viajes implements ColumnasViajes {
        public static final Uri URI_CONTENIDO =
                URI_BASE.buildUpon().appendPath(RUTA_VIAJES).build();

        public static Uri crearUriViajes(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }
        public static String[] obtenerIdViaje(Uri uri) {
            return uri.getLastPathSegment().split("#");
        }

    }
    /*
    public static Uri crearUriViajes(String id) {
        return URI_CONTENIDO.buildUpon().appendPath(id).appendPath("viajes").build();
    }
    */
    public static class Categorias implements ColumnasCategorias {
        public static final Uri URI_CONTENIDO =
                URI_BASE.buildUpon().appendPath(RUTA_CATEGORIAS).build();

        public static Uri crearUriCategorias(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static String generarIdCategoria() {
            return "CAT-" + UUID.randomUUID().toString();
        }

        public static String obtenerIdCategoria(Uri uri) {
            return uri.getLastPathSegment();
        }
    }

    public static class Monedas implements ColumnasMonedas {
        public static final Uri URI_CONTENIDO =
                URI_BASE.buildUpon().appendPath(RUTA_MONEDAS).build();

        public static Uri crearUriMonedas(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static String generarIdMoneda() {
            return "MO-" + UUID.randomUUID().toString();
        }

        public static String obtenerIdMoneda(Uri uri) {
            return uri.getLastPathSegment();
        }
    }

    public static class MPago implements ColumnasMPago {
        public static final Uri URI_CONTENIDO =
                URI_BASE.buildUpon().appendPath(RUTA_MPAGO).build();

        public static Uri crearUriMPago(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static String generarIdMPago() {
            return "MP-" + UUID.randomUUID().toString();
        }

        public static String obtenerIdMPago(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class TipoV implements ColumnasTipoV {
        public static final Uri URI_CONTENIDO =
                URI_BASE.buildUpon().appendPath(RUTA_TIPOV).build();

        public static Uri crearUriTipoV(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static String generarIdTipoV() {
            return "TV-" + UUID.randomUUID().toString();
        }

        public static String obtenerIdTipoV(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    private ContratoViajes() {
    }

}
