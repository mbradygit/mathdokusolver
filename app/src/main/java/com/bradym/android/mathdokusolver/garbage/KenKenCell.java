package com.bradym.android.mathdokusolver.garbage;

import com.bradym.android.mathdokusolver.garbage.BorderedLayout;
import com.bradym.android.mathdokusolver.garbage.Variable;

/**
* View that represents a single Cell in a KenKen grid.
*/

public class KenKenCell {

    public Variable variable;
    public BorderedLayout btv;

    public KenKenCell(Variable v) {
        variable = v;
    }

    public boolean isConstrained() {
        return variable != null ? variable.isConstrained() : false;
    }
    public boolean constrain() {
        return variable.isConstrained();
    }

}
