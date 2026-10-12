import java.io.*;
import java.util.*;




/* =====================================================
   ENUMS
   ===================================================== */


enum TipoDestino {
    PLANETA,
    SATELITE_NATURAL
}




enum TipoRecurso {
    DRON,
    SATELITE_ARTIFICIAL,
    CARGA,
    TRIPULANTE
}




enum TipoMision {
    EXPLORACION,
    RESCATE,
    TRANSPORTE
}




enum Capacidad {
    COMUNICABLE,
    RASTREABLE,
    OPERABLE,
    ESCANEABLE
}




enum EstadoValidacion {
    OK,
    FALLIDA
}




enum EstadoPaso {
    OK,
    NO_EJECUTADO
}




enum ResultadoObjetivo {
    ESCANEO_COMPLETADO,
    RESCATE_COMPLETADO,
    CARGA_ENTREGADA,
    NO_EJECUTADO
}




/* =====================================================
   DTO DESTINO
   ===================================================== */


final class DestinoDTO {


    private final TipoDestino tipo;
    private final String codigo;
    private final String nombre;
    private final double gravedad;
    private final String datoEspecifico;




    public DestinoDTO(
            TipoDestino tipo,
            String codigo,
            String nombre,
            double gravedad,
            String datoEspecifico) {


        this.tipo = tipo;
        this.codigo = codigo;
        this.nombre = nombre;
        this.gravedad = gravedad;
        this.datoEspecifico = datoEspecifico;
    }




    public TipoDestino getTipo() {
        return tipo;
    }


    public String getCodigo() {
        return codigo;
    }


    public String getNombre() {
        return nombre;
    }


    public double getGravedad() {
        return gravedad;
    }


    public String getDatoEspecifico() {
        return datoEspecifico;
    }
}




/* =====================================================
   DTO NAVE
   ===================================================== */


final class NaveDTO {


    private final String id;
    private final String nombre;
    private final String posicion;
    private final boolean operativa;




    public NaveDTO(
            String id,
            String nombre,
            String posicion,
            boolean operativa) {


        this.id = id;
        this.nombre = nombre;
        this.posicion = posicion;
        this.operativa = operativa;
    }




    public String getId() {
        return id;
    }


    public String getNombre() {
        return nombre;
    }


    public String getPosicion() {
        return posicion;
    }


    public boolean isOperativa() {
        return operativa;
    }
}




/* =====================================================
   DTO BASE DE RECURSOS
   ===================================================== */


abstract class RecursoDTO {


    private final TipoRecurso tipo;
    private final String id;




    protected RecursoDTO(
            TipoRecurso tipo,
            String id) {


        this.tipo = tipo;
        this.id = id;
    }




    public TipoRecurso getTipo() {
        return tipo;
    }


    public String getId() {
        return id;
    }
}




/* =====================================================
   DTO DRON
   ===================================================== */


final class DronDTO extends RecursoDTO {


    private final String modelo;
    private final String posicion;
    private final boolean operativo;




    public DronDTO(
            String id,
            String modelo,
            String posicion,
            boolean operativo) {


        super(TipoRecurso.DRON, id);


        this.modelo = modelo;
        this.posicion = posicion;
        this.operativo = operativo;
    }




    public String getModelo() {
        return modelo;
    }


    public String getPosicion() {
        return posicion;
    }


    public boolean isOperativo() {
        return operativo;
    }
}




/* =====================================================
   DTO SATELITE ARTIFICIAL
   ===================================================== */


final class SateliteArtificialDTO extends RecursoDTO {


    private final String nombre;
    private final String posicion;
    private final boolean operativo;




    public SateliteArtificialDTO(
            String id,
            String nombre,
            String posicion,
            boolean operativo) {


        super(TipoRecurso.SATELITE_ARTIFICIAL, id);


        this.nombre = nombre;
        this.posicion = posicion;
        this.operativo = operativo;
    }




    public String getNombre() {
        return nombre;
    }


    public String getPosicion() {
        return posicion;
    }


    public boolean isOperativo() {
        return operativo;
    }
}




/* =====================================================
   DTO CARGA
   ===================================================== */


final class CargaDTO extends RecursoDTO {


    private final String descripcion;
    private final double peso;
    private final String posicion;




    public CargaDTO(
            String id,
            String descripcion,
            double peso,
            String posicion) {


        super(TipoRecurso.CARGA, id);


        this.descripcion = descripcion;
        this.peso = peso;
        this.posicion = posicion;
    }




