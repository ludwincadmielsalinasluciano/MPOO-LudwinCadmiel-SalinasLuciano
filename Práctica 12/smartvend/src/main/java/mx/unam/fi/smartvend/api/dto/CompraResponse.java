package mx.unam.fi.smartvend.api.dto;

import mx.unam.fi.smartvend.domain.pago.MetodoPago;

public record CompraResponse(
    String codigoProducto,
    String nombreProducto,
    MetodoPago metodoPago,
    double precioPagado,
    double cambio,
    int existenciasRestantes,
    String mensaje
) {}
