package calculate.tasks;

import calculate.Edge;
import calculate.KochFractal;
import calculate.KochManager;
import calculate.threading.KochSide;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class CalculateTask extends Task<ArrayList<Edge>> implements Observer
{
    private final KochSide side;
    private ArrayList<Edge> edges;
    private KochFractal fractal;
    private int count;
    private int maxEdges;
    private KochManager kochManager;

    public CalculateTask(KochSide side, KochManager kochManager)
    {
        this.side = side;
        this.kochManager = kochManager;

        edges = new ArrayList<>();
        fractal = new KochFractal();
        fractal.addObserver(this);
        fractal.setLevel(kochManager.getLevel());

        maxEdges = fractal.getNrOfEdges() / 3;
    }


    public KochFractal getFractal()
    {
        return fractal;
    }

    public void setFractal(KochFractal fractal)
    {
        this.fractal = fractal;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        edges.add((Edge) arg);
        kochManager.addEdges((Edge) arg);

        Platform.runLater(new Runnable()
        {

            Edge e = (Edge) arg;

            @Override
            public void run()
            {
                count++;
                updateProgress(count, maxEdges);
                double percentage = (count * 100.0) / maxEdges;
                updateMessage(String.format("%.2f %s", percentage, "%"));
                kochManager.drawEdge(e);
            }
        });

        //Vertraging want dat ziet er mooi uit (van github)
        try
        {
            switch (side)
            {
                case LEFT:
                    Thread.sleep(1);
                    break;

                case RIGHT:
                    Thread.sleep(1);
                    break;

                case BOTTOM:
                    Thread.sleep(2);
                    break;
            }
        }
        catch (Exception exc)
        {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    protected ArrayList<Edge> call() throws Exception
    {
        edges.clear();

        switch (side)
        {
            case LEFT:
                fractal.generateLeftEdge();
                break;

            case RIGHT:
                fractal.generateRightEdge();
                break;

            case BOTTOM:
                fractal.generateBottomEdge();
                break;
        }

        //TODO naar manager onFinish
//        kochManager.finished();

        return edges;
    }

    @Override
    public void cancelled()
    {
        super.cancelled();
        fractal.cancel();
    }
}
