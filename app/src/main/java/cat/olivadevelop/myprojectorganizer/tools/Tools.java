package cat.olivadevelop.myprojectorganizer.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import cat.olivadevelop.myprojectorganizer.R;
import cat.olivadevelop.myprojectorganizer.managers.ProjectManager;

/**
 * Created by Oliva on 26/09/2016.
 */
public class Tools {

    //public static final String HOSTNAME = "http://projects.codeduo.cat";
    public static final int REQUEST_CODE = 1;
    public static final int INFO_CODE = 1;
    public static final int WARN_CODE = 2;
    public static final int ERRO_CODE = 3;
    public static final String SERVER = "10.0.2.2";
    public static final String HOSTNAME = "http://" + SERVER + "/myprojectsorg";
    //public static final String HOSTNAME = "http://192.168.1.43/myprojectsorg";
    public static final String CLIENT_DIR = "clients";
    public static final String IMAGE_DIR = "img";
    public static final String EXTERNAL_DIR = Environment.getExternalStorageDirectory() + "/MyProjectPictures/";
    public static final String PREFS_USER_ID = "id_user";
    public static final String PREFS_USER_EMAIL = "email";

    public static final String FONT_DEFAULT = "fonts/Ubuntu-Regular.ttf";
    public static final String FONT_BOLD = "fonts/Ubuntu-Bold.ttf";
    public static final String FONT_ITALIC = "fonts/Ubuntu-Italic.ttf";
    public static final String FONT_BOLD_ITALIC = "fonts/Ubuntu-BoldItalic.ttf";
    public static final String PATTERN_EDIT_TEXT = "[ ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzáéíóúýàèìòùäëïöüÿâêîôûçñÁÉÍÓÚÝÀÈÌÒÙÄËÏÖÜŸÂÊÎÔÛÇÑ1234567890,;.:_/*-+!·$%&/()=?¿ºª|@#~€¬]*";
    private static final String PREFS_NAME = "prefs_organizer";
    private static final String CRYPT_KEY = "myprojectorganizerolivadevelop";
    private static final String FALSE = "false";
    private static SharedPreferences prefs;
    private static String currentBooleanValue;
    private static AlertDialog alertError;
    private static boolean projectListResult;

    public static void init(Context c) {
        prefs = c.getSharedPreferences(Tools.PREFS_NAME, Context.MODE_PRIVATE);
        ProjectManager.setDefaultPrefs();
        setProjectListResult(false);
    }

    public static void getDeviceEmails(Activity a) {
        Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
        a.startActivityForResult(googlePicker, REQUEST_CODE);
    }

    private static boolean isNetworkActive(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }

