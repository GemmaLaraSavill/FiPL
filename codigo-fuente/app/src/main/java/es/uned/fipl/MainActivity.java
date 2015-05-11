package es.uned.fipl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import es.uned.fipl.block.CommandBlock;
import es.uned.fipl.block.CommandBlockFactory;
import es.uned.fipl.block.CommandBlockName;
import es.uned.fipl.block.CommandBlockPosition;
import es.uned.fipl.block.listeners.CommandBlockTouchListener;
import es.uned.fipl.mision.Mission;
import es.uned.fipl.mision.MissionFactory;
import es.uned.fipl.mision.RobotMission;
import es.uned.fipl.mision.RobotMissionFactory;
import es.uned.fipl.mision.evaluators.MissionEvaluator;
import es.uned.fipl.program.BlockProgram;
import es.uned.fipl.program.Program;
import es.uned.fipl.stage.MissionTarget;
import es.uned.fipl.stage.NumberButtonAdapter;
import es.uned.fipl.stage.ProgrammableObject;
import es.uned.fipl.stage.ProgrammingCanvas;
import es.uned.fipl.stage.Robot;
import es.uned.fipl.stage.Rotate3dAnimation;
import es.uned.fipl.stage.TileAdapter;
import es.uned.fipl.stage.listeners.WasteBinDragListener;

/**
 * FiPL "My First Programming Language" por Gemma Lara Savill
 * Proyecto Fin de Grado 2015
 * Grado en Ingeniería en Tecnologías de la Información UNED
 *
 * Esta es la ventana principal del programa
 * Se carga la interfaz de usuario
 * Se evalúa el nivel guardado del usuario
 * Se le solicita una Mission del nivel asignado
 * Se configura la interfaz de usuario según los parámetros de la Mission
 * Se espera el aviso del Robot: llegado a los objetivos
 * Se le pide veredicto al MissionEvaluator para pasar o no a la siguiente Mission
 */
public class MainActivity extends Activity {

    // UI de izq a dcha
    private FrameLayout gridFrame; // área donde se mueve el Robot
    private GridView runArea; // grid de cuadrados del Robot
    private LinearLayout colorSensorPanel; // panel de sensor de color
    private ImageView colorSensor; // sensor de color
    private LinearLayout numberSensorPanel; // panel de sensor de número
    private TextView numberSensor; // sensor de número
    private GridView numberKeyPad; // grid con botones de número
    private ViewGroup centerColumn; // columna central: paleta bloques + consola log Robot
    private LinearLayout commandBlockPalette; // paleta de bloques
    private ScrollView logConsole; // log del Robot con scroll
    private TextView logConsoleScreen; // texto del log
    private RelativeLayout programmingCanvas; // lienzo de programación, adonde puedes arrastrar bloques
    private ToggleButton runStopButton; // botón ejecutar programa
    private ImageView wastepaperbasket; // papelera
    // fin UI

    // elementos
    private int missionLevel; // nivel del usuario
    private Mission mission; // misión cargada
    private CommandBlockFactory blockFactory; // fábrica de bloques
    private BlockProgram blockProgram; // programa de bloques
    private Robot robot;
    private Context context; // contexto de la ventana Android
    private Dialog missionDialog; // informa al usuario sobre la misión

    private boolean logScreenVisible = false;
    private static final int screenFlipSpeed = 250;
    private MediaPlayer mp;

