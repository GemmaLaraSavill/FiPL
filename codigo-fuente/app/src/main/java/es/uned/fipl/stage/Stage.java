package es.uned.fipl.stage;

import android.content.Context;

import es.uned.fipl.R;

/**
 * Created by Gemma Lara Savill on 30/04/2015.
 * Escenario por donde corre el robot
 * Es una cuadrícula de colores
 */
public class Stage {

    private int[] colorTiles; // lista de colores
    public static int ROWS = 5;
    public static int COLS = 5;

//    public static int TILE_WIDTH = 70; // multiplicado por la densidad de la pantallla

    public Stage() {

    }

    public int[] getColorTiles() {
        return colorTiles;
    }

    public void setColorTiles(int[] colorTiles) {
        this.colorTiles = colorTiles;
    }

    public int getColor(int x, int y) {
        return this.colorTiles[x+(this.COLS*y)];
    }

    /**
     * Devuelve el ancho de la baldosa según la densidad de píxeles
     * del dispositivo
     * @param context
     * @return
     */
    public static int getTileWidth(Context context) {
//        float density = context.getResources().getDisplayMetrics().density;
//        return Stage.TILE_WIDTH * (int)density;
        return (int)context.getResources().getDimension(R.dimen.tile_width);
    }
}
