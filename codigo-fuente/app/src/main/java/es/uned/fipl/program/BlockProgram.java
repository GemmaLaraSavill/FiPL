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
package es.uned.fipl.program;

import java.util.ArrayList;

import es.uned.fipl.block.CommandBlock;
import es.uned.fipl.block.CommandBlockName;
import es.uned.fipl.block.CommandBlockPosition;


/**
 * Created by Gemma Lara Savill on 07/03/2015.
 *
 * Representa una lista de bloques enlazados
 *
 * Una lista de BlockLink: cada uno contiene un bloque y la posición relativo a otro
 *
 * Gestiona la lista de bloques que ProgramCanvas se encargará de dibujar en la pantalla
 * Aquí se realizan las operaciones de añadir, reordenar y borrar bloques
 *
 */
public class BlockProgram extends ArrayList {

    private ArrayList<BlockLink> blockProgram; // programa de bloques: lista de BlockLinks
    private boolean tellme = false; // para debugging

    /**
     * Lo creo pasándole el programa de bloques
     */
    public BlockProgram() {
        blockProgram = new ArrayList<BlockLink>();
    }

    /**
     * Añado un CommandBlock al BlockProgram
     * Se ocupa de añadir información extra si va dentro de un bucle Repetir
     * @param block el CommandBlock que quiero añadir
     * @param position la posición relativa
     * @param anchordId id del View (CommandBlock) al que se conecta
     */
    public void addBlock(CommandBlock block, CommandBlockPosition position, int anchordId) {


        // creo el nuevo BlockLink a añadir
        BlockLink blockLink = new BlockLink(block, position, anchordId);
        if (tellme) { System.out.println("BlockProgram addBlock()"); }
        blockProgram.add(blockLink);


        if (tellme) { System.out.println("Añado block " + block.getType() + " " + position + " de " + anchordId); }

        if (block.getType() == CommandBlockName.REPEAT) {
            // si es un bloque Repetir guardo la info en el blockLink
            blockLink.setRepeat(BlockLink.REPEAT_OPEN);
            // quito cualquier otro repetir open
            removeOtherRepeatBlocks(blockProgram.size() - 1);
        } else if (!block.isSensor()) {
            // compruebo si su ancla es un bloque repetir
            int anchorBlockIndex = getBlockIndex(anchordId);
            CommandBlock anchorBlock = blockProgram.get(anchorBlockIndex).getBlock();
            if (anchorBlock.getType() == CommandBlockName.REPEAT) {
                // añadido el último bajo bloque repetir -> cierro bucle repetir
                blockLink.setRepeat(BlockLink.REPEAT_LAST);
                if (tellme) {
                    System.out.println("Activado último de bucle");
                }
            }
        }
    }

