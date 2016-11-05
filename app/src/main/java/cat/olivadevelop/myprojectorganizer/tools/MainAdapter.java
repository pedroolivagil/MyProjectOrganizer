package cat.olivadevelop.myprojectorganizer.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.managers.Project;
import cat.olivadevelop.myprojectorganizer.managers.ProjectManager;
import cat.olivadevelop.myprojectorganizer.screens.ProjectSelected;

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
                vi.setId(currentProject.getId());
                CustomTextView title = (CustomTextView) vi.findViewById(R.id.projectName);
                CustomTextView lastUpdate = (CustomTextView) vi.findViewById(R.id.projectLastUpdate);
                CustomWebView descrip = (CustomWebView) vi.findViewById(R.id.projectMainDescript);
                ImageView image = (ImageView) vi.findViewById(R.id.projectHomeImg);
                if (currentProject.getName().trim().length() > 20) {
                    title.setTextCapitalized(currentProject.getName().substring(0, 16).concat("..."));
                } else {
                    title.setTextCapitalized(currentProject.getName());
                }
                if (currentProject.getDescription() != null) {
                    descrip.setText(currentProject.getDescription(), R.dimen.size12);
                } else {
                    descrip.setText(activity.getString(R.string.description_empty), R.dimen.size12);
                }
                lastUpdate.setTextCapitalized(activity.getString(R.string.card_last_update)
                        .concat(" ")
                        .concat(currentProject.getLastUpdate())
                );
                Transformation transformation = new RoundedTransformationBuilder()
                        .borderColor(Color.BLACK).borderWidthDp(0)
                        .cornerRadiusDp(3).oval(false).build();

                Picasso.with(activity).load(currentProject.getHomeImage())
                        .placeholder(R.drawable.ic_camera_black_48dp)
                        .error(R.drawable.ic_close_light).fit()
                        .transform(transformation).centerCrop().into(image);

                vi.setOnClickListener(this);
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
        if (v.getId() >= 0) {
            Intent projectSelected = new Intent(activity, ProjectSelected.class);
            projectSelected.putExtra(ProjectManager.NEW_SELECTED, v.getId());
            activity.startActivity(projectSelected);
        }
    }
}
