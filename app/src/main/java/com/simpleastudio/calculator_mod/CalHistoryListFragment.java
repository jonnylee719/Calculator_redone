package com.simpleastudio.calculator_mod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Jonathan on 1/10/2015.
 */
public class CalHistoryListFragment extends ListFragment {
    private static final String TAG = "CalHistoryListFragment";
    public static final String EXTRA_RETRIEVED_EQUATION = "com.simpleastudio.CalHistoryListFragment.retrieved_equation";
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        Log.d(TAG, "List item is clicked.");
        Equation e = ((EquationAdapter)getListAdapter()).getItem(position);
        Intent returnIntent = new Intent();
        returnIntent.putExtra(EXTRA_RETRIEVED_EQUATION, e);
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().finish();
    }

    private class EquationAdapter extends ArrayAdapter<Equation>{
        private NumberFormatter nf = new NumberFormatter();

        public EquationAdapter(ArrayList<Equation> equations) {
            super(getActivity(), 0, equations);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.equation_item_info, null);
            }

            if(position % 2 == 0){
                convertView.setBackgroundResource(android.R.drawable.divider_horizontal_dim_dark);
            }
            else{
                convertView.setBackgroundResource(android.R.drawable.screen_background_light_transparent);
            }

            Equation e = getItem(position);

            TextView textViewNum1 = (TextView) convertView.findViewById(R.id.textview_num1);
            TextView textViewNum2 = (TextView) convertView.findViewById(R.id.textview_num2);
            TextView textViewResult = (TextView) convertView.findViewById(R.id.textview_result);
            TextView textViewOperator = (TextView) convertView.findViewById(R.id.textview_operator);

            textViewNum1.setText(nf.formatNumber(e.getFormattedNum1()));
            textViewNum2.setText(nf.formatNumber(e.getFormattedNum2()));
            textViewResult.setText(nf.formatNumber(e.getResult()));
            textViewOperator.setText(e.getOperator());

            return convertView;
        }
    }


}
