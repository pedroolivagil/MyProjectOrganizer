package cat.olivadevelop.myprojectorganizer.tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import cat.olivadevelop.myprojectorganizer.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cat.olivadevelop.myprojectorganizer.tools.Tools.HOSTNAME;

/**
 * Created by Oliva on 26/09/2016.
 */
public class CreateProject extends AsyncTask<Void, Void, RequestBody> {
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
    public static final String FINISH_PJT = "finished";
    private String clsPjtName;
    private String pjtName;
    private String ba1;
    private Activity activity;
    private ProgressDialog progressDialog;
    private HashMap<String, String> values = new HashMap<String, String>();

    public CreateProject(Activity activity, String pjtName, HashMap<String, String> values) {
        this.pjtName = pjtName;
        this.clsPjtName = pjtName.toLowerCase().replaceAll("\\W\\s", "");
        this.activity = activity;
        this.values = values;
        this.ba1 = "";
    }

    private String getString(int id_string) {
        return activity.getBaseContext().getString(id_string);
    }

    @Override
    protected RequestBody doInBackground(Void... urls) {
        try {
            URL url = new URL(HOSTNAME + "/clients/" + Tools.getUserID() + "/" + Tools.PROJECTS_FILENAME);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            String resStr = response.body().string();
            JSONObject json = new JSONObject(resStr);
            // close response
            response.close();

            Log.i("RESULT", "" + resStr);

            // insertamos el nuevo proyecto en el JSON
            JSONArray category = json.getJSONArray(CATEGORY);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setTimeZone(TimeZone.getDefault());
            String currentDate = sdf.format(new Date());

            JSONArray jsnImages = new JSONArray();
            jsnImages.put("url 1");
            jsnImages.put("url 2");
            jsnImages.put("url 3");

            JSONObject jsnForm = new JSONObject();
            for (String key : values.keySet()) {
                jsnForm.put(key, values.get(key));
            }

            JSONObject newjsonObject = new JSONObject();
            newjsonObject.put("id_project", category.length());
            newjsonObject.put("name", pjtName);
            newjsonObject.put("create_data", currentDate);
            newjsonObject.put("last_update", currentDate);
            newjsonObject.put("dir_files", "/project" + (category.length()));
            newjsonObject.put("home_img", "home.jpg");
            newjsonObject.put("images", jsnImages);
            newjsonObject.put("form", jsnForm);

            // añadimos un nuevo proyecto con un nuevo jsonobject
            category.put(category.length(), newjsonObject);

            Log.i("JSON", json.toString());
            Log.i("JSON", URLEncoder.encode(json.toString(), "UTF-8"));
            Log.i("IDCLIENT", Tools.getUserID());

            // actualizamos el json del server
            RequestBody formBody = new FormBody.Builder()
                    .add("id_client", "" + Tools.getUserID())
                    .add("jsonObject", json.toString())
                    .add("projectName", "project" + ((category.length()) - 1))
                    .build();
            return formBody;

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
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
}
