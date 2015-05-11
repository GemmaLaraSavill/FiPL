package es.uned.fipl.mision;


import android.content.Context;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.Random;

import es.uned.fipl.R;
import es.uned.fipl.block.CommandBlockName;
import es.uned.fipl.mision.evaluators.MissionEvaluator;
import es.uned.fipl.mision.evaluators.MissionFourEvaluator;
import es.uned.fipl.mision.evaluators.MissionOneEvaluator;
import es.uned.fipl.mision.evaluators.MissionThreeEvaluator;
import es.uned.fipl.mision.evaluators.MissionTwoEvaluator;
import es.uned.fipl.program.CodeCommand;
import es.uned.fipl.program.Program;
import es.uned.fipl.stage.Coordinates;
import es.uned.fipl.stage.MissionTarget;
import es.uned.fipl.stage.Stage;



/**
 * Created by Gemma Lara Savill on 19/03/2015.
 *
 * Crea misiones
 */
public class RobotMissionFactory implements MissionFactory {

    private final Context context;
    private Mission mission;
    private ArrayList<CommandBlockName> blockList;
    private ArrayList<MissionTarget> targets;
    private Coordinates targetTilePosition;
    private MissionEvaluator evaluator;

    public RobotMissionFactory(Context context) {
        this.context = context;
    }

    @Override
    public Mission createMission(int id) {
        switch (id){
            case 0:
                mission = createMissionNoMission();
                System.out.println("No hay misión, cargo por defecto");
                break;
            case 1:
                mission = createMissionOne();
                System.out.println("Creo una MISION de nivel UNO");
                break;
            case 2:
                mission = createMissionTwo();
                System.out.println("Creo una MISION de nivel DOS");
                break;
            case 3:
                mission = createMissionThree();
                System.out.println("Creo una MISION de nivel TRES");
                break;
            case 4:
                mission = createMissionFour();
                System.out.println("Creo una MISION de nivel CUATRO");
                break;
            case 5:
                mission = createMissionFive();
                System.out.println("Creo una MISION de nivel CINCO, con "+targets.size()+ " objetivos");
                break;
        }
        return mission;
    }

    /**
     * Crea la misión nivel 0: no hay misión
     * Se carga cuando el usuario ya ha cumplido todas las misiones
     * No tiene objetivos ni evaluador
     * Se pueden usar TODOS los bloques
     * Programación libre
     * @return
     */
    private Mission createMissionNoMission() {
        Mission mission = new RobotMission();
        mission.setLevel(0);
        mission.setTitle(R.string.mission0_title);
        mission.setDescription(R.string.mission0_description);
        // bloques que se pueden usar (todos)
        blockList = new ArrayList<>();
        blockList.add(CommandBlockName.START);
        blockList.add(CommandBlockName.MOVE);
        blockList.add(CommandBlockName.POSITION);
        blockList.add(CommandBlockName.SPEED);
        blockList.add(CommandBlockName.WAIT);
        blockList.add(CommandBlockName.REPEAT);
        blockList.add(CommandBlockName.COLORSENSOR);
        blockList.add(CommandBlockName.NUMBERSENSOR);
        mission.setBlockList(blockList);
        // escenario para la misión
        mission.setStage(createStage());
        // objetivos de la misión (no hay)
        targets = new ArrayList<MissionTarget>();
        mission.setTargets(targets);
        return mission;

    }

    /**
     * Crea la misión de nivel 1
     * Sólo puedes usar el bloque MOVER
     * @return
     */
    private Mission createMissionOne() {
        Mission mission = new RobotMission();
        // datos de la misión
        mission.setLevel(1);
        mission.setTitle(R.string.mission1_title);
        mission.setDescription(R.string.mission1_description);
        // bloques que se pueden usar
        blockList = new ArrayList<>();
        blockList.add(CommandBlockName.START);
        blockList.add(CommandBlockName.MOVE);
        mission.setBlockList(blockList);
        // escenario para la misión
        mission.setStage(createStage());
        // objetivos de la misión
        targetTilePosition = new Coordinates(2,2);
        targets = new ArrayList<MissionTarget>();
        targets.add(new MissionTarget(targetTilePosition, R.drawable.robot_target_1));
        mission.setTargets(targets);
        // programa solución
        Program solution = new Program();
        solution.addCommand(new CodeCommand(1, CommandBlockName.MOVE.toString(), context.getString(R.string.param_right)));
        solution.addCommand(new CodeCommand(2, CommandBlockName.MOVE.toString(), context.getString(R.string.param_right)));
        solution.addCommand(new CodeCommand(3, CommandBlockName.MOVE.toString(), context.getString(R.string.param_down)));
        solution.addCommand(new CodeCommand(4, CommandBlockName.MOVE.toString(), context.getString(R.string.param_down)));
        evaluator = new MissionOneEvaluator(solution, context);
        evaluator.setTargets(targets);
        mission.setEvaluator(evaluator);
        return mission;
    }

