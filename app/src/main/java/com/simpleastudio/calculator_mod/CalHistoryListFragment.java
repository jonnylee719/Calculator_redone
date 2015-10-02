package com.simpleastudio.calculator_mod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jonathan on 1/10/2015.
 */
public class CalHistoryListFragment extends ListFragment {
    private static final String TAG = "CalHistoryListFragment";
    private ArrayList<Equation> mEquations;
    private EquationAdapter mAdapter;
    private Button mClearHistoryButton;
    private Button mBackButton;

    public CalHistoryListFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mEquations = CalHistory.get(getActivity()).getmEquations();

        mAdapter = new EquationAdapter(mEquations);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_list_calhistory, container, false);

        mClearHistoryButton = (Button) v.findViewById(R.id.button_clear_history);
        mClearHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalHistory.get(getActivity()).clearCalHistory();
                ((EquationAdapter)getListAdapter()).notifyDataSetChanged();
            }
        });
        mBackButton = (Button) v.findViewById(R.id.button_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                getActivity().setResult(Activity.RESULT_CANCELED, returnIntent);
                getActivity().finish();
            }
        });

        return v;
    }

    private class EquationAdapter extends ArrayAdapter<Equation>{

        public EquationAdapter(ArrayList<Equation> equations) {
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
