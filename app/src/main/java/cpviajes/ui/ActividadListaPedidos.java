package cpviajes.ui;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.herprogramacion.CPViajes.R;
import com.herprogramacion.CPViajes.sqlite.ContratoPedidos;
import com.herprogramacion.CPViajes.sqlite.ContratoPedidos.CabecerasPedido;
import com.herprogramacion.CPViajes.sqlite.ContratoPedidos.Clientes;
import com.herprogramacion.CPViajes.sqlite.ContratoPedidos.DetallesPedido;
import com.herprogramacion.CPViajes.sqlite.ContratoPedidos.FormasPago;
import com.herprogramacion.CPViajes.sqlite.ContratoPedidos.Productos;

import java.util.ArrayList;
import java.util.Calendar;

public class ActividadListaPedidos extends AppCompatActivity {

    public class TareaPruebaDatos extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            ContentResolver r = getContentResolver();

            // Lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            // [INSERCIONES]
            String fechaActual = Calendar.getInstance().getTime().toString();

            // Inserción Clientes
            String cliente1 = Clientes.generarIdCliente();
            String cliente2 = Clientes.generarIdCliente();
            ops.add(ContentProviderOperation.newInsert(Clientes.URI_CONTENIDO)
                    .withValue(Clientes.ID, cliente1)
                    .withValue(Clientes.NOMBRES, "Veronica")
                    .withValue(Clientes.APELLIDOS, "Del Topo")
                    .withValue(Clientes.TELEFONO, "4552000")
                    .build());
            ops.add(ContentProviderOperation.newInsert(Clientes.URI_CONTENIDO)
                    .withValue(Clientes.ID, cliente2)
                    .withValue(Clientes.NOMBRES, "Carlos")
                    .withValue(Clientes.APELLIDOS, "Villagran")
                    .withValue(Clientes.TELEFONO, "4440000")
                    .build());


            // Inserción Formas de pago
            String formaPago1 = FormasPago.generarIdFormaPago();
            String formaPago2 = FormasPago.generarIdFormaPago();
            ops.add(ContentProviderOperation.newInsert(FormasPago.URI_CONTENIDO)
                    .withValue(FormasPago.ID, formaPago1)
                    .withValue(FormasPago.NOMBRE, "Efectivo")
                    .build());
            ops.add(ContentProviderOperation.newInsert(FormasPago.URI_CONTENIDO)
                    .withValue(FormasPago.ID, formaPago2)
                    .withValue(FormasPago.NOMBRE, "Crédito")
                    .build());

            // Inserción Productos
            String producto1 = Productos.generarIdProducto();
            String producto2 = Productos.generarIdProducto();
            String producto3 = Productos.generarIdProducto();
            String producto4 = Productos.generarIdProducto();
            ops.add(ContentProviderOperation.newInsert(Productos.URI_CONTENIDO)
                    .withValue(Productos.ID, producto1)
                    .withValue(Productos.NOMBRE, "Manzana unidad")
                    .withValue(Productos.PRECIO, 2)
                    .withValue(Productos.EXISTENCIAS, 100)
                    .build());
            ops.add(ContentProviderOperation.newInsert(Productos.URI_CONTENIDO)
                    .withValue(Productos.ID, producto2)
                    .withValue(Productos.NOMBRE, "Pera unidad")
                    .withValue(Productos.PRECIO, 3)
                    .withValue(Productos.EXISTENCIAS, 230)
                    .build());
            ops.add(ContentProviderOperation.newInsert(Productos.URI_CONTENIDO)
                    .withValue(Productos.ID, producto3)
                    .withValue(Productos.NOMBRE, "Guayaba unidad")
                    .withValue(Productos.PRECIO, 5)
                    .withValue(Productos.EXISTENCIAS, 55)
                    .build());
            ops.add(ContentProviderOperation.newInsert(Productos.URI_CONTENIDO)
                    .withValue(Productos.ID, producto4)
                    .withValue(Productos.NOMBRE, "Maní unidad")
                    .withValue(Productos.PRECIO, 3.6f)
                    .withValue(Productos.EXISTENCIAS, 60)
                    .build());

