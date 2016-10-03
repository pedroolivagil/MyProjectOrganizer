package cat.olivadevelop.myprojectorganizer.screens;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.tools.CreateProject;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

public class NewProjectFinish extends AppCompatActivity implements View.OnClickListener {

    private String projectName;
    private String projectImag;
    private LinearLayout btnAddField;
    private LinearLayout fieldsContainer;
    private int countFields;
    private int ID_LABEL_EXTRA = 10;
    private int LIMIT_EDTEXT = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project_finish);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //cabecera de proyecto
        projectName = Tools.getPrefs().getString(Tools.PROJECT_NAME, null);
        projectImag = Tools.getPrefs().getString(Tools.PROJECT_IMG, null);

        TextView textView = (TextView) findViewById(R.id.nameProjectPreview);
        textView.setText(projectName);

        if (projectImag != null) {
            ImageView iv = (ImageView) findViewById(R.id.imgProjectPreview);
            iv.setImageURI(Tools.getImageContentUri(this, new File(projectImag)));
        }

        // boton añadir campo
        btnAddField = (LinearLayout) findViewById(R.id.btnAddField);
        btnAddField.setOnClickListener(this);

        // recogemos el contenedor de celdas
        fieldsContainer = (LinearLayout) findViewById(R.id.fieldsContainer);
        // iniciamos el contador de celdas
        countFields = 0;
        generateNewField(R.id.projectDescript, R.string.label_description, R.string.description, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_last_step, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_publish_project) {
            HashMap<String, String> values = new HashMap<>();
            values.put("" + CreateProject.FINISH_PJT, String.valueOf(((CheckBox) findViewById(R.id.isFinished)).isChecked())); // checkbox finished
            //values.put(getString(R.string.label_description), ((EditText) findViewById(R.id.projectDescript)).getText().toString()); // descript
            for (int x = 1; x <= countFields; x++) {
                LinearLayout ly = (LinearLayout) findViewById(R.id.fieldsContainer); // main container
                LinearLayout lyChild = (LinearLayout) ly.getChildAt(x); // container cells
                lyChild = (LinearLayout) lyChild.getChildAt(0); // container cells

                EditText label = (EditText) lyChild.getChildAt(0); // label
                EditText value = (EditText) lyChild.getChildAt(1); // value

                // si ninguno de las dos celdas estan vacías agregamos los valores al hashmap
                if (!(label.getText().toString().equals("") && value.getText().toString().equals(""))) {
                    values.put(label.getText().toString(), value.getText().toString());
                }
            }
            // button clicked
            Log.i("Button create", "Clicked");
            new CreateProject(this, projectName, values).execute();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == btnAddField) {
            if (countFields >= 10) {
                Tools.newSnackBarWithIcon(getWindow().getCurrentFocus(), this, R.string.err_max_fields, R.drawable.ic_warning_white_24dp).show();
            } else {
                final ScrollView scrollview = ((ScrollView) findViewById(R.id.scroll_project_finish));
                generateNewField(countFields, R.string.type_field, R.string.value_field, true);
                scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        }
    }

    private void generateNewField(int id, int idStringLabel, int idStringValue, boolean labelText) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 50, 0, 0);

        LinearLayout lyExtern = new LinearLayout(this);
        lyExtern.setLayoutParams(layoutParams);
        lyExtern.setOrientation(LinearLayout.VERTICAL);
        lyExtern.setPadding(10, 10, 10, 10);
        lyExtern.setBackgroundResource(R.color.secondary_light);


        LinearLayout ly = new LinearLayout(this);
        ly.setLayoutParams(
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                )
        );
        ly.setOrientation(LinearLayout.VERTICAL);
        ly.setPadding(10, 10, 10, 10);
        ly.setBackgroundResource(R.color.white);

        // Label del nuevo campo
        EditText label = new EditText(this);
        label.setLayoutParams(
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                )
        );
        label.setEms(6);
        label.setEllipsize(TextUtils.TruncateAt.END);
        label.setMaxLines(1);
        label.setGravity(Gravity.CENTER);
        label.setBackgroundResource(R.color.secondary_light);
        label.setTypeface(null, Typeface.BOLD);
        if (labelText) {
            label.setHint(getString(idStringLabel));
        } else {
            label.setText(getString(idStringLabel));
            label.setEnabled(false);
            label.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        // Valor del nuevo campo
        EditText value = new EditText(this);
        value.setLayoutParams(
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                )
        );
        value.setMaxLines(5);
        value.setFilters(new InputFilter[]{new InputFilter.LengthFilter(LIMIT_EDTEXT)});
        value.setHint(getString(idStringValue));
        value.setBackgroundResource(R.color.white);
        value.setPadding(10, 20, 10, 20);

        // insertamos los editText en el linear
        ly.addView(label);
        ly.addView(value);

        lyExtern.addView(ly);

        fieldsContainer.addView(lyExtern);
        countFields++;
    }
}
