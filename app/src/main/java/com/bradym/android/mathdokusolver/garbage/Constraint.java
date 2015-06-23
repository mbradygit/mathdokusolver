package com.bradym.android.mathdokusolver.garbage;

import android.util.Log;

import com.bradym.android.mathdokusolver.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;

/**
* Created by Michael on 4/8/2015.
*/
public class Constraint {
    List<Variable> scope;
    //List<List<Integer>> tuples2;
    List<List<Integer>> prunedTuples = new ArrayList();
    Stack<List<List<Integer>>> rohit = new Stack();
    Stack<List<List<Integer>>> noSupports = new Stack();
    List<List<Node>> supportNodes = new ArrayList();
    private Map<Variable, Integer> indicies = new HashMap<>();
    AssignmentManager assignmentManager;
    int op;
    Integer value;
    String name = "";

    List<Node> supportsN = new ArrayList<>();


    public Constraint(List<Variable> scope, int op, Integer value, String name) {
        this.scope = scope;
        this.op = op;
        this.value = value;
        this.name = name;

        for (int i = 0; i < scope.size(); i++) {
            indicies.put(scope.get(i), i);
        }


//        for (Variable v : scope) {
//            List<Boolean> l = new ArrayList();
//            for (int i = 0; i < v.getDomain().size(); i++) {
//                l.add(null);
//            }
//        }

        assignmentManager = new AssignmentManager();


        for (Variable v : scope) {
            supportsN.add(new Node());
        }

        amazingN(scope, 0, new ArrayList<Integer>(), value, op, false );


        dfs(supportsN.get(0), "");
    }

    public void populate() {

    }

    public void addAssignments(List<AssignmentOLD> assignments) {
        assignmentManager.addAssignments(assignments);
    }

    public Constraint(List<Variable> scope, int op, Integer value) {
        this(scope, op, value, "");
    }

    public void addSatisfiers(List<List<Integer>> tuples) {
        this.rohit.push(tuples);
    }

    public List<List<Integer>> popSatisfiers() {
        return this.rohit.pop();
    }

    public List<List<Integer>> peekSatisfiers() {
        return this.rohit.peek();
    }

    public void filterAndPush(Variable v, Integer d) {
        int index = scope.indexOf(v);
        List<List<Integer>> newTuples = new ArrayList();
        for (List<Integer> tuple : rohit.peek()) {
            if (tuple.get(index).equals(d)) {
                newTuples.add(tuple);
            }
        }
        rohit.push(newTuples);

    }

    public void flagPruned(List<Integer> pruned) {
        prunedTuples.add(pruned);
    }

//    public void restorePruned() {
//        this.tuples2.addAll(prunedTuples);
//        prunedTuples.clear();
//    }

//    public int numUnassigned() {
//        int num = 0;
//        for (Variable var : scope) {
//            if (var.getValue() == null) {
//                num++;
//            }
//        }
//
//        return num;
//    }
//
//    public List<Variable> getUnassigned() {
//        List<Variable> unassigned = new ArrayList();
//        for (Variable var : scope) {
//            if (var.getValue() == null) {
//                unassigned.add(var);
//            }
//        }
//        return unassigned;
//    }

//    public boolean hasSupport(Variable v, Integer d) {
//        int i1 = scope.indexOf(v);
//        int i2 = v.getCurDomain().indexOf(d);
//
//        if (hasSupports.get(i1).get(i2) == false) {
//            return false;
//        } else {
//            for (List<Integer> tuple : rohit.peek()) {
//                if (tuple.get(i1).equals(d)) {
//                    return true;
//                }
//            }
//            hasSupports.get(i1).set(i2, false);
//            return false;
//        }
//    }

    public int numCurrentUnassigned() {
        int num = 0;
        for (Variable var : scope) {
            if (var.getCurValue() == null) {
                num++;
            }
        }

        return num;
    }

    public String getName() {
        return name;
    }

    public void fixVariable(Variable var, Integer val) {
        assignmentManager.restrict(indicies.get(var), val);
    }

