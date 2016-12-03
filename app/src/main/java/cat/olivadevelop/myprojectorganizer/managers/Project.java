package cat.olivadevelop.myprojectorganizer.managers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cat.olivadevelop.myprojectorganizer.tools.Tools;

import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.FINISH_PJT;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_descript;

/**
 * Created by Oliva on 03/11/2016.
 * <p>
 * Maneja un proyecto.
 */

public class Project {
    /*
    "id_project": 0,
    "name": "Proyecto de prueba simple",
    "create_data": "23-10-2016",
    "last_update": "23-10-2016",
    "dir_files": "/project0",
    "home_img": "home.jpg",
    "images": [
        "url 1",
        "url 2",
        "url 3"
    ],
    "form": {
        "Agradecimientos": "A mi pareja",
        "description": "Descripción simple para el proyecto de prueba. Esto tiene un límite de caracteres que se mostrarán como información en la pantalla de proyectos.",
        "Autor": "Pedro Oliva Gil (OlivaDevelop)",
        "Tiempo": "4 semanas",
        "finished": "false"
    }
    */
    private int id;
    private String name;
    private String createDate;
    private String lastUpdate;
    private String homeDir;
    private String homeImage;
    private JSONArray urlImages;
    private JSONObject form;
    private boolean empty;
    private boolean flag_activo;

    public Project() {
    }

    /* Getters y Setters*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public String getName() {
        return name.trim();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getHomeDir() {
        return homeDir;
    }

    public void setHomeDir(String homeDir) {
        this.homeDir = homeDir;
    }

    public String mountUrlImage(String name) {
        //return homeImage;
        return Tools.HOSTNAME + "/clients/" + Tools.getUserID() +
                getHomeDir() + "/img/" + name;
    }

    public String getHomeImage() {
        //return homeImage;
        return Tools.HOSTNAME + "/clients/" + Tools.getUserID() +
                getHomeDir() + "/" + homeImage;
    }

    public void setHomeImage(String homeImage) {
        this.homeImage = homeImage;
    }

    public JSONArray getUrlImages() {
        return urlImages;
    }

    public void setUrlImages(JSONArray urlImages) {
        this.urlImages = urlImages;
    }

    public JSONObject getForm() {
        return form;
    }

    public void setForm(JSONObject form) {
        this.form = form;
    }

    public String getDescription() {
        try {
            return getForm().getString(json_project_descript).trim();
        } catch (JSONException e) {
            return null;
        }
    }

    public boolean isFinished() {
        try {
            return getForm().getBoolean(FINISH_PJT);
        } catch (JSONException e) {
            return false;
        }
    }

    public boolean isFlag_activo() {
        return flag_activo;
    }

    public void setFlag_activo(boolean flag_activo) {
        this.flag_activo = flag_activo;
    }
}
