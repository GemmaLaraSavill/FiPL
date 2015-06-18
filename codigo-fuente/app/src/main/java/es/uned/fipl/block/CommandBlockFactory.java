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
import android.content.res.TypedArray;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;

import es.uned.fipl.R;
import es.uned.fipl.block.parameters.ColorSensorParamAdapter;
import es.uned.fipl.block.parameters.MoveBlockParamAdapter;
import es.uned.fipl.stage.Stage;

/**
 * Created by Gemma Lara Savill on 19/03/2015.
 *
 * Crea diferentes tipos de CommandBlock
 */
public class CommandBlockFactory implements BlockFactory {


    private static int id = 0;
    private CommandBlock block; // el bloque que voy a crear
    private CommandBlockName commandBlockName; // el tipo de bloque
    private Context context; // contexto de la ventana Android en la que se crea
    private ArrayList<String> params; // lista de parámetros que tendrá el bloque (si es que tiene)

    private boolean tellme = false; // para debugging

    /**
     * Crea un bloque según su tipo
     *
     * @param commandBlockName el tipo de bloque que debo crear
     * @return un CommandBlock recien creado
     */
    @Override
    public CommandBlock createBlock(CommandBlockName commandBlockName, Context context) {

        this.context = context;
        block = new CommandBlock(context);
        params = new ArrayList<String>();
        this.commandBlockName = commandBlockName;

        int blockWidth = CommandBlock.getCommandBlockWidth(context);
        int blockHeight = CommandBlock.getCommandBlockHeight(context);

        switch (commandBlockName) {
            case START:
                createStartBlock();
                break;
            case WAIT:
                createWaitBlock();
                break;
            case SPEED:
                createSpeedBlock();
                break;
            case POSITION:
                createPositionBlock();
                break;
            case MOVE:
                createMoveBlock();
                break;
            case REPEAT:
                createRepeatBlock();
                break;
            case COLORSENSOR:
                createColorSensorBlock();
                blockWidth = CommandBlock.getSensorBlockWidth(context);
                break;
            case NUMBERSENSOR:
                createNumberSensorBlock();
                blockWidth = CommandBlock.getSensorBlockWidth(context);
                break;
            default:
                if (tellme) {
                    System.out.println("No puedo crear este block");
                }
                break;
        }
        block.setId(generateId());
        // especificaciones para los layouts Android
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(blockWidth, blockHeight);
        // left, top, right, bottom
        lp.setMargins(0,0,0,(int) context.getResources().getDimension(R.dimen.command_block_padding_bottom)); // separación uno de otro
        block.setLayoutParams(lp);
        return block;
    }

