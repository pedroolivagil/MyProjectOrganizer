package cat.olivadevelop.myprojectorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import cat.olivadevelop.myprojectorganizer.screens.NewProject;
import cat.olivadevelop.myprojectorganizer.tools.UrlDownloader;


public class MainActivity extends AppCompatActivity {

    public static final String id_client = "c508260d3dd0d72608864428f71b4571";
    private String url = "http://projects.codeduo.cat/" + id_client + "/projects.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // cargamos los projects del usuario
        UrlDownloader.activity = MainActivity.this;
        new UrlDownloader().execute(url);

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
