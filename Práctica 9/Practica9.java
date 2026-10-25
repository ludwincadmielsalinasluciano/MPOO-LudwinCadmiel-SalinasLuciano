import java.time.LocalDate;
import java.util.Optional;

/* =========================================================
   ENUMS Y DTOs
   ========================================================= */
enum CategoriaProducto { BEBIDA, SNACK }
enum ResultadoRegistro { PRODUCTO_REGISTRADO, CODIGO_DUPLICADO, PRODUCTO_INVALIDO, PRECIO_INVALIDO }
enum ResultadoReabastecimiento { REABASTECIMIENTO_REALIZADO, PRODUCTO_NO_ENCONTRADO, CANTIDAD_INVALIDA }
enum ResultadoEntrega { PRODUCTO_PREPARADO, PRODUCTO_NO_ENCONTRADO, SIN_EXISTENCIAS }
enum EstadoCobro { COBRO_REALIZADO, MONTO_INVALIDO, SIN_FONDOS_SUFICIENTES }
enum EstadoCompra { VENTA_REALIZADA, PRODUCTO_NO_ENCONTRADO, SIN_EXISTENCIAS, PAGO_INSUFICIENTE, SIN_FONDOS_SUFICIENTES }

class ProductoInventario {
    private final Producto producto;
    private final double precio;
    private final int existencias;
    public ProductoInventario(Producto producto, double precio, int existencias) {
        this.producto = producto; this.precio = precio; this.existencias = existencias;
    }
    public Producto getProducto() { return producto; }
    public double getPrecio() { return precio; }
    public int getExistencias() { return existencias; }
}

class ResultadoCobro {
    private final EstadoCobro estado;
    private final double cambio;
    public ResultadoCobro(EstadoCobro estado, double cambio) {
        this.estado = estado; this.cambio = cambio;
    }
    public EstadoCobro getEstado() { return estado; }
    public double getCambio() { return cambio; }
}

class ResultadoCompra {
    private final EstadoCompra estado;
    private final Producto producto;
    private final double cambio;
    public ResultadoCompra(EstadoCompra estado, Producto producto, double cambio) {
        this.estado = estado; this.producto = producto; this.cambio = cambio;
    }
    public EstadoCompra getEstado() { return estado; }
    public Producto getProducto() { return producto; }
    public double getCambio() { return cambio; }
}

class RegistroProductoDTO {
    private final String codigo;
    private final String nombre;
    private final LocalDate fechaCaducidad;
    private final CategoriaProducto categoria;
    private final double precio;
    public RegistroProductoDTO(String codigo, String nombre, LocalDate fechaCaducidad, CategoriaProducto categoria, double precio) {
        this.codigo = codigo; this.nombre = nombre; this.fechaCaducidad = fechaCaducidad; this.categoria = categoria; this.precio = precio;
    }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public CategoriaProducto getCategoria() { return categoria; }
    public double getPrecio() { return precio; }
}

class ReabastecimientoDTO {
    private final String codigo;
    private final int cantidad;
    public ReabastecimientoDTO(String codigo, int cantidad) {
        this.codigo = codigo; this.cantidad = cantidad;
    }
    public String getCodigo() { return codigo; }
    public int getCantidad() { return cantidad; }
}

class CompraDTO {
    private final String codigoProducto;
    private final double cantidadRecibida;
    public CompraDTO(String codigoProducto, double cantidadRecibida) {
        this.codigoProducto = codigoProducto; this.cantidadRecibida = cantidadRecibida;
    }
    public String getCodigoProducto() { return codigoProducto; }
    public double getCantidadRecibida() { return cantidadRecibida; }
}

/* =========================================================
   INTERFACES
   ========================================================= */
interface Inventario {
    ResultadoRegistro registrarProducto(Producto producto, double precio);
    Optional<ProductoInventario> buscarProducto(String codigo);
    ResultadoReabastecimiento reabastecer(String codigo, int cantidad);
    ResultadoEntrega despachar(String codigo);
}

interface Monedero {
    ResultadoCobro cobrar(double precio, double cantidadRecibida);
    double consultarFondos();
}

interface MaquinaDespachadora {
    ResultadoRegistro registrarProducto(RegistroProductoDTO producto);
    Optional<ProductoInventario> buscarProducto(String codigo);
    ResultadoReabastecimiento reabastecer(ReabastecimientoDTO solicitud);
    ResultadoCompra comprar(CompraDTO solicitud);
    double consultarFondos();
}

/* =========================================================
   IMPLEMENTACIONES
   ========================================================= */
class Producto {
    private final String codigo;
    private final String nombre;
    private final LocalDate fechaCaducidad;
    private final CategoriaProducto categoria;
    public Producto(String codigo, String nombre, LocalDate fechaCaducidad, CategoriaProducto categoria) {
        this.codigo = codigo; this.nombre = nombre; this.fechaCaducidad = fechaCaducidad; this.categoria = categoria;
    }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public CategoriaProducto getCategoria() { return categoria; }
}

