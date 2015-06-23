package com.bradym.android.mathdokusolver.garbage;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.Toast;

import com.bradym.android.mathdokusolver.R;
import com.bradym.android.mathdokusolver.TrueCell;
import com.bradym.android.mathdokusolver.logic.DiffConstraint;
import com.bradym.android.mathdokusolver.logic.DivConstraint;
import com.bradym.android.mathdokusolver.logic.MinusConstraint;
import com.bradym.android.mathdokusolver.logic.PlusConstraint;
import com.bradym.android.mathdokusolver.logic.ProdConstraint;
import com.bradym.android.mathdokusolver.logic.TrueConstraint;
import com.bradym.android.mathdokusolver.logic.TrueVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


public class PuzzleActivity extends ActionBarActivity implements SelectionDialog.OnFragmentInteractionListener {

    KenKenGrid gridview;
    GridLayout gridLayout;

    List<TrueConstraint> constraints = new ArrayList();
    TrueVariable[] variables;

    int num_col = 6;




    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_activity);
        Intent intent = getIntent();
        num_col = intent.getIntExtra("size", num_col);

        //cells = new KenKenCell[num_col*num_col];
        variables = new TrueVariable[num_col*num_col];

        //variables = new Variable[num_col*num_col];
        for (int i = 0; i < num_col*num_col; i++) {
            List<Integer> dom = new ArrayList();
            for (int j = 1; j <= num_col; j++) {
                dom.add(j);
            }
            //cells[i] = new KenKenCell(new Variable(dom, i));
//            variables[i] = new TrueVariable(i + "", dom);
        }





        //solver = new Solver();



//

        //gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        gridLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });









        gridview = (KenKenGrid) findViewById(R.id.gridView);
        gridview.setAdapter(new CellAdapter(this, variables));
        gridview.setNumColumns(num_col);
        gridview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int pos = gridview.pointToPosition((int) event.getX(), (int) event.getY());

                TrueCell btv = (TrueCell) gridview.getChildAt(pos);
                if (btv == null) {
                    gridview.cleanSelection();
                    return true;
                }
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_OUTSIDE:
                    case MotionEvent.ACTION_CANCEL:
                        gridview.cleanSelection();
                        return true;
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        gridview.selectView(btv);
                        return false;
                    case MotionEvent.ACTION_UP:
                        gridview.selectView(btv);
                        SelectionDialog sd = new SelectionDialog();
                        FragmentManager fm = getFragmentManager();
                        sd.setNumSelect(gridview.getSelected().size());
                        sd.show(fm, "start fragment");
                        return true;
                }
                return false;
            }
        });
        gridview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int pLength;
                int pWidth = gridview.getWidth();
                int pHeight = gridview.getHeight();

                //Set myGridLayout equal width and height
                if (pWidth >= pHeight) {
                    pLength = pHeight;
                } else {
                    pLength = pWidth;
                }
                ViewGroup.LayoutParams pParams = gridview.getLayoutParams();
                pParams.width = pLength;
                pParams.height = pLength;
                gridview.setLayoutParams(pParams);

                gridview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


        List<AssignmentOLD> assignments = null;
        for(int i = 0; i < num_col; i++) {
            List<TrueVariable> rowVars = new ArrayList();
            List<TrueVariable> colVars = new ArrayList();
            for(int j = 0; j < num_col; j++) {
                rowVars.add(gridview.getVariables()[num_col*i + j]);
                colVars.add(gridview.getVariables()[i + num_col*j]);
            }

            TrueConstraint rowConstraint = new DiffConstraint(rowVars);

            TrueConstraint colConstraint = new DiffConstraint(colVars);

            if (assignments == null) {

                //assignments = amazing2(rowVars, 0, new ArrayList<Integer>());
            }
//            rowConstraint.addAssignments(assignments);
//            colConstraint.addAssignments(assignments);

            constraints.add(rowConstraint);
            constraints.add(colConstraint);
        }

