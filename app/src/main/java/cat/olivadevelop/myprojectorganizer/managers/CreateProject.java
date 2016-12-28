package cat.olivadevelop.myprojectorganizer.managers;

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
 * Created by Oliva on 26/09/2016.
 * Inicializa el proyecto, con el nombre y el JSON que contiene la información del proyecto
 * Al finalizar, envía el formbody a UpdateJSONFile
 * <p>
 * convertir el string de umagen en un fichero y usar getName() para insertarlo en el array de nombres
 */
public class CreateProject extends AsyncTask<RequestBody, Void, Boolean> {
    // claves fichero json
    private String url;
    private Activity activity;
    private ProgressDialog progressDialog;

    public CreateProject(Activity activity, String url) {
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