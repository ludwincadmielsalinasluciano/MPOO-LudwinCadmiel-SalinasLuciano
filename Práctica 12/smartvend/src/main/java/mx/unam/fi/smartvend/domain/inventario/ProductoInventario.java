package mx.unam.fi.smartvend.domain.inventario;

import mx.unam.fi.smartvend.domain.producto.Producto;
import java.util.Objects;

public class ProductoInventario {
    private final Producto producto;
    private double precio;
    private int existencias;

    public ProductoInventario(Producto producto, double precio, int existenciasIniciales) {
        this.producto = Objects.requireNonNull(producto, "El producto no puede ser nulo.");
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser estrictamente mayor a 0.");
        }
        if (existenciasIniciales < 0) {
            throw new IllegalArgumentException("Las existencias iniciales no pueden ser negativas.");
        }
        this.precio = precio;
        this.existencias = existenciasIniciales;
    }

    public Producto getProducto() { return producto; }
    public double getPrecio() { return precio; }
    public int getExistencias() { return existencias; }

    public void agregarExistencias(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a reabastecer debe ser mayor a cero.");
        }
        this.existencias += cantidad;
    }

    // Visibilidad protegida de dominio: solo el inventario coordina el descuento
    public void descontarExistencia() {
        if (this.existencias <= 0) {
            throw new IllegalStateException("No hay existencias disponibles para despachar.");
        }
        this.existencias--;
    }
}
