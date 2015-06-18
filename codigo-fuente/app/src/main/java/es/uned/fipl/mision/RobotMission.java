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
