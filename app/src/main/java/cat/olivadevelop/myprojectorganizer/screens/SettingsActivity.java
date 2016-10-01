package cat.olivadevelop.myprojectorganizer.screens;

import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

import java.io.IOException;
import java.net.URL;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.tools.Tools;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cat.olivadevelop.myprojectorganizer.tools.Tools.HOSTNAME;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private int REQUEST_CODE = 1;
    private TextView putEmailInPrefs;
    private LinearLayout changeEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        putEmailInPrefs = (TextView) findViewById(R.id.putEmailInPrefs);
        changeEmail = (LinearLayout) findViewById(R.id.changeEmail);
        changeEmail.setOnClickListener(this);
        if (Tools.getPrefs().getString(Tools.PREFS_USER_EMAIL, null) == null) {
            setEmail();
        } else {
            putEmailInPrefs.setText(Tools.getPrefs().getString(Tools.PREFS_USER_EMAIL, null));
        }
    }

    private void setEmail() {
        Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
        startActivityForResult(googlePicker, REQUEST_CODE);
    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            putEmailInPrefs.setText(accountName);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            if (!putEmailInPrefs.getText().toString().equals("")) {
                saveEmail();
            } else {
                Tools.newSnackBarWithIcon(getWindow().getCurrentFocus(), this, R.string.notnull_field, R.drawable.ic_warning_white_24dp).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == changeEmail) {
            setEmail();
        }
    }

    public void saveEmail() {
        Tools.putInPrefs().putString(Tools.PREFS_USER_EMAIL, putEmailInPrefs.getText().toString()).apply();
        Tools.putInPrefs().putString(Tools.PREFS_USER_ID, Tools.encrypt(putEmailInPrefs.getText().toString())).apply();
        Tools.putInPrefs().putString(Tools.PREFS_USER_URL, HOSTNAME + "/" + Tools.encrypt(putEmailInPrefs.getText().toString()) + "/projects.json").apply();

        //Tools.newSnackBarWithIcon(getWindow().getCurrentFocus(), this, R.string.settings_updated, R.drawable.ic_info_white_24dp).show();
        Toast.makeText(this, R.string.settings_updated, Toast.LENGTH_LONG).show();
        new UploadToServer().execute();
    }

    public class UploadToServer extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(SettingsActivity.this);

        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            URL url = null;
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

    /*private void createParentDirectory() {
        ArrayList<> parameters = new ArrayList<>();
        URL url = null;
        try {
            url = new URL(HOSTNAME + "/new_projects_file.php");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            String resStr = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
