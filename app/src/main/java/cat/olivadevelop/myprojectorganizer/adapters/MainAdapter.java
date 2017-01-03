package cat.olivadevelop.myprojectorganizer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.entities.Project;
import cat.olivadevelop.myprojectorganizer.managers.ProjectManager;
import cat.olivadevelop.myprojectorganizer.screens.ProjectSelected;
import cat.olivadevelop.myprojectorganizer.tools.CustomTextView;
import cat.olivadevelop.myprojectorganizer.tools.CustomWebView;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

/**
 * Created by Oliva on 05/10/2016.
 */

public class MainAdapter extends BaseAdapter implements View.OnClickListener {

    private LayoutInflater inflater = null;
    private ArrayList<Project> projectsList;
    private Activity activity;

    public MainAdapter(Activity activity, ArrayList<Project> projectsList) {
        this.activity = activity;
        this.projectsList = projectsList;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        if (this.projectsList != null) {
            return this.projectsList.size();
        } else {
            return 0;
        }
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (projectsList != null) {
            Project currentProject = projectsList.get(position);
            if (!currentProject.isEmpty()) {
                if (convertView == null) {
                    vi = inflater.inflate(R.layout.project_list, null);
                }
                ImageView btnSelectProject = (ImageView) vi.findViewById(R.id.btnSelectProject);
                CustomTextView title = (CustomTextView) vi.findViewById(R.id.projectName);
                title.setBold();
                CustomTextView lastUpdate = (CustomTextView) vi.findViewById(R.id.projectLastUpdate);
                CustomWebView descrip = (CustomWebView) vi.findViewById(R.id.projectMainDescript);
                ImageView image = (ImageView) vi.findViewById(R.id.projectHomeImg);
                if (currentProject.getName().trim().length() > 28) {
                    title.setTextCapitalized(currentProject.getName().substring(0, 25).concat("..."));
                } else {
                    title.setTextCapitalized(currentProject.getName());
                }
                if (currentProject.getDescription() != null) {
                    descrip.setText(currentProject.getDescription(), R.dimen.size14);
                } else {
                    descrip.setText(activity.getString(R.string.description_empty), R.dimen.size14);
                }
                lastUpdate.setTextCapitalized(activity.getString(R.string.card_last_update)
                        .concat(" ")
                        .concat(currentProject.getLastUpdate())
                );
                Transformation transformation = new RoundedTransformationBuilder()
                        .borderColor(Color.BLACK).borderWidthDp(0)
                        .cornerRadiusDp(3).oval(false).build();

                Tools.picassoImage(activity, currentProject.getHomeImage(), image, transformation);

                LinearLayout label_bg1_finished_pjct_list = (LinearLayout) vi.findViewById(R.id.label_bg1_finished_pjct_list);
                LinearLayout label_bg2_finished_pjct_list = (LinearLayout) vi.findViewById(R.id.label_bg2_finished_pjct_list);
                RelativeLayout label_finished_pjct_list = (RelativeLayout) vi.findViewById(R.id.label_finished_pjct_list);

                if (currentProject.isFlagFinished()) {
                    label_bg1_finished_pjct_list.setVisibility(View.VISIBLE);
                    label_bg2_finished_pjct_list.setVisibility(View.VISIBLE);
                    label_finished_pjct_list.setVisibility(View.VISIBLE);
                } else {
                    label_bg1_finished_pjct_list.setVisibility(View.GONE);
                    label_bg2_finished_pjct_list.setVisibility(View.GONE);
                    label_finished_pjct_list.setVisibility(View.GONE);
                }

                if (!currentProject.getId().equals("")) {
                    btnSelectProject.setTag(currentProject.getId());
                }
                btnSelectProject.setOnClickListener(this);
            } else { // cargamos un layout diferente para el proyecto vacío
                if (convertView == null) {
                    vi = inflater.inflate(R.layout.project_empty, null);
                }
            }
        } else { // cargamos un layout diferente para el proyecto vacío
            if (convertView == null) {
                vi = inflater.inflate(R.layout.project_empty, null);
            }
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        if (!v.getTag().equals("")) {
            Intent projectSelected = new Intent(activity, ProjectSelected.class);
            projectSelected.putExtra(ProjectManager.PROJECT_SELECTED, v.getTag().toString());
            activity.startActivity(projectSelected);
        }
    }
}
