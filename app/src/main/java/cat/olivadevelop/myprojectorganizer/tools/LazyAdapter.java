package cat.olivadevelop.myprojectorganizer.tools;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cat.olivadevelop.myprojectorganizer.R;

/**
 * Created by Oliva on 05/10/2016.
 */

public class LazyAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    private String[] description;
    private String[] img;
    private String[] lstUpdt;
    private String[] txt;

    public LazyAdapter(Activity a, String[] img, String[] txt, String[] lstUpdt, String[] description) {
        activity = a;
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

        TextView title = (TextView) vi.findViewById(R.id.projectName);
        TextView date = (TextView) vi.findViewById(R.id.projectLastUpdate);
        TextView descrip = (TextView) vi.findViewById(R.id.projectMainDescript);
        ImageView image = (ImageView) vi.findViewById(R.id.projectHomeImg);
        if (this.txt[position].replaceAll(" ", "").length() > 17) {
            title.setText(this.txt[position].substring(0, 16).concat("..."));
        } else {
            title.setText(this.txt[position]);
        }
        date.setText(this.lstUpdt[position]);
        if (this.description.length > 0) {
            descrip.setText(this.description[position]);
        } else {
            descrip.setText(activity.getString(R.string.description_empty));
        }
        Picasso.with(activity)
                .load(this.img[position])
                .placeholder(R.drawable.ic_camera_black_48dp)
                .error(R.drawable.ic_close_light)
                .into(image);
        return vi;
    }

    /*
    Permite limpiar todos los elementos del recycler
     */
    public void clear() {
        notifyDataSetChanged();
    }

}
