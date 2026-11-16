package mx.unam.fi.smartvend.api.dto;

import mx.unam.fi.smartvend.domain.pago.MetodoPago;

public record CompraRequest(
    String codigoProducto,
    MetodoPago metodoPago,
    double cantidadRecibida
) {}
