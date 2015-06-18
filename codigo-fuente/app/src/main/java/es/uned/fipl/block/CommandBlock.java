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
package es.uned.fipl.block;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import es.uned.fipl.R;
import es.uned.fipl.block.listeners.Parameter2SelectedListener;
import es.uned.fipl.block.listeners.ParameterSelectedListener;


/**
 * Created by Gemma Lara Savill on 19/03/2015
 *
 * Un CommandBlock
 * Un bloque que representa un comando en un programa
 * Puede ser de diferentes tipos especificados en CommandBlockName
 * Puede tener diferentes formas
 * Puede tener hasta 2 selectores de parámetros
 * Se crean en el CommandBlockFactory
 */
public class CommandBlock extends LinearLayout  {

    private boolean docked; // conectado en un programa

    // estrucutura
    private CommandBlockName type; // tipo de bloque
    private TextView tagName; // etiqueta del bloque (texto)
    private Spinner paramSelector; // selector de parámetro uno
    private Spinner paramSelector2; // un segundo selector de parámetros, inicialmente invisible2

    // parámetros
    private ArrayAdapter<String> adapter; // adaptador de la lista de parámetros
    private String parameter; // parámetro uno seleccionado
    private String parameter2; // parámetro dos seleccionado
    private ArrayList<String> parameterList; // lista del primer selector de parámetros
    private ArrayList<String> parameter2List; // lista del segundo selector de parámetros
    private boolean hasSensorConnected; // tiene conectado un sensor lateral
    private int rightSideSensorId; // id del bloque sensor conectado
    private boolean canHaveSideSensor = false; // admite conexiones de sensor

    /**
     * Constructor
     * @param context el contexto de la ventana
     */
    public CommandBlock(Context context) {
        super(context);

        int blockBottomPadding = (int) context.getResources().getDimension(R.dimen.command_block_bottom_padding);
        int blockLeftPadding = (int) context.getResources().getDimension(R.dimen.command_block_left_padding);

        // customizo el view
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        // padding left, top, right, bottom
        setPadding(blockLeftPadding,0,0,blockBottomPadding);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View block = inflater.inflate(R.layout.command_block, this,true);

        // accedo a los componentes UI del bloque
        tagName = (TextView)block.findViewById(R.id.label);
        paramSelector = (Spinner)block.findViewById(R.id.selector);
        paramSelector2 = (Spinner)block.findViewById(R.id.selector2);

        // oculto los selectores
        paramSelector.setVisibility(View.GONE);
        paramSelector2.setVisibility(View.GONE);

        // está conectado formando parte de un programa
        docked = false;
        // tiene un sensor conectado por el lateral
        hasSensorConnected = false;
        rightSideSensorId = -1;
    }

    /**
     * Nombre de este bloque
     * @param text
     */
    public void setText(String text) {
        tagName.setText(text);
    }


    /**
     * Configuro el primer selector de parámetros
     * @param params lista de los parámetros disponibles en este bloque
     * @param selected el inicialmente seleccionado
     */
    public void parameterSelectorSetup(ArrayList<String> params, int selected) {
        this.parameterList = params;
        if (params.size() > 0) {
            // si voy a usarlo lo muestro
            paramSelector.setVisibility(View.VISIBLE);
        }
        paramSelector.setAdapter(adapter);
        paramSelector.setSelection(selected);
        paramSelector.setOnItemSelectedListener(new ParameterSelectedListener(this));
    }



