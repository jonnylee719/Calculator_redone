package com.simpleastudio.calculator_mod;

import android.content.Context;
import android.util.Log;
import android.widget.TableRow;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by Jonathan on 29/9/2015.
 */
public class NumberFormatter implements Serializable {
    private static final String TAG = "NumberFormatter";
    private NumberFormat nf;
    private Locale currentLocale;
    private char dp;

    public NumberFormatter(){
        currentLocale = Locale.getDefault();
        dp = DecimalFormatSymbols.getInstance(currentLocale).getDecimalSeparator();
    }

    public String formatNumber(String inputNumber){
        String formattedWhole = "";
        String formattedFraction = "";
        String displayNumber = "something has gone wrong";
        if(conditionZero(inputNumber)){
            return inputNumber;
        }
        String whole;
        String fraction;
        if(inputNumber.contains(".")){
            int dpIndex = inputNumber.indexOf(".");
            whole = inputNumber.substring(0, dpIndex);
            fraction = inputNumber.substring(dpIndex+1);
        }
        else {
            whole = inputNumber;
            fraction = "";
        }


        if(needExponential(whole, fraction)){
            displayNumber = exponentialFormat(inputNumber);
        }
        else{
            formattedWhole = formatWhole(whole);
            formattedFraction = formatFraction(fraction);
            if(formattedFraction.equals("")){
                displayNumber = formattedWhole;
            }
            else if(formattedFraction.equals("roundTo1")){
                String newInputNumber = (Long.parseLong(whole) + 1) + "";
                Log.d(TAG, "newInputNumber after rounding up: " + newInputNumber);
                displayNumber = formatNumber(newInputNumber);
            }
            else{
                displayNumber = formattedWhole + dp + formattedFraction;
            }
        }
        Log.d(TAG, "displayNumber is: " + displayNumber);
        return displayNumber;
    }

    private String formatWhole(String whole){
        Long wholeLong = Long.parseLong(whole);
        nf = NumberFormat.getInstance(currentLocale);
        Log.d(TAG, "formattedWhole: " + nf.format(wholeLong));
        return nf.format(wholeLong);
    }

    private String formatFraction(String fraction){
        Double checkFraction = Double.parseDouble("0." + fraction);
        String formattedFraction;
        if(checkFraction == 0){
            formattedFraction = fraction;
        }
        else{
            if(checkFraction < 0.000000001){ //To insignificant to be displayed at a maximum of 9 decimal places
                formattedFraction = "";
            }
            else{
                formattedFraction = roundFraction(fraction);
            }
        }
        Log.d(TAG, "formattedFraction: " + formattedFraction);
        return formattedFraction;
    }

    private String roundFraction(String fraction){
        String roundedFraction;
        int length = fraction.length();
        if(length>10){              //Need to round the fraction
            String ninthDecimal = fraction.substring(0,9);
            Double ninthPlace = Double.parseDouble(ninthDecimal);
            Log.d(TAG, "ninthDecimal: " + ninthDecimal);
            int tenthPlace = Integer.parseInt(String.valueOf(fraction.charAt(9)));
            if(tenthPlace >= 5){        //round up
                ninthPlace = ninthPlace + 0.000000001;
            }
            if(ninthPlace == 1){       //fraction round up to 1.0
                roundedFraction = "roundTo1";
            }
            else {                              //fraction doesnt need to be rounded up
                roundedFraction = String.valueOf(ninthDecimal);
            }
        }
        else {
            roundedFraction = fraction;
        }
        return roundedFraction;
    }

    private boolean needExponential(String whole, String fraction){
        boolean needExpo = false;
        Double checkFraction = Double.parseDouble("0." + fraction);
        if(whole.length() > 10){
            needExpo = true;
        }
        else if((whole.equals("0") || whole.equals("-0")) &&
                (checkFraction != 0 && checkFraction < 0.000000001)){
            needExpo = true;
        }
        return needExpo;
    }

    private String exponentialFormat(String inputNumber){
        BigDecimal bd = new BigDecimal(inputNumber);
        nf = new DecimalFormat("0.###E0");
        return nf.format(bd);
    }

    private boolean conditionZero(String inputNumber){
        boolean conditionZero = false;
        if(inputNumber.equals("")||
                inputNumber.equals("-")||
                inputNumber.equals("0")||
                inputNumber.equals("-0")){
            conditionZero = true;
        }
        else if(inputNumber.indexOf(".") == inputNumber.length()-1){
            conditionZero = true;
        }
        return conditionZero;
    }
}