    /**
     * Sólo puede quedar uno
     * Para niños de primaria no anidar Repeats
     */
    private void removeOtherRepeatBlocks(int repeatIdThatRemains) {
        System.out.println("removeOtherRepeatBlocks except " + repeatIdThatRemains);
        this.printOut();
        int indexOfBlockToEliminate = -1;
        int oldAnchor = -1;
        int oldSensor = -1;
        BlockLink newRepeatLink = null;
        for(int i=0; i<blockProgram.size();i++) {
            if (i != repeatIdThatRemains) {
                BlockLink link = blockProgram.get(i);
                CommandBlock block = link.getBlock();
                if (block.getType() == CommandBlockName.REPEAT) {
                    // sólo puede quedar uno
                    oldAnchor = blockProgram.get(i).getAnchor();
                    indexOfBlockToEliminate = i;
                    if(block.hasSensorConnected()) {
                        oldSensor = block.getRightSideSensorId();
                    }
                }
            } else {
                newRepeatLink = blockProgram.get(i);
            }
        }
        if (indexOfBlockToEliminate > -1 && oldAnchor > -1) {
            BlockLink toBeGone = blockProgram.get(indexOfBlockToEliminate);
            BlockLink lastOnList = null;
            // ahora busco el que usa el ancla que voy a borrar
            for (int i = 0; i < blockProgram.size(); i++) {
                BlockLink link = blockProgram.get(i);

                if (link.getBlock().getType() != CommandBlockName.REPEAT && i < blockProgram.size() - 1) {
//                    link.printOut();
                    link.setRepeat(BlockLink.REPEAT_NONE);
                }
                if (link.getAnchor() == toBeGone.getBlock().getId()) {
//                    link.printOut();
                    link.setAnchor(oldAnchor);
                }
                if (link.getBlock().getId() == oldSensor) {
                    if (newRepeatLink != null) {
//                        newRepeatLink.printOut();
//                        link.printOut();
                        newRepeatLink.getBlock().setSensorConnected(true);
                        newRepeatLink.getBlock().setRightSideSensor(link.getBlock().getId());
                        link.setAnchor(newRepeatLink.getBlock().getId());
                    }
                }
                if (i != indexOfBlockToEliminate) {
                    if (!link.getBlock().isSensor() && link.getBlock().getType() != CommandBlockName.REPEAT) {
                        lastOnList = link;
                    }
                    if (link.getBlock().getType() == CommandBlockName.REPEAT) {
                        lastOnList = null;
                    }
                }
            }
            blockProgram.remove(indexOfBlockToEliminate);
            if (lastOnList != null) {
                System.out.println("last on list");
                lastOnList.printOut();
                lastOnList.setRepeat(BlockLink.REPEAT_LAST);
            } else {
                System.out.println("last on list is NULL");
            }
        }
        printOut();

    }

    /**
     * Borra un bloque de la lista
     * Reorganiza la lista cambiando los enlaces relativos al borrado
     * Si estoy borrando un bloque Repetir o un fin de repetir reorganiza la info de los bloques afectados
     * @param blockId el id del bloque
     */
    public void removeBlock(int blockId) {

        if (tellme) {
            System.out.println("BlockProgram removeBlock()");
            System.out.println("Voy a borrar block id "+blockId);
        }

        int indexOfBlockToEliminate = -1;
        boolean deletingARepeatBlock = false;

        // iteramos el programa
        for(int i=0; i<blockProgram.size();i++) {
            CommandBlock block = blockProgram.get(i).getBlock();
            BlockLink link = blockProgram.get(i);
            int idAncla = blockProgram.get(i).getAnchor();

            link.printOut();

            // busco el BlockLink que usa el bloque a borrar como ancla
            if (idAncla == blockId && link.getPosition() == CommandBlockPosition.BELOW) {
                if (tellme) {System.out.println("borrando su ancla, lo cambio por..."); }
                int newAnchorIndex = 0;
                for(int x=i-2; x>=0; x--) {
//                    System.out.println("busco ancla en "+x);
                    if(blockProgram.get(x).getPosition() == CommandBlockPosition.BELOW && blockProgram.get(x).getBlock().getId() != blockId) {
//                        System.out.println("nuevo ancla será");
//                        blockProgram.get(x).printOut();
                        newAnchorIndex = x;
                        break;
                    }
                }
                int nuevoAncla = blockProgram.get(newAnchorIndex).getBlock().getId();
                if (tellme) {System.out.println("... nuevo ancla "+nuevoAncla); }
                link.setAnchor(nuevoAncla);
            }

            // encontrado el bloque a borrar: anoto su id y veo de si es un repetir para reorganizar la lista
            if (block.getId() == blockId) {
                indexOfBlockToEliminate = i;

                if (link.getRepeat() == BlockLink.REPEAT_LAST) {
                    // borrando último de bucle repetir
                    if (tellme) { System.out.println("OJO Borrando fin de bucle!"); }
                    if (i>0) {
                       if(blockProgram.get(i-1).getPosition() == CommandBlockPosition.BELOW && !blockProgram.get(i-1).getBlock().isSensor()) {
                           // cambio la indicación de fin de bucle a otro bloque
                           if (tellme) { System.out.println("anterior es un below"); }
                               if (blockProgram.get(i-1).getBlock().getType() == CommandBlockName.REPEAT) {
                                   // fin de bucle para el siguiente, si lo hay
                                   if (tellme) { System.out.println("anterior es un repetir: fin de bucle para el siguiente"); }
                                   if (i+1<blockProgram.size()) {
                                       blockProgram.get(i+1).setRepeat(BlockLink.REPEAT_LAST);
                                   }
                               } else {
                                   // fin de bucle para el anterior
                                   if (tellme) { System.out.println("fin de bucle para el anterior"); }
                                   blockProgram.get(i-1).setRepeat(BlockLink.REPEAT_LAST);
                               }
                       }
                    }
                }

                // si estoy borrando un bloque repetir lo anoto para buscar el fin del bucle
                if (link.getRepeat() == BlockLink.REPEAT_OPEN) {
                    deletingARepeatBlock = true;
                    if (tellme) { System.out.println("Borrando un Bloque REPETIR"); }
                }
                if (tellme) {System.out.println("Borro block "+block.getType()+" id "+block.getId());}
            } // fin de block a borrar encontrado

            // si he anotado que estoy borrando un bloque repetir: se elimina el fin de repetir
            if (deletingARepeatBlock && link.getRepeat() == BlockLink.REPEAT_LAST) {
                if (tellme) { System.out.println("Encontrado fin de bucle en el "+i+" quitando final de este bucle"); }
                link.setRepeat(BlockLink.REPEAT_NONE);
                // no quiero afectar a otros finales de bucles
                deletingARepeatBlock = false;
            }

        } // fin de iteración de la lista de bloques

        // ahora lo puedo borrar
        if (indexOfBlockToEliminate > -1) {
            blockProgram.remove(indexOfBlockToEliminate);
        }
    }


