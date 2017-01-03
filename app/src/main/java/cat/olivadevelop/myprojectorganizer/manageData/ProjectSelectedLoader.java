package cat.olivadevelop.myprojectorganizer.manageData;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import cat.olivadevelop.myprojectorganizer.entities.Project;
import cat.olivadevelop.myprojectorganizer.tools.Tools;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.ALL_PROJECTS;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.IMAGES_PROJECTS;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.TARGETS_PROJECT;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_create_data;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_descript;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_dir_files;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_flag_activo;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_flag_finish;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_home_img;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_id_project;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_last_update;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_name;

/**
 * Created by Oliva on 26/09/2016.
 * <p>
 * Descarga los proyectos del usuario
 */
public class ProjectSelectedLoader extends AsyncTask<RequestBody, Void, Project> {

    private String url;
    private Activity activity;

    public ProjectSelectedLoader(Activity activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    @Override
    protected Project doInBackground(RequestBody... formbody) {
        Project p;
        try {
            URL url = new URL(this.url);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formbody[0])
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject json = new JSONObject(response.body().string());

            JSONArray jsonObject = json.getJSONArray(ALL_PROJECTS);
            JSONObject jsonObjectLine = jsonObject.getJSONObject(0);

            p = new Project();
            p.setFlagActivo(Tools.parseBoolean(jsonObjectLine.getString(json_project_flag_activo)));
            p.setFlagFinished(Tools.parseBoolean(jsonObjectLine.getString(json_project_flag_finish)));
            p.setIdProject(jsonObjectLine.getString(json_project_id_project));
            p.setName(jsonObjectLine.getString(json_project_name));
            p.setCreateDate(jsonObjectLine.getString(json_project_create_data));
            p.setLastUpdate(jsonObjectLine.getString(json_project_last_update));
            p.setUserRoot(jsonObjectLine.getString(json_project_dir_files));
            p.setDescription(jsonObjectLine.getString(json_project_descript));
            p.setHomeImage(jsonObjectLine.getString(json_project_home_img));
            p.setTarjetas(json.getJSONArray(TARGETS_PROJECT));
            p.setProjectImages(json.getJSONArray(IMAGES_PROJECTS));
            p.setEmpty(false);
            return p;
        } catch (IOException | JSONException e) {
            Log.e(Tools.tagLogger(activity), e.getMessage());
            e.printStackTrace();
        }
        p = new Project();
        p.setEmpty(true);
        return p;
    }
}
