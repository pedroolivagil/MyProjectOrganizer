package cat.olivadevelop.myprojectorganizer.tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import cat.olivadevelop.myprojectorganizer.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Oliva on 26/09/2016.
 */
public class UrlDownloader extends AsyncTask<String, Void, JSONObject> implements AdapterView.OnItemClickListener {
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
            HashMap<String, String> hashmap;
            // despues de ejecutar el codigo: doInBackground(String... params)
            progressDialog.dismiss();
            // cojer el array que queremos desde el archivo JSON

            JSONArray category;
            JSONObject jsonObjectLine;

            category = jsonObject.getJSONArray(CATEGORY);
            if (category.length() > 0) {
                Log.i("CategoryLength +0", "" + category.length());
                for (int z = 0; z < category.length(); z++) {
                    jsonObjectLine = category.getJSONObject(z);

                    // añadir la clave-valor a un objeto HashMap
                    hashmap = new HashMap<String, String>();
                    hashmap.put(json_project_name, jsonObjectLine.getString(json_project_name));
                    hashmap.put(json_project_last_update, jsonObjectLine.getString(json_project_last_update));
                    hashmap.put(json_project_home_img, jsonObjectLine.getString(json_project_dir_files) + "/" + jsonObjectLine.getString(json_project_home_img));

                    // añadir en la osList los valores
                    arLstProjectList.add(hashmap);

                    ListView projectList = (ListView) activity.findViewById(R.id.projectList);
                    ListAdapter adapter = new SimpleAdapter(
                            activity,              // Context
                            arLstProjectList,                         // Lista de claves-valor
                            R.layout.project_list,                 // view a la que queremos enlazar
                            new String[]{json_project_name, json_project_last_update/*, json_project_home_img*/},    // array de los campos a insertar
                            new int[]{R.id.projectName, R.id.projectLastUpdate/*, R.id.projectHomeImg*/}           // valores de los campos a insertar
                    );
                    projectList.setAdapter(adapter);
                    projectList.setOnItemClickListener(this);
                }
            } else {
                Log.i("CategoryLength 0", "" + category.length());
                // añadir la clave-valor a un objeto HashMap
                hashmap = new HashMap<String, String>();
                hashmap.put(json_project_name, getString(R.string.noProjectTitle));

                // añadir en la osList los valores
                arLstProjectList.add(hashmap);

                ListView projectList = (ListView) activity.findViewById(R.id.projectList);
                ListAdapter adapter = new SimpleAdapter(
                        activity,              // Context
                        arLstProjectList,                         // Lista de claves-valor
                        R.layout.project_empty,                 // view a la que queremos enlazar
                        new String[]{json_project_name},    // array de los campos a insertar
                        new int[]{R.id.projectName}           // valores de los campos a insertar
                );
                projectList.setAdapter(adapter);
                projectList.setOnItemClickListener(this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(getString(R.string.pgd_loading));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(activity, getString(R.string.item_clic_toast), LENGTH_LONG).show();
    }
}
