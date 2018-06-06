package com.example.karthik.colorduneswallpaper;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by karthik on 6/6/18.
 */

public class TimePreferenceActivity extends PreferenceActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs);

    }



}
