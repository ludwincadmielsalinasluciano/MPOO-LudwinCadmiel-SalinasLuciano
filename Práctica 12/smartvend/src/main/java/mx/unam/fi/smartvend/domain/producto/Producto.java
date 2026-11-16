package mx.unam.fi.smartvend.domain.producto;

import java.time.LocalDate;
import java.util.Objects;

public class Producto {
    private final String tipo;
    private final String codigo;
    private final String nombre;
    private final LocalDate fechaCaducidad;
    private final CategoriaProducto categoria;

    public Producto(String tipo, String codigo, String nombre, LocalDate fechaCaducidad, CategoriaProducto categoria) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del producto no puede estar vacío.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        this.tipo = (tipo == null || tipo.isBlank()) ? "GENERAL" : tipo.trim();
        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.fechaCaducidad = Objects.requireNonNull(fechaCaducidad, "La fecha de caducidad es obligatoria.");
        this.categoria = Objects.requireNonNull(categoria, "La categoría es obligatoria.");
    }

    public String getTipo() { return tipo; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public CategoriaProducto getCategoria() { return categoria; }
}
