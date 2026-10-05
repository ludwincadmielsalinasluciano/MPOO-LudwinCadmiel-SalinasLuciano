import java.io.*;
import java.util.*;

/* =====================================================
   ENUMERACIONES BASE
   ===================================================== */

enum AccionPedido {
    AGREGAR,
    CONFIRMAR
}

enum TipoDescuento {
    REGULAR,
    FRECUENTE,
    MAYOREO
}

enum EstadoAgregarProducto {
    AGREGADO,
    PRODUCTO_NO_EXISTE,
    CANTIDAD_INVALIDA
}

/* =====================================================
   DTO DE PRODUCTO
   ===================================================== */

final class ProductoDTO {
    private final String id;
    private final String nombre;
    private final String precio;

    public ProductoDTO(String id, String nombre, String precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getPrecio() { return precio; }
}

/* =====================================================
   DTO DE DETALLE DEL PEDIDO
   ===================================================== */

final class DetallePedidoDTO {
    private final int idProducto;
    private final String nombre;
    private final int cantidad;

    public DetallePedidoDTO(int idProducto, String nombre, int cantidad) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public int getIdProducto() { return idProducto; }
    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
}

/* =====================================================
   DTO DEL RESUMEN DEL PEDIDO
   ===================================================== */

final class ResumenPedidoDTO {
    private final List<DetallePedidoDTO> detalles;
    private final double subtotal;
    private final double descuento;
    private final double total;

    public ResumenPedidoDTO(
            List<DetallePedidoDTO> detalles,
            double subtotal,
            double descuento,
            double total) {
        this.detalles = detalles;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
    }

    public List<DetallePedidoDTO> getDetalles() { return detalles; }
    public double getSubtotal() { return subtotal; }
    public double getDescuento() { return descuento; }
    public double getTotal() { return total; }
}

/* =====================================================
   STRATEGY
   ===================================================== */

interface DescuentoStrategy {
    double calcularDescuento(double subtotal);
}

/* =====================================================
   CONTRATO PARA OBJETOS CON DESCUENTO
   ===================================================== */

interface Discountable {
    void setDiscount(DescuentoStrategy descuentoStrategy);
}

/* =====================================================
   REPOSITORIO DE PRODUCTOS
   ===================================================== */

interface ProductoRepository {
    boolean guardar(ProductoDTO producto);
    Producto buscarPorId(int idProducto);
    boolean existe(int idProducto);
}

/* =====================================================
   SERVICIO DE PEDIDOS
   ===================================================== */

interface PedidoService {
    EstadoAgregarProducto agregarProducto(Pedido pedido, int idProducto, int cantidad);
    DescuentoStrategy SelectorDescuento(TipoDescuento tipoDescuento);
    ResumenPedidoDTO confirmarPedido(Pedido pedido, TipoDescuento tipoDescuento);
}

/* =====================================================
   IMPLEMENTACION DEL ALUMNO
   ===================================================== */

class Producto {
    private final int id;
    private final String nombre;
    private final double precio;

    public Producto(int id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
}

class ElementoPedido {
    private final Producto producto;
    private int cantidad;

    public ElementoPedido(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    
    public void agregarCantidad(int extra) {
        this.cantidad += extra;
    }

    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }
}

class Pedido implements Discountable {
    private final Map<Integer, ElementoPedido> elementos = new LinkedHashMap<>();
    private DescuentoStrategy estrategiaDescuento;

    private Pedido() {}

    public static Pedido empty() {
        return new Pedido();
    }

    @Override
    public void setDiscount(DescuentoStrategy descuentoStrategy) {
        this.estrategiaDescuento = descuentoStrategy;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        if (elementos.containsKey(producto.getId())) {
            elementos.get(producto.getId()).agregarCantidad(cantidad);
        } else {
            elementos.put(producto.getId(), new ElementoPedido(producto, cantidad));
        }
    }

    public List<ElementoPedido> getElementos() {
        return new ArrayList<>(elementos.values());
    }

    public double getSubtotal() {
        double subtotal = 0.0;
        for (ElementoPedido elemento : elementos.values()) {
            subtotal += elemento.getSubtotal();
        }
        return subtotal;
    }

    public double getDescuento() {
        if (estrategiaDescuento == null) return 0.0;
        return estrategiaDescuento.calcularDescuento(getSubtotal());
    }

    public double getTotal() {
        return getSubtotal() - getDescuento();
    }
}

class DescuentoRegular implements DescuentoStrategy {
    @Override
    public double calcularDescuento(double subtotal) {
        return 0.0;
    }
}

class DescuentoFrecuente implements DescuentoStrategy {
    @Override
    public double calcularDescuento(double subtotal) {
        return subtotal * 0.10;
    }
}

class DescuentoMayoreo implements DescuentoStrategy {
    @Override
    public double calcularDescuento(double subtotal) {
        if (subtotal >= 5000.00) {
            return subtotal * 0.15;
        }
        return 0.0;
    }
}

class ProductoRepositoryImpl implements ProductoRepository {
    private static final Map<Integer, Producto> baseDatos = new HashMap<>();

    public ProductoRepositoryImpl() {}

