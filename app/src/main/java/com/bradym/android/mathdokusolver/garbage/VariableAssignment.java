package com.bradym.android.mathdokusolver.garbage;

/**
 * Created by Michael on 5/25/2015.
 */
public class VariableAssignment {

    private Variable var;
    private Integer val;
    private boolean cut;

    public VariableAssignment(Variable var, Integer val) {
        this.var = var;
        this.val = val;
        this.cut = false;
    }

    public boolean isCut() {
        return cut;
    }

    public void cut() {
        cut = true;
    }
    
    public void uncut() {
        cut = false;
    }

    public Integer getValue() {
        return val;
    }

    public Variable getVariable() { return var; }
    
}
