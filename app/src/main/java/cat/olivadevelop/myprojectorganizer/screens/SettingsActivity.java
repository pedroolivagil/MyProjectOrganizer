package cat.olivadevelop.myprojectorganizer.screens;

import android.accounts.AccountManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.managers.ProjectManager;
import cat.olivadevelop.myprojectorganizer.tools.CustomRadioButton;
import cat.olivadevelop.myprojectorganizer.tools.CustomTextView;
import cat.olivadevelop.myprojectorganizer.tools.GenericScreen;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

public class SettingsActivity extends GenericScreen implements View.OnClickListener {

    private int REQUEST_CODE = 1;
    private CustomTextView putEmailInPrefs;
    private LinearLayout changeEmail;
    private CustomTextView optionSeeAllPermissions;
    private CustomTextView optionChangeAccount;
    private LinearLayout optionDelAllProjects;
    private LinearLayout optionSortBy;
    private CustomTextView resultOrderBy;
    private AlertDialog alert;
    private RadioGroup sortBy;
    private RadioGroup typeSortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        putEmailInPrefs = (CustomTextView) findViewById(R.id.putEmailInPrefs);
        changeEmail = (LinearLayout) findViewById(R.id.changeEmail);
        changeEmail.setOnClickListener(this);
        resultOrderBy = (CustomTextView) findViewById(R.id.resultOrderBy);
        resultOrderBy.setOnClickListener(this);
        optionSeeAllPermissions = (CustomTextView) findViewById(R.id.optionSeeAllPermissions);
        optionSeeAllPermissions.setOnClickListener(this);
        optionChangeAccount = (CustomTextView) findViewById(R.id.optionChangeAccount);
        optionChangeAccount.setOnClickListener(this);
        optionDelAllProjects = (LinearLayout) findViewById(R.id.optionDelAllProjects);
        optionDelAllProjects.setOnClickListener(this);
        optionSortBy = (LinearLayout) findViewById(R.id.optionSortBy);
        optionSortBy.setOnClickListener(this);

        if (Tools.getPrefs().getString(Tools.PREFS_USER_ID, null) == null) {
            setEmail();
        } else {
            putEmailInPrefs.setText(Tools.getPrefs().getString(Tools.PREFS_USER_EMAIL, null));
        }
        alert = orderByDialog();
    }

    private void setEmail() {
        Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
        startActivityForResult(googlePicker, REQUEST_CODE);
    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            putEmailInPrefs.setText(accountName);
            saveEmail();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_save) {
            if (!putEmailInPrefs.getText().toString().equals("")) {
                saveEmail();
            } else {
                Tools.newSnackBarWithIcon(findViewById(R.id.activity_settings), this, R.string.notnull_field, R.drawable.ic_warning_white_24dp).show();
            }
            return true;
        } else if (id == R.id.action_clear_prefs) {
            Tools.putInPrefs().clear().apply();
            Tools.newSnackBarWithIcon(findViewById(R.id.activity_settings), this, R.string.prefs_clean, R.drawable.ic_warning_white_24dp).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == changeEmail || v == optionChangeAccount) {
            setEmail();
        }
        if (v == optionSortBy) {
            showAlert();
        }
        if (v == optionDelAllProjects) {
            Tools.newSnackBarWithIcon(findViewById(R.id.activity_settings), this, R.string.soon, R.drawable.ic_warning_white_24dp).show();
        }
        if (v == optionSeeAllPermissions) { // desactivado
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", this.getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    private void showAlert() {
        if (alert != null) {
            alert.show();
            // order by
            CustomRadioButton c1 = (CustomRadioButton) alert.findViewById(R.id.checkboxSortByIdPrjct);
            CustomRadioButton c2 = (CustomRadioButton) alert.findViewById(R.id.checkboxSortByLatUpdt);
            CustomRadioButton c3 = (CustomRadioButton) alert.findViewById(R.id.checkboxSortByPrjctName);
            if ((c1 != null) && (c2 != null) && (c3 != null)) {
                if (ProjectManager.getSortBy().equals(ProjectManager.json_project_id_project)) {
                    c1.setChecked(true);
                } else if (ProjectManager.getSortBy().equals(ProjectManager.json_project_last_update)) {
                    c2.setChecked(true);
                } else if (ProjectManager.getSortBy().equals(ProjectManager.json_project_name)) {
                    c3.setChecked(true);
                } else {
                    c1.setChecked(true);
                }
            }

            // typeorder by
            CustomRadioButton c4 = (CustomRadioButton) alert.findViewById(R.id.checkboxTypeSortByASC);
            CustomRadioButton c5 = (CustomRadioButton) alert.findViewById(R.id.checkboxTypeSortByDESC);
            if ((c4 != null) && (c5 != null)) {
                if (ProjectManager.getTypeSortBy()) {
                    c4.setChecked(true);
                } else if (ProjectManager.getTypeSortBy()) {
                    c5.setChecked(true);
                } else {
                    c5.setChecked(true);
                }
            }
        }
    }

    public void saveEmail() {
        Tools.putInPrefs().putString(Tools.PREFS_USER_ID, Tools.encrypt(putEmailInPrefs.getText().toString())).apply();
        msgSuccess();
        //new CreateNewProjectFile(this).execute();
    }

    public AlertDialog orderByDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_prefs_sortby, null);
        builder.setView(view);
        final CardView cancel = (CardView) view.findViewById(R.id.action_cancel_dialog);
        CardView accept = (CardView) view.findViewById(R.id.action_accept_dialog);
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                }
        );
        accept.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setOrderByPrefs(view);
                    }
                }

        );
        return builder.create();
    }

    private void dismiss() {
        alert.dismiss();
    }

    private void setOrderByPrefs(View v) {
        sortBy = (RadioGroup) v.findViewById(R.id.sortByGroup);
        typeSortBy = (RadioGroup) v.findViewById(R.id.typeSortByGroup);

        // order by
        switch (sortBy.getCheckedRadioButtonId()) {
            default:
            case R.id.checkboxSortByIdPrjct:
                ProjectManager.setSortBy(ProjectManager.json_project_id_project);
                //Log.i("ORDER", getString(R.string.sortByIdProject));
                break;
            case R.id.checkboxSortByLatUpdt:
                ProjectManager.setSortBy(ProjectManager.json_project_last_update);
                //Log.i("ORDER", getString(R.string.sortByLastUpdate));
                break;
            case R.id.checkboxSortByPrjctName:
                ProjectManager.setSortBy(ProjectManager.json_project_name);
                //Log.i("ORDER", getString(R.string.sortByNameProject));
                break;
        }

        // type order by
        switch (typeSortBy.getCheckedRadioButtonId()) {
            case R.id.checkboxTypeSortByASC:
                ProjectManager.setTypeSortBy(ProjectManager.TYPE_SORT_BY_ASC);
                //Log.i("ORDER", getString(R.string.typeOrderAscendant));
                break;
            default:
            case R.id.checkboxTypeSortByDESC:
                ProjectManager.setTypeSortBy(ProjectManager.TYPE_SORT_BY_DESC);
                //Log.i("ORDER", getString(R.string.typeOrderDescendant));
                break;
        }
        alert.dismiss();
        msgSuccess();
    }

    public void msgSuccess() {
        Tools.newSnackBarWithIcon(findViewById(R.id.activity_settings), this, R.string.settings_updated, R.drawable.ic_info_white_24dp).show();
    }
}