     /**
     * Devuelve el indice de un bloque en el programa (ArrayList)
     * @param block
     * @return
     */
    private int getBlockIndex(CommandBlock block) {
        for(int i=0; i<blockProgram.size();i++) {
            CommandBlock aBlock = blockProgram.get(i).getBlock();
//            System.out.println(i+" "+aBlock.getType()+" id "+aBlock.getId());
            if (aBlock.getId() == block.getId()) {
//                System.out.println("encontrado en posicion " + i);
                return i;
            }
        }
        return 0;
    }

    /**
     * Devuelve el indice de un bloque en el programa (ArrayList)
     * @param blockIndex
     * @return
     */
    private int getBlockIndex(int blockIndex) {
        for(int i=0; i<blockProgram.size();i++) {
            CommandBlock aBlock = blockProgram.get(i).getBlock();
//            System.out.println(i+" "+aBlock.getType()+" id "+aBlock.getId());
            if (aBlock.getId() == blockIndex) {
//                System.out.println("encontrado en posicion " + i);
                return i;
            }
        }
        return 0;
    }

    /**
     * Imprime el bloque de comandos por la consola
     * Se usa sólo para debugging
     */
    public void printOut() {
        System.out.println("## Inicio BlockProgram:");
        for(int i=0; i<this.getSize(); i++) {
            System.out.print(i+" ");
            this.get(i).printOut();
        }
        System.out.println("## Fin BlockProgram.");
    }

    public int getSize() {
        return blockProgram.size();
    }

    public BlockLink get(int i) {
        return blockProgram.get(i);
    }


