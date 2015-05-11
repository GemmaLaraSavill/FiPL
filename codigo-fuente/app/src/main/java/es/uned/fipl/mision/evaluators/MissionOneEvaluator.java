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
