package com.bradym.android.mathdokusolver.logic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * Created by Michael on 6/7/2015.
 *
 * Solves the puzzle using a slightly modified GAC algorithm
 *
 */
public class TrueSolver {

    final List<TrueVariable> variables;
    final Cupe cupe;
    final Queue<TrueConstraint> GACQueue = new ArrayDeque<>();

    long solveTime = 0L;

    long updateVariableTime = 0L;
    long validateTime = 0L;
    long enforceGACTime = 0L;
    long restoreTime = 0L;
    long valueCleanUpTime = 0L;

    long enforceGACValidateTime = 0L;
    long enforceGACPruneTime = 0L;
    long enforceGACCleanUpTime = 0L;

    public TrueSolver(TrueVariable[] variables) {
        this.variables = new ArrayList<>(variables.length);
        this.variables.addAll(Arrays.asList(variables));
        this.cupe = new Cupe(variables, variables[0].domain.size());
    }

    public long solveGAC() {
        long s = System.currentTimeMillis();
        boolean success = GAC();
        solveTime = System.currentTimeMillis() - s;

//        Log.d("UPDATE VARIABLE TIME", updateVariableTime + "");
//        Log.d("VALIDATE TIME", validateTime + "");
//        Log.d("ENFORCE GAC TIME", enforceGACTime + "");
//        Log.d("RESTORE TIME", restoreTime + "");
//        Log.d("VALUE CLEANUP TIME", valueCleanUpTime + "");
//
//        Log.d("ENFORCE GAC VALIDATE", enforceGACValidateTime + "");
//        Log.d("ENFORCE GAC PRUNE", enforceGACPruneTime + "");
//        Log.d("ENFORCE GAC CLEAN UP", enforceGACCleanUpTime + "");
//
//        Log.d("FULL SOLVE", solveTime + "");
        GACQueue.clear();


        if(success == false) {
            return -1;
        } else {
            for (TrueVariable tv : variables) {
                tv.completeRestore();
                for (TrueConstraint tc : tv.constraints) {
                    tc.restore();
                }
            }
            return solveTime;
        }
    }

    private boolean GAC() {
        long s;
        boolean allAssigned = cupe.pls.size() == 0;
        if (allAssigned) {
            //Log.d("ALL ASSIGNED", "MUCH WOW");
            return true;
        }

        TrueVariable v = cupe.dequeue();
        //Log.d("POLLING VARIABLE", v.detail());
        for (Integer d : new ArrayList<>(v.domain)) {
            v.assign(d);
            //Log.d("VARIABLE STATUS", v.detail());
            boolean valid = true;
            ArrayList<TrueConstraint> alteredC = new ArrayList<>(v.constraints.size());

            for (TrueConstraint tc : v.constraints) {

                valid = tc.updateVariable(v);

                if (!valid) {
                    //Log.d("CONSTRAINT INVALIDATED:", tc.toString() );
                    break;
                } else {
                    alteredC.add(tc);
                    //Log.d("UPDATED CONSTRAINT", tc.toString());
                }
            }

            if (valid) {

                for (TrueConstraint tc : v.constraints) {
                    GACQueue.add(tc);
                }

                HashSet<TrueVariable> set = new HashSet<>();
                s = System.currentTimeMillis();
                if (enforce_GAC(GACQueue, set)) {
                    //Log.d("ENFORCE GAC ", "COMPLETE");
                    enforceGACTime += (System.currentTimeMillis() - s);
                    if (GAC()) {
                        return true;
                    }
                } else {
                    enforceGACTime += (System.currentTimeMillis() - s);
                }
                s = System.currentTimeMillis();
                for (TrueVariable tv : set) {
                    tv.restore();
                    cupe.update(tv);
                }
                restoreTime += (System.currentTimeMillis() - s);
            }

            s = System.currentTimeMillis();
            v.restore();
            v.value = -1;

            for (TrueConstraint c : alteredC) {
                c.pop();
            }
            valueCleanUpTime += (System.currentTimeMillis() - s);
        }
        cupe.enqueue(v);
        return false;
    }

    private boolean enforce_GAC(Queue<TrueConstraint> GACQueue, HashSet<TrueVariable> alteredV) {
        long s;
        while (!GACQueue.isEmpty()) {
            TrueConstraint constraint = GACQueue.poll();
            //Log.d("ENFORCE_GAC", "CONSTRAINT " + constraint);

            for (int i = 0; i < constraint.scope.size(); i++) {
                TrueVariable v = constraint.scope.get(i);
                //Log.d("ENFORCE_GAC", "VARIABLE " + v.detail());
                Iterator<Integer> iterator = v.domain.iterator();

                while (v.value == -1 && iterator.hasNext()) {
                    s = System.currentTimeMillis();
                    Integer d = iterator.next();
                    boolean found = constraint.validate(v, d);
                    enforceGACValidateTime += (System.currentTimeMillis() - s);

                    if (!found) {
                        //Log.d("VARIABLE: " + v, "VALUE " + d + " INVALID");
                        s = System.currentTimeMillis();
                        iterator.remove();
                        v.flagPruned(d);
                        alteredV.add(v);
                        if (v.domain.isEmpty()) {
                            enforceGACPruneTime += (System.currentTimeMillis() - s);
                            GACQueue.clear();
                            return false;
                        } else {
                            for (TrueConstraint c : v.constraints) {
                                if (!GACQueue.contains(c)) {
                                    GACQueue.add(c);
                                }
                            }
                            enforceGACPruneTime += (System.currentTimeMillis() - s);
                        }

                    }
                }
            }
        }

        s = System.currentTimeMillis();
        for (TrueVariable tv : alteredV) {
            tv.lockIn();
            cupe.update(tv);
            //Log.d("UPDATED VARIABLE " + tv + " DOMAIN", tv.domain.toString());
        }
        enforceGACCleanUpTime += (System.currentTimeMillis() - s);
        return true;
    }

}