            // Inserción Pedidos
            String pedido1 = CabecerasPedido.generarIdCabeceraPedido();
            String pedido2 = CabecerasPedido.generarIdCabeceraPedido();
            ops.add(ContentProviderOperation.newInsert(CabecerasPedido.URI_CONTENIDO)
                    .withValue(CabecerasPedido.ID, pedido1)
                    .withValue(CabecerasPedido.FECHA, fechaActual)
                    .withValue(CabecerasPedido.ID_CLIENTE, cliente1)
                    .withValue(CabecerasPedido.ID_FORMA_PAGO, formaPago1)
                    .build());
            ops.add(ContentProviderOperation.newInsert(CabecerasPedido.URI_CONTENIDO)
                    .withValue(CabecerasPedido.ID, pedido2)
                    .withValue(CabecerasPedido.FECHA, fechaActual)
                    .withValue(CabecerasPedido.ID_CLIENTE, cliente2)
                    .withValue(CabecerasPedido.ID_FORMA_PAGO, formaPago2)
                    .build());

            // Inserción Detalles
            Uri uriParaDetalles = CabecerasPedido.crearUriParaDetalles(pedido1);
            ops.add(ContentProviderOperation.newInsert(uriParaDetalles)
                    .withValue(DetallesPedido.SECUENCIA, 1)
                    .withValue(DetallesPedido.ID_PRODUCTO, producto1)
                    .withValue(DetallesPedido.CANTIDAD, 5)
                    .withValue(DetallesPedido.PRECIO, 2)
                    .build());
            ops.add(ContentProviderOperation.newInsert(uriParaDetalles)
                    .withValue(DetallesPedido.SECUENCIA, 2)
                    .withValue(DetallesPedido.ID_PRODUCTO, producto1)
                    .withValue(DetallesPedido.CANTIDAD, 10)
                    .withValue(DetallesPedido.PRECIO, 3)
                    .build());

            uriParaDetalles = CabecerasPedido.crearUriParaDetalles(pedido2);
            ops.add(ContentProviderOperation.newInsert(uriParaDetalles)
                    .withValue(DetallesPedido.SECUENCIA, 1)
                    .withValue(DetallesPedido.ID_PRODUCTO, producto1)
                    .withValue(DetallesPedido.CANTIDAD, 30)
                    .withValue(DetallesPedido.PRECIO, 5)
                    .build());
            ops.add(ContentProviderOperation.newInsert(uriParaDetalles)
                    .withValue(DetallesPedido.SECUENCIA, 2)
                    .withValue(DetallesPedido.ID_PRODUCTO, producto1)
                    .withValue(DetallesPedido.CANTIDAD, 20)
                    .withValue(DetallesPedido.PRECIO, 3.6f)
                    .build());

            // Eliminación Pedido
            ops.add(ContentProviderOperation
                    .newDelete(CabecerasPedido.crearUriCabeceraPedido(pedido1))
                    .build());

            // Actualización Cliente
            ops.add(ContentProviderOperation.newUpdate(Clientes.crearUriCliente(cliente2))
                    .withValue(Clientes.ID, cliente2)
                    .withValue(Clientes.NOMBRES, "Carlos Alberto")
                    .withValue(Clientes.APELLIDOS, "Villagran")
                    .withValue(Clientes.TELEFONO, "3333333")
                    .build());

            try {
                r.applyBatch(ContratoPedidos.AUTORIDAD, ops);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }

            // [QUERIES]
            Log.d("Clientes", "Clientes");
            DatabaseUtils.dumpCursor(r.query(Clientes.URI_CONTENIDO, null, null, null, null));
            Log.d("Formas de pago", "Formas de pago");
            DatabaseUtils.dumpCursor(r.query(FormasPago.URI_CONTENIDO, null, null, null, null));
            Log.d("Productos", "Productos");
            DatabaseUtils.dumpCursor(r.query(Productos.URI_CONTENIDO, null, null, null, null));
            Log.d("Cabeceras de pedido", "Cabeceras de pedido");
            DatabaseUtils.dumpCursor(r.query(CabecerasPedido.URI_CONTENIDO, null, null, null, null));
            Log.d("Detalles de pedido", "Detalles del pedido #1");
            DatabaseUtils.dumpCursor(r.query(CabecerasPedido.crearUriParaDetalles(pedido1),
                    null, null, null, null));
            Log.d("Detalles de pedido", "Detalles del pedido #2");
            DatabaseUtils.dumpCursor(r.query(CabecerasPedido.crearUriParaDetalles(pedido2),
                    null, null, null, null));

            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_lista_pedidos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getApplicationContext().deleteDatabase("pedidos.db");

        new TareaPruebaDatos().execute();
    }

}