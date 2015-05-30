package com.proyecto.sistembebidosii;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    Menu menus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menus = menu;
        Switch swt = (Switch) menu.findItem(R.id.switchOnOff).getActionView().findViewById(R.id.switchForActionBar);
        if(isMyServiceRunning(ServicioUbicacion.class))
            swt.setChecked(true);
        else
            swt.setChecked(false);
        swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ManejadorBD bd = new ManejadorBD(getApplicationContext());
                    if (bd.cuentaUbicacionesActivas() > 0) {
                        empezarServicio(ServicioUbicacion.TAG);
                    } else {
                        Toast.makeText(getApplicationContext(), "No hay ubicaciones activas.", Toast.LENGTH_SHORT).show();
                        buttonView.setChecked(false);
                    }
                } else {
                    detenerServicio(ServicioUbicacion.TAG);
                }
            }
        });
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

    public void opcUbicacion(View view) {
        Intent i = new Intent(this, UbicacionActivity.class);
        startActivity(i);
    }

    private void empezarServicio(final String tag){
            Intent intent = new Intent(getApplicationContext(), ServicioUbicacion.class);
            intent.addCategory(tag);
            startService(intent);
    }

    private void detenerServicio(final String tag){
        Intent intent = new Intent(getApplicationContext(), ServicioUbicacion.class);
        intent.addCategory(tag);
        stopService(intent);
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
