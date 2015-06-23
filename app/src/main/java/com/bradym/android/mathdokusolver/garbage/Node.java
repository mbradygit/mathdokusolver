package com.bradym.android.mathdokusolver.garbage;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Michael on 5/16/2015.
 */
public class Node {

    //private Container container;
    private Integer value;
    private VariableAssignment va;
    private Map<Integer, Node> children;
    private Node parent;
    private List<Node> cut;


    public Node(VariableAssignment va, Node parent) {
        this.va = va;
        this.parent = parent;
        this.parent.addChild(this);
        this.children = new HashMap<>();
    }

    public Node() {
        this.va = null;
        this.parent = null;
        this.children = new HashMap<>();
    }

//    public Node(Container container, Value value, Node parent) {
//        this.container = container;
//        this.value = value;
//        this.parent = parent;
//        this.parent.addChild(this);
//        this.pruned = new ArrayList<>();
//    }
//
//    public Node(Container container, Value value) {
//        this(container, value, null);
//    }

    public Node getParent() {
        return parent;
    }

    public void addChild(Node child) {
        this.children.put(child.getValue(), child);
    }

    public boolean hasValidChild(VariableAssignment childVA) {
        for (Node child : children.values()) {
            if (child.getVA().equals(childVA)) {
                return !child.isCut();
            }
        }
        return false;
    }

    public VariableAssignment getVA() {
        return va;
    }

//    public void setParent(Node parent) {
//        this.parent.addChild(this);
//        this.parent = parent;
//    }

//    public void addChild(Node<Container, Value> child) {
//        if (!children.containsKey(child.getValue())) {
//            children.put(child.getValue(), child);
//        }
//    }
//
    public Node hasChild(Integer child) {
        return children.get(child);
    }

    public void cut() {
        if (!this.va.isCut()) {
            this.va.cut();
        }
    }

    public boolean isCut() {
        return this.va.isCut();
    }

    public Stack<Node> findUncutPath () {

        if (children.isEmpty()) {
            return new Stack<Node>();
        }
        for (Node child : children.values()) {
            if (!child.isCut()) {
                Stack<Node> s = child.findUncutPath();
                if (s != null) {
                    s.push(this);
                    return s;
                }
            }
        }
        return null;
    }




//    public void restrictChildren(Integer v) {
//        List<Node> pruned = new ArrayList();
//        for (Map.Entry<Integer, Node> entry : children.entrySet()) {
//            if (!entry.getKey().equals(v)) {
//                pruned.add(entry.getValue());
//                children.remove(entry.getKey());
//            }
//        }
//        this.pruned.addAll(pruned);
//        if (children.isEmpty()) {
//            parent.removeChild(value);
//        }
//    }
//
//    public void removeChild(Integer v) {
//        List<Node> pruned = new ArrayList(1);
//        pruned.add(children.get(v));
//        children.remove(v);
//
//        if (children.isEmpty() && parent != null) {
//            parent.removeChild(value);
//        }
//        this.pruned.addAll(pruned);
//    }

//    public void restorePruned() {
//        for (Node<Container, Value> node : pruned) {
//            children.put(node.value, node);
//        }
//        pruned.clear();
//    }

    public Integer getValue() {
        return this.va.getValue();
    }

    public Collection<Node> getChildren() {
        return children.values();
    }

    public Node getRoot() {
        if (parent == null) {
            return this;
        } else {
            return parent.getRoot();
        }
    }

}
