package es.uned.fipl.block.listeners;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Gemma Lara Savill on 03/04/2015.
 *
 * Detecta cuando un bloque es tocado por un usuario
 * Se inicia la posibilidad de arrastrarlo
 */
public class CommandBlockTouchListener implements View.OnTouchListener {

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            return true;
        } else {
            return false;
        }
    }
}
