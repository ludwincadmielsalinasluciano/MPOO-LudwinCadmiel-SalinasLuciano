public class CuentaBancariaService {
    private CuentaBancaria[] cuentas;

    public CuentaBancariaService() {
        this.cuentas = new CuentaBancaria[] {
            new CuentaBancaria("CTA-101", "Carlos Mendoza", 5000.0, true),
            new CuentaBancaria("CTA-102", "Valeria Gomez", 2500.0, true),
            new CuentaBancaria("CTA-103", "Alejandro Rios", 100.0, false),
            new CuentaBancaria("CTA-104", "Mariana Sosa", 12000.0, true)
        };
    }

    private CuentaBancaria buscarCuenta(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.trim().isEmpty()) return null;
        for (CuentaBancaria c : cuentas) {
            if (c != null && c.getNumeroCuenta().equalsIgnoreCase(numeroCuenta.trim())) {
                return c;
            }
        }
        return null;
    }

    public boolean transferir(String cuentaOrigen, String cuentaDestino, double cantidad) {
        if (cuentaOrigen == null || cuentaDestino == null || cuentaOrigen.equalsIgnoreCase(cuentaDestino)) return false;
        CuentaBancaria origen = buscarCuenta(cuentaOrigen);
        CuentaBancaria destino = buscarCuenta(cuentaDestino);
        
        if (origen == null || destino == null) return false;
        if (!origen.retirar(cantidad)) return false;
        if (!destino.depositar(cantidad)) {
            origen.depositar(cantidad);
            return false;
        }
        return true;
    }

    public boolean depositar(String cuentaDestino, double cantidad) {
        CuentaBancaria destino = buscarCuenta(cuentaDestino);
        if (destino == null) return false;
        return destino.depositar(cantidad);
    }

    public boolean retirar(String cuentaOrigen, double cantidad) {
        CuentaBancaria origen = buscarCuenta(cuentaOrigen);
        if (origen == null) return false;
        return origen.retirar(cantidad);
    }
}