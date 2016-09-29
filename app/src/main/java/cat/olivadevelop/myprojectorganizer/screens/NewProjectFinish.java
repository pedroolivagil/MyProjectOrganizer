package cat.olivadevelop.myprojectorganizer.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

public class NewProjectFinish extends AppCompatActivity {

    private String projectName;
    private String projectImag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project_finish);

        projectName = getIntent().getExtras().getString("nameProject");
        projectImag = getIntent().getExtras().getString("imageProject");

        TextView textView = (TextView) findViewById(R.id.nameProjectPreview);
        textView.setText(projectName);

        if (projectImag != null) {
            Log.i("NEWPROJECTFINISH", projectImag);
            ImageView iv = (ImageView) findViewById(R.id.imgProjectPreview);
            iv.setImageURI(Tools.getImageContentUri(this, new File(projectImag)));
        }

    }
}
