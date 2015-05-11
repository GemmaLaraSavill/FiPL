package es.uned.fipl.mision.evaluators;

import android.content.Context;

import es.uned.fipl.program.BlockProgram;
import es.uned.fipl.program.Program;

/**
 * Created by Gemma Lara Savill on 03/05/2015.
 * Evaluador de la misión 2
 * Se evalúa que el Robot haya terminado sobre el objetivo
 * y que se haya programado un aumento de velocidad
 */
public class MissionTwoEvaluator extends MissionEvaluator {



    public MissionTwoEvaluator(Program solution, Context context) {
        super(solution, context);
    }

    /**
     * En la Mision Dos se evalua llegar a las coordenadas objetivo
     * y que haya habido un incremento de velocidad
     * @param blockProgram programa creado por el usuario
     * @return String PERFECT, OK o NOOK constantes estáticas de esta clase
     */
    @Override
    public String evaluate(BlockProgram blockProgram) {
//        System.out.println("Evaluation mission two");
//        System.out.println("Llega a evaluador programa de "+blockProgram.getSize());
        // compruebo que llego a objetivo
        Program proposedSolution = new Program(blockProgram);
        boolean reachedTarget = reachedTarget(proposedSolution, finalTarget);
        // compruebo uso bloque Velocidad y el incremento
        boolean speedChanged = speedChanged(blockProgram);

        if (reachedTarget && speedChanged) {
            return MissionEvaluator.PERFECT;
        } else {
            return MissionEvaluator.NOTOK;
        }
    }




}
