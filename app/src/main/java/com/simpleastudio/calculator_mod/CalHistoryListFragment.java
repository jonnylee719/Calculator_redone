package com.simpleastudio.calculator_mod;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.LayoutInflaterFactory;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jonathan on 1/10/2015.
 */
public class CalHistoryListFragment extends ListFragment {
    private static final String TAG = "CalHistoryListFragment";
    private ArrayList<Equation> mEquations;
    private EquationAdaptor mAdapter;

    public CalHistoryListFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mEquations = CalHistory.get(getActivity()).getmEquations();

        mAdapter = new EquationAdaptor(mEquations);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_list_calhistory, container, false);

        return v;
    }

    private class EquationAdaptor extends ArrayAdapter<Equation>{

        public EquationAdaptor(ArrayList<Equation> equations) {
            super(getActivity(), 0, equations);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.equation_item_info, null);
            }

            Equation e = getItem(position);

            TextView equationTextView = (TextView) convertView.findViewById(R.id.textview_equation);
            equationTextView.setText(e.toString());

            return convertView;
        }
    }


}
