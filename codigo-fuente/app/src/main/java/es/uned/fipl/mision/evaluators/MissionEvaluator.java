package es.uned.fipl.mision.evaluators;

import android.content.Context;

import java.util.ArrayList;

import es.uned.fipl.R;
import es.uned.fipl.block.CommandBlockName;
import es.uned.fipl.program.BlockProgram;
import es.uned.fipl.program.CodeCommand;
import es.uned.fipl.program.Program;
import es.uned.fipl.stage.Coordinates;
import es.uned.fipl.stage.MissionTarget;

/**
 * Created by Gemma Lara Savill on 03/05/2015.
 * Evalúa una misión para ver si se ha cumplido
 * Cada misión tendrá diferentes puntos de evaluación
 */
public abstract class MissionEvaluator {

    protected Context context;
    protected Program solution;
    protected ArrayList<MissionTarget> targets;
    protected Coordinates finalTarget;

    // posibles resultados de la evaluacion
    public final static String NOTOK = "NOTOK"; // la mision no se ha cumplido
    public final static String OK = "OK"; // la mision se ha cumplido pero la solucion es optima
    public final static String PERFECT = "PERFECT"; // la mision se ha cumplido y la solucion es optima

    public MissionEvaluator(Program solution, Context context) {
        this.solution = solution;
        this.context = context;
    }

    /**
     * Le paso los objetivos al evaluador
     * @param targets
     */
    public void setTargets(ArrayList<MissionTarget> targets) {
        this.targets = targets;
        extractTargets();
    }

    /**
     * Extrae las coordenadas objetivo de la mision
     * Todas las misiones tienen un objetivo final
     * Pueden tener objetivos parciales
     * Todos se extraen aqui
     */
    private void extractTargets() {
        finalTarget = null;
        if (targets.size() == 1) {
            // un solo objetivo
            finalTarget = targets.get(0).getCoordinates();
        } else {
            // mas de un objetivo: parciales y final.
            // objetivo final es el ultimo de la lista
            finalTarget = targets.get(targets.size()-1).getCoordinates();
            extractPartialTargets();

        }
    }

    private void extractPartialTargets() {

    }

    /**
     * Evalua si el programa cumple la funcion
     * de llegar a unas determinadas coordenadas
     * Lo hace sumando las coordenadas
     * @return true or false
     * @param program programa con los cambios de posición
     * @param finalTarget datos de posición final buscados
     */
    protected boolean reachedTarget(Program program, Coordinates finalTarget) {
        ArrayList<CodeCommand> commandList = program.getCommandList();
        Coordinates position = new Coordinates(0,0);
        for (int i=0; i<commandList.size(); i++) {
            String command = commandList.get(i).getCommandName();
            if (command.equals(CommandBlockName.MOVE.toString())) {
                String parameter = commandList.get(i).getCommandParameter();
                if (parameter.equals(context.getString(R.string.param_right))) {
                    // sumo uno a la coordenada X
                    position = new Coordinates(position.getX()+1, position.getY());
                } else if (parameter.equals(context.getString(R.string.param_left))) {
                    // resto uno a la coordenada X
                    position = new Coordinates(position.getX()-1, position.getY());
                } if (parameter.equals(context.getString(R.string.param_down))) {
                    // sumo uno a la coordenada Y
                    position = new Coordinates(position.getX(), position.getY()+1);
                } if (parameter.equals(context.getString(R.string.param_up))) {
                    // resto uno a la coordenada Y
                    position = new Coordinates(position.getX(), position.getY()-1);
                }
            } else if (command.equals(CommandBlockName.POSITION.toString())) {
                // nuevas coordenadas para la posicion
                String parameter = commandList.get(i).getCommandParameter();
                String[] newPosition = parameter.split(",");
                int newX = Integer.parseInt(newPosition[0]);
                int newY = Integer.parseInt(newPosition[1]);
                position = new Coordinates(newX, newY);
            }
        }
        if (position.getX() == finalTarget.getX() && position.getY() == finalTarget.getY()) {
            return  true;
        }
        return  false;
    }

    /**
     * Evalua si ha habido un cambio de velocidad en el programa
     * @param blockProgram
     * @return true or false
     */
    protected boolean speedChanged(BlockProgram blockProgram) {
        // examino a ver si hay un bloque velocidad antes del ultimo
        // velocidad en la ultima posicion no seria valido
        for(int i=0; i<blockProgram.getSize()-1; i++){
            if (blockProgram.get(i).getBlock().getType() == CommandBlockName.SPEED) {
                // encontrado bloque velocidad
                // hay que comprobrar que el parametro es mayor que uno
                String speedSelected = blockProgram.get(i).getBlock().getParameter();
                if (Integer.parseInt(speedSelected)>1) {
                    // OK velocidad mayor que uno
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Evalúa si hay un bloque comando de un tipo específico en el orden position del programa
     * @param program programa que contiene el bloque buscado
     * @param blockType el tipo de bloque buscado
     * @param position la posición en la que el bloque debe ser encontrado
     * @return true or false
     */
    protected boolean commandAtPosition(Program program, CommandBlockName blockType, int position) {
        CodeCommand command = program.getCommandList().get(position);
        if (command.getCommandName().equals(blockType.toString())) {
            System.out.println("Found "+blockType.toString()+" at "+position);
            return true;
        }
        program.printProgram();
        return false;
    }

    /**
     * Busca un comando en el programa y devuelve su posicion
     * @param program programa que contiene el bloque
     * @param blockType tipo de comando que busco
     * @return int posición del bloque buscado
     */
    protected int commandUsed(Program program, CommandBlockName blockType) {
//        System.out.println("buscando en programa de "+program.getCommandList().size());
        ArrayList<CodeCommand> commandList = program.getCommandList();
        for (int i=0; i<commandList.size(); i++) {
//            System.out.println("Examinando "+commandList.get(i).getCommandName()+" en "+i);
            if (commandList.get(i).getCommandName().equals(blockType.toString())) {
//                System.out.println("Encontrado "+blockType.toString()+" en programa en el "+i);
                return i;
            }
        }
//        System.out.println("NO encontrado "+blockType.toString()+" en programa: -1");
        return -1;
    }

    /**
     * Función principal del evaluador
     * Cada misión tendrá su implementación
     * @param program programa de bloques a evaluar
     * @return String PERFECT, OK o NOOK constantes estáticas de esta clase
     */
    public abstract String evaluate(BlockProgram program);
}
