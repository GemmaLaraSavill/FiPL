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
package es.uned.fipl.stage.listeners;

import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by Gemma Lara Savill on 12/04/2015.
 */
public class NumberOnClickListener implements View.OnClickListener {

    private final int number;
    private final TextView numberSensor;

    public NumberOnClickListener(int number, TextView numberSensor) {
        this.number = number;
        this.numberSensor = numberSensor;
    }

    @Override
    public void onClick(View view) {
        Button b = (Button)view;
        // change number sensor value
        numberSensor.setText(b.getText());
        // enable the whole number keypad
        GridView keypad = (GridView) b.getParent();
        int buttonCount = keypad.getChildCount();
        for(int i=0; i<buttonCount; i++) {
            keypad.getChildAt(i).setEnabled(true);
        }
        // disable number button
        b.setEnabled(false);

    }
}
