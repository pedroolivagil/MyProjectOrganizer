package cat.olivadevelop.myprojectorganizer.tools;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import cat.olivadevelop.myprojectorganizer.managers.Project;
import cat.olivadevelop.myprojectorganizer.managers.ProjectManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.CATEGORY;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.FLAG_ACTIVO;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_create_data;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_dir_files;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_form;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_home_img;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_id_project;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_images;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_last_update;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_name;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.sortJSON;

/**
 * Created by Oliva on 26/09/2016.
 * <p>
 * Descarga los proyectos del usuario
 */
public class MainLoader extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            String resStr = response.body().string();

            Project p;
            ArrayList<Project> projectList = new ArrayList<>();
            ProjectManager.setProjectList(projectList);

            if (!resStr.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    JSONArray jProjects;
                    JSONObject jsonObjectLine;
                    jProjects = sortJSON(jsonObject.getJSONArray(CATEGORY), ProjectManager.getSortBy(), ProjectManager.getTypeSortBy());
                    if (jProjects.length() > 0) { // si t0do es correcto
                        for (int z = 0; z < jProjects.length(); z++) {
                            jsonObjectLine = jProjects.getJSONObject(z);
                            if(jsonObjectLine.getBoolean(FLAG_ACTIVO)) {
                                p = new Project();
                                p.setEmpty(false);
                                p.setId(jsonObjectLine.getInt(json_project_id_project));
                                p.setName(jsonObjectLine.getString(json_project_name));
                                p.setCreateDate(jsonObjectLine.getString(json_project_create_data));
                                p.setLastUpdate(jsonObjectLine.getString(json_project_last_update));
                                p.setHomeDir(jsonObjectLine.getString(json_project_dir_files));
                                p.setHomeImage(jsonObjectLine.getString(json_project_home_img));
                                p.setUrlImages(jsonObjectLine.getJSONArray(json_project_images));
                                p.setForm(jsonObjectLine.getJSONObject(json_project_form));
                                projectList.add(p);
                            }
                        }
                    } else { // si no hay proyectos
                        p = new Project();
                        p.setEmpty(true);
                        projectList.add(p);
                        Log.e("ADD Project", "false");
                    }
                } catch (JSONException e) { // si ocurrió algun error con el json
                    p = new Project();
                    p.setEmpty(true);
                    projectList.add(p);
                    Log.e("ADD Project", "false");
                }
            } else { // si json es null
                p = new Project();
                p.setEmpty(true);
                projectList.add(p);
                Log.e("ADD Project", "false");
            }
            ProjectManager.setProjectList(projectList);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Log.e("LoaderCorrect", "" + aBoolean);
    }
}
