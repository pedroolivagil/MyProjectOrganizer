package cat.olivadevelop.myprojectorganizer.tools;

import android.content.Context;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

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
    public final static String EXTERNAL_DIR = Environment.getExternalStorageDirectory() + "/MyProjectPictures/";

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
}
