package cat.olivadevelop.myprojectorganizer.entities;

import cat.olivadevelop.myprojectorganizer.tools.Tools;

/**
 * Created by Oliva on 29/12/2016.
 */

public class User {
    private String id_usuario;
    private String correo;
    private String password;
    private String fecha_alta;
    private String flag_activo;
    // optionals
    private String nif;
    private String phone;
    private Pais pais;
    private String poblacion;

    public User() {
    }

    public User(String correo, String password) {
        this.correo = correo;
        this.password = password;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public void setEncryptedID(String id_usuario) {
        this.id_usuario = Tools.encrypt(id_usuario);
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }
}