    /**
     * Inserta un bloque delante de otro y reordena la lista
     * Realiza todas las operaciones de ajuste necesarios:
     * actualiza los anclas de los bloques afectados
     * tiene en cuenta la información sobre la situación dentro de los bucles Repetir
     * @param droppedBlock bloque que el usuario ha soltado
     * @param receiverBlock bloque sobre el que el usuario ha soltado el bloque
     */
    public void insertBlock(CommandBlock droppedBlock, CommandBlock receiverBlock) {
        if (tellme) { System.out.println("BlockProgram insertBlock()"); }
        // desactivo el conector de repetir
        droppedBlock.activateRepeatConnector(false, droppedBlock.getContext());
//        if (tellme) { this.printOut(); }

        // indice donde insertar
        int insertIndex = getBlockIndex(receiverBlock);

        if (droppedBlock.isDocked()) {
            // si el bloque estaba conectado lo borro del programa
            removeBlock(droppedBlock.getId());
        }

        // el ancla es el id del anterior tipo below
        int anchorId = get(0).getBlock().getId(); // id del bloque inicio
        int indexOfAnchorOnList = 0;
        if (this.get(insertIndex-1).getPosition() == CommandBlockPosition.BELOW) {
            // si el anterior bloque era un BELOW será el nuevo ancla
            anchorId = this.get(insertIndex - 1).getBlock().getId();
            indexOfAnchorOnList = insertIndex-1;
        }

        // creo el BlockLink que insertaremos en la lista
        BlockLink insertLink = new BlockLink(droppedBlock, CommandBlockPosition.BELOW, anchorId);
        if (tellme) { System.out.println("Inserto en " + insertIndex); }
        blockProgram.add(insertIndex, insertLink);




        // si NO soy el último de la lista
        if (insertIndex != blockProgram.size()-1) {
            if (tellme) { System.out.println("No soy el último"); }
            // solo cambio ancla si soy un below
            if (this.get(insertIndex + 1).getPosition() == CommandBlockPosition.BELOW) {
                if (tellme) {
                    System.out.println("Cambio ancla a " + insertIndex + 1);
                }
                this.get(insertIndex + 1).setAnchor(droppedBlock.getId());
            }

            // si soy el penúltimo de la lista
            if(insertIndex == blockProgram.size()-2) {
                if (tellme) { System.out.println("Soy el penúltimo"); }
                // pero el último es un sensor
                // si mi ancla es Repetir seré fin de bucle
                if (this.get(indexOfAnchorOnList).getBlock().getType() == CommandBlockName.REPEAT) {
                    if (this.get(insertIndex + 1).getBlock().isSensor()) {
                        // mi ancla es un Repetir, soy fin de repetir
                        insertLink.setRepeat(BlockLink.REPEAT_LAST);
                    }
                }
            }
        } else {
            if (tellme) { System.out.println("Soy el último");  }
            // soy el último de la lista, si mi ancla es Repetir soy cierre de bucle
            if (this.get(indexOfAnchorOnList).getBlock().getType() == CommandBlockName.REPEAT) {
                // mi ancla es un Repetir, soy fin de repetir
                insertLink.setRepeat(BlockLink.REPEAT_LAST);
            }
        }

        // si el bloque insertado es un bloque REPETIR tendremos que añadir un fin de REPETIR al final de la lista
        if (insertLink.getBlock().getType() == CommandBlockName.REPEAT) {
            if (tellme) { System.out.println("OJO Insertando un REPEAT block"); }
            insertLink.setRepeat(BlockLink.REPEAT_OPEN);
            int lastBlockIndex = this.getSize()-1;
            if (tellme) { System.out.println("Compruebo que insertIndex "+insertIndex+" no es el último "+lastBlockIndex); }
            if (insertIndex != lastBlockIndex) {
                // no he insertado el último
                if (get(lastBlockIndex).getPosition() == CommandBlockPosition.BELOW) {
                    if (tellme) {
                        System.out.println("Repeat close a " + lastBlockIndex);
                    }
                    if (get(lastBlockIndex).getBlock().getType() != CommandBlockName.REPEAT) {
                        // si el último no es un sensor y no es repetir
                        get(lastBlockIndex).setRepeat(BlockLink.REPEAT_LAST);
                    }
                } else {
                    if (lastBlockIndex-1 != insertIndex) {
                        // el último no era BELOW el penúltimo tiene que serlo
                        get(lastBlockIndex - 1).setRepeat(BlockLink.REPEAT_LAST);
                    }
                }
            }
        }

        if (droppedBlock.getType() == CommandBlockName.REPEAT) {
            // sólo puede quedar uno
            removeOtherRepeatBlocks(insertIndex);
        }

        if (tellme) { this.printOut(); }
     }
}
