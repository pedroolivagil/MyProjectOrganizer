package cat.olivadevelop.myprojectorganizer.tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import cat.olivadevelop.myprojectorganizer.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Oliva on 26/09/2016.
 */
public class UrlUploader extends AsyncTask<String, Void, JSONObject> implements AdapterView.OnItemClickListener {
    ProgressDialog progressDialog;
    public static Activity activity;
    private ArrayList<HashMap<String, String>> arLstProjectList = new ArrayList<HashMap<String, String>>();

    // claves fichero json
    private static final String ID_USER = "id_user";
    private static final String CATEGORY = "project";
    private static final String json_project_name = "name";
    private static final String json_project_last_update = "last_update";
    private static final String json_project_create_data = "create_data";
    private static final String json_project_dir_files = "dir_files";
    private static final String json_project_home_img = "home_img";
    private static final String json_project_images = "images";
    private static final String json_project_form = "form";

    private String getString(int id_string) {
        return activity.getBaseContext().getString(id_string);
    }

    @Override
    protected JSONObject doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            String resStr = response.body().string();
            JSONObject json = new JSONObject(resStr);
            return json;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        try {
            // despues de ejecutar el codigo: doInBackground(String... params)
            progressDialog.dismiss();

            // seleccionamos la etiqueta proyecto
            JSONArray category = jsonObject.getJSONArray(CATEGORY);

            /*
            "id_project" : 1,
			"name" : "La tortuga verde de ganchillo número 3",
			"create_data" : "20-09-2016",
			"last_update" : "23-09-2016",
			"dir_files" : "http://projects.codeduo.cat/c508260d3dd0d72608864428f71b4571/project1",
			"home_img" : "home.jpg",
			"images" : [
				"img1.jpg",
				"img2.jpg"
			],
			"form" : {
				"Tipo de pregunta 1" : "respuesta 1",
				"Tipo de pregunta 2" : "respuesta 2",
				"Tipo de pregunta 3" : "respuesta 3",
				"Tipo de pregunta 4" : "respuesta 4",
				"Tipo de pregunta 5" : "respuesta 5"
			}*/
            SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
            sdf.setTimeZone(TimeZone.getDefault());
            String currentDate = sdf.format(new Date());

            JSONArray jsnImages = new JSONArray();
            jsnImages.put(0, "url 1");
            jsnImages.put(1, "url 2");
            jsnImages.put(2, "url 3");

            JSONObject jsnForm = new JSONObject();
            jsnForm.put("Question 1", "answer 1");
            jsnForm.put("Question 2", "answer 2");
            jsnForm.put("Question 3", "answer 3");
            jsnForm.put("Question 4", "answer 4");

            JSONObject newjsonObject = new JSONObject();
            newjsonObject.put("id_project", category.length());
            newjsonObject.put("name", "");
            newjsonObject.put("create_data", currentDate);
            newjsonObject.put("last_update", currentDate);
            newjsonObject.put("dir_files", "http://projects.codeduo.cat/" + Tools.getPrefs().getString(Tools.PREFS_USER_ID, null) + "/project1");
            newjsonObject.put("home_img", "home.jpg");
            newjsonObject.put("images", jsnImages);
            newjsonObject.put("form", jsnForm);

            // añadimos un nuevo proyecto con un nuevo jsonobject
            category.put(category.length(), newjsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(getString(R.string.pgd_creating_project));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(activity, getString(R.string.item_clic_toast), LENGTH_LONG).show();
    }
}
