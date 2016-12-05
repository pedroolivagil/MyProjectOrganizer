package cat.olivadevelop.myprojectorganizer.tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.managers.Project;
import cat.olivadevelop.myprojectorganizer.managers.ProjectManager;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.PROJECT;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_flag_activo;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_id_project;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_last_update;
import static cat.olivadevelop.myprojectorganizer.tools.Tools.HOSTNAME;

/**
 * Created by Oliva on 26/09/2016.
 * Inicializa el proyecto, con el nombre y el JSON que contiene la información del proyecto
 * Al finalizar, envía el formbody a UpdateJSONFile
 * <p>
 * convertir el string de umagen en un fichero y usar getName() para insertarlo en el array de nombres
 */
public class DeleteProject extends AsyncTask<Void, Void, RequestBody> {
    private Project project;
    private Activity activity;
    private ProgressDialog progressDialog;


    public DeleteProject(Activity activity, Project project) {
        this.activity = activity;
        this.project = project;
    }

    private String getString(int id_string) {
        return activity.getBaseContext().getString(id_string);
    }

    @Override
    protected RequestBody doInBackground(Void... urls) {
        try {
            URL url = new URL(HOSTNAME + "/clients/" + Tools.getUserID() + "/" + ProjectManager.PROJECTS_FILENAME);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            String resStr = response.body().string();
            response.close();
            JSONObject json = new JSONObject(resStr);

            JSONArray projects = json.getJSONArray(PROJECT);

            for (int x = 0; x < projects.length(); x++) {
                JSONObject currentProject = projects.getJSONObject(x);
                if (currentProject.getInt(json_project_id_project) == this.project.getId()) {
                    currentProject.put(json_project_last_update, Tools.getCurrentDate());
                    currentProject.put(json_project_flag_activo, false);
                }
            }
            json.put(json_project_last_update, Tools.getCurrentDate());

            // actualizamos el json del server
            FormBody.Builder form = new FormBody.Builder()
                    .add("id_client", "" + Tools.getUserID())
                    .add("jsonObject", json.toString());

            return form.build();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(RequestBody formBody) {
        super.onPostExecute(formBody);
        progressDialog.dismiss();
        if (formBody != null) {
            ProjectManager.delete(activity, formBody);
        } else {
            Tools.showAlertError(activity);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(getString(R.string.pgd_creating_project));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}