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

import android.media.MediaPlayer;
import android.view.DragEvent;
import android.view.View;
import android.widget.RelativeLayout;

import es.uned.fipl.R;
import es.uned.fipl.block.CommandBlock;
import es.uned.fipl.block.CommandBlockFactory;
import es.uned.fipl.block.CommandBlockName;
import es.uned.fipl.program.BlockProgram;
import es.uned.fipl.stage.ProgrammingCanvas;

/**
 * Created by Gemma Lara Savill on 03/04/2015.
 * Detecta cuando un bloque del programa es soltado encima de otro
 * Entonces lo sustituye y el programa se reordena
 */
public class InsertBlockDragListener implements View.OnDragListener {

    private final CommandBlockFactory blockFactory; // fabrica de bloques al que pediremos nuevo bloque
    private final BlockProgram blockProgram; // programa de bloques al que conectaremos el bloque
    private MediaPlayer mp;

    public InsertBlockDragListener(BlockProgram blockProgram) {
        this.blockFactory = new CommandBlockFactory();
        this.blockProgram = blockProgram;

    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        //Toast.makeText(getApplicationContext(), "Action "+action, Toast.LENGTH_SHORT).show();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                //v.setBackgroundColor(0xffffffff);
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
//                v.setAlpha(1f);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
//                v.setAlpha(0.5f);
                break;
            case DragEvent.ACTION_DROP:
                // bloque soltado
                CommandBlock droppedBlock = (CommandBlock) event.getLocalState();
                // encima de bloque
                CommandBlock receiverBlock = (CommandBlock) v;
                if (droppedBlock.getType() != CommandBlockName.COLORSENSOR && droppedBlock.getType() != CommandBlockName.NUMBERSENSOR) {

                    // el que contiene a los bloques
                    RelativeLayout canvas = (RelativeLayout) v.getParent();

                    CommandBlock blockToInsert = null;
                    if (droppedBlock.isDocked()) {
                        // voy a insertar un bloque que ya estaba en la lista
                        blockToInsert = droppedBlock;
                    } else {
                        // clono el bloque: pido al blockFactory un bloque del mismo tipo
                        CommandBlock newBlock = blockFactory.createBlock(droppedBlock.getType(), receiverBlock.getContext());
                        newBlock.setId(CommandBlockFactory.generateId());
                        newBlock.setDocked(true);
                        newBlock.setOnTouchListener(new CommandBlockTouchListener());
                        newBlock.setOnDragListener(new InsertBlockDragListener(blockProgram));
                        ProgrammingCanvas.drawBlockProgram(canvas, blockProgram);
                        blockToInsert = newBlock;
                    }
                    mp = MediaPlayer.create(v.getContext(), R.raw.block_union);
                    mp.start();
                    blockProgram.insertBlock(blockToInsert, receiverBlock);
                    ProgrammingCanvas.drawBlockProgram(canvas, blockProgram);

                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                //v.setBackgroundColor(0xfff0b3c7);
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                //v.setBackgroundColor(0xffCC0000);
                break;
            default:
                break;
        }
        return true;
    }
}