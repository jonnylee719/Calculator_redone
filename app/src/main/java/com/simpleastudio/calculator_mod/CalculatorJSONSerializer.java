package com.simpleastudio.calculator_mod;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Jonathan on 2/10/2015.
 */
public class CalculatorJSONSerializer {
    private static final String TAG = "JSONSerializer";
    private Context mContext;
    private String mFilename_equations;
    private String mFilename_current;

    public CalculatorJSONSerializer(Context c, String f){
        mContext = c;
        mFilename_equations = f;
    }

    public CalculatorJSONSerializer(Context c){
        mContext = c;
    }

    public void saveEquations(ArrayList<Equation> equations)
        throws JSONException, IOException{
        JSONArray array = new JSONArray();
        for(Equation equation : equations){
           array.put(equation.toJSON());
        }

        Writer writer = null;
        try{
            OutputStream out = mContext.openFileOutput(mFilename_equations, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        }finally {
            if (writer != null)
                writer.close();
        }
    }

    public ArrayList<Equation> loadEquations() throws JSONException, IOException{
        ArrayList<Equation> equations = new ArrayList<Equation>();
        BufferedReader reader = null;
        try{
            InputStream in = mContext.openFileInput(mFilename_equations);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine())!= null){
                jsonString.append(line);
            }
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue();
            for(int i = 0; i < array.length(); i++){
                equations.add(new Equation(array.getJSONObject(i)));
            }
        }catch (FileNotFoundException e){}
        finally {
            if(reader!=null)
                reader.close();
        }
        return equations;
    }

    public void saveCurrentEquation(Equation e, String filename)
        throws JSONException, IOException{
        mFilename_current = filename;
        Writer writer = null;
        try{
            OutputStream out = mContext.openFileOutput(mFilename_current, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(e.toJSON().toString());
            Log.d(TAG, "Current equation JSON string: " + e.toJSON().toString());
        }finally {
            if(writer!=null)
                writer.close();
        }
    }

    public Equation getSavedCurrentEquation(String filename) throws JSONException, IOException{
        mFilename_current = filename;
        Equation currentEquation = new Equation();
        BufferedReader reader = null;
        try{
            InputStream in = mContext.openFileInput(mFilename_current);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while((line = reader.readLine())!=null){
                jsonString.append(line);
                Log.d(TAG, "Read JSON string: " + line);
            }
            JSONObject object = new JSONObject(jsonString.toString());
            currentEquation = new Equation(object);
        }catch (FileNotFoundException e){}
        finally {
            if(reader!= null)
                reader.close();
        }
        return currentEquation;
    }
}
