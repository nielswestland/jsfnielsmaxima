package calculate.weektwaalf;

import calculate.Edge;
import calculate.KochFractal;
import calculate.KochManager;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class ReadEdgesFromFile
{
    public static ArrayList<Edge> getEdgesFromFileBuffered(String url) throws IOException, ClassNotFoundException
    {
        ArrayList<Edge> out = new ArrayList<>();

        try (FileInputStream fileStream = new FileInputStream(new File(url)))
        {
            //Als buffer
            BufferedInputStream bufferStream = new BufferedInputStream(fileStream);
            //Als object
            ObjectInputStream objectStream = new ObjectInputStream(bufferStream);

            try
            {
                while(true)
                {
                    out.add((Edge) objectStream.readObject());
                }
            }

            catch (EOFException e)
            {
                //Niks doen aub! loop stopt vanzelf
            }
        }

        return out;
    }

    public static ArrayList<Edge> getEdgesFromFile(String url) throws IOException, ClassNotFoundException
    {
        ArrayList<Edge> out = new ArrayList<>();

        try (FileInputStream fileStream = new FileInputStream(new File(url)))
        {
            //Als object
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);

            try
            {
                while(true)
                {
                    out.add((Edge) objectStream.readObject());
                }
            }

            catch (EOFException e)
            {
                //Niks doen aub! loop stopt vanzelf
            }
        }

        return out;
    }

    public static ArrayList<Edge> getEdgesFromTextFileBuffered(String url) throws IOException
    {
        ArrayList<String> stringEdges = new ArrayList<>();
        ArrayList<Edge> out = new ArrayList<>();

        try (FileReader fileStream = new FileReader(new File(url)))
        {
            //Als buffer
            BufferedReader bufferStream = new BufferedReader(fileStream);
            //Als object

            String line;
            while ((line = bufferStream.readLine()) != null)
            {
                stringEdges.add(line);
            }
        }

        for(String edge : stringEdges)
        {
            out.add(formatEdge(edge));
        }

        return out;
    }

    public static ArrayList<Edge> getEdgesFromMappedFile(String url) throws IOException {
        ArrayList<Edge> out = new ArrayList<>();

        FileChannel fc = new RandomAccessFile((new File(url)), "rw").getChannel();
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

        while (buffer.hasRemaining()) {
            Edge e = new Edge();
            e.X1 = buffer.getDouble();
            e.X2 = buffer.getDouble();
            e.Y1 = buffer.getDouble();
            e.Y2 = buffer.getDouble();
            out.add(e);
        }
        return out;
    }

//    private ArrayList<Edge> getEdgesFromTextFile(String url) throws IOException
//    {
//        ArrayList<String> stringEdges = new ArrayList<>();
//        ArrayList<Edge> out = new ArrayList<>();
//
//        try (FileReader fileStream = new FileReader(new File(url)))
//        {
//            int line;
//            while ((line = fileStream.read()) != -1)
//            {
//                stringEdges.add(line);
//            }
//        }
//
//        for(String edge : stringEdges)
//        {
//            out.add(formatEdge(edge));
//        }
//
//        return out;
//    }

    private static Edge formatEdge(String edgeString)
    {
        String[] parts = edgeString.split(":");

        double X1;
        double X2;
        double Y1;
        double Y2;
        Color color;

        X1 = Double.parseDouble(parts[0]);
        X2 = Double.parseDouble(parts[1]);
        Y1 = Double.parseDouble(parts[2]);
        Y2 = Double.parseDouble(parts[3]);
        color = Color.LIGHTSKYBLUE;

        return new Edge(X1, Y1, X2, Y2, color);
    }
}
