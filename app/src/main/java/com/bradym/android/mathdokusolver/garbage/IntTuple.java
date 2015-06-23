package com.bradym.android.mathdokusolver.garbage;


import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Michael on 4/28/2015.
 */
public class IntTuple {

    private List<Integer> elements;
    private int index;

    public IntTuple(int size) {
        elements = Arrays.asList(new Integer[size]);
        index = size - 1;
    }

    public IntTuple(int size, Integer element) {
        this(size);
        Log.d("SIZE", size + "");
        add(element);
    }

    public void add(Integer element) {
        if (index > -1) {
            elements.set(index, element);
            index--;
        }
    }


    public List<Integer> getElements() {
        return elements;
    }

    public Integer elementAt(int index) {
        return elements.get(index);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer elmt : elements) {
            sb.append(elmt + " ");
        }
        return sb.toString();
    }



}
