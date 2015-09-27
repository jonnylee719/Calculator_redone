package com.simpleastudio.calculator_mod;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Created by Jonathan on 26/9/2015.
 */
public class CalManager {
    private static final String TAG = "CalManager";
    private String result = "";
    private String formattedResult;
    private String mLastEquation;
    private Equation mCurrentEquation;

    public CalManager(){
        mCurrentEquation = new Equation();
    }

    public String numButtonClicked(String number) {
        //4 Scenarios:
        //a) before or after num1Entered
        //b) before or after num2Entered

        if(!mCurrentEquation.isOperEntered()) {
            if (!mCurrentEquation.isNum1Entered()) {
                result = ""; //might be straight after the last operation
                mCurrentEquation.setNum1(number);
                mCurrentEquation.setNum1Entered(true);
            } else if (mCurrentEquation.isNum1Entered()) {
                if (mCurrentEquation.getNum1().equals("0")) {
                    mCurrentEquation.setNum1(number);
                } else {
                    if(mCurrentEquation.getNum1().indexOf(".") != -1){
                        int dp = mCurrentEquation.getNum1().indexOf(".");                 //the position of the decimal point
                        String fractionPart = mCurrentEquation.getNum1().substring(dp);
                        if(fractionPart.length() > 9){             //10 digits after the decimal point is the limit of num1
                            //do Nothing
                        }
                        else{
                            mCurrentEquation.setNum1(mCurrentEquation.getNum1() + number);                   //appends the number
                        }
                    }
                    else {                                          // not a fraction number
                        if(mCurrentEquation.getNum1().length()>9){
                            //do Nothing
                        }
                        else {
                            mCurrentEquation.setNum1(mCurrentEquation.getNum1() + number);
                        }
                    }
                }
            }
        }
        else if(mCurrentEquation.isOperEntered()){               //oper has been entered
            if(!mCurrentEquation.isNum2Entered()){               //first check if num1 has been entered
                mCurrentEquation.setNum2(number);
                mCurrentEquation.setNum2Entered(true);
            }
            else if(mCurrentEquation.isNum2Entered()){                          //num2 has been entered
                if(mCurrentEquation.getNum2().equals("0")){
                    mCurrentEquation.setNum2(number);
                }
                else{
                    if(mCurrentEquation.getNum2().indexOf(".") != -1){
                        int dp = mCurrentEquation.getNum2().indexOf(".");                 //the position of the decimal point
                        String fractionPart = mCurrentEquation.getNum2().substring(dp);
                        if(fractionPart.length() > 9){             //10 digits after the decimal point is the limit of num
                            //do Nothing
                        }
                        else{
                            mCurrentEquation.setNum2(mCurrentEquation.getNum2()+number);                   //appends the number
                        }
                    }
                    else {                                          // not a fraction number
                        if(mCurrentEquation.getNum2().length()>9){
                            //do Nothing
                        }
                        else {
                            mCurrentEquation.setNum2(mCurrentEquation.getNum2()+number);
                        }
                    }
                }
            }
        }
        return mCurrentEquation.toString();
    }

    public String operButtonClicked(String operatorText) {
        // 3 scenarios: 1) num1Entered is false
        //              2) operEntered is false
        //              3) operEntered is true

        if(!mCurrentEquation.isNum1Entered()){           // check for result
            if(!result.equals("")){        //result contains something
                if(result.indexOf("-") == 0){   //result is negative
                    mCurrentEquation.setNegNum1(true);
                    mCurrentEquation.setNum1(result.substring(1));
                }
                else{
                    mCurrentEquation.setNum1(result);
                }
                mCurrentEquation.setNum1Entered(true);
                result = "";
                mCurrentEquation.setOperator(operatorText);
                mCurrentEquation.setOperEntered(true);

            } // end of result equals something
            else{
                mCurrentEquation.setNum1("0");
                mCurrentEquation.setNum1Entered(true);
                mCurrentEquation.setOperator(operatorText);
                mCurrentEquation.setOperEntered(true);
            }
        }
        else if(!mCurrentEquation.isOperEntered()){                                      //Number is entered
            //if(num1.indexOf(".") == (num1.length() - 1)){
            //    num1 = num1.substring(0, (num1.indexOf(".")));     //if dp is at the end of num1
            //}
            mCurrentEquation.setOperator(operatorText);
            mCurrentEquation.setOperEntered(true);
        }
        else {           //there's 2 scenarios: 1) num2 not entered, so just switch the oper around, 2) num2 entered, doOperation() and treat as above
            if (!mCurrentEquation.isNum2Entered()) {
                mCurrentEquation.setOperator(operatorText);
            } else {
                //if (num2.indexOf(".") == (num2.length() - 1)) {
                //    num2 = num2.substring(0, (num2.indexOf(".")));
               // }
                doOperation();
                if (result.indexOf("-") == 0) {
                    mCurrentEquation.setNegNum1(true);
                    mCurrentEquation.setNum1(result.substring(1));
                } else {                      //result is positive
                    mCurrentEquation.setNum1(result);
                }
                mCurrentEquation.setNum1Entered(true);
                result = "";
                operButtonClicked(operatorText);       //reloop as if this is a new operButClicked
            }
        }
        return mCurrentEquation.toString();
    }

