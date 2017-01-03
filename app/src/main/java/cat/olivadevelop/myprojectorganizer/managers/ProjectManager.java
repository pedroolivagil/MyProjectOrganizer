package cat.olivadevelop.myprojectorganizer.managers;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cat.olivadevelop.myprojectorganizer.entities.Project;
import cat.olivadevelop.myprojectorganizer.manageData.CreateProject;
import cat.olivadevelop.myprojectorganizer.manageData.DeleteProject;
import cat.olivadevelop.myprojectorganizer.manageData.MainLoader;
import cat.olivadevelop.myprojectorganizer.manageData.ProjectSelectedLoader;
import cat.olivadevelop.myprojectorganizer.tools.Tools;
import okhttp3.FormBody;

import static cat.olivadevelop.myprojectorganizer.tools.Tools.HOSTNAME;

/**
 * Created by Oliva on 22/10/2016.
 */

public abstract class ProjectManager {
    public static final boolean TYPE_SORT_BY_ASC = true;
    public static final boolean TYPE_SORT_BY_DESC = false;
    public static final int MAX_IMAGES_PROJECT = 12;
    public static final String IS_APP = "app";
    public static final String ID_USUARIO = "id_usuario";
    public static final String ID_PROJECT = "id_proyecto";
    public static final String USER_DATA = "usuario";
    public static final String ALL_PROJECTS = "proyectos_usuario";
    public static final String TARGETS_PROJECT = "tarjetas_proyecto";
    public static final String IMAGES_PROJECTS = "imagenes_proyecto";
    public static final String DEFAULT_PROJECT_IMG_NAME = "home.jpg";
    public static final String IMG_BODY64 = "img_base64";
    public static final String IMAGES_BODY_BASE64 = "images_body_base64";
    public static final String IMAGE_NAMES_BODY_BASE64 = "image_names_body_base64";
    public static final String PROJECT = "project";
    public static final String PROJECT_NAME = "nameProject";
    public static final String PROJECT_IMG = "imageProject";
    public static final String PROJECT_IMG_BODY = "imageProjectBody";
    public static final String PROJECT_IMG_DESCRIPTION = "imageProjectDescriptions";
    @Deprecated
    public static final String PROJECTS_FILENAME = "projects.json";
    public static final String PROJECT_SELECTED = "new_selected";
    public static final String SORT_BY = "sortBy";
    public static final String TYPE_SORT_BY = "typeSortBy";
    public static final String json_project_flag_activo = "flag_activo";
    public static final String json_project_flag_finish = "flag_finish";
    public static final String json_project_id_project = "id_proyecto";
    public static final String json_project_name = "nombre";
    public static final String json_project_last_update = "fecha_actualizacion";
    public static final String json_project_create_data = "fecha_creacion";
    public static final String json_project_dir_files = "directorio_root";
    public static final String json_project_descript = "description";
    public static final String json_project_home_img = "home_image";
    public static final String json_project_targets = "project_targets";
    public static final String json_project_targets_id_tarjeta = "id_tarjeta";
    public static final String json_project_targets_label = "label";
    public static final String json_project_targets_value = "valor";
    public static final String json_project_images = "project_images";
    public static final String json_project_images_id_imagen = "id_imagen";
    public static final String json_project_images_url = "url";
    public static final String json_project_images_descript = "descripcion";
    public static final String json_project_images_upload = "fecha_subida";
    public static final String json_project_images_width = "width";
    public static final String json_project_images_height = "height";
    private static ArrayList<Project> projectList;

    public static void setDefaultPrefs() {
        if (Tools.getPrefs().getString(SORT_BY, "").equals("")) {
            Tools.putInPrefs().putString(SORT_BY, json_project_id_project).apply();
            Tools.putInPrefs().putBoolean(TYPE_SORT_BY, TYPE_SORT_BY_DESC).apply();
        }
    }

    public static String getSortBy() {
        return Tools.getPrefs().getString(SORT_BY, json_project_id_project);
    }

    public static void setSortBy(String sortBy) {
        Tools.putInPrefs().putString(SORT_BY, sortBy).apply();
    }

    public static boolean getTypeSortBy() {
        return Tools.getPrefs().getBoolean(TYPE_SORT_BY, TYPE_SORT_BY_DESC);
    }

    public static void setTypeSortBy(boolean typeSortBy) {
        Tools.putInPrefs().putBoolean(TYPE_SORT_BY, typeSortBy).apply();
    }

    public static JSONArray sortJSON(JSONArray jsonArray, final String key, final boolean asc) throws JSONException {
        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonValues.add(jsonArray.getJSONObject(i));
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = "", valB = "";
                try {
                    valA = String.valueOf(a.get(key));
                    valB = String.valueOf(b.get(key));
                } catch (JSONException e) {
                    //do something
                }
                //if you want to change the sort order, simply use the following:
                if (asc) {
                    return valA.compareToIgnoreCase(valB);
                } else {
                    return -valA.compareToIgnoreCase(valB);
                }
            }
        });

        for (int i = 0; i < jsonArray.length(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }
        return sortedJsonArray;
    }

    public static ArrayList<Project> getProjectList() {
        return projectList;
    }

    public static void setProjectList(ArrayList<Project> projectList) {
        ProjectManager.projectList = projectList;
    }

    public static void addProjectList(Project project) {
        ProjectManager.projectList.add(project);
    }

    public static void download(Activity activity, SwipeRefreshLayout swipeLayout) {
        new MainLoader(activity, HOSTNAME + "/php/project_manager/projects_user.php", swipeLayout).execute(new FormBody.Builder()
                .add(ProjectManager.IS_APP, "true")
                .add(ProjectManager.ID_USUARIO, Tools.getUserID())
                .build());
    }

    public static Project downloadById(Activity activity, String id) throws ExecutionException, InterruptedException {
        ProjectSelectedLoader p = new ProjectSelectedLoader(activity, HOSTNAME + "/php/project_manager/project_selected.php");
        p.execute(new FormBody.Builder()
                .add(ProjectManager.IS_APP, "true")
                .add(ProjectManager.ID_USUARIO, Tools.getUserID())
                .add(ProjectManager.ID_PROJECT, id)
                .build());
        return p.get();
    }

    public static void delete(Activity activity, Project project) throws JSONException {
        new DeleteProject(activity, HOSTNAME + "/php/project_manager/delete_project.php").execute(new FormBody.Builder()
                .add(ProjectManager.IS_APP, "true")
                .add(ProjectManager.ID_USUARIO, Tools.getUserID())
                .add(ProjectManager.ID_PROJECT, project.getId())
                .build());
    }

    public static void create(Activity activity, Project project, String projectHeaderImag, List<String> listStringFilesBase64, List<String> listStringFilesNames) throws JSONException {
        new CreateProject(activity, HOSTNAME + "/php/project_manager/create_project.php").execute(new FormBody.Builder()
                .add(ProjectManager.IS_APP, "true")
                .add(ProjectManager.ID_USUARIO, Tools.getUserID())
                .add(ProjectManager.PROJECT, project.toJSON().toString())
                .add(ProjectManager.IMG_BODY64, (projectHeaderImag != null) ? Tools.getImageBase64(projectHeaderImag) : "")
                .add(ProjectManager.IMAGES_BODY_BASE64, (listStringFilesBase64 != null) ? listStringFilesBase64.toString() : "")
                .add(ProjectManager.IMAGE_NAMES_BODY_BASE64, (listStringFilesNames != null) ? listStringFilesNames.toString() : "")
                .build());
    }
}
