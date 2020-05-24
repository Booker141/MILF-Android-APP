package es.uca.toolbaractionbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AsistenteAdapter extends RecyclerView.Adapter<AsistenteAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> asistentesDni;

    //Carga la lista de items a partir de la BD
    public AsistenteAdapter(ArrayList<String> asistentes) {
        //Obtener listado de DNIs de la API
        asistentesDni = asistentes;
    }

    //Carga informacion previa
    @Override
    public AsistenteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    //Rellena los campos del RecyclerView y añade accion
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.dni.setText(String.valueOf(asistentesDni.get(position)));
        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent asistente = new Intent(v.getContext(), AsistenteActivity.class);
                Bundle b = new Bundle();
                b.putString("dni", asistentesDni.get(position));
                asistente.putExtras(b);
                v.getContext().startActivity(asistente);
            }
        });
    }

    //Sobreescribe el metodo que devuelve el numero de items del RecyclerView
    @Override
    public int getItemCount() { return asistentesDni.size(); }

    //Crea una vista personalizada para nuestro RecyclerView
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dni;
        Button show;

        public MyViewHolder(View v) {
            super(v);
            dni = (TextView) v.findViewById(R.id.dni_asistente);
            show = (Button) v.findViewById(R.id.boton_asistente);
        }
    }

}
