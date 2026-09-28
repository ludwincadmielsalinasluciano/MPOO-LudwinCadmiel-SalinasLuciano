import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Estacionamiento {

    enum Vehiculo {
        MOTOCICLETA(15.0, 100.0),
        AUTOMOVIL(25.0, 180.0),
        CAMIONETA(35.0, 250.0),
        ELECTRICO(20.0, 150.0);

        final double tarifaPorHora;
        final double maximoPorBloque;

        Vehiculo(double tarifaPorHora, double maximoPorBloque) {
            this.tarifaPorHora = tarifaPorHora;
            this.maximoPorBloque = maximoPorBloque;
        }
    }

    enum TipoEstancia {
        NORMAL,
        NOCTURNA,
        FIN_SEMANA,
        MIXTA
    }

    public static String calcularEstancia(String tipoVehiculo, String fechaEntrada, String horaEntrada, String fechaSalida, String horaSalida) {
        String[] fE = fechaEntrada.split("/");
        String[] hE = horaEntrada.split(":");
        Calendar calEntrada = new GregorianCalendar(
            Integer.parseInt(fE[2]),
            Integer.parseInt(fE[1]) - 1,
            Integer.parseInt(fE[0]),
            Integer.parseInt(hE[0]),
            Integer.parseInt(hE[1])
        );

        String[] fS = fechaSalida.split("/");
        String[] hS = horaSalida.split(":");
        Calendar calSalida = new GregorianCalendar(
            Integer.parseInt(fS[2]),
            Integer.parseInt(fS[1]) - 1,
            Integer.parseInt(fS[0]),
            Integer.parseInt(hS[0]),
            Integer.parseInt(hS[1])
        );

        if (calSalida.compareTo(calEntrada) <= 0) {
            return "INVALID";
        }

        long diffMillis = calSalida.getTimeInMillis() - calEntrada.getTimeInMillis();
        long horasCobradas = (long) Math.ceil(diffMillis / (1000.0 * 60 * 60));

        Vehiculo vehiculo = Vehiculo.valueOf(tipoVehiculo);

        double costoBase = 0.0;
        long horasRestantes = horasCobradas;
        while (horasRestantes > 0) {
            long horasBloque = Math.min(24, horasRestantes);
            costoBase += Math.min(horasBloque * vehiculo.tarifaPorHora, vehiculo.maximoPorBloque);
            horasRestantes -= horasBloque;
        }

        boolean esFinSemana = (calEntrada.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                               calEntrada.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
                               calSalida.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                               calSalida.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);

        boolean esNocturna = (Integer.parseInt(hE[0]) >= 20 ||
                              Integer.parseInt(hS[0]) < 6 ||
                              !fechaEntrada.equals(fechaSalida));

        double costoFinal = costoBase;
        if (esFinSemana) costoFinal *= 1.20;
        if (esNocturna) costoFinal *= 1.15;
        if (vehiculo == Vehiculo.ELECTRICO) costoFinal *= 0.90;

        TipoEstancia clasificacion = TipoEstancia.NORMAL;
        if (esFinSemana && esNocturna) {
            clasificacion = TipoEstancia.MIXTA;
        } else if (esFinSemana) {
            clasificacion = TipoEstancia.FIN_SEMANA;
        } else if (esNocturna) {
            clasificacion = TipoEstancia.NOCTURNA;
        }

        return String.format(Locale.US, "%d %.2f %s", horasCobradas, costoFinal, clasificacion.name());
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String tipoVehiculo = reader.readLine();
        String fechaEntrada = reader.readLine();
        String horaEntrada = reader.readLine();
        String fechaSalida = reader.readLine();
        String horaSalida = reader.readLine();

        if (tipoVehiculo != null && fechaEntrada != null && horaEntrada != null && fechaSalida != null && horaSalida != null) {
            String result = calcularEstancia(tipoVehiculo.trim(), fechaEntrada.trim(), horaEntrada.trim(), fechaSalida.trim(), horaSalida.trim());
            System.out.println(result);
        }

        reader.close();
    }
}