    /**
     * Crea la misión de nivel 2
     * Bloques usados: MOVER y VELOCIDAD
     * @return
     */
    private Mission createMissionTwo() {
        Mission mission = new RobotMission();
        // datos de la misión
        mission.setLevel(2);
        mission.setTitle(R.string.mission2_title);
        mission.setDescription(R.string.mission2_description);
        // bloques que se pueden usar
        blockList = new ArrayList<>();
        blockList.add(CommandBlockName.START);
        blockList.add(CommandBlockName.MOVE);
        blockList.add(CommandBlockName.SPEED);
        mission.setBlockList(blockList);
        // escenario para la misión
        mission.setStage(createStage());
        // objetivos de la misión
        targetTilePosition = new Coordinates(2,2);
        targets = new ArrayList<MissionTarget>();
        targets.add(new MissionTarget(targetTilePosition, R.drawable.robot_target_1));
        mission.setTargets(targets);
        // programa solución
        Program solution = new Program();
        solution.addCommand(new CodeCommand(1, CommandBlockName.SPEED.toString(), "2"));
        solution.addCommand(new CodeCommand(2, CommandBlockName.MOVE.toString(), context.getString(R.string.param_right)));
        solution.addCommand(new CodeCommand(3, CommandBlockName.MOVE.toString(), context.getString(R.string.param_right)));
        solution.addCommand(new CodeCommand(4, CommandBlockName.MOVE.toString(), context.getString(R.string.param_down)));
        solution.addCommand(new CodeCommand(5, CommandBlockName.MOVE.toString(), context.getString(R.string.param_down)));
        evaluator = new MissionTwoEvaluator(solution, context);
        evaluator.setTargets(targets);
        mission.setEvaluator(evaluator);
        mission.setFailedExplanation(R.string.mission2_failed_explanation);
        return mission;
    }

    /**
     * Crea la misión de nivel 3
     * Bloques usados: MOVER, ESPERAR y SENSOR NUMERO
     * @return
     */
    private Mission createMissionThree() {
        Mission mission = new RobotMission();
        // datos de la misión
        mission.setLevel(3);
        mission.setTitle(R.string.mission3_title);
        mission.setDescription(R.string.mission3_description);
        // bloques que se pueden usar
        blockList = new ArrayList<>();
        blockList.add(CommandBlockName.START);
        blockList.add(CommandBlockName.MOVE);
        blockList.add(CommandBlockName.WAIT);
        blockList.add(CommandBlockName.NUMBERSENSOR);
        mission.setBlockList(blockList);
        // escenario para la misión
        mission.setStage(createStage());
        // objetivos de la misión
        targetTilePosition = new Coordinates(2,1);
        targets = new ArrayList<MissionTarget>();
        targets.add(new MissionTarget(targetTilePosition, R.drawable.robot_target_1));
        mission.setTargets(targets);
        // programa solución
        Program solution = new Program();
        solution.addCommand(new CodeCommand(1, CommandBlockName.WAIT.toString(), ""));
        solution.addCommand(new CodeCommand(2, CommandBlockName.NUMBERSENSOR.toString(), "5"));
        solution.addCommand(new CodeCommand(3, CommandBlockName.MOVE.toString(), context.getString(R.string.param_right)));
        solution.addCommand(new CodeCommand(4, CommandBlockName.MOVE.toString(), context.getString(R.string.param_right)));
        solution.addCommand(new CodeCommand(5, CommandBlockName.MOVE.toString(), context.getString(R.string.param_down)));
        evaluator = new MissionThreeEvaluator(solution, context);
        evaluator.setTargets(targets);
        mission.setEvaluator(evaluator);
        mission.setFailedExplanation(R.string.mission3_failed_explanation);
        return mission;
    }

    /**
     * Crea la misión de nivel 4
     * Bloques usados: MOVER, SENSOR COLOR, POSICION y REPETIR
     * Tiene escenario de cuadrados especial
     * Tiene un objetivo parcial y uno final
     * @return
     */
    private Mission createMissionFour() {
        Mission mission = new RobotMission();
        // datos de la misión
        mission.setLevel(4);
        mission.setTitle(R.string.mission4_title);
        mission.setDescription(R.string.mission4_description);
        // bloques que se pueden usar
        blockList = new ArrayList<>();
        blockList.add(CommandBlockName.START);
        blockList.add(CommandBlockName.MOVE);
        blockList.add(CommandBlockName.COLORSENSOR);
        blockList.add(CommandBlockName.POSITION);
        blockList.add(CommandBlockName.REPEAT);
        mission.setBlockList(blockList);
        // escenario para la misión
        mission.setStage(createMissionFourStage());
        // objetivos de la misión
        targets = new ArrayList<MissionTarget>();
        targets.add(new MissionTarget(new Coordinates(4,0), R.color.solution_tile_color));
        targets.add(new MissionTarget(new Coordinates(1,3), R.drawable.robot_target_1));
        mission.setTargets(targets);
        // programa solución
        Program solution = new Program();
        solution.addCommand(new CodeCommand(1, CommandBlockName.REPEAT.toString(), ""));
        String solutionTileColor = Integer.toHexString(context.getResources().getColor(R.color.solution_tile_color) & 0x00ffffff);
        solution.addCommand(new CodeCommand(2, CommandBlockName.COLORSENSOR.toString(), solutionTileColor));
        solution.addCommand(new CodeCommand(3, CommandBlockName.MOVE.toString(), context.getString(R.string.param_right)));
        solution.addCommand(new CodeCommand(4, CommandBlockName.POSITION.toString(), "2,4"));
        evaluator = new MissionFourEvaluator(solution, context);
        evaluator.setTargets(targets);
        mission.setEvaluator(evaluator);
        mission.setFailedExplanation(R.string.mission4_failed_explanation);
        return mission;
    }