    /**
     * Crea un bloque de Inicio
     */
    private void createStartBlock() {
        block.setText(context.getResources().getString(R.string.start));
        block.setType(commandBlockName);
        block.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.one_link_shape));
        block.setCanHaveSideSensor(false);
        if (tellme) { System.out.println("Creo un START block."); }
    }

    /**
     * Crea un bloque Esperar
     */
    private void createWaitBlock() {
        block.setText(context.getResources().getString(R.string.wait));
        block.setType(commandBlockName);
        block.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.three_link_shape));
        block.setCanHaveSideSensor(true);
        if (tellme) { System.out.println("Creo un WAIT block."); }
    }

    /**
     * Crea un bloque Velocidad
     */
    private void createSpeedBlock() {
        block.setText(context.getResources().getString(R.string.speed));
        block.setType(commandBlockName);
        block.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.two_link_shape));
        params = new ArrayList<String>();
        for (int i=1; i<=5; i++) {
            params.add(i-1, ""+i);
        }
        // TODO ver si es necesario el spinner_text + estilo
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_text, params);
        block.setAdapter(adapter);
        block.parameterSelectorSetup(params, 0);
        block.setCanHaveSideSensor(false);
        if (tellme) { System.out.println("Creo un SPEED block."); }

    }

    /**
     * Crea un bloque de Posición
     * Tiene dos selectores numéricos de posición
     * tipo coordenadas X e Y
     */
    private void createPositionBlock() {
        // su nombre
        block.setText(context.getResources().getString(R.string.position));
        // su tipo
        block.setType(commandBlockName);
        // su forma
        block.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.two_link_shape));
        // sus parámetros, tiene dos selectores
        params = new ArrayList<String>();
        for (int i=1; i<= Stage.COLS; i++) {
            params.add(i-1, ""+i);
        }
        // selector numérico normal
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, params);
        block.setAdapter(adapter);
        block.parameterSelectorSetup(params, 0);
        block.parameterSelector2Setup(params, 0);
        block.setCanHaveSideSensor(false);
        if (tellme) { System.out.println("Creo un POSITION block."); }
    }

    /**
     * Creo un bloque tipo MOVER
     * Tiene cuatro parámetros tipo flecha
     */
    private void createMoveBlock() {
        block.setText(context.getResources().getString(R.string.move));
        block.setType(commandBlockName);
        block.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.two_link_shape));
        params = new ArrayList<String>();
        params.add(0, context.getResources().getString(R.string.param_right));
        params.add(1, context.getResources().getString(R.string.param_down));
        params.add(2, context.getResources().getString(R.string.param_left));
        params.add(3, context.getResources().getString(R.string.param_up));
        String[] paramList = new String[params.size()];
        paramList = params.toArray(paramList);
        MoveBlockParamAdapter moveSelectorAdapter = new MoveBlockParamAdapter(context, R.layout.move_parameter_layout, paramList);
        block.setAdapter(moveSelectorAdapter);
        block.parameterSelectorSetup(params, 0);
        block.setCanHaveSideSensor(false);
        if (tellme) { System.out.println("Creo un MOVE block."); }
    }

    /**
     * Creo un bloque REPETIR
     * No tiene parámetros
     */
    private void createRepeatBlock() {
        block.setText(context.getResources().getString(R.string.repeat));
        block.setType(commandBlockName);
        block.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.repeat_shape));
        block.setCanHaveSideSensor(true);
        if (tellme) { System.out.println("Creo un REPEAT block."); }

    }

    /**
     * Creo un bloque SENSOR COLOR
     * Tiene una lista de parámetros tipo bloques de color
     */
    private void createColorSensorBlock() {
        // su nombre
        block.setText(context.getResources().getString(R.string.color));
        // su tipo
        block.setType(commandBlockName);
        // su forma
        block.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sensor_shape));
        // sus parámetros
        TypedArray ta = context.getResources().obtainTypedArray(R.array.colors);
        String[] colors = new String[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getString(i);
        }
        ta.recycle();
        params = new ArrayList<String>(Arrays.asList(colors));
        // selector especial de colores
        ColorSensorParamAdapter adapter = new ColorSensorParamAdapter(context,
                R.layout.move_parameter_layout, colors);
        block.setAdapter(adapter);
        block.parameterSelectorSetup(params, 0);
        if (tellme) { System.out.println("Creo un COLORSENSOR block."); }
    }

    /**
     * Crea un bloque de sensor de número
     */
    private void createNumberSensorBlock() {
        // su nombre
        block.setText(context.getResources().getString(R.string.number));
        // su tipo
        block.setType(commandBlockName);
        // su forma
        block.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sensor_shape));
        // sus parámetros
        params = new ArrayList<String>();
        for (int i=1; i<=9; i++) {
            params.add(i-1, ""+i);
        }
        // selector de números
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, params);
        block.setAdapter(adapter);
        block.parameterSelectorSetup(params, 4);
        if (tellme) {System.out.println("Creo un NUMBERSENSOR block."); }
    }

    /**
     * Genera un id para un View
     * @return el id tipo int
     */
    public static int generateId() {
        id++;
        return id;
    }




}
