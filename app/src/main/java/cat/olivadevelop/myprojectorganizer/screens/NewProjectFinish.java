package cat.olivadevelop.myprojectorganizer.screens;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.managers.ProjectManager;
import cat.olivadevelop.myprojectorganizer.tools.CreateProject;
import cat.olivadevelop.myprojectorganizer.tools.CustomCheckBox;
import cat.olivadevelop.myprojectorganizer.tools.CustomEditText;
import cat.olivadevelop.myprojectorganizer.tools.CustomTextView;
import cat.olivadevelop.myprojectorganizer.tools.GenericScreen;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

public class NewProjectFinish extends GenericScreen implements View.OnClickListener {

    static ScrollView scrollview;
    private String projectName;
    private String projectHeaderImag;
    private String projectBodyImag;
    private List<String> listFileString;
    private LinearLayout btnAddField;
    private LinearLayout fieldsContainer;
    private int countFields;
    private int LIMIT_EDTEXT = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project_finish);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        scrollview = ((ScrollView) findViewById(R.id.scroll_project_finish));

        projectName = getIntent().getStringExtra(ProjectManager.PROJECT_NAME);
        listFileString = new ArrayList<String>();
        if (getIntent().getStringExtra(ProjectManager.PROJECT_IMG) != null) {
            projectHeaderImag = getIntent().getStringExtra(ProjectManager.PROJECT_IMG);
        } else {
            projectHeaderImag = null;
        }
        if (getIntent().getStringExtra(ProjectManager.PROJECT_IMG_BODY) != null) {
            projectBodyImag = getIntent().getStringExtra(ProjectManager.PROJECT_IMG_BODY);
            for (String str : projectBodyImag.split(",")) {
                listFileString.add(str.replace("[", "").replace("]", "").trim());
            }
        } else {
            projectBodyImag = null;
        }

        CustomTextView textView = (CustomTextView) findViewById(R.id.nameProjectPreview);
        textView.setText(projectName);

        ImageView iv = (ImageView) findViewById(R.id.imgProjectPreview);
        iv.setVisibility(View.GONE);

        // boton añadir campo
        btnAddField = (LinearLayout) findViewById(R.id.btnAddField);
        btnAddField.setOnClickListener(this);

        // recogemos el contenedor de celdas
        fieldsContainer = (LinearLayout) findViewById(R.id.fieldsContainer);
        // iniciamos el contador de celdas
        countFields = 0;
        generateNewField(-1, R.string.description, false);

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
            values.put("" + ProjectManager.FINISH_PJT, String.valueOf(((CustomCheckBox) findViewById(R.id.isFinished)).isChecked())); // checkbox finished
            //values.put(getString(R.string.label_description), ((CustomEditText) findViewById(R.id.projectDescript)).getText().toString()); // descript
            for (int x = 1; x <= countFields; x++) {
                LinearLayout ly = (LinearLayout) findViewById(R.id.fieldsContainer); // main container
                LinearLayout lyChild = (LinearLayout) ly.getChildAt(x); // container cells
                lyChild = (LinearLayout) lyChild.getChildAt(0); // container cells

                CustomEditText label = (CustomEditText) lyChild.getChildAt(0); // label
                CustomEditText value = (CustomEditText) lyChild.getChildAt(1); // value

                // si ninguno de las dos celdas estan vacías agregamos los valores al hashmap
                if (!(label.getText().toString().equals("") && value.getText().toString().equals(""))) {
                    values.put(label.getText().toString(), value.getText().toString());
                }
            }
            // button clicked
            //Log.i("Button create", "Clicked");
            new CreateProject(this, projectName, values, projectHeaderImag, listFileString).execute();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == btnAddField) {
            if (countFields >= 10) {
                Tools.newSnackBarWithIcon(getWindow().getCurrentFocus(), this, R.string.err_max_fields, R.drawable.ic_warning_white_24dp).show();
            } else {
                generateNewField(R.string.type_field, R.string.value_field, true);
                scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        }
    }

    private void generateNewField(int idStringLabel, int idStringValue, boolean labelText) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 50, 0, 0);

        LinearLayout lyExtern = new LinearLayout(this);
        lyExtern.setLayoutParams(layoutParams);
        lyExtern.setOrientation(LinearLayout.VERTICAL);
        lyExtern.setPadding(10, 10, 10, 10);
        lyExtern.setBackgroundResource(R.color.colorPrimaryAlpha);


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
        CustomEditText label = new CustomEditText(this);
        label.setLayoutParams(
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                )
        );
        label.setEllipsize(TextUtils.TruncateAt.END);
        label.setMaxLines(1);
        label.setGravity(Gravity.CENTER);
        label.setBackgroundResource(R.color.colorPrimaryAlpha);
        if (labelText) {
            label.setHint(getString(idStringLabel));
            label.setHintTextColor(getResources().getColor(R.color.white));
            label.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            label.setPadding(0, 10, 0, 10);
        } else {
            label.setVisibility(View.GONE);
            label.setText(ProjectManager.json_project_descript);
        }

        // Valor del nuevo campo
        CustomEditText value = new CustomEditText(this);
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

    private boolean checkAllFields() {
        /**
         * comprobamos que las celdas, label no estan vacía si su value esta rellenado.
         */
        for (int x = 0; x < countFields; x++) {

        }
        return true;
    }
}
