package cat.olivadevelop.myprojectorganizer.tools;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Oliva on 22/10/2016.
 */

public abstract class ProjectManager {
    public static final boolean TYPE_SORT_BY_ASC = true;
    public static final boolean TYPE_SORT_BY_DESC = false;
    public static final String CATEGORY = "project";
    public static final String PROJECT_NAME = "nameProject";
    public static final String PROJECT_IMG = "imageProject";
    public static final String PROJECTS_FILENAME = "projects.json";
    public static final String NEW_SELECTED = "new_selected";
    public static final String SORT_BY = "sortBy";
    public static final String TYPE_SORT_BY = "typeSortBy";
    public static final String FINISH_PJT = "finished";
    public static final String json_project_id_project = "id_project";
    public static final String json_project_name = "name";
    public static final String json_project_last_update = "last_update";
    public static final String json_project_create_data = "create_data";
    public static final String json_project_dir_files = "dir_files";
    public static final String json_project_home_img = "home_img";
    public static final String json_project_images = "images";
    public static final String json_project_form = "form";
    public static final String json_project_descript = "description";

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

    public static JSONArray getProjects() throws JSONException {
        return new JSONArray(Tools.getPrefs().getString(ProjectManager.NEW_SELECTED, "[]"));
    }

    public static void setProjects(JSONArray projects) {
        Tools.putInPrefs().putString(ProjectManager.NEW_SELECTED, projects.toString()).apply();
    }

    public static void cleanTempPrefs(){
        Tools.putInPrefs().putString(ProjectManager.PROJECT_NAME, null).apply();
        Tools.putInPrefs().putString(ProjectManager.PROJECT_IMG, null).apply();
    }
}
