package calculate;

import calculate.weektwaalf.JSF31KochFractalConsole;
import calculate.weektwaalf.ReadEdgesFromFile;
import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;

import java.util.ArrayList;

public class KochManager
{
    private JSF31KochFractalFX application;

//    private ExecutorService pool;

    private ArrayList<Edge> edges;

    private int level;

//    //New tasks
//    private Task taskLeft = null;
//    private Task taskBottom = null;
//    private Task taskRight = null;
//    private int count;

    public KochManager(JSF31KochFractalFX application)
    {
        this.application = application;

        edges = new ArrayList<>();

//        pool = Executors.newFixedThreadPool(3);
    }

    public void changeLevel(int nxt)
    {
        TimeStamp readStamp = new TimeStamp();
        edges.clear();

        try
        {
            readStamp.setBegin();
//            edges = ReadEdgesFromFile.getEdgesFromFile("koch.jsf");
//            edges = ReadEdgesFromFile.getEdgesFromFileBuffered("koch.jsf");
//            edges = ReadEdgesFromFile.getEdgesFromTextFileBuffered("koch.txt");
            edges = ReadEdgesFromFile.getEdgesFromMappedFileWithLock("mapped.bin", this);
            readStamp.setEnd();
            System.out.println("Zo lang:" + readStamp.toString());
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        application.setTextCalc(readStamp.toString());
        application.requestDrawEdges();
    }

    public int getLevel()
    {
        return level;
    }

    public void setEdges(ArrayList<Edge> givenEdges)
    {
//        edges = givenEdges;
//        application.requestDrawEdges();
    }

    public synchronized void addEdges(Edge edge)
    {
        this.edges.add(edge);
    }

    public void drawEdges()
    {
        TimeStamp drawStamp = new TimeStamp();
        application.clearKochPanel();

        drawStamp.setBegin();
        for(Edge e : edges)
        {
            application.drawEdge(e);
        }
        drawStamp.setEnd();

        application.setTextDraw(drawStamp.toString());
    }

//    public void runGenerationTasks()
//    {
//        if(taskLeft != null)
//        {
//            application.getProgressNrEdgesLeft().textProperty().unbind();
//            application.getLeftProgress().progressProperty().unbind();
//        }
//
//        if(taskRight != null)
//        {
//            application.getProgressNrEdgesRight().textProperty().unbind();
//            application.getRightProgress().progressProperty().unbind();
//        }
//
//        if(taskBottom != null)
//        {
//            application.getProgressNrEdgesBottom().textProperty().unbind();
//            application.getBottomProgress().progressProperty().unbind();
//        }
//
//        taskLeft = new CalculateTask(KochSide.LEFT, this);
//        taskRight = new CalculateTask(KochSide.RIGHT, this);
//        taskBottom = new CalculateTask(KochSide.BOTTOM, this);
//
//        application.getLeftProgress().setProgress(0);
//        application.getLeftProgress().progressProperty().bind(taskLeft.progressProperty());
//        application.getProgressNrEdgesLeft().textProperty().bind(taskLeft.messageProperty());
//
//        application.getRightProgress().setProgress(0);
//        application.getRightProgress().progressProperty().bind(taskRight.progressProperty().add(taskLeft.progressProperty()).add(taskBottom.progressProperty()).divide(3));
//        application.getProgressNrEdgesRight().textProperty().bind(taskRight.messageProperty());
//
//        application.getBottomProgress().setProgress(0);
//        application.getBottomProgress().progressProperty().bind(taskBottom.progressProperty());
//        application.getProgressNrEdgesBottom().textProperty().bind(taskBottom.messageProperty());
//
//
//        Thread thLeft = new Thread(taskLeft);
//        Thread thRight = new Thread(taskRight);
//        Thread thBottom = new Thread(taskBottom);
//
//        pool.submit(thLeft);
//        pool.submit(thRight);
//        pool.submit(thBottom);
//    }

    public JSF31KochFractalFX getApplication()
    {
        return application;
    }

//    public synchronized void finished() throws ExecutionException, InterruptedException {
//
//        count++;
//
//        if(count >= 3){
//            application.requestDrawEdges();
//            count = 0;
//        }
//    }


    public void drawEdge(Edge e)
    {
        application.drawWhiteEdge(e);
    }
}
