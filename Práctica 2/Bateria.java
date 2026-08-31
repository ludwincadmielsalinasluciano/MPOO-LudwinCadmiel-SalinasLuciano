
//Salinas Luciano Ludwin Cadmiel

public class Bateria {
    private String fabricante;
    private int nivelActual;

    //this
    public Bateria(String fabricante, int nivelActual) {
        this.fabricante = fabricante;
        this.nivelActual = nivelActual;
    }

    public String getFabricante() {
        return fabricante;
    }

    public int getNivelActual() {
        return nivelActual;
    }

    // En lugar de usar un set directo, la batería sabe cómo gastarse a sí misma
    public void consumirEnergia(int cantidad) {
        if (this.nivelActual - cantidad >= 0) {
            this.nivelActual -= cantidad;
        } else {
            this.nivelActual = 0; // La batería no baja de 0
        }
    }
}