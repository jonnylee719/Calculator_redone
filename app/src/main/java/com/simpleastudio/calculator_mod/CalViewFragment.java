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

    public CalViewFragment(){

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
                        TextView view = (TextView) v;
                        String buttonText = view.getText().toString();
                        Log.d(TAG, "Number button " + buttonText + " was clicked.");
                        String text = mCurrentCalTextView.getText().toString() + buttonText;
                        mCurrentCalTextView.setText(text);
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
        String clearButtonText = getString(R.string.but_clear);
        String[] operatorColumn = {"+", "-", multiplication, division, clearButtonText};
        for(int i = 2; i< tableLayout.getChildCount(); i++){
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            Button button = (Button) row.getChildAt(3);
            String buttonText = operatorColumn[i-2];
            button.setText(buttonText);
        }

        TableRow row = (TableRow) tableLayout.getChildAt(6);
        Button dpButton = (Button) row.getChildAt(0);
        String dp = new DecimalFormatSymbols().getDecimalSeparator() + "";
        dpButton.setText(dp);

        Button zeroButton = (Button) row.getChildAt(1);
        zeroButton.setText("0");

        Button equalButton = (Button) row.getChildAt(2);
        equalButton.setText("=");




        return v;

    }
}
