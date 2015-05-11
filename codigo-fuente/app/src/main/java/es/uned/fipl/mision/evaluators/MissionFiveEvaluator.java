package es.uned.fipl.mision.evaluators;

import android.content.Context;

import es.uned.fipl.program.BlockProgram;
import es.uned.fipl.program.Program;

/**
 * Created by Gemma Lara Savill on 03/05/2015.
 */
public class MissionFiveEvaluator extends MissionEvaluator {


    public MissionFiveEvaluator(Program solution, Context context) {
        super(solution, context);
    }

    @Override
    public String evaluate(BlockProgram blockProgram) {
        // TODO ver que evaluar para mision cinco
        Program proposedSolution = new Program(blockProgram);
//        if (reachedTarget(proposedSolution, finalTarget)) {
            return MissionEvaluator.PERFECT;
//        }
//        return MissionEvaluator.NOTOK;
    }


}
