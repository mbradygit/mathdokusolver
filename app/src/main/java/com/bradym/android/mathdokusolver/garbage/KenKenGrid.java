package com.bradym.android.mathdokusolver.garbage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.GridView;

import com.bradym.android.mathdokusolver.TrueCell;
import com.bradym.android.mathdokusolver.logic.TrueVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 4/8/2015.
 */
public class KenKenGrid extends GridView {

    //private List<BorderedTextView> selected = new ArrayList();
    private List<TrueCell> selected = new ArrayList();
    private Context mContext;

    public KenKenGrid(Context context) {
        super(context);
        //init(context);
    }

    public KenKenGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        //init(context);
    }

    public KenKenGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //init(context);
    }


    public void selectView(TrueCell btv) {
        if (!selected.contains(btv) && btv.getVariable().constraints.size() == 2) {
            selected.add(btv);
            btv.setBackgroundColor(Color.LTGRAY);
        }
    }

//    public void selectCell(KenKenCell cell) {
//        if (!selected.contains(cell.btv) && cell.btv.isConstrained())
//            btv.setBackgroundColor(Color.LTGRAY);
//            selected.add(cell);
//        }
//    }

    public List<TrueCell> getSelected() {
        return selected;
    }

    public CellAdapter getAdapter() {
        return (CellAdapter) super.getAdapter();
    }

    public TrueVariable[] getVariables() {
        return getAdapter().getVariables();
    }

    public void cleanSelection() {
        //Log.d("CLEARING", ((BorderedTextView) getChildAt(0)).getText().toString());
        for (TrueCell btv : selected) {
            btv.setBackgroundColor(Color.TRANSPARENT);
        }
        selected.clear();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