    /**
     * Crea la ventanta
     * Accede al nivel de misión del usuario
     * Carga la misión
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        setupInterface();
        missionLevel = getStoredMissionLevel();
        loadMission(missionLevel);
    }

    /**
     * Accede a los elementos de la interfaz de usuario
     * para poder usarlos durante el programa
     */
    public void setupInterface() {
        // cargar elementos de la interfaz
        // columna izquierda
        gridFrame = (FrameLayout)findViewById(R.id.gridFrame);
        runArea = (GridView)findViewById(R.id.runArea);
        colorSensorPanel = (LinearLayout)findViewById(R.id.colorSensorPanel);
        colorSensor = (ImageView) findViewById(R.id.colorSensor);
        numberSensorPanel = (LinearLayout)findViewById(R.id.numberSensorPanel);
        numberSensor = (TextView) findViewById(R.id.numberSensor);
        numberKeyPad = (GridView) findViewById(R.id.numberKeyPad);

        // columna central
        centerColumn = (ViewGroup) findViewById(R.id.centerColumn);
        commandBlockPalette = (LinearLayout)findViewById(R.id.commandPalette);
        logConsole = (ScrollView)findViewById(R.id.logConsole);
        logConsoleScreen = (TextView)findViewById(R.id.logScreen);

        // column derecha
        programmingCanvas = (RelativeLayout)findViewById(R.id.programCanvas);
        wastepaperbasket = (ImageView)findViewById(R.id.wastepaperbasket);
        runStopButton = (ToggleButton)findViewById(R.id.runButton);
        // elementos de la interfaz cargados

        // añado Toast por si pulsan en la papelera
        wastepaperbasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, context.getString(R.string.wastepaperbasket_hint), Toast.LENGTH_LONG).show();
            }
        });
        // inicio los sensores apagados (invisibles) no todas las misiones los requiere
        activateColorSensor(false);
        activateNumberSensor(false);
        // cada misión activará los sensores que necesite
    }

    /**
     * Cargo una misión de un nivel determinado
     * @param missionLevel nivel de la misión
     */
    private void loadMission(final int missionLevel) {
        unloadPreviousMission();
        // setup mision
        MissionFactory misionFactory = new RobotMissionFactory(this);
        mission = misionFactory.createMission(missionLevel);
        ArrayList<CommandBlockName> blockList = mission.getBlockList();
        // inicializo el programa de bloques
        blockProgram = new BlockProgram();
        // inicializo la papelera
        wastepaperbasket.setOnDragListener(new WasteBinDragListener(blockProgram));
        // cargo los bloques que se podrán usar en esta misión
        setUpBlockPalette(blockList);
        // inicio el lienzo de programación
        setUpProgrammingCanvas();
        // configuro el escenario para esta misión
        runArea.setAdapter(new TileAdapter(this, mission.getStage().getColorTiles()));
        // colocamos los objetivos, pueden ser más de uno!
        ArrayList<MissionTarget> targets = mission.getTargets();
        for (int i=0; i<targets.size(); i++) {
            MissionTarget target = targets.get(i);
            target.addTarget(this, gridFrame);

        }
        // pasamos datos al crear el Robot
        robot = new Robot(gridFrame, runStopButton, numberSensor, colorSensor, mission.getStage(), logConsoleScreen);
        robot.addToStage(gridFrame);
        robot.setStartPosition();
        // pasamos objetivos al Robot
        robot.setTargets(targets);
        robot.setObserver(this);
        // configuro botón de run / stop program
        runStopButton.setOnCheckedChangeListener(new RunButtonOnClickListener(blockProgram, robot));
        // informo al usuario de la misión
        missionDialog = createDialog(getString(mission.getTitle()), getString(mission.getDescription()));
        missionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (missionLevel == 1) {
                    Toast.makeText(context, context.getString(R.string.mission1_start_hint), Toast.LENGTH_LONG).show();
                }
            }
        });
        missionDialog.show();
    }

    /**
     * Robot avisa que ha llegado al objetivo final
     * Pasando por los objetivos parciales (si los hubiera)
     * Hay que evaluar el programa de bloques
     */
    public void missionAccomplished() {
        // cargo el evaluador de esta misión
        MissionEvaluator evaluator = mission.getEvaluator();
        if (evaluator != null) {
            System.out.println("Paso a evaluador programa de " + blockProgram.getSize());
            String evaluation = evaluator.evaluate(blockProgram);
            System.out.println("Resultado evaluación: " + evaluation);

            // misión perfecta
            if (evaluation.equals(MissionEvaluator.PERFECT)) {
                // informo al usuario
                Dialog missionSuccess = createDialog(getString(R.string.mission_accomplished), getString(R.string.mission_success));
                missionSuccess.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        loadMission(missionLevel);
                    }
                });
                mp = MediaPlayer.create(context, R.raw.tada);
                mp.start();
                missionSuccess.show();
                // paso a siguiente misión
                changeMissionLevel();
            // misión no está mal: objetivos cumplidos, programación con bloques mejorable
            } else if (evaluation.equals(MissionEvaluator.OK)) {
                // informo al usuario
                Dialog missionAverage = createTwoOptionsDialog(getString(R.string.mission_average), getString(R.string.mission_average_text));
//                missionAverage.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                        changeMissionLevel();
//                        loadMission(missionLevel);
//                    }
//                });
                missionAverage.show();
                // el usuario decidirá si probar de nuevo o pasar a la siguiente misión

            // misión no está bien: no se ha cumplido algún requisito
            } else if (evaluation.equals(MissionEvaluator.NOTOK)) {
                // informo al usuario
                Dialog missionFailiure = createDialog(getString(R.string.mission_not_accomplished), getString(mission.getFailedExplanation()));
                missionFailiure.show();
            }
        } else {
            // no hay evaluador en esta misión... no hago nada
        }
    }

    /**
     * Actualizo el nivel de misión del usuario
     */
    private void changeMissionLevel() {
        // si me quedan misiones paso a la siguiente
        if (missionLevel < 4) {
            missionLevel = missionLevel + 1;
        } else {
            // cargar interfaz por defecto con todos los bloques
            missionLevel = 0;
        }
        // guardo nivel en memoria dispositivo
        setStoredMissionLevel(missionLevel);
    }

    /**
     * Prepara el escenario para cargar la siguiente misión
     * Quita los objetivos de la anterior misión
     * Quita el anterior objeto programado (robot)
     */
    private void unloadPreviousMission() {
        // refresco escenario
        if (mission!=null) {
            ArrayList<MissionTarget> targets = mission.getTargets();
            for (int i = 0; i < targets.size(); i++) {
                MissionTarget target = targets.get(i);
                target.removeTarget(gridFrame);
            }
        }
        if (robot != null) {
            robot.removeFromStage(gridFrame);
        }
        if(logScreenVisible) {
            toggleLogScreen();
        }
    }

    /**
     * Se encarga de crear y cargar los tipos de bloque que se pueden
     * usar en esta misión.
     * Activa / Desactiva los paneles de los sensores según se necesiten
     * Si no se usa el bloque NumberSensor se desactiva el sensor de número y panel de botones de números
     * Si no se usa el bloque ColorSensor se desactiva el detector de color
     * @param blockList
     */
    private void setUpBlockPalette(ArrayList<CommandBlockName> blockList) {
        boolean colorSensorActive = false;
        boolean numberSensorActive = false;
        commandBlockPalette.removeAllViews();
        TextView consoleText = new TextView(this);
        consoleText.setText(context.getString(R.string.commandPaletteTitle));
        consoleText.setPadding(5,5,5,5);
        commandBlockPalette.addView(consoleText);
        // vamos a crear los bloques para esta misión
        blockFactory = new CommandBlockFactory();
        CommandBlock block = null;
        // creo y añado bloques a la paleta
        if (blockList.size() > 0) {
            for (int i = 0; i < blockList.size(); i++) {
                // le pido al BlockFactory un bloque de este tipo
                block = blockFactory.createBlock(blockList.get(i), this);
                if (block.isSensor()) {
                    if (block.getType() == CommandBlockName.COLORSENSOR) {
                        colorSensorActive = true;
                    } else {
                        numberSensorActive = true;
                    }
                }
                // añado touch listener a todos menos a Start
                if(!blockList.get(i).equals(CommandBlockName.START)) {
                    // no quiero que se desplieguen los selectores al moverlos, los desactivo
                    block.setParamSelectorsActive(false);
                    commandBlockPalette.addView(block);
                    block.setOnTouchListener(new CommandBlockTouchListener());
                }
            }
        }
        // activo los sensores que necesito según el tipo de bloques cargados
        activateColorSensor(colorSensorActive);
        activateNumberSensor(numberSensorActive);
    }

    /**
     * Cambio la visibilidad del sensor de número
     * Incluye el grid de botones numéricos
     * @param activate true or false
     */
    private void activateNumberSensor(boolean activate) {
        if (activate) {
            numberKeyPad.setVisibility(View.VISIBLE);
            numberSensorPanel.setVisibility(View.VISIBLE);
            numberKeyPad.setAdapter(new NumberButtonAdapter(this, numberSensor));
        } else {
            numberSensorPanel.setVisibility(View.INVISIBLE);
            numberKeyPad.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Cambio la visibilidad del sensor de color
     * El sensor de color tendrá el color de la baldosa donde está el robot
     * @param activate true or false
     */
    private void activateColorSensor(boolean activate) {
        if (activate) {
            colorSensorPanel.setVisibility(View.VISIBLE);
            int robotTileColor = mission.getStage().getColor(0, 0);
            colorSensor.setBackgroundColor(robotTileColor);
            String tag = "#" + Integer.toHexString(robotTileColor);
            colorSensor.setTag(tag);
        } else {
            colorSensorPanel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Inicio el lienzo de programación
     * Coloco bloque INICIO y llamo al draw de ProgrammingCanvas
     * Éste añadirá los conectores necesarios para otros bloques
     */
    private void setUpProgrammingCanvas() {
        // iniciamos el programa con un bloque de inicio
        CommandBlock block = blockFactory.createBlock(CommandBlockName.START, this);
        blockProgram.addBlock(block, CommandBlockPosition.FIRST, R.id.programmingCanvasText);
        // refresco el lienzo de programación
        ProgrammingCanvas.drawBlockProgram(programmingCanvas, blockProgram);
    }

    /**
     * Devuelve el nivel de misión de este usuario
     * @return int nivel
     */
    private int getStoredMissionLevel() {
        int newMissionLevel = 1;
        // busca en la memoria del dispositivo cual es el nivel de la última misión en este dispositivo
        SharedPreferences prefs = getSharedPreferences(RobotMission.MY_MISSION_LEVEL, MODE_PRIVATE);
        newMissionLevel = prefs.getInt("missionLevel", 1); // 1 por defecto, por si usuario nuevo
        return newMissionLevel;
    }

    /**
     * Guarda el nivel de misión de este usuario
     * en la memoria del dispositivo
     */
    private void setStoredMissionLevel(int newMissionLevel) {
        SharedPreferences.Editor editor = getSharedPreferences(RobotMission.MY_MISSION_LEVEL, MODE_PRIVATE).edit();
        editor.putInt("missionLevel", newMissionLevel);
        editor.commit();
    }

    /**
     * Acción que se ejecuta cuando se retorna desde otra pantalla
     * @param requestCode usando 2 para volver de pantalla misiones
     * @param resultCode si OK o no
     * @param data datos que traigo (como un POST)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // si vuelvo OK de la pantalla misiones
        if (resultCode == RESULT_OK && requestCode == 2) {
            if (data.hasExtra("mission")) {
                // si traigo una misión que quiero cargar
                int missionLevelToLoad = data.getExtras().getInt("mission");
                missionLevel = missionLevelToLoad;
                // guardo el nuevo nivel en memoria dispositivo
                setStoredMissionLevel(missionLevel);
                // cargo la misión seleccionada
                loadMission(missionLevel);
            }
        }
    }

    /**
     * Creo el menú de la ventana
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Cambio en el menú del Action Bar
     * Cuando giro columna central, cambia el menú
     * @param menu el menú que estamos cambiando
     * @return siempre true
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem showHideLogButton = menu.findItem(R.id.action_logScreen);
        if (logScreenVisible) {
            showHideLogButton.setTitle(getResources().getString(R.string.action_blockPalette));
            showHideLogButton.setIcon(R.drawable.ic_back_to_blocks);
        } else {
            showHideLogButton.setTitle(getResources().getString(R.string.action_logScreen));
            showHideLogButton.setIcon(R.drawable.ic_robot_log);
        }
        return true;
    }

    /**
     * Acciones a realizar cuando el usuario activa una opción del menú
     * @param item opción de menú que ha sido seleccionada
     * @return siempre true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logScreen:
                toggleLogScreen();
                return true;
            case R.id.action_missionDialog:
                missionDialog.show();
                return true;
            case R.id.action_helpScreen:
                Intent helpScreenIntent = new Intent(this, HelpActivity.class);
                startActivityForResult(helpScreenIntent, 1);
                return true;
            case R.id.action_missionScreen:
                Intent missionScreenIntent = new Intent(this, MissionsActivity.class);
                startActivityForResult(missionScreenIntent, 2);
                return true;
            case R.id.action_closeApp:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Muestra la consola log del Robot
     */
    public void toggleLogScreen() {
        applyRotation(0, 90);
        if (logScreenVisible) {
            logScreenVisible = false;
        } else {
            logScreenVisible = true;
        }
        invalidateOptionsMenu();
    }


    /**
     * Crea un Dialogo de un sólo botón
     * @param title titulo que queremos mostrar
     * @param text texto del mensaje
     * @return el Dialog creado
     */
    public Dialog createDialog(String title, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(text);
        // añado el botón
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    /**
     * Dialogo con dos opciones: cancelar o volver a intentarlo
     * @param title titulo que queremos mostrar
     * @param text texto del mensaje
     * @return el Dialog creado
     */
    public Dialog createTwoOptionsDialog(String title, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(text);
        // añado botones
        builder.setNegativeButton(context.getText(R.string.no_thanks), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
//                dialog.dismiss();
                changeMissionLevel();
                loadMission(missionLevel);
            }
        });
        builder.setPositiveButton(context.getText(R.string.try_again), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // probar de nuevo
                loadMission(missionLevel);
            }
        });
        return builder.create();
    }


    /**
     * Crea una nueva rotación 3D
     * @param start ángulo de inicio
     * @param end ángulo final
     */
    private void applyRotation(float start, float end) {
        // centro del contenedor
        final float centerX = centerColumn.getWidth() / 2.0f;
        final float centerY = centerColumn.getHeight() / 2.0f;

        // Creo la animación, añado listener para detectar el final
        final Rotate3dAnimation rotation =
                new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        rotation.setDuration(screenFlipSpeed);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView());

        centerColumn.startAnimation(rotation);
    }


    /**
     * Detecta la primera mitad de la animación
     * Cuando se ha rotado 90 grados inicia la otra mitad de la animaciónible.
     */
    private final class DisplayNextView implements Animation.AnimationListener {

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
           centerColumn.post(new SwapViews());
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    /**
     * Intercambia los views: log y paleta
     * Inicia la segunda mitad de la animación
     */
    private final class SwapViews implements Runnable {


        public void run() {
            final float centerX = centerColumn.getWidth() / 2.0f;
            final float centerY = centerColumn.getHeight() / 2.0f;
            Rotate3dAnimation rotation;

            if (logScreenVisible) {
                // come in log screen
                commandBlockPalette.setVisibility(View.GONE);
                logConsole.setVisibility(View.VISIBLE);
                logConsole.requestFocus();
                logScreenVisible = true;
                rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 310.0f, false);
            } else {
                // come in command palette
                logConsole.setVisibility(View.GONE);
                commandBlockPalette.setVisibility(View.VISIBLE);
                commandBlockPalette.requestFocus();
                logScreenVisible = false;
                rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 310.0f, false);
            }

            rotation.setDuration(screenFlipSpeed);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());

            centerColumn.startAnimation(rotation);
        }
    }

    /**
     * Detecta la pulsación en el botón de Ejecutar / Detener programa
     */
    private class RunButtonOnClickListener implements CompoundButton.OnCheckedChangeListener {

        private final BlockProgram blockProgram;
        private final int waitBeforeConsoleFlip = 5000; // seconds

        private ProgrammableObject robot;

        public RunButtonOnClickListener(BlockProgram blockProgram, ProgrammableObject robot) {
            this.blockProgram = blockProgram;
            this.robot = robot;
        }

        @Override
        public void onCheckedChanged(CompoundButton runButton, boolean checked) {
            if (checked) {
                // ejecutar
                // leer el programa
                Program program = new Program(blockProgram);
                // pasarlo al robot
                robot.runProgram(program);
                if (!logScreenVisible) {
                    toggleLogScreen();
                }
            } else {
                // parar programa
                robot.stopAction();
                // esperar y quitar consola log robot
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (logScreenVisible) {
                            toggleLogScreen();
                        }
                    }
                }, waitBeforeConsoleFlip);

            }

        }
    }
}

