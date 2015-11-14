package ch.epfl.sweng.evento.tabsFragment.MyView;

/**
 * Created by thomas on 25/10/15.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MyView extends ImageView {

    public interface OnToggledListener {
        void OnToggled(MyView v, boolean touchOn);
    }

    private boolean mTouchOn;
    private boolean mDownTouch = false;
    private OnToggledListener mToggledListener;
    private int midX = 0; //default
    private int midY = 0; //default

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
        mTouchOn = false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mTouchOn = !mTouchOn;
                invalidate();

                if (mToggledListener != null) {
                    mToggledListener.OnToggled(this, mTouchOn);
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
        mToggledListener = listener;
    }

    public int getIdX() {
        return midX;
    }

    public int getIdY() {
        return midY;
    }

}