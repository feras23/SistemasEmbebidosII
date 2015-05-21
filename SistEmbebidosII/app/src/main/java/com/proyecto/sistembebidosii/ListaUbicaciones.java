package com.proyecto.sistembebidosii;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class ListaUbicaciones extends BaseAdapter implements ListAdapter {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_ubicacion, null);
        }
        final Ubicacion  ub= list.get(position);
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
        return view;
    }
}