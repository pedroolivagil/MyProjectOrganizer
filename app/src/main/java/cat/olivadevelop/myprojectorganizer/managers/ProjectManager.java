package cat.olivadevelop.myprojectorganizer.managers;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cat.olivadevelop.myprojectorganizer.tools.Tools;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static cat.olivadevelop.myprojectorganizer.tools.Tools.HOSTNAME;

/**
 * Created by Oliva on 22/10/2016.
 */

public abstract class ProjectManager {
    public static final boolean TYPE_SORT_BY_ASC = true;
    public static final boolean TYPE_SORT_BY_DESC = false;
    public static final int MAX_IMAGES_PROJECT = 12;
    public static final String PROJECT = "project";
    public static final String PROJECT_NAME = "nameProject";
    public static final String PROJECT_IMG = "imageProject";
    public static final String PROJECT_IMG_BODY = "imageProjectBody";
    public static final String PROJECT_IMG_DESCRIPTION = "imageProjectDescriptions";
    public static final String PROJECTS_FILENAME = "projects.json";
    public static final String NEW_SELECTED = "new_selected";
    public static final String SORT_BY = "sortBy";
    public static final String TYPE_SORT_BY = "typeSortBy";
    public static final String FINISH_PJT = "finished";
    public static final String json_project_flag_activo = "flag_activo";
    public static final String json_project_id_project = "id_project";
    public static final String json_project_name = "name";
    public static final String json_project_last_update = "last_update";
    public static final String json_project_create_data = "create_data";
    public static final String json_project_dir_files = "dir_files";
    public static final String json_project_home_img = "home_img";
    public static final String json_project_images = "images";
    public static final String json_project_images_url = "url";
    public static final String json_project_images_descript = "description";
    public static final String json_project_images_upload = "upload";
    public static final String json_project_images_width = "width";
    public static final String json_project_images_height = "height";
    public static final String json_project_form = "form";
    public static final String json_project_descript = "description";
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

    public static void download(Activity activity) {
        FormBody.Builder form = new FormBody.Builder().add("id_client", Tools.getUserID());
        new MainLoader(activity, HOSTNAME + "/php/read_project.php").execute(form.build());
    }

    public static void update(Activity activity, RequestBody formBody) {
        new UpdateJSONFile(activity, HOSTNAME + "/php/create_project.php").execute(formBody);
    }

    public static void delete(Activity activity, RequestBody formBody) {
        new UpdateJSONFile(activity, HOSTNAME + "/php/delete_project.php").execute(formBody);
    }
}