    /**
     * Configuro el segundo selector de parámetros
     * @param params lista de los parámetros disponibles en este bloque
     * @param selected el inicialmente seleccionado
     */
    public void parameterSelector2Setup(ArrayList<String> params, int selected) {
        this.parameter2List = params;
        if (params.size() > 0) {
            // si voy a usarlo lo muestro
            paramSelector2.setVisibility(View.VISIBLE);
        }
        // populate spinner
       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),
                    android.R.layout.simple_spinner_item, params);*/
        paramSelector2.setAdapter(adapter);
        paramSelector2.setSelection(selected);
        paramSelector2.setOnItemSelectedListener(new Parameter2SelectedListener(this));

    }

    /**
     * Devuelve su tipo de bloque
      * @return CommandBlockName
     */
    public CommandBlockName getType() {
        return type;
    }

    /**
     * Establece el tipo de bloque
     * @param type CommandBlockName
     */
    public void setType(CommandBlockName type) {
        this.type = type;
    }


    /**
     * Establece el adaptador para el selector de parámetros
     * @param adapter
     */
    public void setAdapter(ArrayAdapter<String> adapter) {
        this.adapter = adapter;
    }

    /**
     * Devuelve si el bloque está conectado
     * formando parte de un programa
     * @return true si está conectado
     *          false si no está conectado
     */
    public boolean isDocked() {
        return docked;
    }

    /**
     * Establece si el bloque está conectado en un program de bloques
     * @param docked true or false
     */
    public void setDocked(boolean docked) {
        this.docked = docked;
    }

    /**
     * Devuelve el parámetro seleccionado del primer selector
     * @return String
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * Establece el parámetro seleccionado en el primer selector
     * @param position en la lista
     */
    public void setParameter(int position) {
        this.parameter = this.parameterList.get(position);
    }

    /**
     * Devuelve el parámetro seleccionado del segundo selector
     * @return String
     */
    public String getParameter2() {
        return parameter2;
    }

    /**
     * Establece el parámetro seleccionado en el segundo selector
     * @param position en la lista
     */
    public void setParameter2(int position) {
        this.parameter2 = this.parameter2List.get(position);
    }

    /**
     * Activa o desactiva los selectores de parámetros
     * @param b true or false
     */
    public void setParamSelectorsActive(boolean b) {
        paramSelector.setEnabled(b);
        paramSelector2.setEnabled(b);
    }

    /**
     * Devuelve el ancho del bloque según la densidad de píxeles
     * del dispositivo
     * @param context
     * @return ancho en dp (density pixels)
     */
    public static int getCommandBlockWidth(Context context) {
        return (int) context.getResources().getDimension(R.dimen.command_block_width);
    }

    /**
     * Devuelve el alto del bloque según la densidad de píxeles
     * del dispositivo
     * @param context
     * @return alto en dp (density pixels)
     */
    public static int getCommandBlockHeight(Context context) {
        return (int) context.getResources().getDimension(R.dimen.command_block_height);
    }

    /**
     * Devuelve el ancho del bloque según la densidad de píxeles
     * del dispositivo
     * @param context
     * @return ancho en dp (density pixels)
     */
    public static int getSensorBlockWidth(Context context) {
        return (int) context.getResources().getDimension(R.dimen.sensor_block_width);
    }

    /**
     * Devuelve si este bloque acepta un sensor lateral
     * @return boolean true or false
     */
    public boolean acceptsSideSensor() {
        return this.canHaveSideSensor;
    }

    /**
     * Establece si este bloque puede aceptar un sensor lateral
     * @param canHaveSideSensor boolean true or false
     */
    public void setCanHaveSideSensor(boolean canHaveSideSensor) {
        this.canHaveSideSensor = canHaveSideSensor;
    }

    /**
     * Devuelve si el bloque tiene un sensor lateral conectado
     * @return boolean true or false
     */
    public boolean hasSensorConnected() {
        return hasSensorConnected;
    }

    /**
     * Establece si el bloque tiene un sensor lateral conectado
     * @param hasSensorConnected boolean true or false
     */
    public void setSensorConnected(boolean hasSensorConnected) {
        this.hasSensorConnected = hasSensorConnected;
    }

    /**
     * Estable el ID del sensor que tiene conectado
     * @param sensorBlockId -1 si no tiene
     */
    public void setRightSideSensor(int sensorBlockId) {
        this.rightSideSensorId = sensorBlockId;
    }

    /**
     * Devuelve el ID del sensor que tiene conectado
     * @return -1 si no tiene
     */
    public int getRightSideSensorId() {
        return rightSideSensorId;
    }

    /**
     * Activa/desactiva el background de connector izquierdo de bucle
     * @param activate true or false
     * @param context
     */
    public void activateRepeatConnector(boolean activate, Context context) {
        Drawable bg = null;
        if (this.getType() == CommandBlockName.REPEAT) {
            // repeat siempre tiene otra forma si conectado
            bg = context.getResources().getDrawable(R.drawable.repeat_shape_docked);
        }
        if (this.getType() != CommandBlockName.REPEAT && !this.isSensor()) {
            if (activate) {
                if (acceptsSideSensor()) {
                    bg = context.getResources().getDrawable(R.drawable.repeat_shape_r);
                } else {
                    bg = context.getResources().getDrawable(R.drawable.repeat_shape_r2);
                }
            } else {
                if (acceptsSideSensor()) {
                    bg = context.getResources().getDrawable(R.drawable.three_link_shape);
                } else {
                    bg = context.getResources().getDrawable(R.drawable.two_link_shape);
                }
            }
        }
        if (bg != null) {
            this.setBackgroundDrawable(bg);
        }
    }

    /**
     * Este bloque es el último dentro de un ciclo repetir
     * @param activate boolean true or false
     * @param context
     */
    public void lastInRepeatCycle(boolean activate, Context context) {
        Drawable bg = null;
        if (activate) {
            if (acceptsSideSensor()) {
                bg = context.getResources().getDrawable(R.drawable.repeat_shape_rf);
            } else {
                bg = context.getResources().getDrawable(R.drawable.repeat_shape_rf2);
            }
        } else {
            bg = context.getResources().getDrawable(R.drawable.repeat_shape_r);
        }
        if (bg != null) {
            this.setBackgroundDrawable(bg);
        }
    }

    /**
     * Devuelve si es un bloque tipo sensor
     * @return boolean true or false
     */
    public boolean isSensor() {
        if (this.getType() == CommandBlockName.COLORSENSOR || this.getType() == CommandBlockName.NUMBERSENSOR) {
            return true;
        } else {
            return false;
        }
    }
}
