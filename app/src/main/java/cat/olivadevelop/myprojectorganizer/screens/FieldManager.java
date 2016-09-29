package cat.olivadevelop.myprojectorganizer.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import cat.olivadevelop.myprojectorganizer.R;

public class FieldManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_manager);
        Log.i("FieldManagerScreen", "True");
    }
}
