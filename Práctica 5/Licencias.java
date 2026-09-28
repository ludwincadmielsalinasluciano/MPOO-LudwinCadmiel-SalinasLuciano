import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Licencias {

    enum TipoLicencia {
        BASICA(1000.0, 1, 0),
        PROFESIONAL(1500.0, 2, 0),
        EMPRESARIAL(2500.0, 3, 0),
        TEMPORAL(600.0, 0, 6);

        final double costoBase;
        final int aniosVigencia;
        final int mesesVigencia;

        TipoLicencia(double costoBase, int aniosVigencia, int mesesVigencia) {
            this.costoBase = costoBase;
            this.aniosVigencia = aniosVigencia;
            this.mesesVigencia = mesesVigencia;
        }
    }

    enum EstadoLicencia {
        VIGENTE,
        PROXIMA_A_VENCER,
        VENCIDA,
        BLOQUEADA
    }

    public static String evaluarLicencia(String fechaActual, String fechaVencimiento, String tipoLicencia, int renovacionesPrevias) {
        String[] fA = fechaActual.split("/");
        Calendar calActual = new GregorianCalendar(
            Integer.parseInt(fA[2]),
            Integer.parseInt(fA[1]) - 1,
            Integer.parseInt(fA[0])
        );
        calActual.set(Calendar.HOUR_OF_DAY, 0);
        calActual.set(Calendar.MINUTE, 0);
        calActual.set(Calendar.SECOND, 0);
        calActual.set(Calendar.MILLISECOND, 0);

        String[] fV = fechaVencimiento.split("/");
        Calendar calVencimiento = new GregorianCalendar(
            Integer.parseInt(fV[2]),
            Integer.parseInt(fV[1]) - 1,
            Integer.parseInt(fV[0])
        );
        calVencimiento.set(Calendar.HOUR_OF_DAY, 0);
        calVencimiento.set(Calendar.MINUTE, 0);
        calVencimiento.set(Calendar.SECOND, 0);
        calVencimiento.set(Calendar.MILLISECOND, 0);

        long diffMillis = calVencimiento.getTimeInMillis() - calActual.getTimeInMillis();
        long diferenciaDias = diffMillis / (1000 * 60 * 60 * 24);

        EstadoLicencia estado;
        if (diferenciaDias > 30) {
            estado = EstadoLicencia.VIGENTE;
        } else if (diferenciaDias >= 0) {
            estado = EstadoLicencia.PROXIMA_A_VENCER;
        } else if (diferenciaDias >= -90) {
            estado = EstadoLicencia.VENCIDA;
        } else {
            estado = EstadoLicencia.BLOQUEADA;
        }

        TipoLicencia tipo = TipoLicencia.valueOf(tipoLicencia);

        double costoFinal = tipo.costoBase;
        Calendar nuevaVigencia = null;

        if (estado == EstadoLicencia.BLOQUEADA) {
            costoFinal = 0.00;
        } else {
            if (estado == EstadoLicencia.VIGENTE) {
                costoFinal *= 0.90;
                nuevaVigencia = (Calendar) calVencimiento.clone();
            } else if (estado == EstadoLicencia.PROXIMA_A_VENCER) {
                nuevaVigencia = (Calendar) calVencimiento.clone();
            } else if (estado == EstadoLicencia.VENCIDA) {
                costoFinal *= 1.20;
                nuevaVigencia = (Calendar) calActual.clone();
            }

            if (renovacionesPrevias > 3) {
                costoFinal *= 0.95;
            }

            nuevaVigencia.add(Calendar.YEAR, tipo.aniosVigencia);
            nuevaVigencia.add(Calendar.MONTH, tipo.mesesVigencia);
        }

        String strNuevaFecha = "NO_DISPONIBLE";
        if (nuevaVigencia != null) {
            strNuevaFecha = String.format(
                "%02d/%02d/%04d",
                nuevaVigencia.get(Calendar.DAY_OF_MONTH),
                nuevaVigencia.get(Calendar.MONTH) + 1,
                nuevaVigencia.get(Calendar.YEAR)
            );
        }

        return String.format(Locale.US, "%s %d %.2f %s", estado.name(), diferenciaDias, costoFinal, strNuevaFecha);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String fechaActual = reader.readLine();
        String fechaVencimiento = reader.readLine();
        String tipoLicencia = reader.readLine();
        String renStr = reader.readLine();

        if (fechaActual != null && fechaVencimiento != null && tipoLicencia != null && renStr != null) {
            int renovacionesPrevias = Integer.parseInt(renStr.trim());
            String result = evaluarLicencia(fechaActual.trim(), fechaVencimiento.trim(), tipoLicencia.trim(), renovacionesPrevias);
            System.out.println(result);
        }

        reader.close();
    }
}