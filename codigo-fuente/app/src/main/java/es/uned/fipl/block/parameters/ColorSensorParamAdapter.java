package es.uned.fipl.block.parameters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import es.uned.fipl.R;


/**
 * Created by Gemma Lara Savill on 27/03/2015.
 *
 * Adaptador especifico una lista desplegable mostrando una lista de cuadrados de colores
 */
public class ColorSensorParamAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] colors;

    public ColorSensorParamAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.colors = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.color_parameter_layout, parent, false);

        View square = (View) row.findViewById(R.id.colorView);
//        System.out.println("Color en lista "+colors[position]);


        square.setBackgroundColor(Color.parseColor(colors[position]));

        return row;
    }



}