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

import es.uned.fipl.program.BlockProgram;
import es.uned.fipl.program.Program;

/**
 * Created by Gemma Lara Savill on 03/05/2015.
 * Evaluador de la misión 1
 * en la Mision Uno sólo se evalua llegar a las coordenadas objetivo
 */
public class MissionOneEvaluator extends MissionEvaluator {


    public MissionOneEvaluator(Program solution, Context context) {
        super(solution, context);
    }

    /**
     *
     * @param blockProgram
     * @return String PERFECT, OK o NOOK constantes estáticas de esta clase
     */
    @Override
    public String evaluate(BlockProgram blockProgram) {
        Program proposedSolution = new Program(blockProgram);
        if (reachedTarget(proposedSolution, finalTarget)) {
            return MissionEvaluator.PERFECT;
        }
        return MissionEvaluator.NOTOK;
    }


}
