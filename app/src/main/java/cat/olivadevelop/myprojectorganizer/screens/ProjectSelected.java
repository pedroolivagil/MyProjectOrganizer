package cat.olivadevelop.myprojectorganizer.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.style.TextAlignment;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.tools.CustomTextView;
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

                CustomTextView title = (CustomTextView) findViewById(R.id.titleProjectSelected);
                title.setTextCapitalized(selected_project.getString(ProjectManager.json_project_name));

                CustomTextView subTitle = (CustomTextView) findViewById(R.id.subTitleProjectSelected);
                subTitle.setTextCapitalized(getString(R.string.card_last_update).concat(" ").concat(selected_project.getString(ProjectManager.json_project_last_update)));

                LinearLayout target;
                String labelStr;
                String valueStr;
                CustomTextView tvLabel;
                DocumentView tvValue;
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

                    tvLabel = new CustomTextView(this);
                    tvLabel.setBold();
                    tvLabel.setTextSize(Tools.getPX(this, getResources().getDimension(R.dimen.size18)));
                    if (labelStr.equals(ProjectManager.json_project_descript)) {
                        tvLabel.setTextCapitalized(getString(R.string.label_description));
                    } else if (labelStr.equals(ProjectManager.FINISH_PJT)) {
                        tvLabel.setTextCapitalized(getString(R.string.projectIsFinalized));
                    } else {
                        tvLabel.setTextCapitalized(labelStr);
                    }

                    LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    tvParams.setMargins(0, Tools.getDP(this, 8), 0, 0);

                    /*tvValue = new TextView(this);
                    tvValue.setLayoutParams(tvParams);*/
                    // Create DocumentView and set plain text
                    // Important: Use DocumentLayout.class
                    tvValue = new DocumentView(this, DocumentView.PLAIN_TEXT);  // Support plain text
                    tvValue.getDocumentLayoutParams().setTextAlignment(TextAlignment.JUSTIFIED);
                    if (Tools.isBooleanValue(valueStr)) {
                        tvValue.setText(Tools.getCurrentBooleanValueAsString(this));
                    } else {
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
