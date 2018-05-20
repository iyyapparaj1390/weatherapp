package customfont;

/**
 * Created by samsung on 4/14/2018.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by samsung on 5/20/2018.
 */


public class Customfontregular extends TextView {

    public Customfontregular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Customfontregular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Customfontregular(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Lato-Regular.ttf");
        setTypeface(tf ,1);

    }

}

