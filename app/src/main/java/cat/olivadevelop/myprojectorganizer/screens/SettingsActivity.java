package cat.olivadevelop.myprojectorganizer.screens;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.tools.ProjectManager;
import cat.olivadevelop.myprojectorganizer.tools.Tools;
import cat.olivadevelop.myprojectorganizer.tools.UploadToServer;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private int REQUEST_CODE = 1;
    private TextView putEmailInPrefs;
    private LinearLayout changeEmail;
    private TextView optionCleanPrefs;
    private TextView optionChangeAccount;
    private LinearLayout optionDelAllProjects;
    private LinearLayout optionSortBy;
    private TextView resultOrderBy;
    private AlertDialog alert;
    private RadioGroup sortBy;
    private RadioGroup typeSortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        putEmailInPrefs = (TextView) findViewById(R.id.putEmailInPrefs);
        changeEmail = (LinearLayout) findViewById(R.id.changeEmail);
        changeEmail.setOnClickListener(this);
        resultOrderBy = (TextView) findViewById(R.id.resultOrderBy);
        resultOrderBy.setOnClickListener(this);
        optionCleanPrefs = (TextView) findViewById(R.id.optionCleanPrefs);
        optionCleanPrefs.setOnClickListener(this);
        optionChangeAccount = (TextView) findViewById(R.id.optionChangeAccount);
        optionChangeAccount.setOnClickListener(this);
        optionDelAllProjects = (LinearLayout) findViewById(R.id.optionDelAllProjects);
        optionDelAllProjects.setOnClickListener(this);
        optionSortBy = (LinearLayout) findViewById(R.id.optionSortBy);
        optionSortBy.setOnClickListener(this);

        if (Tools.getPrefs().getString(Tools.PREFS_USER_EMAIL, null) == null) {
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
        if (v == optionCleanPrefs) { // desactivado
        }
    }

    private void showAlert() {
        if (alert != null) {
            alert.show();
            // order by
            RadioButton c1 = (RadioButton) alert.findViewById(R.id.checkboxSortByIdPrjct);
            RadioButton c2 = (RadioButton) alert.findViewById(R.id.checkboxSortByLatUpdt);
            RadioButton c3 = (RadioButton) alert.findViewById(R.id.checkboxSortByPrjctName);
            if ((c1 != null) && (c2 != null) && (c3 != null)) {
                if (ProjectManager.getSortBy().equals(ProjectManager.json_project_id_project)) {
                    c1.setChecked(true);
                } else if (ProjectManager.getSortBy().equals(ProjectManager.json_project_last_update)) {
                    c2.setChecked(true);
                } else if (ProjectManager.getSortBy().equals(ProjectManager.json_project_last_update)) {
                    c3.setChecked(true);
                } else {
                    c1.setChecked(true);
                }
            }

            // typeorder by
            RadioButton c4 = (RadioButton) alert.findViewById(R.id.checkboxTypeSortByASC);
            RadioButton c5 = (RadioButton) alert.findViewById(R.id.checkboxTypeSortByDESC);
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
        Tools.putInPrefs().putString(Tools.PREFS_USER_EMAIL, putEmailInPrefs.getText().toString()).apply();
        Tools.putInPrefs().putString(Tools.PREFS_USER_ID, Tools.encrypt(putEmailInPrefs.getText().toString())).apply();
        msgSuccess();
        new UploadToServer(this).execute();
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
                Log.i("ORDER", getString(R.string.sortByIdProject));
                break;
            case R.id.checkboxSortByLatUpdt:
                ProjectManager.setSortBy(ProjectManager.json_project_last_update);
                Log.i("ORDER", getString(R.string.sortByLastUpdate));
                break;
            case R.id.checkboxSortByPrjctName:
                ProjectManager.setSortBy(ProjectManager.json_project_name);
                Log.i("ORDER", getString(R.string.sortByNameProject));
                break;
        }

        // type order by
        switch (typeSortBy.getCheckedRadioButtonId()) {
            case R.id.checkboxTypeSortByASC:
                ProjectManager.setTypeSortBy(ProjectManager.TYPE_SORT_BY_ASC);
                Log.i("ORDER", getString(R.string.typeOrderAscendant));
                break;
            default:
            case R.id.checkboxTypeSortByDESC:
                ProjectManager.setTypeSortBy(ProjectManager.TYPE_SORT_BY_DESC);
                Log.i("ORDER", getString(R.string.typeOrderDescendant));
                break;
        }
        alert.dismiss();
        msgSuccess();
    }

    public void msgSuccess(){
        Tools.newSnackBarWithIcon(findViewById(R.id.activity_settings), this, R.string.settings_updated, R.drawable.ic_info_white_24dp).show();
    }
}
