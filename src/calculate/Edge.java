/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package calculate;

import javafx.scene.paint.Color;

import java.io.Serializable;

public class Edge implements Serializable
{
    public double X1, Y1, X2, Y2;
    public transient Color color;

    public Edge(double X1, double Y1, double X2, double Y2, Color color)
    {
        this.X1 = X1;
        this.Y1 = Y1;
        this.X2 = X2;
        this.Y2 = Y2;
        this.color = Color.LIGHTSKYBLUE;
    }

    public Edge() {
        this.color = Color.LIGHTSKYBLUE;
    }

    @Override
    public String toString()
    {
        return(String.format("%f:%f:%f:%f", X1, X2, Y1, Y2));
    }
}
