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
package es.uned.fipl.stage;

import android.widget.FrameLayout;

import java.util.ArrayList;

import es.uned.fipl.program.Program;

/**
 * Created by Gemma Lara Savill on 15/04/2015.
 *
 * Un objeto programable
 */
public interface ProgrammableObject {

    Coordinates getCoordinates();

    void setStartPosition();

    void runProgram(Program program);
    void stopProgram();

    void badEnding();

    void move(String commandParameter);

    void changePosition(String commandParameter);

    void setSpeed(int speed);
    int getSpeed();


    void setTargets(ArrayList<MissionTarget> targets);


    void addToStage(FrameLayout gridFrame);
    void removeFromStage(FrameLayout gridFrame);

    void stopAction();

    void waitAction();
}
