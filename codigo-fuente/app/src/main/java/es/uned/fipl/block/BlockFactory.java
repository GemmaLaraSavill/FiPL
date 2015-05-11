package es.uned.fipl.block;

import android.content.Context;

/**
 * Created by Gemma Lara Savill on 19/03/2015.
 *
 * Crea CommandBlock
 */
public interface BlockFactory {

    /**
     * Crea un bloque según su tipo
     * @param commandBlockName el tipo de bloque
     * @param context El contexto de la ventana Android
     * @return un CommandBlock nuevecito
     */
    CommandBlock createBlock(CommandBlockName commandBlockName, Context context);
}
