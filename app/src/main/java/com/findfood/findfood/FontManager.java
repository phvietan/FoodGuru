package com.findfood.findfood;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by longa_000 on 5/8/2016.
 */
public class FontManager {

    public static final String ROOT = "fonts/",
            FONTAWESOME = ROOT + "fontawesome-webfont.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

}
