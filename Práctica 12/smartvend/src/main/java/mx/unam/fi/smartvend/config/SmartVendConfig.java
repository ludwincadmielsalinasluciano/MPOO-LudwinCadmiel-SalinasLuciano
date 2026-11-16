package mx.unam.fi.smartvend.config;

import mx.unam.fi.smartvend.domain.pago.MetodoPago;
import mx.unam.fi.smartvend.domain.pago.Monedero;
import mx.unam.fi.smartvend.infrastructure.pago.MonederoEfectivo;
import mx.unam.fi.smartvend.infrastructure.pago.MonederoTarjeta;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class SmartVendConfig {

    @Bean("monederoEfectivo")
    public Monedero monederoEfectivo() {
        // Inicializado con $500.00 de fondo base para cambio
        return new MonederoEfectivo(500.00);
    }

    @Bean("monederoTarjeta")
    public Monedero monederoTarjeta() {
        // Inicializado en $0.00 para acumular cobros electrónicos (o configurable)
        return new MonederoTarjeta(0.00);
    }

    @Bean
    public Map<MetodoPago, Monedero> registroMonederos(
            @Qualifier("monederoEfectivo") Monedero efectivo,
            @Qualifier("monederoTarjeta") Monedero tarjeta) {
        Map<MetodoPago, Monedero> mapa = new EnumMap<>(MetodoPago.class);
        mapa.put(MetodoPago.EFECTIVO, efectivo);
        mapa.put(MetodoPago.TARJETA, tarjeta);
        return mapa;
    }
}
