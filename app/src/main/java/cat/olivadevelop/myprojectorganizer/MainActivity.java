package cat.olivadevelop.myprojectorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import cat.olivadevelop.myprojectorganizer.screens.NewProject;
import cat.olivadevelop.myprojectorganizer.screens.SettingsActivity;
import cat.olivadevelop.myprojectorganizer.tools.MainLoader;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

import static cat.olivadevelop.myprojectorganizer.tools.Tools.PREFS_USER_URL;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View view) {
        if (view == fab) {
            Intent intent = new Intent(this, NewProject.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        if (Tools.getPrefs().getString(Tools.PREFS_USER_EMAIL, null) == null) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
        } else {
            if (Tools.getPrefs().getString("url", null) != null) {
                // cargamos los projects del usuario
                new MainLoader(this).execute(Tools.getPrefs().getString(PREFS_USER_URL, null));
            }
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(this);
        }
        Log.i(Tools.PREFS_USER_ID, Tools.getUserID());
        Log.i(Tools.PREFS_USER_EMAIL, Tools.getPrefs().getString(Tools.PREFS_USER_EMAIL, ""));
        Log.i(Tools.PREFS_USER_URL, Tools.getPrefs().getString(Tools.PREFS_USER_URL, ""));
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_auto_update) {
            new MainLoader(this).execute(Tools.getPrefs().getString(PREFS_USER_URL, null));
        }

        return super.onOptionsItemSelected(item);
    }
}
