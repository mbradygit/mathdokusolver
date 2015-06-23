package com.bradym.android.mathdokusolver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bradym.android.mathdokusolver.logic.DivConstraint;
import com.bradym.android.mathdokusolver.logic.MinusConstraint;
import com.bradym.android.mathdokusolver.logic.PlusConstraint;
import com.bradym.android.mathdokusolver.logic.ProdConstraint;
import com.bradym.android.mathdokusolver.logic.TrueConstraint;
import com.bradym.android.mathdokusolver.logic.TrueVariable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Michael on 6/19/2015.
 */
public class TrueDialog extends DialogFragment implements View.OnClickListener {

    private String buffer = "";
    private int operation = -1;
    private String constraintValue = "";

    private TextView field;

    private int plus = R.string.plus;
    private int times = R.string.times;
    private int div =  R.string.div;
    private int minus = R.string.minus;

    private TrueDialogListener trueListener;
    private List<TrueVariable> scope = new ArrayList();
    private HashSet<TrueCell> cells;

    private Button plusButton;
    private Button minusButton;
    private Button timesButton;
    private Button divButton;


    private int max;

    public void addParameters(HashSet<TrueCell> scope, int max) {
        for (TrueCell tc : scope) {
            this.scope.add(tc.getVariable());
        }
        this.max = max;
        this.cells = scope;
    }
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select the constraint");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //View inflated = inflater.inflate(R.layout.dialog_window, null);
        View inflated = inflater.inflate(R.layout.true_dialog, null);

        field = (TextView) inflated.findViewById(R.id.dialogTextView);
        inflated.findViewById(R.id.dialogButton0).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButton1).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButton2).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButton3).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButton4).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButton5).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButton6).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButton7).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButton8).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButton9).setOnClickListener(this);

        plusButton = (Button) inflated.findViewById(R.id.dialogButtonPlus);
        plusButton.setOnClickListener(this);

        timesButton = (Button) inflated.findViewById(R.id.dialogButtonTimes);
        timesButton.setOnClickListener(this);

        minusButton = (Button) inflated.findViewById(R.id.dialogButtonMinus);
        minusButton.setOnClickListener(this);

        divButton = (Button) inflated.findViewById(R.id.dialogButtonDiv);
        divButton.setOnClickListener(this);


        if (scope.size() == 1) {
            adjustButtons(true, plusButton);
            adjustButtons(false,timesButton, divButton, minusButton );
        } else if (scope.size() > 1) {
            adjustButtons(true, plusButton, timesButton, divButton, minusButton);
        }


        inflated.findViewById(R.id.dialogButtonUndo).setOnClickListener(this);

        return builder.setView(inflated).create();
    }

    public void adjustButtons(boolean enable, Button ... buttons) {
        for (Button b : buttons) {
            if (enable) {
                b.setClickable(true);
                b.setTextColor(b.getTextColors().withAlpha(255));
            } else {
                b.setClickable(false);
                b.setTextColor(b.getTextColors().withAlpha(64));

            }
        }
    }


    @Override
    public void onClick(View v) {
        TrueConstraint tc;

        v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
        switch (v.getId()) {
            case R.id.dialogButton0:
            case R.id.dialogButton1:
            case R.id.dialogButton2:
            case R.id.dialogButton3:
            case R.id.dialogButton4:
            case R.id.dialogButton5:
            case R.id.dialogButton6:
            case R.id.dialogButton7:
            case R.id.dialogButton8:
            case R.id.dialogButton9:
                buffer += ((TextView) v).getText();
                field.setText(buffer);
                break;
            case R.id.dialogButtonUndo:
                buffer = buffer.length() > 0 ? buffer.substring(0, buffer.length() - 1) : buffer;
                field.setText(buffer);

                break;
            case R.id.dialogButtonDiv:
                tc = new DivConstraint(Integer.parseInt(buffer), scope, max);
                trueListener.onPositiveClick(tc, cells);
                dismiss();
                break;
            case R.id.dialogButtonPlus:
                tc = new PlusConstraint(Integer.parseInt(buffer), scope, max);
                trueListener.onPositiveClick(tc, cells);
                dismiss();
                break;
            case R.id.dialogButtonMinus:
                tc = new MinusConstraint(Integer.parseInt(buffer), scope, max);
                trueListener.onPositiveClick(tc, cells);
                dismiss();
                break;
            case R.id.dialogButtonTimes:
                tc = new ProdConstraint(Integer.parseInt(buffer), scope, max);
                trueListener.onPositiveClick(tc, cells);
                dismiss();
                break;
        }
    }

    interface TrueDialogListener {
        void onPositiveClick(TrueConstraint tc, HashSet<TrueCell> cells);
        void onCancel();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            trueListener = (TrueDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void dismiss() {
        for (TrueCell tc : cells) {
            tc.setBackgroundColor(Color.TRANSPARENT);
        }
        cells.clear();
        super.dismiss();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        dismiss();
    }

}
