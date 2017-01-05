package cat.olivadevelop.myprojectorganizer.tools;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.CheckBox;

import cat.olivadevelop.myprojectorganizer.R;

/**
 * Created by Oliva on 01/11/2016.
 */

public class CustomCheckBox extends CheckBox implements Custom {
    public CustomCheckBox(Context context) {
        super(context);
        init();
    }

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomCheckBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        setBackgroundResource(R.drawable.border_black);
        this.setTypeface(Typeface.createFromAsset(getContext().getAssets(), Tools.FONT_DEFAULT));
    }

    public void setBold() {
        this.setTypeface(Typeface.createFromAsset(getContext().getAssets(), Tools.FONT_BOLD));
    }
}
