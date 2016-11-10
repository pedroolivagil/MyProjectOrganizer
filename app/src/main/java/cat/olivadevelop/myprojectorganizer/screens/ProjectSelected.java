package cat.olivadevelop.myprojectorganizer.screens;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.managers.Project;
import cat.olivadevelop.myprojectorganizer.managers.ProjectManager;
import cat.olivadevelop.myprojectorganizer.tools.CustomTextView;
import cat.olivadevelop.myprojectorganizer.tools.CustomWebView;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

@RequiresApi(api = Build.VERSION_CODES.M)
public class ProjectSelected extends AppCompatActivity implements View.OnScrollChangeListener {

    private static final String TAG = "PROJECT_SELECTED";
    int alpha;
    private int id_project_selected;
    private Project project;

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
                for (int x = 0; x < ProjectManager.getProjectList().size(); x++) {
                    if (ProjectManager.getProjectList().get(x).getId() == id_project_selected) {
                        project = ProjectManager.getProjectList().get(x);
                    }
                }
                setTitle(Tools.capitalize(project.getName()));
                ScrollView mainContainer = (ScrollView) findViewById(R.id.activity_project_selected);
                mainContainer.setOnScrollChangeListener(this);
                LinearLayout container = (LinearLayout) findViewById(R.id.layoutWrapperProjectSelected);

                ImageView image = (ImageView) findViewById(R.id.headerImgPrjSelected);
                Picasso.with(this)
                        .load(project.getHomeImage())
                        .placeholder(R.drawable.ic_camera_black_48dp)
                        .error(R.drawable.ic_close_light)
                        .fit()
                        .centerCrop()
                        .into(image);

                CustomTextView title = (CustomTextView) findViewById(R.id.titleProjectSelected);
                title.setTextCapitalized(project.getName());

                CustomTextView subTitle = (CustomTextView) findViewById(R.id.subTitleProjectSelected);
                subTitle.setTextCapitalized(getString(R.string.card_last_update).concat(" ").concat(project.getLastUpdate()));

                JSONObject form = project.getForm();

                //añadimos la tarjetas de información obligatorias, descripción y terminado
                container.addView(getTarget(ProjectManager.FINISH_PJT, form));
                container.addView(getTarget(ProjectManager.json_project_descript, form));

                // añadimos las tarjetas de información creadas por el usuario
                String labelStr;
                Iterator<String> allLabels = form.keys();
                while (allLabels.hasNext()) {
                    labelStr = allLabels.next();
                    if ((!labelStr.equals(ProjectManager.json_project_descript))
                            && (!labelStr.equals(ProjectManager.FINISH_PJT))) {
                        container.addView(getTarget(labelStr, form));
                    }
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

    private CardView getTarget(String labelStr, JSONObject form) throws JSONException {
        String valueStr = form.getString(labelStr);
        Log.e("FORM", "" + labelStr + "; " + valueStr);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        //layoutParams.setMargins(Tools.getDP(this, 8), Tools.getDP(this, 8), Tools.getDP(this, 8), Tools.getDP(this, 8));

        LinearLayout target = new LinearLayout(this);
        target.setLayoutParams(layoutParams);
        target.setOrientation(LinearLayout.VERTICAL);
        target.setPadding(Tools.getDP(this, 24), Tools.getDP(this, 24), Tools.getDP(this, 24), Tools.getDP(this, 24));
        target.setBackgroundResource(R.color.white);

        CustomTextView tvLabel = new CustomTextView(this);
        tvLabel.setBold();
        tvLabel.setTextSize(Tools.getPX(this, getResources().getDimension(R.dimen.size18)));
        switch (labelStr) {
            case ProjectManager.json_project_descript:
                tvLabel.setTextCapitalized(getString(R.string.label_description));
                break;
            case ProjectManager.FINISH_PJT:
                tvLabel.setTextCapitalized(getString(R.string.projectIsFinalized));
                break;
            default:
                tvLabel.setTextCapitalized(labelStr);
                break;
        }

        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        tvParams.setMargins(0, Tools.getDP(this, 8), 0, 0);

        CustomWebView webView = new CustomWebView(this);
        webView.setLayoutParams(tvParams);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        if (Tools.isBooleanValue(valueStr)) {
            webView.setText(Tools.getCurrentBooleanValueAsString(this));
        } else {
            webView.setText(valueStr);
        }

        target.addView(tvLabel);
        target.addView(webView);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(Tools.getDP(this, 8), Tools.getDP(this, 8), Tools.getDP(this, 8), Tools.getDP(this, 8));

        CardView cardView = new CardView(this);
        cardView.setContentPadding(Tools.getDP(this, 8), Tools.getDP(this, 8), Tools.getDP(this, 8), Tools.getDP(this, 8));
        cardView.setCardElevation(Tools.getDP(this, 5));
        cardView.setLayoutParams(cardParams);
        cardView.addView(target);
        return cardView;
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //Log.e(TAG, "x->" + scrollX + "; y->" + scrollY + "; ox->" + oldScrollX + "; xy->" + oldScrollY);
        if (scrollY < 600) {
            alpha = ((scrollY * 100) / 600);
            Log.e(TAG, "" + alpha);
        }
    }
}
