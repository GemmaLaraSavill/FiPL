package es.uned.fipl.block.parameters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import es.uned.fipl.R;


/**
 * Created by Gemma Lara Savill on 27/03/2015.
 *
 * Adaptador especifico una lista desplegable mostrando una lista de flechas
 */
public class MoveBlockParamAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] directions;

    public MoveBlockParamAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.directions = objects;
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
        View row = inflater.inflate(R.layout.move_parameter_layout, parent, false);

        ImageView icon = (ImageView) row.findViewById(R.id.imageView);

        if (directions[position].equals(context.getResources().getString(R.string.param_up))) {
            icon.setImageResource(R.drawable.arrow_up);
        } else if (directions[position].equals(context.getResources().getString(R.string.param_down))) {
            icon.setImageResource(R.drawable.arrow_down);
        } else if (directions[position].equals(context.getResources().getString(R.string.param_left))) {
            icon.setImageResource(R.drawable.arrow_left);
        } else if (directions[position].equals(context.getResources().getString(R.string.param_right))) {
            icon.setImageResource(R.drawable.arrow_right);
        }
        return row;
    }

}