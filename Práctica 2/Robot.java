
//Salinas Luciano Ludwin Cadmiel
import java.util.UUID;

public class Robot {
    private UUID id;
    private String nombreRobot;
    private double peso;
    private double velocidadActual;
    private int velocidadMaxima;
    
    // Composición
    private InfoPropietario propietario;
    private Bateria bateria;

    // this
    public Robot(String nombreRobot, double peso, double velocidadActual, int velocidadMaxima, InfoPropietario propietario, Bateria bateria) {
        this.id = UUID.randomUUID(); 
        this.nombreRobot = nombreRobot;
        this.peso = peso;
        this.velocidadActual = velocidadActual;
        this.velocidadMaxima = velocidadMaxima;
        this.propietario = propietario;
        this.bateria = bateria;
    }

    public UUID getId() { return id; }
    public String getNombreRobot() { return nombreRobot; }
    public double getPeso() { return peso; }
    public double getVelocidadActual() { return velocidadActual; }
    public int getVelocidadMaxima() { return velocidadMaxima; }
    public InfoPropietario getPropietario() { return propietario; }
    public Bateria getBateria() { return bateria; }

    // Acelerar 
    public void acelerar(double incremento) {
        if (this.velocidadActual + incremento <= this.velocidadMaxima) {
            this.velocidadActual += incremento;
        } else {
            this.velocidadActual = this.velocidadMaxima; 
        }
    }

    // Método main
    public static void main(String[] args) {
        InfoPropietario propietario = new InfoPropietario("Ludwin", "UNAM");
        Bateria bateria = new Bateria("Duracell Pro", 100);

        Robot miRobot = new Robot("Titan", 14.5, 0.0, 30, propietario, bateria);

        System.out.println("Robot creado con éxito: " + miRobot.getNombreRobot());
        System.out.println("ID Asignado: " + miRobot.getId());
        System.out.println("Velocidad inicial: " + miRobot.getVelocidadActual());

        // Probando acciones 
        miRobot.acelerar(10.5);
        System.out.println("Velocidad tras acelerar: " + miRobot.getVelocidadActual());
    }
}