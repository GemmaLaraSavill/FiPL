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

import android.graphics.Point;

/**
 * Created by Gemma Lara Savill on 14/04/2015.
 *
 * Unas coordenadas X,Y en un plano bidimensional
 */
public class Coordinates extends Point {

    /**
     * Crea una coordenada pasándole sus valores
     * @param x int valor en el plano X (horizontal)
     * @param y int valor en el plano Y (vertical)
     */
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Suma una cantidad a la dimensión X de la coordenada (horizontal)
     * @param ammount int cantidad en la que se quiere variar la posición X
     */
    public void incrementX(int ammount) {
        this.x = this.x + ammount;
    }

    /**
     * Suma una cantidad a la dimensión Y de la coordenada (vertical)
     * @param ammount int cantidad en la que se quiere variar la posición Y
     */
    public void incrementY(int ammount) {
        this.y = this.y + ammount;
    }

    /**
     * Imprime el valor de los puntos X,Y de la coordenda
     * en la consola del sistema
     * Para debugging
     */
    public void printOut() {
        System.out.println("X: "+this.x+" Y:"+this.y);
    }
}
