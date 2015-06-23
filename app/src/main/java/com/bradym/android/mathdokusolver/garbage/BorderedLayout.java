package com.bradym.android.mathdokusolver.garbage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bradym.android.mathdokusolver.logic.TrueVariable;

/**
 * Created by Michael on 6/16/2015.
 */
public class BorderedLayout extends RelativeLayout {


    private static Paint mThickBorderPaint = new Paint();
    private static Paint mThinBorderPaint = new Paint();

    int right = 0;
    int up = 0;
    int left = 0;
    int down = 0;

    private TextView constraint;
    private TextView value;

    private TrueVariable tv;

    static {
        mThickBorderPaint.setStyle(Paint.Style.STROKE);
        mThickBorderPaint.setStrokeWidth(7);
        mThickBorderPaint.setColor(Color.BLACK);
        mThinBorderPaint.setColor(Color.BLACK);
    }

    public BorderedLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public BorderedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BorderedLayout(Context context) {

        super(context);

        constraint = new TextView(getContext());
        constraint.setId(View.generateViewId());
        constraint.setTextSize(20f);
        constraint.setTextColor(Color.BLACK);
        constraint.setText("HAHAHA");
        constraint.setBackgroundColor(Color.RED);
        RelativeLayout.LayoutParams constraintLayout = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        constraintLayout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        constraintLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        value = new TextView(getContext());
        value.setTextSize(20f);
        value.setId(View.generateViewId());
        value.setTextColor(Color.BLACK);
        value.setText("HAHAHA");
        value.setBackgroundColor(Color.GREEN);
        RelativeLayout.LayoutParams valueLayout = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        valueLayout.addRule(RelativeLayout.CENTER_IN_PARENT);

        addView(constraint, constraintLayout);
        addView(value, valueLayout);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (right != 0)
            canvas.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight() - 1, right == 2 ? mThickBorderPaint : mThinBorderPaint);
        if (up != 0)
            canvas.drawLine(0, 0, getWidth() - 1, 0, up == 2 ? mThickBorderPaint : mThinBorderPaint);
        if (left != 0)
            canvas.drawLine(0, 0, 0, getHeight() - 1, left == 2 ? mThickBorderPaint : mThinBorderPaint);
        if (down != 0)
            canvas.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1, down == 2 ? mThickBorderPaint : mThinBorderPaint);

    }

    public void setConstraintString(String c) {
        constraint.setText(c);
    }

    public String getConstraintString() {
        return constraint.getText().toString();
    }

    public TrueVariable getVariable() {
        return tv;
    }

    public void setVariable(TrueVariable tv) {
        this.tv = tv;
    }

    public void setBorders(int right, int up, int left, int down) {
        this.right = right;
        this.up = up;
        this.left = left;
        this.down = down;
    }


    public void setValue(String v) {
        value.setText(v);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }
}