class InventarioImpl implements Inventario {
    private final java.util.Map<String, ProductoInventario> productos = new java.util.HashMap<>();
    @Override
    public ResultadoRegistro registrarProducto(Producto producto, double precio) {
        if (producto == null || producto.getCodigo() == null) return ResultadoRegistro.PRODUCTO_INVALIDO;
        if (precio < 0) return ResultadoRegistro.PRECIO_INVALIDO;
        if (productos.containsKey(producto.getCodigo())) return ResultadoRegistro.CODIGO_DUPLICADO;
        productos.put(producto.getCodigo(), new ProductoInventario(producto, precio, 0));
        return ResultadoRegistro.PRODUCTO_REGISTRADO;
    }
    @Override
    public Optional<ProductoInventario> buscarProducto(String codigo) {
        return Optional.ofNullable(productos.get(codigo));
    }
    @Override
    public ResultadoReabastecimiento reabastecer(String codigo, int cantidad) {
        if (cantidad <= 0) return ResultadoReabastecimiento.CANTIDAD_INVALIDA;
        ProductoInventario actual = productos.get(codigo);
        if (actual == null) return ResultadoReabastecimiento.PRODUCTO_NO_ENCONTRADO;
        productos.put(codigo, new ProductoInventario(actual.getProducto(), actual.getPrecio(), actual.getExistencias() + cantidad));
        return ResultadoReabastecimiento.REABASTECIMIENTO_REALIZADO;
    }
    @Override
    public ResultadoEntrega despachar(String codigo) {
        ProductoInventario actual = productos.get(codigo);
        if (actual == null) return ResultadoEntrega.PRODUCTO_NO_ENCONTRADO;
        if (actual.getExistencias() <= 0) return ResultadoEntrega.SIN_EXISTENCIAS;
        productos.put(codigo, new ProductoInventario(actual.getProducto(), actual.getPrecio(), actual.getExistencias() - 1));
        return ResultadoEntrega.PRODUCTO_PREPARADO;
    }
}

class MonederoImpl implements Monedero {
    private double fondos;
    public MonederoImpl(double fondosIniciales) {
        this.fondos = Math.max(fondosIniciales, 0.0);
    }
    @Override
    public ResultadoCobro cobrar(double precio, double cantidadRecibida) {
        if (precio < 0 || cantidadRecibida < 0) return new ResultadoCobro(EstadoCobro.MONTO_INVALIDO, cantidadRecibida);
        if (cantidadRecibida < precio) return new ResultadoCobro(EstadoCobro.MONTO_INVALIDO, cantidadRecibida);
        double cambio = Math.round((cantidadRecibida - precio) * 100.0) / 100.0;
        double fondosActuales = Math.round(this.fondos * 100.0) / 100.0;
        if (cambio > fondosActuales) return new ResultadoCobro(EstadoCobro.SIN_FONDOS_SUFICIENTES, cantidadRecibida);
        this.fondos = Math.round((this.fondos + precio) * 100.0) / 100.0;
        return new ResultadoCobro(EstadoCobro.COBRO_REALIZADO, cambio);
    }
    @Override
    public double consultarFondos() { return fondos; }
}

class MaquinaExpendedora implements MaquinaDespachadora {
    private final Inventario inventario;
    private final Monedero monedero;
    private MaquinaExpendedora(Builder builder) {
        this.inventario = builder.inventario;
        this.monedero = builder.monedero;
    }
    @Override
    public ResultadoRegistro registrarProducto(RegistroProductoDTO dto) {
        Producto producto = new Producto(dto.getCodigo(), dto.getNombre(), dto.getFechaCaducidad(), dto.getCategoria());
        return inventario.registrarProducto(producto, dto.getPrecio());
    }
    @Override
    public Optional<ProductoInventario> buscarProducto(String codigo) {
        return inventario.buscarProducto(codigo);
    }
    @Override
    public ResultadoReabastecimiento reabastecer(ReabastecimientoDTO solicitud) {
        return inventario.reabastecer(solicitud.getCodigo(), solicitud.getCantidad());
    }
    @Override
    public ResultadoCompra comprar(CompraDTO solicitud) {
        Optional<ProductoInventario> opt = inventario.buscarProducto(solicitud.getCodigoProducto());
        if (opt.isEmpty()) return new ResultadoCompra(EstadoCompra.PRODUCTO_NO_ENCONTRADO, null, solicitud.getCantidadRecibida());
        ProductoInventario pi = opt.get();
        if (pi.getExistencias() <= 0) return new ResultadoCompra(EstadoCompra.SIN_EXISTENCIAS, pi.getProducto(), solicitud.getCantidadRecibida());
        if (solicitud.getCantidadRecibida() < pi.getPrecio()) return new ResultadoCompra(EstadoCompra.PAGO_INSUFICIENTE, pi.getProducto(), solicitud.getCantidadRecibida());
        ResultadoCobro resCobro = monedero.cobrar(pi.getPrecio(), solicitud.getCantidadRecibida());
        if (resCobro.getEstado() == EstadoCobro.SIN_FONDOS_SUFICIENTES) return new ResultadoCompra(EstadoCompra.SIN_FONDOS_SUFICIENTES, pi.getProducto(), solicitud.getCantidadRecibida());
        if (resCobro.getEstado() == EstadoCobro.COBRO_REALIZADO) {
            inventario.despachar(solicitud.getCodigoProducto());
            return new ResultadoCompra(EstadoCompra.VENTA_REALIZADA, pi.getProducto(), resCobro.getCambio());
        }
        return new ResultadoCompra(EstadoCompra.PAGO_INSUFICIENTE, pi.getProducto(), solicitud.getCantidadRecibida());
    }
    @Override
    public double consultarFondos() { return monedero.consultarFondos(); }

