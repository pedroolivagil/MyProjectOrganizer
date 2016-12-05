package cat.olivadevelop.myprojectorganizer.tools;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

import cat.olivadevelop.myprojectorganizer.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cat.olivadevelop.myprojectorganizer.tools.Tools.HOSTNAME;

/**
 * Created by Oliva on 01/10/2016.
 * Crea la raiz del usuario en el server.
 */

public class CreateNewProjectFile extends AsyncTask<Void, Void, String> {

    private ProgressDialog pd;
    private Context context;

    public CreateNewProjectFile(Context context) {
        this.context = context;
    }

    private String getString(int id_string) {
        return context.getString(id_string);
    }

    @Override
    protected String doInBackground(Void... params) {
        URL url;
        String resStr = null;
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("id_client", Tools.getUserID())
                    .build();

            url = new URL(HOSTNAME + "/php/new_projects_file.php");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = client.newCall(request).execute();
            resStr = response.body().string();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resStr;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setMessage(getString(R.string.pgd_updating));
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.e(Tools.tagLogger(context), result);
        pd.dismiss();
    }
}