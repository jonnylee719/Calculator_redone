package com.simpleastudio.calculator_mod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormatSymbols;

/**
 * Created by Jonathan on 26/9/2015.
 */
public class CalViewFragment extends Fragment{
    private static final String TAG = "CalViewFragment";
    private TextView mPastCalTextView;
    private TextView mCurrentCalTextView;
    private CalManager mCalManager;
    private final static String ARGS_CALMANAGER = "calManager";
    public final static int REQUEST_CODE = 1;
    private final static String ARGS_EQUATION = "equation";

    private Button clearButton;
    private Button equalButton;
    private Button negativeButton;

    public CalViewFragment(){

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.d(TAG, "onActivityResult");
        if(requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Log.d(TAG, "Retrieved equation sent from calHistoryListFragment.");
                Equation retrievedEquation = (Equation) data.getSerializableExtra(CalHistoryListFragment.EXTRA_RETRIEVED_EQUATION);
                setRetrievedEquation(retrievedEquation);
            }
            else if(resultCode == Activity.RESULT_CANCELED){
                Log.d(TAG, "No retrieved equation from CalHistoryListFragment.");
            }
        }

    }

    public void setRetrievedEquation(Equation e){
        mCalManager.resetToNewEquation(e);
        updateUI();
    }

    public void updateUI(){
        Log.d(TAG, "updateUI");
        mPastCalTextView.setText(mCalManager.getmLastEquation());
        mCurrentCalTextView.setText(mCalManager.getmCurrentEquationDisplay());
        scrollDisplayToEnd();
        resetClearButtonText();
    }

    private void resetClearButtonText(){
        clearButton.setText(R.string.but_clear);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mCalManager = new CalManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.d(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.fragment_calview, container, false);

        mPastCalTextView = (TextView)v.findViewById(R.id.textview_past_equation);
        mPastCalTextView.setText(mCalManager.getmLastEquation());
        LinearLayout clickableArea = (LinearLayout)v.findViewById(R.id.calHis_clickable_area);
        clickableArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CalHistoryActivity.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        mCurrentCalTextView = (TextView)v.findViewById(R.id.textview_current_equation);
        mCurrentCalTextView.setText(mCalManager.getmCurrentEquationDisplay());


        View.OnClickListener numberButtonListener =
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearButton.setText(R.string.but_clear);
                        TextView view = (TextView) v;
                        String buttonText = view.getText().toString();
                        String text = mCalManager.numButtonClicked(buttonText);
                        mCurrentCalTextView.setText(text);
                        scrollDisplayToEnd();
                    }
                };

        View.OnClickListener operatorButtonListener =
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearButton.setText(R.string.but_clear);
                        TextView view = (TextView) v;
                        String buttonText = view.getText().toString();
                        String text = mCalManager.operButtonClicked(buttonText);
                        mCurrentCalTextView.setText(text);
                        mPastCalTextView.setText(mCalManager.getmLastEquation());
                        scrollDisplayToEnd();
                    }
                };

        TableLayout tableLayout = (TableLayout) v.findViewById(R.id.tableLayout_calview);
        int numberPad = 9;
        for(int i = 3; i< tableLayout.getChildCount()-1; i++){
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for(int j = 0; j < row.getChildCount()-1; j++){
                Button button = (Button) row.getChildAt(j);
                button.setText(numberPad+"");
                button.setOnClickListener(numberButtonListener);
                numberPad--;
            }
        }

        String multiplication = "\u00D7";
        String division = "\u00F7";
        String[] operatorColumn = {"+", "-", multiplication, division};
        for(int i = 2; i< tableLayout.getChildCount()-1; i++){
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            Button button = (Button) row.getChildAt(3);
            String buttonText = operatorColumn[i-2];
            button.setText(buttonText);
            button.setOnClickListener(operatorButtonListener);
        }

        TableRow row1 = (TableRow) tableLayout.getChildAt(2);
        negativeButton = (Button) row1.getChildAt(2);
        negativeButton.setText(R.string.but_negative);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mCalManager.negButtonClicked();
                mCurrentCalTextView.setText(text);
                scrollDisplayToEnd();
            }
        });

        TableRow row = (TableRow) tableLayout.getChildAt(6);
        Button dpButton = (Button) row.getChildAt(0);
        String dp = new DecimalFormatSymbols().getDecimalSeparator() + "";
        dpButton.setText(dp);
        dpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearButton.setText(R.string.but_clear);
                String text = mCalManager.dpButtonClicked();
                mCurrentCalTextView.setText(text);
                scrollDisplayToEnd();
            }
        });

        Button zeroButton = (Button) row.getChildAt(1);
        zeroButton.setText("0");
        zeroButton.setOnClickListener(numberButtonListener);

        equalButton = (Button) row.getChildAt(2);
        equalButton.setText("=");
        equalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultText = mCalManager.equalButtonClicked();
                mCurrentCalTextView.setText(resultText);
                mPastCalTextView.setText(mCalManager.getmLastEquation());
                clearButton.setText(R.string.but_clear_all);
                scrollDisplayToEnd();
            }
        });

        clearButton = (Button) row.getChildAt(3);
        clearButton.setText(R.string.but_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearButton.setText(R.string.but_clear);
                String text = mCalManager.cancelButClicked();
                mCurrentCalTextView.setText(text);
                mPastCalTextView.setText(mCalManager.getmLastEquation());
                scrollDisplayToEnd();
            }
        });
        clearButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clearButton.setText(R.string.but_clear);
                mCurrentCalTextView.setText(mCalManager.onLongClickClear());
                mPastCalTextView.setText(mCalManager.getmLastEquation());
                return false;
            }
        });
        return v;

    }

    @Override
    public void onPause(){
        super.onPause();
        mCalManager.saveEquations();
    }

    private void scrollDisplayToEnd(){
        View v = getView();
        final HorizontalScrollView currentEquationScrollView = (HorizontalScrollView) v.findViewById(R.id.currentEquationScrollView);
        currentEquationScrollView.post(new Runnable() {
            @Override
            public void run() {
                currentEquationScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        final HorizontalScrollView pastEquationScrollView = (HorizontalScrollView) v.findViewById(R.id.pastEquationScrollView);
        pastEquationScrollView.post(new Runnable() {
            @Override
            public void run() {
                pastEquationScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });
    }


}
