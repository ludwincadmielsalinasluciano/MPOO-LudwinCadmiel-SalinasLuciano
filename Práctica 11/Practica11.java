import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


/*
 * Contrato comun de las publicaciones.
 */

interface Publicable {

    ResultadoPublicacion publicar();

    Long getId();

    String getAutor();
}


/*
 * ==================================================
 * DTO BASE
 * ==================================================
 */

abstract class PublicacionDTO {

    private final Long id;
    private final String autor;
    private final String titulo;

    private final int meGusta;
    private final int comentarios;
    private final int compartidos;


    protected PublicacionDTO(
            Long id,
            String autor,
            String titulo,
            int meGusta,
            int comentarios,
            int compartidos
    ) {

        this.id = id;
        this.autor = autor;
        this.titulo = titulo;
        this.meGusta = meGusta;
        this.comentarios = comentarios;
        this.compartidos = compartidos;
    }


    public Long getId() {
        return id;
    }

    public String getAutor() {
        return autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getMeGusta() {
        return meGusta;
    }

    public int getComentarios() {
        return comentarios;
    }

    public int getCompartidos() {
        return compartidos;
    }
}


/*
 * ==================================================
 * DTO TEXTO
 * ==================================================
 */

final class PublicacionTextoDTO
        extends PublicacionDTO {

    private final String texto;


    public PublicacionTextoDTO(
            Long id,
            String autor,
            String titulo,
            int meGusta,
            int comentarios,
            int compartidos,
            String texto
    ) {

        super(
                id,
                autor,
                titulo,
                meGusta,
                comentarios,
                compartidos
        );

        this.texto = texto;
    }


    public String getTexto() {
        return texto;
    }
}


/*
 * ==================================================
 * DTO IMAGEN
 * ==================================================
 */

final class PublicacionImagenDTO
        extends PublicacionDTO {

    private final String descripcionImagen;
    private final String formato;


    public PublicacionImagenDTO(
            Long id,
            String autor,
            String titulo,
            int meGusta,
            int comentarios,
            int compartidos,
            String descripcionImagen,
            String formato
    ) {

        super(
                id,
                autor,
                titulo,
                meGusta,
                comentarios,
                compartidos
        );

        this.descripcionImagen =
                descripcionImagen;

        this.formato =
                formato;
    }


    public String getDescripcionImagen() {
        return descripcionImagen;
    }

    public String getFormato() {
        return formato;
    }
}


/*
 * ==================================================
 * DTO VIDEO
 * ==================================================
 */

final class PublicacionVideoDTO
        extends PublicacionDTO {

    private final int duracionSegundos;
    private final int visualizaciones;


    public PublicacionVideoDTO(
            Long id,
            String autor,
            String titulo,
            int meGusta,
            int comentarios,
            int compartidos,
            int duracionSegundos,
            int visualizaciones
    ) {

        super(
                id,
                autor,
                titulo,
                meGusta,
                comentarios,
                compartidos
        );

        this.duracionSegundos =
                duracionSegundos;

        this.visualizaciones =
                visualizaciones;
    }


    public int getDuracionSegundos() {
        return duracionSegundos;
    }

    public int getVisualizaciones() {
        return visualizaciones;
    }
}


/*
 * ==================================================
 * SOLUCION DE DOMINIO - VIBE
 * ==================================================
 */

enum FormatoImagen {
    JPG, PNG, WEBP
}

enum EstadoPublicacion {
    RECHAZADA, PUBLICADA, DESTACADA, VIRAL
}


/*
 * Resultado inmutable del procesamiento: clase final,
 * atributos private final y solo metodos de consulta.
 */
final class ResultadoPublicacion {

    private final Long idPublicacion;
    private final String autor;
    private final String descripcion;
    private final double relevancia;
    private final EstadoPublicacion estado;
    private final LocalDateTime fechaProcesamiento;

    public ResultadoPublicacion(
            Long idPublicacion,
            String autor,
            String descripcion,
            double relevancia,
            EstadoPublicacion estado,
            LocalDateTime fechaProcesamiento
    ) {
        this.idPublicacion = idPublicacion;
        this.autor = autor;
        this.descripcion = descripcion;
        this.relevancia = relevancia;
        this.estado = estado;
        this.fechaProcesamiento = fechaProcesamiento;
    }

    public Long getIdPublicacion() {
        return idPublicacion;
    }

