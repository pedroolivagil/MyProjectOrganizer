package cat.olivadevelop.myprojectorganizer.screens;

import android.os.Bundle;
import android.view.WindowManager;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.tools.PermisionsActivity;

public class SignOnScreen extends PermisionsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_sign_on_screen);
        setTitle(getString(R.string.signon).concat(" ").concat(getString(R.string.in)).concat(" ").concat(getString(R.string.app)));

        if (!havePermissionStorage()) {
            setPermissionStorage();
        }
        if (!havePermissionCamera()) {
            setPermissionCamera();
        }
    }
}
