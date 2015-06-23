package com.bradym.android.mathdokusolver.garbage;

import android.util.Log;

import com.bradym.android.mathdokusolver.logic.TrueConstraint;
import com.bradym.android.mathdokusolver.logic.TrueVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Michael on 5/1/2015.
 */
public class Solver {

    private List<Constraint> constraints = new ArrayList();
    private List<TrueConstraint> constraintsN = new ArrayList();
    private PriorityQueue<Variable> unassigned;
    private PriorityQueue<TrueVariable> unassignedN;

    private Queue<Constraint> GACQueue = new LinkedList();
    private Queue<TrueConstraint> GACQueueN = new LinkedList();
    private Variable[] variables;
    private TrueVariable[] variablesN;
    private long timestamp;

    private long pruningDomainTime = 0L;
    private long pruningScopeTime = 0L;
    private long unassignTime = 0L;
    private long restorePrunedTime = 0L;
    private long assignmentCheckTime = 0L;

    private Comparator<Variable> comparator = new Comparator<Variable>() {
        @Override
        public int compare(Variable lhs, Variable rhs) {
                if (lhs.getCurDomain().size() < rhs.getCurDomain().size()) {
                    return -1;
                } else if (lhs.getCurDomain().size() > rhs.getCurDomain().size()) {
                    return 1;
                } else {
                    return 0;
                }
        }
    };


    public Solver(Variable[] variables, List<Constraint> constraints) {
        this.constraints.addAll(constraints);
        this.unassigned = new PriorityQueue(variables.length, comparator);
        this.unassigned.addAll(Arrays.asList(variables));
        this.variables = variables;
    }

    public Solver(TrueVariable[] variables, List<TrueConstraint> constraints) {
        this.constraintsN.addAll(constraints);
        this.unassignedN = new PriorityQueue(variables.length, comparator);
        this.unassignedN.addAll(Arrays.asList(variables));
        this.variablesN = variables;
    }

    public void solveGAC() {
        GAC();
        GACQueue.clear();
        Log.d("PRUNING DOMAIN TIME", pruningDomainTime + "");
        Log.d("PRUNING SCOPE TIME", pruningScopeTime + "");
        Log.d("UNASSIGN TIME", unassignTime + "");
        Log.d("RESTORE PRUNED TIME", restorePrunedTime + "");
        Log.d("ASSIGNMENT CHECK TIME", assignmentCheckTime + "");
    }



    private boolean enforce_GAC(Queue<Constraint> GACQueue) {
        while (!GACQueue.isEmpty()) {
            Constraint constraint = GACQueue.poll();
            //Log.d("ENFORCE_GAC", "CONSTRAINT " + constraint.getName());

            for (int i = 0; i < constraint.scope.size(); i++) {
                Variable v = constraint.scope.get(i);
                //Log.d("ENFORCE_GAC", "VAR " + v.getIndex());
                Iterator<Integer> iterator = v.getCurDomain().iterator();

                while (iterator.hasNext()) {
                    Integer d = iterator.next();
                    timestamp = System.currentTimeMillis();
                    boolean found = constraint.hasAssignment(v,d);
                    assignmentCheckTime += (System.currentTimeMillis() - timestamp);
                    if (!found) {
                        //Log.d("ENFORCE_GAC", "VALUE " + d + " NOT FOUND IN TUPLES ");
                        iterator.remove();
                        v.flagPruned(d);
                        if (v.getCurDomain().isEmpty()) {
                            //Log.d("ENFORCE_GAC","CUR DOMAIN NOW EMPTY");
                            GACQueue.clear();
                            return false;
                        } else {
                            timestamp = System.currentTimeMillis();
                            for (Constraint c : constraints) {
                                if (c.scope.contains(v) && !GACQueue.contains(c)) {
                                    //Log.d("ENFORCE_GAC","CONSTRAINT " + c.getName() + " ADDED TO Q");
                                    GACQueue.add(c);
                                }
                            }

                        }
                    } else {
                        //Log.d("ENFORCE_GAC", "VALUE " + d + " FOUND IN TUPLES ");
                    }
                }
                //Log.d("ENFORCE_GAC", "CONSTRAINT " + constraint.getName() +  " REDUCED DOMAIN" + v.getCurDomain().toString());
            }
        }

        return true;
    }

//    private boolean enforce_GACN(Queue<TrueConstraint> GACQueue) {
//        while (!GACQueue.isEmpty()) {
//            TrueConstraint constraint = GACQueue.poll();
//            //Log.d("ENFORCE_GAC", "CONSTRAINT " + constraint.getName());
//
//
//            for (int i = 0; i < constraint.scope.size(); i++) {
//                Variable v = constraint.scope.get(i);
//
//                Iterator<VariableAssignment> iterator = v.getDomainN().values().iterator();
//
//                while (iterator.hasNext()) {
//                    VariableAssignment va = iterator.next();
//                    Node root = constraint.supportsN.get(i);
//                    if (!root.hasValidChild(va)) {
//                        iterator.remove();
//                        v.updateCut(va);
//
//                        if (v.getDomainN().isEmpty()) {
//                            GACQueue.clear();
//                        } else {
//                            for (Constraint c : constraints) {
//                                if (c.scope.contains(v) && !GACQueue.contains(c)) {
//                                    GACQueue.offer(c);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return true;
//    }

