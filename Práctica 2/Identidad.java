
//Salinas Luciano Ludwin Cadmiel
import java.util.UUID;

public class Identidad {
    
    // Atributos del robot
    private UUID id;
    private String nombre;
    private int bateriaActual;
    private double peso;
    private double velocidadActual;
    private int velocidadMaxima;

    // Clase
    public Identidad(String nombre, int bateriaActual, double peso, double velocidadActual, int velocidadMaxima) {
        this.id = UUID.randomUUID(); // Genera un identificador único automáticamente
        this.nombre = nombre;
        this.bateriaActual = bateriaActual;
        this.peso = peso;
        this.velocidadActual = velocidadActual;
        this.velocidadMaxima = velocidadMaxima;
    }

    // Consultar los atributos
    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getBateriaActual() {
        return bateriaActual;
    }

    public double getPeso() {
        return peso;
    }

    public double getVelocidadActual() {
        return velocidadActual;
    }

    public int getVelocidadMaxima() {
        return velocidadMaxima;
    }

    // Metodo main
    public static void main(String[] args) {
        // Creamos tres robots pasando los valores requeridos en el constructor
        Identidad robot1 = new Identidad("Alpha", 100, 15.5, 0.0, 20);
        Identidad robot2 = new Identidad("Beta", 85, 12.0, 5.5, 25);
        Identidad robot3 = new Identidad("Alpha", 90, 14.2, 2.1, 18); // aquí tengo el mismo nombre pero diferente entidad y datos

        // Imprimimos la informacion de cada robot para verificar y ver que no nos hayamos equivocado 
        System.out.println("--- Robot 1 ---");
        System.out.println("ID: " + robot1.getId());
        System.out.println("Nombre: " + robot1.getNombre() + " | Batería: " + robot1.getBateriaActual() + "% | Peso: " + robot1.getPeso() + "kg | Vel. Actual: " + robot1.getVelocidadActual() + " m/s | Vel. Máxima: " + robot1.getVelocidadMaxima() + " m/s\n");

        System.out.println("--- Robot 2 ---");
        System.out.println("ID: " + robot2.getId());
        System.out.println("Nombre: " + robot2.getNombre() + " | Batería: " + robot2.getBateriaActual() + "% | Peso: " + robot2.getPeso() + "kg | Vel. Actual: " + robot2.getVelocidadActual() + " m/s | Vel. Máxima: " + robot2.getVelocidadMaxima() + " m/s\n");

        System.out.println("--- Robot 3 ---");
        System.out.println("ID: " + robot3.getId());
        System.out.println("Nombre: " + robot3.getNombre() + " | Batería: " + robot3.getBateriaActual() + "% | Peso: " + robot3.getPeso() + "kg | Vel. Actual: " + robot3.getVelocidadActual() + " m/s | Vel. Máxima: " + robot3.getVelocidadMaxima() + " m/s");
    }
}