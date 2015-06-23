package com.bradym.android.mathdokusolver.garbage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 5/16/2015.
 */
public class TrueNode {

    final Integer value;
    List<TrueNode> children;

    public TrueNode() {
        this(null);
    }

    public TrueNode(Integer val) {
        this.value = val;
        this.children = new ArrayList<>();
    }

    public TrueNode(Integer val, TrueNode parent) {
        this(val);
        parent.addChild(this);
    }

    public void addChild(TrueNode child) {
        this.children.add(child);
    }

    public void depthFirstSearch(String partial) {
        if (children.isEmpty()) {
            //Log.d("TrueNode", partial + value);
        }
        for (TrueNode child : children) {
            child.depthFirstSearch(partial + (value == null ? "" : value));
        }
    }

}
