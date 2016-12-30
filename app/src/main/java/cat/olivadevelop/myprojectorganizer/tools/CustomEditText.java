package cat.olivadevelop.myprojectorganizer.tools;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.regex.Pattern;

import cat.olivadevelop.myprojectorganizer.R;

/**
 * Created by Oliva on 01/11/2016.
 */

public class CustomEditText extends EditText implements Custom {
    public CustomEditText(Context context) {
        super(context);
        init(255);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(255);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(255);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(255);
    }

    @Override
    public void init() {
        setBackgroundResource(R.drawable.border_black);
        setPadding(Tools.getDP(getContext(), 8f), Tools.getDP(getContext(), 8f), Tools.getDP(getContext(), 8f), Tools.getDP(getContext(), 8f));
    }

    public void init(int maxChars) {
        this.init();
        this.setTypeface(Typeface.createFromAsset(getContext().getAssets(), Tools.FONT_DEFAULT));
        setMaxLength(maxChars);
    }

    public void setBold() {
        this.setTypeface(Typeface.createFromAsset(getContext().getAssets(), Tools.FONT_BOLD));
    }

    public void setTextCapitalized(String text) {
        setText(Tools.capitalize(text));
    }

    public void setMaxLength(int maxChars) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; ++i) {
                    if (!Pattern.compile(Tools.PATTERN_EDIT_TEXT).matcher(String.valueOf(source.charAt(i))).matches()) {
                        return "";
                    }
                }

                return null;
            }
        };
        this.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(maxChars)});
    }

}
