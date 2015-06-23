package com.bradym.android.mathdokusolver.garbage;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bradym.android.mathdokusolver.TrueCell;
import com.bradym.android.mathdokusolver.logic.TrueVariable;

/**
 * Created by Michael on 4/7/2015.
 */
public class CellAdapter extends BaseAdapter {

    private Context mContext;
    private int size;
    private TrueCell[] items;
    private TrueVariable[] vars;

    //private KenKenCell[] cells;

    public CellAdapter(Context context, TrueVariable[] vars) {
        mContext = context;
        //this.vars = vars;
        this.size = vars.length;
        this.vars = vars;
        items = new TrueCell[size];

    }

    public TrueCell[] getViews() { return items; }

    public TrueVariable[] getVariables() {
        return vars;
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        if (items[position] == null) {
            return -1;
        } else {
            return items[position].getId();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //BorderedLayout btv;


        //LayoutInflater inflater = ((Activity) mContext.getApplicationContext()).getLayoutInflater();
        //View v = inflater.inflate(R.layout.bordered_text_view, null);

        //LayoutInflater.from(mContext).inflate(R.layout.bordered_text_view, null);
        if (items[position] != null) {
            return items[position];
        }
        //btv = new BorderedLayout(mContext);

//        View v  = LayoutInflater.from(mContext).inflate(R.layout.bordered_text_view, null);
//        v.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
        TrueCell v = new TrueCell(mContext);

        //cell.setPadding(0,0,0,3);
        //btv.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
        v.setTag(position);

//        int right = 0;
//        int up = 1;
//        int left = 1;
//        int down = 0;
        int num_col = (int) Math.sqrt((double) size);

//        if (position % num_col == 0) {
//            v.left = 2;
//        }
//
//        if (position / num_col == num_col - 1) {
//            v.down = 2;
//        }
//
//        if (position / num_col == 0) {
//            v.up = 2;
//        }
//        if (position % num_col == num_col - 1) {
//            v.right = 2;
//        }
        //btv.setBorders(right, up, left, down);
        //btv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        //btv.setTextColor(Color.BLACK);
        //btv.setGravity(Gravity.LEFT);
        v.setBackgroundColor(Color.TRANSPARENT);
        items[position] = v;
        v.setVariable(vars[position]);

//        TextView test = new TextView(mContext);
//        test.setBackgroundColor(Color.RED);
//        btv.addView(test, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return v;
    }


}
