package cat.olivadevelop.myprojectorganizer.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.tools.ProjectManager;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

public class ProjectSelected extends AppCompatActivity {

    private int id_project_selected;
    private JSONObject selected_project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_selected);
        id_project_selected = getIntent().getExtras().getInt(ProjectManager.NEW_SELECTED);
        init();
    }

    private void init() {
        try {
            if (id_project_selected >= 0) {
                selected_project = ProjectManager.getProjects().getJSONObject(id_project_selected);
                setTitle(Tools.capitalize(selected_project.getString(ProjectManager.json_project_name)));
            } else {
                msgFailReadProject();
            }
        } catch (JSONException e) {
            msgFailReadProject();
        }
    }

    private void msgFailReadProject() {
        Tools.newSnackBarWithIcon(findViewById(R.id.activity_project_selected), this,
                R.string.error_project_selected,
                R.drawable.ic_warning_white_24dp)
                .show();
    }
}
