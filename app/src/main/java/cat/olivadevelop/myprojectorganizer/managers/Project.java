package cat.olivadevelop.myprojectorganizer.managers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cat.olivadevelop.myprojectorganizer.tools.Tools;

import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.FINISH_PJT;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_create_data;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_descript;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_dir_files;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_flag_activo;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_form;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_home_img;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_id_project;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_images;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_last_update;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_name;

/**
 * Created by Oliva on 03/11/2016.
 * <p>
 * Maneja un proyecto.
 */

public class Project {

    private int id;
    private String name;
    private String createDate;
    private String lastUpdate;
    private String homeDir;
    private String homeImage;
    private JSONArray urlImages;
    private JSONObject form;
    private boolean empty;
    private boolean flagActivo;

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
        return Tools.HOSTNAME + "/" + Tools.CLIENT_DIR + "/" + Tools.getUserID() + getHomeDir() + "/" + Tools.IMAGE_DIR + "/" + name;
    }

    public String getHomeImage() {
        return Tools.HOSTNAME + "/" + Tools.CLIENT_DIR + "/" + Tools.getUserID() + getHomeDir() + "/" + homeImage;
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

    public boolean isFlagActivo() {
        return flagActivo;
    }

    public void setFlagActivo(boolean flagActivo) {
        this.flagActivo = flagActivo;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(json_project_id_project, getId());
        json.put(json_project_name, getName());
        json.put(json_project_flag_activo, isFlagActivo());
        json.put(json_project_create_data, getCreateDate());
        json.put(json_project_last_update, getLastUpdate());
        json.put(json_project_dir_files, getHomeDir());
        json.put(json_project_home_img, "home.jpg");
        json.put(json_project_images, getUrlImages());
        json.put(json_project_form, getForm());
        return json;
    }
}
