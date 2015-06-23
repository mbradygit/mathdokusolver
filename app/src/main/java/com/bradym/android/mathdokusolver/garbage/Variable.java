package com.bradym.android.mathdokusolver.garbage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael on 4/8/2015.
 */
public class Variable extends Object {

    private List<Integer> domain;
    private List<Integer> curDomain;
    private List<Integer> pruned;
    private int value = -1;
    private Integer curValue = null;
    private boolean constrained = false;
    private int index;
    private BorderedLayout attachedCell = null;



    private Map<Integer, VariableAssignment> domainN;
    private List<VariableAssignment> cutN;
    private VariableAssignment valueN;

    public Variable(List<Integer> domain, int index) {
        this.domain = domain;
        this.curDomain = new ArrayList(domain);
        this.index = index;
        this.pruned = new ArrayList();
        this.domainN = new HashMap<>();
        this.cutN = new ArrayList<>();
        for (Integer i : domain) {
            domainN.put(i,new VariableAssignment(this, i));
        }
    }

//    public void assignN(VariableAssignment va) {
//        Iterator<VariableAssignment> iterator = domainN.iterator();
//        valueN = va;
//        while (iterator.hasNext()) {
//            VariableAssignment v = iterator.next();
//            if (v != va) {
//                v.cut();
//                cutN.add(v);
//                iterator.remove();
//            }
//        }
//    }

//    public void unassignN() {
//        Iterator<VariableAssignment> iterator = cutN.iterator();
//        valueN = null;
//        while (iterator.hasNext()) {
//            VariableAssignment v = iterator.next();
//            v.uncut();
//            domainN.add(v);
//            iterator.remove();
//        }
//    }

    public void setValue(Integer value) {
        this.curValue = value;
        this.value = value;
    }

    public void cut(VariableAssignment va) {
        domainN.remove(va.getValue());
        updateCut(va);
    }

    public void updateCut(VariableAssignment va) {
        cutN.add(va);
        va.cut();
    }

    public void pruneAllExcept(Integer d) {
        Iterator<Integer> iterator = this.getCurDomain().iterator();
        while (iterator.hasNext()) {
            Integer p = iterator.next();
            if (!p.equals(d)) {
                pruned.add(p);
                iterator.remove();
            }
        }
    }


    public boolean hasAssignment(VariableAssignment ass) {
        return domainN.values().contains(ass);
    }

    public void setCell(BorderedLayout btv) {
        attachedCell = btv;
    }

    public BorderedLayout getCell() {
        return attachedCell;
    }

    public void setCurValue(Integer value) {
        this.curValue = value;
    }

    public int getValue() {
        return this.value;
    }

    public Integer getCurValue() { return this.curValue; }

    public List<Integer> getDomain() {
        return domain;
    }

    public List<Integer> getCurDomain() {
        return curDomain;
    }

    public Map<Integer, VariableAssignment> getDomainN() {
        return domainN;
    }

    public boolean isConstrained() {

        return constrained;
    }

    public void constrain() {
        constrained = true;
    }

    public int getIndex() {
        return index;
    }

    public void flagPruned(Integer p) {
        pruned.add(p);
    }

    public void prune(Integer p) {
        pruned.add(p);
        curDomain.remove(p);
    }

    public void restorePruned() {
        curDomain.addAll(pruned);
        pruned.clear();
    }

}