    public String getDescripcion() {
        return descripcion;
    }


    public double getPeso() {
        return peso;
    }


    public String getPosicion() {
        return posicion;
    }
}




/* =====================================================
   DTO TRIPULANTE
   ===================================================== */


final class TripulanteDTO extends RecursoDTO {


    private final String nombre;
    private final String posicion;




    public TripulanteDTO(
            String id,
            String nombre,
            String posicion) {


        super(TipoRecurso.TRIPULANTE, id);


        this.nombre = nombre;
        this.posicion = posicion;
    }




    public String getNombre() {
        return nombre;
    }


    public String getPosicion() {
        return posicion;
    }
}




/* =====================================================
   DTO MISION
   ===================================================== */


final class MisionDTO {


    private final String id;
    private final String nombre;
    private final TipoMision tipo;
    private final List<String> idsRecursos;




    public MisionDTO(
            String id,
            String nombre,
            TipoMision tipo,
            List<String> idsRecursos) {


        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.idsRecursos =
                new ArrayList<String>(idsRecursos);
    }




    public String getId() {
        return id;
    }


    public String getNombre() {
        return nombre;
    }


    public TipoMision getTipo() {
        return tipo;
    }


    public List<String> getIdsRecursos() {
        return new ArrayList<String>(idsRecursos);
    }
}




/* =====================================================
   RESULTADO DE UNA MISION
   ===================================================== */


final class EjecucionMisionDTO {


    private final EstadoValidacion validacion;
    private final EstadoPaso preparacion;
    private final EstadoPaso viaje;


    private final ResultadoObjetivo objetivo;


    private final EstadoPaso finalizacion;
    private final EstadoPaso reporte;




    public EjecucionMisionDTO(
            EstadoValidacion validacion,
            EstadoPaso preparacion,
            EstadoPaso viaje,
            ResultadoObjetivo objetivo,
            EstadoPaso finalizacion,
            EstadoPaso reporte) {


        this.validacion = validacion;
        this.preparacion = preparacion;
        this.viaje = viaje;
        this.objetivo = objetivo;
        this.finalizacion = finalizacion;
        this.reporte = reporte;
    }




    public EstadoValidacion getValidacion() {
        return validacion;
    }


    public EstadoPaso getPreparacion() {
        return preparacion;
    }


    public EstadoPaso getViaje() {
        return viaje;
    }


    public ResultadoObjetivo getObjetivo() {
        return objetivo;
    }


    public EstadoPaso getFinalizacion() {
        return finalizacion;
    }


    public EstadoPaso getReporte() {
        return reporte;
    }
}




/* =====================================================
   CONTRATOS DE MAPPERS
   ===================================================== */


interface DestinoMapper {


    DestinoEspacial toEntity(
            DestinoDTO dto);
}




interface NaveMapper {


    Nave toEntity(
            NaveDTO dto);
}




interface RecursoMapper {


    Recurso toEntity(
            RecursoDTO dto);
}




/* =====================================================
   SERVICIO DE CREACION DE MISIONES
   ===================================================== */


interface MisionFactoryService {


    Mision crearMision(
            MisionDTO dto,
            Nave nave,
            DestinoEspacial destino,
            Map<String, Recurso> recursos);
}




/* =====================================================
   SERVICIO DE CAPACIDADES
   ===================================================== */


interface CapacidadService {


    List<Capacidad> obtenerCapacidades(
            Object entidad);
}


