package com.bradym.android.mathdokusolver.garbage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.widget.TextView;

import com.bradym.android.mathdokusolver.logic.TrueVariable;


/**
 * TODO: document your custom view class.
 */
public class BorderedTextView extends TextView {
    private static Paint mThickBorderPaint = new Paint();
    private static Paint mThinBorderPaint = new Paint();


    private String constraintString = "";

    private TrueVariable var;
    private TextView constraintView;

    int right = 0;
    int up = 0;
    int left = 0;
    int down = 0;

    static {
        mThickBorderPaint.setStyle(Paint.Style.STROKE);
        mThickBorderPaint.setStrokeWidth(7);
        mThickBorderPaint.setColor(Color.BLACK);

        mThinBorderPaint.setColor(Color.BLACK);
    }

    public BorderedTextView(Context context) {
        super(context);
    }

    public void setVariable(TrueVariable var) {
        this.var = var;
    }

    public void setConstraintString(String constraintString) {
        this.constraintString = constraintString;
        refreshText();
        Log.d("SETTING CONSTRAINT", constraintString);
    }

//    public void setVariableValue(Integer val) {
//        this.var.setValue(val);
//        refreshText();
//    }

    private void init() {
        //this.setTextSize(12);
        //this.setGravity(Gravity.TOP);
    }

    public String getConstraintString() {
        return this.constraintString;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Log.d("DRAWING CELL", "DRAWING CELL " + getText());
        if (right != 0)
            canvas.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight() - 1, right == 2 ? mThickBorderPaint : mThinBorderPaint);
        if (up != 0)
            canvas.drawLine(0, 0, getWidth() - 1, 0, up == 2 ? mThickBorderPaint : mThinBorderPaint);
        if (left != 0)
            canvas.drawLine(0, 0, 0, getHeight() - 1, left == 2 ? mThickBorderPaint : mThinBorderPaint);
        if (down != 0)
            canvas.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1, down == 2 ? mThickBorderPaint : mThinBorderPaint);



    }

    public void setBorders(int right, int up, int left, int down) {
        this.right = right;
        this.up = up;
        this.left = left;
        this.down = down;
    }

    public void setRightBorder(int right) {
        this.right = right;
    }

    public void setUpBorder(int up) {
        this.up = up;
    }

    public void setLeftBorder(int left) {
        this.left = left;
    }

    public void setDownBorder(int down) {
        this.down = down;
    }

    public TrueVariable getVariable() {
        return var;
    }

    public void refreshText() {
        String t =  constraintString + "\n" + "\n";
        String value = var.value == -1 ? " " : var.value + "";
        //Log.d("CONSTRAINT", t + "XXX");
        SpannableString s = new SpannableString(t +  value);
        //s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL), 0, t.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE );
        s.setSpan(new RelativeSizeSpan(1.6f), t.length(), t.length() + value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), t.length(), t.length() +  value.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        setText(s);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }
}