    public static class Builder {
        private Inventario inventario;
        private Monedero monedero;
        public Builder inventario(Inventario inventario) { this.inventario = inventario; return this; }
        public Builder monedero(Monedero monedero) { this.monedero = monedero; return this; }
        public MaquinaExpendedora build() {
            if (inventario == null || monedero == null) throw new IllegalStateException("El inventario y monedero son obligatorios");
            return new MaquinaExpendedora(this);
        }
    }
}

/* =========================================================
   CLASE PRINCIPAL (MAIN) PARA GENERAR EVIDENCIAS
   ========================================================= */
public class Practica9 {
    public static void main(String[] args) {
        System.out.println("=== INICIANDO SMARTVEND ===\n");

        // 1. Construir la maquina con el Builder (Fondos iniciales: $50.0)
        Inventario inventario = new InventarioImpl();
        Monedero monedero = new MonederoImpl(50.0);
        MaquinaDespachadora maquina = new MaquinaExpendedora.Builder()
                .inventario(inventario)
                .monedero(monedero)
                .build();

        // 2. Registrar y reabastecer productos
        System.out.println("--- REGISTRO Y REABASTECIMIENTO ---");
        RegistroProductoDTO cocaCola = new RegistroProductoDTO("B01", "Coca Cola", LocalDate.of(2025, 12, 31), CategoriaProducto.BEBIDA, 18.50);
        RegistroProductoDTO papas = new RegistroProductoDTO("S01", "Papas Sabritas", LocalDate.of(2024, 6, 15), CategoriaProducto.SNACK, 20.00);
        
        System.out.println("Registro Coca Cola: " + maquina.registrarProducto(cocaCola));
        System.out.println("Registro Papas: " + maquina.registrarProducto(papas));
        
        System.out.println("Reabastecer Coca Cola (5 uds): " + maquina.reabastecer(new ReabastecimientoDTO("B01", 5)));
        System.out.println("Reabastecer Papas (1 ud): " + maquina.reabastecer(new ReabastecimientoDTO("S01", 1)));
        System.out.println("Fondos iniciales de la maquina: $" + maquina.consultarFondos() + "\n");

        // 3. EVIDENCIA: COMPRA CORRECTA
        System.out.println("--- EVIDENCIA: COMPRA CORRECTA ---");
        System.out.println("Intentando comprar 'Coca Cola' ($18.50) pagando con $20.00...");
        ResultadoCompra compraExitosa = maquina.comprar(new CompraDTO("B01", 20.00));
        System.out.println("Estado de compra: " + compraExitosa.getEstado());
        System.out.println("Producto despachado: " + compraExitosa.getProducto().getNombre());
        System.out.println("Cambio devuelto: $" + compraExitosa.getCambio());
        System.out.println("Fondos en la maquina tras la venta: $" + maquina.consultarFondos() + "\n");

        // 4. EVIDENCIA: COMPRA RECHAZADA (Por pago insuficiente)
        System.out.println("--- EVIDENCIA: COMPRA RECHAZADA (PAGO INSUFICIENTE) ---");
        System.out.println("Intentando comprar 'Papas Sabritas' ($20.00) pagando con $10.00...");
        ResultadoCompra compraPagoInsuficiente = maquina.comprar(new CompraDTO("S01", 10.00));
        System.out.println("Estado de compra: " + compraPagoInsuficiente.getEstado());
        System.out.println("Cambio devuelto (dinero intacto): $" + compraPagoInsuficiente.getCambio() + "\n");

        // 5. EVIDENCIA: COMPRA RECHAZADA (Sin existencias)
        System.out.println("--- EVIDENCIA: COMPRA RECHAZADA (SIN EXISTENCIAS) ---");
        System.out.println("Alguien mas compra las unicas papas...");
        maquina.comprar(new CompraDTO("S01", 20.00)); // Vaciamos el stock de papas
        
        System.out.println("Intentando comprar 'Papas Sabritas' nuevamente pagando con $50.00...");
        ResultadoCompra compraSinStock = maquina.comprar(new CompraDTO("S01", 50.00));
        System.out.println("Estado de compra: " + compraSinStock.getEstado());
        System.out.println("Cambio devuelto (dinero intacto): $" + compraSinStock.getCambio() + "\n");
    }
}