/*
 * =====================================================
 *
 * Desarrolle todo el codigo de la practica en esta
 * seccion.
 *
 * Los DTO, enums y contratos necesarios ya fueron
 * proporcionados.
 *
 *
 * =====================================================
 * 1. INTERFACES DE CAPACIDADES
 * =====================================================
 *
 * Cree:
 *
 *      Comunicable
 *      Rastreable
 *      Operable
 *      Escaneable
 *
 *
 * Utilice las siguientes operaciones:
 *
 * Comunicable:
 *
 *      String comunicar()
 *
 *
 * Rastreable:
 *
 *      String obtenerPosicion()
 *
 *
 * Operable:
 *
 *      boolean estaOperativo()
 *
 *
 * Escaneable:
 *
 *      String escanear()
 *
 *
 * =====================================================
 * 2. DESTINOS ESPACIALES
 * =====================================================
 *
 * Cree una clase abstracta:
 *
 *      DestinoEspacial
 *
 *
 * Debe manejar:
 *
 *      codigo
 *      nombre
 *      gravedad
 *
 *
 * Cree las subclases:
 *
 *      Planeta
 *      SateliteNatural
 *
 *
 * Planeta debe manejar:
 *
 *      boolean tieneAtmosfera
 *
 * SateliteNatural debe manejar:
 *
 *      String orbitaA
 *
 *
 * =====================================================
 * 3. NAVE
 * =====================================================
 *
 * Cree la clase:
 *
 *      Nave
 *
 *
 * Utilice los datos disponibles en NaveDTO.
 *
 * Nave debe tener las capacidades:
 *
 *      Comunicable
 *      Rastreable
 *      Operable
 *
 *
 * =====================================================
 * 4. RECURSOS
 * =====================================================
 *
 * Cree una clase abstracta:
 *
 *      Recurso
 *
 * Debe almacenar un identificador
 *
 * Y existen los subtipos:
 *
 *      Dron
 *      SateliteArtificial
 *      Carga
 *      Tripulante
 *
 *
 * Utilice los datos disponibles en sus DTO.
 *
 *
 * =====================================================
 * 5. CAPACIDADES
 * =====================================================
 *
 * Las entidades deben implementar:
 *
 * Nave:
 *
 *      Comunicable
 *      Rastreable
 *      Operable
 *
 *
 * Dron:
 *
 *      Comunicable
 *      Rastreable
 *      Operable
 *      Escaneable
 *
 *
 * SateliteArtificial:
 *
 *      Comunicable
 *      Rastreable
 *      Operable
 *      Escaneable
 *
 *
 * Carga:
 *
 *      Rastreable
 *
 *
 * Tripulante:
 *
 *      Comunicable
 *      Rastreable
 *
 *
 * =====================================================
 * 6. JERARQUIA DE MISIONES
 * =====================================================
 *
 * Cree una clase abstracta:
 *
 *      Mision
 *
 *
 * Debe manejar:
 *
 *      id
 *      nombre
 *      Nave
 *      DestinoEspacial
 *
 *
 * Cree las subclases:
 *
 *      MisionExploracion
 *      MisionRescate
 *      MisionTransporte
 *
 *
 * Utilice composicion para relacionar una Mision
 * con su Nave y su DestinoEspacial.
 *
 *
 * =====================================================
 * 7. USO DE TEMPLATE METHOD
 * =====================================================
 *
 * La clase Mision debe proporcionar exactamente:
 *
 *      public final EjecucionMisionDTO ejecutarMision()
 *
 *
 * ejecutarMision() debe mantener este orden:
 *
 *      1. Validar la mision
 *      2. Preparar recursos
 *      3. Iniciar viaje
 *      4. Ejecutar objetivo
 *      5. Finalizar operacion
 *      6. Generar reporte
 *
 *
 * El metodo debe ser final para impedir que las
 * subclases cambien el flujo general.
 *
 * Utilice sobrescritura para los pasos cuyo
 * comportamiento cambie entre las misiones.
 *
 * No utilice un switch dentro de ejecutarMision()
 * para decidir el comportamiento de cada subtipo.
 *
 * =====================================================
 * 8. RESULTADO DE LA EJECUCION
 * =====================================================
 *
 * ejecutarMision() debe regresar:
 *
 *      EjecucionMisionDTO
 *
 *
 * Utilice los enums proporcionados:
 *
 *      EstadoValidacion
 *      EstadoPaso
 *      ResultadoObjetivo
 *
 *
 * Si la Nave no esta operativa:
 *
 *      EstadoValidacion.FALLIDA
 *
 * y los pasos posteriores deben quedar como:
 *
 *      EstadoPaso.NO_EJECUTADO
 *
 * El objetivo debe quedar como:
 *
 *      ResultadoObjetivo.NO_EJECUTADO
 *
 *
 * =====================================================
 * 9. OBJETIVO DE LAS MISIONES
 * =====================================================
 *
 * MisionExploracion debe producir:
 *
 *      ResultadoObjetivo.ESCANEO_COMPLETADO
 *
 *
 * MisionRescate debe producir:
 *
 *      ResultadoObjetivo.RESCATE_COMPLETADO
 *
 *
 * MisionTransporte debe producir:
 *
 *      ResultadoObjetivo.CARGA_ENTREGADA
 *
 *
 * Este comportamiento debe resolverse mediante
 * sobrescritura.
 *
 *
 * =====================================================
 * 10. RECURSOS DE CADA MISION
 * =====================================================
 *
 * EXPLORACION:
 *
 * Debe utilizar un recurso que implemente:
 *
 *      Escaneable
 *
 *
 * RESCATE:
 *
 * Debe utilizar uno o varios:
 *
 *      Tripulante
 *
 *
 * TRANSPORTE:
 *
 * Debe utilizar:
 *
 *      Carga
 *
 *
 * Los identificadores de los recursos se encuentran
 * disponibles mediante:
 *
 *      MisionDTO.getIdsRecursos()
 *
 *
 * =====================================================
 * 11. MAPPERS
 * =====================================================
 *
 * Implemente:
 *
 *      DestinoMapperImpl
 *      NaveMapperImpl
 *      RecursoMapperImpl
 *
 *
 * Cada clase debe implementar su interfaz
 * correspondiente.
 *
 * Su responsabilidad debe ser solamente:
 *
 *      DTO -> Entidad
 *
 *
 * DestinoMapperImpl:
 *
 *      DestinoDTO PLANETA
 *          -> Planeta
 *
 *      DestinoDTO SATELITE_NATURAL
 *          -> SateliteNatural
 *
 *
 * RecursoMapperImpl:
 *
 *      DronDTO
 *          -> Dron
 *
 *      SateliteArtificialDTO
 *          -> SateliteArtificial
 *
 *      CargaDTO
 *          -> Carga
 *
 *      TripulanteDTO
 *          -> Tripulante
 *
 *
 * =====================================================
 * 12. FACTORY DE MISIONES
 * =====================================================
 *
 * Implemente:
 *
 *      MisionFactoryServiceImpl
 *
 *
 * Debe implementar:
 *
 *      MisionFactoryService
 *
 *
 * Debe crear:
 *
 *      EXPLORACION
 *          -> MisionExploracion
 *
 *      RESCATE
 *          -> MisionRescate
 *
 *      TRANSPORTE
 *          -> MisionTransporte
 *
 *
 * Puede utilizar switch o if/else para decidir
 * que objeto crear.
 *
 *
 * =====================================================
 * 13. SERVICIO DE CAPACIDADES
 * =====================================================
 *
 * Implemente:
 *
 *      CapacidadServiceImpl
 *
 *
 * Debe implementar:
 *
 *      CapacidadService
 *
 *
 * obtenerCapacidades() debe regresar:
 *
 *      List<Capacidad>
 *
 *
 * Puede utilizar instanceof para determinar las
 * interfaces que implementa una entidad.
 *
 * El orden de las capacidades debe ser:
 *
 *      COMUNICABLE
 *      RASTREABLE
 *      OPERABLE
 *      ESCANEABLE
 *
 *
 * =====================================================
 * IMPORTANTE
 * =====================================================
 *
 * El alumno NO debe construir el texto final que
 * imprime HackerRank.
 *
 * El alumno trabaja en la creacion de:
 *
 *      Entidades
 *      Interfaces
 *      Enums
 *      DTO
 *      Mappers
 *      Servicios
 *
 * HackerRank se encargara de construir la salida.
 *
 *
 * =====================================================
 * DESARROLLE SU SOLUCION A PARTIR DE AQUI
 * =====================================================
 */
 interface Comunicable {
    String comunicar();
}


