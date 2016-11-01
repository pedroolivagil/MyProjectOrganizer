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

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.screens.ProjectSelected;

/**
 * Created by Oliva on 05/10/2016.
 */

public class LazyAdapter extends BaseAdapter implements View.OnClickListener {

    private static LayoutInflater inflater = null;
    private Activity activity;
    private String[] description;
    private String[] img;
    private String[] lstUpdt;
    private String[] txt;
    private int[] ids;

    public LazyAdapter(Activity a, String[] img, String[] txt, String[] lstUpdt, String[] description, int[] ids) {
        activity = a;
        this.ids = ids;
        this.img = img;
        this.txt = txt;
        this.lstUpdt = lstUpdt;
        this.description = description;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return img.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.project_list, null);
        }
        vi.setOnClickListener(this);
        if (this.ids.length > 0) {
            vi.setId(this.ids[position]);
        }
        CustomTextView title = (CustomTextView) vi.findViewById(R.id.projectName);
        CustomTextView date = (CustomTextView) vi.findViewById(R.id.projectLastUpdate);
        CustomTextView descrip = (CustomTextView) vi.findViewById(R.id.projectMainDescript);
        ImageView image = (ImageView) vi.findViewById(R.id.projectHomeImg);
        if (this.txt[position].replaceAll(" ", "").length() > 17) {
            title.setTextCapitalized(this.txt[position].substring(0, 16).concat("..."));
        } else {
            title.setTextCapitalized(this.txt[position]);
        }
        date.setTextCapitalized(activity.getString(R.string.card_last_update) + " " + this.lstUpdt[position]);
        if (this.description.toString().trim().length() > 0) {
            descrip.setTextCapitalized(this.description[position]);
        } else {
            descrip.setTextCapitalized(activity.getString(R.string.description_empty));
        }
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(0)
                .cornerRadiusDp(3)
                .oval(false)
                .build();

        Picasso.with(activity)
                .load(this.img[position])
                .placeholder(R.drawable.ic_camera_black_48dp)
                .error(R.drawable.ic_close_light)
                .fit()
                .transform(transformation)
                .centerCrop()
                .into(image);
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
