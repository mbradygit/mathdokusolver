package com.bradym.android.mathdokusolver;

import android.os.AsyncTask;

import com.bradym.android.mathdokusolver.logic.TrueSolver;

import java.util.List;

/**
 * Created by Michael on 5/14/2015.
 */
public class SolverTask extends AsyncTask<TrueSolver, Void, Void>{

    List<TrueCell> trueCells;

    public SolverTask(List<TrueCell> trueCells) {
        this.trueCells = trueCells;
    }

    TrueSolver solver;
    @Override
    protected Void doInBackground(TrueSolver... params) {
        solver = params[0];
        solver.solveGAC();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        for (TrueCell tc : trueCells) {
            tc.refreshValue();
        }
    }
}
