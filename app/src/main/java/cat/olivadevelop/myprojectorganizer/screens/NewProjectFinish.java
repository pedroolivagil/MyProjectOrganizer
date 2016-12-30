package cat.olivadevelop.myprojectorganizer.screens;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.managers.Project;
import cat.olivadevelop.myprojectorganizer.managers.ProjectManager;
import cat.olivadevelop.myprojectorganizer.tools.CustomCheckBox;
import cat.olivadevelop.myprojectorganizer.tools.CustomEditText;
import cat.olivadevelop.myprojectorganizer.tools.CustomTextView;
import cat.olivadevelop.myprojectorganizer.tools.GenericScreen;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_targets_id_tarjeta;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_targets_label;
import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.json_project_targets_value;

public class NewProjectFinish extends GenericScreen implements View.OnClickListener {

    static ScrollView scrollview;
    private Project project;
    private String projectName;
    private String projectHeaderImag;
    private String projectBodyImag;
    private List<String> listFileString;
    private LinearLayout btnAddField;
    private LinearLayout fieldsContainer;
    private int countFields;
    private int LIMIT_EDTEXT = 255;
    private HashMap<String, String> mapDescriptions;
    private List<String> listStringFilesBase64;
    private List<String> listStringFilesNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project_finish);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        scrollview = ((ScrollView) findViewById(R.id.scroll_project_finish));

        project = new Project();
        projectName = getIntent().getStringExtra(ProjectManager.PROJECT_NAME);
        listFileString = new ArrayList<String>();
        mapDescriptions = new HashMap<String, String>();
        if (getIntent().getStringExtra(ProjectManager.PROJECT_IMG) != null) {
            projectHeaderImag = getIntent().getStringExtra(ProjectManager.PROJECT_IMG);
        } else {
            projectHeaderImag = null;
        }
        if (getIntent().getStringExtra(ProjectManager.PROJECT_IMG_DESCRIPTION) != null) {
            Log.e(Tools.tagLogger(this), getIntent().getStringExtra(ProjectManager.PROJECT_IMG_DESCRIPTION));
            //mapDescriptions = getIntent().getStringExtra(ProjectManager.PROJECT_IMG_DESCRIPTION);
            mapDescriptions = Tools.convertToHashMap(getIntent().getStringExtra(ProjectManager.PROJECT_IMG_DESCRIPTION));
        }
        if (getIntent().getStringExtra(ProjectManager.PROJECT_IMG_BODY) != null) {
            projectBodyImag = getIntent().getStringExtra(ProjectManager.PROJECT_IMG_BODY);
            for (String str : projectBodyImag.split(",")) {
                listFileString.add(str.replace("[", "").replace("]", "").trim());
            }
        } else {
            projectBodyImag = null;
        }
        this.listStringFilesBase64 = new ArrayList<String>();
        this.listStringFilesNames = new ArrayList<String>();

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

        project.setName(projectName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_last_step, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_publish_project) {

            LinearLayout ly = (LinearLayout) findViewById(R.id.fieldsContainer); // main container
            LinearLayout lyChild = (LinearLayout) ly.getChildAt(1); // container cells
            LinearLayout lyChild2 = (LinearLayout) lyChild.getChildAt(0); // container cells

            CustomEditText label; // label
            CustomEditText value = (CustomEditText) lyChild2.getChildAt(1); // value

            // creamos el proyecto
            project.setFlagFinished(((CustomCheckBox) findViewById(R.id.isFinished)).isChecked());
            project.setHomeImage(ProjectManager.DEFAULT_PROJECT_IMG_NAME);
            project.setDescription(value.getText().toString());
            project.setFlagActivo(true);
            project.setIdProject(Tools.generateID(20));

            JSONArray tarjetas = new JSONArray();
            for (int x = 2; x <= countFields; x++) {
                lyChild = (LinearLayout) ly.getChildAt(x); // container cells
                lyChild2 = (LinearLayout) lyChild.getChildAt(0); // container cells

                label = (CustomEditText) lyChild2.getChildAt(0); // label
                value = (CustomEditText) lyChild2.getChildAt(1); // value

                // si ninguno de las dos celdas estan vacías agregamos los valores al hashmap
                if (!(label.getText().toString().equals("") && value.getText().toString().equals(""))) {
                    try {
                        JSONObject tarjeta = new JSONObject();
                        tarjeta.put(json_project_targets_id_tarjeta, Tools.generateID(20));
                        tarjeta.put(json_project_targets_label, label.getText().toString());
                        tarjeta.put(json_project_targets_value, value.getText().toString());
                        tarjetas.put(tarjeta);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            JSONArray imagenes = new JSONArray();
            try {
                if (listFileString != null) {
                    JSONObject jsnImages;
                    for (String str : listFileString) {
                        jsnImages = new JSONObject();
                        File currentFile = new File(str);
                        Bitmap b = BitmapFactory.decodeFile(str.trim());
                        jsnImages.put(ProjectManager.json_project_images_id_imagen,Tools.generateID(20));
                        jsnImages.put(ProjectManager.json_project_images_url, currentFile.getName().trim());
                        jsnImages.put(ProjectManager.json_project_images_width, b.getWidth());
                        jsnImages.put(ProjectManager.json_project_images_height, b.getHeight());
                        if (this.mapDescriptions != null && this.mapDescriptions.size() > 0) {
                            if (mapDescriptions.get(currentFile.getName().trim()) != null) {
                                jsnImages.put(ProjectManager.json_project_images_descript, mapDescriptions.get(currentFile.getName().trim()));
                            } else {
                                jsnImages.put(ProjectManager.json_project_images_descript, "");
                            }
                        } else {
                            jsnImages.put(ProjectManager.json_project_images_descript, "");
                        }
                        imagenes.put(jsnImages);
                        listStringFilesBase64.add(Tools.getImageBase64(str.replace("\\", "").trim()));
                        listStringFilesNames.add(currentFile.getName().trim());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            project.setTarjetas(tarjetas);
            project.setProjectImages(imagenes);
            project.setUserRoot(Tools.generateID());

            if (checkAllFields()) {
                try {
                    ProjectManager.create(this, this.project, this.projectHeaderImag, this.listStringFilesBase64, this.listStringFilesNames);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Tools.newSnackBarWithIcon(getWindow().getCurrentFocus(), this, R.string.complete_fields, R.drawable.ic_warning_white_24dp).show();
            }
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
            //label.setPadding(0, 10, 0, 10);
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
        //value.setPadding(10, 20, 10, 20);
        if (!labelText) {
            value.setMaxLength(350);
        }

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
        int errors = 0;
        LinearLayout ly = (LinearLayout) findViewById(R.id.fieldsContainer); // main container
        LinearLayout lyChild = (LinearLayout) ly.getChildAt(1); // container cells
        lyChild = (LinearLayout) lyChild.getChildAt(0); // container cells

        CustomEditText label = (CustomEditText) lyChild.getChildAt(0); // label
        CustomEditText value = (CustomEditText) lyChild.getChildAt(1); // value

        if (!value.getText().toString().equals("") && label.getText().toString().equals("")) {
            errors++;
        }
        if (value.getText().toString().equals("") && !label.getText().toString().equals("")) {
            errors++;
        }
        for (int x = 2; x <= countFields; x++) {
            lyChild = (LinearLayout) ly.getChildAt(x); // container cells
            lyChild = (LinearLayout) lyChild.getChildAt(0); // container cells

            label = (CustomEditText) lyChild.getChildAt(0); // label
            value = (CustomEditText) lyChild.getChildAt(1); // value

            if (!value.getText().toString().equals("") && label.getText().toString().equals("")) {
                errors++;
            }
            if (value.getText().toString().equals("") && !label.getText().toString().equals("")) {
                errors++;
            }
        }
        return errors == 0;
    }
}
