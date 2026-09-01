
//Salinas Luciano Ludwin Cadmiel
import java.util.UUID;
import java.util.Random;

public class Robot {
    private UUID id;
    private String nombreRobot;
    private double peso;
    private double velocidadActual;
    private int velocidadMaxima;
    private InfoPropietario propietario;
    private Bateria bateria;

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

    public void acelerar(double incremento) {
        if (this.velocidadActual + incremento <= this.velocidadMaxima) {
            this.velocidadActual += incremento;
        } else {
            this.velocidadActual = this.velocidadMaxima;
        }
        this.bateria.consumirEnergia(5); 
    }

    public void detenerse() {
        this.velocidadActual = 0.0;
    }

    public void recargarBateria(int cantidad) {
        int nuevoNivel = this.bateria.getNivelActual() + cantidad;
        if (nuevoNivel > 100) {
            this.bateria.setNivelActual(100);
        } else {
            this.bateria.setNivelActual(nuevoNivel);
        }
    }

    public double calcularTiempoRecorrido(double distanciaKm) {
        if (this.velocidadActual == 0) {
            return -1.0; 
        }
        return distanciaKm / this.velocidadActual;
    }

    public void simularEnfrentamiento() {
        Random random = new Random();
        
        int danioRecibido = random.nextInt(31);
        
        double velocidadVariable = 5.0 + random.nextDouble() * 15.5;
        
        System.out.println("\n--- SIMULACIÓN DE BATALLA ---");
        System.out.println("Robot: " + this.nombreRobot);
        System.out.println("Impacto recibido con daño de: " + danioRecibido);
        System.out.println("Variación de velocidad en terreno: " + String.format("%.2f", velocidadVariable) + " km/h");
        
        this.velocidadActual = velocidadVariable;
        
        int bateriaActual = this.bateria.getNivelActual();
        int nuevaBateria = bateriaActual - (danioRecibido / 2);
        
        if (nuevaBateria < 0) {
            this.bateria.setNivelActual(0);
        } else {
            this.bateria.setNivelActual(nuevaBateria);
        }
        
        System.out.println("Nueva velocidad ajustada: " + String.format("%.2f", this.velocidadActual) + " km/h");
        System.out.println("Batería restante tras el combate: " + this.bateria.getNivelActual() + "%");
    }

    public static void main(String[] args) {
        InfoPropietario prop = new InfoPropietario("Ludwin", "UNAM");
        Bateria bat = new Bateria("Stark Industries", 100);
        Robot bot = new Robot("Optimus", 25.0, 0.0, 40, prop, bat);

        System.out.println("--- PRUEBAS DE COMPORTAMIENTO Y BATALLA ---");
        
        bot.acelerar(15.0);
        System.out.println("Velocidad tras acelerar: " + bot.getVelocidadActual() + " km/h");

        bot.simularEnfrentamiento();
    }
}