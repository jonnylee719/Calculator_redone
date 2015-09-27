package com.simpleastudio.calculator_mod;

import android.util.Log;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Created by Jonathan on 26/9/2015.
 */
public class CalManager {
    private static final String TAG = "CalManager";
    //Variables for equation data
    private String num1 = "";
    private boolean negNum1 = false;
    private String formattedNum1;
    
    private String num2 = "";
    private boolean negNum2 = false;
    private String formattedNum2;
    
    private String operator = "";
    private String result = "";
    private String formattedResult;

    //States that the controller can be in:
    //0 = no number entered yet
    //1 = num1 entered
    //2 = num1 entered and operatorText entered
    private boolean num1Entered = false;
    private boolean num2Entered = false;
    private boolean operEntered = false;

    public String numButtonClicked(String number) {
        //4 Scenarios:
        //a) before or after num1Entered
        //b) before or after num2Entered

        if(!operEntered) {
            if (!num1Entered) {
                result = ""; //might be straight after the last operation
                num1 = number;
                num1Entered = true;
            } else if (num1Entered) {
                if (num1.equals("0")) {
                    num1 = number;
                } else {
                    if(num1.indexOf(".") != -1){
                        int dp = num1.indexOf(".");                 //the position of the decimal point
                        String fractionPart = num1.substring(dp);
                        if(fractionPart.length() > 9){             //10 digits after the decimal point is the limit of num1
                            //do Nothing
                        }
                        else{
                            num1 = num1 + number;                   //appends the number
                        }
                    }
                    else {                                          // not a fraction number
                        if(num1.length()>9){
                            //do Nothing
                        }
                        else {
                            num1 = num1 + number;
                        }
                    }
                }
            }
        }
        else if(operEntered){               //oper has been entered
            if(!num2Entered){               //first check if num1 has been entered
                num2 = number;
                num2Entered = true;
            }
            else if(num2Entered){                          //num2 has been entered
                if(num2.equals("0")){
                    num2 = number;
                }
                else{
                    if(num2.indexOf(".") != -1){
                        int dp = num2.indexOf(".");                 //the position of the decimal point
                        String fractionPart = num2.substring(dp);
                        if(fractionPart.length() > 9){             //10 digits after the decimal point is the limit of num
                            //do Nothing
                        }
                        else{
                            num2 = num2 + number;                   //appends the number
                        }
                    }
                    else {                                          // not a fraction number
                        if(num2.length()>9){
                            //do Nothing
                        }
                        else {
                            num2 = num2 + number;
                        }
                    }
                }
            }
        }
        return getDisplayText();
    }

    public String operButtonClicked(String operatorText) {
        // 3 scenarios: 1) num1Entered is false
        //              2) operEntered is false
        //              3) operEntered is true

        if(!num1Entered){           // check for result
            if(!result.equals("")){        //result contains something
                if(result.indexOf("-") == 0){   //result is negative
                    negNum1 = true;
                    num1 = result.substring(1);
                }
                else{
                    num1 = result;
                }
                num1Entered = true;
                result = "";
                operator = operatorText;
                operEntered = true;

            } // end of result equals something
            else{
                num1 = "0";
                num1Entered = true;
                operator = operatorText;
                operEntered = true;
            }
        }
        else if(!operEntered){                                      //Number is entered
            //if(num1.indexOf(".") == (num1.length() - 1)){
            //    num1 = num1.substring(0, (num1.indexOf(".")));     //if dp is at the end of num1
            //}
            operator = operatorText;
            operEntered = true;
        }
        else {           //there's 2 scenarios: 1) num2 not entered, so just switch the oper around, 2) num2 entered, doOperation() and treat as above
            if (!num2Entered) {
                operator = operatorText;
            } else {
                //if (num2.indexOf(".") == (num2.length() - 1)) {
                //    num2 = num2.substring(0, (num2.indexOf(".")));
               // }
                doOperation();
                if (result.indexOf("-") == 0) {
                    negNum1 = true;
                    num1 = result.substring(1);
                } else {                      //result is positive
                    num1 = result;
                }
                num1Entered = true;
                result = "";
                operButtonClicked(operatorText);       //reloop as if this is a new operButClicked
            }
        }

        return getDisplayText();
    }

    private String negNumberConvertor(String number, boolean negative){
        String newNumber;
        if(negative)
            newNumber = "-" + number;
        else
            newNumber = number;
        return newNumber;

    }

    public void doOperation(){
        MathContext context = new MathContext(120, RoundingMode.HALF_UP);

        BigDecimal num1Bd = new BigDecimal(negNumberConvertor(num1, negNum1));
        BigDecimal num2Bd = new BigDecimal(negNumberConvertor(num2, negNum2));
        BigDecimal resultBd;

        final String multiSign = "\u00D7";
        final String divSign = "\u00F7";

        switch (operator){
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

        num1 = "";
        num2 = "";
        operator = "";
        operEntered = false;
        num1Entered = false;
        num2Entered = false;
        negNum1 = false;
        negNum2 = false;
    }

    private String getDisplayText(){
        String displayText;
        if(!num1Entered && !result.equals("")){
            displayText = result;
        }
        else if(!num1Entered){
            displayText = "0";
        }
        else{
            displayText = negNumberConvertor(num1, negNum1) + operator + negNumberConvertor(num2, negNum2);
        }
        return displayText;
    }
}
