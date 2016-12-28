package cat.olivadevelop.myprojectorganizer.managers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cat.olivadevelop.myprojectorganizer.tools.Tools;

import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_create_data;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_descript;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_dir_files;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_flag_activo;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_flag_finish;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_home_img;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_id_project;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_images;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_last_update;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_name;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_targets;

/**
 * Created by Oliva on 03/11/2016.
 * <p>
 * Maneja un proyecto.
 */

public class Project {

    private String id;
    private boolean empty;
    private boolean flagActivo;
    private boolean flagFinished;
    private String name;
    private String createDate;
    private String lastUpdate;
    private String userRoot;
    private String homeImage;
    private String description;
    private JSONArray projectImages;
    private JSONArray tarjetas;

    public Project() {
    }

    /* Getters y Setters*/
    public String getId() {
        return id;
    }

    public void setIdProject(String id) {
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

    public String getUserRoot() {
        return userRoot;
    }

    public void setUserRoot(String userRoot) {
        this.userRoot = userRoot;
    }

    public JSONArray getProjectImages() {
        return projectImages;
    }

    public void setProjectImages(JSONArray projectImages) {
        this.projectImages = projectImages;
    }

    public JSONArray getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(JSONArray tarjetas) {
        this.tarjetas = tarjetas;
    }

    public boolean isFlagActivo() {
        return flagActivo;
    }

    public void setFlagActivo(boolean flagActivo) {
        this.flagActivo = flagActivo;
    }

    public boolean isFlagFinished() {
        return flagFinished;
    }

    public void setFlagFinished(boolean flagFinished) {
        this.flagFinished = flagFinished;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomeImage() {
        return Tools.HOSTNAME + "/" + Tools.CLIENT_DIR + "/" + Tools.getUserID() + "/" + getUserRoot() + "/" + homeImage;
    }

    public void setHomeImage(String homeImage) {
        this.homeImage = homeImage;
    }

    public String mountUrlImage(String name) {
        return Tools.HOSTNAME + "/" + Tools.CLIENT_DIR + "/" + Tools.getUserID() + "/" + getUserRoot() + "/" + Tools.IMAGE_DIR + "/" + name;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(json_project_id_project, getId());
        json.put(json_project_name, getName());
        json.put(json_project_descript, getDescription());
        json.put(json_project_flag_activo, isFlagActivo());
        json.put(json_project_flag_finish, isFlagFinished());
        json.put(json_project_create_data, getCreateDate());
        json.put(json_project_last_update, getLastUpdate());
        json.put(json_project_dir_files, getUserRoot());
        json.put(json_project_home_img, homeImage);
        json.put(json_project_images, getProjectImages());
        json.put(json_project_targets, getTarjetas());
        return json;
    }
}
