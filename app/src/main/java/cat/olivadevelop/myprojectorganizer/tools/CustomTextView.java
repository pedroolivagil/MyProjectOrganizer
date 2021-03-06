package cat.olivadevelop.myprojectorganizer.tools;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Oliva on 31/10/2016.
 */

public class CustomTextView extends TextView implements Custom {
    public CustomTextView(Context context) {
        super(context);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        this.setTypeface(Typeface.createFromAsset(getContext().getAssets(), Tools.FONT_DEFAULT));
    }

    public void setBold() {
        this.setTypeface(Typeface.createFromAsset(getContext().getAssets(), Tools.FONT_BOLD));
    }

    public void setTextCapitalized(String text) {
        if (text.length() > 0) {
            setText(Tools.capitalize(text));
        } else {
            setText(text);
        }
    }
}
