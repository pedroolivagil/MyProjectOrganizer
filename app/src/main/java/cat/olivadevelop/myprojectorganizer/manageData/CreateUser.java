package cat.olivadevelop.myprojectorganizer.manageData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.screens.MainScreen;
import cat.olivadevelop.myprojectorganizer.tools.Tools;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Oliva on 03/01/2017.
 */
public class CreateUser extends AsyncTask<RequestBody, Void, Boolean> {
    private Activity activity;
    private String url;
    private ProgressDialog progressDialog;

    public CreateUser(Activity activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    private String getString(int id_string) {
        return activity.getBaseContext().getString(id_string);
    }

    @Override
    protected Boolean doInBackground(RequestBody... formbody) {
        String retorno = "false";
        try {
            URL url = new URL(this.url);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formbody[0])
                    .build();
            Response response = client.newCall(request).execute();
            retorno = response.body().string();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Tools.parseBoolean(retorno);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(getString(R.string.pgd_creating_user));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        progressDialog.dismiss();
        if (aBoolean) {
            Intent intent = new Intent(activity, MainScreen.class);
            activity.startActivity(intent);
        } else {
            Tools.showAlertError(activity);
        }
    }
}
