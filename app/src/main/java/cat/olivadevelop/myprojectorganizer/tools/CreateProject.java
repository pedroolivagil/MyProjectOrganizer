package cat.olivadevelop.myprojectorganizer.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.managers.ProjectManager;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.CATEGORY;
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
 * Al finalizar, envía el formbody a UploadJSON
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


    public CreateProject(Activity activity, String pjtName, HashMap<String, String> values, String projectHeaderImag, List<String> listFileString) {
        this.pjtName = pjtName;
        this.clsPjtName = pjtName.toLowerCase().replaceAll("\\W\\s", "");
        this.activity = activity;
        this.values = values;
        this.ba1 = "";
        this.projectHeaderImag = projectHeaderImag;
        this.listFileString = listFileString;
        this.listStringFilesBase64 = new ArrayList<String>();
        this.listStringFilesNames = new ArrayList<String>();
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
            // close response
            response.close();

            JSONObject json = new JSONObject(resStr);
            //Log.i("RESULT", "" + resStr);

            // insertamos el nuevo proyecto en el JSON
            JSONArray category = json.getJSONArray(CATEGORY);

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setTimeZone(TimeZone.getDefault());
            String currentDate = sdf.format(new Date());

            /* Hay que implementar las acciones de captura y/o recogida de imágenes dentro del
             * proyecto y subirlas igual que la imagen de cabecera
             */
            JSONArray jsnArrImages = new JSONArray();
            if (listFileString != null) {
                JSONObject jsnImages;
                for (String str : listFileString) {
                    jsnImages = new JSONObject();
                    File currentFile = new File(str);
                    jsnImages.put(ProjectManager.json_project_images_url, currentFile.getName().trim());
                    jsnImages.put(ProjectManager.json_project_images_descript, "");
                    jsnImages.put(ProjectManager.json_project_images_width, "");
                    jsnImages.put(ProjectManager.json_project_images_height, "");
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
            newjsonObject.put(json_project_id_project, category.length());
            newjsonObject.put(json_project_name, pjtName);
            newjsonObject.put(json_project_flag_activo, true);
            newjsonObject.put(json_project_create_data, currentDate);
            newjsonObject.put(json_project_last_update, currentDate);
            newjsonObject.put(json_project_dir_files, "/project" + (category.length()));
            newjsonObject.put(json_project_home_img, "home.jpg");
            newjsonObject.put(json_project_images, jsnArrImages);
            newjsonObject.put(json_project_form, jsnForm);

            // añadimos un nuevo proyecto con un nuevo jsonobject
            category.put(category.length(), newjsonObject);

            /**
             * Terminar de dubir las imágenes a la carpeta
             */
            // actualizamos el json del server
            FormBody.Builder form = new FormBody.Builder()
                    .add("id_client", "" + Tools.getUserID())
                    .add("jsonObject", json.toString())
                    .add("projectName", "project" + ((category.length()) - 1))
                    .add("img_base64", Tools.getImageBase64(projectHeaderImag))
                    .add("images_body_base64", listStringFilesBase64.toString())
                    .add("image_names_body_base64", listStringFilesNames.toString());

            for (String str : listStringFilesBase64) {
                //Log.e("images_body_base64", str);
            }

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
        new UploadJSON(activity).execute(formBody);
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