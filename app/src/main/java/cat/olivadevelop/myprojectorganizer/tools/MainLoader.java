package cat.olivadevelop.myprojectorganizer.tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

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
public class MainLoader extends AsyncTask<String, Void, JSONObject> implements AdapterView.OnItemClickListener {
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
    ProgressDialog progressDialog;
    private Activity activity;
    private ArrayList<HashMap<String, String>> arLstProjectList = new ArrayList<HashMap<String, String>>();

    public MainLoader(Activity activity) {
        this.activity = activity;
    }

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
        HashMap<String, String> hashmap;
        if (jsonObject != null) {
            try {
                // despues de ejecutar el codigo: doInBackground(String... params)
                JSONArray category;
                JSONObject jsonObjectLine;

                category = jsonObject.getJSONArray(CATEGORY);
                String[] img_url = new String[category.length()];
                String[] title_arr = new String[category.length()];
                String[] date_arr = new String[category.length()];

                if (category.length() > 0) {
                    for (int z = 0; z < category.length(); z++) {
                        jsonObjectLine = category.getJSONObject(z);

                        title_arr[z] = jsonObjectLine.getString(json_project_name);
                        date_arr[z] = jsonObjectLine.getString(json_project_last_update);
                        img_url[z] = Tools.HOSTNAME + "/clients/" + Tools.getUserID() +
                                jsonObjectLine.getString(json_project_dir_files) + "/" +
                                jsonObjectLine.getString(json_project_home_img);

                    }
                    Log.i("LENGTH img_url", img_url.length + "");
                    Tools.setUrlImgArray(img_url);
                    Tools.setTitlePrjctArray(title_arr);
                    Tools.setDatePrjctArray(date_arr);
                } else {
                    Tools.setUrlImgArray(new String[]{"http://projects.codeduo.cat/logo.png"});
                    Tools.setTitlePrjctArray(new String[]{getString(R.string.noProjectTitle)});
                    Tools.setDatePrjctArray(new String[]{"Empty"});
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Tools.setUrlImgArray(new String[]{"http://projects.codeduo.cat/logo.png"});
            Tools.setTitlePrjctArray(new String[]{getString(R.string.unableToConnect)});
            Tools.setDatePrjctArray(new String[]{"Empty"});
        }
        progressDialog.dismiss();
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
