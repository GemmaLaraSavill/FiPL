package es.uned.fipl;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import es.uned.fipl.block.CommandBlock;
import es.uned.fipl.block.CommandBlockFactory;
import es.uned.fipl.block.CommandBlockName;

/**
 * Created by Gemma Lara Savill on 07/03/2015
 * Ventana con información sobre el programa (ayuda)
 */
public class HelpActivity extends Activity {

    /**
     * Al crearse la ventana
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Configuro barra de menú
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // flechita volver
        actionBar.setSubtitle(R.string.action_helpScreen);

        Context context = this;

        // cargo la interfaz
        LinearLayout moveBlockHolder = (LinearLayout) findViewById(R.id.moveBlockHolder);
        LinearLayout speedBlockHolder = (LinearLayout) findViewById(R.id.speedBlockHolder);
        LinearLayout waitBlockHolder = (LinearLayout) findViewById(R.id.waitBlockHolder);
        LinearLayout positionBlockHolder = (LinearLayout) findViewById(R.id.positionBlockHolder);
        LinearLayout repeatBlockHolder = (LinearLayout) findViewById(R.id.repeatBlockHolder);
        LinearLayout numberSensorBlockHolder = (LinearLayout) findViewById(R.id.numberSensorBlockHolder);
        LinearLayout colorSensorBlockHolder = (LinearLayout) findViewById(R.id.colorSensorBlockHolder);

        CommandBlockFactory blockFactory = new CommandBlockFactory();
        // creo y cargo bloque mover
        CommandBlock moveBlock = blockFactory.createBlock(CommandBlockName.MOVE, context);
        moveBlockHolder.addView(moveBlock);
        // creo y cargo bloque velocidad
        CommandBlock speedBlock = blockFactory.createBlock(CommandBlockName.SPEED, context);
        speedBlockHolder.addView(speedBlock);
        // creo y cargo bloque esperar
        CommandBlock waitBlock = blockFactory.createBlock(CommandBlockName.WAIT, context);
        waitBlockHolder.addView(waitBlock);
        // creo y cargo bloque posisión
        CommandBlock positionBlock = blockFactory.createBlock(CommandBlockName.POSITION, context);
        positionBlockHolder.addView(positionBlock);
        // creo y cargo bloque repetir
        CommandBlock repeatBlock = blockFactory.createBlock(CommandBlockName.REPEAT, context);
        repeatBlockHolder.addView(repeatBlock);
        // creo y cargo bloque sensor numero
        CommandBlock numberSensorBlock = blockFactory.createBlock(CommandBlockName.NUMBERSENSOR, context);
        numberSensorBlockHolder.addView(numberSensorBlock);
        // creo y cargo bloque sensor color
        CommandBlock colorSensorBlock = blockFactory.createBlock(CommandBlockName.COLORSENSOR, context);
        colorSensorBlockHolder.addView(colorSensorBlock);

        // los textos están especificados en su xml /layouts/action_helpScreen.xml
    }


}
