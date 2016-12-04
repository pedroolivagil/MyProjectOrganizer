package cat.olivadevelop.myprojectorganizer.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.List;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.managers.ProjectManager;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

/**
 * Created by Oliva on 17/11/2016.
 */

public class GridViewAdapter extends BaseAdapter {

    private final Activity activity;
    private final LayoutInflater inflater;
    private final List<File> urlsImages;

    public GridViewAdapter(Activity activity, List<File> urlsImages) {
        this.activity = activity;
        this.urlsImages = Tools.sortFileList(urlsImages, ProjectManager.TYPE_SORT_BY_DESC);
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (urlsImages != null) {
            return urlsImages.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.image_view_adapter, null);
            if (getCount() > 0) {
//                LinearLayout borrarImagen = (LinearLayout) vi.findViewById(R.id.borrarImagen);
//                borrarImagen.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.e("POSICION", "" + position);
//                    }
//                });
                ImageView image = (ImageView) vi.findViewById(R.id.imgBodyAdapter);
                Transformation transformation = new RoundedTransformationBuilder()
                        .borderColor(Color.BLACK).borderWidthDp(0)
                        .cornerRadiusDp(3).oval(false).build();
                Tools.picassoImage(activity, urlsImages.get(position), image, transformation);
            }
            return vi;
        }
        return convertView;
    }
}