interface Rastreable {
    String obtenerPosicion();
}


interface Operable {
    boolean estaOperativo();
}


interface Escaneable {
    String escanear();
}


abstract class DestinoEspacial {
    private final String codigo;
    private final String nombre;
    private final double gravedad;


    public DestinoEspacial(String codigo, String nombre, double gravedad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.gravedad = gravedad;
    }


    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public double getGravedad() { return gravedad; }
}


class Planeta extends DestinoEspacial {
    private final boolean tieneAtmosfera;


    public Planeta(String codigo, String nombre, double gravedad, boolean tieneAtmosfera) {
        super(codigo, nombre, gravedad);
        this.tieneAtmosfera = tieneAtmosfera;
    }


    public boolean tieneAtmosfera() { return tieneAtmosfera; }
}


class SateliteNatural extends DestinoEspacial {
    private final String orbitaA;


    public SateliteNatural(String codigo, String nombre, double gravedad, String orbitaA) {
        super(codigo, nombre, gravedad);
        this.orbitaA = orbitaA;
    }


    public String getOrbitaA() { return orbitaA; }
}


class Nave implements Comunicable, Rastreable, Operable {
    private final String id;
    private final String nombre;
    private final String posicion;
    private final boolean operativa;


    public Nave(String id, String nombre, String posicion, boolean operativa) {
        this.id = id;
        this.nombre = nombre;
        this.posicion = posicion;
        this.operativa = operativa;
    }


