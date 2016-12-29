package cat.olivadevelop.myprojectorganizer.managers;

/**
 * Created by Oliva on 29/12/2016.
 */

public class User {
    private String id_usuario;
    private String correo;
    private String fecha_alta;
    private String flag_activo;

    public User() {
    }

    public User(String id_usuario, String correo) {
        this.id_usuario = id_usuario;
        this.correo = correo;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFecha_alta() {
        return fecha_alta;
    }

    public void setFecha_alta(String fecha_alta) {
        this.fecha_alta = fecha_alta;
    }

    public String getFlag_activo() {
        return flag_activo;
    }

    public void setFlag_activo(String flag_activo) {
        this.flag_activo = flag_activo;
    }
}
