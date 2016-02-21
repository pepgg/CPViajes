package cpviajes.sqlite;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

import cpviajes.sqlite.BaseDatosViajes.Tablas;
import cpviajes.sqlite.ContratoViajes.Categorias;
import cpviajes.sqlite.ContratoViajes.Eventos;
import cpviajes.sqlite.ContratoViajes.MPago;
import cpviajes.sqlite.ContratoViajes.Monedas;
import cpviajes.sqlite.ContratoViajes.TipoV;
import cpviajes.sqlite.ContratoViajes.Viajes;

public class ProviderViajes extends ContentProvider {


    public static final String TAG = "Provider";
    public static final String URI_NO_SOPORTADA = "Uri no soportada";

    private BaseDatosViajes helper;

    private ContentResolver resolver;


    public ProviderViajes() {
    }

    // [URI_MATCHER]
    public static final UriMatcher uriMatcher;

    // Casos
    public static final int VIAJES = 100;
    public static final int VIAJES_ID = 101;
    public static final int VIAJES_DET = 102;

    public static final int EVENTOS = 200;
    public static final int EVENTOS_ID = 201;
    public static final int EVENTOS_DET= 202;

    public static final int CATEGORIAS = 300;
    public static final int CATEGORIAS_ID = 301;

    public static final int MONEDAS = 400;
    public static final int MONEDAS_ID = 401;

    public static final int M_PAGO = 500;
    public static final int M_PAGO_ID = 501;

    public static final int TIPO_V = 600;
    public static final int TIPO_V_ID = 601;

    //public static final String AUTORIDAD = "com.herprogramacion.pedidos";
    public static final String AUTORIDAD = "pep.gg.viajes";

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTORIDAD, "viajes", VIAJES);
        uriMatcher.addURI(AUTORIDAD, "viajes/*", VIAJES_ID);
        uriMatcher.addURI(AUTORIDAD, "viajes/*/detalles", VIAJES_DET);

        uriMatcher.addURI(AUTORIDAD, "eventos", EVENTOS);
        uriMatcher.addURI(AUTORIDAD, "eventos/*", EVENTOS_ID);
        uriMatcher.addURI(AUTORIDAD, "eventos/*/detalles", EVENTOS_DET);

        uriMatcher.addURI(AUTORIDAD, "categorias", CATEGORIAS);
        uriMatcher.addURI(AUTORIDAD, "categorias/*", CATEGORIAS_ID);

        uriMatcher.addURI(AUTORIDAD, "monedas", MONEDAS);
        uriMatcher.addURI(AUTORIDAD, "monedas/*", MONEDAS_ID);

        uriMatcher.addURI(AUTORIDAD, "m_pago", M_PAGO);
        uriMatcher.addURI(AUTORIDAD, "m_pago/*", M_PAGO_ID);

        uriMatcher.addURI(AUTORIDAD, "tipo_v", TIPO_V);
        uriMatcher.addURI(AUTORIDAD, "tipo_v/*", TIPO_V_ID);
    }
    // [/URI_MATCHER]
