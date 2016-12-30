package cat.olivadevelop.myprojectorganizer.tools;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by Oliva on 01/11/2016.
 */

public class CustomRadioButton extends RadioButton implements Custom {
    public CustomRadioButton(Context context) {
        super(context);
        init();
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        this.setTypeface(Typeface.createFromAsset(getContext().getAssets(), Tools.FONT_DEFAULT));
    }

    public void setBold() {
        this.setTypeface(Typeface.createFromAsset(getContext().getAssets(), Tools.FONT_BOLD));
    }
}
