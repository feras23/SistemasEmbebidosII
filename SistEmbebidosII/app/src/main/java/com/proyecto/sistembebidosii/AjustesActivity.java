package com.proyecto.sistembebidosii;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.media.AudioManager;
import android.widget.SeekBar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.TextView;


public class AjustesActivity extends ActionBarActivity {



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ajustes, menu);
        return true;
    }



    SoundManager snd;
    int laser, explode, pickup, meow, bark, moo;
    SeekBar.OnSeekBarChangeListener barChange;
    View.OnClickListener buttonClick;
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        // Create an instance of our sound manger
        snd = new SoundManager(getApplicationContext());

        // Set volume rocker mode to media volume
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Load the samples from res/raw
        laser = snd.load(R.raw.laser);
        explode = snd.load(R.raw.explosion);
        pickup = snd.load(R.raw.pickup);
        meow = snd.load(R.raw.cat);
        bark = snd.load(R.raw.barkloud);
        moo = snd.load(R.raw.cow);

        // Create a seek bar handler
        barChange = new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {	}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {  }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                switch (seekBar.getId())
                {
                    case R.id.volBar1:
                        snd.setVolume((float)progress/100.0f);
                        break;


                }
            }
        };

        // Set our handler as the ChangeListener for the seekbar controls
        SeekBar sb;
        sb = (SeekBar)findViewById(R.id.volBar1);
        sb.setOnSeekBarChangeListener(barChange);

        final TextView button2;
        button2 = (TextView) findViewById(R.id.sonido);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(AjustesActivity.this, button2);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                     private int opcionSeleccionada = 0;
                                                     public boolean onMenuItemClick(MenuItem item) {
                                                         int id = v.getId(); // Use the button id to determine which sample should be played

                                                         switch (item.getItemId()) {
                                                             case R.id.one:
                                                                 opcionSeleccionada = 1;
                                                                 item.setChecked(true);
                                                                 snd.play(laser);
                                                                 Log.i("---", "Alarma1");
                                                                 break;

                                                             case R.id.two:
                                                                 opcionSeleccionada = 2;
                                                                 item.setChecked(true);
                                                                 snd.play(explode);
                                                                 Log.i("---", "Alarma2");
                                                                 break;

                                                             case R.id.three:
                                                                 opcionSeleccionada = 3;
                                                                 item.setChecked(true);
                                                                 snd.play(moo);
                                                                 Log.i("---", "Alarma3");
                                                                 break;


                                                         }
                                                         return true;
                                                     }
                                                 }
                );

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method */


    }


}
