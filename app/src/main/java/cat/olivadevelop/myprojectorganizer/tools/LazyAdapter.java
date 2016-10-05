package cat.olivadevelop.myprojectorganizer.tools;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cat.olivadevelop.myprojectorganizer.R;

/**
 * Created by Oliva on 05/10/2016.
 */

public class LazyAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    private Activity activity;
    private String[] data;
    private String[] da;
    private String[] t;

    public LazyAdapter(Activity a, String[] d, String[] t, String[] da) {
        activity = a;
        data = d;
        this.t = t;
        this.da = da;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getBaseContext());
    }

    public int getCount() {
        return data.length;
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
        ImageView image = (ImageView) vi.findViewById(R.id.projectHomeImg);
        title.setText(this.t[position]);
        date.setText(this.da[position]);
        imageLoader.DisplayImage(data[position], image);
        return vi;
    }

    /*
    Permite limpiar todos los elementos del recycler
     */
    public void clear(){
        notifyDataSetChanged();
    }

}
