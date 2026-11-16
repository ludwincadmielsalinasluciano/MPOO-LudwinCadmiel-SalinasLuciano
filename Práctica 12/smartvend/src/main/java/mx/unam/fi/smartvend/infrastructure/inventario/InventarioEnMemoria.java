package mx.unam.fi.smartvend.infrastructure.inventario;

import mx.unam.fi.smartvend.domain.inventario.Inventario;
import mx.unam.fi.smartvend.domain.inventario.ProductoInventario;
import mx.unam.fi.smartvend.domain.producto.Producto;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InventarioEnMemoria implements Inventario {
    private final Map<String, ProductoInventario> catalogo = new ConcurrentHashMap<>();

    @Override
    public void registrarProducto(Producto producto, double precio) {
        String codigo = producto.getCodigo();
        if (catalogo.containsKey(codigo)) {
            throw new IllegalArgumentException("Ya existe un producto registrado con el código: " + codigo);
        }
        ProductoInventario nuevoRegistro = new ProductoInventario(producto, precio, 0);
        catalogo.put(codigo, nuevoRegistro);
    }

    @Override
    public Optional<ProductoInventario> buscarProducto(String codigo) {
        return Optional.ofNullable(catalogo.get(codigo));
    }

    @Override
    public void reabastecer(String codigo, int cantidad) {
        ProductoInventario item = catalogo.get(codigo);
        if (item == null) {
            throw new IllegalArgumentException("Producto no encontrado: " + codigo);
        }
        item.agregarExistencias(cantidad);
    }

    @Override
    public void despachar(String codigo) {
        ProductoInventario item = catalogo.get(codigo);
        if (item == null) {
            throw new IllegalArgumentException("Producto no encontrado: " + codigo);
        }
        item.descontarExistencia();
    }
}
