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
    
    private static void getFiles(File folder, ArrayList<String> list,Hashtable HT) throws IOException {
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
            		list.add(filename);
            		
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
        	File trainfolder = new File("../training_corpus");
        	File testfolder = new File("../testing_corpus");
        	Hashtable HTrain = new Hashtable();
        	Hashtable HTest = new Hashtable();
        	int [][]EvaluationMatrix = new int[2][2];
        	
        	for(int i=0;i<2;i++)
        		for(int j=0;j<2;j++)
        			EvaluationMatrix[i][j]=0;
        	
        	ArrayList<String> trainingDocumentVector = new ArrayList<String>();
        	ArrayList<String> testingDocumentVector = new ArrayList<String>();
        	ArrayList<String> trainoutputDocumentVector = new ArrayList<String>();
        	ArrayList<String> testoutputDocumentVector = new ArrayList<String>();
        
        	getFiles(trainfolder,trainingDocumentVector,HTrain);
        	String exec="";
        
        	File traindir = new File("train");
        	
        	/*if(traindir.exists())
        	{
    			Process p = Runtime.getRuntime().exec("rmdir -rf train");
        		p.waitFor();
    		}*/
        		
        	boolean success = traindir.mkdir();
   			if (success) {
      			System.out.println("Training directory created");
    		}
    		
    		File testdir = new File("test");
    		
    	/*	if(testdir.exists())
    		{
    			Process p = Runtime.getRuntime().exec("rmdir -rf test");
        		p.waitFor();
    		}*/
        		
        	success = testdir.mkdir();
   			if (success) {
      			System.out.println("Testing directory created");
    		}
    		
        	for(int i=0;i<trainingDocumentVector.size();i++)
        	{
        		exec=exec+trainingDocumentVector.get(i)+" "; 	
        		trainoutputDocumentVector.add("output_"+trainingDocumentVector.get(i));
        	}
        	
            Process p = Runtime.getRuntime().exec("java -jar stemmer.jar "+exec);
        	p.waitFor();
    		
    		for(int i=0;i<trainoutputDocumentVector.size();i++)
        	{
        		File file = new File(trainoutputDocumentVector.get(i));
        		File fileTodel = new File(trainingDocumentVector.get(i));
        		file.renameTo(new File(traindir, file.getName())); //moving files
        		fileTodel.delete();
        	}  
    	
        	getFiles(testfolder,testingDocumentVector,HTest);
        	
        	exec="";
      
        	for(int i=0;i<testingDocumentVector.size();i++)
        	{
        		exec=exec+testingDocumentVector.get(i)+" "; 	
        		testoutputDocumentVector.add("output_"+testingDocumentVector.get(i));
        	}
        	
            p = Runtime.getRuntime().exec("java -jar stemmer.jar "+exec);
        	p.waitFor();
        	
        	for(int i=0;i<testoutputDocumentVector.size();i++)
        	{
        		File file = new File(testoutputDocumentVector.get(i));
        		File fileTodel = new File(testingDocumentVector.get(i));
        		file.renameTo(new File(testdir, file.getName()));
        		fileTodel.delete();
        	}  
        	
        	Similarity s = new Similarity();
        	
        	Scanner scan = new Scanner(System.in);
     		int k;

     		System.out.print("Enter the value of k:");
     		k = scan.nextInt();
     			
        	for(int i=0;i<testoutputDocumentVector.size();i++)
        	{
        		String testdoc = "./test/"+testoutputDocumentVector.get(i);
        	
        		String []knn = new String[k];
     			double []freq = new double[k];
     		
     			for(int a=0; a<k; a++)
     			{
     				knn[a] = new String("");
     				freq[a] = 0;
     			}
     				
     			
        		for(int j=0;j<trainoutputDocumentVector.size();j++)
        		{
        			String traindoc = "./train/"+trainoutputDocumentVector.get(j);
        			
        			double v = s.computeSimilarity(testdoc,traindoc);
        			
        			for(int a=0;a<k;a++)
        			{
        				if(freq[a]<v)
        				{
        					freq[a] = v;
        					knn[a] = traindoc.substring(15);
        					break;
        				}
        			}
        		}
        		
        		int []count= new int[10];
        		String []categories = {"alt.atheism","comp.windows.x","misc.forsale",
        			                   "rec.autos","rec.motorcycles","rec.sport.baseball",
        			                   "sci.electronis","sci.med","talk.politics.misc",
        			                   "talk.religion.misc"};
        			                    
        		for(int h=0;h<k;h++)
        		{
        			String category = (String)HTrain.get((String)knn[h]);
        			
        			for(int a=0;a<10;a++)
        			{
        				String c = categories[a];
        				if(category!=null && category.equals(c))
        					count[a]++;
        			}
        		}
        		
        		/*for(int a=0;a<k;a++)
        		{
        			System.out.println(knn[a]+" "+freq[a]);
        		}
        		
        		for(int a=0;a<10;a++)
        		{
        			System.out.println(categories[a]+" "+count[a]);
        		}*/
        		
        		int max = -1;
        		int pos = -1;
        			
        		for(int a=0;a<10;a++)
        			if(count[a]> max)
        			{
        					max = count[a];
        					pos = a;
        			}
        			
        		String retrievedcategory=""; 
        		if(pos!=-1)
        			retrievedcategory = categories[pos];
        		String relevantcategory = (String)HTest.get((String)testingDocumentVector.get(i));
        	
        		System.out.println("Classified as "+retrievedcategory);
  				System.out.println("Test category is "+relevantcategory);
  				//System.out.println("Testing doc is "+testingDocumentVector.get(i));      		
        		if(pos==-1)
        			EvaluationMatrix[1][0]++;
        		else
        		{
        			if(retrievedcategory.equals(relevantcategory))
        				EvaluationMatrix[0][0]++;
        			else if(!retrievedcategory.equals(relevantcategory))
        				EvaluationMatrix[0][1]++;
        		}
        	}
        	
        	for(int i=0;i<2;i++)
        	{
        		for(int j=0;j<2;j++)
        		{
        			System.out.print(EvaluationMatrix[i][j]+" ");
        		}
        		System.out.println();
        	}
        	
       		System.out.println("Finished execution");
        }
    	catch (Exception e) {System.out.println(e);}
    }
}