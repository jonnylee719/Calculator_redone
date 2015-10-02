package com.simpleastudio.calculator_mod;

import android.support.v4.app.Fragment;

/**
 * Created by Jonathan on 1/10/2015.
 */
public class CalHistoryActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CalHistoryListFragment();
    }
}
