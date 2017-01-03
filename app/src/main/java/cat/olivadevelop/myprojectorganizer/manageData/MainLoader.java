package cat.olivadevelop.myprojectorganizer.manageData;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import cat.olivadevelop.myprojectorganizer.entities.Project;
import cat.olivadevelop.myprojectorganizer.managers.ProjectManager;
import cat.olivadevelop.myprojectorganizer.tools.Tools;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_create_data;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_descript;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_dir_files;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_flag_activo;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_flag_finish;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_home_img;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_id_project;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_last_update;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_name;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.sortJSON;

/**
 * Created by Oliva on 26/09/2016.
 * <p>
 * Descarga los proyectos del usuario
 */
public class MainLoader extends AsyncTask<RequestBody, Void, String> {

    private SwipeRefreshLayout swipeLayout;
    private String url;
    private Activity activity;

    public MainLoader(Activity activity, String url, SwipeRefreshLayout swipeLayout) {
        this.activity = activity;
        this.url = url;
        this.swipeLayout = swipeLayout;
    }

    @Override
    protected String doInBackground(RequestBody... formbody) {
        try {
            URL url = new URL(this.url);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formbody[0])
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e(Tools.tagLogger(activity), e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String json) {
        super.onPostExecute(json);
        ProjectManager.setProjectList(new ArrayList<Project>());
        Project p;
        JSONObject jsonObject;
        JSONObject jsonObjectLine;
        JSONArray jProjects;
        if (json != null && !json.equals("")) {
            try {
                jsonObject = new JSONObject(json);
                jProjects = sortJSON(jsonObject.getJSONArray(ProjectManager.ALL_PROJECTS), ProjectManager.getSortBy(), ProjectManager.getTypeSortBy());
                //jProjects = jsonObject.getJSONArray(ProjectManager.ALL_PROJECTS);
                if (jProjects.length() > 0) { // si t0do es correcto
                    for (int z = 0; z < jProjects.length(); z++) {
                        jsonObjectLine = jProjects.getJSONObject(z);
                        if (Tools.parseBoolean(jsonObjectLine.getString(json_project_flag_activo))) {
                            p = new Project();
                            p.setEmpty(false);
                            p.setFlagActivo(Tools.parseBoolean(jsonObjectLine.getString(json_project_flag_activo)));
                            p.setFlagFinished(Tools.parseBoolean(jsonObjectLine.getString(json_project_flag_finish)));
                            p.setIdProject(jsonObjectLine.getString(json_project_id_project));
                            p.setName(jsonObjectLine.getString(json_project_name));
                            p.setCreateDate(jsonObjectLine.getString(json_project_create_data));
                            p.setLastUpdate(jsonObjectLine.getString(json_project_last_update));
                            p.setUserRoot(jsonObjectLine.getString(json_project_dir_files));
                            p.setDescription(jsonObjectLine.getString(json_project_descript));
                            p.setHomeImage(jsonObjectLine.getString(json_project_home_img));
                            ProjectManager.addProjectList(p);
                        }
                    }
                    Tools.setProjectListResult(true);
                } else { // si no hay proyectos
                    Log.e(Tools.tagLogger(activity), "3 - Error leyendo los proyectos, no hay proyectos");
                }
            } catch (JSONException e) { // si ocurrió algun error con el json
                Log.e(Tools.tagLogger(activity), e.getMessage());
            }
        } else {
            Log.e(Tools.tagLogger(activity), "3 - Error leyendo los proyectos, error de lectura del JSON");
        }
    }
}
