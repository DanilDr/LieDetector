package ru.wildleaf.liedetectorprank.app;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Данил on 20.06.2014.
 */
public class ACLineText extends TextView {

    public ACLineText(Context context) {
        this(context, null, 0);
    }

    public ACLineText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ACLineText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode() == false) {
            Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/ACLine.ttf");
            this.setTypeface(font);
        }
    }
}
