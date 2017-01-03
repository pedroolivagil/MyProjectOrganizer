package cat.olivadevelop.myprojectorganizer.entities;

/**
 * Created by Oliva on 30/12/2016.
 */

public class Pais {
    private int id;
    private String ISO;
    private String nombre;

    public Pais() {
    }

    public Pais(int id, String ISO, String nombre) {
        this.id = id;
        this.ISO = ISO;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getISO() {
        return ISO;
    }

    public void setISO(String ISO) {
        this.ISO = ISO;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        String name;
        if (getNombre().length() > 20) {
            name = getNombre().substring(0, 20).concat("...");
        } else {
            name = getNombre();
        }
        String iso = "";
        if (!getISO().equals("00")) {
            iso = ("(" + getISO().toUpperCase() + ")").concat(" ");
        }
        return iso.concat(name);
    }
}
