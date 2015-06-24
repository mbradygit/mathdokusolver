package com.bradym.android.mathdokusolver.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Michael on 6/14/2015.
 *
 * Queue backed by HashSet and HashMap
 */
public class Cupe {

    List<HashSet<TrueVariable>> cupe;
    HashMap<TrueVariable, Integer> pls;

    public Cupe(TrueVariable[] tvs, int max) {
        cupe = new ArrayList<>();
        pls = new HashMap<>();
        for (int i = 0; i < max; i++) {
            cupe.add(i, new HashSet<TrueVariable>());
        }

        for (TrueVariable tv : tvs) {
            cupe.get(tv.domain.size() - 1).add(tv);
            pls.put(tv, tv.domain.size() - 1);
        }
    }

    public void update(TrueVariable tv) {
        Integer i;
        if ((i=pls.get(tv)) != null) {
            cupe.get(i).remove(tv);
            cupe.get(tv.domain.size() - 1).add(tv);

            pls.remove(tv);
            pls.put(tv, tv.domain.size() - 1);
        }
    }

    public TrueVariable dequeue() {
        for (HashSet<TrueVariable> hs : cupe) {
            if (!hs.isEmpty()) {
                Iterator<TrueVariable> iterator = hs.iterator();
                TrueVariable tv = iterator.next();
                iterator.remove();
                pls.remove(tv);
                return tv;
            }
        }
        return null;
    }

    public void enqueue(TrueVariable tv) {
        cupe.get(tv.domain.size() - 1).add(tv);
        pls.put(tv, tv.domain.size() - 1);
    }

}
