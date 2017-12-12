package calculate.weektwaalf;

import calculate.Edge;
import calculate.KochFractal;
import calculate.KochManager;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
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
                while (true)
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
                while (true)
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

        for (String edge : stringEdges)
        {
            out.add(formatEdge(edge));
        }

        return out;
    }


    public static ArrayList<Edge> getEdgesFromMappedFileWithLock(String url) throws IOException
    {
        FileLock lock;
        final int NBYTES = 40;
        final int STATUS_NOT_READ = 1;
        final int STATUS_READ = 0;
        int nextBytes = 0;
        ArrayList<Edge> out = new ArrayList<>();

        try
        {
            FileChannel fc = new RandomAccessFile((new File(url)), "rw").getChannel();
            MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size());

            boolean finished = false;

            while (!finished)
            {
                lock = fc.lock(0, NBYTES, false);

                //Vraag maximum
                buffer.position(nextBytes);
                int MAXVAL = buffer.getInt();
                System.out.println("Maxval: "+ MAXVAL);
                int STATUS = buffer.getInt();
                System.out.println("Status:" + STATUS);

                if (STATUS == STATUS_NOT_READ)
                {
                    buffer.position(NBYTES + 4);
                    buffer.putInt(STATUS_READ);

                    Edge e = new Edge();
                    e.X1 = buffer.getDouble();
                    e.X2 = buffer.getDouble();
                    e.Y1 = buffer.getDouble();
                    e.Y2 = buffer.getDouble();
                    out.add(e);

                    nextBytes += NBYTES ;

                    finished = (out.indexOf(e) == MAXVAL && MAXVAL != 0);
                }

                Thread.sleep(10);
                lock.release();
            }
        }

        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
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
