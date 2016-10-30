package cat.olivadevelop.myprojectorganizer.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.tools.ProjectManager;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

import static cat.olivadevelop.myprojectorganizer.tools.ProjectManager.json_project_dir_files;
import static cat.olivadevelop.myprojectorganizer.tools.ProjectManager.json_project_form;
import static cat.olivadevelop.myprojectorganizer.tools.ProjectManager.json_project_home_img;

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
                for (int x = 0; x < ProjectManager.getProjects().length(); x++) {
                    if (ProjectManager.getProjects().getJSONObject(x).getInt(ProjectManager.json_project_id_project) == id_project_selected) {
                        selected_project = ProjectManager.getProjects().getJSONObject(x);
                    }
                }
                setTitle(Tools.capitalize(selected_project.getString(ProjectManager.json_project_name)));
                LinearLayout container = (LinearLayout) findViewById(R.id.layoutWrapperProjectSelected);

                String imageUrl = Tools.HOSTNAME + "/clients/" + Tools.getUserID() +
                        selected_project.getString(json_project_dir_files) + "/" +
                        selected_project.getString(json_project_home_img);

                ImageView image = (ImageView) findViewById(R.id.headerImgPrjSelected);
                Picasso.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_camera_black_48dp)
                        .error(R.drawable.ic_close_light)
                        .fit()
                        .centerCrop()
                        .into(image);

                TextView title = (TextView) findViewById(R.id.titleProjectSelected);
                title.setText(Tools.capitalize(selected_project.getString(ProjectManager.json_project_name)));

                TextView subTitle = (TextView) findViewById(R.id.subTitleProjectSelected);
                subTitle.setText(getString(R.string.card_last_update).concat(" ").concat(selected_project.getString(ProjectManager.json_project_last_update)));

                LinearLayout target;
                String labelStr;
                String valueStr;
                TextView tvLabel;
                TextView tvValue;
                JSONObject form = selected_project.getJSONObject(json_project_form);
                Iterator<String> allLabels = form.keys();
                while (allLabels.hasNext()) {
                    labelStr = allLabels.next();
                    valueStr = form.getString(labelStr);
                    Log.e("FORM", "" + labelStr + "; " + valueStr);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.setMargins(Tools.getDP(this, 8), Tools.getDP(this, 8), Tools.getDP(this, 8), Tools.getDP(this, 8));

                    target = new LinearLayout(this);
                    target.setLayoutParams(layoutParams);
                    target.setOrientation(LinearLayout.VERTICAL);
                    target.setPadding(Tools.getDP(this, 24), Tools.getDP(this, 24), Tools.getDP(this, 24), Tools.getDP(this, 24));
                    target.setBackgroundResource(R.color.white);

                    tvLabel = new TextView(this);
                    if (labelStr.equals(ProjectManager.json_project_descript)) {
                        tvLabel.setText(getString(R.string.label_description));
                    } else if (labelStr.equals(ProjectManager.FINISH_PJT)) {
                        tvLabel.setText(getString(R.string.projectIsFinalized));
                    } else {
                        tvLabel.setText(labelStr);
                    }

                    tvValue = new TextView(this);
                    if(Tools.isBooleanValue(valueStr)){
                        tvValue.setText(Tools.getCurrentBooleanValueAsString(this));
                    }else {
                        tvValue.setText(valueStr);
                    }

                    target.addView(tvLabel);
                    target.addView(tvValue);
                    container.addView(target);
                }

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
