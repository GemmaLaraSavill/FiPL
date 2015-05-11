package es.uned.fipl.stage.listeners;

import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by Gemma Lara Savill on 12/04/2015.
 */
public class NumberOnClickListener implements View.OnClickListener {

    private final int number;
    private final TextView numberSensor;

    public NumberOnClickListener(int number, TextView numberSensor) {
        this.number = number;
        this.numberSensor = numberSensor;
    }

    @Override
    public void onClick(View view) {
        Button b = (Button)view;
        // change number sensor value
        numberSensor.setText(b.getText());
        // enable the whole number keypad
        GridView keypad = (GridView) b.getParent();
        int buttonCount = keypad.getChildCount();
        for(int i=0; i<buttonCount; i++) {
            keypad.getChildAt(i).setEnabled(true);
        }
        // disable number button
        b.setEnabled(false);

    }
}
