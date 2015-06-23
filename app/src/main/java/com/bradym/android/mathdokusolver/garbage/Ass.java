package com.bradym.android.mathdokusolver.garbage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael on 6/3/2015.
 */
public class Ass {

    private int size;
    boolean complete = false;
    int sum;
    int product;
    List<Integer> differences = new ArrayList<>();
    List<Fraction> quotients = new ArrayList<>();
    List<Integer> set = new ArrayList<>();

    Map<Variable, Integer> ass;

    public Ass(int ass, int size) {
        differences = new ArrayList<>(size);
        quotients = new ArrayList<>(size);
        set = new ArrayList<>(size);

        this.ass = new HashMap<>();

        size = 1;
        sum = ass;
        product = ass;
        differences.add(ass);
        quotients.add(new Fraction(ass, 1));
        for (int i = 1; i < size; i++) {
            differences.add(-ass);
            quotients.add(new Fraction(1, ass));
        }
    }

    public void assign(int ass) {
        sum += ass;
        product *= ass;
        for (int i = 0; i < set.size(); i++) {
            if (i == size) {
                differences.add(differences.get(i) + ass);
                quotients.get(i).num = ass;
            } else {
                differences.add(differences.get(i) - ass);
                quotients.get(i).den *= ass;
            }
        }
        size++;
        if (size == set.size()) {
            complete = true;
        }
    }

    public void assign(Variable var, int val) {
        this.ass.put(var,val);
    }

    public int getSize() {
        return size;
    }

    private class Fraction {
        int num;
        int den;

        public Fraction(int n, int d) {
            this.num = n;
            this.den = d;
        }
    }

}
