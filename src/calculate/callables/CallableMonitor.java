package calculate.callables;

import calculate.Edge;
import calculate.KochManager;
import calculate.threading.KochSide;
import javafx.application.Platform;
import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;

import java.util.ArrayList;
import java.util.concurrent.*;

public class CallableMonitor implements Runnable
{
    private KochManager kochManager;
    private EdgeCallable leftCallable = new EdgeCallable(KochSide.LEFT);
    private EdgeCallable rightCallable = new EdgeCallable(KochSide.RIGHT);
    private EdgeCallable bottomCallable = new EdgeCallable(KochSide.BOTTOM);

    ExecutorService executorService = Executors.newFixedThreadPool(3);


    private ArrayList<Edge> resultEdges;

    public CallableMonitor(int nxt, KochManager kochManager)
    {
        this.kochManager = kochManager;
        setLevel(nxt);
    }

    public void setLevel(int nxt)
    {
        leftCallable.setLevel(nxt);
        rightCallable.setLevel(nxt);
        bottomCallable.setLevel(nxt);
    }

    @Override
    public void run()
    {
        TimeStamp generationStamp = new TimeStamp();

        this.resultEdges = new ArrayList<>();

        generationStamp.setBegin();

        Future<ArrayList<Edge>> leftEdges = executorService.submit(leftCallable);
        Future<ArrayList<Edge>> rightEdges = executorService.submit(rightCallable);
        Future<ArrayList<Edge>> bottomEdges = executorService.submit(bottomCallable);

        try
        {
            if(!leftEdges.get().isEmpty() && !rightEdges.get().isEmpty() && !bottomEdges.get().isEmpty())
            {
                resultEdges.addAll(leftEdges.get());
                resultEdges.addAll(rightEdges.get());
                resultEdges.addAll(bottomEdges.get());
            }

            generationStamp.setEnd();

            executorService.shutdown();

            kochManager.setEdges(resultEdges);

//            Platform.runLater(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    application.setTextCalc(generationStamp.toString());
//                    application.setTextNrEdges("" + leftCallable.getFractal().getNrOfEdges());
//                }
//            });
        }

        catch (InterruptedException | ExecutionException e)
        {
            generationStamp.setEnd();
            e.printStackTrace();
        }
    }
}
