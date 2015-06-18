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

import es.uned.fipl.block.CommandBlock;
import es.uned.fipl.block.CommandBlockPosition;

/**
 * Created by Gemma Lara Savill on 07/03/2015.
 *
 * Estructura que representa un eslabón en un programa de bloques gráficos
 * Guarda un bloque, la posición en la que se conecta y el id del bloque al que se conecta
 *
 * Formato: CommandBlock    CommandoBlockPosition   anchorId
 * Ejemplo: block           BELOW                   3
 *
 * También almacena información sobre la situación del bloque respecto a un bucle Repetir
 */
public class BlockLink {

    private int insideRepeat;
    private CommandBlock block; // el bloque
    private CommandBlockPosition position; // la posición relativa al bloque que conecta
    private int anchordId; // id del bloque al que está conectado (su ancla)

    // Posibles estados de un BlockLink dentro de un bucle REPETIR
    public static final int REPEAT_NONE = 0; // no está dentro de un bucle Repetir
    public static final int REPEAT_OPEN = 1; // está dentro de un bucle Repetir
    public static final int REPEAT_LAST = 2; // es el último bloque de un bucle Repetir


    /**
     * Crea un BlockLink
     * @param block un CommandBlock
     * @param position una posición CommandBlockPosition
     * @param anchordId id de otro bloque al que conecta
     */
    public BlockLink(CommandBlock block, CommandBlockPosition position, int anchordId) {
        this.block = block;
        this.position = position;
        this.anchordId = anchordId;
        this.insideRepeat = REPEAT_NONE;
    }

    public CommandBlock getBlock() {
        return block;
    }

    public void setBlock(CommandBlock block) {
        this.block = block;
    }

    public CommandBlockPosition getPosition() {
        return position;
    }

    public void setPosition(CommandBlockPosition position) {
        this.position = position;
    }

    public int getAnchor() {
        return anchordId;
    }

    public void setAnchor(int anchordId) {
        this.anchordId = anchordId;
    }

    /**
     * Devuelve información sobre el estado de este BlockLink
     * dentro de un bucle REPETIR
     * @return int REPEAT_NONE, REPEAT_OPEN, REPEAT_LAST constantes en esta class
     */
    public int getRepeat() {
        return insideRepeat;
    }

    /**
     *  Establece información sobre el estado de este BlockLink
     * dentro de un bucle REPETIR
     * @param repeatStatus REPEAT_NONE, REPEAT_OPEN, REPEAT_LAST constantes en esta class
     */
    public void setRepeat(int repeatStatus) {
        this.insideRepeat = repeatStatus;
    }

    /**
     * Imprime la información de este BlockLink
     * por la consola
     */
    public void printOut() {
        System.out.println("Block: "+block.getType()+" "+block.getId()+" Position: "+position+" AnchorId: "+anchordId+ " Repeat mode: "+this.getRepeat());
    }
}
