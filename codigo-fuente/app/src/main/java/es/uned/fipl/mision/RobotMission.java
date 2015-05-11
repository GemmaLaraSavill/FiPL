package es.uned.fipl.mision;


import java.util.ArrayList;

import es.uned.fipl.block.CommandBlockName;
import es.uned.fipl.mision.evaluators.MissionEvaluator;
import es.uned.fipl.stage.MissionTarget;
import es.uned.fipl.stage.Stage;

/**
 * Created by Gemma Lara Savill on 19/03/2015.
 *
 * Mision para un Robot:
 * nivel
 * titulo
 * descripcion (instrucciones)
 * escenario de cuadrados
 * objetivos final y parciales (si los hay)
 * evaluador de la misión
 * explicación de misión fallada y cómo cumplirla
 */
public class RobotMission implements Mission {

    private int level; // nivel de la misión
    private ArrayList<CommandBlockName> blockList;
    private int title;
    private int description;
    private Stage stage;
    private ArrayList<MissionTarget> targets;
    private MissionEvaluator evaluator;
    private int failedExplanation;

    // clave para almacenar el nivel del usuario en el dispositivo
    public static final String MY_MISSION_LEVEL = "MyMissionLevel";


    public RobotMission() {
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public ArrayList<CommandBlockName> getBlockList() {
        return blockList;
    }

    @Override
    public void setBlockList(ArrayList<CommandBlockName> blockList) {
        this.blockList = blockList;
    }

    @Override
    public int getTitle() {
        return title;
    }

    @Override
    public int getDescription() {
        return description;
    }

    @Override
    public void setDescription(int description) {
        this.description = description;
    }

    @Override
    public void setTargets(ArrayList<MissionTarget> targets) {
        this.targets = targets;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setTitle(int title) {
        this.title = title;

    }

    @Override
    public void setEvaluator(MissionEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public MissionEvaluator getEvaluator() {
        return evaluator;
    }

    @Override
    public ArrayList<MissionTarget> getTargets() {
        return targets;
    }

    @Override
    public int getFailedExplanation() {
        return failedExplanation;
    }

    @Override
    public void setFailedExplanation(int textResource) {
        this.failedExplanation = textResource;
    }


}