    public String getId() { return id; }
    public String getNombre() { return nombre; }
   
    @Override public String comunicar() { return "Nave comunicando"; }
    @Override public String obtenerPosicion() { return posicion; }
    @Override public boolean estaOperativo() { return operativa; }
}


abstract class Recurso {
    private final String id;
    public Recurso(String id) { this.id = id; }
    public String getId() { return id; }
}


class Dron extends Recurso implements Comunicable, Rastreable, Operable, Escaneable {
    private final String modelo;
    private final String posicion;
    private final boolean operativo;


    public Dron(String id, String modelo, String posicion, boolean operativo) {
        super(id);
        this.modelo = modelo;
        this.posicion = posicion;
        this.operativo = operativo;
    }


    @Override public String comunicar() { return "Dron comunicando"; }
    @Override public String obtenerPosicion() { return posicion; }
    @Override public boolean estaOperativo() { return operativo; }
    @Override public String escanear() { return "Dron escaneando"; }
}


class SateliteArtificial extends Recurso implements Comunicable, Rastreable, Operable, Escaneable {
    private final String nombre;
    private final String posicion;
    private final boolean operativo;


    public SateliteArtificial(String id, String nombre, String posicion, boolean operativo) {
        super(id);
        this.nombre = nombre;
        this.posicion = posicion;
        this.operativo = operativo;
    }


    @Override public String comunicar() { return "Satelite comunicando"; }
    @Override public String obtenerPosicion() { return posicion; }
    @Override public boolean estaOperativo() { return operativo; }
    @Override public String escanear() { return "Satelite escaneando"; }
}


class Carga extends Recurso implements Rastreable {
    private final String descripcion;
    private final double peso;
    private final String posicion;


    public Carga(String id, String descripcion, double peso, String posicion) {
        super(id);
        this.descripcion = descripcion;
        this.peso = peso;
        this.posicion = posicion;
    }


    @Override public String obtenerPosicion() { return posicion; }
}


class Tripulante extends Recurso implements Comunicable, Rastreable {
    private final String nombre;
    private final String posicion;


    public Tripulante(String id, String nombre, String posicion) {
        super(id);
        this.nombre = nombre;
        this.posicion = posicion;
    }


    @Override public String comunicar() { return "Tripulante comunicando"; }
    @Override public String obtenerPosicion() { return posicion; }
}


abstract class Mision {
    protected final String id;
    protected final String nombre;
    protected final Nave nave;
    protected final DestinoEspacial destino;
    protected final Map<String, Recurso> recursos;


    public Mision(String id, String nombre, Nave nave, DestinoEspacial destino, Map<String, Recurso> recursos) {
        this.id = id;
        this.nombre = nombre;
        this.nave = nave;
        this.destino = destino;
        this.recursos = recursos;
    }


    public final EjecucionMisionDTO ejecutarMision() {
        if (!nave.estaOperativo() || !validarRecursos()) {
            return new EjecucionMisionDTO(
                EstadoValidacion.FALLIDA,
                EstadoPaso.NO_EJECUTADO,
                EstadoPaso.NO_EJECUTADO,
                ResultadoObjetivo.NO_EJECUTADO,
                EstadoPaso.NO_EJECUTADO,
                EstadoPaso.NO_EJECUTADO
            );
        }


        return new EjecucionMisionDTO(
            EstadoValidacion.OK,
            EstadoPaso.OK,
            EstadoPaso.OK,
            ejecutarObjetivo(),
            EstadoPaso.OK,
            EstadoPaso.OK  
        );
    }


    protected abstract boolean validarRecursos();
    protected abstract ResultadoObjetivo ejecutarObjetivo();
}


class MisionExploracion extends Mision {
    public MisionExploracion(String id, String nombre, Nave nave, DestinoEspacial destino, Map<String, Recurso> recursos) {
        super(id, nombre, nave, destino, recursos);
    }


    @Override
    protected boolean validarRecursos() {
        for (Recurso r : recursos.values()) {
            if (r instanceof Escaneable) return true;
        }
        return false;
    }


    @Override
    protected ResultadoObjetivo ejecutarObjetivo() {
        return ResultadoObjetivo.ESCANEO_COMPLETADO;
    }
}


class MisionRescate extends Mision {
    public MisionRescate(String id, String nombre, Nave nave, DestinoEspacial destino, Map<String, Recurso> recursos) {
        super(id, nombre, nave, destino, recursos);
    }


