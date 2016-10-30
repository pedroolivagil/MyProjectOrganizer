package cat.olivadevelop.myprojectorganizer.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.tools.LazyAdapter;
import cat.olivadevelop.myprojectorganizer.tools.MainLoader;
import cat.olivadevelop.myprojectorganizer.tools.ProjectManager;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

import static cat.olivadevelop.myprojectorganizer.tools.Tools.HOSTNAME;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    FloatingActionButton fab;
    ListView list;
    LazyAdapter adapter;
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.i(Tools.PREFS_USER_ID, Tools.getUserID());
        Log.i(Tools.PREFS_USER_EMAIL, Tools.getPrefs().getString(Tools.PREFS_USER_EMAIL, ""));

        /*Intent projectSelected = new Intent(this, ProjectSelected.class);
        projectSelected.putExtra(ProjectManager.NEW_SELECTED, 2);
        startActivity(projectSelected);*/

        if (Tools.getPrefs().getString(Tools.PREFS_USER_EMAIL, null) == null) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
        } else {
            //Obtenemos una referencia al viewgroup SwipeLayout
            swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
            //Indicamos que listener recogerá la retrollamada (callback), en este caso, será el metodo OnRefresh de esta clase.
            swipeLayout.setOnRefreshListener(this);
            //Podemos espeficar si queremos, un patron de colores diferente al patrón por defecto.
            swipeLayout.setColorSchemeResources(
                    R.color.colorAccent,
                    R.color.colorPrimary,
                    android.R.color.holo_orange_light,
                    R.color.colorPrimaryDark
            );
            // cargamos los projects del usuario
            loadProjects();
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProjects();
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
        /*if (id == R.id.action_auto_update) {
            autoRefresh();
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void loadProjects() {
        new MainLoader(this).execute(HOSTNAME + "/clients/" + Tools.getUserID() + "/" + ProjectManager.PROJECTS_FILENAME);
        list = (ListView) findViewById(R.id.projectList);
        adapter = new LazyAdapter(this,
                Tools.getUrlImgArray(),
                Tools.getTitlePrjctArray(),
                Tools.getDatePrjctArray(),
                Tools.getDescriptPrjctArray(),
                Tools.getIDSPrjctArray()
        );
        list.setAdapter(adapter);
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int filaSuperior = (list == null || list.getChildCount() == 0) ? 0 : list.getChildAt(0).getTop();    //Estamos en el elemento superior
                if (swipeLayout != null) {
                    swipeLayout.setEnabled(filaSuperior >= 0);  //Activamos o desactivamos el swipe layout segun corresponda
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (list != null) {
            list.setAdapter(null);
        }
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        autoRefresh();
    }

    public void autoRefresh() {
        if (swipeLayout != null) {
            //Aqui ejecutamos el codigo necesario para refrescar nuestra interfaz grafica.
            //Antes de ejecutarlo, indicamos al swipe layout que muestre la barra indeterminada de progreso.
            swipeLayout.setRefreshing(true);
            loadProjects();
            list.postDelayed(new Runnable() {
                public void run() {
                    //Se supone que aqui hemos realizado las tareas necesarias de refresco, y que ya podemos ocultar la barra de progreso
                    swipeLayout.setRefreshing(false);
                }
            }, 2000);
        }
    }
}
