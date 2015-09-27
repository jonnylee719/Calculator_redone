package com.simpleastudio.calculator_mod;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class CalViewActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CalViewFragment();
    }
}
