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

import android.media.MediaPlayer;
import android.view.DragEvent;
import android.view.View;
import android.widget.RelativeLayout;

import es.uned.fipl.R;
import es.uned.fipl.block.CommandBlock;
import es.uned.fipl.program.BlockProgram;
import es.uned.fipl.stage.ProgrammingCanvas;

/**
 * Created by Gemma Lara Savill on 03/04/2015.
 * // Acciones a tomar con el Drag sobre la Papelera
 */
public class WasteBinDragListener implements View.OnDragListener {

    private final BlockProgram blockProgram;
    private MediaPlayer mp;

    public WasteBinDragListener(BlockProgram program) {
        this.blockProgram = program;

    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        CommandBlock block = null;
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                block = (CommandBlock)event.getLocalState();
                if (block.isDocked()) {
                    v.setBackgroundDrawable(block.getContext().getResources().getDrawable(R.drawable.bin_open));
                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                block = (CommandBlock)event.getLocalState();
                if (block.isDocked()) {
                    v.setBackgroundDrawable(block.getContext().getResources().getDrawable(R.drawable.bin_closed));
                }
                break;
            case DragEvent.ACTION_DROP:
                block = (CommandBlock)event.getLocalState();
//                System.out.println("Arrastrado a papelera block "+block.getId());
                if (block.isDocked()) {
                    // si el bloque estaba conectado al programa: voy a quitar este block
                    RelativeLayout owner = (RelativeLayout) block.getParent();
                    blockProgram.removeBlock(block.getId());
                    if (block.isSensor()) {
                        // si estoy borrando un sensor
                        CommandBlock hisTarget = (CommandBlock)owner.findViewById((int)block.getTag());
                        // el bloque al que estaba conectado ya no tendrá un sensor conectado
                        hisTarget.setSensorConnected(false);
                    } else {
                        if (block.hasSensorConnected()) {
                            // si tiene un sensor lo tendremos que borrar tambien
                            int rightSensorId = block.getRightSideSensorId();
                            if (rightSensorId != -1) {
                                blockProgram.removeBlock(rightSensorId);
                            }
                        }
                    }

                    mp = MediaPlayer.create(block.getContext(), R.raw.trash);
                    mp.start();
                    // cierro papelera
                    v.setBackgroundDrawable(block.getContext().getResources().getDrawable(R.drawable.bin_closed));
                    ProgrammingCanvas.drawBlockProgram(owner, blockProgram);

                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                block = (CommandBlock)event.getLocalState();
                if (block != null) {
                    block.setVisibility(View.VISIBLE);
                }
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                break;
            default:
                break;
        }
        return true;
    }
}