//        LinkedList<Integer> values = new LinkedList<>();
//        for (int i = 1; i < num_col + 1; i++) {
//            values.add(i);
//        }
//        TrueNode root = new TrueNode();
//        Log.d("BEGIN AMAZINGER", "");
//        amazinger(root, values);
//        Log.d("END AMAZINGER", "");
//        Log.d("BEGIN DFS", "");
//        root.depthFirstSearch("");
//        Log.d("END AMAZINGER", "");


        for (TrueConstraint tc : constraints) {
            Log.d("CONSTRAINT", tc.toString());
        }

//        Log.d("BEGIN AMAZING", "");
//        Constraint.amazing2N(indices, constraints, new ArrayList<Integer>(num_col));
//        Log.d("END AMAZING", "");
//        for (Constraint c : constraints) {
//            Log.d("CONSTRAINT", c.getName());
//            c.dfs(c.supportsN.get(0), "");
////            for (Assignment t : c.assignmentManager.getAssignments()) {
////                Log.d("TUPLE", t.toString());
////            }
//        }

        //test6x6();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDialogPositiveClick(SelectionDialog dialog) {
        //List<Integer> prev = new ArrayList();
        List<TrueCell> prev = new ArrayList();
        List<TrueCell> selected = gridview.getSelected();
        for (TrueCell btv : selected) {
            Log.d("BTV = ", btv.getTag() + "");
            int pos = (int) btv.getTag();
            //btv.setBorders(1,1,1,1);
            btv.setMaxBorders(1,1,1,1);

            for (TrueCell btv2 : prev) {
                int pos2 = (int ) btv2.getTag();
                int diff = pos - pos2;

//                if (diff == 1) {
//                    btv.left = 0;
//                    btv2.right = 0;
//                } else if (diff == -1) {
//                    btv.right = 0;
//                    btv2.left = 0;
//                } else if (diff == num_col) {
//                    btv.up = 0;
//                    btv2.down = 0;
//                } else if (diff == -num_col) {
//                    btv.down = 0;
//                    btv2.up = 0;
//                }
            }
            prev.add(btv);
        }

        TrueCell trueCell;
        if (selected.size() == 1) {
            trueCell = gridview.getSelected().get(0);

            trueCell.setConstraintString(dialog.getValue().toString());

        } else  {
            Integer min = null;
            trueCell = null;
            for (TrueCell j : selected) {
                if (min == null || (int) j.getTag() < min) {
                    min = (int) j.getTag();
                    trueCell = j;
                }
            }
            trueCell.setConstraintString(dialog.getValue() + getString(dialog.getOperation()));
        }

        trueCell.invalidate();

        List<TrueVariable> scope = new ArrayList();
        for (TrueCell celly : selected) {
            scope.add(celly.getVariable());
        }
        TrueConstraint c = buildConstraint(scope, dialog.getOperation(), dialog.getValue());
        Toast.makeText(this, "Added Constraint " + trueCell, Toast.LENGTH_SHORT).show();

        Log.d("CONSTRAINT", c.toString());
        constraints.add(c);
        gridview.cleanSelection();
        boolean allConstrained = true;
        for (TrueVariable v : variables) {
            if (v.constraints.size() == 2) {
                allConstrained = false;
                break;
            }
        }

        if (allConstrained) {
//            TrueSolver solver = new TrueSolver(variables, constraints);
            Log.d("ALL CONSTRAINED", "SOLVING WITH GAC");

//            SolverTask st = new SolverTask(gridview);
//            st.execute(solver);

//            while (st.getStatus() != AsyncTask.Status.FINISHED) {
//                for (BorderedTextView child : gridview.getAdapter().getViews()) {
//                    child.refreshText();
//                }
//            }
        }

    }


    @Override
    public void onDialogNegativeClick(SelectionDialog dialog) {
       gridview.cleanSelection();
    }


    private boolean validAssignment(Constraint c) {
        int result;
        switch(c.op) {
            case R.string.plus:
                result = 0;
                for (Variable var : c.scope) {
                    result += var.getCurValue();
                }
                return result == c.value;
            case R.string.times:
                result = 1;
                for (Variable var : c.scope) {
                    result *= var.getCurValue();
                }
                return result == c.value;
            case R.string.div:
                Integer d1 = c.scope.get(0).getCurValue();
                Integer d2 = c.scope.get(1).getCurValue();
                return ((d1 / d2 == c.value) || (d2 / d1 == c.value));
            case R.string.minus:
                Integer m1 = c.scope.get(0).getCurValue();
                Integer m2 = c.scope.get(1).getCurValue();
                return (Math.abs(m1 - m2) == c.value);
            case R.string.equals:
                return c.value == c.scope.get(0).getCurValue();
            case R.string.ALLDIFF:
                for (int i = 0; i < c.scope.size(); i++) {
                    for (int j = i + 1; j < c.scope.size(); j++) {
                        if (c.scope.get(i).getCurValue() == c.scope.get(j).getCurValue()) {
                            return false;
                        }
                    }
                }
                return true;

        }

        return false;
    }

