package es.uned.fipl.stage;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import es.uned.fipl.MainActivity;
import es.uned.fipl.R;
import es.uned.fipl.block.CommandBlockName;
import es.uned.fipl.program.CodeCommand;
import es.uned.fipl.program.Program;

/**
 * Created by Gemma Lara Savill on 15/04/2015.
 *
 * Un Robot programable
 *
 * Puede ejecutar un programa (Program)
 * Ejecuta comandos tipo CommandBlockName
 * Su Program es una lista de CodeCommand
 * que ejecuta de manera secuencial, manteniendo
 * el índice del comando ejecutado en un contador
 * de programa (programCounter) lo cual le permite
 * ejecutar bucles
 *
 * Imprime logs de sus acciones y estados en una consola
 *
 * Tiene dos sensores: Color y Número
 */
public class Robot implements ProgrammableObject {

    // relativo a la UI
    private final Context context; // contexto de la ventana Android en la que se crea
    private final ImageView robotView; // contenedor de la imagen gráfica del Robot
    private final Stage stage; // escenario por donde discurre
    private final TextView logScreen; // texto en el que escribir el log
    private final ToggleButton runStopButton; // botón de iniciar/parar su programa
    private MainActivity observer; // ventana observador, se le comunicará el final del programa

    // configuración de actuadores
    private final int BASE_SPEED = 1000; // velocidad de desplazamiento inicial
    private Coordinates position; // posición en el escenario en formato X,Y
    private final int stepSize; // tamaño de un paso en píxeles
    private final int tileWidth; // ancho de un cuadrado (baldosa) del escenario
    private int speed; // velocidad
    private Coordinates finalTargetPosition; // posición final (al terminar el programa)
    private ArrayList<MissionTarget> targets; // lista de objetivos
    private int partialTargetsReached; // número de objetivos parciales alcanzados
    private Animator robotAction; // acción ejectutada por el Robot


    // configuración de ejecución de su programa
    private ArrayList<CodeCommand> commandList; // lista de comandos: programa a ejecutar
    private int programCounter = 1; // se inicia en 1, comando 0 es siempre Inicio: no hace nada
    private int programLength; // número de comandos en el programa
    private boolean repeatMode = false; // indica si se ha ejecutado un comando inicio de bucle
    private int repeatEntry; // dirección del comando en que inicia el bucle
    private int repeatEscape; // dirección del comando que sigue al fin de bucle
    private final String lineStart = "Robot# "; // inicio de cada mensaje en el log
    private boolean badEnding;  // si el programa ha acabado mal (en error)

    // sensor de número
    private boolean numberSensorActivated = false; // activado si o no
    private final TextView numberSensor; // texto que contiene el valor del último nº detectado
    private String numberTarget; // número que se quiere detectar
    private String lastNumberPressed; // último número pulsado (detectado)

    // sensor de color
    private boolean colorSensorActivated = false; // activado si o no
    private final ImageView colorSensor; // imagen que toma el valor del último color detectado
    private String colorTarget; // color que se quiere detectar

    private boolean tellme = false; // activa logs en consola java, sólo para depuración
    private MediaPlayer mp;

