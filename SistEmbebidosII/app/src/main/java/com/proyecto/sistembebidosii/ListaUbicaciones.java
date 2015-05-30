package com.proyecto.sistembebidosii;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListaUbicaciones extends BaseSwipeAdapter implements ListAdapter {
    private ManejadorBD bd;
    private List<Ubicacion> list = new ArrayList<Ubicacion>();
    private Context context;

    public ListaUbicaciones(List<Ubicacion> list, Context context) {
        this.list = list;
        this.context = context;
        bd = new ManejadorBD(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getID();
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int i, ViewGroup viewGroup) {
            return LayoutInflater.from(context).inflate(R.layout.activity_ubicacion, null);
    }

    @Override
    public void fillValues(final int i, final View view) {
        final Ubicacion  ub= list.get(i);
        TextView listItemText = (TextView)view.findViewById(R.id.titUbicacion);
        listItemText.setText(ub.getTitulo());

        Switch btnToggle = (Switch)view.findViewById(R.id.btnToggle);
        final int activo = ub.getActivo();
        if (activo == 1){
            btnToggle.setChecked(true);
        }else{
            btnToggle.setChecked(false);
        }
        btnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    bd.actualizarEstado(ub,1);
                }else{
                    bd.actualizarEstado(ub,0);
                }
            }
        });
        ImageView btnEliminar = (ImageView) view.findViewById(R.id.btnBorrar);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("Confirmar");
        builder.setMessage("¿Esta seguro que desea eliminar esta ubicación?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (Eliminar(ub)){
                    Toast.makeText(context,"Se elimino exitosamente.", Toast.LENGTH_LONG).show();
                    list.remove(i);
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(context,"Ocurrio un error al eliminar.", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alert = builder.create();
        btnEliminar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();
            }
        });
    }

    private boolean Eliminar(Ubicacion ub){
        bd.eliminarUbicacion(ub);
        return true;
    }
}