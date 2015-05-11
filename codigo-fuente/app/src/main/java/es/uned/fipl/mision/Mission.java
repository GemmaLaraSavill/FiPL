package es.uned.fipl.mision;

import java.util.ArrayList;

import es.uned.fipl.block.CommandBlockName;
import es.uned.fipl.mision.evaluators.MissionEvaluator;
import es.uned.fipl.stage.MissionTarget;
import es.uned.fipl.stage.Stage;

/**
 * Created by Gemma Lara Savill on 19/03/2015
 *
 * Una mision
 * Se identifica por un id (int) de nivel
 *
 */
public interface Mission {

    // nivel de la mision
    void setLevel(int level);

    // lista de bloques que se podran usar
    ArrayList<CommandBlockName> getBlockList();
    void setBlockList(ArrayList<CommandBlockName> blockList);

    // titulo de la mision
    int getTitle();
    void setTitle(int s);

    // descripicion para el usuario: instrucciones
    int getDescription();
    void setDescription(int description);

    // lista de objetivos
    void setTargets(ArrayList<MissionTarget> targets);
    ArrayList<MissionTarget> getTargets();

    // escenario de cuadrados
    Stage getStage();
    void setStage(Stage stage);

    // evaluador de la mision: analizara la correccion del programa de bloques
    void setEvaluator(MissionEvaluator evaluator);
    MissionEvaluator getEvaluator();

    // explicacion de porque no se ha cumplido la mision
    int getFailedExplanation();
    void setFailedExplanation(int text);
}