    public static Boolean isNetworkAvailable(Context c) {
        if (isNetworkActive(c)) {
            try {
                Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 " + SERVER);
                int val = p.waitFor();
                return (val == 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
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
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateID(int length) {
        return generateID().substring(0, length);
    }

    public static void newSnackBar(View v, String string) {
        Snackbar.make(v, string, Snackbar.LENGTH_LONG).show();
    }

    public static void newSnackBar(View v, Context cnxt, int string) {
        Snackbar.make(v, cnxt.getString(string), Snackbar.LENGTH_LONG).show();
    }

    public static void newSnackBarWithIcon(View v, Context cnxt, int string, int icon) {
        Snackbar snackbar = Snackbar.make(v, string, Snackbar.LENGTH_LONG);
        View snackbarLayout = snackbar.getView();
        TextView textView = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
        textView.setCompoundDrawablePadding(cnxt.getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));
        snackbar.show();
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

    public static Bitmap resizeBitmap(Bitmap b) {
        while (b.getHeight() > 4096 || b.getWidth() > 4096) {
            b = Tools.getResizedBitmap(b, .9f);
        }
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
        CharSequence retorno = "";
        if (string != null && string.length() > 0) {
            retorno = st.substring(0, 1).toUpperCase().concat(st.substring(1));
        }
        return retorno;
    }

    public static int getDP(Context context, float px) {
        return (int) (px * context.getResources().getDisplayMetrics().density);
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

    public static String getImageBase64(String url) {
        if (url != null) {
            Bitmap bm = Tools.resizeBitmap(BitmapFactory.decodeFile(url.trim()));
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
            byte[] ba = bao.toByteArray();
            //return Base64.encodeToString(ba, Base64.DEFAULT);
            return Tools.encode64(ba);
        } else {
            return null;
        }
    }

    public static String encode64(byte[] str) {
        return Base64.encodeToString(str, Base64.DEFAULT);
    }

    public static void picassoImage(Context c, File url, ImageView view) {
        Tools.picassoImage(c, url, view, new RoundedTransformationBuilder().build());
    }

    public static void picassoImage(Context c, String url, ImageView view) {
        Tools.picassoImage(c, url, view, new RoundedTransformationBuilder().build());
    }

    public static void picassoImage(Context c, String url, ImageView view, Transformation transformation) {
        Picasso.with(c).load(url).noFade().fit().transform(transformation).centerCrop()
                .placeholder(R.drawable.ic_camera_black_48dp)
                .error(R.drawable.ic_close_light)
                .into(view);
    }

    public static void picassoImage(Context c, File url, ImageView view, Transformation transformation) {
        Picasso.with(c).load(url).noFade().fit().transform(transformation).centerCrop()
                .placeholder(R.drawable.ic_camera_black_48dp)
                .error(R.drawable.ic_close_light)
                .into(view);
    }

    public static void picassoImageWithoutTransform(Context c, String url, ImageView view, float[] sizes) {
        float deseado = Tools.getDP(c, 400);
        Picasso.with(c).load(url).noFade().placeholder(R.drawable.ic_camera_black_48dp)
                .error(R.drawable.ic_close_light).resize((int) deseado, (int) Tools.resizeImg(sizes[0], sizes[1], deseado))
                .centerCrop().into(view);
    }

    public static String tagLogger(Context context) {
        return context.getClass().getSimpleName().toUpperCase();
    }

    private static float resizeImg(float anchoOriginal, float altoOriginal, float anchoDeseado) {
        return (anchoDeseado * altoOriginal) / anchoOriginal;
    }

    public static List<File> sortFileList(List<File> list, boolean typeSortByDesc) {
        List<File> newList = new ArrayList<>();
        if (list != null) {
            if (list.size() > 1) {
                if (typeSortByDesc) {
                    for (int z = 0; z < list.size(); z++) {
                        newList.add(list.get(z));
                    }
                } else {
                    for (int z = list.size(); z > 0; z--) {
                        newList.add(list.get(z - 1));
                    }
                }
            } else {
                newList = list;
            }
        }
        return newList;
    }

    public static HashMap<String, String> convertToHashMap(String stringExtra) {
        HashMap<String, String> map = new HashMap<String, String>();
        for (String current : stringExtra.replace("{", "").replace("}", "").split(",")) {
            String[] keyMap = current.trim().split("=");
            map.put(keyMap[0].trim(), keyMap[1].trim());
        }
        return map;
    }

    private static AlertDialog alertError(Activity a) {
        LayoutInflater inflater = a.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        builder.setView(view);

        CardView cancel = (CardView) view.findViewById(R.id.action_ok);
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertError.dismiss();
                    }
                }
        );
        return builder.create();
    }

    public static void showAlertError(Activity a) {
        alertError = Tools.alertError(a);
        alertError.show();
    }

    public static String getCurrentDate() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date());
    }

    public static boolean isprojectListResult() {
        return projectListResult;
    }

    public static void setProjectListResult(boolean projectListResult) {
        Tools.projectListResult = projectListResult;
    }

    public static boolean parseBoolean(String string) {
        return string.trim().equals("1");
    }

}
