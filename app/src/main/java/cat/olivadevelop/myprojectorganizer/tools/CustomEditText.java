package cat.olivadevelop.myprojectorganizer.tools;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Oliva on 01/11/2016.
 */

public class CustomEditText extends EditText {
    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        this.setTypeface(Typeface.createFromAsset(getContext().getAssets(), Tools.FONT_DEFAULT));
    }

    public void setBold() {
        this.setTypeface(Typeface.createFromAsset(getContext().getAssets(), Tools.FONT_BOLD));
    }

    public void setTextCapitalized(String text){
        setText(Tools.capitalize(text));
    }
}
