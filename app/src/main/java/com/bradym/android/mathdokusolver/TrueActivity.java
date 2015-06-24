package com.bradym.android.mathdokusolver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayout;
import android.view.HapticFeedbackConstants;
import android.view.View;

import com.bradym.android.mathdokusolver.logic.DiffConstraint;
import com.bradym.android.mathdokusolver.logic.TrueConstraint;
import com.bradym.android.mathdokusolver.logic.TrueSolver;
import com.bradym.android.mathdokusolver.logic.TrueVariable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Michael on 6/19/2015.
 *
 * Activity that contains grid
 */
public class TrueActivity extends ActionBarActivity implements TrueDialog.TrueDialogListener {

    int num_col = 6;
    TrueVariable[] variables;
    TrueGrid trueGrid;
    List<TrueCell> trueCells;
    Deque<TrueConstraint> constraints;

    Deque<TrueConstraint> undoHistory;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.true_activity);
        Intent intent = getIntent();
        num_col = intent.getIntExtra("size", num_col);
        trueGrid = (TrueGrid) findViewById(R.id.trueGrid);
        trueCells = new ArrayList<>();
        trueGrid.setColumnCount(num_col);
        trueGrid.setRowCount(num_col);


        variables = new TrueVariable[num_col*num_col];
        constraints = new ArrayDeque<>();
        undoHistory = new ArrayDeque<>();

        //Initialize variables and cells
        for (int i = 0; i < num_col*num_col; i++) {
            List<Integer> dom = new ArrayList<>();
            for (int j = 1; j <= num_col; j++) {
                dom.add(j);
            }
            TrueCell tc = new TrueCell(this);
            variables[i] = new TrueVariable(i + "", dom, tc);
            tc.setAttributes(variables[i], i);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(i / num_col,1f), GridLayout.spec(i % num_col,1f));
            trueGrid.addView(tc, params);
            params.width = 0;
            params.height = 0;
            trueCells.add(tc);

        }

        //Initialize Row/Column constraints
        for(int i = 0; i < num_col; i++) {
            List<TrueVariable> rowVars = new ArrayList<>();
            List<TrueVariable> colVars = new ArrayList<>();
            for(int j = 0; j < num_col; j++) {
                rowVars.add(variables[num_col*i + j]);
                colVars.add(variables[i + num_col*j]);
            }
            TrueConstraint rowConstraint = new DiffConstraint(rowVars);
            TrueConstraint colConstraint = new DiffConstraint(colVars);

            constraints.offer(rowConstraint);
            constraints.offer(colConstraint);
        }
    }

    public void onUndo(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        //Don't want to remove row/column constraints
        if (!(constraints.peekLast() instanceof DiffConstraint)) {
            TrueConstraint tc = constraints.pollLast();
            tc.disable();

            for (TrueVariable tv : tc.scope) {
                TrueCell cell = tv.cell;
                cell.setBorders(0, 0, 0, 0);
                cell.setConstraintString("");
            }
            undoHistory.offerLast(tc);
        }
    }

    public void onRedo(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        TrueConstraint tc;
        if ((tc = undoHistory.pollLast()) != null) {
            addConstraint(tc);
            tc.enable();
        }
    }

    public void addConstraint(TrueConstraint tc) {
        constraints.offerLast(tc);
        ArrayList<TrueCell> nVars = new ArrayList<>(tc.scope.size());
        TrueCell min = null;
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

    @Override
    public void onPositiveClick(TrueConstraint tc, HashSet<TrueCell> cells) {
        boolean allConstrained = true;
        addConstraint(tc);
        undoHistory.clear();


        for (TrueVariable tv : variables) {
            if (tv.constraints.size() != 3) {
                allConstrained = false;
                break;
            }
        }

        if (allConstrained) {
            TrueSolver solver = new TrueSolver(variables);
            SolverTask st = new SolverTask(this, trueCells);
            st.execute(solver);
        }
    }
}
