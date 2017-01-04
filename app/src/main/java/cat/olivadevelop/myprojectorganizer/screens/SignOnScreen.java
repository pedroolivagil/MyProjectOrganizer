package cat.olivadevelop.myprojectorganizer.screens;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.concurrent.ExecutionException;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.entities.Pais;
import cat.olivadevelop.myprojectorganizer.entities.User;
import cat.olivadevelop.myprojectorganizer.managers.PaisManager;
import cat.olivadevelop.myprojectorganizer.tools.CustomEditText;
import cat.olivadevelop.myprojectorganizer.tools.CustomSpinner;
import cat.olivadevelop.myprojectorganizer.tools.PermisionsActivity;

public class SignOnScreen extends PermisionsActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private CardView signupuser;
    private CustomSpinner spinner_paises;
    private CustomEditText new_user_email;
    private CustomEditText new_user_pass;
    private CustomEditText new_user_nif;
    private CustomEditText new_user_phone;
    private CustomEditText new_user_prov;
    private Pais currentPais;

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

        signupuser = (CardView) findViewById(R.id.signupuser);
        signupuser.setOnClickListener(this);
        new_user_email = (CustomEditText) findViewById(R.id.new_user_email);
        new_user_pass = (CustomEditText) findViewById(R.id.new_user_pass);
        new_user_nif = (CustomEditText) findViewById(R.id.new_user_nif);
        new_user_phone = (CustomEditText) findViewById(R.id.new_user_phone);
        new_user_prov = (CustomEditText) findViewById(R.id.new_user_prov);
        spinner_paises = (CustomSpinner) findViewById(R.id.spinner_paises);
        spinner_paises.setOnItemSelectedListener(this);

        new_user_email.setMaxLength(255);
        new_user_pass.setMaxLength(32);
        new_user_nif.setMaxLength(12);
        new_user_phone.setMaxLength(15);
        new_user_prov.setMaxLength(80);
        try {
            List<Pais> list = PaisManager.downloadCountryList(this);
            ArrayAdapter spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
            spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_paises.setAdapter(spinner_adapter);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == signupuser) {
            signOnDB();
        }
    }

    private void signOnDB() {
        if (new_user_email != null && new_user_pass != null) {
            User user = new User();
            user.setCorreo(new_user_email.getText().toString());
            user.setEncryptedPassword(new_user_pass.getText().toString());
            user.setEncryptedID(new_user_email.getText().toString());
            user.setNif(new_user_nif.getText().toString());
            user.setPhone(new_user_phone.getText().toString());
            user.setPoblacion(new_user_prov.getText().toString());
            user.setPais(getCurrentPais());
            //UserManager.create(this, user);
            Log.e("USER", "" + user.toString());
        } else {
            // mensaje email y password obligatorios
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // acabar de establecer el idioma al seleccionar
        currentPais = ((Pais) parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public Pais getCurrentPais() {
        if (currentPais == null) {
            currentPais = new Pais(0, "", "");
        }
        return currentPais;
    }
}
