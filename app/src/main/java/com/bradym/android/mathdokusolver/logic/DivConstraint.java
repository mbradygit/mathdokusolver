package com.bradym.android.mathdokusolver.logic;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael on 6/3/2015.
 */
public class DivConstraint extends TrueConstraint {

    final Deque<Map<TrueVariable, Fraction>> partials;
    int rem = scope.size();

    public DivConstraint(int value, List<TrueVariable> scope, int max) {

        super(value, scope, max);
        partials = new ArrayDeque<>();
        Map<TrueVariable, Fraction> map = new HashMap<>();
        for (TrueVariable tv : scope) {
            map.put(tv, new Fraction());
        }
        partials.addLast(map);
    }


    //TODO: Optimize
    public boolean updateVariable(TrueVariable var) {

        int maxHeuristic = (int) Math.pow(MAX_NUM, rem-1);
        //Log.d("HEURISTIC, VALUE", maxHeuristic + " " + value);

        Map<TrueVariable, Fraction> map = new HashMap<>();
        for (Map.Entry<TrueVariable, Fraction> entry : partials.peekLast().entrySet()) {

            TrueVariable v = entry.getKey();
            Fraction f  = entry.getValue();
            //Log.d("NUM, DEN", f.num + " " + f.den);

            if (v.equals(var)) {
                if (!f.bNum && (var.value <= f.den * maxHeuristic * value) && (var.value >= value*f.den)) {
                    map.put(v, f.multiplyNum(var.value));
                }
            } else {
                if (f.bNum) {
                    if ((f.num <= f.den * var.value * maxHeuristic * value) && (f.num >= value * f.den * var.value)) {
                        map.put(v, f.multiplyDen(var.value));
                    }
                } else if (rem > 1) {
                    int maxT = (int) Math.pow(MAX_NUM, rem-2);

                    if ((MIN_NUM <= f.den * var.value * maxT * value) && (MAX_NUM >= value*f.den * var.value)) {
                        map.put(v, f.multiplyDen(var.value));
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

        int maxHeuristic = (int) Math.pow(MAX_NUM, rem-1);
        //Log.d("HEURISTIC, VALUE", maxHeuristic + " " + value);

        for (Map.Entry<TrueVariable, Fraction> entry : partials.peekLast().entrySet()) {
            TrueVariable v = entry.getKey();
            Fraction f  = entry.getValue();
            //Log.d("NUM, DEN", f.num + " " + f.den);

            if (v.equals(var)) {
                if (!f.bNum && (val <= f.den * maxHeuristic * value) && (val >= value*f.den)) {
                    return true;
                }
            } else {
                if (f.bNum) {
                    if ((f.num <= f.den * maxHeuristic * value * val) && (f.num >= value*f.den*val)) {
                        return true;
                    }
                } else if (rem > 1){
                    int maxT = (int) Math.pow(MAX_NUM, rem-2);

                    if ((MIN_NUM <= f.den * maxT * value * val) && (MAX_NUM >= value*f.den * val)) {
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

    private class Fraction {
        int num;
        boolean bNum = false;
        int den;

        public Fraction() {
            this.num = 1;
            this.den = 1;
        }

        public Fraction(int n, int d, boolean bNum) {
            this.num = n;
            this.den = d;
            this.bNum = bNum;
        }

        public Fraction multiplyDen(int multiplicand) {
            return new Fraction(num, den * multiplicand, bNum);
        }

        public Fraction multiplyNum(int multiplicand) {
            return new Fraction(num * multiplicand, den, true);
        }

        @Override
        public String toString() {
            return num + "/" + den + " " + bNum;
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return "DIV CONSTRAINT " + value + " | SCOPE = " + scope.toString() + " | " + partials.peekLast().toString();
    }

    public String simpleString() {
        return value + "\u00F7";
    }


}
