package cat.olivadevelop.myprojectorganizer.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.tools.Tools;

public class NewProject extends AppCompatActivity implements View.OnClickListener {

    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;
    private String filename;
    private LinearLayout lytBtnCamera;
    private LinearLayout lytBtnGallery;
    private EditText editPjctName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        lytBtnCamera = (LinearLayout) findViewById(R.id.btnCamera);
        lytBtnGallery = (LinearLayout) findViewById(R.id.btnGalery);

        editPjctName = (EditText) findViewById(R.id.edtProjectName);

        File file = new File(Tools.EXTERNAL_DIR);
        file.mkdirs();

        lytBtnCamera.setOnClickListener(this);
        lytBtnGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int code = 0;
        Intent intent = null;
        if (view == lytBtnCamera) {
            filename = Tools.EXTERNAL_DIR + "home" + Tools.generateID() + ".jpg";
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri output = Uri.fromFile(new File(filename));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
            code = TAKE_PICTURE;
            new MediaScannerConnection.MediaScannerConnectionClient() {
                private MediaScannerConnection msc = null;

                {
                    msc = new MediaScannerConnection(getApplicationContext(), this);
                    msc.connect();
                }

                public void onMediaScannerConnected() {
                    msc.scanFile(filename, null);
                }

                public void onScanCompleted(String path, Uri uri) {
                    msc.disconnect();
                }
            };
        }
        if (view == lytBtnGallery) {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            code = SELECT_PICTURE;
        }
        startActivityForResult(intent, code);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {
            if (!editPjctName.getText().toString().equals("")) {

            } else {
                //.show();
                /*Snackbar snackbar = Snackbar.make(getWindow().getCurrentFocus(), R.string.fail_pjt_name, Snackbar.LENGTH_LONG);
                View snackbarLayout = snackbar.getView();
                TextView textView = (TextView)snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning_white_24dp, 0, 0, 0);
                textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));
                snackbar.show();*/
                Tools.newSnackBarWithIcon(
                        getWindow().getCurrentFocus(),
                        this,
                        R.string.fail_pjt_name,
                        R.drawable.ic_warning_white_24dp
                ).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE) {
            ImageView iv = (ImageView) findViewById(R.id.image_thumb);
            iv.setImageBitmap(BitmapFactory.decodeFile(filename));
        } else if (requestCode == SELECT_PICTURE) {
            try {
                Uri selectedImage = data.getData();
                InputStream is;
                is = getContentResolver().openInputStream(selectedImage);
                BufferedInputStream bis = new BufferedInputStream(is);
                Bitmap bitmap = BitmapFactory.decodeStream(bis);
                ImageView iv = (ImageView) findViewById(R.id.image_thumb);
                iv.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
            }
        }
    }
}