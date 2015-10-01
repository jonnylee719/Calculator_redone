package com.simpleastudio.calculator_mod;

import java.io.Serializable;

/**
 * Created by Jonathan on 27/9/2015.
 */
public class Equation implements Serializable{
    //Variables for equation data
    private String num1 = "0";
    private boolean negNum1 = false;
    private String formattedNum1;

    private String num2 = "";
    private boolean negNum2 = false;
    private String formattedNum2;

    private String operator = "";

    //States that the controller can be in:
    //0 = no number entered yet
    //1 = num1 entered
    //2 = num1 entered and operatorText entered
    private boolean num1Entered = false;
    private boolean num2Entered = false;
    private boolean operEntered = false;
    
    public String getNum1() {
        return num1;
    }

    public void setNum1(String num1) {
        this.num1 = num1;
    }

    public boolean isNegNum1() {
        return negNum1;
    }

    public void setNegNum1(boolean negNum1) {
        this.negNum1 = negNum1;
    }

    public String getNum2() {
        return num2;
    }

    public void setNum2(String num2) {
        this.num2 = num2;
    }

    public boolean isNegNum2() {
        return negNum2;
    }

    public void setNegNum2(boolean negNum2) {
        this.negNum2 = negNum2;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public boolean isNum1Entered() {
        return num1Entered;
    }

    public void setNum1Entered(boolean num1Entered) {
        this.num1Entered = num1Entered;
    }

    public boolean isNum2Entered() {
        return num2Entered;
    }

    public void setNum2Entered(boolean num2Entered) {
        this.num2Entered = num2Entered;
    }

    public boolean isOperEntered() {
        return operEntered;
    }

    public void setOperEntered(boolean operEntered) {
        this.operEntered = operEntered;
    }
    
    public String toString(){
        String equationString = negNumberConvertor(num1, negNum1) + operator + negNumberConvertor(num2, negNum2);
        return equationString;
    }

    private String negNumberConvertor(String number, boolean negative){
        String newNumber;
        if(negative)
            newNumber = "-" + number;
        else
            newNumber = number;
        return newNumber;

    }

    public String getFormattedNum1(){
        return negNumberConvertor(num1, negNum1);
    }

    public String getFormattedNum2(){
        return negNumberConvertor(num2, negNum2);
    }

}
