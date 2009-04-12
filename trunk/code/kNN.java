/**
 * @(#)kNN.java
 * @author Choon Meng, Chris and Wei Jie
 * @version 1.00 2009/4/4
 */

import java.io.*;
import java.io.File;
import java.util.*;

public class kNN {

	String train = "./train ";
    String test = "./test ";
    TermSelection ts;
    File trainfolder = new File("../training_corpus");
    File testfolder = new File("../testing_corpus");
    
    int [][]EvaluationMatrix = new int[2][2];
    
    Hashtable HTrain;
    ArrayList<String> trainingDocumentVector;
    ArrayList<String> trainingDocumentVector_address;
    ArrayList<String> trainoutputDocumentVector;
    
    Hashtable HTest;
    ArrayList<String> testingDocumentVector;
    ArrayList<String> testingDocumentVector_address;
    ArrayList<String> testoutputDocumentVector;
        	
    public kNN() {
    	for(int i=0;i<2;i++)
        		for(int j=0;j<2;j++)
        			EvaluationMatrix[i][j]=0;

    	ts = new TermSelection();
    }
    
    public static void bubbleSort(double[] x,String[] y) {
    	int n = x.length;
    	for (int pass=1; pass < n; pass++) {  // count how many times
        	// This next loop becomes shorter and shorter
        	for (int i=0; i < n-pass; i++) {
            	if (x[i] < x[i+1]) {
                	// exchange elements
                	double temp = x[i];  x[i] = x[i+1];  x[i+1] = temp;
                	String stemp = y[i];  y[i] = y[i+1];  y[i+1] = stemp;
            	}
        	}
    	}
	}