//    public boolean BT(int level) {
//        boolean done = true;
//        for (Variable var : variables) {
//            if (var.getCurValue() == null) {
//                done = false;
//                break;
//            }
//        }
//        if (done) {
//            for (Variable var : variables) {
//                var.setValue(var.getCurValue());
//            }
//            Log.d(level + " DONE", "DONE");
//            return true;
//        }
//
//        Variable mrv = pickUnassignedVar();
//        for (Integer d : mrv.getDomain()) {
//            mrv.setCurValue(d);
//            Log.d(level + " SETTING VARIABLE " + mrv.getIndex(), "TO " + d);
//
//            boolean ok = true;
//            for (Constraint c : constraints) {
//                if (c.scope.contains(mrv)) {
//                    //Log.d(level + " CONSTRAINT " + c.getName(), "CONTAINS MRV " + mrv.getIndex());
//                    if (c.numCurrentUnassigned() == 0) {
//                        if (!validAssignment(c)) {
//                            Log.d(level + "CONSTRAINT " + c.getName(), "NOT VALID");
//                            ok = false;
//                            break;
//                        }
//                    } else if ((c.op == R.string.plus || c.op == R.string.times) && !validatePartial(c)) {
//                        mrv.setCurValue(null);
//                        Log.d(level + " PRUNED", "");
//                        return false;
//                    }
//                }
//            }
//
//            if (ok) {
//                if (BT(level + 1)) {
//                    return true;
//                }
//            }
//        }
//        mrv.setCurValue(null);
//        Log.d(level + " BYE", "BYE");
//        return false;
//    }
//






    private boolean validatePartial(Constraint c) {
        int result = 0;
        boolean plus = c.op == R.string.plus;
        if (!plus)
            result = 1;

        for (Variable v : c.scope) {
            if (v.getCurValue() != null) {
                result = plus ? result + v.getCurValue() : result * v.getCurValue();
            }
            if (result > c.value) {
                return false;
            }
        }
        return true;

    }

