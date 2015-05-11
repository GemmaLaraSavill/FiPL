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

            // busco el BlockLink que usa el bloque a borrar como ancla
            if (idAncla == blockId && link.getPosition() == CommandBlockPosition.BELOW) {
                if (tellme) {System.out.println("borrando su ancla, lo cambio por..."); }
                int nuevoAncla = blockProgram.get(i - 2).getBlock().getId();
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
        if (tellme) { this.printOut(); }

        // indice donde insertar
        int insertIndex = getBlockIndex(receiverBlock);

        if (droppedBlock.isDocked()) {
            // si el bloque estaba conectado lo borro del programa
            removeBlock(droppedBlock.getId());
        }

        // el ancla es el id del anterior tipo below
        int anchorId = get(0).getBlock().getId(); // id del bloque inicio
        if (this.get(insertIndex-1).getPosition() == CommandBlockPosition.BELOW) {
            // si el anterior bloque era un BELOW será el nuevo ancla
            anchorId = this.get(insertIndex - 1).getBlock().getId();
        }

        // creo el BlockLink que insertaremos en la lista
        BlockLink insertLink = new BlockLink(droppedBlock, CommandBlockPosition.BELOW, anchorId);
        if (tellme) { System.out.println("Inserto en "+insertIndex); }
        blockProgram.add(insertIndex, insertLink);

        // si el insertado no es el último de la lista el ancla del siguiente será el bloque insertado
        if (insertIndex != blockProgram.size()-1) {
            // solo cambio ancla si soy un below
            if (this.get(insertIndex+1).getPosition() == CommandBlockPosition.BELOW) {
                if(tellme) { System.out.println("Cambio ancla a "+insertIndex+1); }
                this.get(insertIndex+1).setAnchor(droppedBlock.getId());
            }
        }

        // si el bloque insertado es un bloque REPETIR tendremos que añadir un fin de REPETIR al final de la lista
        if (insertLink.getBlock().getType() == CommandBlockName.REPEAT) {
            if (tellme) { System.out.println("OJO Insertando un REPEAT block"); }
            int lastBlockIndex = this.getSize()-1;
            if (tellme) { System.out.println("Compruebo que insertIndex "+insertIndex+" no es el último "+lastBlockIndex); }
            if (insertIndex != lastBlockIndex) {
                if (get(lastBlockIndex).getPosition() == CommandBlockPosition.BELOW) {
                    if (tellme) {
                        System.out.println("Repeat close a " + lastBlockIndex);
                    }
                    get(lastBlockIndex).setRepeat(BlockLink.REPEAT_LAST);
                }
            }
        }
        if (tellme) { this.printOut(); }
     }
}
