package calculate.callables;

import calculate.Edge;
import calculate.KochFractal;
import calculate.threading.KochSide;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.*;

public class EdgeCallable implements Callable<ArrayList<Edge>>, Observer
{
    private ArrayList<Edge> localEdges;
    private KochFractal fractal;
    private KochSide side;

    public EdgeCallable(KochSide side)
    {
        this.side = side;
        this.localEdges = new ArrayList<>();
        this.fractal = new KochFractal();
        this.fractal.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        localEdges.add((Edge)arg);
    }

    @Override
    public ArrayList<Edge> call() throws Exception
    {
        localEdges.clear();

        switch(side)
        {
            case LEFT:
                fractal.generateLeftEdge();
                return localEdges;
            case RIGHT:
                fractal.generateRightEdge();
                return localEdges;
            case BOTTOM:
                fractal.generateBottomEdge();
                return localEdges;
        }

        return null;
    }

//    @Override
//    public boolean cancel(boolean mayInterruptIfRunning)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isCancelled()
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isDone()
//    {
//        return false;
//    }
//
//    @Override
//    public ArrayList<Edge> get() throws InterruptedException, ExecutionException
//    {
//        return localEdges;
//    }
//
//    @Override
//    public ArrayList<Edge> get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
//    {
//        return null;
//    }

    public void setLevel(int level)
    {
        this.fractal.setLevel(level);
    }

    public KochFractal getFractal()
    {
        return fractal;
    }
}
