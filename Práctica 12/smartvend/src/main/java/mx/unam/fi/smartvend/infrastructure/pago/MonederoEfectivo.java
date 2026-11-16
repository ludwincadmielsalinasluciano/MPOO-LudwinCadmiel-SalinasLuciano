package mx.unam.fi.smartvend.infrastructure.pago;

import mx.unam.fi.smartvend.domain.pago.EstadoCobro;
import mx.unam.fi.smartvend.domain.pago.Monedero;
import mx.unam.fi.smartvend.domain.pago.ResultadoCobro;

public class MonederoEfectivo implements Monedero {
    private double fondos;

    public MonederoEfectivo(double fondosIniciales) {
        if (fondosIniciales < 0) {
            throw new IllegalArgumentException("Los fondos iniciales no pueden ser negativos.");
        }
        this.fondos = fondosIniciales;
    }

    @Override
    public synchronized ResultadoCobro cobrar(double precio, double cantidadRecibida) {
        if (precio <= 0 || cantidadRecibida <= 0) {
            return new ResultadoCobro(EstadoCobro.MONTO_INVALIDO, 0.0, cantidadRecibida,
                    "El precio y la cantidad recibida deben ser mayores a cero.");
        }
        if (cantidadRecibida < precio) {
            return new ResultadoCobro(EstadoCobro.FONDOS_INSUFICIENTES, 0.0, cantidadRecibida,
                    "Efectivo insuficiente para cubrir el precio del producto.");
        }
        double cambio = Math.round((cantidadRecibida - precio) * 100.0) / 100.0;
        if (cambio > this.fondos) {
            return new ResultadoCobro(EstadoCobro.FONDOS_INSUFICIENTES, 0.0, cantidadRecibida,
                    "La máquina no cuenta con fondos suficientes para dar cambio.");
        }
        this.fondos = Math.round((this.fondos + precio) * 100.0) / 100.0;
        return new ResultadoCobro(EstadoCobro.EXITOSO, precio, cambio, "Pago en efectivo procesado con éxito.");
    }

    @Override
    public synchronized double consultarFondos() {
        return fondos;
    }
}
