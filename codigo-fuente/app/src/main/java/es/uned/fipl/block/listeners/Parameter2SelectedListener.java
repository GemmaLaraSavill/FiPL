package es.uned.fipl.block.listeners;

import android.view.View;
import android.widget.AdapterView;

import es.uned.fipl.block.CommandBlock;

/**
 * Created by Gemma Lara Savill on 28/03/2015.
 *
 * Detecta un cambio en el segundo selector de parámetros de un bloque
 */
public class Parameter2SelectedListener implements AdapterView.OnItemSelectedListener {


    private final CommandBlock block;

    /**
     * Le pasamos su bloque
     * @param commandBlock bloque al que pertenece el selector
     */
    public Parameter2SelectedListener(CommandBlock commandBlock) {
        this.block = commandBlock;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // le paso el parámetro al bloque
        block.setParameter2(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
