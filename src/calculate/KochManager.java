package calculate;

import calculate.weektwaalf.JSF31KochFractalConsole;
import calculate.weektwaalf.ReadEdgesFromFile;
import javafx.application.Platform;
import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;

import java.util.ArrayList;

public class KochManager
{
    private JSF31KochFractalFX application;

    private ArrayList<Edge> edges;

    private int level;

    public KochManager(JSF31KochFractalFX application)
    {
        this.application = application;

        edges = new ArrayList<>();
    }

    public void changeLevel(int nxt)
    {
        TimeStamp readStamp = new TimeStamp();
        edges.clear();

        try
        {
            readStamp.setBegin();
            ReadEdgesFromFile.getEdgesFromMappedFileWithLock("mapped.bin", this);
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
        //Leeg
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
        for (Edge e : edges)
        {
            application.drawEdge(e);
        }
        drawStamp.setEnd();

        application.setTextDraw(drawStamp.toString());
    }

    public JSF31KochFractalFX getApplication()
    {
        return application;
    }

    public void drawEdge(Edge e)
    {
        application.drawWhiteEdge(e);
    }
}
