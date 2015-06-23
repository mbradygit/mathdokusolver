package com.bradym.android.mathdokusolver.logic;

import android.util.Log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;

/**
 * Created by Michael on 6/7/2015.
 */
public class TrueSolver {

    final Deque<TrueVariable> unassigned;
    final Cupe cupe;
    final Queue<TrueConstraint> GACQueue = new ArrayDeque<>();


    private long updateVariableTime = 0L;
    private long validateTime = 0L;
    private long enforceGACTime = 0L;
    private long restoreTime = 0L;
    private long valueCleanUpTime = 0L;

    public TrueSolver(TrueVariable[] variables) {
        this.unassigned = new ArrayDeque<>(variables.length);
        this.unassigned.addAll(Arrays.asList(variables));
        this.cupe = new Cupe(variables, variables[0].domain.size());
    }

    public void solveGAC() {
        GAC();
        GACQueue.clear();
    }

    private boolean GAC() {
        long s;
        boolean allAssigned = cupe.pls.size() == 0;
        if (allAssigned) {
            Log.d("ALL ASSIGNED", "MUCH WOW");
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
                    Integer d = iterator.next();

                    s = System.currentTimeMillis();
                    boolean found = constraint.validate(v, d);

                    if (!found) {
                        //Log.d("VARIABLE: " + v, "VALUE " + d + " INVALID");
                        iterator.remove();
                        v.flagPruned(d);
                        alteredV.add(v);
                        if (v.domain.isEmpty()) {
                            GACQueue.clear();
                            return false;
                        } else {
                            for (TrueConstraint c : v.constraints) {
                                if (!GACQueue.contains(c)) {
                                    GACQueue.add(c);
                                }
                            }
                        }
                    }
                }
            }
        }

        for (TrueVariable tv : alteredV) {
            tv.lockIn();
            cupe.update(tv);
            //Log.d("UPDATED VARIABLE " + tv + " DOMAIN", tv.domain.toString());
        }
        return true;
    }

}
