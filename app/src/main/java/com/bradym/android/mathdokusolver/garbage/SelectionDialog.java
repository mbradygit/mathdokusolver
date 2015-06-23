package com.bradym.android.mathdokusolver.garbage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bradym.android.mathdokusolver.R;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectionDialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectionDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectionDialog extends DialogFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private String buffer = "";

    private int operation = R.string.equals;
    private int value = 0;
    private int num_select = 0;

    private TextView field;

    private int plus = R.string.plus;
    private int times = R.string.times;
    private int div =  R.string.div;
    private int minus = R.string.minus;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectionDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectionDialog newInstance(String param1, String param2) {
        SelectionDialog fragment = new SelectionDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SelectionDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private RadioButton initRadioButton(String text, int id) {
        RadioButton rb = new RadioButton(getActivity());
        rb.setText(text);
        rb.setId(id);
        rb.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT));
        rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        rb.setTypeface(Typeface.DEFAULT_BOLD);

        return rb;
    }

    public void dialogButton(View v) {
        TextView tv = (TextView) v;
        switch (tv.getId()) {
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
                buffer += tv.getText();
                field.setText(buffer);
                break;
            case R.id.dialogButtonUndo:
                buffer = buffer.substring(0, buffer.length() - 1);
                break;
            case R.id.dialogButtonDiv:
                operation = div;
                value = Integer.parseInt(buffer);
                mListener.onDialogPositiveClick(this);
                break;
            case R.id.dialogButtonPlus:
                operation = plus;
                value = Integer.parseInt(buffer);
                mListener.onDialogPositiveClick(this);
                break;
            case R.id.dialogButtonMinus:
                operation = minus;
                value = Integer.parseInt(buffer);
                mListener.onDialogPositiveClick(this);
                break;
            case R.id.dialogButtonTimes:
                operation = times;
                value = Integer.parseInt(buffer);
                mListener.onDialogPositiveClick(this);
                break;
        }
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

        inflated.findViewById(R.id.dialogButtonPlus).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButtonTimes).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButtonMinus).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButtonDiv).setOnClickListener(this);




//        final RadioGroup rg1 = (RadioGroup) inflated.findViewById(R.id.radioGroup1);
//        final RadioGroup rg2 = (RadioGroup) inflated.findViewById(R.id.radioGroup2);
//
//
//        builder.setItems(new CharSequence[]
//                {getString(plus), getString(times), getString(minus), getString(div)},new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                // The 'which' argument contains the index position
//                // of the selected item
//                switch (which) {
//                    case 0:
//                        Toast.makeText(getActivity(), "clicked 1", Toast.LENGTH_SHORT).show();
//                        break;
//                    case 1:
//                        Toast.makeText(getActivity(), "clicked 2", Toast.LENGTH_SHORT).show();
//                        break;
//                    case 2:
//                        Toast.makeText(getActivity(), "clicked 3", Toast.LENGTH_SHORT).show();
//                        break;
//                    case 3:
//                        Toast.makeText(getActivity(), "clicked 4", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        });


//        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Log.d("CHECKING :", checkedId + "");
//                if (checkedId == R.id.radioPlus) {
//                    operation = R.string.plus;
//                    rg2.clearCheck();
//                    rg1.check(R.id.radioPlus);
//                } else if (checkedId == R.id.radioTimes) {
//                    operation = R.string.times;
//                    rg2.clearCheck();
//                    rg1.check(R.id.radioTimes);
//                }
//
//            }
//        });
//
//        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Log.d("CHECKING :", checkedId + "");
//                if (checkedId == R.id.radioMinus) {
//                    operation = R.string.minus;
//                    rg1.clearCheck();
//                    rg2.check(R.id.radioMinus);
//                } else if (checkedId == R.id.radioDiv) {
//                    operation = R.string.div;
//                    rg1.clearCheck();
//                    rg2.check(R.id.radioDiv);
//                }
//            }
//        });

//        if (num_select > 1) {
//            rg1.addView(initRadioButton(getString(plus), R.id.radioPlus));
//            rg1.addView(initRadioButton(getString(times), R.id.radioTimes));
//            rg2.addView(initRadioButton(getString(minus), R.id.radioMinus));
//            rg2.addView(initRadioButton(getString(div), R.id.radioDiv));
//        }
//
//        final EditText field = ((EditText) inflated.findViewById(R.id.editText));
//
//        builder.setView(inflated)
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.d("OK", field.getText().toString() + " " + operation);
//                        if (field.getText().toString().isEmpty()) {
//                            mListener.onDialogNegativeClick(SelectionDialog.this);
//                        } else {
//                            value = Integer.parseInt(field.getText().toString());
//                            mListener.onDialogPositiveClick(SelectionDialog.this);
//                        }
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mListener.onDialogNegativeClick(SelectionDialog.this);
//                    }
//
//                });
        //d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return builder.setView(inflated).create();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onClick(View v) {
        TextView tv = (TextView) v;
        switch (tv.getId()) {
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
                buffer += tv.getText();
                field.setText(buffer);
                break;
            case R.id.dialogButtonUndo:
                buffer = buffer.substring(0, buffer.length() - 1);
                break;
            case R.id.dialogButtonDiv:
                operation = div;
                value = Integer.parseInt(buffer);
                mListener.onDialogPositiveClick(this);
                dismiss();
                break;
            case R.id.dialogButtonPlus:
                operation = plus;
                value = Integer.parseInt(buffer);
                mListener.onDialogPositiveClick(this);
                dismiss();
                break;
            case R.id.dialogButtonMinus:
                operation = minus;
                value = Integer.parseInt(buffer);
                mListener.onDialogPositiveClick(this);
                dismiss();
                break;
            case R.id.dialogButtonTimes:
                operation = times;
                value = Integer.parseInt(buffer);
                mListener.onDialogPositiveClick(this);
                dismiss();
                break;
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onDialogPositiveClick(SelectionDialog dialog);
        public void onDialogNegativeClick(SelectionDialog dialog);
    }

    public int getOperation() {
        return operation;
    }

    public Integer getValue() {
        return value;
    }

    public int getNumSelect() { return num_select; }

    public void setNumSelect(int num_select) { this.num_select = num_select; }

    @Override
    public void onCancel(DialogInterface dialog) {
        mListener.onDialogNegativeClick(SelectionDialog.this);
        dismiss();
    }

}
