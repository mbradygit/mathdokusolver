package com.bradym.android.mathdokusolver.logic;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael on 6/3/2015.
 * Represents the Minus Constraint.
 * Given n variables, any of the n can be the minuend of the difference yielding n possible values
 *
 */
public class MinusConstraint extends TrueConstraint {

    final Deque<Map<TrueVariable, Difference>> partials;
    int rem;

    public MinusConstraint(int value, List<TrueVariable> scope, int max) {
        super(value, scope, max);
        partials = new ArrayDeque<>();
        rem = scope.size();

        Map<TrueVariable, Difference> map = new HashMap<>();
        for (TrueVariable tv : scope) {
            map.put(tv, new Difference());
        }
        partials.addLast(map);
    }

    public boolean updateVariable(TrueVariable var) {
        Map<TrueVariable, Difference> map = new HashMap<>();
        int maxHeuristic = (MAX_NUM) * (rem - 1);
        int minHeuristic = (MIN_NUM) * (rem - 1);
        //Log.d("MAX, MIN, CONST, VARVAL", maxHeuristic + " " + minHeuristic + " " + value + " " + var.value);
        for (Map.Entry<TrueVariable, Difference> entry : partials.peekLast().entrySet()) {
            TrueVariable v = entry.getKey();
            Difference d = entry.getValue();

            //Log.d("NUM, DEN", d.minuend + " " + d.subtrahend);


            if (v.equals(var)) {
                if ((d.minuend == 0) && (var.value <= d.subtrahend + maxHeuristic + value) && (var.value >= value + d.subtrahend + minHeuristic)) {
                    map.put(v, d.add(var.value));
                }
            } else {
                if (d.minuend != 0) {
                    if ((d.minuend <= d.subtrahend + var.value + maxHeuristic + value) && (d.minuend >= value + d.subtrahend + var.value + minHeuristic)) {
                        map.put(v, d.subtract(var.value));
                    }
                } else if (rem > 1){
                    int maxT = (MAX_NUM) * (rem - 2);
                    int minT = (MIN_NUM) * (rem - 2);

                    if ((MIN_NUM <= d.subtrahend + var.value + maxT + value) && (MAX_NUM >= value + d.subtrahend + var.value + minT)) {
                        map.put(v, d.subtract(var.value));
                    }
                }
            }
        }
        if (map.isEmpty()) {
            return false;
        } else {
            rem--;
            partials.addLast(map);
            return true;
        }
    }

    public boolean validate(TrueVariable var, Integer val) {
        int maxHeuristic = (MAX_NUM) * (rem - 1);
        int minHeuristic = (MIN_NUM) * (rem - 1);
        //Log.d("MAX, MIN, CONST, VARVAL", maxHeuristic + " " + minHeuristic + " " + value + " " + val);

        for (Map.Entry<TrueVariable, Difference> entry : partials.peekLast().entrySet()) {
            TrueVariable v = entry.getKey();
            Difference d = entry.getValue();

            if (v.equals(var)) {
                if ((d.minuend == 0) && (val <= d.subtrahend + maxHeuristic + value) && (val >= value + d.subtrahend + minHeuristic)) {
                    return true;
                }
            } else {
                if (d.minuend != 0) {
                    if ((d.minuend <= d.subtrahend + val + maxHeuristic + value) && (d.minuend >= value + d.subtrahend + val + minHeuristic)) {
                        return true;
                    }
                } else if (rem > 1){
                    int maxT = (MAX_NUM) * (rem - 2);
                    int minT = (MIN_NUM) * (rem - 2);

                    if ((MIN_NUM <= d.subtrahend + val + maxT + value) && (MAX_NUM >= value + d.subtrahend + val + minT)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void pop() {
        partials.pollLast();
        rem++;
    }

    @Override
    public String toString() {
        return "MINUS CONSTRAINT | SCOPE = " + scope.toString() + " | " + partials.peekLast().toString();
    }

    private class Difference {
        final int minuend;
        final int subtrahend;

        public Difference() {
            minuend = 0;
            subtrahend = 0;
        }

        public Difference(int m, int s) {
            this.minuend = m;
            this.subtrahend = s;
        }

        public Difference add(int a) {
            return new Difference(a, subtrahend);
        }

        public Difference subtract(int s) {
            return new Difference(minuend, subtrahend + s);
        }

        @Override
        public String toString() {
            return (minuend - subtrahend) + "";
        }
    }

    public String simpleString() {
        return value + "\u2212";
    }

}
