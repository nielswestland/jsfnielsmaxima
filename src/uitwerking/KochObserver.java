package uitwerking;

import calculate.Edge;

import java.util.Observable;
import java.util.Observer;

public class KochObserver implements Observer
{
    @Override
    public void update(Observable o, Object arg)
    {
        Edge e = (Edge) arg;
        System.out.println(e.X1 + " " + e.X2 + " " + e.Y1 + " " + e.Y2);
    }
}
