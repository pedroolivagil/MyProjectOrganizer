package cat.olivadevelop.myprojectorganizer.tools;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import cat.olivadevelop.myprojectorganizer.R;

/**
 * Created by Oliva on 01/11/2016.
 */

public class CustomWebView extends WebView {
    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setText(String str) {
        setText(str, R.dimen.size16);
    }

    public void setText(String str, int sizeDP) {
        sizeDP = (int) getResources().getDimension(sizeDP);
        String htmlText = " %s ";
        String data;
        data = String.format(htmlText,
                "<!DOCTYPE html><html>" +
                        "<head>" +
                        "   <style type=\"text/css\">" + loadStyle() + "</style>" +
                        "</head>" +
                        "<body><p style='font-size:"+Tools.getPX(getContext(),sizeDP)+"px;'>" +
                        Tools.capitalize(str)
                        + "</p>" +
                        "</body></html>"
        );
        super.loadDataWithBaseURL(null, data, "text/html; charset=utf-8", "UTF-8", null);
    }

    private String loadStyle() {
        StringBuilder file = new StringBuilder();
        String linea;
        try {
            InputStream fraw = getResources().openRawResource(R.raw.style);
            BufferedReader brin = new BufferedReader(new InputStreamReader(fraw));
            do {
                linea = brin.readLine();
                if (linea != null) {
                    file.append(linea);
                }
            } while (linea != null);
            fraw.close();
        } catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde recurso raw");
        }
        return file.toString();
    }
}
