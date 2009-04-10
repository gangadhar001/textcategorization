// Decompiled by DJ v3.0.0.63 Copyright 2002 Atanas Neshkov  Date: 10/04/2009 11:27:49 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   StopListStemmer.java


import java.io.*;
import java.util.*;

// Referenced classes of package sg.edu.nus.soc.imageSearchLab:
//            Stemmer

public class StopListStemmer
{

    public StopListStemmer()
    {
        inputMap = new TreeMap();
    }

    public void insertItem(String key)
    {
        if(inputMap.containsKey(key))
        {
            Integer i = (Integer)inputMap.get(key);
            i = Integer.valueOf(i.intValue() + 1);
            inputMap.remove(key);
            inputMap.put(key, i);
        } else
        {
            inputMap.put(key, Integer.valueOf(1));
        }
    }

    public static void printMap(Map map)
    {
        Iterator keyValuePairs1 = map.entrySet().iterator();
        for(int i = 0; i < map.size(); i++)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)keyValuePairs1.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println((new StringBuilder()).append(key).append("\t").append(value).toString());
        }

    }

    public void printIntoFile(String path)
    {
        try
        {
            PrintWriter outprint = new PrintWriter(new FileOutputStream(path));
            Iterator keyValuePairs1 = inputMap.entrySet().iterator();
            for(int i = 0; i < inputMap.size(); i++)
            {
                java.util.Map.Entry entry = (java.util.Map.Entry)keyValuePairs1.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                outprint.println((new StringBuilder()).append(key).append("\t").append(value).toString());
            }

            outprint.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void fileInsertIntoMap(String filepath)
    {
        char w[] = new char[501];
        Stemmer s = new Stemmer();
        try
        {
            FileInputStream in = new FileInputStream(filepath);
            try
            {
                int k = 0;
                do
                {
                    int ch = in.read();
                    if(Character.isLetter((char)ch))
                    {
                        int j = 0;
                        do
                        {
                            ch = Character.toLowerCase((char)ch);
                            w[j] = (char)ch;
                            if(j < 500)
                                j++;
                            ch = in.read();
                        } while(Character.isLetter((char)ch));
                        for(int c = 0; c < j; c++)
                            s.add(w[c]);

                        s.stem();
                        String u = s.toString();
                        insertItem(u);
                    }
                    if(ch < 0)
                        break;
                    k++;
                } while(true);
            }
            catch(IOException e)
            {
                System.out.println((new StringBuilder("error reading ")).append(filepath).toString());
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println((new StringBuilder("file ")).append(filepath).append(" not found").toString());
        }
    }

    public void handleStopList()
    {
        try
        {
            FileInputStream fstream = new FileInputStream("stoplist.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String word;
            while((word = br.readLine()) != null) 
                if(inputMap.containsKey(word))
                    inputMap.remove(word);
            br.close();
            in.close();
            fstream.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        StopListStemmer sl = new StopListStemmer();
        for(int i = 1; i < args.length; i++)
        {
            sl.fileInsertIntoMap(args[i]);
            sl.handleStopList();
            
            String filename ="";
            StringTokenizer st = new StringTokenizer((args[i]).toString(),"/");
		     while (st.hasMoreTokens()) {
         		filename = st.nextToken();
     		}

            sl.printIntoFile(".\\"+(args[0]).toString()+"\\output_"+filename);
        }

    }

    public Map inputMap;
}