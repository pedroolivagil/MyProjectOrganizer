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
import android.widget.ListView;

import cat.olivadevelop.myprojectorganizer.screens.NewProject;
import cat.olivadevelop.myprojectorganizer.screens.SettingsActivity;
import cat.olivadevelop.myprojectorganizer.tools.LazyAdapter;
import cat.olivadevelop.myprojectorganizer.tools.MainLoader;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

import static cat.olivadevelop.myprojectorganizer.tools.Tools.HOSTNAME;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab;
    ListView list;
    LazyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Tools.getPrefs().getString(Tools.PREFS_USER_EMAIL, null) != null) {
        }
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
        list = (ListView) findViewById(R.id.projectList);
        adapter = new LazyAdapter(this, Tools.getUrlImgArray(), Tools.getTitlePrjctArray(), Tools.getDatePrjctArray());
        list.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        list.setAdapter(null);
        super.onDestroy();
    }
}
