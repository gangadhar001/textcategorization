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
    
    private static void getFiles(File folder, List<File> list) throws IOException {
        folder.setReadOnly();
        File[] files = folder.listFiles();
        for(int j = 0; j < files.length; j++) {
            if(files[j].isDirectory())
                getFiles(files[j], list);
            else
            {
            	if(files[j].getAbsolutePath().indexOf("svn")==-1)
            	{
            		copyFile(files[j],new File("./"+files[j].getName()));
            		list.add(new File(files[j].getName()));
            	}
            }
            	
        }
    }//recursive traversing of files

    public static void copyFile(File srcFile, File destFile) throws IOException
	{
        InputStream oInStream = new FileInputStream(srcFile);
        OutputStream oOutStream = new FileOutputStream(destFile);

        // Transfer bytes from in to out
        byte[] oBytes = new byte[1024];
        int nLength;
        BufferedInputStream oBuffInputStream = new BufferedInputStream( oInStream );
        while ((nLength = oBuffInputStream.read(oBytes)) > 0)
        {
                oOutStream.write(oBytes, 0, nLength);
        }
        oInStream.close();
        oOutStream.close();
	}// to copy files from 1 destination to another
	
    public static void main(String[] args)
    {
        try
        {
        	File folder = new File("../training_corpus");
        	List<File> list = new ArrayList<File>();
        	getFiles(folder, list);
        	String exec="";
        
        	for(int i=0;i<list.size();i++)
        		exec=exec+list.get(i)+" ";
        		
        	Runtime.getRuntime().exec("java -jar stemmer.jar "+exec);
        		
       		//for(int i=0;i<list.size();i++)
        	//	list.get(i).delete();
        		
       		System.out.println("Finished execution");
        }
    	catch (IOException e) {System.out.println(e);}
    }
}