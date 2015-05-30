package com.proyecto.sistembebidosii;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by Fernando Perez on 27/05/2015.
 */
public class Alarma extends Activity {
    Vibrator v = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma);
        String titulo = getIntent().getStringExtra("Titulo");
        String direccion = getIntent().getStringExtra("Direccion");
        TextView txtTitulo = (TextView) findViewById(R.id.nombreDestino);
        TextView txtDireccion = (TextView) findViewById(R.id.direccionDestino);
        txtTitulo.setText(titulo);
        txtDireccion.setText(direccion);
        SharedPreferences getAlarms = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String alarms = getAlarms.getString("pref_tone", "default ringtone");
        Uri uri = Uri.parse(alarms);
        SharedPreferences getCheckBox = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean checkBox = getCheckBox.getBoolean("vibrar", true);
        if(checkBox){
            v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            int long_gap = 1000;
            long[] pattern = {
                    200,  // Start immediately
                    long_gap
            };
            v.vibrate(pattern,0);
        }
        playSound(this, uri);
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
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy(){
        stopAlarm(getCurrentFocus());
        if (v!= null)
            v.cancel();
        super.onDestroy();
    }

    public void stopAlarm(View view){
        SharedPreferences getAlarms = PreferenceManager.
                getDefaultSharedPreferences(getBaseContext());
        String alarms = getAlarms.getString("pref_tone", "default ringtone");
        Uri uri = Uri.parse(alarms);
        stopSound(this, uri);
        //call mMediaPlayer.stop(); when you want the sound to stop
    }

    private MediaPlayer mMediaPlayer;
    private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopSound(Context context, Uri alert){
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();

        }

    }
}
