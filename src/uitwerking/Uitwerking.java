package uitwerking;

import calculate.KochFractal;

public class Uitwerking
{
    public static void main(String[] args)
    {
        CalcKochFractal();
    }

    private static void CalcKochFractal()
    {
        KochFractal fractal = new KochFractal();
        fractal.addObserver(new KochObserver());
        fractal.setLevel(2);

        fractal.generateBottomEdge();
        fractal.generateLeftEdge();
        fractal.generateRightEdge();
    }
}
