package ch.epfl.sweng.evento.tabs_fragment.MyView;

/**
 * Created by thomas on 25/10/15.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyView extends ImageView {

    int midX = 0; //default
    int midY = 0; //default


    public MyView(Context context, int x, int y) {
        super(context);
        midX = x;
        midY = y;
    }




    public MyView(Context context) {
        super(context);
    }

    public int getIdX() {
        return midX;
    }

    public int getIdY() {
        return midY;
    }

}