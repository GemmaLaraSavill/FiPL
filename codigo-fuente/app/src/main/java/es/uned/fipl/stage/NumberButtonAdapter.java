/*
* Copyright 2015 Gemma Samantha Lara Savill
* FiPL My First Programming Language
* Proyecto de Fin de Grado en Ingeniería en Tecnologías de la Información
* en la Universidad Nacional de Educación a Distancia UNED España
*
* Final project for my Bachelor of Science in Information Technology Engineering
* At the Spanish National Distance University UNED
* Project manager Dr. Anselmo Peñas Padilla

* Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
* You may not use this work except in compliance with the Licence.
* You may obtain a copy of the Licence at:
*
* http://ec.europa.eu/idabc/eupl
*
* Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the Licence for the specific language governing permissions and limitations under the Licence.
*/
package es.uned.fipl.stage;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import es.uned.fipl.R;
import es.uned.fipl.stage.listeners.NumberOnClickListener;

/**
 * Created by Gemma Lara Savill on 15/04/2015.
 *
 * Crea el Grid de botones numéricos que activan el sensor numérico del Robot
 */
public class NumberButtonAdapter extends BaseAdapter {

    private final TextView numberSensor;
    private Context context;
    // creo el teclado numérico
    private String[] numbers = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    public NumberButtonAdapter(Context context, TextView numberSensor) {
        this.context = context;
        this.numberSensor = numberSensor;
    }

    @Override
    public int getCount() {
        return numbers.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * Crea cada botón
     * @param position posición en el array de números de este botón
     * @param view el botón
     * @param grid el layout al que pertenece (el grid)
     * @return cada botón como View, en concreto ButtonView
     */
    @Override
    public View getView(int position, View view, ViewGroup grid) {

        Button numberButton;

        if (view == null) {
            // nuevo botón
            numberButton = new Button(context);
            numberButton.setLayoutParams( new GridView.LayoutParams(
                    GridView.LayoutParams.MATCH_PARENT,
                    GridView.LayoutParams.MATCH_PARENT) );
            numberButton.setPadding(1, 1, 1, 1);
            numberButton.setText(numbers[position]);
            numberButton.setTextColor(Color.parseColor("#333333"));
            numberButton.setId(position);
            numberButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.number_button));
            // añado el listener al botón
            numberButton.setOnClickListener(new NumberOnClickListener(position, numberSensor));
        } else {
            numberButton = (Button) view;
        }

        // si es el botón seleccionado lo anoto
        int numberSelected = Integer.parseInt(numberSensor.getText().toString());
        if (numberSelected == position+1) {
            numberButton.setEnabled(false);
        }
        return numberButton;
    }
}
