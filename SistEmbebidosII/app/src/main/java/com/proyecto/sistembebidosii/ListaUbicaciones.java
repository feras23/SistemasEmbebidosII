package com.proyecto.sistembebidosii;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

        ToggleButton btnToggle = (ToggleButton)view.findViewById(R.id.btnToggle);
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
        btnEliminar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Eliminar(ub)){
                    Toast.makeText(context,"Se elimino exitosamente.", Toast.LENGTH_LONG).show();
                    list.remove(i);
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(context,"Ocurrio un error al eliminar.", Toast.LENGTH_LONG).show();
               }

            }
        });
    }

    private boolean Eliminar(Ubicacion ub){
        bd.eliminarUbicacion(ub);
        return true;
    }
}