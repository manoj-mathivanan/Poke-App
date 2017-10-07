package com.manoj.prick;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class TextProgressBar extends ProgressBar {  
    private String text;
    private Paint textPaint;

    public TextProgressBar(Context context) {
        super(context);
        text = "HPI";
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
    }

    public TextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        text = "HP";
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
    }

    public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        text = "HP";
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // First draw the regular progress bar, then custom draw our text
        super.onDraw(canvas);
        Rect bounds = new Rect();
        textPaint.setTextSize(40f);
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        //int x = getWidth();
        //int i = bounds.centerX();
        int y = getHeight();
        int j = bounds.centerY();
        canvas.drawText(text, 13, y/2-j, textPaint);
    }

    public synchronized void setText(String text) {
        this.text = text;
        drawableStateChanged();
    }

    public void setTextColor(int color) {
        textPaint.setColor(color);
        drawableStateChanged();
    }
}