    @Override
    public boolean guardar(ProductoDTO productoDTO) {
        int id = Integer.parseInt(productoDTO.getId());
        double precio = Double.parseDouble(productoDTO.getPrecio());
        Producto producto = new Producto(id, productoDTO.getNombre(), precio);
        
        baseDatos.put(id, producto);
        return true;
    }

    @Override
    public Producto buscarPorId(int idProducto) {
        return baseDatos.get(idProducto);
    }

    @Override
    public boolean existe(int idProducto) {
        return baseDatos.containsKey(idProducto);
    }
}

class PedidoServiceImpl implements PedidoService {
    private final ProductoRepository productoRepository;

    public PedidoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public EstadoAgregarProducto agregarProducto(Pedido pedido, int idProducto, int cantidad) {
        if (!productoRepository.existe(idProducto)) {
            return EstadoAgregarProducto.PRODUCTO_NO_EXISTE;
        }
        if (cantidad <= 0) {
            return EstadoAgregarProducto.CANTIDAD_INVALIDA;
        }
        
        Producto producto = productoRepository.buscarPorId(idProducto);
        pedido.agregarProducto(producto, cantidad);
        
        return EstadoAgregarProducto.AGREGADO;
    }

    @Override
    public DescuentoStrategy SelectorDescuento(TipoDescuento tipoDescuento) {
        switch (tipoDescuento) {
            case FRECUENTE:
                return new DescuentoFrecuente();
            case MAYOREO:
                return new DescuentoMayoreo();
            case REGULAR:
            default:
                return new DescuentoRegular();
        }
    }

    @Override
    public ResumenPedidoDTO confirmarPedido(Pedido pedido, TipoDescuento tipoDescuento) {
        DescuentoStrategy estrategia = SelectorDescuento(tipoDescuento);
        pedido.setDiscount(estrategia);
        
        List<DetallePedidoDTO> detallesDTO = new ArrayList<>();
        
        for (ElementoPedido ep : pedido.getElementos()) {
            detallesDTO.add(new DetallePedidoDTO(
                    ep.getProducto().getId(),
                    ep.getProducto().getNombre(),
                    ep.getCantidad()
            ));
        }
        
        return new ResumenPedidoDTO(
                detallesDTO,
                pedido.getSubtotal(),
                pedido.getDescuento(),
                pedido.getTotal()
        );
    }
}

/* =====================================================
   MAIN / ENTRY POINT
   ===================================================== */

public class Solution {

    public static void main(String[] args) throws Exception {

        Locale.setDefault(Locale.US);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String productos = br.readLine();
        String operaciones = br.readLine();

        ProductoRepository productoRepository = new ProductoRepositoryImpl();
        PedidoService pedidoService = new PedidoServiceImpl(productoRepository);
        Pedido pedido = Pedido.empty();

        if (productos != null && !productos.isBlank() && !productos.equals("-")) {
            String[] registros = productos.split(";");

            for (String registro : registros) {
                String[] datos = registro.split("\\|", 3);

                ProductoDTO productoDTO = new ProductoDTO(
                        datos[0].trim(),
                        datos[1].trim(),
                        datos[2].trim()
                );
                productoRepository.guardar(productoDTO);
            }
        }

        StringBuilder salida = new StringBuilder();

        if (operaciones != null && !operaciones.isBlank() && !operaciones.equals("-")) {
            String[] listaOperaciones = operaciones.split(";");

            for (String operacion : listaOperaciones) {
                String[] datos = operacion.split("\\|");
                AccionPedido accion = AccionPedido.valueOf(datos[0].trim());

                if (accion == AccionPedido.AGREGAR) {
                    int idProducto = Integer.parseInt(datos[1].trim());
                    int cantidad = Integer.parseInt(datos[2].trim());

                    EstadoAgregarProducto estado = pedidoService.agregarProducto(
                            pedido, idProducto, cantidad
                    );

                    salida.append("AGREGAR ")
                            .append(idProducto)
                            .append(" ")
                            .append(estado)
                            .append("\n");
                } 
                else if (accion == AccionPedido.CONFIRMAR) {
                    TipoDescuento tipoDescuento = TipoDescuento.valueOf(datos[1].trim());

                    ResumenPedidoDTO resumen = pedidoService.confirmarPedido(
                            pedido, tipoDescuento
                    );

                    if (resumen == null) {
                        throw new IllegalStateException("confirmarPedido no debe regresar null");
                    }

                    imprimirResumen(salida, resumen);
                }
            }
        }

        System.out.print(salida.toString());
    }

    private static void imprimirResumen(StringBuilder salida, ResumenPedidoDTO resumen) {
        List<DetallePedidoDTO> detalles = resumen.getDetalles();

        if (detalles == null) {
            throw new IllegalStateException("La lista de detalles no debe ser null");
        }

        for (DetallePedidoDTO detalle : detalles) {
            salida.append("ITEM ")
                    .append(detalle.getIdProducto())
                    .append(" ")
                    .append(detalle.getNombre())
                    .append(" ")
                    .append(detalle.getCantidad())
                    .append("\n");
        }

        salida.append(
                String.format(
                        Locale.US,
                        "RESUMEN %.2f %.2f %.2f",
                        resumen.getSubtotal(),
                        resumen.getDescuento(),
                        resumen.getTotal()
                )
        );
    }
}