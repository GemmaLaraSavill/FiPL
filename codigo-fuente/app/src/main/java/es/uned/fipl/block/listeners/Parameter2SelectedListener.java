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
package es.uned.fipl.block.listeners;

import android.view.View;
import android.widget.AdapterView;

import es.uned.fipl.block.CommandBlock;

/**
 * Created by Gemma Lara Savill on 28/03/2015.
 *
 * Detecta un cambio en el segundo selector de parámetros de un bloque
 */
public class Parameter2SelectedListener implements AdapterView.OnItemSelectedListener {


    private final CommandBlock block;

    /**
     * Le pasamos su bloque
     * @param commandBlock bloque al que pertenece el selector
     */
    public Parameter2SelectedListener(CommandBlock commandBlock) {
        this.block = commandBlock;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // le paso el parámetro al bloque
        block.setParameter2(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
