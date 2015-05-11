package es.uned.fipl.mision.evaluators;

import android.content.Context;

import es.uned.fipl.block.CommandBlockName;
import es.uned.fipl.program.BlockProgram;
import es.uned.fipl.program.Program;

/**
 * Created by Gemma Lara Savill on 03/05/2015.
 * Evaluacion de la mision tres:
 * usar un bloque esperar
 * sensor numero 5 en comando esperar
 * robot llega a su destino
 */
public class MissionThreeEvaluator extends MissionEvaluator {


    public MissionThreeEvaluator(Program solution, Context context) {
        super(solution, context);
    }

    /**
     * Evaluacion de la mision tres:
     * usar un bloque esperar
     * sensor numero 5 en comando esperar
     * robot llega a su destino
     * @param blockProgram programa creado por el usuario
     * @return String PERFECT, OK o NOOK constantes estáticas de esta clase
     */
    @Override
    public String evaluate(BlockProgram blockProgram) {
        // busco bloque ESPERAR
        Program proposedSolution = new Program(blockProgram);
        int hasWait = commandUsed(proposedSolution, CommandBlockName.WAIT);
        if (hasWait == -1) {
//            System.out.println("No ha usado esperar");
            return MissionEvaluator.NOTOK;
        } else {
            // busco SENSOR NUMERO 5
            int hasNumberSensor = commandUsed(proposedSolution, CommandBlockName.NUMBERSENSOR);
            if (hasNumberSensor == -1) {
//                System.out.println("No ha usado sensor numero");
                return MissionEvaluator.NOTOK;
            } else {
                // debo esperar numero 5
                if (proposedSolution.getCommandList().get(hasNumberSensor).getCommandParameter().equals("5")) {
                    // pinta bien! solo queda llegar al objetivo
                    if (reachedTarget(proposedSolution, finalTarget)) {
                        return MissionEvaluator.PERFECT;
                    }
                } else {
//                    System.out.println("El sensor de numero no esperaba el 5");
                    return MissionEvaluator.NOTOK;
                }
            }
        }
        return MissionEvaluator.NOTOK;
    }

}