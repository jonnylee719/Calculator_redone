package com.simpleastudio.calculator_mod;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Jonathan on 26/9/2015.
 */
public class CalManager implements Serializable{
    private static final String TAG = "CalManager";
    private Context mAppContext;
    private String result = "";
    private String mLastEquation = "";
    private Equation mCurrentEquation;
    private NumberFormatter nf = new NumberFormatter();
    private CalculatorJSONSerializer mSerializer;
    private final static String FILENAME_CURRENT_EQUATION = "currentEquation.json";

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void saveCurrentEquation(){
        try{
            mSerializer.saveCurrentEquation(mCurrentEquation, FILENAME_CURRENT_EQUATION);
            Log.d(TAG, "Saved current equation to file.");
        }catch (Exception e){
            Log.e(TAG, "Error in saving current equation." + e);
        }
    }

    public void loadCurrentEquation(){
        try{
            Equation equation = mSerializer.getSavedCurrentEquation(FILENAME_CURRENT_EQUATION);
            mCurrentEquation = equation;
            Log.d(TAG, "Loaded current equation from file.");
        }catch (Exception e){
            Log.e(TAG, "Error in loading current equation." + e);
        }
    }

    public void setmLastEquation(String equation) {
        mLastEquation = equation;
    }


    public CalManager(Context context){
        mAppContext = context;
        mCurrentEquation = new Equation();
        mSerializer = new CalculatorJSONSerializer(mAppContext);
    }

    public void resetToNewEquation(Equation e){
        Log.d(TAG, "CurrentEquation is set to: " + e.toString());
        mCurrentEquation = e;
        result = "";
        mLastEquation = "";
    }

    private String mCurrentEquationToString() {
        Log.d(TAG, "CurrentEquation: " + mCurrentEquation.toString());
        return nf.formatNumber(mCurrentEquation.getFormattedNum1())
                + mCurrentEquation.getOperator()
                + nf.formatNumber(mCurrentEquation.getFormattedNum2());
    }

    public String getmCurrentEquationDisplay(){
        String currentDisplay;
        if(!result.equals("")){
            currentDisplay = nf.formatNumber(result);
        }
        else{
            currentDisplay = mCurrentEquationToString();
        }
        return currentDisplay;
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
                    if(mCurrentEquation.getNum1().contains(".")){
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
                        if(mCurrentEquation.getNum1().length()<10){
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
                    if(mCurrentEquation.getNum2().contains(".")){
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
        return mCurrentEquationToString();
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
            mCurrentEquation.setOperator(operatorText);
            mCurrentEquation.setOperEntered(true);
        }
        else {           //there's 2 scenarios: 1) num2 not entered, so just switch the oper around, 2) num2 entered, doOperation() and treat as above
            if (!mCurrentEquation.isNum2Entered()) {
                mCurrentEquation.setOperator(operatorText);
            } else {
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
        return mCurrentEquationToString();
    }

    public String equalButtonClicked(){
        //4 scenarios:
        //1) before num1Entered
        //2) after num1Entered + before operEntered
        //3) after operEntered + before num2Entered
        //4) after num2Entered
        String displayText = "Error at EqualButtonClicked()";

        if(!result.equals("")){
            displayText = result;
        }
        else if(!mCurrentEquation.isNum2Entered()){
            displayText = mCurrentEquation.getNum1();
        }
        if(mCurrentEquation.isNum1Entered() && !mCurrentEquation.isNum2Entered()){
            result = negNumberConvertor(mCurrentEquation.getNum1(), mCurrentEquation.isNegNum1());
            mCurrentEquation.setNum1("");
            mCurrentEquation.setNegNum1(false);
            mCurrentEquation.setNum1Entered(false);
            mCurrentEquation.setOperator("");
            mCurrentEquation.setOperEntered(false);
            displayText = result;

        }
        else if(mCurrentEquation.isNum2Entered()){
            doOperation();
            displayText = result;
        }
        return nf.formatNumber(displayText);
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
            if(length>11){
                String newNum1 = nf.formatNumber(mCurrentEquation.getNum1());
                mCurrentEquation.setNum1(newNum1);
                length = mCurrentEquation.getNum1().length();
            }
            String newNum1 = mCurrentEquation.getNum1().substring(0, (length - 1));
            Log.d(TAG, "newNum1 string after cancelButClicked: " + newNum1);
            mCurrentEquation.setNum1(newNum1);

            if(length-1 == 0){
                mCurrentEquation.setNum1("0");
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
            if(length>11){
                String newNum2 = nf.formatNumber(mCurrentEquation.getNum2());
                mCurrentEquation.setNum2(newNum2);
                length = mCurrentEquation.getNum2().length();
            }
            String newNum2 = mCurrentEquation.getNum2().substring(0, (length-1));
            mCurrentEquation.setNum2(newNum2);

            if(length-1 == 0){
                mCurrentEquation.setNum2("");
                mCurrentEquation.setNum2Entered(false);
            }
        }
        return mCurrentEquationToString();
    }

    public String dpButtonClicked(){
        if(!mCurrentEquation.isOperEntered()){
            if(!mCurrentEquation.isNum1Entered()){
                mCurrentEquation.setNum1("0.");
                mCurrentEquation.setNum1Entered(true);
            }
            else if(mCurrentEquation.isNum1Entered()){
                if(!mCurrentEquation.getNum1().contains(".")){
                    String newNum1 = mCurrentEquation.getNum1() + ".";
                    mCurrentEquation.setNum1(newNum1);
                }
            }
        }
        else if(mCurrentEquation.isOperEntered()) {
            if (!mCurrentEquation.isNum2Entered()) {
                mCurrentEquation.setNum2("0.");
                mCurrentEquation.setNum2Entered(true);
            } else if (mCurrentEquation.isNum2Entered()) {
                if (!mCurrentEquation.getNum2().contains(".")) {
                    String newNum2 = mCurrentEquation.getNum2() + ".";
                    mCurrentEquation.setNum2(newNum2);
                }
            }
        }
        return mCurrentEquationToString();
    }

    public String negButtonClicked(){
        if(!mCurrentEquation.isOperEntered()){
            if(!mCurrentEquation.isNum1Entered()){
                mCurrentEquation.setNum1("0");
                mCurrentEquation.setNum1Entered(true);
            }
            if(!mCurrentEquation.isNegNum1()){
                mCurrentEquation.setNegNum1(true);
            }
            else{
                mCurrentEquation.setNegNum1(false);
            }
            result = ""; //this might be straight after last operation
        }
        else if(mCurrentEquation.isOperEntered()){
            if(!mCurrentEquation.isNegNum2()){
                mCurrentEquation.setNegNum2(true);
            }
            else{
                mCurrentEquation.setNegNum2(false);
            }
        }
        return mCurrentEquationToString();
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

        BigDecimal num1Bd = new BigDecimal(negNumberConvertor(mCurrentEquation.getNum1(), mCurrentEquation.isNegNum1()), context);
        BigDecimal num2Bd = new BigDecimal(negNumberConvertor(mCurrentEquation.getNum2(), mCurrentEquation.isNegNum2()), context);
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
        Log.d(TAG, "resultBD is: " + resultBd);
        result = resultBd.stripTrailingZeros().toPlainString();
        mCurrentEquation.setResult(result);

        //Save current equation String as lastEquation
        mLastEquation = mCurrentEquationToString();
        CalHistory.get(mAppContext).addEquation(mCurrentEquation);
        mCurrentEquation = new Equation();
    }

    public String getmLastEquation() {
        return mLastEquation;
    }

    public void saveEquations(){
        CalHistory.get(mAppContext).saveEquations();
    }
}
