package com.bradym.android.mathdokusolver;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.HashSet;

/**
 * Created by Michael on 6/16/2015.
 */
public class TrueGrid extends GridLayout {

    HashSet<TrueCell> selected = new HashSet<>();

    Activity context;

    public TrueGrid(Context context) {
        this(context, null, 0);
    }

    public TrueGrid(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrueGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (context instanceof Activity)
            this.context = (Activity) context;
    }

    private void init() {
    }
    public HashSet<TrueCell> getSelected() {
        return selected;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (context != null) {
                    TrueDialog sd = new TrueDialog();
                    sd.addParameters(selected, getColumnCount());
                    FragmentManager fm = context.getFragmentManager();
                    sd.show(fm, "start fragment");
                }

                return true;
            case MotionEvent.ACTION_CANCEL:
                for (TrueCell tc : selected) {
                    tc.setBackgroundColor(Color.TRANSPARENT);
                }
                selected.clear();
                return true;
            case MotionEvent.ACTION_DOWN:
                for (TrueCell tc : selected) {
                    tc.setBackgroundColor(Color.TRANSPARENT);
                }
                selected.clear();
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < getChildCount(); i++) {
                    TrueCell v = (TrueCell) getChildAt(i);
                    Rect rect = new Rect();
                    v.getHitRect(rect);
                    if (rect.contains((int) event.getX(),  (int) event.getY()) && v.getVariable().constraints.size() == 2) {
                        selected.add(v);
                        v.setBackgroundColor(Color.LTGRAY);
                        return true;
                    }
                }
                return true;

        }

        return false;

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthMeasureSpec > heightMeasureSpec) {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
    }


}
