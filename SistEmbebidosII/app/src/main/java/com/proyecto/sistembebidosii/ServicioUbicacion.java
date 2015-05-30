package com.proyecto.sistembebidosii;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Switch;
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
    private double [] alarmasActivadas;
    int contAlarma;

    private class LocationListener implements android.location.LocationListener{
        Location mLastLocation;
        public LocationListener(String provider)
        {
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
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
    @Override
    public void onCreate()
    {
        cantidadUbicaciones = 0;
        contAlarma = 0;
        bd = new ManejadorBD(this);
        alarmasActivadas =  new double[bd.cuentaUbicacionesActivas()];
        initializeLocationManager();
        crearNotificacion();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void onDestroy()
    {
        cancelarNotificacion();
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private boolean estaCerca(Location location){
        float [] results= new float [2];
        Ubicacion ub = obtenerMasCercano(location);
        if (String.valueOf(ub.getID()) != null){
            distanceBetween(location.getLatitude(), location.getLongitude(), ub.getLatitud(), ub.getLongitud(), results);
            Log.d("Distancia: ", String.valueOf(results[0]));
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String distanciaPreffered = prefs.getString("distancia","150");
            if (results[0]<=Float.parseFloat(distanciaPreffered) && !seActivoAlarmaYa(ub.getID())){
                Intent i = new Intent(this, Alarma.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String datos [] = new String [2];
                datos[0] = ub.getTitulo();
                datos[1] = ub.getDireccion();
                i.putExtra("Titulo", ub.getTitulo());
                i.putExtra("Direccion", ub.getDireccion());
                startActivity(i);
                alarmasActivadas[contAlarma]= ub.getID();
                contAlarma++;
                actualizarStatusNotificacion("Se llego a la ubicacion: " + ub.getTitulo());
            }
            if (contAlarma == bd.cuentaUbicacionesActivas()){
                Toast.makeText(getApplicationContext(),"Ya no hay ubicaciones pendientes. Se apagará el servicio.",Toast.LENGTH_LONG).show();
                onDestroy();
            }
        }
        return true;
    }

    private Ubicacion obtenerMasCercano(Location location){
        float [] results= new float [2];
        double distancia = 10000;
        Ubicacion ubi = new Ubicacion();
        for(Ubicacion ub : ubicaciones) {
            distanceBetween(location.getLatitude(), location.getLongitude(), ub.getLatitud(), ub.getLongitud(), results);
            if (results[0] < distancia){
                distancia = results[0];
                ubi = ub;
            }
        }
        return ubi;
    }

    private boolean seActivoAlarmaYa(int id){
        for (int i = 0; i < bd.cuentaUbicacionesActivas(); i ++){
            if (alarmasActivadas[i] == id){
                return true;
            }
        }
        return false;
    }
    private void crearNotificacion(){
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("Wake Me App!")
                .setContentText("Midiendo distancias...")
                .setSmallIcon(R.drawable.ic_launcher2)
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

    public void actualizarStatusNotificacion(String inString)
    {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentText(inString)
                .setContentTitle("Wake Me App!")
                .setSmallIcon(R.drawable.ic_launcher2)
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
