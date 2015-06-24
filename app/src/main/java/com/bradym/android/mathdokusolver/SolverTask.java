package com.bradym.android.mathdokusolver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.bradym.android.mathdokusolver.logic.TrueSolver;

import java.util.List;

/**
 * Created by Michael on 5/14/2015.
 *
 * Async Task to initiate GAC solve.
 */
public class SolverTask extends AsyncTask<TrueSolver, Void, Long>{

    List<TrueCell> trueCells;
    ProgressDialog pg;
    Activity activity;

    public SolverTask(Activity activity, List<TrueCell> trueCells) {
        this.pg = new ProgressDialog(activity);
        this.trueCells = trueCells;
        this.activity = activity;
    }

    TrueSolver solver;

    @Override
    protected void onPreExecute() {
        pg.setMessage("Solving...");
        pg.show();
    }


    @Override
    protected Long doInBackground(TrueSolver... params) {
        solver = params[0];
        return solver.solveGAC();
    }

    @Override
    protected void onPostExecute(Long result) {
        if (pg.isShowing()) {
            pg.dismiss();
        }
        if (result == -1) {
            Toast.makeText(activity, "Unable to solve", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(activity, "Solved in " + result + " ms", Toast.LENGTH_SHORT).show();
        }
        for (TrueCell tc : trueCells) {
            tc.refreshValue();
        }
    }
}
