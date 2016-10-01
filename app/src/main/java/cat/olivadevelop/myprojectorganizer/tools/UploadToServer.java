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
 */

public class UploadToServer extends AsyncTask<Void, Void, String> {

    private ProgressDialog pd;
    private Context context;

    public UploadToServer(Context context) {
        this.context = context;
    }

    private String getString(int id_string) {
        return context.getString(id_string);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setMessage(getString(R.string.pgd_updating));
        pd.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        URL url;
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("id_client", "" + Tools.getPrefs().getString(Tools.PREFS_USER_ID, null))
                    .build();

            url = new URL(HOSTNAME + "/new_projects_file.php");

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = client.newCall(request).execute();
            String resStr = response.body().string();
            Log.i("ResultHTTP", resStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        pd.hide();
        pd.dismiss();
    }
}