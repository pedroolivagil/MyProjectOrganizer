package cat.olivadevelop.myprojectorganizer.tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.screens.MainScreen;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Oliva on 03/10/2016.
 * Sube el proyecto creado a la web. El proyecto es pasado por parámetro formbody
 */

public class UpdateJSONFile extends AsyncTask<RequestBody, Void, Boolean> {

    private String url;
    private Activity activity;
    private ProgressDialog progressDialog;

    public UpdateJSONFile(Activity activity,String url) {
        this.activity = activity;
        this.url = url;
    }

    private String getString(int id_string) {
        return activity.getBaseContext().getString(id_string);
    }

    @Override
    protected Boolean doInBackground(RequestBody... params) {
        URL url;
        boolean retorno = false;
        try {
            RequestBody formBody = params[0];
            url = new URL(this.url);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = client.newCall(request).execute();
            String resStr = response.body().string();
            if (resStr.length() > 0) {
                Log.e(Tools.tagLogger(activity), resStr);
                retorno = true;
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            Tools.newSnackBarWithIcon(activity.findViewById(R.id.scroll_project_finish),
                    activity, R.string.cannot_be_connect,
                    R.drawable.ic_signal_cellular_connected_no_internet_4_bar_white_24dp)
                    .show();
        }
        return retorno;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        progressDialog.dismiss();
        Intent intent = new Intent(activity, MainScreen.class);
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
