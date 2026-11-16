package mx.unam.fi.smartvend.domain.pago;

public record ResultadoCobro(
    EstadoCobro estado,
    double montoCobrado,
    double cambio,
    String mensaje
) {
    public boolean esExitoso() {
        return estado == EstadoCobro.EXITOSO;
    }
}
