package cat.olivadevelop.myprojectorganizer.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.tools.GenericScreen;

public class WelcomeScreen extends GenericScreen implements View.OnClickListener {

    CardView signon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_welcome);
        setTitle("");

        signon = (CardView) findViewById(R.id.signon);
        signon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == signon){
            Intent sign = new Intent(this,SignOnScreen.class);
            startActivity(sign);
        }
    }
}
