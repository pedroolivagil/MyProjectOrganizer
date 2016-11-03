package cat.olivadevelop.myprojectorganizer.tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.managers.ProjectManager;
import cat.olivadevelop.myprojectorganizer.screens.MainActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cat.olivadevelop.myprojectorganizer.tools.Tools.HOSTNAME;

/**
 * Created by Oliva on 03/10/2016.
 * Sube el proyecto creado a la web. El proyecto es pasado por parámetro formbody
 */

public class UploadJSON extends AsyncTask<RequestBody, Void, Boolean> {

    private Activity activity;
    private ProgressDialog progressDialog;

    public UploadJSON(Activity activity) {
        this.activity = activity;
    }

    private String getString(int id_string) {
        return activity.getBaseContext().getString(id_string);
    }

    /**
     * @param params contiene el formbody creado en CreateProject. Contiene el JSON y otros parámetros
     */
    @Override
    protected Boolean doInBackground(RequestBody... params) {
        URL url;
        try {
            RequestBody formBody = params[0];
            url = new URL(HOSTNAME + "/php/create_project.php");
            Log.i("URL", url.toString());
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = client.newCall(request).execute();
            String resStr = response.body().string();
            Log.i("RESULT FINAL", "" + response.message());
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            progressDialog.dismiss();
            Tools.newSnackBarWithIcon(activity.findViewById(R.id.scroll_project_finish),
                    activity, R.string.cannot_be_connect,
                    R.drawable.ic_signal_cellular_connected_no_internet_4_bar_white_24dp)
                    .show();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        ProjectManager.cleanTempPrefs();
        progressDialog.dismiss();
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(getString(R.string.pgd_uploading_project));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}