    private boolean GAC() {
        boolean allAssigned = unassigned.isEmpty();
        if (allAssigned) {
            Log.d("ALL ASSIGNED", "MUCH WOW");
            for (Variable v : variables) {
                Log.d("VARIABLE " + v.getIndex(), "VALUE " + v.getValue());
            }
            return true;
        }
        Variable v = unassigned.poll();
        Log.d("VARIABLE @ INDEX ", v.getIndex() + "");
        for (Integer d : new ArrayList<>(v.getCurDomain())) {
            List<Constraint> relevantConstraints = assignVariableValue(v,d);

            Log.d("NEW VAR " + v.getIndex() + " VALUE " + v.getValue() + " DOMAIN", v.getCurDomain().toString());

            if (enforce_GAC(GACQueue)) {
                Log.d("ENFORCE GC ", "COMPLETE");

                if (GAC()) {
                    return true;
                }
            } else {
                Log.d("ENFORCE GC ", "FAIL");
            }

            timestamp = System.currentTimeMillis();
            for (Constraint c : relevantConstraints) {
                c.assignmentManager.unassign();
            }
            unassignTime += (System.currentTimeMillis() - timestamp);
            timestamp = System.currentTimeMillis();
            for (Variable p : variables) {
                p.restorePruned();
            }
            restorePrunedTime += (System.currentTimeMillis() - timestamp);

            Log.d("RESTORED VAR " + v.getIndex() + " DOMAIN", v.getCurDomain().toString());
        }
        v.setValue(null);
        unassigned.offer(v);
        return false;
    }

    private boolean GACN() {
        boolean allAssigned = unassigned.isEmpty();
        if (allAssigned) {
            Log.d("ALL ASSIGNED", "MUCH WOW");
            for (Variable v : variables) {
                Log.d("VARIABLE " + v.getIndex(), "VALUE " + v.getValue());
            }
            return true;
        }
        Variable v = unassigned.poll();
        Log.d("VARIABLE @ INDEX ", v.getIndex() + "");
        for (VariableAssignment va : v.getDomainN().values()) {
            assignVariableValue(va);

            Log.d("NEW VAR " + v.getIndex() + " VALUE " + v.getValue() + " DOMAIN", v.getCurDomain().toString());

//            if (enforce_GACN(GACQueue)) {
//                Log.d("ENFORCE GC ", "COMPLETE");
//
//                if (GACN()) {
//                    return true;
//                }
//            } else {
//                Log.d("ENFORCE GC ", "FAIL");
//            }

            timestamp = System.currentTimeMillis();
            for (Constraint c : constraints) {
                if (c.scope.contains(va.getVariable())) {
                    c.assignmentManager.unassign();
                }
            }
            unassignTime += (System.currentTimeMillis() - timestamp);
            timestamp = System.currentTimeMillis();
            for (Variable p : variables) {
                p.restorePruned();
            }
            restorePrunedTime += (System.currentTimeMillis() - timestamp);

            Log.d("RESTORED VAR " + v.getIndex() + " DOMAIN", v.getCurDomain().toString());
        }
        v.setValue(null);
        unassigned.offer(v);
        return false;
    }

    public List<Constraint> assignVariableValue(Variable v, Integer d) {
        timestamp = System.currentTimeMillis();
        v.setValue(d);
        v.pruneAllExcept(d);
        pruningDomainTime += (System.currentTimeMillis() - timestamp);

        timestamp = System.currentTimeMillis();
        List<Constraint> modified = new ArrayList<>();
        for (Constraint c : constraints) {
            if (c.scope.contains(v)) {
                c.fixVariable(v, d);
                modified.add(c);
                GACQueue.offer(c);
            }
        }
        pruningScopeTime += (System.currentTimeMillis() - timestamp);
        return modified;
    }

    public void assignVariableValue(VariableAssignment va) {
        Variable v = va.getVariable();
        for (VariableAssignment ass : v.getDomainN().values()) {
            if (!v.equals(va)) {
                v.cut(va);
            }
        }
    }


}
