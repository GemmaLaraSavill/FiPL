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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Gemma Lara Savill on 30/04/2015.
 *
 * Un objetivo en una misión
 * Se compone de unas coordenas (X,Y) y un drawable (imagen o color)
 */
public class MissionTarget {

    private final Coordinates coordinates; // coordenadas X,Y del objetivo
    private int image; // nombre del recurso de imagen que representará al objetivo
    private ImageView targetView; // View que representará gráficamente el objetivo

    /**
     * Crea un objetivo pasándole unas coordenadas
     * y el nombre del recurso drawable que lo representará
     * ya sea una imagen o un color
     * @param coordinates coordendas de posición del objetivo en el escenario
     * @param image nombre del recurso de imagen que lo representará
     */
    public MissionTarget(Coordinates coordinates, int image) {
        this.image = image;
        this.coordinates = coordinates;
    }

    /**
     * Añade este objetivo al escenario
     * @param context contexto de la ventana Android donde residirá
     * @param gridFrame el layout que lo contendrá (su padre)
     */
    public void addTarget(Context context, FrameLayout gridFrame) {

//        System.out.println("Añado target "+coordinates.getX()+ " "+coordinates.getY());
        // añado Objetivo al escenario
        targetView = new ImageView(context);
        targetView.setBackgroundDrawable(context.getResources().getDrawable(image));
        targetView.setLayoutParams( new LinearLayout.LayoutParams(Stage.getTileWidth(context), Stage.getTileWidth(context)) );
        gridFrame.addView(targetView);
        // lo posiciono en sus coordenadas
        targetView.setX(coordinates.getX()*Stage.getTileWidth(context));
        targetView.setY(coordinates.getY()*Stage.getTileWidth(context));
    }

    /**
     * Borra un objetivo de la interfaz
     * @param gridFrame el layout que lo contiene (su padre)
     */
    public void removeTarget(FrameLayout gridFrame) {
        gridFrame.removeView(targetView);
    }

    /**
     * Devuelve las coordenadas de posición (X,Y)
     * de este objetivo
     * @return Coordinates coordenadas
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }
}
