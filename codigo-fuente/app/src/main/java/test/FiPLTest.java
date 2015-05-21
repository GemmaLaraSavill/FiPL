package test;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import java.util.ArrayList;

import es.uned.fipl.block.CommandBlock;
import es.uned.fipl.block.CommandBlockFactory;
import es.uned.fipl.block.CommandBlockName;
import es.uned.fipl.block.CommandBlockPosition;
import es.uned.fipl.mision.Mission;
import es.uned.fipl.mision.MissionFactory;
import es.uned.fipl.mision.RobotMissionFactory;
import es.uned.fipl.mision.evaluators.MissionEvaluator;
import es.uned.fipl.program.BlockProgram;
import es.uned.fipl.program.Program;

/**
 * Class que realiza pruebas sobre diferentes áreas de la aplicación FiPL
 */
public class FiPLTest extends ApplicationTestCase<Application> {
    private Application mApplication;
    private Context context;

    public FiPLTest() {
        super(Application.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        mApplication = getApplication();
        context = mApplication.getApplicationContext();
    }

    /**
     * Prueba la creación de misiones en su Factory
     * La misión crea lista bloques correctos
     * @throws Exception
     */
    public void testGetMissionBlockList() throws Exception {
        int testMissionLevel = 1;

            // tipo de bloques que debe tener la misión uno
            ArrayList<CommandBlockName> expectedBlockList = new ArrayList<>();
            expectedBlockList.add(CommandBlockName.START);
            expectedBlockList.add(CommandBlockName.MOVE);

            MissionFactory missionFactory = new RobotMissionFactory(context);
            Mission mission = missionFactory.createMission(testMissionLevel);
            // tipo de bloques creados
            ArrayList<CommandBlockName> realBlockList =  mission.getBlockList();
            assertEquals(expectedBlockList, realBlockList);
    }

    /**
     * Prueba la creación de CommandBlocks en el Factory
     * Se crea el bloque del tipo solicitado
     * @throws Exception
     */
    public void testCreateCommandBlock() throws Exception {
        CommandBlockName expectedType = CommandBlockName.SPEED;
        CommandBlockFactory commandBlockFactory = new CommandBlockFactory();
        CommandBlock realBlock = commandBlockFactory.createBlock(CommandBlockName.SPEED, context);
        CommandBlockName realType = realBlock.getType();
        assertEquals(expectedType, realType);
    }

    /**
     * Prueba que el evaluador de la misión tres coteja la solución
     * de manera correcta
     * @throws Exception
     */
    public void testMissionEvaluatorWithCorrectProgram() throws Exception {

        CommandBlockFactory commandBlockFactory = new CommandBlockFactory();

        // creo un Start block
        CommandBlock startBlock = commandBlockFactory.createBlock(CommandBlockName.START, context);
        // creo un Wait block
        CommandBlock waitBlock = commandBlockFactory.createBlock(CommandBlockName.WAIT, context);
        // creo un NumberSensor block
        CommandBlock numberSensorBlock = commandBlockFactory.createBlock(CommandBlockName.NUMBERSENSOR, context);
        numberSensorBlock.setParameter(4); // espero número 5
        // creo un Move block
        CommandBlock moveBlock1 = commandBlockFactory.createBlock(CommandBlockName.MOVE, context);
        moveBlock1.setParameter(0); // derecha
        // creo un Move block
        CommandBlock moveBlock2 = commandBlockFactory.createBlock(CommandBlockName.MOVE, context);
        moveBlock2.setParameter(0); // derecha
        // creo un Move block
        CommandBlock moveBlock3 = commandBlockFactory.createBlock(CommandBlockName.MOVE, context);
        moveBlock3.setParameter(1); // abajo

        // creo un programa de bloques que corresponde a la solución de la misión tres
        BlockProgram aSolution = new BlockProgram();
        aSolution.addBlock(startBlock, CommandBlockPosition.BELOW, 1);
        aSolution.addBlock(waitBlock, CommandBlockPosition.BELOW, 2);
        aSolution.addBlock(numberSensorBlock, CommandBlockPosition.RIGHT, 3);
        aSolution.addBlock(moveBlock1, CommandBlockPosition.BELOW, 4);
        aSolution.addBlock(moveBlock2, CommandBlockPosition.BELOW, 5);
        aSolution.addBlock(moveBlock3, CommandBlockPosition.BELOW, 6);

        // Creo la misión tres y accedo a su evaluador
        MissionFactory missionFactory = new RobotMissionFactory(context);
        Mission missionThree = missionFactory.createMission(3);
        MissionEvaluator evaluator = missionThree.getEvaluator();

        Program proposedSolution = new Program(aSolution);
//        aSolution.printOut();

        String realResult = evaluator.evaluate(aSolution);
        String expectedResult = MissionEvaluator.PERFECT; // la misión debería ser válida
        assertEquals(expectedResult, realResult);
    }

    /**
     * Prueba que el evaluador de la misión tres coteja la solución
     * de manera correcta
     * @throws Exception
     */
    public void testMissionEvaluatorWrongProgram() throws Exception {

        CommandBlockFactory commandBlockFactory = new CommandBlockFactory();

        // creo un Start block
        CommandBlock startBlock = commandBlockFactory.createBlock(CommandBlockName.START, context);
        // creo un Wait block
        CommandBlock waitBlock = commandBlockFactory.createBlock(CommandBlockName.WAIT, context);
        // creo un NumberSensor block
        CommandBlock numberSensorBlock = commandBlockFactory.createBlock(CommandBlockName.NUMBERSENSOR, context);
        numberSensorBlock.setParameter(2); // espero número 5 -> voy a seleccionar el 3 ERROR provocado para test
        // creo un Move block
        CommandBlock moveBlock1 = commandBlockFactory.createBlock(CommandBlockName.MOVE, context);
        moveBlock1.setParameter(0); // derecha
        // creo un Move block
        CommandBlock moveBlock2 = commandBlockFactory.createBlock(CommandBlockName.MOVE, context);
        moveBlock2.setParameter(0); // derecha
        // creo un Move block
        CommandBlock moveBlock3 = commandBlockFactory.createBlock(CommandBlockName.MOVE, context);
        moveBlock3.setParameter(1); // abajo

        // creo un programa de bloques que NO corresponde a la solución de la misión tres
        // parámetro erróneo en el sensor de número
        BlockProgram aSolution = new BlockProgram();
        aSolution.addBlock(startBlock, CommandBlockPosition.BELOW, 1);
        aSolution.addBlock(waitBlock, CommandBlockPosition.BELOW, 2);
        aSolution.addBlock(numberSensorBlock, CommandBlockPosition.RIGHT, 3);
        aSolution.addBlock(moveBlock1, CommandBlockPosition.BELOW, 4);
        aSolution.addBlock(moveBlock2, CommandBlockPosition.BELOW, 5);
        aSolution.addBlock(moveBlock3, CommandBlockPosition.BELOW, 6);

        // Creo la misión tres y accedo a su evaluador
        MissionFactory missionFactory = new RobotMissionFactory(context);
        Mission missionThree = missionFactory.createMission(3);
        MissionEvaluator evaluator = missionThree.getEvaluator();

        Program proposedSolution = new Program(aSolution);
//        aSolution.printOut();

        String realResult = evaluator.evaluate(aSolution);
        String expectedResult = MissionEvaluator.NOTOK; // debería de no dar por válida la misión!
        assertEquals(expectedResult, realResult);
    }
}