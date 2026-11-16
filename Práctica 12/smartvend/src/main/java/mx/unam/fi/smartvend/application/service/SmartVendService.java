package mx.unam.fi.smartvend.application.service;

import mx.unam.fi.smartvend.api.dto.CompraRequest;
import mx.unam.fi.smartvend.api.dto.CompraResponse;
import mx.unam.fi.smartvend.api.dto.FondosResponse;
import mx.unam.fi.smartvend.api.dto.ProductoResponse;
import mx.unam.fi.smartvend.api.dto.RegistroProductoRequest;
import mx.unam.fi.smartvend.domain.inventario.Inventario;
import mx.unam.fi.smartvend.domain.inventario.ProductoInventario;
import mx.unam.fi.smartvend.domain.pago.MetodoPago;
import mx.unam.fi.smartvend.domain.pago.Monedero;
import mx.unam.fi.smartvend.domain.pago.ResultadoCobro;
import mx.unam.fi.smartvend.domain.producto.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class SmartVendService {

    private final Inventario inventario;
    private final Map<MetodoPago, Monedero> monederos;

    @Autowired
    public SmartVendService(Inventario inventario, Map<MetodoPago, Monedero> monederos) {
        this.inventario = inventario;
        this.monederos = monederos;
    }

    public ProductoResponse registrarProducto(RegistroProductoRequest request) {
        Producto nuevoProducto = new Producto(
                request.tipo(),
                request.codigo(),
                request.nombre(),
                request.fechaCaducidad(),
                request.categoria()
        );
        inventario.registrarProducto(nuevoProducto, request.precio());
        return buscarProducto(nuevoProducto.getCodigo());
    }

    public ProductoResponse buscarProducto(String codigo) {
        ProductoInventario item = inventario.buscarProducto(codigo)
                .orElseThrow(() -> new NoSuchElementException("No existe el producto con código: " + codigo));
        Producto p = item.getProducto();
        return new ProductoResponse(
                p.getTipo(),
                p.getCodigo(),
                p.getNombre(),
                p.getFechaCaducidad(),
                p.getCategoria(),
                item.getPrecio(),
                item.getExistencias()
        );
    }

    public ProductoResponse reabastecerProducto(String codigo, int cantidad) {
        if (inventario.buscarProducto(codigo).isEmpty()) {
            throw new NoSuchElementException("No existe el producto con código: " + codigo);
        }
        inventario.reabastecer(codigo, cantidad);
        return buscarProducto(codigo);
    }

    public CompraResponse comprarProducto(String codigoPath, CompraRequest request) {
        if (request.codigoProducto() != null && !request.codigoProducto().equals(codigoPath)) {
            throw new IllegalArgumentException("El código del cuerpo no coincide con el código de la ruta HTTP.");
        }

        ProductoInventario item = inventario.buscarProducto(codigoPath)
                .orElseThrow(() -> new NoSuchElementException("No existe el producto con código: " + codigoPath));

        if (item.getExistencias() <= 0) {
            throw new IllegalStateException("Producto agotado: " + item.getProducto().getNombre());
        }

        Monedero monedero = monederos.get(request.metodoPago());
        if (monedero == null) {
            throw new IllegalArgumentException("Método de pago no soportado: " + request.metodoPago());
        }

        // 1. Intentar cobro primero (preserva el estado válido si el pago falla)
        ResultadoCobro resultado = monedero.cobrar(item.getPrecio(), request.cantidadRecibida());
        if (!resultado.esExitoso()) {
            throw new IllegalArgumentException(resultado.mensaje());
        }

        // 2. Operación interna encapsulada: solo se despacha si el cobro fue exitoso
        inventario.despachar(codigoPath);

        return new CompraResponse(
                item.getProducto().getCodigo(),
                item.getProducto().getNombre(),
                request.metodoPago(),
                resultado.montoCobrado(),
                resultado.cambio(),
                item.getExistencias(),
                resultado.mensaje()
        );
    }

    public FondosResponse consultarFondos() {
        double efectivo = monederos.get(MetodoPago.EFECTIVO).consultarFondos();
        double tarjeta = monederos.get(MetodoPago.TARJETA).consultarFondos();
        double total = Math.round((efectivo + tarjeta) * 100.0) / 100.0;
        return new FondosResponse(efectivo, tarjeta, total);
    }
}
