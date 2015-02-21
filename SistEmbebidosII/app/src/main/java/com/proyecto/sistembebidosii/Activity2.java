package com.proyecto.sistembebidosii;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;



public class Activity2 extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity2);
    }
    public boolean onOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity2, menu);
        return true;
    }


}