    public boolean hasAssignment(Variable var, Integer val) {
        return assignmentManager.hasAssignment(indicies.get(var), val);
    }

    private void amazingN(List<Variable> vars, int index, List<Integer> partial, float rem, int op, boolean first) {
        Variable var = vars.get(index);
        for (Integer val : var.getDomain()) {
            List<Integer> newPartial = new ArrayList(partial);
            newPartial.add(val);
            if (index < vars.size() - 1) {
                switch (op) {
                    case R.string.plus:
                        if (rem - val > 0) {
                            amazingN(vars, index + 1, newPartial, rem - val, op, first);
                        }
                        break;
                    case R.string.minus:
                        if (!first || (first && rem + val < 0)) {
                            amazingN(vars, index + 1, newPartial, rem + val, op, first);
                        }
                        if (!first && (rem - val < 0))
                            amazingN(vars, index + 1, newPartial, rem - val, op, true);
                        break;
                    case R.string.times:
                        if (rem / val >= 1) {
                            amazingN(vars, index + 1, newPartial, rem / val, op, first);
                        }
                        break;
                    case R.string.div:
                        if (!first || (first && rem * val <= 1)) {
                            amazingN(vars, index + 1, newPartial, rem * val, op, first);
                        }
                        if (!first && (rem / val <= 1))
                            amazingN(vars, index + 1, newPartial, rem / val, op, true);
                        break;
                }

            } else if (index == vars.size() - 1) {
                switch (op) {
                    case R.string.plus:
                    case R.string.equals:
                        if (rem == val) {
                            populateTrees(newPartial);
                        }
                        break;
                    case R.string.minus:
                        if (!first) {
                            if (rem == val) {
                                populateTrees(newPartial);
                            }
                        } else {
                            if (rem == -val) {
                                populateTrees(newPartial);
                            }
                        }
                        break;
                    case R.string.times:
                        if (rem / val == 1) {
                            populateTrees(newPartial);
                        }
                        break;
                    case R.string.div:
                        if (!first) {
                            if (Math.abs(rem / val  - 1) <= 0.01) {
                                populateTrees(newPartial);
                            }
                        } else {
                            if (Math.abs(rem * val - 1) <= 0.01) {
                                populateTrees(newPartial);
                            }
                        }
                        break;
                }
            }
        }
    }

    public static void amazing2N(List<Integer> remaining, List<Constraint> constraints, List<Integer> partial) {

        if (remaining.size() == 0) {
            for (int i = 0; i < constraints.size(); i++) {
                List<Variable> scope = constraints.get(i).scope;
                List<Integer> support = new ArrayList<>();
                for (int j = 0; j < partial.size(); j++) {
                    support.add(scope.get(j).getDomain().get(partial.get(j)));
                }
                //constraints.get(i).populateTrees(support);
            }
        } else {
            ListIterator<Integer> iterator = remaining.listIterator();

            while (iterator.hasNext()) {
                Integer i = iterator.next();
                iterator.remove();
                partial.add(i);
                amazing2N(new LinkedList(remaining), constraints, partial);
                iterator.add(i);
                partial.remove(i);
            }

        }
    }


    public void populateTrees(List<Integer> support) {
        int size = supportsN.size();
        for (int i = 0; i < size; i++) {
            Log.d("TIME", System.currentTimeMillis() + "");
            Node parent = supportsN.get(i);
            for (int j = i; j < size + i; j++) {
                int index = j % size;
                Integer val = support.get(index);
                Node t = parent.hasChild(val);
                if (t != null) {
                    parent = t;
                } else {
                    parent = new Node(scope.get(index).getDomainN().get(val), parent);
                }
            }
        }
    }

    public void dfs(Node node, String sb) {
//        if (node.getChildren().size() == 0) {
//            Log.d("NODEY" , sb.toString() + "\n");
//        }
//
//        for (Node c : node.getChildren()) {
//            dfs(c, sb + c.getVA().getValue());
//        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Variable v : scope) {
            s.append(v.getIndex() + " ");
        }

        s.append(": " + name);

        return s.toString();
    }
}


