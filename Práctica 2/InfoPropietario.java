
//Salinas Luciano Ludwin Cadmiel
public class InfoPropietario {
    private String nombre;
    private String universidad; 

    // this 
    public InfoPropietario(String nombre, String universidad) {
        this.nombre = nombre;
        this.universidad = universidad;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUniversidad() {
        return universidad;
    }

    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }
}