package com.proyecto.sistembebidosii;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by miguel on 16/05/2015.
 */
public class OpcionesActivity extends PreferenceActivity {

    public SharedPreferences prefs;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        addPreferencesFromResource(R.xml.opciones);
    }




}
