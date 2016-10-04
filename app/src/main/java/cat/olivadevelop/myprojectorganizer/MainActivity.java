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

import static cat.olivadevelop.myprojectorganizer.tools.Tools.HOSTNAME;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        Log.i(Tools.PREFS_USER_ID, Tools.getUserID());
        Log.i(Tools.PREFS_USER_EMAIL, Tools.getPrefs().getString(Tools.PREFS_USER_EMAIL, ""));

        if (Tools.getPrefs().getString(Tools.PREFS_USER_EMAIL, null) == null) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
        } else {
            // cargamos los projects del usuario
            loadProjects();
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(this);
        }
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        if (view == fab) {
            Intent intent = new Intent(this, NewProject.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
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
            loadProjects();
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadProjects() {
        new MainLoader(this).execute(HOSTNAME + "/clients/" + Tools.getUserID() + "/" + Tools.PROJECTS_FILENAME);
    }
}
