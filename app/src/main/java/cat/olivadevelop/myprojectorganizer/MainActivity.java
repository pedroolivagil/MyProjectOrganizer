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
import cat.olivadevelop.myprojectorganizer.tools.Tools;
import cat.olivadevelop.myprojectorganizer.tools.UrlDownloader;


public class MainActivity extends AppCompatActivity {

    /*public static final String id_client = "c508260d3dd0d72608864428f71b4571";

    private String url = "http://projects.codeduo.cat/" + id_client + "/projects.json";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewProject.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        if (Tools.getPrefs().getString(Tools.USER_EMAIL, null) == null) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
        }
        
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i("email", "" + Tools.getPrefs().getString("email", ""));

        if (Tools.getPrefs().getString("url", null) != null) {
            // cargamos los projects del usuario
            UrlDownloader.activity = MainActivity.this;
            new UrlDownloader().execute(Tools.getPrefs().getString("url", null));
        }
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

        return super.onOptionsItemSelected(item);
    }
}
