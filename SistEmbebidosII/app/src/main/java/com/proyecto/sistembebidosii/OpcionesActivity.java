package com.proyecto.sistembebidosii;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;


public class OpcionesActivity extends PreferenceActivity {

    public SharedPreferences prefs;
    Vibrator v;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.opciones);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager().findPreference("vibrar");//key name in preference xml

        checkboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                if (newValue.toString().equals("true"))
                {
                    v.vibrate(1000);
                }
                return true;
            }
        });
    }


}
