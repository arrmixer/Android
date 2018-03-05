package com.example.andriod.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by Angel on 3/2/18.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
