package com.bradym.android.mathdokusolver.logic;

import java.util.List;

/**
 * Created by Michael on 6/3/2015.
 *
 * Abstract class that all Constraints inherit.
 *
 */
public abstract class TrueConstraint {

    public final int value;
    public final List<TrueVariable> scope;
    final int MAX_NUM;
    final int MIN_NUM;
    boolean disabled = false;

    public TrueConstraint(int value, List<TrueVariable> scope, int max) {
        this.value = value;
        this.scope = scope;
        this.MIN_NUM = 1;
        this.MAX_NUM = max;
        for (TrueVariable tv : scope) {
            tv.constraints.add(this);
        }
    }

    public void disable() {
        if (!disabled) {
            for (TrueVariable tv : scope) {
                tv.constraints.remove(this);
            }
            disabled = true;
        }
    }

    public void enable() {
        if (disabled) {
            for (TrueVariable tv : scope) {
                tv.constraints.add(this);
            }
            disabled = false;
        }
    }

    public abstract void pop();

    public abstract boolean updateVariable(TrueVariable v);

    public abstract boolean validate(TrueVariable v, Integer d);

    public abstract String simpleString();


}