//    private Variable pickUnassignedVar() {
//
//        for (Variable var : ((CellAdapter)gridview.getAdapter()).getVariables()) {
//            if (var.getCurValue() == null) {
//                return var;
//            }
//        }
//        return null;
//    }



    public void test6x6() {

        constraints.add(new MinusConstraint(5, Arrays.asList(variables[3], variables[4]), num_col));
        constraints.add(new MinusConstraint(1, Arrays.asList(variables[9], variables[10]), num_col));
        constraints.add(new MinusConstraint(3, Arrays.asList(variables[14], variables[15]), num_col));
        constraints.add(new DivConstraint(2, Arrays.asList(variables[5], variables[11]), num_col));
        constraints.add(new DivConstraint(2, Arrays.asList(variables[22], variables[23]), num_col));
        constraints.add(new ProdConstraint(80, Arrays.asList(variables[0], variables[1], variables[6]), num_col));
        constraints.add(new ProdConstraint(9, Arrays.asList(variables[12], variables[18], variables[19]), num_col));
        constraints.add(new ProdConstraint(30, Arrays.asList(variables[16], variables[17]), num_col));
        constraints.add(new ProdConstraint(8, Arrays.asList(variables[25], variables[26], variables[32]), num_col));
        constraints.add(new ProdConstraint(10, Arrays.asList(variables[30], variables[31]), num_col));
        constraints.add(new PlusConstraint(3, Arrays.asList(variables[2]), num_col));
        constraints.add(new PlusConstraint(2, Arrays.asList(variables[13]), num_col));
        constraints.add(new PlusConstraint(6, Arrays.asList(variables[24]), num_col));
        constraints.add(new PlusConstraint(1, Arrays.asList(variables[34]), num_col));
        constraints.add(new PlusConstraint(11, Arrays.asList(variables[7],variables[8]), num_col));
        constraints.add(new PlusConstraint(11, Arrays.asList(variables[20],variables[21]), num_col));
        constraints.add(new PlusConstraint(13, Arrays.asList(variables[27],variables[28],variables[33]), num_col));
        constraints.add(new PlusConstraint(8, Arrays.asList(variables[29],variables[35]), num_col));



//        TrueSolver solver = new TrueSolver(variables, constraints);
        Log.d("TEST", "SOLVING WITH GAC");

////        SolverTask st = new SolverTask(gridview);
//        st.execute(solver);



    }

    public void test() {

//        constraints.add(new MinusConstraint(2, Arrays.asList(variables[0], variables[4]), num_col ));
//        constraints.add(new MinusConstraint(3, Arrays.asList(variables[5], variables[9]), num_col ));
//        constraints.add(new DivConstraint(2, Arrays.asList(variables[10], variables[11]), num_col ));
//        constraints.add(new DivConstraint(2, Arrays.asList(variables[13], variables[14]), num_col ));
//        constraints.add(new ProdConstraint(6, Arrays.asList(variables[1], variables[2]), num_col ));
//        constraints.add(new PlusConstraint(8, Arrays.asList(variables[3], variables[6], variables[7]), num_col ));
//        constraints.add(new PlusConstraint(4, Arrays.asList(variables[8], variables[12]), num_col ));
//        constraints.add(new PlusConstraint(3, Arrays.asList(variables[15]), num_col ));
//
//        TrueSolver solver = new TrueSolver(variables, constraints);
//        Log.d("TEST", "SOLVING WITH GAC");
//
//        SolverTask st = new SolverTask(gridview);
//        st.execute(solver);

    }
