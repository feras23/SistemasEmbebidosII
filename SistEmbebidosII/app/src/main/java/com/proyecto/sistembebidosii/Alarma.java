package com.proyecto.sistembebidosii;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by Fernando Perez on 27/05/2015.
 */
public class Alarma extends Activity {
    public static boolean activa = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onResume(){
        super.onResume();
        Button btnParar = (Button) findViewById(R.id.detenerAlarma);
        btnParar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        activa = true;
    }

    @Override
    public void onPause(){
        super.onPause();
        activa = false;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        activa = true;
    }

    public static boolean getActiva(){
        return activa;
    }
}
