package com.bradym.android.mathdokusolver.garbage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 5/10/2015.
 */
public class AssignmentOLD {

    private final List<Integer> assignment;

    public AssignmentOLD(int size) {
        this.assignment = new ArrayList<>(size);
    }

    public AssignmentOLD(List<Integer> assignment) {
        this.assignment = assignment;
    }

    public List<Integer> getAssignment() {
        return this.assignment;
    }

    public Integer getValueOf(int index) {
        return this.assignment.get(index);
    }

    public void add(Integer toAdd) {
        this.assignment.add(toAdd);
    }

    @Override
    public String toString() {
        return assignment.toString();
    }


}
