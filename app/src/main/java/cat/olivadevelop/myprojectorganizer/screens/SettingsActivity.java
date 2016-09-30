package cat.olivadevelop.myprojectorganizer.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

public class SettingsActivity extends AppCompatActivity {

    private EditText putEmailInPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onResume() {

        putEmailInPrefs = (EditText) findViewById(R.id.putEmailInPrefs);
        if (Tools.getPrefs().getString(Tools.USER_EMAIL, null) != null){
            putEmailInPrefs.setText(Tools.getPrefs().getString(Tools.USER_EMAIL, null));
        }
        super.onResume();
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
                Tools.putInPrefs().putString(Tools.USER_EMAIL, putEmailInPrefs.getText().toString()).apply();
                Tools.putInPrefs().putString(Tools.USER_URL, "http://projects.codeduo.cat/" + Tools.encrypt(putEmailInPrefs.getText().toString()) + "/projects.json").apply();

                //Tools.newSnackBarWithIcon(getWindow().getCurrentFocus(), this, R.string.settings_updated, R.drawable.ic_info_white_24dp).show();
                Toast.makeText(this,R.string.settings_updated,Toast.LENGTH_LONG).show();
            } else {
                Tools.newSnackBarWithIcon(getWindow().getCurrentFocus(), this, R.string.notnull_field, R.drawable.ic_warning_white_24dp).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
