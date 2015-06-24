package com.bradym.android.mathdokusolver;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bradym.android.mathdokusolver.logic.TrueVariable;

/**
 * Created by Michael on 6/16/2015.
 *
 * Cell that contains two TextViews. One representing the constraint and another representing
 * the actual value
 */
public class TrueCell extends RelativeLayout {

    private Paint m2BorderPaint = new Paint();
    private Paint m1BorderPaint = new Paint();
    private Paint m0BorderPaint = new Paint();
    private Paint[] paints = new Paint[3];

    private TrueVariable var;
    private TextView constraintView;
    private TextView valueView;
    private int index;

    int right = 0;
    int up = 0;
    int left = 0;
    int down = 0;

    public TrueCell(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TrueCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TrueCell(Context context) {
        this(context, null, 0);
    }

    public int getIndex() {
        return index;
    }

    private void init(Context context) {
        setWillNotDraw(false);

        constraintView = new TextView(context);
        constraintView.setTextSize(10);
        constraintView.setPadding(10, 0, 0, 0);
        RelativeLayout.LayoutParams constraintLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        constraintLayout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        constraintLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        addView(constraintView, constraintLayout);

        valueView = new TextView(context);
        valueView.setTextSize(24);
        RelativeLayout.LayoutParams valueLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        valueLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(valueView, valueLayout);


        m2BorderPaint.setStyle(Paint.Style.STROKE);
        m2BorderPaint.setStrokeWidth(6);
        m2BorderPaint.setColor(Color.BLACK);
        paints[2] = m2BorderPaint;

        m1BorderPaint.setStyle(Paint.Style.STROKE);
        m1BorderPaint.setStrokeWidth(3);
        m1BorderPaint.setColor(Color.BLACK);
        paints[1] = m1BorderPaint;

        m0BorderPaint.setColor(Color.BLACK);
        paints[0] = m0BorderPaint;


    }

    public void setVariable(TrueVariable tv) {
        this.var = tv;
    }

    public void setAttributes(TrueVariable tv, int index) {
        this.var = tv;
        this.index = index;
    }

    public void setConstraintString(String s) {
        this.constraintView.setText(s);
    }

    public TrueVariable getVariable() {
        return var;
    }

    public void setValue(String s) {
        this.valueView.setText(s);
    }

    public void refreshValue() {
        this.valueView.setText(var.value + "");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Log.d("DRAWING CELL", "DRAWING CELL " + getText());
        canvas.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight() - 1, paints[right]);
        canvas.drawLine(0, 0, getWidth() - 1, 0, paints[up]);
        canvas.drawLine(0, 0, 0, getHeight() - 1, paints[left]);
        canvas.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1, paints[down]);

    }

    public void setBorders(int right, int up ,int left, int down) {
        this.right = right;
        this.up = up;
        this.left = left;
        this.down = down;
    }

    public void setMaxBorders(int right, int up ,int left, int down) {
        this.right = Math.max(this.right, right);
        this.up = Math.max(this.up, up);
        this.left = Math.max(this.left, left);
        this.down = Math.max(this.down, down);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthMeasureSpec > heightMeasureSpec) {
            int measure = MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.EXACTLY);
            super.onMeasure(measure, measure);
        } else {
            int measure = MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY);
            super.onMeasure(measure, measure);
        }
    }



}
