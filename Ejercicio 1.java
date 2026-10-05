import java.io.*;
import java.util.*;

/* =====================================================
   ENUMERACIONES BASE
   ===================================================== */

enum TipoAsistente {
    ALUMNO,
    PROFESOR,
    INVITADO
}

enum AccionEvento {
    REGISTRO,
    ENTRADA,
    SALIDA,
    ENTRADA_MASIVA
}

enum EstadoEntrada {
    AUTORIZADO,
    NO_REGISTRADO,
    YA_DENTRO,
    AFORO_COMPLETO
}

enum EstadoSalida {
    AUTORIZADA,
    NO_REGISTRADO,
    NO_ESTA_DENTRO
}

/* =====================================================
   DTO DE ENTRADA
   ===================================================== */

final class AsistenteDTO {
    private final String id;
    private final String nombre;
    private final String tipo;

    public AsistenteDTO(String id, String nombre, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }
}

/* =====================================================
   DTO DE RESULTADO DE ENTRADA
   ===================================================== */

final class ResultadoEntradaDTO {
    private final int idAsistente;
    private final EstadoEntrada estado;

    public ResultadoEntradaDTO(int idAsistente, EstadoEntrada estado) {
        this.idAsistente = idAsistente;
        this.estado = estado;
    }

    public int getIdAsistente() {
        return idAsistente;
    }

    public EstadoEntrada getEstado() {
        return estado;
    }
}

/* =====================================================
   DTO DEL REPORTE
   ===================================================== */

final class ReporteDTO {
    private final int registrados;
    private final int disponibles;
    private final int aforo;
    private final int alumnos;
    private final int profesores;
    private final int invitados;
    private final double ocupacion;

    public ReporteDTO(
            int registrados,
            int disponibles,
            int aforo,
            int alumnos,
            int profesores,
            int invitados,
            double ocupacion) {

        this.registrados = registrados;
        this.disponibles = disponibles;
        this.aforo = aforo;
        this.alumnos = alumnos;
        this.profesores = profesores;
        this.invitados = invitados;
        this.ocupacion = ocupacion;
    }

    public int getRegistrados() { return registrados; }
    public int getDisponibles() { return disponibles; }
    public int getAforo() { return aforo; }
    public int getAlumnos() { return alumnos; }
    public int getProfesores() { return profesores; }
    public int getInvitados() { return invitados; }
    public double getOcupacion() { return ocupacion; }
}

/* =====================================================
   CONTRATO DE LA CAPA DE APLICACION
   ===================================================== */

interface ControlAccesoService {
    int AFORO_MAXIMO = 10;

    boolean registrarAsistente(AsistenteDTO asistente);
    EstadoEntrada registrarEntrada(int idAsistente);
    EstadoSalida registrarSalida(int idAsistente);
    List<ResultadoEntradaDTO> registrarEntradasMasivas(List<Integer> identificadores);
    ReporteDTO generarReporte();
}

/* =====================================================
   IMPLEMENTACION DEL ALUMNO (CAPA DE DOMINIO Y SERVICIOS)
   ===================================================== */

class Asistente {
    private final int id;
    private final String nombre;
    private final TipoAsistente tipo;

    public Asistente(int id, String nombre, TipoAsistente tipo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoAsistente getTipo() {
        return tipo;
    }
}

class ControlAccesoServiceImpl implements ControlAccesoService {
    
    private static final Map<Integer, Asistente> registrados = new HashMap<>();
    private static final Set<Integer> dentro = new HashSet<>();

    @Override
    public boolean registrarAsistente(AsistenteDTO asistenteDTO) {
        int id = Integer.parseInt(asistenteDTO.getId());
        
        if (registrados.containsKey(id)) {
            return false;
        }
        
        TipoAsistente tipo = TipoAsistente.valueOf(asistenteDTO.getTipo().toUpperCase());
        registrados.put(id, new Asistente(id, asistenteDTO.getNombre(), tipo));
        return true;
    }

    @Override
    public EstadoEntrada registrarEntrada(int idAsistente) {
        if (!registrados.containsKey(idAsistente)) {
            return EstadoEntrada.NO_REGISTRADO;
        }
        if (dentro.contains(idAsistente)) {
            return EstadoEntrada.YA_DENTRO;
        }
        if (dentro.size() >= AFORO_MAXIMO) {
            return EstadoEntrada.AFORO_COMPLETO;
        }
        
        dentro.add(idAsistente);
        return EstadoEntrada.AUTORIZADO;
    }

