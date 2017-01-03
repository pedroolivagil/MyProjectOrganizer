package cat.olivadevelop.myprojectorganizer.manageData;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cat.olivadevelop.myprojectorganizer.entities.Pais;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cat.olivadevelop.myprojectorganizer.managers.PaisManager.PAISES_ID;
import static cat.olivadevelop.myprojectorganizer.managers.PaisManager.PAISES_ISO;
import static cat.olivadevelop.myprojectorganizer.managers.PaisManager.PAISES_LIST;
import static cat.olivadevelop.myprojectorganizer.managers.PaisManager.PAISES_NOMBRE;

/**
 * Created by Oliva on 30/12/2016.
 */
public class CountryLoader extends AsyncTask<Void, Void, List<Pais>> {
    private Activity activity;
    private String url;

    public CountryLoader(Activity activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    @Override
    protected List<Pais> doInBackground(Void... formbody) {
        List<Pais> paises = new ArrayList<Pais>();
        paises.add(new Pais(0, "00", "-- Selecciona --"));
        try {
            URL url = new URL(this.url);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject json = new JSONObject(response.body().string());
            JSONArray jsonPaises = json.getJSONArray(PAISES_LIST);
            for (int x = 0; x < jsonPaises.length(); x++) {
                JSONObject pais = jsonPaises.getJSONObject(x);
                paises.add(new Pais(
                        pais.getInt(PAISES_ID),
                        pais.getString(PAISES_ISO),
                        pais.getString(PAISES_NOMBRE)
                ));
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return paises;
    }
}
