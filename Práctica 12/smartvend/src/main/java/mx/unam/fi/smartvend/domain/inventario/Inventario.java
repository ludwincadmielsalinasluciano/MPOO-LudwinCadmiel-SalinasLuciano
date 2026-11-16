package mx.unam.fi.smartvend.domain.inventario;

import mx.unam.fi.smartvend.domain.producto.Producto;
import java.util.Optional;

public interface Inventario {
    void registrarProducto(Producto producto, double precio);
    Optional<ProductoInventario> buscarProducto(String codigo);
    void reabastecer(String codigo, int cantidad);
    void despachar(String codigo);
}
