package com.bradym.android.mathdokusolver.garbage;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Michael on 5/9/2015.
 */
public class AssignmentManager {

    private List<AssignmentOLD> assignments;
    private Stack<List<AssignmentOLD>> prunedStack;
    private Multimap<Integer, Integer> map;

    private List<Map<Integer, AssignmentOLD>> rohito;

    public AssignmentManager() {
        this.assignments = new ArrayList<>();
        this.prunedStack = new Stack();
        this.map = HashMultimap.create();
    }

    public void addAssignments(List<AssignmentOLD> assignments) {
        this.assignments.addAll(assignments);


    }

    public void restrict(int index, Integer val) {
        List<AssignmentOLD> pruned = new ArrayList<>();
        Iterator<AssignmentOLD> iterator = assignments.iterator();
        while (iterator.hasNext()) {
            AssignmentOLD a = iterator.next();
            if (!a.getValueOf(index).equals(val)) {
                pruned.add(a);
                iterator.remove();
            }
        }
        this.prunedStack.push(pruned);
    }

    public boolean hasAssignment(int index, Integer val) {
        if (map.containsEntry(index, val)) {
            return false;
        }
        for (AssignmentOLD a : assignments) {
            if (a.getValueOf(index).equals(val))
                return true;
        }
        map.put(index, val);
        return false;
    }

    public void unassign() {
        if (!prunedStack.empty()) {
            assignments.addAll(prunedStack.pop());
        }
    }

    public List<AssignmentOLD> getAssignments() {
        return this.assignments;
    }

}
