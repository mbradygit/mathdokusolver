package com.bradym.android.mathdokusolver;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.bradym.android.mathdokusolver.logic.TrueConstraint;
import com.bradym.android.mathdokusolver.logic.TrueVariable;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Michael on 6/16/2015.
 *
 * Grid that represents the puzzle stage with touch interactions allowing the manipulation of constraints
 *
 */
public class TrueGrid extends GridLayout {

    private final int CELLS = 0x1;
    private final int CONSTRAINTS = 0x2;



    HashSet<TrueCell> selected = new HashSet<>();
    TrueConstraint selectedConstraint = null;

    Activity context;
    FragmentManager fm;

    private int selectionMode = CELLS;
    private HashSet<TrueConstraint> activeConstraints = new HashSet<>();

    public TrueGrid(Context context) {
        this(context, null, 0);
    }

    public TrueGrid(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrueGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (context instanceof Activity) {
            this.context = (Activity) context;
            fm = this.context.getFragmentManager();
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_UP:
                if (context != null) {
                    switch (selectionMode) {
                        case CELLS:
                            if (!selected.isEmpty()) {
                                TrueDialog sd = new TrueDialog();
                                sd.addParameters(selected, getColumnCount(), ConstraintState.ADD);
                                sd.show(fm, "Begin add");
                            }
                            break;
                        case CONSTRAINTS:
                            if (selectedConstraint != null) {
                                TrueDialog sd = new TrueDialog();
                                sd.addParameters(selectedConstraint, ConstraintState.EDIT);
                                sd.show(fm, "Begin edit");
                            }
                            break;
                    }
                }

                return true;
            case MotionEvent.ACTION_CANCEL:
                for (TrueCell tc : selected) {
                    tc.unSelect();
                }
                selected.clear();
                return true;

            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < getChildCount(); i++) {
                    TrueCell cell = (TrueCell) getChildAt(i);
                    Rect rect = new Rect();
                    cell.getHitRect(rect);
                    if (rect.contains((int) event.getX(), (int) event.getY())) {
                        TrueVariable var = cell.getVariable();
                        if (var.constraints.size() == 2) {
                            selectionMode = CELLS;
                            selected.add(cell);
                            cell.select();
                        } else {
                            selectionMode = CONSTRAINTS;
                            selectedConstraint = var.constraints.get(2);
                            for (TrueVariable tv : selectedConstraint.scope) {
                                tv.cell.select();
                            }
                        }
                        return true;
                    }
                }
                return false;

            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < getChildCount(); i++) {
                    TrueCell v = (TrueCell) getChildAt(i);
                    Rect rect = new Rect();
                    v.getHitRect(rect);
                    if (rect.contains((int) event.getX(),  (int) event.getY()) && v.getVariable().constraints.size() == 2) {
                            switch (selectionMode) {
                                case CELLS:
                                    for (TrueCell tc : selected) {
                                        int dim = getColumnCount();
                                        int colDiff = Math.abs(tc.getIndex() % dim - v.getIndex() % dim);
                                        int rowDiff = Math.abs(tc.getIndex() / dim - v.getIndex() / dim);

                                        if (colDiff * rowDiff == 0 && colDiff + rowDiff == 1) {
                                            selected.add(v);
                                            v.select();
                                            return true;
                                        }
                                    }
                                default:
                                    return true;

                            }
                    }
                }
                return true;
        }

        return false;

    }

    public HashSet<TrueConstraint> getActiveConstraints() {
        return activeConstraints;
    }

    public void addConstraint(TrueConstraint tc) {
        tc.enable();
        activeConstraints.add(tc);

        ArrayList<TrueCell> nVars = new ArrayList<>(tc.scope.size());

        TrueCell min = null;
        int num_col = getColumnCount();
        for (TrueVariable var : tc.scope) {

            TrueCell cell = var.cell;
            int i = cell.getIndex();
            if (min == null || i < min.getIndex()) {
                min = cell;
            }
            cell.up = i / num_col == 0 ? 0 : 2;
            cell.left = i % num_col == 0 ? 0 : 2;
            cell.right = i % num_col == num_col - 1 ? 0 : 2;
            cell.down = i / num_col == num_col - 1 ? 0 : 2;

            for (TrueCell tv : nVars) {
                int diff = i - tv.getIndex();
                if (diff == 1) {
                    cell.left = 0;
                    tv.right = 0;
                } else if (diff == -1) {
                    cell.right = 0;
                    tv.left = 0;
                } else if (diff == -num_col) {
                    cell.down = 0;
                    tv.up = 0;
                } else if (diff == num_col) {
                    cell.up = 0;
                    tv.down = 0;
                }
            }
            nVars.add(cell);
            cell.invalidate();
        }
        if (min != null) {
            min.setConstraintString(tc.simpleString());
        }
    }

    public void deleteConstraint(TrueConstraint tc) {
        tc.disable();
        activeConstraints.remove(tc);

        for (TrueVariable tv : tc.scope) {
            TrueCell cell = tv.cell;
            cell.setBorders(0, 0, 0, 0);
            cell.setConstraintString("");
            cell.invalidate();
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthMeasureSpec > heightMeasureSpec) {
            //int measure = MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.EXACTLY);
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        } else {
            //int measure = MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
    }

}
