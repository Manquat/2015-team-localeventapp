package ch.epfl.sweng.evento.tabs_fragment.MyView;

/**
 * Created by thomas on 25/10/15.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageView;

public class MyView extends ImageView
{
    final int PADDING = 5;
    int midX = 0; //default
    int midY = 0; //default
    private Paint mBorderPaint;
    private static final float STROKE_WIDTH = 16f;

    private void initBorderPaint() {
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(Color.WHITE);
        mBorderPaint.setStrokeWidth(STROKE_WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(PADDING, PADDING, getWidth() - PADDING, getHeight() - PADDING, mBorderPaint);
    }
    public MyView(Context context, int x, int y)
    {
        super(context);
        midX = x;
        midY = y;
        initBorderPaint();
    }


    public MyView(Context context)
    {
        super(context);
    }

    public int getIdX()
    {
        return midX;
    }

    public int getIdY()
    {
        return midY;
    }

}