    public String equalButtonClicked(){
        //4 scenarios:
        //1) before num1Entered
        //2) after num1Entered + before operEntered
        //3) after operEntered + before num2Entered
        //4) after num2Entered
        String displayText = "Error at EqualButtonClicked()";

        if(mCurrentEquation.isOperEntered() && !mCurrentEquation.isNum2Entered()){
            mCurrentEquation.setOperator("");
            mCurrentEquation.setOperEntered(false);
            displayText = mCurrentEquation.toString();

        }
        else if(mCurrentEquation.isNum2Entered()){
            //if(mCurrentEquation.getNum2().indexOf(".") == mCurrentEquation.getNum2().length()){
            //   String num2NoDp = mCurrentEquation.getNum2().substring(0, (mCurrentEquation.getNum2().length() - 1));
            //    mCurrentEquation.setNum2(num2NoDp);
            //}
            doOperation();
            displayText = result;
        }
        return displayText;
    }

    public String cancelButClicked(){
        //4 scenarios:
        //1) before num1Entered
        //2) after num1Entered + before operEntered
        //3) after operEntered + before num2Entered
        //4) after num2Entered
        if(!mCurrentEquation.isNum1Entered()){
            mCurrentEquation = new Equation();          //deletes everything and setDisplay to "0"
            mLastEquation = "";
            result = "";
        }
        else if(!mCurrentEquation.isOperEntered()){
            int length = mCurrentEquation.getNum1().length();
            String newNum1 = mCurrentEquation.getNum1().substring(0, (length-1));
            mCurrentEquation.setNum1(newNum1);

            if(length-1 == 0){
                mCurrentEquation.setNum1Entered(false);
                mCurrentEquation.setNegNum1(false);
            }
        }
        else if(!mCurrentEquation.isNum2Entered()){
            if(mCurrentEquation.isNegNum2()){
                mCurrentEquation.setNegNum2(false);
            }
            else {
                mCurrentEquation.setOperator("");
                mCurrentEquation.setOperEntered(false);
            }
        }
        else if(mCurrentEquation.isNum2Entered()){
            int length = mCurrentEquation.getNum2().length();
            String newNum2 = mCurrentEquation.getNum2().substring(0, (length-1));
            mCurrentEquation.setNum2(newNum2);

            if(length-1 == 0){
                mCurrentEquation.setNum2("");
                mCurrentEquation.setNum2Entered(false);
            }
        }
        return mCurrentEquation.toString();
    }

    private String negNumberConvertor(String number, boolean negative){
        String newNumber;
        if(negative)
            newNumber = "-" + number;
        else
            newNumber = number;
        return newNumber;

    }

    public String onLongClickClear(){
        mCurrentEquation = new Equation();
        mLastEquation = "";
        result = "";
        return mCurrentEquation.toString();
    }

    public void doOperation(){
        MathContext context = new MathContext(120, RoundingMode.HALF_UP);

        BigDecimal num1Bd = new BigDecimal(negNumberConvertor(mCurrentEquation.getNum1(), mCurrentEquation.isNegNum1()));
        BigDecimal num2Bd = new BigDecimal(negNumberConvertor(mCurrentEquation.getNum2(), mCurrentEquation.isNegNum2()));
        BigDecimal resultBd;

        final String multiSign = "\u00D7";
        final String divSign = "\u00F7";

        switch (mCurrentEquation.getOperator()){
            case "+":
                resultBd = num1Bd.add(num2Bd);
                break;
            case "-":
                resultBd = num1Bd.subtract(num2Bd);
                break;
            case multiSign:
                resultBd = num1Bd.multiply(num2Bd);
                break;
            case divSign:
                resultBd = num1Bd.divide(num2Bd, context);
                break;
            default:
                resultBd = new BigDecimal("0");
                Log.d(TAG, "Something went wrong at doOperation");
                break;
        }
        result = resultBd.toPlainString();

        //Save current equation String as lastEquation
        mLastEquation = mCurrentEquation.toString();
        mCurrentEquation = new Equation();
    }

    public String getmLastEquation() {
        return mLastEquation;
    }
}
