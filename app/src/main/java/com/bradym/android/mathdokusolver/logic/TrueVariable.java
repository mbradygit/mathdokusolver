package com.bradym.android.mathdokusolver.logic;

import com.bradym.android.mathdokusolver.TrueCell;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Michael on 6/3/2015.
 */
public class TrueVariable {

    public final String name;
    public final List<TrueConstraint> constraints = new ArrayList<>();
    public final List<Integer> domain = new ArrayList<>();
    public final Deque<List<Integer>> pruned = new ArrayDeque<>();
    List<Integer> toPush = new ArrayList<>();

    public final TrueCell cell;

    public int value = -1;

    public TrueVariable(String name, List<Integer> domain, TrueCell cell) {
        this.name = name;
        this.domain.addAll(domain);
        this.cell = cell;
    }

    public void prune(Integer val) {
        domain.remove(val);
        flagPruned(val);
    }

    public void flagPruned(Integer val) {
        toPush.add(val);
        //pruned.add(val);
    }

    public void assign(Integer val) {
        this.value = val;
        pruneAllExcept(val);
    }

    public void pruneAllExcept(Integer val) {
        List<Integer> list = new ArrayList<>();
        Iterator<Integer> iterator = domain.iterator();
        while (iterator.hasNext()) {
            Integer i = iterator.next();
            if (!i.equals(val)) {
                list.add(i);
                iterator.remove();
            }
        }

        pruned.addLast(list);

    }

    public void lockIn() {
        pruned.addLast(toPush);
        toPush = new ArrayList<>();
    }

    public void restore() {
        if (toPush.isEmpty()) {
            for (Integer i : pruned.pollLast()) {
                domain.add(i);
            }
        } else {
            for (Integer i : toPush) {
                domain.add(i);
            }
            toPush.clear();
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public String detail() {
        return "NAME: " + name + " DOMAIN: " + domain + " VALUE " + value;
    }

}
