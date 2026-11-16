package mx.unam.fi.smartvend.infrastructure.pago;

import mx.unam.fi.smartvend.domain.pago.EstadoCobro;
import mx.unam.fi.smartvend.domain.pago.Monedero;
import mx.unam.fi.smartvend.domain.pago.ResultadoCobro;

public class MonederoTarjeta implements Monedero {
    private double fondos;

    public MonederoTarjeta(double fondosIniciales) {
        if (fondosIniciales < 0) {
            throw new IllegalArgumentException("Los fondos iniciales no pueden ser negativos.");
        }
        this.fondos = fondosIniciales;
    }

    @Override
    public synchronized ResultadoCobro cobrar(double precio, double cantidadRecibida) {
        if (precio <= 0 || cantidadRecibida <= 0) {
            return new ResultadoCobro(EstadoCobro.MONTO_INVALIDO, 0.0, 0.0,
                    "El monto autorizado debe ser mayor a cero.");
        }
        // En pago electrónico no se genera cambio; debe coincidir con el importe exacto
        if (Math.abs(cantidadRecibida - precio) > 0.001) {
            return new ResultadoCobro(EstadoCobro.MONTO_INVALIDO, 0.0, 0.0,
                    "Los pagos con tarjeta no generan cambio y requieren el monto exacto.");
        }
        this.fondos = Math.round((this.fondos + precio) * 100.0) / 100.0;
        return new ResultadoCobro(EstadoCobro.EXITOSO, precio, 0.0, "Pago electrónico con tarjeta aprobado.");
    }

    @Override
    public synchronized double consultarFondos() {
        return fondos;
    }
}
