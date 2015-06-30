package com.bradym.android.mathdokusolver.logic;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Created by Michael on 6/3/2015.
 *
 * Constraint representing Plus.
 *
 *
 */
public class PlusConstraint extends TrueConstraint {

    private final Deque<Integer> partials;
    private int rem;

    public PlusConstraint(int value, List<TrueVariable> scope, int max) {
        super(value, scope, max);
        partials = new ArrayDeque<>();
        partials.addLast(0);
        rem = scope.size();
    }

    public boolean updateVariable(TrueVariable var) {
        Integer top = partials.peekLast();
        if ((top + var.value + MIN_NUM * (rem - 1) <= value) && (top + var.value + MAX_NUM * (rem - 1) >= value)) {
            partials.addLast(top + var.value);
            rem--;
            return true;
        }

        return false;
    }

    public boolean validate(TrueVariable var, Integer val) {
        Integer top = partials.peekLast();
        return ((top + val + MIN_NUM * (rem - 1) <= value) && (top + val + MAX_NUM * (rem - 1) >= value));
    }

    public void restore() {
        partials.clear();
        partials.addLast(0);
        rem = scope.size();
    }

    public void pop() {
        partials.pollLast();
        rem++;
    }

    @Override
    public String toString() {
        return "PLUS CONSTRAINT | SCOPE = " + scope.toString() + " | " + partials.peekLast().toString();
    }

    public String simpleString() {
        if (scope.size() == 1) {
            return value + "";
        } else {
            return value + "\u002B";
        }
    }

}
