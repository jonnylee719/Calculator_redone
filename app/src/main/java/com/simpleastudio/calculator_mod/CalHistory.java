package com.simpleastudio.calculator_mod;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jonathan on 1/10/2015.
 */
public class CalHistory implements Serializable{
    private static CalHistory mCalHistory;
    private Context mAppContext;
    private ArrayList<Equation> mEquations;

    private CalHistory(Context appContext){
        mAppContext = appContext;
        mEquations = new ArrayList<Equation>();
    }

    public static CalHistory get(Context context){
        if(mCalHistory == null){
            mCalHistory = new CalHistory(context.getApplicationContext());
        }
        return mCalHistory;
    }

    public ArrayList<Equation> getmEquations() {
        return mEquations;
    }

    public void addEquation(Equation equation){
        mEquations.add(equation);
    }

    public void clearCalHistory(){
        mEquations.clear();
    }
}
