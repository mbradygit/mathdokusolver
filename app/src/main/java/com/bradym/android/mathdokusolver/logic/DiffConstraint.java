package com.bradym.android.mathdokusolver.logic;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * Created by Michael on 6/5/2015.
 */
public class DiffConstraint extends TrueConstraint {

    int[] found;
    Deque<Integer> prev;

    public DiffConstraint(List<TrueVariable> scope) {
        super(-1, scope, scope.size());
        found = new int[scope.size()];
        prev = new ArrayDeque<>(scope.size());
    }

    public boolean updateVariable(TrueVariable var) {
        if (found[var.value-1] == 1) {
            return false;
        } else {
            found[var.value-1] = 1;
            prev.addLast(var.value);
            return true;
        }
    }

    public boolean validate(TrueVariable var, Integer val) {
        if (found[val-1] == 1) {
            return false;
        } else {
            return true;
        }
    }

    public void pop() {
        found[prev.pollLast() - 1] = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (TrueVariable tv : scope) {
            sb.append(tv.toString() + "|");
        }
        return "DIFFERENT CONSTRAINT | SCOPE = " + scope.toString() + " | " + Arrays.toString(found);
    }

    public String simpleString() {
        return "";
    }

}
