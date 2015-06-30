package com.bradym.android.mathdokusolver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
 *
 * Dialog that allows the user to specify the constraint corresponding to the selected cells
 */
public class TrueDialog extends DialogFragment implements View.OnClickListener {

    private String buffer = "";

    private TextView field;

    private TrueDialogListener trueListener;
    private List<TrueVariable> scope = new ArrayList<>();
    private HashSet<TrueCell> cells;

    Button plusButton;
    Button minusButton;
    Button timesButton;
    Button divButton;

    ImageButton trashButton;


    private int max;
    private int action;
    private TrueConstraint prevConstraint;
    private ConstraintState newState = null;

    public void addParameters(HashSet<TrueCell> scope, int max, int action) {
        for (TrueCell tc : scope) {
            this.scope.add(tc.getVariable());
        }
        this.max = max;
        this.cells = scope;
        this.action = action;
        this.newState = new ConstraintState(action);
    }

    public void addParameters(TrueConstraint tc, int action) {
        this.prevConstraint = tc;
        this.action = action;
        this.newState = new ConstraintState(action, tc);
        this.scope = tc.scope;
    }
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select the constraint");

        View inflated = View.inflate(getActivity(), R.layout.true_dialog, null);

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

        trashButton = (ImageButton) inflated.findViewById(R.id.deleteButton);
        trashButton.setOnClickListener(this);




        adjustButtons(false, plusButton, timesButton, divButton, minusButton);

        if (action != ConstraintState.EDIT) {
            adjustButtons(false, trashButton);
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

    public void adjustButtons(boolean enable, ImageButton ... buttons) {
        for (ImageButton b : buttons) {
            if (enable) {
                b.setClickable(true);
                b.setAlpha(1.0f);
            } else {
                b.setClickable(false);
                b.setAlpha(0.25f);


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

                if (buffer.length() < 9) {
                    buffer += ((TextView) v).getText();
                    field.setText(buffer);
                }

                if (buffer.length() == 1) {
                    if (scope.size() == 1) {
                        adjustButtons(true, plusButton);
                    } else if (scope.size() > 1) {
                        adjustButtons(true, plusButton, timesButton, divButton, minusButton);
                    }
                }



                break;
            case R.id.dialogButtonUndo:
                buffer = buffer.length() > 0 ? buffer.substring(0, buffer.length() - 1) : buffer;
                field.setText(buffer);

                if (buffer.length() == 0) {
                    adjustButtons(false,plusButton, timesButton, divButton, minusButton );
                }

                break;
            case R.id.dialogButtonDiv:
                tc = new DivConstraint(Integer.parseInt(buffer), scope, max);
                //trueListener.onPositiveClick(tc, cells);
                newState.addConstraints(tc);
                trueListener.onPositiveClick(newState);
                dismiss();
                break;
            case R.id.dialogButtonPlus:
                tc = new PlusConstraint(Integer.parseInt(buffer), scope, max);
                newState.addConstraints(tc);
                trueListener.onPositiveClick(newState);
                dismiss();
                break;
            case R.id.dialogButtonMinus:
                tc = new MinusConstraint(Integer.parseInt(buffer), scope, max);
                newState.addConstraints(tc);
                trueListener.onPositiveClick(newState);
                dismiss();
                break;
            case R.id.dialogButtonTimes:
                tc = new ProdConstraint(Integer.parseInt(buffer), scope, max);
                newState.addConstraints(tc);
                trueListener.onPositiveClick(newState);
                dismiss();
                break;
            case R.id.deleteButton:
                newState.setAction(ConstraintState.DELETE);
                trueListener.onPositiveClick(newState);
                dismiss();
                break;
        }
    }

    interface TrueDialogListener {
        void onPositiveClick(ConstraintState cs);
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
        switch (action) {
            case ConstraintState.EDIT:
                for (TrueVariable tv : prevConstraint.scope) {
                    tv.cell.setBackgroundColor(Color.TRANSPARENT);
                }
                break;

            case ConstraintState.DELETE:
                break;

            case ConstraintState.ADD:
                for (TrueCell tc : cells) {
                    tc.setBackgroundColor(Color.TRANSPARENT);
                }
                cells.clear();
                break;
        }


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
