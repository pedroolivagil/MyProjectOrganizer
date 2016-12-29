package cat.olivadevelop.myprojectorganizer.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.adapters.MainAdapter;
import cat.olivadevelop.myprojectorganizer.managers.ProjectManager;
import cat.olivadevelop.myprojectorganizer.tools.GenericScreen;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

import static java.lang.Thread.sleep;


public class MainScreen extends GenericScreen implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    FloatingActionButton fab;
    ListView list;
    MainAdapter adapter;
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Tools.getPrefs().getString(Tools.PREFS_USER_ID, null) == null) {
            Intent welcome = new Intent(this, WelcomeScreen.class);
            startActivity(welcome);
        } else {
            //Obtenemos una referencia al viewgroup SwipeLayout
            swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
            //Indicamos que listener recogerá la retrollamada (callback), en este caso, será el metodo OnRefresh de esta clase.
            swipeLayout.setOnRefreshListener(this);
            //Podemos espeficar si queremos, un patron de colores diferente al patrón por defecto.
            swipeLayout.setColorSchemeResources(
                    R.color.colorAccent,
                    android.R.color.holo_orange_light,
                    R.color.colorPrimary
            );
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(this);

            autoRefresh();
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
        // descargamos los proyectos y obtenemos el estado
        ProjectManager.download(this, swipeLayout);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    int splashTime = 10000;
                    while (waited < splashTime) {
                        sleep(100);
                        if (!Tools.isprojectListResult()) {
                            waited += 100;
                        } else {
                            showProjects();
                            break;
                        }
                        Log.e(Tools.tagLogger(MainScreen.this), "THREAD -> " + Tools.isprojectListResult());
                    }
                    Log.e(Tools.tagLogger(MainScreen.this), "THREAD FINAL -> " + Tools.isprojectListResult());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showProjects() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list = (ListView) findViewById(R.id.projectList);
                // My AsyncTask is done and onPostExecute was called
                adapter = new MainAdapter(MainScreen.this, ProjectManager.getProjectList());
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
                list.postDelayed(new Runnable() {
                    public void run() {
                        // Se supone que aqui hemos realizado las tareas necesarias de refresco,
                        // y que ya podemos ocultar la barra de progreso
                        swipeLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void onRefresh() {
        autoRefresh();
    }

    public void autoRefresh() {
        if (!Tools.isNetworkAvailable(this)) {
            Tools.newSnackBar(findViewById(R.id.swipeRefresh), this, R.string.cannot_be_connect).show();
            if (swipeLayout != null) {
                swipeLayout.setRefreshing(false);
            }
        } else {
            if (swipeLayout != null) {
                swipeLayout.setRefreshing(true);
                loadProjects();
            }
        }
    }
}
