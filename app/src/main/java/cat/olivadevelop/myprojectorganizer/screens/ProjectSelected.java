package cat.olivadevelop.myprojectorganizer.screens;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.entities.Project;
import cat.olivadevelop.myprojectorganizer.managers.ProjectManager;
import cat.olivadevelop.myprojectorganizer.tools.CustomTextView;
import cat.olivadevelop.myprojectorganizer.tools.CustomWebView;
import cat.olivadevelop.myprojectorganizer.tools.GenericScreen;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_flag_activo;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_images_descript;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_images_height;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_images_url;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_images_width;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_targets_label;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_targets_value;

@RequiresApi(api = Build.VERSION_CODES.M)
public class ProjectSelected extends GenericScreen implements View.OnScrollChangeListener {

    int alpha;
    private String id_project_selected;
    private Project project;
    private Project projectOptions;
    private LinearLayout frameProjectGallery;
    private Dialog dialogPreviewImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_selected);

        id_project_selected = getIntent().getExtras().getString(ProjectManager.PROJECT_SELECTED);
        dialogPreviewImg = dialogImagePreview();
        try {
            downloadProjectInfo();
            init();
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_project, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit_project) {
        }
        if (id == R.id.action_delete_project) {
            if (project != null) {
                try {
                    ProjectManager.delete(this, project);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private synchronized void downloadProjectInfo() throws ExecutionException, InterruptedException {
        project = ProjectManager.downloadById(this, id_project_selected);
    }

    private synchronized void init() throws InterruptedException, JSONException {
        // iniciamos el projectView
        if (project != null && !project.isEmpty()) {
            setTitle(Tools.capitalize(project.getName()));
            ScrollView mainContainer = (ScrollView) findViewById(R.id.activity_project_selected);
            mainContainer.setOnScrollChangeListener(this);
            LinearLayout container = (LinearLayout) findViewById(R.id.layoutWrapperProjectSelected);

            try {
                Log.e(Tools.tagLogger(this), project.toJSON().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ImageView image = (ImageView) findViewById(R.id.headerImgPrjSelected);
            Tools.picassoImage(this, project.getHomeImage(), image);

            CustomTextView title = (CustomTextView) findViewById(R.id.titleProjectSelected);
            title.setTextCapitalized(project.getName());

            CustomTextView subTitle = (CustomTextView) findViewById(R.id.subTitleProjectSelected);
            subTitle.setTextCapitalized(getString(R.string.card_last_update).concat(" ").concat(project.getLastUpdate()));

            // Array de imagenes frameProjectGallery
            if (project.getProjectImages().length() > 0) {
                frameProjectGallery = (LinearLayout) findViewById(R.id.frameProjectGallery);
                for (int x = 0; x < project.getProjectImages().length(); x++) {
                    final int finalX = x;
                    final JSONObject urlImages = project.getProjectImages().getJSONObject(x);
                    if (Tools.parseBoolean(urlImages.getString(json_project_flag_activo))) {
                        final String url = urlImages.getString(json_project_images_url);
                        final String descript = urlImages.getString(json_project_images_descript);
                        ImageView ivProject = new ImageView(this);
                        LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        if (x < project.getProjectImages().length() - 1) {
                            ivParams.setMargins(0, 0, Tools.getDP(this, 8), 0);
                        }
                        ivProject.setLayoutParams(ivParams);
                        ivProject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    showAlert(url, descript,
                                            (float) urlImages.getDouble(json_project_images_width),
                                            (float) urlImages.getDouble(json_project_images_height)
                                    );
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Tools.picassoImage(this, project.mountUrlImage(url), ivProject);
                        frameProjectGallery.addView(ivProject);
                    }
                }
            }

            //añadimos la tarjetas de información obligatorias, descripción y terminado
            container.addView(getTarget(ProjectManager.json_project_flag_finish, String.valueOf(project.isFlagFinished())));
            container.addView(getTarget(ProjectManager.json_project_descript, project.getDescription()));

            // añadimos las tarjetas de información creadas por el usuario
            JSONArray targets = project.getTarjetas();
            if (targets.length() > 0) {
                for (int x = 0; x < targets.length(); x++) {
                    JSONObject target = targets.getJSONObject(x);
                    container.addView(getTarget(target.getString(json_project_targets_label), target.getString(json_project_targets_value)));
                }
            }
        } else {
            msgFailReadProject();
            Log.e(Tools.tagLogger(this), "id_project_selected == 0");
        }
    }

    private void msgFailReadProject() {
        Tools.newSnackBarWithIcon(findViewById(R.id.activity_project_selected), this,
                R.string.error_project_selected,
                R.drawable.ic_warning_white_24dp)
                .show();
    }

    @Deprecated
    private CardView getTarget(String labelStr, JSONObject form) throws JSONException {
        String valueStr = form.getString(labelStr);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

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
            case ProjectManager.json_project_flag_finish:
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

    private CardView getTarget(String labelStr, String valueStr) throws JSONException {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

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
            case ProjectManager.json_project_flag_finish:
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
        if (scrollY < 600) {
            alpha = ((scrollY * 100) / 600);
        }
    }

    public AlertDialog dialogImagePreview() {
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_preview_img, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);


        return builder.create();
    }

    private void dismissAlert() {
        if (dialogPreviewImg != null) {
            dialogPreviewImg.dismiss();
        }
    }

    private void showAlert(String url, String descript, float w, float h) {
        if (dialogPreviewImg != null) {
            dialogPreviewImg.show();
            ImageView iv = (ImageView) dialogPreviewImg.findViewById(R.id.imageSelectedPreview);
            ImageView closeView = (ImageView) dialogPreviewImg.findViewById(R.id.closeView);
            closeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissAlert();
                }
            });
            CustomTextView tv = (CustomTextView) dialogPreviewImg.findViewById(R.id.descriptSelectedPreview);
            if (descript != null && !descript.equals("")) {
                tv.setTextCapitalized(descript);
            } else {
                tv.setVisibility(View.GONE);
            }
            Tools.picassoImageWithoutTransform(this, project.mountUrlImage(url), iv, new float[]{w, h});
        }
    }
}