    private static void getFiles(File folder, ArrayList<String> list,ArrayList<String> address,Hashtable HT) throws IOException {
        folder.setReadOnly();
        File[] files = folder.listFiles();
        for(int j = 0; j < files.length; j++) {
            if(files[j].isDirectory())
                getFiles(files[j], list,address, HT);
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
    				
            		list.add(filename);
            		address.add(path);
            		
            		HT.put(filename,filecategory);
            	}
            }
        }
    }//recursive traversing of files; does copying of files and hashing of filename to file category
    
    public void filter(float df,float idf)
    {
    	try
    	{
    		ts.setDFThresh(df);
    		ts.setIDFThresh(idf);
    		ts.filterTerms(trainingDocumentVector, HTrain);
    	}
    	catch(Exception e){}
    	
    }
    
    public void populate()
    {
    	try
    	{
    		HTrain = new Hashtable();
    		trainingDocumentVector = new ArrayList<String>();
    		trainingDocumentVector_address = new ArrayList<String>();
   			trainoutputDocumentVector = new ArrayList<String>();
   		
        	getFiles(trainfolder,trainingDocumentVector,trainingDocumentVector_address,HTrain);
        	
        	for(int i=0;i<trainingDocumentVector.size();i++)
	        	trainoutputDocumentVector.add("output_"+trainingDocumentVector.get(i));
	    
	    	HTest = new Hashtable();
    		testingDocumentVector = new ArrayList<String>();
    		testingDocumentVector_address = new ArrayList<String>();
    		testoutputDocumentVector = new ArrayList<String>();
    	
	    	getFiles(testfolder,testingDocumentVector,testingDocumentVector_address,HTest);	
	    
	    	for(int i=0;i<testingDocumentVector.size();i++)
		        testoutputDocumentVector.add("output_"+testingDocumentVector.get(i));	
    	}
    	catch(Exception e){}
    }	
    	
    public void stemTrainingSet()
    {
    	// Step 1 Part I: Computing the training set
        
        try
        {
        	File traindir = new File(train);
        	
	        if(!traindir.exists())
	        {
	        	boolean success = traindir.mkdir();
	   			if (success)
	      			System.out.println("Training directory created");
	    	}			
	        	
	        for(int i=0;i<trainingDocumentVector.size();i++)
	        { 	
	        	Process p1 = Runtime.getRuntime().exec("java StopListStemmer "+train+trainingDocumentVector_address.get(i));
	        	p1.waitFor();
	        }
	        	
	    	System.out.println("Training is completed for training set");
        }
        catch(Exception e){}
    }
    
    public void stemTestingSet()
    {
    	// Step 1 part II: Computing the testing set
    	
    	try
        {		
	        File testdir = new File(test);
	        
	    	if(!testdir.exists())
	    	{
	    		boolean success = testdir.mkdir();
	   			if (success)
	      			System.out.println("Testing directory created");
	    	}
	    			
		    for(int i=0;i<testingDocumentVector.size();i++)
		    {
		        Process p2 = Runtime.getRuntime().exec("java StopListStemmer "+test+testingDocumentVector_address.get(i));
		        p2.waitFor();
		    }
	    	
	    	System.out.println("Training is completed for testing set");
        }
        catch(Exception e){}
    }
    
    public void classify(int k)
    {
    	// Step 2: Performing KNN classification        	
        	
        Similarity s = new Similarity();
     			
        for(int i=0;i<testoutputDocumentVector.size();i++)
        {
        	String testdoc = "./test/"+testoutputDocumentVector.get(i);
        	
        	String []knn = new String[trainoutputDocumentVector.size()];
     		double []freq = new double[trainoutputDocumentVector.size()];
     				
        	for(int j=0;j<trainoutputDocumentVector.size();j++)
        	{
        		String traindoc = "./train/"+trainoutputDocumentVector.get(j);
        			
        		freq[j] = 0;
        		knn[j] = new String("");
        			
        		double v = s.computeSimilarity(testdoc,traindoc);
       
       			freq[j] = v;
      			knn[j] = traindoc.substring(15);
        	}	
        		
        	bubbleSort(freq,knn);
        	
        	int []count = new int[10];
        	int []freqOfCategory = new int[10];
        	String []categories = {"alt.atheism","comp.windows.x","misc.forsale",
        			                   "rec.autos","rec.motorcycles","rec.sport.baseball",
        			                   "sci.electronics","sci.med","talk.politics.misc",
        			                   "talk.religion.misc"};
        			                    
        	for(int h=0;h<k;h++)
        	{
        		String category = (String)HTrain.get((String)knn[h]);
        			
        		for(int a=0;a<10;a++)
        		{
        			String c = categories[a];
        			if(category.equals(c))
        			{
        				count[a]++;
        				freqOfCategory[a] += freq[h]; 
        			}//computing the total frequency of each category
        		}
        	}
        		
        	System.out.println();
        	
        	for(int a=0;a<k;a++)
        		System.out.println(knn[a]+" "+freq[a]);
        	
        	System.out.println();
        		
        	int max = -1;
        	int pos = -1;
        			
        	for(int a=0;a<10;a++)
        		if(count[a]> max)
        		{
        			max = freqOfCategory[a];
        			pos = a;
        		}
        			
        	String retrievedcategory=""; 
        	if(pos!=-1)
        		retrievedcategory = categories[pos];
        	String relevantcategory = (String)HTest.get((String)testdoc.substring(14));
        	
        	System.out.println(testdoc.substring(14)+" classified as "+retrievedcategory);
  			System.out.println("Test category is "+relevantcategory);
  			     	
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
        		System.out.print(EvaluationMatrix[i][j]+" ");
        	
        	System.out.println();
        }
        	
       	System.out.println("Finished classifying");

    }
    
    public String classify(String filetobeClassified,int k)
    {
    	// Step 2: Performing KNN classification        	
        	
        Similarity s = new Similarity();
		
		String [] array = null;	
        array = filetobeClassified.split("\\\\");
    	String filename = array[array.length - 1];
    	
    	try
    	{
    		Process p1 = Runtime.getRuntime().exec("java StopListStemmer \"\" " + filetobeClassified);
			p1.waitFor();
    	}
    	catch(Exception e){}
    	
        String testdoc = "output_" + filename;
        	
        String []knn = new String[trainoutputDocumentVector.size()];
     	double []freq = new double[trainoutputDocumentVector.size()];
     			
        for(int j=0;j<trainoutputDocumentVector.size();j++)
        {
        	String traindoc = "./train/"+trainoutputDocumentVector.get(j);
        			
        	freq[j] = 0;
        	knn[j] = new String("");			
        	double v = s.computeSimilarity(testdoc,traindoc);
       		freq[j] = v;
      		knn[j] = traindoc.substring(15);
        }	
        		
        bubbleSort(freq,knn);
        	
        int []count = new int[10];
        int []freqOfCategory = new int[10];
        String []categories = {"alt.atheism","comp.windows.x","misc.forsale",
        			           "rec.autos","rec.motorcycles","rec.sport.baseball",
        			           "sci.electronics","sci.med","talk.politics.misc",
        			           "talk.religion.misc"};
        			                    
        for(int h=0;h<k;h++)
        {
        	String category = (String)HTrain.get((String)knn[h]);
        			
        	for(int a=0;a<10;a++)
        	{
        		String c = categories[a];
        		if(category.equals(c))
        		{
        			count[a]++;
        			freqOfCategory[a] += freq[h]; 
        		}//computing the total frequency of each category
        	}
        }
        		
        System.out.println();
        	
        for(int a=0;a<k;a++)
        	System.out.println(knn[a]+" "+freq[a]);
        
        System.out.println();
        		
        int max = -1;
        int pos = -1;
        			
        for(int a=0;a<10;a++)
        	if(count[a]> max)
        	{
        		max = freqOfCategory[a];
        		pos = a;
        	}
        			
        String retrievedcategory=""; 
        if(pos!=-1)
        	retrievedcategory = categories[pos];
  
        System.out.println(filename+" classified as "+retrievedcategory);
  		
        return retrievedcategory;
    }
    
    public static void main(String[] args)
    {
        kNN knn = new kNN();
        Scanner scan = new Scanner(System.in);
     	
     	System.out.print("Enter the value of k:");
     	int k = scan.nextInt();
     	
     	System.out.print("Enter the value of df:");
     	float df = scan.nextFloat();
     	
     	System.out.print("Enter the value of idf:");
     	float idf = scan.nextFloat();
     	
     	knn.populate();
        knn.filter(df,idf);
        knn.stemTrainingSet();
        knn.stemTestingSet();
        knn.classify(k);
    }
}