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
