/*
* Copyright 2015 Gemma Samantha Lara Savill
* FiPL My First Programming Language
* Proyecto de Fin de Grado en Ingeniería en Tecnologías de la Información
* en la Universidad Nacional de Educación a Distancia UNED España
*
* Final project for my Bachelor of Science in Information Technology Engineering
* At the Spanish National Distance University UNED
* Project manager Dr. Anselmo Peñas Padilla

* Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
* You may not use this work except in compliance with the Licence.
* You may obtain a copy of the Licence at:
*
* http://ec.europa.eu/idabc/eupl
*
* Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the Licence for the specific language governing permissions and limitations under the Licence.
*/
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
