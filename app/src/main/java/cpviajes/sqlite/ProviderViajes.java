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
            case CABECERAS_PEDIDOS_ID:
                // Obtener id
                id = CabecerasPedido.obtenerIdCabeceraPedido(uri);
                afectados = bd.delete(
                        Tablas.CABECERA_PEDIDO,
                        CabecerasPedido.ID + " = ? ",
                        new String[]{id}
                );
                notificarCambio(uri);
                break;

            case DETALLES_PEDIDOS_ID:
                String[] claves = DetallesPedido.obtenerIdDetalle(uri);

                String seleccion = String.format("%s=? AND %s=?",
                        DetallesPedido.ID_CABECERA_PEDIDO, DetallesPedido.SECUENCIA);

                afectados = bd.delete(Tablas.DETALLE_PEDIDO, seleccion, claves);
                break;

            case PRODUCTOS_ID:
                id = Productos.obtenerIdProducto(uri);
                afectados = bd.delete(Tablas.PRODUCTO,
                        Productos.ID + "=" + "\"" + id + "\""
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;

            case CLIENTES_ID:
                id = Clientes.obtenerIdCliente(uri);
                afectados = bd.delete(Tablas.CLIENTE,
                        getWhere(selection, id),
                        selectionArgs);
                break;

            case FORMAS_PAGO_ID:
                id = FormasPago.obtenerIdFormaPago(uri);
                afectados = bd.delete(Tablas.FORMA_PAGO,
                        FormasPago.ID + "=" + "\"" + id + "\""
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
        return Productos.ID + "=" + "\"" + id + "\""
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
                return ContratoPedidos.generarMimeItem("eventos");
            case CATEGORIAS:
                return ContratoPedidos.generarMime("categorias");
            case CATEGORIAS_ID:
                return ContratoPedidos.generarMimeItem("categorias");
            case MONEDAS:
                return ContratoPedidos.generarMime("monedas");
            case MONEDAS_ID:
                return ContratoPedidos.generarMimeItem("monedas");
            case M_PAGO:
                return ContratoPedidos.generarMime("mpago");
            case M_PAGO_ID:
                return ContratoPedidos.generarMimeItem("mpago");
	    case TIPOV:
                return ContratoPedidos.generarMime("tipov");
            case TIPOV_ID:
                return ContratoPedidos.generarMimeItem("tipov");
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
                return Productos.crearUriProducto(values.getAsString(Tablas.CATEGORIAS));

            case MONEDAS:
                bd.insertOrThrow(Tablas.MONEDAS, null, values);
                notificarCambio(uri);
                return Clientes.crearUriCliente(values.getAsString(Clientes.ID));

            case MPAGO:
                bd.insertOrThrow(Tablas.FORMA_PAGO, null, values);
                notificarCambio(uri);
                return FormasPago.crearUriFormaPago(values.getAsString(mPago.ID));

	    case TIPOV:
                bd.insertOrThrow(Tablas.TIPOV, null, values);
                notificarCambio(uri);
                return FormasPago.crearUriTipoV(values.getAsString(TipoV.ID));

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
/*
        switch (match) {
            case CABECERAS_PEDIDOS:
                // Obtener filtro
                String filtro = CabecerasPedido.tieneFiltro(uri)
                        ? construirFiltro(uri.getQueryParameter("filtro")) : null;

                // Consultando todas las cabeceras de pedido
                builder.setTables(CABECERA_PEDIDO_JOIN_CLIENTE_Y_FORMA_PAGO);
                c = builder.query(bd, proyCabeceraPedido,
                        null, null, null, null, filtro);
                break;
            case CABECERAS_PEDIDOS_ID:
                // Consultando una cabecera de pedido
                id = CabecerasPedido.obtenerIdCabeceraPedido(uri);
                builder.setTables(CABECERA_PEDIDO_JOIN_CLIENTE_Y_FORMA_PAGO);
                c = builder.query(bd, proyCabeceraPedido,
                        CabecerasPedido.ID + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;
            case CABECERAS_ID_DETALLES:
                id = CabecerasPedido.obtenerIdCabeceraPedido(uri);
                builder.setTables(DETALLE_PEDIDO_JOIN_PRODUCTO);
                c = builder.query(bd, proyDetalle,
                        DetallesPedido.ID_CABECERA_PEDIDO + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, sortOrder);
                break;

            case DETALLES_PEDIDOS:
                builder.setTables(DETALLE_PEDIDO_JOIN_PRODUCTO);
                c = builder.query(bd, proyDetalle,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case PRODUCTOS:
                c = bd.query(Tablas.PRODUCTO, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PRODUCTOS_ID:
                id = Productos.obtenerIdProducto(uri);
                c = bd.query(Tablas.PRODUCTO, projection,
                        Productos.ID + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs,
                        null, null, sortOrder);
                break;

            case CLIENTES:
                c = bd.query(Tablas.CLIENTE, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case CLIENTES_ID:
                id = Clientes.obtenerIdCliente(uri);
                c = bd.query(Tablas.CLIENTE, projection,
                        Clientes.ID + " = ?",
                        new String[]{id}, null, null, null);
                break;

            case FORMAS_PAGO:
                c = bd.query(Tablas.FORMA_PAGO, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case FORMAS_PAGO_ID:
                id = FormasPago.obtenerIdFormaPago(uri);
                c = bd.query(Tablas.FORMA_PAGO, projection,
                        FormasPago.ID + " = ?",
                        new String[]{id}, null, null, null);
                break;

            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);
        }
*/
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
            case CABECERAS_PEDIDOS_ID:
                id = Viajes.obtenerIdCabeceraPedido(uri);
                afectados = bd.update(Tablas.CABECERA_PEDIDO, values,
                        Viajes.ID + " = ?", new String[]{id});
                notificarCambio(uri);
                break;

            case DETALLES_PEDIDOS_ID:
                String[] claves = DetallesPedido.obtenerIdDetalle(uri);

                String seleccion = String.format("%s=? AND %s=?",
                        DetallesPedido.ID_CABECERA_PEDIDO, DetallesPedido.SECUENCIA);

                afectados = bd.update(Tablas.DETALLE_PEDIDO, values, seleccion, claves);
                break;

            case PRODUCTOS_ID:
                id = Productos.obtenerIdProducto(uri);
                afectados = bd.update(Tablas.PRODUCTO, values,
                        Productos.ID + "=" + "\"" + id + "\""
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;

            case CLIENTES_ID:
                id = Clientes.obtenerIdCliente(uri);
                afectados = bd.update(Tablas.CLIENTE, values,
                        Clientes.ID + "=" + "\"" + id + "\""
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;

            case FORMAS_PAGO_ID:
                id = FormasPago.obtenerIdFormaPago(uri);
                afectados = bd.update(Tablas.FORMA_PAGO, values,
                        FormasPago.ID + "=" + "\"" + id + "\""
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