    public String getAutor() {
        return autor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getRelevancia() {
        return relevancia;
    }

    public EstadoPublicacion getEstado() {
        return estado;
    }

    public LocalDateTime getFechaProcesamiento() {
        return fechaProcesamiento;
    }

    @Override
    public String toString() {
        return String.format(
                Locale.US,
                "%d|%s|%.2f|%s",
                idPublicacion,
                descripcion,
                relevancia,
                estado
        );
    }
}


/*
 * Clase base de la jerarquia. Aplica TEMPLATE METHOD:
 * publicar() es final y define el algoritmo; las
 * subclases solo implementan los pasos variables
 * (validarContenido, calcularRelevancia, getDescripcion).
 */
abstract class Publicacion implements Publicable {

    private static final double UMBRAL_DESTACADA = 100.0;
    private static final double UMBRAL_VIRAL = 300.0;

    private final Long id;
    private final String autor;
    private final String titulo;
    private final LocalDateTime fechaCreacion;
    private final int meGusta;
    private final int comentarios;
    private final int compartidos;

    protected Publicacion(
            Long id,
            String autor,
            String titulo,
            int meGusta,
            int comentarios,
            int compartidos
    ) {
        this.id = id;
        this.autor = autor;
        this.titulo = titulo;
        this.fechaCreacion = LocalDateTime.now();
        this.meGusta = meGusta;
        this.comentarios = comentarios;
        this.compartidos = compartidos;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getAutor() {
        return autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public int getMeGusta() {
        return meGusta;
    }

    public int getComentarios() {
        return comentarios;
    }

    public int getCompartidos() {
        return compartidos;
    }

    // Paso 1: validacion comun a todas las publicaciones.
    protected boolean validarDatosGenerales() {
        return id != null && id > 0
                && autor != null && !autor.trim().isEmpty()
                && titulo != null && !titulo.trim().isEmpty()
                && meGusta >= 0
                && comentarios >= 0
                && compartidos >= 0;
    }

    // Pasos variables (los resuelve cada subclase por polimorfismo).
    protected abstract boolean validarContenido();

    protected abstract double calcularRelevancia();

    protected abstract String getDescripcion();

    // Paso 4: clasificacion por umbrales de relevancia.
    private EstadoPublicacion determinarEstado(boolean esValida, double relevancia) {
        if (!esValida) {
            return EstadoPublicacion.RECHAZADA;
        }
        if (relevancia >= UMBRAL_VIRAL) {
            return EstadoPublicacion.VIRAL;
        }
        if (relevancia >= UMBRAL_DESTACADA) {
            return EstadoPublicacion.DESTACADA;
        }
        return EstadoPublicacion.PUBLICADA;
    }

    // METODO PLANTILLA: final para que ninguna subclase altere el algoritmo.
    @Override
    public final ResultadoPublicacion publicar() {
        boolean datosValidos = validarDatosGenerales();                  // 1
        boolean esValida = datosValidos && validarContenido();           // 2
        double relevancia = esValida ? calcularRelevancia() : 0.0;       // 3
        EstadoPublicacion estado = determinarEstado(esValida, relevancia); // 4
        String descripcion = getDescripcion();                           // 5

        return new ResultadoPublicacion(                                 // 6
                id,
                autor,
                descripcion,
                relevancia,
                estado,
                LocalDateTime.now()
        );
    }
}


final class PublicacionTexto extends Publicacion {

    private static final int LONGITUD_MINIMA = 10;
    private static final int LONGITUD_MAXIMA = 280;

    private final String texto;

    public PublicacionTexto(
            Long id,
            String autor,
            String titulo,
            int meGusta,
            int comentarios,
            int compartidos,
            String texto
    ) {
        super(id, autor, titulo, meGusta, comentarios, compartidos);
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    @Override
    protected boolean validarContenido() {
        if (texto == null) {
            return false;
        }
        int longitud = texto.trim().length();
        return longitud >= LONGITUD_MINIMA && longitud <= LONGITUD_MAXIMA;
    }

    @Override
    protected double calcularRelevancia() {
        return getMeGusta()
                + getComentarios() * 3.0
                + getCompartidos() * 5.0;
    }

    @Override
    protected String getDescripcion() {
        return String.format("Texto | %s | %s", getTitulo(), getAutor());
    }
}


final class PublicacionImagen extends Publicacion {

    private final String descripcionImagen;
    private final FormatoImagen formato;

    public PublicacionImagen(
            Long id,
            String autor,
            String titulo,
            int meGusta,
            int comentarios,
            int compartidos,
            String descripcionImagen,
            FormatoImagen formato
    ) {
        super(id, autor, titulo, meGusta, comentarios, compartidos);
        this.descripcionImagen = descripcionImagen;
        this.formato = formato;
    }

    public String getDescripcionImagen() {
        return descripcionImagen;
    }

    public FormatoImagen getFormato() {
        return formato;
    }

    @Override
    protected boolean validarContenido() {
        return descripcionImagen != null
                && !descripcionImagen.trim().isEmpty()
                && formato != null;
    }

    @Override
    protected double calcularRelevancia() {
        return getMeGusta() * 2.0
                + getComentarios() * 3.0
                + getCompartidos() * 5.0;
    }

    @Override
    protected String getDescripcion() {
        return String.format(
                "Imagen | %s | %s | %s",
                getTitulo(),
                formato,
                getAutor()
        );
    }
}


final class PublicacionVideo extends Publicacion {

    private static final int DURACION_MAXIMA = 600;

    private final int duracionSegundos;
    private int visualizaciones;

    public PublicacionVideo(
            Long id,
            String autor,
            String titulo,
            int meGusta,
            int comentarios,
            int compartidos,
            int duracionSegundos,
            int visualizaciones
    ) {
        super(id, autor, titulo, meGusta, comentarios, compartidos);
        this.duracionSegundos = duracionSegundos;
        this.visualizaciones = visualizaciones;
    }

    public int getDuracionSegundos() {
        return duracionSegundos;
    }

    public int getVisualizaciones() {
        return visualizaciones;
    }

    // Unica forma de modificar las visualizaciones (sin setter publico).
    // Si el estado es invalido (negativo) no se modifica.
    public void registrarVisualizacion() {
        if (visualizaciones >= 0) {
            visualizaciones++;
        }
    }

    @Override
    protected boolean validarContenido() {
        return duracionSegundos > 0
                && duracionSegundos <= DURACION_MAXIMA
                && visualizaciones >= 0;
    }

    @Override
    protected double calcularRelevancia() {
        return getMeGusta() * 2.0
                + getComentarios() * 4.0
                + getCompartidos() * 6.0
                + visualizaciones / 100.0;
    }

    @Override
    protected String getDescripcion() {
        return String.format(
                "Video | %s | %d s | %s",
                getTitulo(),
                duracionSegundos,
                getAutor()
        );
    }
}


/*
 * Feed depende solo de la abstraccion Publicable (DIP).
 * No usa instanceof, if ni switch sobre el tipo concreto:
 * cada publicacion resuelve su comportamiento por polimorfismo.
 */
class Feed {

    private final List<ResultadoPublicacion> publicaciones;

    public Feed() {
        this.publicaciones = new ArrayList<>();
    }

    // Copia defensiva: nadie externo puede alterar la lista interna.
    public List<ResultadoPublicacion> getPublicaciones() {
        return new ArrayList<>(publicaciones);
    }

    public ResultadoPublicacion agregar(Publicable publicacion) {
        if (publicacion == null) {
            throw new IllegalArgumentException("La publicacion no puede ser nula");
        }
        ResultadoPublicacion resultado = publicacion.publicar();
        publicaciones.add(resultado);
        return resultado;
    }

    public List<ResultadoPublicacion> agregar(List<Publicable> lista) {
        if (lista == null) {
            throw new IllegalArgumentException("La coleccion de publicaciones no puede ser nula");
        }
        // Se valida toda la coleccion ANTES de procesar para que una
        // operacion invalida conserve el estado previo del Feed.
        for (Publicable publicacion : lista) {
            if (publicacion == null) {
                throw new IllegalArgumentException("La coleccion contiene publicaciones nulas");
            }
        }
        List<ResultadoPublicacion> resultados = new ArrayList<>();
        for (Publicable publicacion : lista) {
            resultados.add(agregar(publicacion)); // reutiliza el metodo individual
        }
        return resultados;
    }

    public ResultadoPublicacion procesar(Publicable publicacion) {
        return agregar(publicacion);
    }

    public List<ResultadoPublicacion> procesar(List<Publicable> lista) {
        return agregar(lista);
    }

    public List<ResultadoPublicacion> obtenerVirales() {
        List<ResultadoPublicacion> virales = new ArrayList<>();
        for (ResultadoPublicacion resultado : publicaciones) {
            if (resultado.getEstado() == EstadoPublicacion.VIRAL) {
                virales.add(resultado);
            }
        }
        return virales;
    }

    // List.sort es estable: en empate se conserva el orden de procesamiento.
    public List<ResultadoPublicacion> generarRanking() {
        List<ResultadoPublicacion> ranking = new ArrayList<>(publicaciones);
        ranking.sort(
                Comparator.comparingDouble(ResultadoPublicacion::getRelevancia)
                        .reversed()
        );
        return ranking;
    }
}


/*
 * ==================================================
 * ADAPTADOR ENTRE HACKERRANK Y TU DOMINIO
 * ==================================================
 */

class Result {

    static {
        Locale.setDefault(Locale.US);
    }

    public static Publicable convertir(
            PublicacionTextoDTO entrada
    ) {
        return new PublicacionTexto(
                entrada.getId(),
                entrada.getAutor(),
                entrada.getTitulo(),
                entrada.getMeGusta(),
                entrada.getComentarios(),
                entrada.getCompartidos(),
                entrada.getTexto()
        );
    }

    public static Publicable convertir(
            PublicacionImagenDTO entrada
    ) {
        FormatoImagen formato = null;
        if (entrada.getFormato() != null && !entrada.getFormato().isBlank()) {
            try {
                formato = FormatoImagen.valueOf(entrada.getFormato().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                formato = null; // formato desconocido -> la publicacion sera RECHAZADA
            }
        }

        return new PublicacionImagen(
                entrada.getId(),
                entrada.getAutor(),
                entrada.getTitulo(),
                entrada.getMeGusta(),
                entrada.getComentarios(),
                entrada.getCompartidos(),
                entrada.getDescripcionImagen(),
                formato
        );
    }

    public static Publicable convertir(
            PublicacionVideoDTO entrada
    ) {
        return new PublicacionVideo(
                entrada.getId(),
                entrada.getAutor(),
                entrada.getTitulo(),
                entrada.getMeGusta(),
                entrada.getComentarios(),
                entrada.getCompartidos(),
                entrada.getDuracionSegundos(),
                entrada.getVisualizaciones()
        );
    }
}




final class HackerRankVibe {

    private HackerRankVibe() {
    }


    public static void ejecutar()
            throws Exception {

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                System.in
                        )
                );


        String primeraLinea =
                reader.readLine();


        if (primeraLinea == null
                || primeraLinea.isBlank()) {

            return;
        }


        int numeroPublicaciones =
                Integer.parseInt(
                        primeraLinea.trim()
                );


        List<Publicable> publicaciones =
                new ArrayList<>();


        /*
         * ==============================================
         * ENTRADA
         * ==============================================
         */

        for (int i = 0;
             i < numeroPublicaciones;
             i++) {

            String linea =
                    reader.readLine();


            if (linea == null) {

                throw new IllegalArgumentException(
                        "Faltan publicaciones"
                );
            }


            Publicable publicacion =
                    convertirEntrada(
                            linea
                    );


            if (publicacion == null) {

                throw new IllegalStateException(
                        "La conversion regreso null"
                );
            }


            publicaciones.add(
                    publicacion
            );
        }


        /*
         * ==============================================
         * PROCESAMIENTO INDIVIDUAL
         * ==============================================
         */

        Feed feedIndividual =
                new Feed();


        for (Publicable publicacion
                : publicaciones) {

            ResultadoPublicacion resultado =
                    feedIndividual.procesar(
                            publicacion
                    );


            imprimirResultado(
                    resultado
            );
        }


        /*
         * ==============================================
         * PROCESAMIENTO DE COLECCION
         * ==============================================
         */

        Feed feed =
                new Feed();


        feed.procesar(
                publicaciones
        );


        /*
         * ==============================================
         * RANKING
         * ==============================================
         */

        System.out.println(
                "RANKING"
        );


        List<ResultadoPublicacion> ranking =
                feed.generarRanking();


        for (ResultadoPublicacion resultado
                : ranking) {

            imprimirRanking(
                    resultado
            );
        }


        /*
         * ==============================================
         * VIRALES
         * ==============================================
         */

        System.out.println(
                "VIRALES"
        );


        List<ResultadoPublicacion> virales =
                feed.obtenerVirales();


        for (ResultadoPublicacion resultado
                : virales) {

            imprimirViral(
                    resultado
            );
        }
    }


    /*
     * ==============================================
     * IDENTIFICACION DEL TIPO DE ENTRADA
     * ==============================================
     */

    private static Publicable convertirEntrada(
            String linea
    ) {

        if (linea == null
                || linea.isBlank()) {

            throw new IllegalArgumentException(
                    "La linea no puede estar vacia"
            );
        }


        String[] partes =
                linea.split(
                        "\\|",
                        -1
                );


        String tipo =
                partes[0]
                        .trim()
                        .toUpperCase();


        switch (tipo) {

            case "TEXTO":
                return convertirTexto(
                        partes
                );

            case "IMAGEN":
                return convertirImagen(
                        partes
                );

            case "VIDEO":
                return convertirVideo(
                        partes
                );

            default:
                throw new IllegalArgumentException(
                        "Tipo de publicacion invalido"
                );
        }
    }


    /*
     * ==============================================
     * TEXTO
     * ==============================================
     */

    private static Publicable convertirTexto(
            String[] partes
    ) {

        if (partes.length != 8) {

            throw new IllegalArgumentException(
                    "Formato TEXTO invalido"
            );
        }


        PublicacionTextoDTO entrada =
                new PublicacionTextoDTO(

                        Long.parseLong(
                                partes[1].trim()
                        ),

                        partes[2].trim(),

                        partes[3].trim(),

                        Integer.parseInt(
                                partes[4].trim()
                        ),

                        Integer.parseInt(
                                partes[5].trim()
                        ),

                        Integer.parseInt(
                                partes[6].trim()
                        ),

                        partes[7].trim()
                );


        return Result.convertir(
                entrada
        );
    }


    /*
     * ==============================================
     * IMAGEN
     * ==============================================
     */

    private static Publicable convertirImagen(
            String[] partes
    ) {

        if (partes.length != 9) {

            throw new IllegalArgumentException(
                    "Formato IMAGEN invalido"
            );
        }


        PublicacionImagenDTO entrada =
                new PublicacionImagenDTO(

                        Long.parseLong(
                                partes[1].trim()
                        ),

                        partes[2].trim(),

                        partes[3].trim(),

                        Integer.parseInt(
                                partes[4].trim()
                        ),

                        Integer.parseInt(
                                partes[5].trim()
                        ),

                        Integer.parseInt(
                                partes[6].trim()
                        ),

                        partes[7].trim(),

                        partes[8]
                                .trim()
                                .toUpperCase()
                );


        return Result.convertir(
                entrada
        );
    }


    /*
     * ==============================================
     * VIDEO
     * ==============================================
     */

    private static Publicable convertirVideo(
            String[] partes
    ) {

        if (partes.length != 9) {

            throw new IllegalArgumentException(
                    "Formato VIDEO invalido"
            );
        }


        PublicacionVideoDTO entrada =
                new PublicacionVideoDTO(

                        Long.parseLong(
                                partes[1].trim()
                        ),

                        partes[2].trim(),

                        partes[3].trim(),

                        Integer.parseInt(
                                partes[4].trim()
                        ),

                        Integer.parseInt(
                                partes[5].trim()
                        ),

                        Integer.parseInt(
                                partes[6].trim()
                        ),

                        Integer.parseInt(
                                partes[7].trim()
                        ),

                        Integer.parseInt(
                                partes[8].trim()
                        )
                );


        return Result.convertir(
                entrada
        );
    }


    /*
     * ==============================================
     * SALIDA PRINCIPAL
     * ==============================================
     */

    private static void imprimirResultado(
            ResultadoPublicacion resultado
    ) {

        System.out.printf(
                "%d|%s|%.2f|%s%n",

                resultado.getIdPublicacion(),

                resultado.getDescripcion(),

                resultado.getRelevancia(),

                resultado.getEstado()
        );
    }


    /*
     * ==============================================
     * RANKING
     * ==============================================
     */

    private static void imprimirRanking(
            ResultadoPublicacion resultado
    ) {

        System.out.printf(
                "%d|%.2f|%s%n",

                resultado.getIdPublicacion(),

                resultado.getRelevancia(),

                resultado.getEstado()
        );
    }


    /*
     * ==============================================
     * VIRALES
     * ==============================================
     */

    private static void imprimirViral(
            ResultadoPublicacion resultado
    ) {

        System.out.printf(
                "%d|%s|%.2f%n",

                resultado.getIdPublicacion(),

                resultado.getDescripcion(),

                resultado.getRelevancia()
        );
    }
}


/*
 * ==================================================
 * MAIN (en HackerRank: public class Solution)
 * ==================================================
 */

public class Practica11 {

    public static void main(String[] args)
            throws Exception {

        HackerRankVibe.ejecutar();
    }
}
