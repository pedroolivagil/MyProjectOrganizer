package cat.olivadevelop.myprojectorganizer.tools;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.managers.ProjectManager;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

/**
 * Created by Oliva on 26/09/2016.
 */
public class Tools {

    public static final String HOSTNAME = "http://projects.codeduo.cat";
    public static final String EXTERNAL_DIR = Environment.getExternalStorageDirectory() + "/MyProjectPictures/";
    public static final String PREFS_USER_ID = "id_user";
    public static final String PREFS_USER_EMAIL = "email";

    public static final String FONT_DEFAULT = "fonts/Ubuntu-Regular.ttf";
    public static final String FONT_BOLD = "fonts/Ubuntu-Bold.ttf";
    public static final String FONT_ITALIC = "fonts/Ubuntu-Italic.ttf";
    public static final String FONT_BOLD_ITALIC = "fonts/Ubuntu-BoldItalic.ttf";

    private static final String PREFS_NAME = "prefs_organizer";
    private static final String PREFS_IMG_URL_ARRAY_SIZE = "urlImgArray_size";
    private static final String PREFS_IMG_URL_ARRAY = "urlImgArray";
    private static final String PREFS_TITLE_PROJECT_ARRAY_SIZE = "mainTitleProject_size";
    private static final String PREFS_TITLE_PROJECT_ARRAY = "mainTitleProject";
    private static final String PREFS_DATE_PROJECT_ARRAY_SIZE = "mainDateProject_size";
    private static final String PREFS_DATE_PROJECT_ARRAY = "mainDateProject";
    private static final String PREFS_DESCRIPT_PROJECT_ARRAY_SIZE = "mainDescriptProject_size";
    private static final String PREFS_DESCRIPT_PROJECT_ARRAY = "mainDescriptProject";
    private static final String PREFS_IDS_PROJECT_ARRAY_SIZE = "mainIdsProject_size";
    private static final String PREFS_IDS_PROJECT_ARRAY = "mainIdsProject";

    private static final String CRYPT_KEY = "myprojectorganizerolivadevelop";
    private static final String PICTURE_PATH = "picture_path";
    private static final String FALSE = "false";
    private static SharedPreferences prefs;
    private static String currentBooleanValue;

    public static void init(Context c) {
        prefs = c.getSharedPreferences(Tools.PREFS_NAME, Context.MODE_PRIVATE);
        ProjectManager.setDefaultPrefs();
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

    public static SharedPreferences.Editor putInPrefs() {
        return prefs.edit();
    }

    public static String getUserID() {
        return Tools.getPrefs().getString(Tools.PREFS_USER_ID, "");
    }

    public static String getUserEmail() {
        return Tools.getPrefs().getString(Tools.PREFS_USER_EMAIL, "");
    }

    public static String getPicturePath() {
        return Tools.getPrefs().getString(Tools.PICTURE_PATH, null);
    }

    public static void setPicturePath(String picturePath) {
        Tools.putInPrefs().putString(Tools.PICTURE_PATH, picturePath).apply();
    }

    public static Bitmap getResizedBitmap(Bitmap bm, float scaleXY) {
        if (bm != null) {
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
        return null;
    }

    public static Bitmap checkResizedBitmap(Bitmap b) {
        Log.i("IMAGE SIZES", "H->" + b.getHeight() + "; W->" + b.getWidth());
        while (b.getHeight() > 4096 || b.getWidth() > 4096) {
            b = Tools.getResizedBitmap(b, .9f);
        }
        Log.i("IMAGE SIZES", "H->" + b.getHeight() + "; W->" + b.getWidth());
        return b;
    }

    public static boolean checkBitmapSize(Bitmap b, int limit) {
        if (b == null) {
            return false;
        }
        return (b.getHeight() > limit || b.getWidth() > limit);
    }

    public static CharSequence capitalize(String string) {
        StringBuilder st = new StringBuilder(string);
        return st.substring(0, 1).toUpperCase().concat(st.substring(1));
    }

    public static int getDP(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static float getPX(Context context, float dimension) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return dimension / scaledDensity;
    }

    public static boolean isBooleanValue(String valueStr) {
        /** Revisar la funcion para que solo devuelva true en caso de que pueda convertirlo */
        if ("1".equalsIgnoreCase(valueStr)
                || "0".equalsIgnoreCase(valueStr)
                || "true".equalsIgnoreCase(valueStr)
                || "false".equalsIgnoreCase(valueStr)) {
            Tools.setCurrentBooleanValue(valueStr);
            return true;
        } else {
            return false;
        }
    }

    public static String getCurrentBooleanValueAsString(Context c) {
        String result;
        if (Tools.currentBooleanValue.equalsIgnoreCase(FALSE)) {
            result = c.getString(R.string.falseValue);
        } else {
            result = c.getString(R.string.trueValue);
        }
        return result;
    }

    public static void setCurrentBooleanValue(String currentBooleanValue) {
        Tools.currentBooleanValue = currentBooleanValue;
    }


    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (SDK_INT >= LOLLIPOP) {
            Network[] redes = connec.getAllNetworks();
            for (Network red : redes) {
                NetworkInfo subRed = connec.getNetworkInfo(red);
                // ¿Tenemos conexión? ponemos a true
                if (subRed.getState() == NetworkInfo.State.CONNECTED) {
                    bConectado = true;
                }
            }
        } else {
            NetworkInfo[] redes = connec.getAllNetworkInfo();
            for (NetworkInfo red : redes) {
                // ¿Tenemos conexión? ponemos a true
                if (red.getState() == NetworkInfo.State.CONNECTED) {
                    bConectado = true;
                }
            }
        }
        return bConectado;
    }
}
