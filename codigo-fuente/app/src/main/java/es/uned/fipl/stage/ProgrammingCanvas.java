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

import android.widget.RelativeLayout;
import android.widget.TextView;

import es.uned.fipl.R;
import es.uned.fipl.block.CommandBlock;
import es.uned.fipl.block.CommandBlockName;
import es.uned.fipl.block.CommandBlockPosition;
import es.uned.fipl.block.listeners.BottomConnectorDragListener;
import es.uned.fipl.block.listeners.RightSensorDragListener;
import es.uned.fipl.program.BlockLink;
import es.uned.fipl.program.BlockProgram;

/**
 * Created by Gemma Lara Savill on 15/04/2015.
 * The canvas where I shall draw my blocks
 */
public class ProgrammingCanvas {


    public static void drawBlockProgram(RelativeLayout canvas, BlockProgram blockProgram) {

        boolean tellme = false;
        boolean lastBlockIsEndOfCycle = false;
        boolean repeatCycleOpen = false;

        if (tellme) { System.out.println("Canvas tiene " + canvas.getChildCount() + " hijos"); }

        int blockWidth = CommandBlock.getCommandBlockWidth(canvas.getContext());
        int blockHeight = CommandBlock.getCommandBlockHeight(canvas.getContext());

        int sensorBlockWidth =  CommandBlock.getSensorBlockWidth(canvas.getContext());

        int leftMargin = (int) canvas.getContext().getResources().getDimension(R.dimen.command_block_left_margin);
        int topMargin = (int) canvas.getContext().getResources().getDimension(R.dimen.link_area_height);
        int rightMargin = (int) canvas.getContext().getResources().getDimension(R.dimen.link_area_width);
        //borro todos los bloques
        canvas.removeAllViews();

        // creo el texto inicial
        TextView txt = new TextView(canvas.getContext());
        // left top right bottom
        txt.setPadding(leftMargin, 10, 10, 10);
        txt.setId(R.id.programmingCanvasText);
        txt.setText(R.string.programmingCanvasTitle);
        canvas.addView(txt);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(blockWidth, blockHeight);
        int lastOnList = R.id.programmingCanvasText;

        int blockCount = blockProgram.getSize();
        BlockLink lowestLink = null;
        for(int i=0; i<blockCount; i++) {
            CommandBlock block = blockProgram.get(i).getBlock();

            if (tellme) { System.out.println("Draw "+block.getType()+"["+block.getId()+"] "+ blockProgram.get(i).getPosition()+" "+blockProgram.get(i).getAnchor()); }
            if (blockProgram.get(i).getPosition() == CommandBlockPosition.FIRST) {
                // inicio es un caso especial, va debajo del texto titulo
                params = new RelativeLayout.LayoutParams(blockWidth, blockHeight);
                params.setMargins(leftMargin, 0, 0, 0); // separación uno de otro
                params.addRule(RelativeLayout.BELOW, R.id.programmingCanvasText); // posición relativa
                lastOnList = block.getId();
                lowestLink = blockProgram.get(i);
            }

            if (blockProgram.get(i).getPosition() == CommandBlockPosition.RIGHT) {
                // sensor a la derecha
                params = new RelativeLayout.LayoutParams(sensorBlockWidth, blockHeight);
                params.setMargins(-rightMargin, 0, 0, 0); // separación uno de otro
                params.addRule(RelativeLayout.RIGHT_OF, blockProgram.get(i).getAnchor()); // posición relativa
                params.addRule(RelativeLayout.ALIGN_TOP, blockProgram.get(i).getAnchor());
                block.setSensorConnected(true);
            }

            if (blockProgram.get(i).getPosition() == CommandBlockPosition.BELOW) {
                // bloque debajo

                // reseteo repetir
                block.activateRepeatConnector(false, canvas.getContext());
                params = new RelativeLayout.LayoutParams(blockWidth, blockHeight);
                if (lastBlockIsEndOfCycle) {
                    params.setMargins(leftMargin, -(topMargin/2)+1, 0, 0); // separación uno de otro
                } else {
                    params.setMargins(leftMargin, -topMargin, 0, 0); // separación uno de otro
                }
                params.addRule(RelativeLayout.BELOW,  blockProgram.get(i).getAnchor()); // posición relativa
                block.setTag(blockProgram.get(i).getAnchor()); // guardo su anterior para reordenar
                lastOnList = block.getId();
                lowestLink = blockProgram.get(i);
                if(block.acceptsSideSensor() && !block.hasSensorConnected()) {
                    // añado conector de sensor (lateral derecho)
                    CommandBlock rightConnector = new CommandBlock(canvas.getContext());
//                    rightConnector.setBackgroundDrawable(new ShapeDrawable(new SensorBlockShape(Color.parseColor("#FFFFFF"), canvas.getContext())));
                    rightConnector.setBackgroundDrawable(canvas.getContext().getResources().getDrawable(R.drawable.right_connector_shape));
                    rightConnector.setAlpha(0.1f);
                    RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(sensorBlockWidth, blockHeight);
                    paramsRight.setMargins(-rightMargin, 0, 0, 0); // separación uno de otro
                    paramsRight.addRule(RelativeLayout.RIGHT_OF, block.getId()); // posición relativa
                    paramsRight.addRule(RelativeLayout.ALIGN_TOP, block.getId());
                    canvas.addView(rightConnector, paramsRight);
                    rightConnector.setTag(block.getId());
                    if (tellme) {System.out.println("Añado conector dcha de  "+block.getId()); }
                    rightConnector.setOnDragListener(new RightSensorDragListener(blockProgram));
                }

            }

            // casos especiales
            if (block.getType() == CommandBlockName.REPEAT && block.isDocked()) {
                repeatCycleOpen = true;
            }
            if (!block.isSensor() && repeatCycleOpen) {
                block.activateRepeatConnector(true, canvas.getContext());
            }
            if (lowestLink.getRepeat() == BlockLink.REPEAT_LAST) {
                // bloque último dentro de un bloque repetir
                repeatCycleOpen = false;
                lastBlockIsEndOfCycle = true;
                lowestLink.getBlock().lastInRepeatCycle(true, canvas.getContext());
            } else {
                lastBlockIsEndOfCycle = false;
            }

            canvas.addView(block, params);
        } // fin de bucle del programa

        // añado conector inferior
        CommandBlock bottomConnector = new CommandBlock(canvas.getContext());
        params =  new RelativeLayout.LayoutParams(blockWidth, blockHeight);
        // left top right bottom
        if (lastBlockIsEndOfCycle) {
            params.setMargins(leftMargin, -(topMargin/2), 0, 0); // separación uno de otro
        } else {
            params.setMargins(leftMargin, -topMargin, 0, 0);
        }
        bottomConnector.setBackgroundDrawable(canvas.getContext().getResources().getDrawable(R.drawable.bottom_connector_shape));
        bottomConnector.setAlpha(0.1f);
        params.addRule(RelativeLayout.BELOW, lastOnList); // posición relativa
        canvas.addView(bottomConnector, params);
        bottomConnector.setTag(lastOnList);
        if (tellme) { System.out.println("Añado conector bajo "+lastOnList); }
        bottomConnector.setOnDragListener(new BottomConnectorDragListener(blockProgram));



        // para dejar los bloques en cualquier parte del canvas, sin conectar
//        canvas.setOnDragListener(new CanvasDragListener(blockProgram));
        canvas.invalidate();
    }

}
