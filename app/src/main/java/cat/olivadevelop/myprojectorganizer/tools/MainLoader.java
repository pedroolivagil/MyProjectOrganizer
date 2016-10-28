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

import static cat.olivadevelop.myprojectorganizer.tools.Project.CATEGORY;
import static cat.olivadevelop.myprojectorganizer.tools.Project.json_project_descript;
import static cat.olivadevelop.myprojectorganizer.tools.Project.json_project_dir_files;
import static cat.olivadevelop.myprojectorganizer.tools.Project.json_project_form;
import static cat.olivadevelop.myprojectorganizer.tools.Project.json_project_home_img;
import static cat.olivadevelop.myprojectorganizer.tools.Project.json_project_last_update;
import static cat.olivadevelop.myprojectorganizer.tools.Project.json_project_name;
import static cat.olivadevelop.myprojectorganizer.tools.Tools.sortJSON;

/**
 * Created by Oliva on 26/09/2016.
 */
public class MainLoader extends AsyncTask<String, Void, JSONObject> implements AdapterView.OnItemClickListener {
    // claves fichero json
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

                category = sortJSON(jsonObject.getJSONArray(CATEGORY), Project.getSortBy(), Project.getTypeSortBy());
                String[] img_url = new String[category.length()];
                String[] title_arr = new String[category.length()];
                String[] descrip_arr = new String[category.length()];
                String[] date_arr = new String[category.length()];

                if (category.length() > 0) {
                    for (int z = 0; z < category.length(); z++) {
                        jsonObjectLine = category.getJSONObject(z);

                        title_arr[z] = jsonObjectLine.getString(json_project_name);
                        date_arr[z] = jsonObjectLine.getString(json_project_last_update);
                        descrip_arr[z] = jsonObjectLine.getJSONObject(json_project_form).getString(json_project_descript);
                        Log.i("PRJCT DESCRIPT JSON", "" + descrip_arr[z]);
                        img_url[z] = Tools.HOSTNAME + "/clients/" + Tools.getUserID() +
                                jsonObjectLine.getString(json_project_dir_files) + "/" +
                                jsonObjectLine.getString(json_project_home_img);
                    }
                    Log.i("LENGTH img_url", img_url.length + "");
                    Tools.setUrlImgArray(img_url);
                    Tools.setTitlePrjctArray(title_arr);
                    Tools.setDescriptPrjctArray(descrip_arr);
                    Tools.setDatePrjctArray(date_arr);
                } else {
                    Tools.setUrlImgArray(new String[]{"http://projects.codeduo.cat/logo.png"});
                    Tools.setTitlePrjctArray(new String[]{getString(R.string.noProjectTitle)});
                    Tools.setDescriptPrjctArray(new String[]{getString(R.string.descript_pjct404)});
                    Tools.setDatePrjctArray(new String[]{getString(R.string.empty)});
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Tools.setUrlImgArray(new String[]{"http://projects.codeduo.cat/logo.png"});
            Tools.setTitlePrjctArray(new String[]{getString(R.string.unableToConnect)});
            Tools.setDescriptPrjctArray(new String[]{getString(R.string.descript_pjct404)});
            Tools.setDatePrjctArray(new String[]{getString(R.string.empty)});
        }
        //progressDialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(getString(R.string.pgd_loading));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        //progressDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(activity, getString(R.string.item_clic_toast), LENGTH_LONG).show();
    }
}