    /**
     * Constructor del Robot
     * @param container FrameLayout elemento Layout que contiene al Robot en la UI
     * @param runStopButton ToggleButton botón que inicia y apaga su programa
     * @param numberSensor TextView caja de texto donde se indica el último nº detectado
     * @param colorSensor ImageView imagen que muestra el último color detectado
     * @param stage Stage escenario en el cual se moverá
     * @param logScreen TextView caja texto en el que se escribirán los logs
     */
    public Robot(FrameLayout container, ToggleButton runStopButton,
                 TextView numberSensor, ImageView colorSensor, Stage stage,
                 TextView logScreen) {

        // pasamos los elementos UI a los que accederemos
        this.context = container.getContext();
        this.stage = stage;
        this.runStopButton = runStopButton;
        this.numberSensor = numberSensor;
        this.colorSensor = colorSensor;
        this.logScreen = logScreen;

        // configuramos estado inicial
        this.speed = BASE_SPEED;
        // step size = stage tile width + padding entre baldosas
        this.stepSize = Stage.getTileWidth(context);
        this.tileWidth = Stage.getTileWidth(context);

        // añado listener al sensor de número
        numberSensor.addTextChangedListener(new NumberChangeListener());

        // añado Robot gráfico al escenario
        robotView =  new ImageView(context);
        robotView.setLayoutParams(new LinearLayout.LayoutParams(tileWidth, tileWidth));
        robotView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.robot));
    }

    /**
     * La posición X y Y en pixeles en el escenario
     * @return Coordinates posición X,Y
     */
    @Override
    public Coordinates getCoordinates() {
        Coordinates currentPosition = new Coordinates((int) robotView.getX(), (int) robotView.getY());
        if (tellme) { currentPosition.printOut(); }
        return currentPosition;
    }

    /**
     * Escribo un mensaje en la consola del Robot
     * en una nueva linea
     * @param message String mensaje a escribir
     */
    private void writeToLogScreen(String message) {
        CharSequence currentText = logScreen.getText();
        String newText = currentText+"\n"+lineStart+" "+message;
        logScreen.setText(newText);
    }

    /**
     * Borro la consola del Robot
     */
    private void clearLogScreen() {
        String newText = lineStart+" "+context.getResources().getString(R.string.robot_ready_txt);
        logScreen.setText(newText);
    }

    /**
     * Devuelve el color del suelo bajo el robot
     * en su posición actual en el programa
     * @return
     */
    private String getColor() {
        int xCoord = position.getX() / stepSize;
        int yCoord = position.getY() / stepSize;
        if (tellme) { System.out.println("Robot (x,y)=(" + xCoord + "," + yCoord + ")"); }
        int robotTileColor = stage.getColor(xCoord, yCoord);
        String nowColor = "#" + Integer.toHexString(robotTileColor);
        return nowColor;
    }

    /**
     * Robot a posición de inicio
     */
    @Override
    public void setStartPosition() {
        robotView.setX(0);
        robotView.setY(0);
        this.speed = BASE_SPEED;
        robotView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.robot));
        int robotTileColor = stage.getColor(0,0);
        // color sensor reset
        colorSensor.setBackgroundColor(robotTileColor);
        String tag = "#"+Integer.toHexString(robotTileColor);
        colorSensor.setTag(tag);
        clearLogScreen();
    }

    /**
     * Ejecuta el programa
     * @param program Program a ejecutar
     */
    @Override
    public void runProgram(Program program) {

        // cargo el programa como una list de comandos
        commandList = program.getCommandList();
        programLength = commandList.size()-1; // restamos uno por Start
        if (tellme) {
            System.out.println("Robot: ejecuto programa de " + programLength + " comandos");
        }

        // empezamos en posición de inicio
        setStartPosition();
        position = getCoordinates();
        if (tellme) {
            position.printOut();
        }
        // configuración de parámetros inicial
        numberSensorActivated = false;
        colorSensorActivated = false;
        repeatMode = false;
        repeatEntry = 0;
        repeatEscape = 0;
        programCounter = 1; // se inicia en 1, comando 0 es siempre Inicio: no hace nada
        partialTargetsReached = 0;
        lastNumberPressed = numberSensor.getText().toString();
        // end of program setup -> a una función aparte? a setStarPosition?

        if (programLength>0) {
            // comunicamos que allá vamos
            clearLogScreen();
            writeToLogScreen(context.getString(R.string.robot_start_txt));
            // cargo y ejecuto el primer comando
            loadCommand(commandList.get(programCounter));
        } else {
            // el programa no tiene comandos! vacio
            stopProgram();
        }
    }

    /**
     * Carga un comando en el Robot y lo ejecuta
     * @param command CodeCommand trae el tipo de comando y sus parámetros específicos
     */
    private void loadCommand(CodeCommand command) {
        tellme = true;

        if (tellme) {
            System.out.println("Cargo comando PC "+programCounter);
            command.printOut();
        }

        if (programCounter == repeatEntry) {
            writeToLogScreen(context.getString(R.string.robot_repeat_start_txt));
        }

        if (command.getCommandName().equals(CommandBlockName.MOVE.toString())) {
            // ejecutar mover el robot un paso
            this.move(command.getCommandParameter());
        }  else if (command.getCommandName().equals(CommandBlockName.POSITION.toString())) {
            // ejectuar un cambio de posición
            this.changePosition(command.getCommandParameter());
        } else if (command.getCommandName().equals(CommandBlockName.SPEED.toString())) {
            // ejecutar un cambio velocidad
            this.setSpeed(Integer.parseInt(command.getCommandParameter()));
            writeToLogScreen(context.getString(R.string.robot_speed_change_txt)+ " "+command.getCommandParameter());
            advanceProgram();
        } else if (command.getCommandName().equals(CommandBlockName.WAIT.toString())) {
            // ejecutar una espera
            this.waitAction();
        } else if (command.getCommandName().equals(CommandBlockName.REPEAT.toString())) {
            // inicio de un bucle, la posición del último comando del bucle viene como parámetro
            if (programLength > programCounter) {
                repeatMode = true;
                repeatEntry = programCounter + 1;
                String repeatEscapeNumber = command.getCommandParameter();
                if (repeatEscapeNumber.length()>0) {
                    repeatEscape = Integer.parseInt(command.getCommandParameter());
                }
//                writeToLogScreen(context.getString(R.string.robot_repeat_start_txt));
                advanceProgram();
            } else {
                // repetir es último comando... repito nada...
                writeToLogScreen(context.getString(R.string.robot_repeat_nothing));
                badEnding();
            }
        } else if (command.getCommandName().equals(CommandBlockName.COLORSENSOR.toString())) {
            // comando sensor: viene siempre inmediatamente detrás de su target
            // repito el comando anterior hasta encontrar el color o fin de programa
            CodeCommand lastCommand = commandList.get(programCounter - 1);
            if (tellme) { System.out.println("color tras "+lastCommand.getCommandName()); }
            colorTarget = command.getCommandParameter();
            String nowColor = getColor();
//            if (tellme) { System.out.println("Color " + nowColor + " esperando " + colorTarget); }
            colorSensorActivated = true;
            if (lastCommand.getCommandName().equals(CommandBlockName.REPEAT.toString())) {
                if (repeatMode) {
                    if (repeatEntry<programLength) {
                        // si la diferencia entre entry y escape tal actualizo
                        repeatEntry = repeatEntry + 1; // no hay que activar el sensor en cada bucle
                        repeatEscape = repeatEscape+1;
                        programCounter = repeatEntry-1;
                        if (tellme) { System.out.println("Inicio bucle en busca de color. PC a " + programCounter); }
                    }
                    // anotado el repetir, seguimos
                    advanceProgram();
                }
            }
        } else if (command.getCommandName().equals(CommandBlockName.NUMBERSENSOR.toString())) {
            // comando sensor: viene siempre inmediatamente detrás de su target
            // repito el comando anterior hasta que se pulse el número esperado o fin de programa
            CodeCommand lastCommand = commandList.get(programCounter - 1);
            if (tellme) {
                System.out.println("number tras " + lastCommand.getCommandName());
            }
            numberTarget = command.getCommandParameter();
            if (tellme) {
                System.out.println("Último número " + lastNumberPressed + " esperando " + numberTarget);
            }
            writeToLogScreen(context.getString(R.string.robot_waiting_for_number) + " " + numberTarget);
            if (lastNumberPressed.equals(numberTarget)) {
                // no hay que esperar el número, ya está detectado
                writeToLogScreen(context.getString(R.string.robot_number_detected_txt));
                if (lastCommand.getCommandName().equals(CommandBlockName.REPEAT.toString())) {
                    if (repeatMode) {
                        repeatMode = false;
                        programCounter = repeatEscape;
                        System.out.println("Fin de bucle: PC a " + programCounter);
                        repeatEntry = 0;
                        repeatEscape = 0;
                        writeToLogScreen(context.getString(R.string.robot_repeat_released));
                        if (tellme) {
                            System.out.println("Salgo de bucle por numero pulsado. PC a " + programCounter);
                        }
                    }
                }
                advanceProgram();
            } else {
                numberSensorActivated = true;
                if (lastCommand.getCommandName().equals(CommandBlockName.REPEAT.toString())) {
                    if (repeatMode) {
                        if (repeatEntry < programLength) {
                            repeatEntry = repeatEntry + 1; // no hay que activar el sensor en cada bucle
                            repeatEscape = repeatEscape + 1;
                            programCounter = repeatEntry-1;
                            if (tellme) {
                                System.out.println("Inicio bucle en espera de número. PC a " + programCounter);
                            }
                        }
                        advanceProgram();
                    }
                }
            }
        }

        }

    /**
     * Avanza el programa
     * Actualiza el contador de programa
     * y ejecuta el nuevo comando
     */
    private void advanceProgram() {
        if (tellme) { System.out.println("Advance program"); }
        if(!repeatMode) {
            // no estamos en modo bucle
            if (tellme) { System.out.println("PC en "+programCounter+" de "+programLength); }
            if(programCounter < programLength) {
                // si no he llegado al final incremento contador de programa
                programCounter = programCounter + 1;
                if (tellme) { System.out.println("Cargo siguiente PC "+programCounter); }
                // cargo y ejecuto comando en contador de programa
                loadCommand(commandList.get(programCounter));
            } else {
                // no hay más comandos: fin de programa
                stopProgram();
                checkEnding(); // a ver cómo ha terminado...
            }
        } else {
            // estoy en modo bucle
            if (tellme) { System.out.println("advance program in Repeat Mode");
                System.out.println("repeatEntry "+repeatEntry);
                System.out.println("repeatEscape "+repeatEscape);}

            if (programCounter == repeatEscape) {
                // ejecutado último comando en un bucle repetir:
                // contador de progrma a inicio del bucle
                programCounter = repeatEntry;
                // cargo y ejecuto comando en contador de programa
                loadCommand(commandList.get(programCounter));
            } else if(programCounter <= programLength) {
                // estoy en modo repetir pero no en el inicio o final
                // estoy dentro del bucle, incremento contador de programa
                programCounter = programCounter + 1;
                // cargo y ejecuto comando en contador de programa
                loadCommand(commandList.get(programCounter));
            } else {
                // no me quedan más comandos: fin de programa
                stopProgram();
                checkEnding(); // a ver cómo ha terminado...
            }
        }

    }

    /**
     * Evalúa cómo ha terminado el programa
     * Escribe el resultado en la consola
     * Si es positivo avisa al observador
     */
    private void checkEnding() {
        if (targets.size() > 0) {
            // seamos positivos: se han cumplido los objetivos parciales
            boolean partialTargetsAccomplished = true;
            if (targets.size() > 1) {
                // tengo objetivos parciales
                if (partialTargetsReached == targets.size() - 1) {
                    // se han cumplido todos los objetivos parciales, menos el final
                } else {
                    // oh,no parece que no se han cumplido
                    partialTargetsAccomplished = false;
                }
            }

            // compruebo posición final del Robot
            Coordinates finalPosition = getCoordinates();
            Coordinates getFinalGridPosition = new Coordinates(finalPosition.getX() / stepSize, finalPosition.getY() / stepSize);
            if (onTarget(getFinalGridPosition) && partialTargetsAccomplished) {
                // bien! hemos acabado en la posición (coordenadas) indicadas
                writeToLogScreen(context.getString(R.string.mission_accomplished));
                // aviso al observador, aquí no hay más que hacer
                observer.missionAccomplished();
            } else {
                // oh,no esto no ha acabado sobre la posición de objetivo final
                // a ver si me retocan el programa...
                writeToLogScreen(context.getString(R.string.mission_not_accomplished));

                mp = MediaPlayer.create(context, R.raw.error);
                mp.start();
            }
        } else {
            // no hay objetivos... no hay misión... no hago nada
        }
    }

    /**
     * Mueve el Robot en ejes X,Y
     * El parámetro indica la dirección
     * @param commandParameter dirección en formato resource
     */
    @Override
    public void move(String commandParameter) {
        if (robotAction != null) { robotAction.cancel();}
        robotAction = null;
        String logMessage = "";
        if (commandParameter == context.getResources().getString(R.string.param_right)) {
            robotAction = stepRight();
            logMessage = context.getString(R.string.robot_step_right_txt);
            position.incrementX(stepSize);
            if (tellme) { position.printOut(); }
        } else if (commandParameter == context.getResources().getString(R.string.param_down)) {
            robotAction = stepDown();
            logMessage = context.getString(R.string.robot_step_down_txt);
            position.incrementY(stepSize);
            if (tellme) { position.printOut(); }
        } else if (commandParameter == context.getResources().getString(R.string.param_left)) {
            robotAction = stepLeft();
            position.incrementX(-stepSize);
            logMessage = context.getString(R.string.robot_step_left_txt);
            if (tellme) {position.printOut(); }
        } else if (commandParameter ==  context.getResources().getString(R.string.param_up)) {
            robotAction = stepUp();
            position.incrementY(-stepSize);
            logMessage = context.getString(R.string.robot_step_up_txt);
            if (tellme) { position.printOut(); }
        }
        // verificar que no se sale del tablero
        if (outOfBounds()) {
            if (tellme) { System.out.println("Out of bounds -> añado listener detener programa"); }
            logMessage = logMessage.concat("\n"+lineStart+context.getString(R.string.robot_outside_txt));
            badEnding = true;
            // añado listener que antes de dar el paso de salirme detendrá el programa en error
            robotAction.addListener(new GameOverListener());
        } else {
            // a ver si he llegado a algún objetivo parcial
            boolean onPartialTarget = detectTarget();
            if (onPartialTarget) {
                // si! lo comunico
                logMessage = logMessage.concat(("\n"+lineStart+context.getString(R.string.mission_partial_target)));
            }
            // añado listener que detectará el final del movimiento y avanzará el programa
            robotAction.addListener(new CommandListener());
        }
        writeToLogScreen(logMessage);
        robotAction.start();
    }

    /**
     * Ejecuta una acción espera en el Robot
     * Comprueba si va a ser una espera inútil y lo explica
     */
    @Override
    public void waitAction() {
        // ejecutar una espera
        ObjectAnimator waitAction = ObjectAnimator.ofFloat(robotView, View.ALPHA, 1, 1); // falsa animación larguísima alpha entre 1 y 1
        waitAction.setDuration(BASE_SPEED * 200);
        if (programCounter == programLength) {
            // espera es último comando: espera indefinida
            if (tellme) {
                System.out.println("ESPERA es el último comando del programa, espera indefinida");
            }
            writeToLogScreen(context.getString(R.string.robot_waiting_for_nothing));
            writeToLogScreen(context.getString(R.string.robot_waiting_dont_know));
            // añado listener que antes que esperar para siempre detendrá el programa en error
            waitAction.addListener(new GameOverListener());
            waitAction.start();
        } else {
            // ver si el siguiente comando es su sensor
            if (programCounter + 1 <= programLength) {
                String nextCommandName = commandList.get(programCounter + 1).getCommandName();
                if (tellme) { System.out.println("ESPERA: verificando siguiente comando " + nextCommandName); }
                if (nextCommandName.equals(CommandBlockName.NUMBERSENSOR.toString()) || nextCommandName.equals(CommandBlockName.COLORSENSOR.toString())) {
                        // espera con sensor
//                    writeToLogScreen(context.getString(R.string.robot_waiting_for_nothing));
                    // siguiente comando trae el evento que espero: avanzo programa
                    advanceProgram();
                } else {
                        // espera sin sensor = espera indefinida
                        if (tellme) { System.out.println("ESPERA sin sensor = espera indefinida"); }
                        writeToLogScreen(context.getString(R.string.robot_waiting_dont_know));
                        // añado listener que antes que esperar para siempre detendrá el programa en error
                        waitAction.addListener(new GameOverListener());
                        waitAction.start();
                }
            } else {
                // espera sin sensor = espera indefinida
                if (tellme) { System.out.println("ESPERA sin sensor = espera indefinida"); }
                writeToLogScreen(context.getString(R.string.robot_waiting_dont_know));
                // añado listener que antes que esperar para siempre detendrá el programa en error
                waitAction.addListener(new GameOverListener());
                waitAction.start();
            }
        }
    }

    /**
     * Verifica si el Robot ha terminado sobre el objetivo final
     * @param finalPosition coordenadas finales
     * @return boolean si o no
     */
    private boolean onTarget(Coordinates finalPosition) {
        if(finalTargetPosition != null) {
            if (finalPosition.getX() == finalTargetPosition.getX() && finalPosition.getY() == finalTargetPosition.getY()) {
                // bien! he acabado en la posición del objetivo final
                return true;
            } else {
                // oops! esto no ha acabado donde debería
                return false;
            }
        } else {
            // no tenía objetivo final, así que seguimos adelante
            return true;
        }

    }

    /**
     * Detiene las acciones del Robot
     */
    @Override
       public void stopAction() {
        if (robotAction!=null) {
            robotAction.cancel();
        }
            repeatMode = false;
            if (robotView.getAnimation() != null) {
                robotView.getAnimation().cancel();
            }
    }


    /**
     * Detiene la ejecución del programa (en el Robot)
     * Actualiza el botón de RunStopProgram
     */
    @Override
    public void stopProgram() {
        // cambio estado del botón Ejecutar a detenido
        runStopButton.setChecked(false);
        writeToLogScreen(context.getString(R.string.robot_endofprogram_txt));
    }

    /**
     * Programa terminado por comando ilegal
     * Muestro imagen de Robot muerto
     */
    @Override
    public void badEnding() {
        runStopButton.setChecked(false);
//        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
        writeToLogScreen(context.getString(R.string.robot_error_txt));
        writeToLogScreen(context.getString(R.string.robot_endofprogram_txt));
        mp = MediaPlayer.create(context, R.raw.crash);
        mp.start();
        robotView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.robot_dead));
    }

    /**
     * Cambia el robot de posición
     * Para suavizar el salto ejecuto un
     * Fade out + cambio posición X e Y + fade in
     * @param commandParameter coordenadas en formato string x,y
     */
    @Override
    public void changePosition(String commandParameter) {
        // cambio posición será: desvanecerse, cambiar de posición X e Y, reaparecer
        ObjectAnimator[] changes = new ObjectAnimator[4];
        // fade out
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(robotView, View.ALPHA, 1,0);
        fadeOut.setRepeatCount(0);
        fadeOut.setDuration(100);

        // mover a nueva posicón (dos animaciones, una por X y una por Y)
        String[] newPosition = commandParameter.split(",");
        int newX = (Integer.parseInt(newPosition[0])*stepSize)-stepSize;
        int newY = (Integer.parseInt(newPosition[1])*stepSize)-stepSize;
        ObjectAnimator changePositionX = ObjectAnimator.ofFloat(robotView, "translationX", newX);
        ObjectAnimator changePositionY = ObjectAnimator.ofFloat(robotView, "translationY", newY);
        changePositionX.setRepeatCount(0);
        changePositionX.setDuration(100);
        changePositionY.setRepeatCount(0);
        changePositionY.setDuration(100);

        // update robot position
        position.set(newX, newY);
        position.printOut();

        // fade back
        ObjectAnimator fadeBack = ObjectAnimator.ofFloat(robotView, View.ALPHA, 0,1);
        fadeBack.setRepeatCount(0);
        fadeBack.setDuration(100);

        String logMessage = context.getString(R.string.robot_position_txt)+" ("+newPosition[0]+","+newPosition[1]+")";
        //  miro a ver si he llegado a un objetivo parcial
        boolean onPartialTarget = detectTarget();
        if (onPartialTarget) {
            // siii! lo comunico
            logMessage = logMessage.concat(("\n"+lineStart+context.getString(R.string.mission_partial_target)));
        }
        // añado listener que detectará el final de la última animación y avanzará el programa
        fadeBack.addListener(new CommandListener());

        // compilo la lista de las cuatro animaciones: desaparezco, muevo x, muevo y, reaparezco
        ArrayList<Animator> positionChangeAction = new ArrayList<Animator>();
        positionChangeAction.add(fadeOut);
        positionChangeAction.add(changePositionX);
        positionChangeAction.add(changePositionY);
        positionChangeAction.add(fadeBack);
        AnimatorSet thisAction = new AnimatorSet();
        // ejecutaremos estas cuatro acciones secuencialmente
        thisAction.playSequentially(positionChangeAction);
        writeToLogScreen(logMessage);
        thisAction.start();
    }

    /**
     * Cambia la velocidad del Robot
     * @param speed int nuevo índice de velocidad
     * la velocidad real se calcula dividiendo la velocidad
     * base entre este índice. Como si fueran marchas.
     */
    @Override
    public void setSpeed(int speed) {
        this.speed = BASE_SPEED/speed;
    }

    /**
     * Detecta si el Robot ha llegado a un objetivo parcial
     * y lo anota
     * @return boolean verdadero o false
     */
    private boolean detectTarget() {
        if (targets.size() > 1) {
            // tengo más de un objetivo: objetivos parciales +  final
//            if (tellme) { System.out.println("Detectando objetivos parciales"); }
            Coordinates currentPosition = new Coordinates(position.getX()/stepSize, position.getY()/stepSize);
            if (tellme) { currentPosition.printOut(); }
            for(int i=0; i<targets.size()-1; i++) {
                Coordinates partialTargetCoords = targets.get(i).getCoordinates();
//                if (tellme) { partialTargetCoords.printOut(); }
                if (partialTargetCoords.getX() == currentPosition.getX() && partialTargetCoords.getY() == currentPosition.getY()) {
                    if (tellme) { System.out.println("Encontrado objetivo parcial en indice " + i); }
                    // bien! objetivo parcial detectado, lo anoto
                    partialTargetsReached++;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Verifica que el movimiento no acabe fuera
     * de los límites del escenario
     */
    private boolean outOfBounds() {
        // calculo límites del escenario
        int rightLimit = stepSize * (Stage.COLS);
        int bottomLimit = stepSize * (Stage.ROWS);
        int leftLimit = 0;
        int topLimit = 0;
        if (tellme) {
//            System.out.println("Out of bounds check!");
//            System.out.println("Limites: dcho " + rightLimit + " abajo " + bottomLimit
//                    + " izq " + leftLimit + " arriba " + topLimit);
//            position.printOut();
        }

        if (position.getX() >= rightLimit || position.getX() < leftLimit) {
            // fuera de los límites en el plano horizontal
            if (tellme) { System.out.println("Fuera de límites horizontal!"); }
            return true;
        } else if (position.getY() >= bottomLimit || position.getY() < topLimit) {
            // fuera de los límites en el plano vertical
            if (tellme) { System.out.println("Fuera de límites vertical!"); }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Paso a la derecha
     * @return ObjectAnimator animación que lo ejectua
     */
    private ObjectAnimator stepRight() {
        float destino = position.getX()+stepSize;
        if (tellme) { System.out.println("Robot paso a la derecha a "+destino); }
        ObjectAnimator stepRight = ObjectAnimator.ofFloat(robotView, "translationX", destino);
        stepRight.setInterpolator(new DecelerateInterpolator());
        stepRight.setRepeatCount(0);
        stepRight.setDuration(this.getSpeed());
        return stepRight;
    }

    /**
     * Paso hacia abajo
     * @return ObjectAnimator animación que lo ejecuta
     */
    private ObjectAnimator stepDown() {
        float destino = position.getY()+stepSize;
        if (tellme) { System.out.println("Robot paso abajo a "+destino); }
        ObjectAnimator stepDown = ObjectAnimator.ofFloat(robotView, "translationY", destino);
        stepDown.setInterpolator(new DecelerateInterpolator());
        stepDown.setRepeatCount(0);
        stepDown.setDuration(this.getSpeed());
        return stepDown;
    }

    /**
     * Paso a la izquierda
     * @return ObjectAnimator animación que lo ejectua
     */
    private ObjectAnimator stepLeft() {
        float destino = position.getX()-stepSize;
        if (tellme) { System.out.println("Robot paso izquierda a "+destino); }
        ObjectAnimator stepLeft = ObjectAnimator.ofFloat(robotView, "translationX", destino);
        stepLeft.setInterpolator(new DecelerateInterpolator());
        stepLeft.setRepeatCount(0);
        stepLeft.setDuration(this.getSpeed());
        return stepLeft;
    }

    /**
     * Paso hacia arriba
     * @return ObjectAnimator animación que lo ejectua
     */
    private ObjectAnimator stepUp() {
        float destino = position.getY()-stepSize;
        if (tellme) { System.out.println("Robot paso arriba a "+destino); }
        ObjectAnimator stepUp = ObjectAnimator.ofFloat(robotView, "translationY", destino);
        stepUp.setInterpolator(new DecelerateInterpolator());
        stepUp.setRepeatCount(0);
        stepUp.setDuration(this.getSpeed());
        return stepUp;
    }

    /**
     * Devuelve la velocidad del Robot
     * @return int velocidad
     */
    @Override
    public int getSpeed() {
        return this.speed;
    }

    /**
     * Carga los objetivos: final y parciales
     * @param targets ArrayList<MissionTarget> lista de objetivos
     */
    @Override
    public void setTargets(ArrayList<MissionTarget> targets) {
        // guardo la lista de objetivos
        this.targets = targets;
        if (targets.size() == 1) {
            // solo traigo un objetivo: el final
            finalTargetPosition = targets.get(0).getCoordinates();
        }else if (targets.size() == 0) {
            // no hay objetivos
            finalTargetPosition = null;
        } else {
            // traigo más de un objetivo
            // el objetivo final es el último de la lista
            finalTargetPosition = targets.get(targets.size()-1).getCoordinates();
        }
    }


    /**
     * Añade el observador
     * @param mainActivity Activity o ventana que contiene al robot y observa
     */
    public void setObserver(MainActivity mainActivity) {
        this.observer = mainActivity;
    }

    /**
     * Añade el Robot al escenario
     * @param gridFrame layout que lo contendrá (su padre)
     */
    @Override
    public void addToStage(FrameLayout gridFrame) {
        gridFrame.addView(robotView);
    }

    /**
     * Borra al Robot del escenario
     * @param gridFrame layout que lo contiene (su padre)
     */
    @Override
    public void removeFromStage(FrameLayout gridFrame) {
        gridFrame.removeView(robotView);
    }


    /**
     * Detecta un cambio en el sensor de número
     * Cambia el número en el sensor interfaz
     */
    private class NumberChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        /**
         * Detecta un cambio en el texto del sensor
         * @param charSequence nuevo número
         */
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (tellme) { System.out.println("Esperando número: " + numberTarget); }
            lastNumberPressed = charSequence.toString();
            if (tellme) { System.out.println("Detectado número: " + lastNumberPressed); }
            if (numberSensorActivated) {
                if (!repeatMode) {
                    // si no estoy en modo bucle avanzo el programa
                    advanceProgram();
                    // los bucles verifican el cambio de número al final de su ciclo
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }


    /**
     * Detiene el programa ANTES de iniciarse un comando erróneo
     */
    private class GameOverListener implements Animator.AnimatorListener {
        boolean tellme = false;

            @Override
            public void onAnimationStart(Animator animator) {
                stopAction();
                badEnding();
                if (tellme) { System.out.println("Game over listener detectado"); }
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }

    /**
     * Detecta el fin de una acción del Robot
     * Actualiza el sensor de color y avanza el programa
     */
    private class CommandListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }


        /**
         * Detecta el color de suelo sobre el que está el robot
         * Actualiza el color del sensor del robot
         * @return String del color detectado
         */
        private String refreshColorSensor() {
            Coordinates posicionActual = getCoordinates();
            int robotTileColor = stage.getColor(posicionActual.getX()/stepSize, posicionActual.getY()/stepSize);
            colorSensor.setBackgroundColor(robotTileColor);
            String colorDetected = "#"+Integer.toHexString(robotTileColor);
            colorSensor.setTag(colorDetected);
            return colorDetected;
        }

        /**
         * Acciones que se realzan al terminar una acción de Robot:
         * refresco sensor de color
         * actualizo el contador del programa si es necesario (bucles, sensores)
         * avanzo o termino el programa
         * @param animation la animación que vigila
         */
        @Override
        public void onAnimationEnd(Animator animation) {
            // TODO revisar, creo que se puede mejorar / simplificar
            if (tellme) {
                System.out.println("Comando ejecutado: avanzo programa");
                System.out.println("Fin de paso -> actualizo sensor color");
            }
            String colorDetected = refreshColorSensor();
            if (colorSensorActivated) {
                // sensor color activado
//                System.out.println("Sensor color activado");
                if (colorDetected.equals(colorTarget)) {
                    // encontrado color que espera un comando
                    writeToLogScreen(context.getString(R.string.robot_color_detected));
                    if (repeatMode) {
                        // si este color me libera de un bucle
                        repeatMode = false;
                        // contador de programa a final del bucle
                        programCounter = repeatEscape;
//                        System.out.println("Fin de bucle: PC a " + programCounter);
                        repeatEntry = 0;
                        repeatEscape = 0;
                        writeToLogScreen(context.getString(R.string.robot_repeat_released));
                    }
                    colorSensorActivated = false;
                }
            } else if(numberSensorActivated) {
                // sensor número activado
//                System.out.println("Sensor numero activado");
                if (lastNumberPressed.equals(numberTarget)) {
                    // se ha pulsado el número que espera un comando
                    writeToLogScreen(context.getString(R.string.robot_number_detected_txt));
                    if (repeatMode) {
                        // si este número me libera de un bucle
                        repeatMode = false;
                        // contador de programa a final del bucle
                        programCounter = repeatEscape;
//                        System.out.println("Fin de bucle: PC a " + programCounter);
                        repeatEntry = 0;
                        repeatEscape = 0;
                        writeToLogScreen(context.getString(R.string.robot_repeat_released));
                    }
                    numberSensorActivated = false;
                }
            }
            advanceProgram();
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }
        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
