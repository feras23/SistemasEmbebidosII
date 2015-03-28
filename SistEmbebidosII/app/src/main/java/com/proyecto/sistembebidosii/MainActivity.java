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
      public boolean onOptionsMenu(Menu menu) {
          getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menu) {
            System.out.println("Se presionó el menú");
        if (id == R.id.action_settings) {
            System.out.println("Se presionó configuración");
            return true;
        }else if (id == R.id.action_search)
            System.out.println("Se presionó la búsqueda");

        return super.onOptionsItemSelected(item);
    }

    public void lanzar (View view) {
        Intent i = new Intent (this, Activity2.class);
        startActivity(i);
    }
        public void lanzar (View view) {
            Intent i = new Intent (this, Activity2.class);
            startActivity(i);
        }



>>>>>>> origin/DevelopmentFanny
}
