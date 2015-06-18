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
import es.uned.fipl.block.CommandBlockPosition;
import es.uned.fipl.program.BlockProgram;
import es.uned.fipl.stage.ProgrammingCanvas;

/**
 * Created by Gemma Lara Savill on 24/04/2015.
 *
 * Detecta si un bloque es arrastrado sobre su conector lateral derecho: conecta un sensor
 */
public class RightSensorDragListener implements View.OnDragListener {

    private final CommandBlockFactory blockFactory; // fábrica de bloques al que pediremos nuevo bloque
    private final BlockProgram blockProgram; // programa de bloques al que conectaremos el bloque
    private final float connectorTransparencyIdle = 0.1f; // transparencia contector inactivo
    private final float connectorTransparencyActive = 0.5f; // transparencia contector activo
    private MediaPlayer mp;

    /**
     * Le pasamos el program de bloques
     * @param blockProgram el programa de bloques
     */
    public RightSensorDragListener(BlockProgram blockProgram) {
        this.blockFactory = new CommandBlockFactory();
        this.blockProgram = blockProgram;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                v.setAlpha(connectorTransparencyIdle);
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                v.setAlpha(connectorTransparencyActive);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setAlpha(connectorTransparencyIdle);
                break;
            case DragEvent.ACTION_DROP:
                // Soltado el bloque
                CommandBlock view = (CommandBlock)event.getLocalState();
                // el que contiene a los bloques
                RelativeLayout container = (RelativeLayout) v.getParent();

                CommandBlock connectedBlock = null;
                if (view.isSensor()) {
                    if (view.isDocked()) {
                        // no tengo que clonar, sólo mover
                        connectedBlock = view;
                        // primero lo tengo que borrar
                        blockProgram.removeBlock(connectedBlock.getId());
                        CommandBlock hisTarget = (CommandBlock) container.findViewById((int) connectedBlock.getTag());
                        hisTarget.setRightSideSensor(-1);
                        hisTarget.setSensorConnected(false);

                    } else {
                        // pido al blockFactory un bloque del mismo tipo
                        connectedBlock = blockFactory.createBlock(view.getType(), v.getContext());
                        connectedBlock.setId(CommandBlockFactory.generateId());

                    }

                    // decido posición según el tipo de bloque
                    CommandBlockPosition position = CommandBlockPosition.RIGHT;

                    // El receptor es un sensor, trae el ID de su bloque asociado en el tag
                    int anchorId = (int) v.getTag();
                    CommandBlock receptor = (CommandBlock) ((RelativeLayout) v.getParent()).findViewById(anchorId);
                    if (receptor.acceptsSideSensor() && !receptor.hasSensorConnected()) {
                        receptor.setSensorConnected(true);
                        receptor.setRightSideSensor(connectedBlock.getId());
                        blockProgram.addBlock(connectedBlock, position, anchorId);
                        mp = MediaPlayer.create(view.getContext(), R.raw.block_union);
                        mp.start();
                        connectedBlock.setDocked(true);
                        connectedBlock.setTag(receptor.getId());
                        connectedBlock.setOnTouchListener(new CommandBlockTouchListener());
                        ProgrammingCanvas.drawBlockProgram(container, blockProgram);
                    }
                } else {
                    // este tipo de bloque no se conecta aquí
                    mp = MediaPlayer.create(view.getContext(),R.raw.error);
                    mp.start();
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