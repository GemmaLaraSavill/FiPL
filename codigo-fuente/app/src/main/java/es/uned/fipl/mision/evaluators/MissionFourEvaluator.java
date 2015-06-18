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
package es.uned.fipl.mision.evaluators;

import android.content.Context;

import java.util.ArrayList;

import es.uned.fipl.block.CommandBlockName;
import es.uned.fipl.program.BlockProgram;
import es.uned.fipl.program.CodeCommand;
import es.uned.fipl.program.Program;

/**
 * Created by Gemma Lara Savill on 03/05/2015.
 * Evaluador de la misión 4
 * El robot ha verificado que ha pasado por el objetivo intermedio
 * y ha terminado en el cuadrado objetivo final
 *
 * Ahora puedo evaluar la calidad del programa
 * Quiero que los niños usen el bloque repetir
 * Que usen todos los bloques para hacer el programa
 * para darle la máxima nota
 */
public class MissionFourEvaluator extends MissionEvaluator {


    public MissionFourEvaluator(Program solution, Context context) {
        super(solution, context);
    }

    /**
     * Evalúa si se han usado todos los comandos disponibles: perfecto
     * Si no: OK, pero se puede mejorar
     * @param blockProgram el programa realizado por el usuario
     * @return String PERFECT, OK o NOOK constantes estáticas de esta clase
     */
    @Override
    public String evaluate(BlockProgram blockProgram) {
//        System.out.println("evaluando misión cuatro");
        Program proposedSolution = new Program(blockProgram);
        boolean programPerfect = evaluateCommands(proposedSolution);
        if (programPerfect) {
            return MissionEvaluator.PERFECT;
        } else {
            // si el Robot ha devuelto mision cumplida es que ha pasado por el
            // objetivo intermedio y ha terminado sobre el objetivo final
            // podemos mostrar el programa perfecto, con repetir
            return MissionEvaluator.OK;
        }
    }

    /**
     * Quiero comprobar que se haya usado un bloque de cada tipo
     * @param proposedSolution lista de bloques usados
     * @return boolean true or false
     */
    private boolean evaluateCommands(Program proposedSolution) {
        ArrayList<CodeCommand> commandList = proposedSolution.getCommandList();
        // ver si se ha usado cada comando
        int usedRepeatCommand = commandUsed(proposedSolution, CommandBlockName.REPEAT);
        int usedColorCommand = commandUsed(proposedSolution, CommandBlockName.COLORSENSOR);
        int usedMoveCommand = commandUsed(proposedSolution, CommandBlockName.MOVE);
        int usedPositionCommand = commandUsed(proposedSolution, CommandBlockName.POSITION);
//        System.out.println("evaluateCommands");
//        System.out.println("usedRepeatCommand "+usedRepeatCommand);
//        System.out.println("usedColorCommand "+usedColorCommand);
//        System.out.println("usedMoveCommand "+usedMoveCommand);
//        System.out.println("usedPositionCommand "+usedPositionCommand);
        if (usedRepeatCommand > 0 && usedMoveCommand > 0 && usedPositionCommand > 0 && usedColorCommand > 0) {
            // bien!!
            return true;
        } else {
            // no se han usado todos los comandos
            return false;
        }
    }


}
