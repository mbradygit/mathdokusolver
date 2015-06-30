package com.bradym.android.mathdokusolver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayout;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageButton;

import com.bradym.android.mathdokusolver.logic.DiffConstraint;
import com.bradym.android.mathdokusolver.logic.TrueConstraint;
import com.bradym.android.mathdokusolver.logic.TrueSolver;
import com.bradym.android.mathdokusolver.logic.TrueVariable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    Deque<ConstraintState> history;
    Deque<ConstraintState> undoHistory;

    ImageButton solverButton;
    ImageButton undoButton;
    ImageButton redoButton;
    ImageButton clearButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.true_activity);
        Intent intent = getIntent();
        num_col = intent.getIntExtra("size", num_col);
        trueGrid = (TrueGrid) findViewById(R.id.trueGrid);

        solverButton = (ImageButton) findViewById(R.id.solveButton);
        undoButton = (ImageButton) findViewById(R.id.undoButton);
        redoButton = (ImageButton) findViewById(R.id.redoButton);
        clearButton = (ImageButton) findViewById(R.id.clearButton);


        trueCells = new ArrayList<>();
        trueGrid.setColumnCount(num_col);
        trueGrid.setRowCount(num_col);


        variables = new TrueVariable[num_col*num_col];
        history = new ArrayDeque<>();
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
            new DiffConstraint(rowVars);
            new DiffConstraint(colVars);
        }

        adjustButtons(false, undoButton, redoButton, solverButton, clearButton);
    }

    public void adjustButtons(boolean enable, ImageButton... buttons) {
        for (ImageButton b : buttons) {
            if (enable) {
                b.setClickable(true);
                b.setAlpha(1.0f);
            } else {
                b.setClickable(false);
                b.setAlpha(0.25f);

            }
        }
    }

    public void onTrueUndo(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        ConstraintState state;
        if ((state = history.pollLast()) != null) {
            switch (state.getAction()) {
                case ConstraintState.ADD:
                    for (TrueConstraint tc : state.getConstraints()) {
                        trueGrid.deleteConstraint(tc);
                    }
                    undoHistory.offerLast(state);
                    adjustButtons(true, redoButton);
                    break;

                case ConstraintState.DELETE:
                    for (TrueConstraint tc: state.getConstraints()) {
                        trueGrid.addConstraint(tc);
                    }
                    undoHistory.offerLast(state);
                    adjustButtons(true, redoButton);
                    break;

                case ConstraintState.EDIT:
                    trueGrid.deleteConstraint(state.getConstraints().get(1));
                    trueGrid.addConstraint(state.getConstraints().get(0));
                    undoHistory.offerLast(state);
                    adjustButtons(true, redoButton);
                    break;
            }
        }

        if (history.isEmpty()) {
            adjustButtons(false, undoButton);
        }

        checkSolverButton();
        checkClearButton();
    }
    public void onTrueRedo(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        ConstraintState cs;
        if ((cs = undoHistory.pollLast()) != null) {
            switch (cs.getAction()) {
                case ConstraintState.ADD:
                    for (TrueConstraint tc : cs.getConstraints()) {
                        trueGrid.addConstraint(tc);
                    }
                    history.offerLast(cs);
                    adjustButtons(true, undoButton);
                    break;

                case ConstraintState.DELETE:
                    for (TrueConstraint tc: cs.getConstraints()) {
                        trueGrid.deleteConstraint(tc);
                    }
                    history.offerLast(cs);
                    adjustButtons(true, undoButton);
                    break;

                case ConstraintState.EDIT:
                    trueGrid.deleteConstraint(cs.getConstraints().get(0));
                    trueGrid.addConstraint(cs.getConstraints().get(1));
                    history.offerLast(cs);
                    adjustButtons(true, undoButton);
                    break;
            }
        }
        if (undoHistory.isEmpty()) {
            adjustButtons(false, redoButton);
        }

        checkSolverButton();
        checkClearButton();
    }


    public void onSolveClick(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        TrueSolver solver = new TrueSolver(variables);
        SolverTask st = new SolverTask(this, trueCells);
        Long result;
        try {
            result = st.execute(solver).get();
        } catch (InterruptedException|ExecutionException ex) {
            result = null;
        }

        if (result!= -1L) {
            history.clear();
            undoHistory.clear();
            adjustButtons(false, redoButton);
            adjustButtons(false, undoButton);
            adjustButtons(false, solverButton);

        } else {
            checkSolverButton();

        }
        checkClearButton();




    }

    public void onClearClick(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

        ConstraintState state = new ConstraintState(ConstraintState.DELETE);

        for (TrueConstraint tc : new ArrayList<>(trueGrid.getActiveConstraints())) {
            state.addConstraints(tc);
            for (TrueVariable tv : tc.scope) {
                tv.cell.resetValue();
            }
            trueGrid.deleteConstraint(tc);
        }

        history.offerLast(state);
        undoHistory.clear();
        adjustButtons(false, redoButton);
        adjustButtons(true, undoButton);
        checkSolverButton();
        checkClearButton();
    }

    public void onPositiveClick(ConstraintState cs) {
        switch (cs.getAction()) {
            case ConstraintState.ADD:
                for (TrueConstraint tc : cs.getConstraints()) {
                    trueGrid.addConstraint(tc);
                }
                history.offerLast(cs);
                break;

            case ConstraintState.DELETE:
                for (TrueConstraint tc: cs.getConstraints()) {
                    trueGrid.deleteConstraint(tc);
                }
                history.offerLast(cs);
                break;

            case ConstraintState.EDIT:
                trueGrid.deleteConstraint(cs.getConstraints().get(0));
                trueGrid.addConstraint(cs.getConstraints().get(1));
                history.offerLast(cs);
                break;
        }
        adjustButtons(false, redoButton);
        adjustButtons(true, undoButton);
        checkSolverButton();
        checkClearButton();
        undoHistory.clear();
    }

    public void checkSolverButton() {
        boolean allConstrained = true;
        for (TrueVariable tv : variables) {
            if (tv.constraints.size() != 3) {
                allConstrained = false;
                break;
            }
        }
        if (allConstrained) {
            adjustButtons(true, solverButton);
        } else {
            adjustButtons(false, solverButton);
        }
    }

    public void checkClearButton() {
        if (trueGrid.getActiveConstraints().isEmpty()) {
            adjustButtons(false, clearButton);
        } else {
            adjustButtons(true, clearButton);
        }

    }

}