//
//    public void test2() {
//        Variable[] vars = ((CellAdapter)gridview.getAdapter()).getVariables();
//
//        constraints.add(new Constraint(Arrays.asList(vars[0], vars[1]), R.string.times, 8));
//        constraints.add(new Constraint(Arrays.asList(vars[2], vars[3]), R.string.plus, 4));
//        constraints.add(new Constraint(Arrays.asList(vars[4]), R.string.equals, 3));
//        constraints.add(new Constraint(Arrays.asList(vars[5], vars[6]), R.string.div, 2));
//        constraints.add(new Constraint(Arrays.asList(vars[7]), R.string.equals, 1));
//        constraints.add(new Constraint(Arrays.asList(vars[8], vars[9]), R.string.minus, 2));
//        constraints.add(new Constraint(Arrays.asList(vars[10], vars[11]), R.string.plus, 6));
//        constraints.add(new Constraint(Arrays.asList(vars[12], vars[13]), R.string.plus, 5));
//        constraints.add(new Constraint(Arrays.asList(vars[14], vars[15]), R.string.minus, 1));
//
//    }

    private TrueConstraint buildConstraint(List<TrueVariable> scope, int operation, int value) {

        switch (operation) {
            case R.string.plus:
            case R.string.equals:
                return new PlusConstraint(value, scope, num_col);
            case R.string.times:
                return new ProdConstraint(value, scope, num_col);
            case R.string.div:
                return new DivConstraint(value, scope, num_col);
            case R.string.minus:
                return new MinusConstraint(value, scope, num_col);

        }
        return null;


//        Constraint constraint = new Constraint(scope, operation, value, "" + getResources().getString(operation) + "" + value);
//        constraint.addAssignments(amazing(scope, 0, new ArrayList<Integer>(), (float) value, operation, false));
//        for (AssignmentOLD a : constraint.assignmentManager.getAssignments()) {
//            Log.d("CONSTRAINT", a.toString());
//        }
//
//        return constraint;
    }
    private List<IntTuple> pushToFront(List<IntTuple> list, Integer i) {
        for (IntTuple tuple : list) {
            tuple.add(i);
        }
        return list;
    }
    private List<AssignmentOLD> amazing(List<Variable> vars, int index, List<Integer> partial, float rem, int op, boolean first) {
        List<AssignmentOLD> completes = new ArrayList();

        Variable var = vars.get(index);
        for (Integer val : var.getDomain()) {
            List<Integer> newPartial = new ArrayList(partial);
            newPartial.add(val);
            if (index < vars.size() - 1) {
                switch (op) {
                    case R.string.plus:
                        if (rem - val > 0) {
                            completes.addAll(amazing(vars, index + 1, newPartial, rem - val, op, first));
                        }
                        break;
                    case R.string.minus:
                        if (!first || (first && rem + val < 0)) {
                            completes.addAll(amazing(vars, index + 1, newPartial, rem + val, op, first));
                        }
                        if (!first && (rem - val < 0))
                            completes.addAll(amazing(vars, index + 1, newPartial, rem - val, op, true));
                        break;
                    case R.string.times:
                        if (rem / val >= 1) {
                            completes.addAll(amazing(vars, index + 1, newPartial, rem / val, op, first));
                        }
                        break;
                    case R.string.div:
                        if (!first || (first && rem * val <= 1)) {
                            completes.addAll(amazing(vars, index + 1, newPartial, rem * val, op, first));
                        }
                        if (!first && (rem / val <= 1))
                            completes.addAll(amazing(vars, index + 1, newPartial, rem / val, op, true));
                        break;
                }

            } else if (index == vars.size() - 1) {
                AssignmentOLD result = new AssignmentOLD(newPartial);
                switch (op) {
                    case R.string.plus:
                    case R.string.equals:
                        if (rem == val) {
                            completes.add(result);
                        }
                        break;
                    case R.string.minus:
                        if (!first) {
                            if (rem == val) {
                                completes.add(result);
                            }
                        } else {
                            if (rem == -val) {
                                completes.add(result);
                            }
                        }
                        break;
                    case R.string.times:
                        if (rem / val == 1) {
                            completes.add(result);
                        }
                        break;
                    case R.string.div:
                        if (!first) {
                            if (Math.abs(rem / val  - 1) <= 0.01) {
                                completes.add(result);
                            }
                        } else {
                            if (Math.abs(rem * val - 1) <= 0.01) {
                                completes.add(result);
                            }
                        }
                        break;
                }
            }
        }
        return completes;
    }


    public List<AssignmentOLD> amazing2(List<Variable> vars, int index, List<Integer> partial) {
        List<AssignmentOLD> completes = new ArrayList();
        if (index < vars.size()) {
            for (Integer val : vars.get(index).getDomain()) {
                if (!partial.contains(val)) {
                    List<Integer> newList = new ArrayList(partial);
                    newList.add(val);
                    if (index == vars.size() - 1) {
                        AssignmentOLD a = new AssignmentOLD(newList);
                        completes.add(a);
                    } else {
                        completes.addAll(amazing2(vars, index + 1, newList));
                    }
                }
            }
        }
        return completes;
    }



    public void amazinger(TrueNode parent, List<Integer> remaining) {
        ListIterator<Integer> iterator = remaining.listIterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            TrueNode node = new TrueNode(next, parent);
            iterator.remove();
            amazinger(node, new LinkedList(remaining));
            iterator.add(next);
        }
    }



}

