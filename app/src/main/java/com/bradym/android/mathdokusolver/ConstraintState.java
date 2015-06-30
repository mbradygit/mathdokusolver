package com.bradym.android.mathdokusolver;

import com.bradym.android.mathdokusolver.logic.TrueConstraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Michael on 6/25/2015.
 */
public class ConstraintState {

    public static final int ADD = 0;
    public static final int EDIT = 1;
    public static final int DELETE = 2;

    private int action;
    private List<TrueConstraint> constraints = new ArrayList<>();


    /*
    If action is ADD, constraints is an array of all constraints that have been added
    If action is EDIT, constraints[0] is the previous constraint and constraints[1] is the new one
    If action is DELETE, constraints[2] is an array of all deleted constraints
     */
    public ConstraintState(int action, TrueConstraint ... constraints) {
        if (action < 3 && action > -1) {
            this.action = action;
            this.constraints.addAll(Arrays.asList(constraints));
        }
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        if (action < 3 && action > -1) {
            this.action = action;
        }
    }

    public List<TrueConstraint> getConstraints() {
        return constraints;
    }

    public void addConstraints(TrueConstraint ... constraints) {
        this.constraints.addAll(Arrays.asList(constraints));
    }




}