/*
    // [CAMPOS_AUXILIARES]
    private static final String CABECERA_PEDIDO_JOIN_CLIENTE_Y_FORMA_PAGO = "cabecera_pedido " +
            "INNER JOIN cliente " +
            "ON cabecera_pedido.id_cliente = cliente.id " +
            "INNER JOIN forma_pago " +
            "ON cabecera_pedido.id_forma_pago = forma_pago.id";

    private static final String DETALLE_PEDIDO_JOIN_PRODUCTO =
            "detalle_pedido " +
                    "INNER JOIN producto " +
                    "ON detalle_pedido.id_producto = producto.id";

    private final String[] proyCabeceraPedido = new String[]{
            BaseDatosViajes.Tablas.VIAJES + "." + ContratoViajes.Eventos.E_ID,
            ContratoViajes.CabecerasPedido.FECHA,
            ContratoViajes.Clientes.NOMBRES,
            ContratoViajes.Clientes.APELLIDOS,
            ContratoViajes.FormasPago.NOMBRE};

    private String[] proyDetalle = {
            Tablas.DETALLE_PEDIDO + ".*",
            Productos.NOMBRE
    };
    // [/CAMPOS_AUXILIARES]
*/
    @Override
    public boolean onCreate() {
        helper = new BaseDatosViajes(getContext());
        resolver = getContext().getContentResolver();
        return true;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete: " + uri);

        SQLiteDatabase bd = helper.getWritableDatabase();
        String id;
        int afectados;

        switch (uriMatcher.match(uri)) {
            case EVENTOS_ID:
                // Obtener id
                id = Eventos.obtenerIdEvento(uri);
                afectados = bd.delete(
                        Tablas.EVENTOS,
                        Eventos.E_ID + " = ? ",
                        new String[]{id}
                );
                notificarCambio(uri);
                break;

            case VIAJES_ID:
                //String[] claves = Viajes.obtenerIdViaje(uri);
                id = Viajes.obtenerIdViaje(uri);
                afectados = bd.delete(Tablas.VIAJES,
                        Viajes.V_ID + "=" + "\"" + id + "\""
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;

            case CATEGORIAS_ID:
                id = Categorias.obtenerIdCategoria(uri);
                afectados = bd.delete(Tablas.CATEGORIAS,
                        Categorias.CAT_ID + "=" + "\"" + id + "\""
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;

            case MONEDAS_ID:
                id = Monedas.obtenerIdMoneda(uri);
                afectados = bd.delete(Tablas.MONEDAS,
                        getWhere(selection, id),
                        selectionArgs);
                break;

            case M_PAGO_ID:
                id = MPago.obtenerIdMPago(uri);
                afectados = bd.delete(Tablas.MPAGO,
                        MPago.MPAG_ID + "=" + "\"" + id + "\""
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;

            case TIPO_V_ID:
                id = TipoV.obtenerIdTipoV(uri);
                afectados = bd.delete(Tablas.MPAGO,
                        MPago.MPAG_ID + "=" + "\"" + id + "\""
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);
        }
        return afectados;
    }

    @NonNull
    private String getWhere(String selection, String id) {
        return Categorias.CAT_ID + "=" + "\"" + id + "\""
                + (!TextUtils.isEmpty(selection) ?
                " AND (" + selection + ')' : "");
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case VIAJES:
                return ContratoViajes.generarMime("viajes");
            case VIAJES_ID:
                return ContratoViajes.generarMimeItem("viajes");
            case VIAJES_DET:
                return ContratoViajes.generarMime("viajes");

            case EVENTOS:
                return ContratoViajes.generarMime("eventos");
            case EVENTOS_ID:
                return ContratoViajes.generarMimeItem("eventos");
            case EVENTOS_DET:
                return ContratoViajes.generarMime("eventos");

            case CATEGORIAS:
                return ContratoViajes.generarMime("categorias");
            case CATEGORIAS_ID:
                return ContratoViajes.generarMimeItem("categorias");

            case MONEDAS:
                return ContratoViajes.generarMime("monedas");
            case MONEDAS_ID:
                return ContratoViajes.generarMimeItem("monedas");

            case M_PAGO:
                return ContratoViajes.generarMime("mpago");
            case M_PAGO_ID:
                return ContratoViajes.generarMimeItem("mpago");

	        case TIPO_V:
                return ContratoViajes.generarMime("tipov");
            case TIPO_V_ID:
                return ContratoViajes.generarMimeItem("tipov");

            default:
                throw new UnsupportedOperationException("Uri desconocida =>" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Log.d(TAG, "Inserción en " + uri + "( " + values.toString() + " )\n");

        SQLiteDatabase bd = helper.getWritableDatabase();
        String id = null;

        switch (uriMatcher.match(uri)) {

            case VIAJES:
                bd.insertOrThrow(Tablas.VIAJES, null, values);
                notificarCambio(uri);
                return Categorias.crearUriCategorias(values.getAsString(Tablas.VIAJES));

            case EVENTOS:
                // Generar Pk
                if (null == values.getAsString(Eventos.E_ID)) {
                    id = Eventos.generarIdEvento();
                    values.put(Eventos.E_ID, id);
                }

                bd.insertOrThrow(Tablas.EVENTOS, null, values);
                notificarCambio(uri);
                return Eventos.crearUriEvento(id);  /////este es rarooooooooo

            case CATEGORIAS:
                bd.insertOrThrow(Tablas.CATEGORIAS, null, values);
                notificarCambio(uri);
                return Categorias.crearUriCategorias(values.getAsString(Tablas.CATEGORIAS));

            case MONEDAS:
                bd.insertOrThrow(Tablas.MONEDAS, null, values);
                notificarCambio(uri);
                return Monedas.crearUriMonedas(values.getAsString(Monedas.MON_ID));

            case M_PAGO:
                bd.insertOrThrow(Tablas.MPAGO, null, values);
                notificarCambio(uri);
                return MPago.crearUriMPago(values.getAsString(MPago.MPAG_ID));

	        case TIPO_V:
                bd.insertOrThrow(Tablas.TIPOVIAJE, null, values);
                notificarCambio(uri);
                return TipoV.crearUriTipoV(values.getAsString(TipoV.TIPO_ID));

            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);
        }

    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Obtener base de datos
        SQLiteDatabase bd = helper.getReadableDatabase();

        // Comparar Uri
        int match = uriMatcher.match(uri);

        // string auxiliar para los ids
        String id;

        Cursor c = null;

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        switch (match) {
            case EVENTOS:
                /*
                // Obtener filtro
                String filtro = Eventos.tieneFiltro(uri)
                        ? construirFiltro(uri.getQueryParameter("filtro")) : null;

                // Consultando todos los eventos


                builder.setTables(EVENTOS_JOIN_MONEDAS_Y_M_PAGO);
                c = builder.query(bd, proyEvento,
                        null, null, null, null, filtro);
                */
                c = bd.query(Tablas.EVENTOS, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case EVENTOS_ID:
                // Consultando un evento
                id = Eventos.obtenerIdEvento(uri);
                /*
                builder.setTables(EVENTOS_JOIN_MONEDAS_Y_M_PAGO);
                c = builder.query(bd, proyEvento,
                        Evento.ID + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                */
                c = bd.query(Tablas.EVENTOS, projection,
                        Eventos.E_ID + " = ?",
                        new String[]{id}, null, null, null);
                break;
            case EVENTOS_DET:
                id = Eventos.obtenerIdEvento(uri);
                /*
                builder.setTables(DETALLE_EVENTO_JOIN_CATEGORIA);
                c = builder.query(bd, proyDetalle,
                        DetallesEvento.ID_EVENTOS + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, sortOrder);
                */
                break;

            case VIAJES:
                /*
                builder.setTables(DETALLE_VIAJE_JOIN_CATEGORIA);
                c = builder.query(bd, proyViaje,
                        selection, selectionArgs, null, null, sortOrder);
                */
                c = bd.query(Tablas.VIAJES, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case VIAJES_ID:
                // Consultando un viaje
                id = Viajes.obtenerIdViaje(uri);
                /*
                builder.setTables(EVENTOS_JOIN_MONEDAS_Y_M_PAGO);
                c = builder.query(bd, proyEvento,
                        Evento.ID + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                */
                c = bd.query(Tablas.VIAJES, projection,
                        Viajes.V_ID + " = ?",
                        new String[]{id}, null, null, null);
                break;

            case VIAJES_DET:
                id = Viajes.obtenerIdViaje(uri);
                /*
                builder.setTables(DETALLE_EVENTO_JOIN_CATEGORIA);
                c = builder.query(bd, proyDetalle,
                        DetallesEvento.ID_EVENTOS + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, sortOrder);
                 */
                break;




            case CATEGORIAS:
                c = bd.query(Tablas.CATEGORIAS, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CATEGORIAS_ID:
                id = Categorias.obtenerIdCategoria(uri);
                c = bd.query(Tablas.CATEGORIAS, projection,
                        Categorias.CAT_ID + " = ?",
                        new String[]{id}, null, null, null);
                break;

            case MONEDAS:
                c = bd.query(Tablas.MONEDAS, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case MONEDAS_ID:
                id = Monedas.obtenerIdMoneda(uri);
                c = bd.query(Tablas.MONEDAS, projection,
                        Monedas.MON_ID + " = ?",
                        new String[]{id}, null, null, null);
                break;

            case M_PAGO:
                c = bd.query(Tablas.MPAGO, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case M_PAGO_ID:
                id = MPago.obtenerIdMPago(uri);
                c = bd.query(Tablas.MPAGO, projection,
                        MPago.MPAG_ID + " = ?",
                        new String[]{id}, null, null, null);
                break;

            case TIPO_V:
                c = bd.query(Tablas.TIPOVIAJE, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case TIPO_V_ID:
                id = TipoV.obtenerIdTipoV(uri);
                c = bd.query(Tablas.TIPOVIAJE, projection,
                        TipoV.TIPO_ID + " = ?",
                        new String[]{id}, null, null, null);
                break;

            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);
        }
        c.setNotificationUri(resolver, uri);

        return c;

    }
/*
    private String construirFiltro(String filtro) {
      //  String sentencia = null;


        String nombre = null;
        switch (filtro) {
            case Viajes.FILTRO_CLIENTE:
                sentencia = "MONEDAS.nombres";
                break;
            case Viajes.FILTRO_FECHA:
                sentencia = "Viajes.fecha";
                break;
        }

        return sentencia;

    }
*/
    private void notificarCambio(Uri uri) {
        resolver.notifyChange(uri, null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase bd = helper.getWritableDatabase();
        String id;
        int afectados;

        switch (uriMatcher.match(uri)) {
            case EVENTOS_ID:
                id = Eventos.obtenerIdEvento(uri);
                afectados = bd.update(Tablas.EVENTOS, values,
                        Eventos.E_ID + " = ?", new String[]{id});
                notificarCambio(uri);
                break;

            case VIAJES_ID:
                id = Viajes.obtenerIdViaje(uri);
                afectados = bd.update(Tablas.VIAJES, values,
                        Viajes.V_ID + " = ?", new String[]{id});
                notificarCambio(uri);
                break;

            case CATEGORIAS_ID:
                id = Categorias.obtenerIdCategoria(uri);
                afectados = bd.update(Tablas.CATEGORIAS, values,
                        Categorias.CAT_ID + " = ?", new String[]{id});
                notificarCambio(uri);
                break;

            case MONEDAS_ID:
                id = Monedas.obtenerIdMoneda(uri);
                afectados = bd.update(Tablas.MONEDAS, values,
                        Monedas.MON_ID + " = ?", new String[]{id});
                notificarCambio(uri);
                break;

            case M_PAGO_ID:
                id = MPago.obtenerIdMPago(uri);
                afectados = bd.update(Tablas.MPAGO, values,
                        MPago.MPAG_ID + " = ?", new String[]{id});
                notificarCambio(uri);
                break;

	    case TIPO_V_ID:
                id = TipoV.obtenerIdTipoV(uri);
            afectados = bd.update(Tablas.TIPOVIAJE, values,
                    TipoV.TIPO_ID + " = ?", new String[]{id});
            notificarCambio(uri);
                break;

            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);
        }


        return afectados;
    }
}