    @Override
    protected boolean validarRecursos() {
        for (Recurso r : recursos.values()) {
            if (r instanceof Tripulante) return true;
        }
        return false;
    }


    @Override
    protected ResultadoObjetivo ejecutarObjetivo() {
        return ResultadoObjetivo.RESCATE_COMPLETADO;
    }
}


class MisionTransporte extends Mision {
    public MisionTransporte(String id, String nombre, Nave nave, DestinoEspacial destino, Map<String, Recurso> recursos) {
        super(id, nombre, nave, destino, recursos);
    }


    @Override
    protected boolean validarRecursos() {
        for (Recurso r : recursos.values()) {
            if (r instanceof Carga) return true;
        }
        return false;
    }


    @Override
    protected ResultadoObjetivo ejecutarObjetivo() {
        return ResultadoObjetivo.CARGA_ENTREGADA;
    }
}


class DestinoMapperImpl implements DestinoMapper {
    @Override
    public DestinoEspacial toEntity(DestinoDTO dto) {
        if (dto.getTipo() == TipoDestino.PLANETA) {
            return new Planeta(dto.getCodigo(), dto.getNombre(), dto.getGravedad(), Boolean.parseBoolean(dto.getDatoEspecifico()));
        } else {
            return new SateliteNatural(dto.getCodigo(), dto.getNombre(), dto.getGravedad(), dto.getDatoEspecifico());
        }
    }
}


class NaveMapperImpl implements NaveMapper {
    @Override
    public Nave toEntity(NaveDTO dto) {
        return new Nave(dto.getId(), dto.getNombre(), dto.getPosicion(), dto.isOperativa());
    }
}


class RecursoMapperImpl implements RecursoMapper {
    @Override
    public Recurso toEntity(RecursoDTO dto) {
        if (dto instanceof DronDTO) {
            DronDTO d = (DronDTO) dto;
            return new Dron(d.getId(), d.getModelo(), d.getPosicion(), d.isOperativo());
        } else if (dto instanceof SateliteArtificialDTO) {
            SateliteArtificialDTO s = (SateliteArtificialDTO) dto;
            return new SateliteArtificial(s.getId(), s.getNombre(), s.getPosicion(), s.isOperativo());
        } else if (dto instanceof CargaDTO) {
            CargaDTO c = (CargaDTO) dto;
            return new Carga(c.getId(), c.getDescripcion(), c.getPeso(), c.getPosicion());
        } else if (dto instanceof TripulanteDTO) {
            TripulanteDTO t = (TripulanteDTO) dto;
            return new Tripulante(t.getId(), t.getNombre(), t.getPosicion());
        }
        throw new IllegalArgumentException("Recurso DTO no reconocido");
    }
}


class MisionFactoryServiceImpl implements MisionFactoryService {
    @Override
    public Mision crearMision(MisionDTO dto, Nave nave, DestinoEspacial destino, Map<String, Recurso> recursos) {
        switch (dto.getTipo()) {
            case EXPLORACION:
                return new MisionExploracion(dto.getId(), dto.getNombre(), nave, destino, recursos);
            case RESCATE:
                return new MisionRescate(dto.getId(), dto.getNombre(), nave, destino, recursos);
            case TRANSPORTE:
                return new MisionTransporte(dto.getId(), dto.getNombre(), nave, destino, recursos);
            default:
                throw new IllegalArgumentException("Tipo de mision no soportado");
        }
    }
}


class CapacidadServiceImpl implements CapacidadService {
    @Override
    public List<Capacidad> obtenerCapacidades(Object entidad) {
        List<Capacidad> capacidades = new ArrayList<>();
        if (entidad instanceof Comunicable) capacidades.add(Capacidad.COMUNICABLE);
        if (entidad instanceof Rastreable) capacidades.add(Capacidad.RASTREABLE);
        if (entidad instanceof Operable) capacidades.add(Capacidad.OPERABLE);
        if (entidad instanceof Escaneable) capacidades.add(Capacidad.ESCANEABLE);
        return capacidades;
    }
}


public class Solution {




    public static void main(String[] args)
            throws Exception {




        BufferedReader br =
                new BufferedReader(
                        new InputStreamReader(System.in));




        String destinoInput = br.readLine();
        String naveInput = br.readLine();
        String recursosInput = br.readLine();
        String misionInput = br.readLine();




        String resultado =
                procesarMision(
                        destinoInput,
                        naveInput,
                        recursosInput,
                        misionInput
                );




        System.out.print(resultado);
    }




