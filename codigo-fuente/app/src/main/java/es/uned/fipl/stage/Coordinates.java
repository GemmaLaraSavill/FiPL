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
