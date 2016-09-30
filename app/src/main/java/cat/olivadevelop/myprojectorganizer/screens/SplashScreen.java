package cat.olivadevelop.myprojectorganizer.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cat.olivadevelop.myprojectorganizer.MainActivity;
import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

public class SplashScreen extends AppCompatActivity {

    protected boolean active = true;
    protected float segundos = 2.5f;
    protected float splashTime = segundos * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (active && (waited < splashTime)) {
                        sleep(100);
                        if (active) {
                            waited += 100;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                }
            }
        };
        splashThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tools.init(this);
    }
}
