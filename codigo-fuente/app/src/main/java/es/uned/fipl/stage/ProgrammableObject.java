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
