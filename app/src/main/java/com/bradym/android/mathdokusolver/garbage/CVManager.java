package com.bradym.android.mathdokusolver.garbage;

import com.bradym.android.mathdokusolver.logic.TrueConstraint;
import com.bradym.android.mathdokusolver.logic.TrueVariable;

/**
 * Created by Michael on 6/7/2015.
 */
public class CVManager {


    public void assignVariable(TrueVariable tv, Integer val) {
        tv.value = val;
        for (TrueConstraint tc : tv.constraints) {
            tc.updateVariable(tv);
        }
    }

}
