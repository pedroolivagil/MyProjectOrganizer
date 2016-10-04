package cat.olivadevelop.myprojectorganizer.screens;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private ImageView image_thumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        lytBtnCamera = (LinearLayout) findViewById(R.id.btnCamera);
        lytBtnGallery = (LinearLayout) findViewById(R.id.btnGalery);
        image_thumb = (ImageView) findViewById(R.id.image_thumb);

        editPjctName = (EditText) findViewById(R.id.edtProjectName);
        if (Tools.getPrefs().getString(Tools.PROJECT_NAME, null) != null) {
            editPjctName.setText(Tools.getPrefs().getString(Tools.PROJECT_NAME, null));
        }

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
            Uri output = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(filename));
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
        getMenuInflater().inflate(R.menu.menu_new_project, menu);
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
                //procedure to next step
                Tools.putInPrefs().putString(Tools.PROJECT_NAME, editPjctName.getText().toString()).apply();
                if (filename != null) {
                    Tools.putInPrefs().putString(Tools.PROJECT_IMG, filename).apply();
                }

                Intent nextStep = new Intent(this, NewProjectFinish.class);
                startActivity(nextStep);
            } else {
                Tools.newSnackBarWithIcon(getWindow().getCurrentFocus(), this, R.string.fail_pjt_name, R.drawable.ic_warning_white_24dp).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImage = null;
        if (requestCode == TAKE_PICTURE) {
            ImageView iv = (ImageView) findViewById(R.id.image_thumb);
            //iv.setImageBitmap(BitmapFactory.decodeFile(filename));
            Bitmap b = BitmapFactory.decodeFile(filename);
            b = Tools.getResizedBitmap(b, .7f);
            iv.setImageBitmap(b);
            selectedImage = Tools.getImageContentUri(this, new File(filename));
        } else if (requestCode == SELECT_PICTURE) {
            if (data != null) {
                try {
                    selectedImage = data.getData();
                    InputStream is;
                    is = getContentResolver().openInputStream(selectedImage);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    Bitmap b = BitmapFactory.decodeStream(bis);
                    b = Tools.getResizedBitmap(b, .7f);
                    ImageView iv = (ImageView) findViewById(R.id.image_thumb);
                    iv.setImageBitmap(b);
                    filename = Tools.getRealPathFromURI(this, selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (selectedImage != null) {
            // Cursor to get image uri to display
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                Log.i("Cursor", "" + cursor.getString(columnIndex));
                Tools.setPicturePath(cursor.getString(columnIndex));
                cursor.close();
            }
        }
    }
}
