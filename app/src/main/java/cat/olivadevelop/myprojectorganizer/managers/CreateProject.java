package cat.olivadevelop.myprojectorganizer.managers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.tools.Tools;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.PROJECT;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_create_data;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_dir_files;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_flag_activo;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_form;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_home_img;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_id_project;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_images;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_last_update;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_name;
import static cat.olivadevelop.myprojectorganizer.tools.Tools.HOSTNAME;

/**
 * Created by Oliva on 26/09/2016.
 * Inicializa el proyecto, con el nombre y el JSON que contiene la información del proyecto
 * Al finalizar, envía el formbody a UpdateJSONFile
 * <p>
 * convertir el string de umagen en un fichero y usar getName() para insertarlo en el array de nombres
 */
public class CreateProject extends AsyncTask<Void, Void, RequestBody> {
    // claves fichero json
    private String clsPjtName;
    private String pjtName;
    private String ba1;
    private Activity activity;
    private ProgressDialog progressDialog;
    private HashMap<String, String> values = new HashMap<String, String>();
    private String projectHeaderImag;
    private List<String> listFileString;
    private List<String> listStringFilesBase64;
    private ArrayList<String> listStringFilesNames;
    private HashMap<String, String> mapDescriptions;


    public CreateProject(Activity activity, String pjtName, HashMap<String, String> values, String projectHeaderImag,
                         List<String> listFileString, HashMap<String, String> mapDescriptions) {
        this.pjtName = pjtName;
        this.clsPjtName = pjtName.toLowerCase().replaceAll("\\W\\s", "");
        this.activity = activity;
        this.values = values;
        this.ba1 = "";
        this.projectHeaderImag = projectHeaderImag;
        this.listFileString = listFileString;
        this.listStringFilesBase64 = new ArrayList<String>();
        this.listStringFilesNames = new ArrayList<String>();
        this.mapDescriptions = mapDescriptions;
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

            // insertamos el nuevo proyecto en el JSON
            JSONArray project = json.getJSONArray(PROJECT);

            JSONArray jsnArrImages = new JSONArray();
            if (listFileString != null) {
                JSONObject jsnImages;
                for (String str : listFileString) {
                    jsnImages = new JSONObject();
                    File currentFile = new File(str);
                    Bitmap b = BitmapFactory.decodeFile(str.trim());
                    jsnImages.put(ProjectManager.json_project_images_url, currentFile.getName().trim());
                    if (this.mapDescriptions != null && this.mapDescriptions.size() > 0) {
                        if (mapDescriptions.get(currentFile.getName().trim()) != null) {
                            jsnImages.put(ProjectManager.json_project_images_descript, mapDescriptions.get(currentFile.getName().trim()));
                        } else {
                            jsnImages.put(ProjectManager.json_project_images_descript, "");
                        }
                    } else {
                        jsnImages.put(ProjectManager.json_project_images_descript, "");
                    }
                    jsnImages.put(ProjectManager.json_project_images_upload, Tools.getCurrentDate());
                    jsnImages.put(ProjectManager.json_project_images_width, b.getWidth());
                    jsnImages.put(ProjectManager.json_project_images_height, b.getHeight());
                    jsnArrImages.put(jsnImages);
                    listStringFilesBase64.add(Tools.getImageBase64(str.replace("\\", "").trim()));
                    listStringFilesNames.add(currentFile.getName().trim());
                }
            }

            JSONObject jsnForm = new JSONObject();
            for (String key : values.keySet()) {
                jsnForm.put(key, values.get(key));
            }

            JSONObject newjsonObject = new JSONObject();
            newjsonObject.put(json_project_id_project, project.length());
            newjsonObject.put(json_project_name, pjtName);
            newjsonObject.put(json_project_flag_activo, true);
            newjsonObject.put(json_project_create_data, Tools.getCurrentDate());
            newjsonObject.put(json_project_last_update, Tools.getCurrentDate());
            newjsonObject.put(json_project_dir_files, "/project" + (project.length()));
            newjsonObject.put(json_project_home_img, "home.jpg");
            newjsonObject.put(json_project_images, jsnArrImages);
            newjsonObject.put(json_project_form, jsnForm);

            // añadimos un nuevo proyecto con un nuevo jsonobject
            project.put(project.length(), newjsonObject);

            json.put(json_project_last_update, Tools.getCurrentDate());

            /**
             * Terminar de dubir las imágenes a la carpeta
             */
            // actualizamos el json del server
            FormBody.Builder form = new FormBody.Builder()
                    .add("id_client", "" + Tools.getUserID())
                    .add("jsonObject", json.toString())
                    .add("projectName", "project" + ((project.length()) - 1))
                    .add("img_base64", (projectHeaderImag != null) ? Tools.getImageBase64(projectHeaderImag) : "")
                    .add("images_body_base64", (listStringFilesBase64 != null) ? listStringFilesBase64.toString() : "")
                    .add("image_names_body_base64", (listStringFilesNames != null) ? listStringFilesNames.toString() : "");

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
            ProjectManager.addProject(activity, formBody);
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