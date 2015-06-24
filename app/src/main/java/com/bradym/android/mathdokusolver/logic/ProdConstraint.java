package com.bradym.android.mathdokusolver.logic;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Created by Michael on 6/3/2015.
 *
 * Constraint representing Product.
 */
public class ProdConstraint extends TrueConstraint {

    final Deque<Integer> partials;
    private int rem;

    public ProdConstraint(int value, List<TrueVariable> scope, int max) {
        super(value, scope, max);
        partials = new ArrayDeque<>();
        partials.addLast(1);
        rem = scope.size();
    }

    public boolean updateVariable(TrueVariable var) {
        Integer top = partials.peekLast();
        if ((top * var.value <= value) && (top * var.value * Math.pow(MAX_NUM, rem-1) >= value)) {
            partials.addLast(top * var.value);
            rem--;
            return true;
        }
        return false;
    }

    public boolean validate(TrueVariable var, Integer val) {
        Integer top = partials.peekLast();
        return ((top * val <= value) && (top * val * Math.pow(MAX_NUM, rem-1) >= value));
    }

    public void pop() {
        partials.pollLast();
        rem++;
    }

    @Override
    public String toString() {
        return "PROD CONSTRAINT | SCOPE = " + scope.toString() + " | " + partials.peekLast().toString();
    }

    public String simpleString() {
        return value + "\u00D7";
    }

}
