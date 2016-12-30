package cat.olivadevelop.myprojectorganizer.screens;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.concurrent.ExecutionException;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.managers.Pais;
import cat.olivadevelop.myprojectorganizer.managers.PaisManager;
import cat.olivadevelop.myprojectorganizer.tools.CustomSpinner;
import cat.olivadevelop.myprojectorganizer.tools.PermisionsActivity;

public class SignOnScreen extends PermisionsActivity {

    CustomSpinner spinner_paises;

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
    }
}
