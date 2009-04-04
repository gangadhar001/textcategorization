/**
 * @(#)kNN.java
 *
 *
 * @author Choon Meng, Chris and Wei Jie
 * @version 1.00 2009/4/4
 */

import java.io.*;
import java.io.File;
import java.util.*;

public class kNN {

    public kNN() {
    }
    
    private static void getFiles(File folder, ArrayList<File> list,Hashtable HT) throws IOException {
        folder.setReadOnly();
        File[] files = folder.listFiles();
        for(int j = 0; j < files.length; j++) {
            if(files[j].isDirectory())
                getFiles(files[j], list, HT);
            else
            {
            	if(files[j].getAbsolutePath().indexOf("svn")==-1)
            	{
            		String [] array = null;
            		String path = files[j].getAbsolutePath();
            
    				array = path.split("\\\\");
    				int arrayLength = array.length;
    				String filename = array[arrayLength - 1];
    				String filecategory = array[arrayLength - 2];
    				
            		copyFile(files[j],new File("./"+filename));
            		list.add(new File(filename));
            		HT.put(filename,filecategory);
            	}
            }
            	
        }
    }//recursive traversing of files; does copying of files and hashing of filename to file category

    public static void copyFile(File srcFile, File destFile) throws IOException
	{
        InputStream oInStream = new FileInputStream(srcFile);
        OutputStream oOutStream = new FileOutputStream(destFile);

        // Transfer bytes from in to out
        byte[] oBytes = new byte[1024];
        int nLength;
        BufferedInputStream oBuffInputStream = new BufferedInputStream( oInStream );
        while ((nLength = oBuffInputStream.read(oBytes)) > 0)
                oOutStream.write(oBytes, 0, nLength);
        
        oInStream.close();
        oOutStream.close();
	}// to copy files from 1 destination to another
	
    public static void main(String[] args)
    {
        try
        {
        	File folder = new File("../training_corpus");
        	Hashtable HT = new Hashtable();
        	
        	ArrayList<File> documentVector = new ArrayList<File>();
        

        	getFiles(folder,documentVector,HT);
        	String exec="";
        
        	for(int i=0;i<documentVector.size();i++)
        		exec=exec+documentVector.get(i)+" ";
        		
        //	Runtime.getRuntime().exec("java -jar stemmer.jar "+exec);
        		
       		//for(int i=0;i<list.size();i++)
        	//	list.get(i).delete();
        		
       		System.out.println("Finished execution");
        }
    	catch (IOException e) {System.out.println(e);}
    }
}