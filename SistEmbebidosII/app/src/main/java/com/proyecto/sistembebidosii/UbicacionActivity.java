package com.proyecto.sistembebidosii;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;


public class UbicacionActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_ubicaciones);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ubicacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_addUbicacion){
            opcAgregar(getCurrentFocus());
            return true;
        }else
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();
        llenarLista();
    }

    public void llenarLista(){
        ManejadorBD bd = new ManejadorBD(this);
        ListaUbicaciones miLista = new ListaUbicaciones(bd.obtenerUbicaciones(),this);
        ListView lView = (ListView) this.findViewById(R.id.locationsList);
        lView.setAdapter(miLista);
    }

    public void opcAgregar (View view) {
        Intent i = new Intent (this, MapaActivity.class);
        i.putExtra("activity","Ubicacion");
        startActivity(i);
    }



}