    @Override
    public EstadoSalida registrarSalida(int idAsistente) {
        if (!registrados.containsKey(idAsistente)) {
            return EstadoSalida.NO_REGISTRADO;
        }
        if (!dentro.contains(idAsistente)) {
            return EstadoSalida.NO_ESTA_DENTRO;
        }
        
        dentro.remove(idAsistente);
        return EstadoSalida.AUTORIZADA;
    }

    @Override
    public List<ResultadoEntradaDTO> registrarEntradasMasivas(List<Integer> identificadores) {
        List<ResultadoEntradaDTO> resultados = new ArrayList<>();
        
        for (int id : identificadores) {
            if (dentro.size() >= AFORO_MAXIMO) {
                break;
            }
            
            EstadoEntrada estado = registrarEntrada(id);
            resultados.add(new ResultadoEntradaDTO(id, estado));
        }
        
        return resultados;
    }

    @Override
    public ReporteDTO generarReporte() {
        int totalRegistrados = registrados.size();
        int aforoActual = dentro.size();
        int lugaresDisponibles = AFORO_MAXIMO - aforoActual;
        
        int alumnos = 0;
        int profesores = 0;
        int invitados = 0;
        
        for (int id : dentro) {
            Asistente a = registrados.get(id);
            if (a.getTipo() == TipoAsistente.ALUMNO) alumnos++;
            else if (a.getTipo() == TipoAsistente.PROFESOR) profesores++;
            else if (a.getTipo() == TipoAsistente.INVITADO) invitados++;
        }
        
        double ocupacion = ((double) aforoActual / AFORO_MAXIMO) * 100.0;
        
        return new ReporteDTO(
                totalRegistrados,
                lugaresDisponibles,
                aforoActual,
                alumnos,
                profesores,
                invitados,
                ocupacion
        );
    }
}

/* =====================================================
   MAIN / ENTRY POINT
   ===================================================== */

public class Solution {

    public static void main(String[] args) throws Exception {

        Locale.setDefault(Locale.US);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String asistentes = br.readLine();
        String operaciones = br.readLine();

        ControlAccesoService servicio = new ControlAccesoServiceImpl();

        if (asistentes != null && !asistentes.isBlank() && !asistentes.equals("-")) {
            String[] registros = asistentes.split(";");

            for (String registro : registros) {
                String[] datos = registro.split("\\|", 3);

                AsistenteDTO dto = new AsistenteDTO(
                        datos[0].trim(),
                        datos[1].trim(),
                        datos[2].trim()
                );

                servicio.registrarAsistente(dto);
            }
        }

        StringBuilder salida = new StringBuilder();

        if (operaciones != null && !operaciones.isBlank() && !operaciones.equals("-")) {
            String[] lista = operaciones.split(";");

            for (String operacion : lista) {
                String[] datos = operacion.split("\\|", 2);
                String accion = datos[0].trim();

                if (accion.equals("ENTRADA")) {
                    int id = Integer.parseInt(datos[1].trim());
                    EstadoEntrada estado = servicio.registrarEntrada(id);

                    salida.append("ENTRADA ")
                            .append(id)
                            .append(" ")
                            .append(estado)
                            .append("\n");
                }
                else if (accion.equals("SALIDA")) {
                    int id = Integer.parseInt(datos[1].trim());
                    EstadoSalida estado = servicio.registrarSalida(id);

                    salida.append("SALIDA ")
                            .append(id)
                            .append(" ")
                            .append(estado)
                            .append("\n");
                }
                else if (accion.equals("MASIVA")) {
                    String[] ids = datos[1].split(",");
                    List<Integer> identificadores = new ArrayList<>();

                    for (String id : ids) {
                        identificadores.add(Integer.parseInt(id.trim()));
                    }

                    List<ResultadoEntradaDTO> resultados = servicio.registrarEntradasMasivas(identificadores);

                    if (resultados == null) {
                        throw new IllegalStateException("registrarEntradasMasivas no debe regresar null");
                    }

                    for (ResultadoEntradaDTO resultado : resultados) {
                        salida.append("ENTRADA ")
                                .append(resultado.getIdAsistente())
                                .append(" ")
                                .append(resultado.getEstado())
                                .append("\n");
                    }
                }
            }
        }

        ReporteDTO reporte = servicio.generarReporte();

        if (reporte == null) {
            throw new IllegalStateException("generarReporte no debe regresar null");
        }

        salida.append(
                String.format(
                        "REPORTE %d %d %d %d %d %d %.2f",
                        reporte.getRegistrados(),
                        reporte.getDisponibles(),
                        reporte.getAforo(),
                        reporte.getAlumnos(),
                        reporte.getProfesores(),
                        reporte.getInvitados(),
                        reporte.getOcupacion()
                )
        );

        System.out.print(salida.toString());
    }
}