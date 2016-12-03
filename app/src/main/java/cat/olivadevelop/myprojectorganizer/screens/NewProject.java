package cat.olivadevelop.myprojectorganizer.screens;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.adapters.GridViewAdapter;
import cat.olivadevelop.myprojectorganizer.managers.ProjectManager;
import cat.olivadevelop.myprojectorganizer.tools.CustomEditText;
import cat.olivadevelop.myprojectorganizer.tools.PermisionsActivity;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

import static cat.olivadevelop.myprojectorganizer.managers.ProjectManager.MAX_IMAGES_PROJECT;

public class NewProject extends PermisionsActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;
    private String headerFilename;
    private String bodyFilename;
    private List<File> adapterFilenames;
    private CustomEditText editPjctName;
    private CardView btnPicToGrid;
    private AlertDialog alertHeader;
    private AlertDialog alertBody;
    private AlertDialog alertSiNo;
    private LinearLayout btnSelectHeaderTakePicture;
    private GridView gvImgBody;
    private boolean option;
    private File selectedHeaderImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        File file = new File(Tools.EXTERNAL_DIR);
        if (havePermissionStorage()) {
            file.mkdirs();
        }

        if (adapterFilenames == null) {
            adapterFilenames = new ArrayList<File>();
        }

        gvImgBody = (GridView) findViewById(R.id.gvImgBody);
        gvImgBody.setAdapter(new GridViewAdapter(this, adapterFilenames));

        btnSelectHeaderTakePicture = (LinearLayout) findViewById(R.id.btnSelectHeaderTakePicture);
        btnSelectHeaderTakePicture.setOnClickListener(this);

        editPjctName = (CustomEditText) findViewById(R.id.edtProjectName);
        alertHeader = dialogHeaderImg();
        alertBody = dialogBodyImg();

        btnPicToGrid = (CardView) findViewById(R.id.addPicToGrid);
        btnPicToGrid.setOnClickListener(this);
    }

    public AlertDialog dialogHeaderImg() {
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_take_select_picture, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        CardView cancel = (CardView) view.findViewById(R.id.action_cancel_dialog);
        CardView accept = (CardView) view.findViewById(R.id.action_accept_dialog);
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissHeader();
                    }
                }
        );
        accept.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectOrTakeHeader(view);
                    }
                }
        );
        return builder.create();
    }

    public AlertDialog dialogBodyImg() {
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_take_select_picture, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        CardView cancel = (CardView) view.findViewById(R.id.action_cancel_dialog);
        CardView accept = (CardView) view.findViewById(R.id.action_accept_dialog);
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissBody();
                    }
                }
        );
        accept.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectOrTakeBody(view);
                    }
                }
        );
        return builder.create();
    }

    private void selectOrTakeHeader(View view) {
        option = true;
        Intent intent = null;
        int code = 0;
        RadioGroup select = (RadioGroup) view.findViewById(R.id.takeSelectPicture);
        // order by
        switch (select.getCheckedRadioButtonId()) {
            default:
            case R.id.checkboxTakePicture:
                if (havePermissionCamera() && havePermissionStorage()) {
                    headerFilename = Tools.EXTERNAL_DIR + "home" + Tools.generateID() + ".jpg";
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri output = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(headerFilename));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
                    code = TAKE_PICTURE;
                    insertMedia(headerFilename);
                } else {
                    Tools.newSnackBarWithIcon(getWindow().getCurrentFocus(), this, R.string.fail_perms_camera_storage, R.drawable.ic_warning_white_24dp).show();
                    setPermissionCamera();
                    setPermissionStorage();
                }
                break;
            case R.id.checkboxSelectPicture:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                code = SELECT_PICTURE;
                break;
        }
        if (intent != null && code != 0) {
            dismissHeader();
            startActivityForResult(intent, code);
        }
    }

    private void selectOrTakeBody(View view) {
        option = false;
        Intent intent = null;
        int code = 0;
        RadioGroup select = (RadioGroup) view.findViewById(R.id.takeSelectPicture);
        // order by
        switch (select.getCheckedRadioButtonId()) {
            default:
            case R.id.checkboxTakePicture:
                if (havePermissionCamera() && havePermissionStorage()) {
                    bodyFilename = Tools.EXTERNAL_DIR + "body" + Tools.generateID() + ".jpg";
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri output = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(bodyFilename));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
                    code = TAKE_PICTURE;
                    insertMedia(bodyFilename);
                } else {
                    Tools.newSnackBarWithIcon(getWindow().getCurrentFocus(), this, R.string.fail_perms_camera_storage, R.drawable.ic_warning_white_24dp).show();
                    setPermissionCamera();
                    setPermissionStorage();
                }
                break;
            case R.id.checkboxSelectPicture:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                code = SELECT_PICTURE;
                break;
        }
        if (intent != null && code != 0) {
            dismissBody();
            startActivityForResult(intent, code);
        }
    }

    private void dismissHeader() {
        if (alertHeader != null) {
            alertHeader.dismiss();
        }
    }

    private void dismissBody() {
        if (alertBody != null) {
            alertBody.dismiss();
        }
    }

    private void showAlertHeader() {
        if (alertHeader != null) {
            alertHeader.show();
        }
    }

    private void showAlertBody() {
        if (adapterFilenames != null && adapterFilenames.size() < MAX_IMAGES_PROJECT) {
            if (alertBody != null) {
                alertBody.show();
            }
        } else {
            Tools.newSnackBarWithIcon(getWindow().getCurrentFocus(), this, R.string.fail_max_pictures_project, R.drawable.ic_warning_white_24dp).show();
        }
    }

    private void insertMedia(final String file) {
        new MediaScannerConnection.MediaScannerConnectionClient() {
            private MediaScannerConnection msc = null;

            {
                msc = new MediaScannerConnection(getApplicationContext(), this);
                msc.connect();
            }

            public void onMediaScannerConnected() {
                msc.scanFile(file, null);
            }

            public void onScanCompleted(String path, Uri uri) {
                msc.disconnect();
            }
        };
    }

    @Override
    public void onClick(View view) {
        if (view == btnSelectHeaderTakePicture) {
            showAlertHeader();
        }
        if (view == btnPicToGrid) {
            showAlertBody();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_project, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_next_step) {
            if (allValid()) {
                //procedure to next step
                Intent nextStep = new Intent(this, NewProjectFinish.class);
                nextStep.putExtra(ProjectManager.PROJECT_NAME, editPjctName.getText().toString().trim());
                if (selectedHeaderImage != null) {
                    nextStep.putExtra(ProjectManager.PROJECT_IMG, selectedHeaderImage.toString());
                }
                if (adapterFilenames != null) {
                    nextStep.putExtra(ProjectManager.PROJECT_IMG_BODY, adapterFilenames.toString());
                }
                startActivity(nextStep);
            } else {
                Tools.newSnackBarWithIcon(getWindow().getCurrentFocus(), this, R.string.fail_pjt_name, R.drawable.ic_warning_white_24dp).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean allValid() {
        int problems = 0;
        if (editPjctName.getText().toString().trim().equals("")) {
            problems++;
        }
        if (editPjctName.getText().toString().trim().length() < 5) {
            problems++;
        }
        return problems == 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 0) {
            if (option) { // imagen de header
                selectedHeaderImage = null;
                btnSelectHeaderTakePicture.removeAllViewsInLayout();
                LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                ImageView iv = new ImageView(this);
                iv.setLayoutParams(ivParams);
                btnSelectHeaderTakePicture.addView(iv);

                if (requestCode == TAKE_PICTURE) { // si capturamos una foto
                    if (headerFilename != null) {
                        selectedHeaderImage = new File(headerFilename);
                    }
                } else if (requestCode == SELECT_PICTURE) {
                    if (data != null) {
                        selectedHeaderImage = new File(Tools.getRealPathFromURI(this, data.getData()));
                    }
                }

                Tools.picassoImage(this, selectedHeaderImage, iv);
            } else { // montar el gridview
                File selectedImage = null;
                if (requestCode == TAKE_PICTURE) { // si capturamos una foto
                    if (bodyFilename != null) {
                        selectedImage = new File(bodyFilename);
                    }
                } else if (requestCode == SELECT_PICTURE) {
                    if (data != null) {
                        selectedImage = new File(Tools.getRealPathFromURI(this, data.getData()));
                    }
                }
                if (selectedImage != null) {
                    adapterFilenames.add(selectedImage);
                    Log.e(Tools.tagLogger(this), "Current imageName -> " + selectedImage.getName());
                    //Log.e(Tools.tagLogger(this), "Current imagePath -> " + selectedImage.getPath());
                }
                setGrid();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        alertSiNo = getAlertSiNo(position);
        alertSiNo.show();
    }

    private void setGrid() {
        gvImgBody.setAdapter(new GridViewAdapter(this, adapterFilenames));
        gvImgBody.setOnItemClickListener(this);
    }

    public AlertDialog getAlertSiNo(final int idPhoto) {
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_sino, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        CardView cancel = (CardView) view.findViewById(R.id.action_no_dialog);
        CardView accept = (CardView) view.findViewById(R.id.action_si_dialog);
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissSiNo();
                    }
                }
        );
        accept.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapterFilenames.remove(idPhoto);
                        dismissSiNo();
                        setGrid();
                    }
                }
        );
        return builder.create();
    }

    private void dismissSiNo() {
        if (alertSiNo != null) {
            alertSiNo.dismiss();
        }
    }
}
