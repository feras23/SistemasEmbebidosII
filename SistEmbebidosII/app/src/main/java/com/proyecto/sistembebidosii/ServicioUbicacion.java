package com.proyecto.sistembebidosii;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import static android.location.Location.distanceBetween;


public class ServicioUbicacion extends Service {

    public static final String TAG = "UbicacionGPS";
    private static final int NOTIFICATION_ID = 1;
    private LocationManager mLocationManager = null;
    private ManejadorBD bd;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private int cantidadUbicaciones;
    private List<Ubicacion> ubicaciones = null;

    private class LocationListener implements android.location.LocationListener{
        Location mLastLocation;
        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }
        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            int ubicacionesActuales = bd.cuentaUbicacionesActivas();
            if (cantidadUbicaciones != ubicacionesActuales){
                cantidadUbicaciones = ubicacionesActuales;
                ubicaciones = bd.obtenerUbicacionesActivas();
            }
            if (ubicaciones != null){
                if (!ubicaciones.isEmpty()){
                    estaCerca(location);
                }
            }
        }
        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }
        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }
    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");
        cantidadUbicaciones = 0;
        bd = new ManejadorBD(this);
        initializeLocationManager();
        crearNotificacion();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }
    @Override
    public void onDestroy()
    {
        cancelarNotificacion();
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }
    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private boolean estaCerca(Location location){
        float [] results= new float [2];
        for (Ubicacion ub : ubicaciones){
            distanceBetween(location.getLatitude(), location.getLongitude(), ub.getLatitud(), ub.getLongitud(), results);
            Log.d("Distancia: ", String.valueOf(results[0]));
            if (results[0]<=100){
                Log.d("YA LLEGO BITCH!!!","Fuck yeah!");
                if (!Alarma.getActiva()) {
                    Intent i = new Intent(this, Alarma.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            }
        }
        return true;
    }

    private void crearNotificacion(){
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("Wake Me App!!")
                .setContentText("Midiendo distancias...")
                .setSmallIcon(R.drawable.ic_launcher2)
                .setOngoing(true)
                .setAutoCancel(false);

        Intent intent = new Intent(this, Alarma.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Alarma.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,  PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void actualizarStatusNotificacion(String inString)
    {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentText(inString)
                .setContentTitle("Wake Me App!!")
                .setSmallIcon(R.drawable.ic_launcher)
                .setOngoing(true)
                .setAutoCancel(false);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,  PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void cancelarNotificacion()
    {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(NOTIFICATION_ID);
    }
}
