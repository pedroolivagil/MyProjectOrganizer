package cat.olivadevelop.myprojectorganizer.tools;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import cat.olivadevelop.myprojectorganizer.R;

/**
 * Created by Oliva on 26/09/2016.
 */
public class Tools {

    private final static String CRYPT_KEY = "myprojectorganizerolivadevelop";
    private static final String PICTURE_PATH = "picture_path";
    private static SharedPreferences prefs;
    public final static String HOSTNAME = "http://projects.codeduo.cat";
    public final static String EXTERNAL_DIR = Environment.getExternalStorageDirectory() + "/MyProjectPictures/";
    public static final String PROJECTS_FILENAME = "projects.json";
    public final static String PREFS_NAME = "prefs_organizer";
    public final static String PREFS_USER_ID = "id_user";
    public final static String PREFS_USER_EMAIL = "email";
    public final static String PREFS_USER_URL = "url";
    public final static String PROJECT_NAME = "nameProject";
    public final static String PROJECT_IMG = "imageProject";
    private static String picturePath;

    public static void init(Context c) {
        prefs = c.getSharedPreferences(Tools.PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(String str) {
        String pass = CRYPT_KEY + "" + str;
        return getMD5(pass);
    }

    public static String generateID() {
        return UUID.randomUUID().toString();
    }

    public static Snackbar newSnackBar(View v, Context cnxt, int string) {
        return Snackbar.make(v, cnxt.getString(string), Snackbar.LENGTH_LONG);
    }

    public static Snackbar newSnackBarWithIcon(View v, Context cnxt, int string, int icon) {
        Snackbar snackbar = Snackbar.make(v, string, Snackbar.LENGTH_LONG);
        View snackbarLayout = snackbar.getView();
        TextView textView = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
        textView.setCompoundDrawablePadding(cnxt.getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));
        return snackbar;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {

            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static String getRealPathFromURI(Context mContext, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(mContext, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public static SharedPreferences getPrefs() {
        return prefs;
    }

    public static void setPrefs(SharedPreferences prefs) {
        Tools.prefs = prefs;
    }

    public static SharedPreferences.Editor putInPrefs() {
        return prefs.edit();
    }

    public static void cleanProjectPrefs() {
        Tools.putInPrefs().putString(Tools.PROJECT_NAME, "").apply();
        Tools.putInPrefs().putString(Tools.PROJECT_IMG, "").apply();
    }

    public static String getUserID() {
        return Tools.getPrefs().getString(Tools.PREFS_USER_ID, "");
    }

    public static String getUserEmail() {
        return Tools.getPrefs().getString(Tools.PREFS_USER_EMAIL, "");
    }

    public static void setPicturePath(String picturePath) {
        Tools.putInPrefs().putString(Tools.PICTURE_PATH, picturePath).apply();
    }

    public static String getPicturePath() {
        return Tools.getPrefs().getString(Tools.PICTURE_PATH, null);
    }

    public static Bitmap getResizedBitmap(Bitmap bm, float scaleXY) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        //matrix.postScale(scaleWidth, scaleHeight);
        matrix.postScale(scaleXY, scaleXY);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
