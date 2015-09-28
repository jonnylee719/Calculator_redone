package com.simpleastudio.calculator_mod;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private Button clearButton;
    private Button equalButton;

    public CalViewFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mCalManager = new CalManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_calview, container, false);

        mPastCalTextView = (TextView)v.findViewById(R.id.textview_past_equation);
        mCurrentCalTextView = (TextView)v.findViewById(R.id.textview_current_equation);

        View.OnClickListener numberButtonListener =
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearButton.setText(R.string.but_clear);
                        TextView view = (TextView) v;
                        String buttonText = view.getText().toString();
                        String text = mCalManager.numButtonClicked(buttonText);
                        mCurrentCalTextView.setText(text);
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
}
