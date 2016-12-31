package cat.olivadevelop.myprojectorganizer.tools;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.Spinner;

import cat.olivadevelop.myprojectorganizer.R;

/**
 * Created by Oliva on 30/12/2016.
 */

public class CustomSpinner extends Spinner implements Custom {
    public CustomSpinner(Context context) {
        super(context);
        init();
    }

    public CustomSpinner(Context context, int mode) {
        super(context, mode);
        init();
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode) {
        super(context, attrs, defStyleAttr, defStyleRes, mode);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, defStyleRes, mode, popupTheme);
        init();
    }

    @Override
    public void init() {
        setBackgroundResource(R.drawable.border_black);
        setPadding(0, getDP(getContext(), 8f), 0, getDP(getContext(), 8f));
    }

    private int getDP(Context context, float px) {
        return (int) (px * context.getResources().getDisplayMetrics().density);
    }
}