    /* =====================================================
       PROCESAMIENTO PRINCIPAL
       ===================================================== */


    public static String procesarMision(
            String destinoInput,
            String naveInput,
            String recursosInput,
            String misionInput) {




        DestinoDTO destinoDTO =
                parseDestino(destinoInput);


        NaveDTO naveDTO =
                parseNave(naveInput);


        List<RecursoDTO> recursosDTO =
                parseRecursos(recursosInput);


        MisionDTO misionDTO =
                parseMision(misionInput);




        /* MAPPERS DEL ALUMNO */


        DestinoMapper destinoMapper =
                new DestinoMapperImpl();


        NaveMapper naveMapper =
                new NaveMapperImpl();


        RecursoMapper recursoMapper =
                new RecursoMapperImpl();




        /* SERVICIOS DEL ALUMNO */


        MisionFactoryService factory =
                new MisionFactoryServiceImpl();


        CapacidadService capacidadService =
                new CapacidadServiceImpl();




        /* DTO -> ENTIDADES */


        DestinoEspacial destino =
                destinoMapper.toEntity(destinoDTO);


        Nave nave =
                naveMapper.toEntity(naveDTO);


        Map<String, Recurso> recursos =
                mapearRecursos(
                        recursosDTO,
                        recursoMapper
                );




        /* CREAR MISION */


        Mision mision =
                factory.crearMision(
                        misionDTO,
                        nave,
                        destino,
                        recursos
                );




        /* EJECUTAR MISION */


        EjecucionMisionDTO ejecucion =
                mision.ejecutarMision();




        /* HACKERRANK CONSTRUYE LA SALIDA */


        return construirSalida(
                destino,
                nave,
                recursos,
                misionDTO,
                ejecucion,
                capacidadService
        );
    }




    /* =====================================================
       MAPEAR RECURSOS
       ===================================================== */


    private static Map<String, Recurso> mapearRecursos(
            List<RecursoDTO> recursosDTO,
            RecursoMapper mapper) {




        Map<String, Recurso> recursos =
                new LinkedHashMap<String, Recurso>();




        for (RecursoDTO dto : recursosDTO) {


            Recurso recurso =
                    mapper.toEntity(dto);


            recursos.put(
                    recurso.getId(),
                    recurso
            );
        }




        return recursos;
    }




    /* =====================================================
       SALIDA
       ===================================================== */


    private static String construirSalida(
            DestinoEspacial destino,
            Nave nave,
            Map<String, Recurso> recursos,
            MisionDTO misionDTO,
            EjecucionMisionDTO ejecucion,
            CapacidadService capacidadService) {




        List<String> salida =
                new ArrayList<String>();




        salida.add(
                "MISION "
                        + misionDTO.getId());




        salida.add(
                "TIPO "
                        + misionDTO.getTipo());




        salida.add(
                construirDestino(destino));




        salida.add(
                construirCapacidades(
                        nave.getId(),
                        nave,
                        capacidadService
                )
        );




        for (String id
                : misionDTO.getIdsRecursos()) {




            Recurso recurso =
                    recursos.get(id);




            if (recurso != null) {


                salida.add(
                        construirCapacidades(
                                recurso.getId(),
                                recurso,
                                capacidadService
                        )
                );
            }
        }




        /* VALIDACION */


        if (ejecucion.getValidacion()
                == EstadoValidacion.FALLIDA) {




            salida.add(
                    "VALIDACION FALLIDA");


            salida.add(
                    "MISION ABORTADA");




            return String.join(
                    "\n",
                    salida
            );
        }




        salida.add(
                "VALIDACION OK");




        /* PREPARACION */


        if (ejecucion.getPreparacion()
                == EstadoPaso.OK) {


            salida.add(
                    "PREPARACION OK");
        }




        /* VIAJE */


        if (ejecucion.getViaje()
                == EstadoPaso.OK) {


            salida.add(
                    "VIAJE "
                            + destino.getNombre());
        }




        /* OBJETIVO */


        if (ejecucion.getObjetivo()
                != ResultadoObjetivo.NO_EJECUTADO) {


            salida.add(
                    "OBJETIVO "
                            + ejecucion
                                    .getObjetivo()
                                    .name());
        }




        /* FINALIZACION */


        if (ejecucion.getFinalizacion()
                == EstadoPaso.OK) {


            salida.add(
                    "FINALIZACION OK");
        }




        /* REPORTE */


        if (ejecucion.getReporte()
                == EstadoPaso.OK) {


            salida.add(
                    "REPORTE COMPLETADO");
        }




        return String.join(
                "\n",
                salida
        );
    }




