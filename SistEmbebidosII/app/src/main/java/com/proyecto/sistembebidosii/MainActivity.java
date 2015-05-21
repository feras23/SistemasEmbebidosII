package com.proyecto.sistembebidosii;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, OpcionesActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    public void opcMapa(View view) {
        Intent i = new Intent(this, MapaActivity.class);
        i.putExtra("activity","Main");
        startActivity(i);
    }

    public void opcAjustes(View view) {
        Intent i = new Intent(this, AjustesActivity.class);
        startActivity(i);
    }

    public void opcUbicacion(View view) {
        Intent i = new Intent(this, UbicacionActivity.class);
        startActivity(i);
    }

    /*public void opcOpciones(View view) {
        Intent i = new Intent(this, OpcionesActivity.class);
        startActivity(i);
    }*/
}
