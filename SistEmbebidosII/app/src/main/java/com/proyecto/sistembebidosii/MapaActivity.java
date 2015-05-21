package com.proyecto.sistembebidosii;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.provider.Settings;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapaActivity extends Activity implements OnMapReadyCallback{


    private static final int GPS_ERRORDIALOG_REQUEST = 9001;
    private ManejadorBD bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        bd = new ManejadorBD(this);
    }

    @Override
    public void onMapReady( GoogleMap map) {
        if (verificarConexion()) {
            if (isLocationEnabled(this)){
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMapToolbarEnabled(false);
            }else {
                Toast.makeText(this, "La localización está apagada", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show();
        }
        if(getIntent().getStringExtra("activity").equals("Main")){
            MapaNormal(map);
        }else {
            AgregarUbicacion(map);
        }
    }

    protected void AgregarUbicacion(final GoogleMap map){
        final Marker[] nuevoMarker = new Marker[1];
        final String[] Direccion = {""};
        final Button btnCancelar = (Button) findViewById(R.id.btnCancelar);
        final EditText titulo = (EditText) findViewById(R.id.nombreLugar);
        final Button btnAceptar = (Button) findViewById(R.id.botonAceptar);
        final GoogleMap.OnMapClickListener mapClickListener = new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (nuevoMarker[0] != null) {
                    nuevoMarker[0].remove();
                }
                btnCancelar.setVisibility(View.VISIBLE);
                btnAceptar.setVisibility(View.VISIBLE);
                titulo.setVisibility(View.VISIBLE);
                Direccion[0] = getDireccion(latLng);
                nuevoMarker[0] = map.addMarker(new MarkerOptions().position(latLng).title("Destino").snippet(Direccion[0]));
                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nuevoMarker[0].remove();
                        btnCancelar.setVisibility(View.INVISIBLE);
                        btnAceptar.setVisibility(View.INVISIBLE);
                        titulo.setVisibility(View.INVISIBLE);
                    }
                });
            }
        };
        map.setOnMapClickListener(mapClickListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirmar");
        builder.setMessage("¿Está seguro que quiere agregar esta ubicación?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (nuevoMarker[0] != null)
                    if (!titulo.getText().equals(null)){
                        guardarUbicacion(titulo.getText().toString(),Direccion[0],nuevoMarker[0]);
                        finish();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Debe de ingresar un título.",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(),"No se seleccionó ningún lugar. Dar clic en el mapa.", Toast.LENGTH_LONG).show();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alert = builder.create();
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();
            }
        });
    }

    protected void guardarUbicacion(String titulo, String direccion, Marker marker){
            Ubicacion ubicacion = new Ubicacion(titulo, direccion, marker.getPosition().latitude, marker.getPosition().longitude,0);
            if (bd.agregarUbicacion(ubicacion))
                Toast.makeText(this,"Se agregó exitosamente.",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this,"Error al agregar.",Toast.LENGTH_LONG).show();
    }


    protected void MapaNormal(GoogleMap map){
        List<Ubicacion> ubicaciones= bd.obtenerUbicacionesActivas();
        if (!ubicaciones.isEmpty()) {
            if (ubicaciones.size() > 0){
                for (Ubicacion ub : ubicaciones){
                    map.addMarker(new MarkerOptions().position(new LatLng(ub.getLatitud(),ub.getLongitud())).title(ub.getTitulo()).snippet(ub.getDireccion()));
                }
            }
        }else{
            Toast.makeText(this,"No hay ubicaciones dadas de alta.", Toast.LENGTH_LONG).show();
        }
    }

    protected String getDireccion(LatLng ubicacion){
        String direccion = "No se encontró dirección";
        try {
            Geocoder geo = new Geocoder(MapaActivity.this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(ubicacion.latitude, ubicacion.longitude, 0);
            if (!addresses.isEmpty())
                if (addresses.size() > 0) {
                    direccion = (addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName()).toString();
                    Toast.makeText(getApplicationContext(), "Dirección: " + direccion, Toast.LENGTH_LONG).show();
                }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return direccion;
    }

    protected boolean verificarConexion(){
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(status == ConnectionResult.SUCCESS){
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
                    this, GPS_ERRORDIALOG_REQUEST);
            dialog.show();
        }  else {
            Toast.makeText(this, "No se puede conectar a Google Maps", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }
}