package cat.olivadevelop.myprojectorganizer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import cat.olivadevelop.myprojectorganizer.screens.NewProject;
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

        Tools.setPrefs(
                getSharedPreferences(
                        Tools.PREFS_NAME,
                        Context.MODE_PRIVATE
                )
        );
        //c13067ecd9533b0504b8db033a794030
        //c13067ecd9533b0504b8db033a794030
        Log.i("id_client", "" + Tools.encrypt(Tools.getPrefs().getString("email", "")));
        Tools.putInPrefs().putString("email", "onion_oliva@gmail.com");
        Tools.putInPrefs().putString("url", "http://projects.codeduo.cat/" + Tools.encrypt(Tools.getPrefs().getString("email", "")) + "/projects.json");
        Tools.putInPrefs().commit();

        if (Tools.getPrefs().getString("url", null) != null) {
            // cargamos los projects del usuario
            UrlDownloader.activity = MainActivity.this;
            new UrlDownloader().execute(Tools.getPrefs().getString("url", null));
        }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
