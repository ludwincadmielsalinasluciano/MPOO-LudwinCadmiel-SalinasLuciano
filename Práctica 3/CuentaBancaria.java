public class CuentaBancaria {
    private String numeroCuenta;
    private String titular;
    private double saldo;
    private boolean activa;

    public CuentaBancaria(String numeroCuenta, String titular, double saldoInicial, boolean activa) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = Math.max(saldoInicial, 0.0);
        this.activa = activa;
    }

    public String getNumeroCuenta() { return numeroCuenta; }
    public String getTitular() { return titular; }
    public double getSaldo() { return saldo; }
    public boolean isActiva() { return activa; }

    public boolean depositar(double cantidad) {
        if (!activa || cantidad <= 0) {
            return false;
        }
        this.saldo += cantidad;
        return true;
    }

    public boolean retirar(double cantidad) {
        if (!activa || cantidad <= 0 || cantidad > this.saldo) {
            return false;
        }
        this.saldo -= cantidad;
        return true;
    }
}