package cat.olivadevelop.myprojectorganizer.screens;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.concurrent.ExecutionException;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.managers.Pais;
import cat.olivadevelop.myprojectorganizer.managers.PaisManager;
import cat.olivadevelop.myprojectorganizer.tools.CustomEditText;
import cat.olivadevelop.myprojectorganizer.tools.CustomSpinner;
import cat.olivadevelop.myprojectorganizer.tools.PermisionsActivity;

public class SignOnScreen extends PermisionsActivity {

    CustomSpinner spinner_paises;
    CustomEditText new_user_email;
    CustomEditText new_user_pass;
    CustomEditText new_user_nif;
    CustomEditText new_user_phone;
    CustomEditText new_user_prov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_sign_on_screen);
        setTitle(getString(R.string.welcome_signon).concat(" ").concat(getString(R.string.new_user_in)).concat(" ").concat(getString(R.string.app)));

        if (!havePermissionStorage()) {
            setPermissionStorage();
        }
        if (!havePermissionCamera()) {
            setPermissionCamera();
        }

        spinner_paises = (CustomSpinner) findViewById(R.id.spinner_paises);

        try {
            List<Pais> list = PaisManager.downloadCountryList(this);
            ArrayAdapter spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
            spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_paises.setAdapter(spinner_adapter);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        new_user_email = (CustomEditText) findViewById(R.id.new_user_email);
        new_user_pass = (CustomEditText) findViewById(R.id.new_user_pass);
        new_user_nif = (CustomEditText) findViewById(R.id.new_user_nif);
        new_user_phone = (CustomEditText) findViewById(R.id.new_user_phone);
        new_user_prov = (CustomEditText) findViewById(R.id.new_user_prov);

        new_user_email.setMaxLength(255);
        new_user_pass.setMaxLength(32);
        new_user_nif.setMaxLength(12);
        new_user_phone.setMaxLength(15);
        new_user_prov.setMaxLength(80);
    }
}
