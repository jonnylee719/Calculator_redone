package com.simpleastudio.calculator_mod;

import android.content.Context;
import android.util.Log;
import android.widget.TableRow;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jonathan on 1/10/2015.
 */
public class CalHistory implements Serializable{
    private static final String TAG = "CalHistory";
    private static final String FILENAME = "equations.json";

    private static CalHistory mCalHistory;
    private CalculatorJSONSerializer mSerializer;

    private Context mAppContext;
    private ArrayList<Equation> mEquations;

    private CalHistory(Context appContext){
        mAppContext = appContext;
        mSerializer = new CalculatorJSONSerializer(mAppContext, FILENAME);
        try{
            mEquations = mSerializer.loadEquations();
        }catch (Exception e){
            mEquations = new ArrayList<Equation>();
            Log.e(TAG, "Error loading equations: " + e);
        }
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

    public boolean saveEquations(){
        try {
            mSerializer.saveEquations(mEquations);
            Log.d(TAG, "equations saved to file");
            return true;
        }
        catch (Exception e) {
            Log.e(TAG, "Error saving equations", e);
            return false;
        }
    }
}
