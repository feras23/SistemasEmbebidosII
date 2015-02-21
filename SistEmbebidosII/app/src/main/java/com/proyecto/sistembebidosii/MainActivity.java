package com.proyecto.sistembebidosii;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


      public boolean onOptionsMenu(Menu menu) {

          getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
        }

        public void lanzar (View view) {
            Intent i = new Intent (this, Activity2.class);
            startActivity(i);


        }

}
