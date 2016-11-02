package cat.olivadevelop.myprojectorganizer.tools;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Oliva on 01/11/2016.
 */

public class GeneralActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    protected boolean active = true;
    protected float segundos = 1.8f;
    protected float splashTime = segundos * 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!havePermissionCamera()) {
                    //no tenemos permisos, los solicitamos
                    setPermissionCamera();
                }
                if (!havePermissionStorage()) {
                    //no tenemos permisos, los solicitamos
                    setPermissionStorage();
                }
            }
        }, 1500);
    }

    protected boolean havePermissionCamera() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    protected boolean havePermissionStorage() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    protected void setPermissionStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestForResultStoragePermission();
        } else {
            requestForResultStoragePermission();
        }
    }

    protected void setPermissionCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            requestForResultCameraPermission();
        } else {
            requestForResultCameraPermission();
        }
    }

    protected void requestForResultStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    protected void requestForResultCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                MY_PERMISSIONS_REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("PERMISSION STORAGE", "GRANTED");
                } else {
                    Log.e("PERMISSION STORAGE", "GRANTED FAIL");
                }
            }
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("PERMISSION CAMERA", "GRANTED");
                } else {
                    Log.e("PERMISSION CAMERA", "GRANTED FAIL");
                }
            }
        }
    }
}
