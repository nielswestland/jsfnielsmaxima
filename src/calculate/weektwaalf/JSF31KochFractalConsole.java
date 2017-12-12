package calculate.weektwaalf;

import calculate.Edge;
import calculate.KochFractal;
import calculate.tasks.CalculateTask;
import timeutil.TimeStamp;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JSF31KochFractalConsole implements Observer {

    private Scanner scanner = new Scanner(System.in);
    private ExecutorService pool;
    private ArrayList<Edge> edgeList = new ArrayList<>();
    private KochFractal fractal = new KochFractal();

    public static void main(String[] args) {
        JSF31KochFractalConsole app = new JSF31KochFractalConsole();
    }

    private JSF31KochFractalConsole() {
        pool = Executors.newFixedThreadPool(3);
        fractal.addObserver(this);

        int input = 0;
        System.out.println("Please enter level of koch fractal to generate: ");
        try {
            input = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input");
            System.exit(0);
        }
        generateEdges(input);
    }

    private void generateEdges(int level) {
        fractal.setLevel(level);
        int nrOfEdges = fractal.getNrOfEdges();

        System.out.println("Number of edges on level " + level + ": " + nrOfEdges);

        TimeStamp timeStamp = new TimeStamp();
        timeStamp.setBegin();
        fractal.generateBottomEdge();
        fractal.generateRightEdge();
        fractal.generateLeftEdge();
        timeStamp.setEnd();
        System.out.println("Generation time: " + timeStamp.toString());

        writeEdgesBinary();
        writeEdgesBufferedBinary();
        writeEdgesText();
        writeEdgesTextBinary();
        writeEdgesMapped();
    }

    private void writeEdgesBinary() {
        TimeStamp timeStamp = new TimeStamp();
        timeStamp.setBegin();
        try (OutputStream os = new FileOutputStream("koch.jsf"); ObjectOutputStream dos = new ObjectOutputStream(os)) {
            for(Edge e: edgeList) {
                dos.writeObject(e);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        timeStamp.setEnd();
        System.out.println("Written to binary file in " + timeStamp.toString());
    }

    private void writeEdgesBufferedBinary() {
        TimeStamp timeStamp = new TimeStamp();
        timeStamp.setBegin();
        try (OutputStream os = new FileOutputStream("koch2.jsf"); BufferedOutputStream bos = new BufferedOutputStream(os)) {
            bos.write(getByteArray());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        timeStamp.setEnd();
        System.out.println("Written to binary file using buffer in " + timeStamp.toString());
    }

    private void writeEdgesText() {
        TimeStamp timeStamp = new TimeStamp();
        timeStamp.setBegin();
        try(Writer writer = new PrintWriter(new FileWriter("koch.txt"))) {
            for(Edge e: edgeList) {
                writer.write(e.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        timeStamp.setEnd();
        System.out.println("Written to text file in " + timeStamp.toString());
    }

    private void writeEdgesTextBinary() {
        TimeStamp timeStamp = new TimeStamp();
        timeStamp.setBegin();
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("koch2.txt"))) {
            for(Edge e: edgeList) {
                writer.write(e.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        timeStamp.setEnd();
        System.out.println("Written to text file using buffer " + timeStamp.toString());
    }

    private byte[] getByteArray() {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                for (Edge e: edgeList) {
                    o.writeObject(e);
                }
            }
            return b.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeEdgesMapped() {
        TimeStamp timeStamp = new TimeStamp();
        byte[] bytes = getByteArray();
        timeStamp.setBegin();
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("mapped.bin", "rw");
            FileChannel fc = randomAccessFile.getChannel();

            int lenght = 4*8;
            lenght = lenght * edgeList.size();

            MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, lenght);
            for(Edge e: edgeList) {
                buffer.putDouble(e.X1);
                buffer.putDouble(e.X2);
                buffer.putDouble(e.Y1);
                buffer.putDouble(e.Y2);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        timeStamp.setEnd();
        System.out.println("Written to memory mapped file in " + timeStamp.toString());
    }

    @Override
    public void update(Observable o, Object arg) {
        edgeList.add((Edge) arg);
    }
}