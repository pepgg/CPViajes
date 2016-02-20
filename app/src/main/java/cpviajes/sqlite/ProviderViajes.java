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

import com.herprogramacion.CPViajes.sqlite.BaseDatosViajes.Tablas;

import java.util.ArrayList;

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
    public static final int EVENTOS_DET= 201;

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
                        Eventos.ID + " = ? ",
                        new String[]{id}
                );
                notificarCambio(uri);
                break;

            case VIAJES_ID:
                String[] claves = Viaje.obtenerIdViaje(uri);

                String seleccion = String.format("%s=? AND %s=?",
                        Viaje.ID_EVENTO, Viaje.NOMBRE);

                afectados = bd.delete(Tablas.VIAJES, seleccion, claves);
                break;

            case CATEGORIAS_ID:
                id = Productos.obtenerIdProducto(uri);
                afectados = bd.delete(Tablas.CATEGORIAS,
                        Categorias.ID + "=" + "\"" + id + "\""
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;

            case MONEDAS_ID:
                id = Monedas.obtenerIdMonedas(uri);
                afectados = bd.delete(Tablas.MONEDAS,
                        getWhere(selection, id),
                        selectionArgs);
                break;

            case MPAGO_ID:
                id = MPago.obtenerIdMPago(uri);
                afectados = bd.delete(Tablas.M_PAGO,
                        MPago.ID + "=" + "\"" + id + "\""
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
        return Categorias.ID + "=" + "\"" + id + "\""
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
            case DETALLES_EVENTOS_ID:
                return ContratoEventos.generarMimeItem("eventos");
            case CATEGORIAS:
                return ContratoCategorias.generarMime("categorias");
            case CATEGORIAS_ID:
                return ContratoCategorias.generarMimeItem("categorias");
            case MONEDAS:
                return ContratoMonedas.generarMime("monedas");
            case MONEDAS_ID:
                return ContratoMonedas.generarMimeItem("monedas");
            case M_PAGO:
                return ContratoMpago.generarMime("mpago");
            case M_PAGO_ID:
                return ContratoMpago.generarMimeItem("mpago");
	    case TIPOV:
                return ContratoTipov.generarMime("tipov");
            case TIPOV_ID:
                return ContratoTipov.generarMimeItem("tipov");
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
            case EVENTOS:
                // Generar Pk
                if (null == values.getAsString(Eventos.ID)) {
                    id = Eventos.generarIdEventos();
                    values.put(Eventos.ID, id);
                }

                bd.insertOrThrow(Tablas.EVENTOS, null, values);
                notificarCambio(uri);
                return Eventos.crearUriEventos(id);

            case VIAJES_ID_DETALLES:
                // Setear id_cabecera_pedido
                id = Viajes.obtenerIdViaje(uri);

                values.put(Viajes.ID_VIAJES, id);
                bd.insertOrThrow(Tablas.VIAJES, null, values);
                notificarCambio(uri);

                String nomviaje = values.getAsString(Viajes.NOMBRE);

                return Viajes.crearUriViajes(id, nombre);

            case CATEGORIAS:
                bd.insertOrThrow(Tablas.CATEGORIAS, null, values);
                notificarCambio(uri);
                return Categorias.crearUriCategorias(values.getAsString(Tablas.CATEGORIAS));

            case MONEDAS:
                bd.insertOrThrow(Tablas.MONEDAS, null, values);
                notificarCambio(uri);
                return Monedas.crearUriMonedas(values.getAsString(Monedas.ID));

            case MPAGO:
                bd.insertOrThrow(Tablas.MPAGO, null, values);
                notificarCambio(uri);
                return MPago.crearUriMPago(values.getAsString(mPago.ID));

	    case TIPOV:
                bd.insertOrThrow(Tablas.TIPOV, null, values);
                notificarCambio(uri);
                return Tipov.crearUriTipoV(values.getAsString(TipoV.ID));

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

        Cursor c;

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        switch (match) {
            case EVENTOS:
                // Obtener filtro
                String filtro = Eventos.tieneFiltro(uri)
                        ? construirFiltro(uri.getQueryParameter("filtro")) : null;

                // Consultando todas las cabeceras de pedido
                builder.setTables(EVENTOS_JOIN_MONEDAS_Y_M_PAGO);
                c = builder.query(bd, proyEvento,
                        null, null, null, null, filtro);
                break;
            case EVENTOS_ID:
                // Consultando una cabecera de pedido
                id = Eventos.obtenerIdEvento(uri);
                builder.setTables(EVENTOS_JOIN_MONEDAS_Y_M_PAGO);
                c = builder.query(bd, proyEvento,
                        Evento.ID + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;
            case EVENTOS_ID_DETALLES:
                id = Eventos.obtenerIdEvento(uri);
                builder.setTables(DETALLE_EVENTO_JOIN_CATEGORIA);
                c = builder.query(bd, proyDetalle,
                        DetallesEvento.ID_EVENTOS + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, sortOrder);
                break;

            case DETALLES_VIAJES:
                builder.setTables(DETALLE_VIAJE_JOIN_CATEGORIA);
                c = builder.query(bd, proyViaje,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case CATEGORIAS:
                c = bd.query(Tablas.CATEGORIAS, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CATEGORIAS_ID:
                id = Categorias.obtenerIdCategoria(uri);
                c = bd.query(Tablas.CATEGORIAS, projection,
                        Categorias.ID + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs,
                        null, null, sortOrder);
                break;

            case MONEDAS:
                c = bd.query(Tablas.MONEDAS, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case MONEDAS_ID:
                id = Monedas.obtenerIdMoneda(uri);
                c = bd.query(Tablas.MONEDa, projection,
                        Monedas.ID + " = ?",
                        new String[]{id}, null, null, null);
                break;

            case M_PAGO:
                c = bd.query(Tablas.M_PAGO, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case M_PAGO_ID:
                id = MPago.obtenerIdMPago(uri);
                c = bd.query(Tablas.M_PAGO, projection,
                        MPago.ID + " = ?",
                        new String[]{id}, null, null, null);
                break;

            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);
        }

        c.setNotificationUri(resolver, uri);

        return c;

    }

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
                id = Viajes.obtenerIdEvento(uri);
                afectados = bd.update(Tablas.EVENTOS, values,
                        Viajes.ID + " = ?", new String[]{id});
                notificarCambio(uri);
                break;

            case DETALLES_EVENTOS_ID:
                String[] claves = DetallesEvento.obtenerIdEvento(uri);

                String seleccion = String.format("%s=? AND %s=?",
                        DetallesEvento.ID_EVENTOS, DetallesEvento.SECUENCIA);

                afectados = bd.update(Tablas.DETALLE_EVENTO, values, seleccion, claves);
                break;

            case CATEGORIAS_ID:
                id = categorias.obtenerIdcategoria(uri);
                afectados = bd.update(Tablas.CATEGORIAS, values,
                        Productos.ID + "=" + "\"" + id + "\""
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;

            case MONEDAS_ID:
                id = Monedas.obtenerIdMoneda(uri);
                afectados = bd.update(Tablas.MONEDAS, values,
                        Monedas.ID + "=" + "\"" + id + "\""
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;

            case M_PAGO_ID:
                id = MPago.obtenerIdMPago(uri);
                afectados = bd.update(Tablas.M_PAGO, values,
                        MPago.ID + "=" + "\"" + id + "\""
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;

	    case TIPOV_ID:
                id = Tipov.obtenerIdTipov(uri);
                afectados = bd.update(Tablas.TIPOV, values,
                        Tipov.ID + "=" + "\"" + id + "\""
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);
        }


        return afectados;
    }
}

