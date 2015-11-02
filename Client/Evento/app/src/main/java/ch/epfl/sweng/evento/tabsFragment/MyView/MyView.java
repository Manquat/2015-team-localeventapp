package ch.epfl.sweng.evento.tabsFragment.MyView;

/**
 * Created by thomas on 25/10/15.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ch.epfl.sweng.evento.R;

public class MyView extends ImageView {

    public interface OnToggledListener {
        void OnToggled(MyView v, boolean touchOn);
    }

    boolean mtouchOn;
    boolean mDownTouch = false;
    private OnToggledListener mtoggledListener;
    int midX = 0; //default
    int midY = 0; //default

    public MyView(Context context, int x, int y) {
        super(context);
        midX = x;
        midY = y;
        init();
    }

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mtouchOn = false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mtouchOn = !mtouchOn;
                invalidate();

                if (mtoggledListener != null) {
                    mtoggledListener.OnToggled(this, mtouchOn);
                }

                mDownTouch = true;
                return true;

            case MotionEvent.ACTION_UP:
                if (mDownTouch) {
                    mDownTouch = false;
                    performClick();
                    return true;
                }
        }
        return false;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    public void setOnToggledListener(OnToggledListener listener) {
        mtoggledListener = listener;
    }

    public int getIdX() {
        return midX;
    }

    public int getIdY() {
        return midY;
    }

}