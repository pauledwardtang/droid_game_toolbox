package com.tangly.scorecard.storage;

/**
 * Simple strategy for committing objects to the file system using
 * a file based approach.
 * TODO UNFINISHED
 */
import java.io.*;
import java.util.*;

public class SimpleFsStoreStrategy implements StoreStrategy
{
    private OutputStream file;
    private OutputStream buffer;
    private ObjectOutput output;
    List<Storable> items;

    @Override
    public void commit(Storable s)
    {
        try
        {
            // use buffering
            file = new FileOutputStream("sc-out");
            buffer = new BufferedOutputStream(file);
            output = new ObjectOutputStream(buffer);
            try
            {
                // output.writeObject(quarks);
            } finally
            {
                output.close();
            }
        } catch (IOException ex)
        {
            // fLogger.log(Level.SEVERE, "Cannot perform output.", ex);
        }
    }

    @Override
    public Storable retrieve(int id)
    {
        // TODO: Implement this method
        return null;
    }

    private void emptyFile(String filename) throws IOException
    {
        OutputStream os = null;
        try
        {
            os = new FileOutputStream(filename);
        } finally
        {
            if (os != null)
            {
                os.close();
            }
        }
    }

    private void deleteFile(String filename)
    {
        File f = new File(filename);
        if (f.delete())
        {
            System.out.println(filename + " deleted sucessfully...");
        } else
        {
            System.out.println(filename + " deletion failed!");
        }
    }

    private void writeObjectsToFile(String filename, List objects) throws IOException
    {
        OutputStream os = null;
        try
        {
            os = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            for (Object object : objects)
            {
                oos.writeObject(object);
            }
            oos.flush();
        } finally
        {
            if (os != null)
            {
                os.close();
            }
        }
    }

    private List readObjectsFromFile(String filename) throws IOException, ClassNotFoundException
    {
        List objects = new ArrayList();
        InputStream is = null;
        try
        {
            is = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(is);
            while (true)
            {
                try
                {
                    Object object = ois.readObject();
                    objects.add(object);
                } catch (EOFException ex)
                {
                    break;
                }
            }
        } finally
        {
            if (is != null)
            {
                is.close();
            }
        }
        return objects;
    }
}
