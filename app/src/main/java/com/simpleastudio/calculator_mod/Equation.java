package com.simpleastudio.calculator_mod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Jonathan on 27/9/2015.
 */
public class Equation implements Serializable{
    //For JSON serialization
    private static final String JSON_NUM1 = "num1";
    private static final String JSON_NEGNUM1 = "negNum1";
    private static final String JSON_NUM2 = "num2";
    private static final String JSON_NEGNUM2 = "negNum2";
    private static final String JSON_RESULT = "result";
    private static final String JSON_OPERATOR = "operator";
    private static final String JSON_NUM1ENTERED = "num1Entered";
    private static final String JSON_NUM2ENTERED = "num2Entered";
    private static final String JSON_OPERENTERED = "operEntered";

    //Variables for equation data
    private String num1 = "0";
    private boolean negNum1 = false;

    private String num2 = "";
    private boolean negNum2 = false;

    private String operator = "";

    private String result = "";
    //States that the controller can be in:
    //0 = no number entered yet
    //1 = num1 entered
    //2 = num1 entered and operatorText entered
    private boolean num1Entered = false;
    private boolean num2Entered = false;
    private boolean operEntered = false;

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_NUM1, num1);
        json.put(JSON_NEGNUM1, negNum1);
        json.put(JSON_NUM2, num2);
        json.put(JSON_NEGNUM2, negNum2);
        json.put(JSON_OPERATOR, operator);
        json.put(JSON_RESULT, result);
        json.put(JSON_NUM1ENTERED, num1Entered);
        json.put(JSON_NUM2ENTERED, num2Entered);
        json.put(JSON_OPERENTERED, operEntered);
        return json;
    }

    public Equation(JSONObject json) throws JSONException{
        num1 = json.getString(JSON_NUM1);
        num2 = json.getString(JSON_NUM2);
        negNum1 = json.getBoolean(JSON_NEGNUM1);
        negNum2 = json.getBoolean(JSON_NEGNUM2);
        result = json.getString(JSON_RESULT);
        operator = json.getString(JSON_OPERATOR);
        num1Entered = json.getBoolean(JSON_NUM1ENTERED);
        num2Entered = json.getBoolean(JSON_NUM2ENTERED);
        operEntered = json.getBoolean(JSON_OPERENTERED);
    }

    public Equation(){

    }

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
        return negNumberConvertor(num1, negNum1) + operator + negNumberConvertor(num2, negNum2);
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