    /**
     * Crea la misión de nivel 5
     * Por hacer
     * @return
     */
    private Mission createMissionFive() {
        Mission mission = new RobotMission();
        // datos de la misión
        mission.setLevel(5);
        mission.setTitle(R.string.mission5_title);
        mission.setDescription(R.string.mission5_description);
        // bloques que se pueden usar
        blockList = new ArrayList<>();
        blockList.add(CommandBlockName.START);
        blockList.add(CommandBlockName.MOVE);
        blockList.add(CommandBlockName.SPEED);
        blockList.add(CommandBlockName.WAIT);
        blockList.add(CommandBlockName.NUMBERSENSOR);
        blockList.add(CommandBlockName.COLORSENSOR);
        blockList.add(CommandBlockName.POSITION);
        blockList.add(CommandBlockName.REPEAT);
        mission.setBlockList(blockList);
        // escenario para la misión
        mission.setStage(createStage());
        // objetivos de la misión
        targetTilePosition = new Coordinates(2,1);
        Coordinates targetTile2Position = new Coordinates(4,2);
        // targets objetivos
        targets = new ArrayList<MissionTarget>();
        targets.add(new MissionTarget(targetTilePosition, R.drawable.robot_target_1));
        targets.add(new MissionTarget(targetTile2Position, R.drawable.robot_target_2));
        mission.setTargets(targets);
        // programa solución
        Program solution = new Program();
        solution.addCommand(new CodeCommand(1, CommandBlockName.REPEAT.toString(), ""));
        String solutionTileColor = Integer.toHexString(context.getResources().getColor(R.color.solution_tile_color) & 0x00ffffff);
        solution.addCommand(new CodeCommand(2, CommandBlockName.COLORSENSOR.toString(), solutionTileColor));
        solution.addCommand(new CodeCommand(3, CommandBlockName.MOVE.toString(), context.getString(R.string.param_right)));
        solution.addCommand(new CodeCommand(4, CommandBlockName.POSITION.toString(), "0,0"));
        evaluator = new MissionFourEvaluator(solution, context);
        evaluator.setTargets(targets);
        mission.setEvaluator(evaluator);
        return mission;
    }



    /**
     * Crea un escenario de cuadros de colores
     * Se crean con colores aleatorios elegidos de la lista
     * de colores
     * @return
     */
    private Stage createStage() {
        // escenario
        int[] tiles = createRandomTiles();
        Stage stage = new Stage();
        stage.setColorTiles(tiles);
        return stage;
    }

    /**
     * Crea un escenario de cuadros de colores
     * especial para la misión 4
     * @return
     */
    private Stage createMissionFourStage() {
        // escenario
        int[] tiles = createMissionFourRandomTiles();
        Stage stage = new Stage();
        stage.setColorTiles(tiles);
        return stage;
    }

    /**
     * Crea una lista de colores aleatorios
     * a partir de una lista de colores establecida
     * en colors.xml
     * @return array de int correpondiente a colores
     */
    private int[] createRandomTiles() {
        // extraigo los colores de la lista en XML
        TypedArray ta = context.getResources().obtainTypedArray(R.array.colors);
        int[] colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
        }
        ta.recycle();

        // cuantas baldosas
        int stageTilesLength = Stage.ROWS * Stage.COLS;

        // creo array de colores random
        Random rand = new Random();
        int numColors = colors.length;
        int randomNum = 0;
        int[] tiles = new int[stageTilesLength];
        for (int i=0; i<stageTilesLength; i++) {
            randomNum = rand.nextInt(numColors);
            tiles[i] = colors[randomNum];
        }
        return tiles;
    }

    /**
     * Crea una lista de colores aleatorios
     * a partir de una lista de colores establecida
     * en colors.xml
     * En la misión 4 necesito un sólo cuadro rojo en un sitio determinado
     * @return array de int correpondiente a colores
     */
    private int[] createMissionFourRandomTiles() {
        // extraigo los colores de la lista en XML
        TypedArray ta = context.getResources().obtainTypedArray(R.array.colors);
        int[] colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
        }
        // cuantas baldosas
        int stageTilesLength = Stage.ROWS * Stage.COLS;

        // creo array de colores random
        Random rand = new Random();
        int numColors = colors.length-1;
        int randomNum = 0;
        int[] tiles = new int[stageTilesLength];
        for (int i=0; i<stageTilesLength; i++) {
            randomNum = rand.nextInt(numColors);
            tiles[i] = colors[randomNum];
        }
        tiles[4] = ta.getColor(4, 0);
        ta.recycle();
        return tiles;
    }


}
