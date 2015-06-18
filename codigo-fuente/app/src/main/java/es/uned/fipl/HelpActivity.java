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
package es.uned.fipl;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import es.uned.fipl.block.CommandBlock;
import es.uned.fipl.block.CommandBlockFactory;
import es.uned.fipl.block.CommandBlockName;

/**
 * Created by Gemma Lara Savill on 07/03/2015
 * Ventana con información sobre el programa (ayuda)
 */
public class HelpActivity extends Activity {

    /**
     * Al crearse la ventana
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Configuro barra de menú
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // flechita volver
        actionBar.setSubtitle(R.string.action_helpScreen);

        Context context = this;

        // cargo la interfaz
        LinearLayout moveBlockHolder = (LinearLayout) findViewById(R.id.moveBlockHolder);
        LinearLayout speedBlockHolder = (LinearLayout) findViewById(R.id.speedBlockHolder);
        LinearLayout waitBlockHolder = (LinearLayout) findViewById(R.id.waitBlockHolder);
        LinearLayout positionBlockHolder = (LinearLayout) findViewById(R.id.positionBlockHolder);
        LinearLayout repeatBlockHolder = (LinearLayout) findViewById(R.id.repeatBlockHolder);
        LinearLayout numberSensorBlockHolder = (LinearLayout) findViewById(R.id.numberSensorBlockHolder);
        LinearLayout colorSensorBlockHolder = (LinearLayout) findViewById(R.id.colorSensorBlockHolder);

        CommandBlockFactory blockFactory = new CommandBlockFactory();
        // creo y cargo bloque mover
        CommandBlock moveBlock = blockFactory.createBlock(CommandBlockName.MOVE, context);
        moveBlockHolder.addView(moveBlock);
        // creo y cargo bloque velocidad
        CommandBlock speedBlock = blockFactory.createBlock(CommandBlockName.SPEED, context);
        speedBlockHolder.addView(speedBlock);
        // creo y cargo bloque esperar
        CommandBlock waitBlock = blockFactory.createBlock(CommandBlockName.WAIT, context);
        waitBlockHolder.addView(waitBlock);
        // creo y cargo bloque posisión
        CommandBlock positionBlock = blockFactory.createBlock(CommandBlockName.POSITION, context);
        positionBlockHolder.addView(positionBlock);
        // creo y cargo bloque repetir
        CommandBlock repeatBlock = blockFactory.createBlock(CommandBlockName.REPEAT, context);
        repeatBlockHolder.addView(repeatBlock);
        // creo y cargo bloque sensor numero
        CommandBlock numberSensorBlock = blockFactory.createBlock(CommandBlockName.NUMBERSENSOR, context);
        numberSensorBlockHolder.addView(numberSensorBlock);
        // creo y cargo bloque sensor color
        CommandBlock colorSensorBlock = blockFactory.createBlock(CommandBlockName.COLORSENSOR, context);
        colorSensorBlockHolder.addView(colorSensorBlock);

        // los textos están especificados en su xml /layouts/action_helpScreen.xml
    }


}