    /* =====================================================
       DESTINO
       ===================================================== */


    private static String construirDestino(
            DestinoEspacial destino) {




        if (destino instanceof Planeta) {




            Planeta planeta =
                    (Planeta) destino;




            return "DESTINO PLANETA "
                    + planeta.getNombre()
                    + " ATMOSFERA "
                    + (
                        planeta.tieneAtmosfera()
                                ? "SI"
                                : "NO"
                    );
        }




        if (destino instanceof SateliteNatural) {




            SateliteNatural satelite =
                    (SateliteNatural) destino;




            return "DESTINO SATELITE_NATURAL "
                    + satelite.getNombre()
                    + " ORBITA "
                    + satelite.getOrbitaA();
        }




        throw new IllegalArgumentException(
                "Destino no valido");
    }




    /* =====================================================
       CAPACIDADES
       ===================================================== */


    private static String construirCapacidades(
            String id,
            Object entidad,
            CapacidadService servicio) {




        List<Capacidad> capacidades =
                servicio.obtenerCapacidades(
                        entidad);




        List<String> nombres =
                new ArrayList<String>();




        for (Capacidad capacidad
                : capacidades) {


            nombres.add(
                    capacidad.name());
        }




        return "CAPACIDADES "
                + id
                + " "
                + String.join(
                        ",",
                        nombres
                );
    }




    /* =====================================================
       PARSE DESTINO
       ===================================================== */


    private static DestinoDTO parseDestino(
            String input) {




        String[] d =
                input.split("\\|", 5);




        return new DestinoDTO(
                TipoDestino.valueOf(
                        d[0].trim()),
                d[1].trim(),
                d[2].trim(),
                Double.parseDouble(
                        d[3].trim()),
                d[4].trim()
        );
    }




    /* =====================================================
       PARSE NAVE
       ===================================================== */


    private static NaveDTO parseNave(
            String input) {




        String[] d =
                input.split("\\|", 4);




        return new NaveDTO(
                d[0].trim(),
                d[1].trim(),
                d[2].trim(),
                Boolean.parseBoolean(
                        d[3].trim())
        );
    }




    /* =====================================================
       PARSE RECURSOS
       ===================================================== */


    private static List<RecursoDTO> parseRecursos(
            String input) {




        List<RecursoDTO> recursos =
                new ArrayList<RecursoDTO>();




        if (input == null
                || input.trim().isEmpty()
                || input.equals("-")) {


            return recursos;
        }




        String[] registros =
                input.split(";");




        for (String registro : registros) {




            String[] d =
                    registro.split("\\|");




            TipoRecurso tipo =
                    TipoRecurso.valueOf(
                            d[0].trim());




            switch (tipo) {




                case DRON:


                    recursos.add(
                            new DronDTO(
                                    d[1].trim(),
                                    d[2].trim(),
                                    d[3].trim(),
                                    Boolean.parseBoolean(
                                            d[4].trim())
                            )
                    );


                    break;




                case SATELITE_ARTIFICIAL:


                    recursos.add(
                            new SateliteArtificialDTO(
                                    d[1].trim(),
                                    d[2].trim(),
                                    d[3].trim(),
                                    Boolean.parseBoolean(
                                            d[4].trim())
                            )
                    );


                    break;




                case CARGA:


                    recursos.add(
                            new CargaDTO(
                                    d[1].trim(),
                                    d[2].trim(),
                                    Double.parseDouble(
                                            d[3].trim()),
                                    d[4].trim()
                            )
                    );


                    break;




                case TRIPULANTE:


                    recursos.add(
                            new TripulanteDTO(
                                    d[1].trim(),
                                    d[2].trim(),
                                    d[3].trim()
                            )
                    );


                    break;
            }
        }




        return recursos;
    }




    /* =====================================================
       PARSE MISION
       ===================================================== */


    private static MisionDTO parseMision(
            String input) {




        String[] d =
                input.split("\\|", 4);




        List<String> ids =
                new ArrayList<String>();




        if (d.length == 4
                && !d[3].trim().isEmpty()
                && !d[3].equals("-")) {




            String[] recursos =
                    d[3].split(",");




            for (String id : recursos) {


                ids.add(
                        id.trim());
            }
        }




        return new MisionDTO(
                d[0].trim(),
                d[1].trim(),
                TipoMision.valueOf(
                        d[2].trim()),
                ids
        );
    }
}


