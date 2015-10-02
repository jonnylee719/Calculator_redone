package com.simpleastudio.calculator_mod;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

public class CalViewActivity extends SingleFragmentActivity{
    private static final String TAG = "CalViewActivity";

    @Override
    protected Fragment createFragment() {
        return new CalViewFragment();
    }

}
