import java.io.*;
import java.util.*;



public class Similarity
{
	
	/*
	 * Description: Compute the Similarity of two file
	 * In:			path of two files
	 * Return:		the Similarity of two file	 * 
	 * */
	public double computeSimilarity(String fileOne, String fileTwo)
	{
		double ret;
		
		Hashtable union = new Hashtable();//originally, it is a HashSet
		//HashSet<String> union = new HashSet<String>();
		HashSet<String> intersection = new HashSet<String>();
		
		FileInputStream fstreamOne;
		DataInputStream inOne;
		BufferedReader brOne;
		String wordOne;
		String wordI;
		FileInputStream fstreamTwo;
		DataInputStream inTwo;
		BufferedReader brTwo;
		String wordTwo;
		String wordJ;
		
		Integer numerator = 0;
		Integer denominator = 0;
		Integer i=0,j=0;
		
		try
		{
			fstreamOne = new FileInputStream(fileOne);
			inOne = new DataInputStream(fstreamOne);
			brOne = new BufferedReader(new InputStreamReader(inOne));
			
			while ((wordOne = brOne.readLine()) != null)
			{
				wordI = wordOne.substring(wordOne.indexOf("\t")+1);
				wordOne = wordOne.substring(0,wordOne.indexOf("\t"));
				union.put(wordOne,Integer.valueOf(wordI));
				
				i=i+Integer.valueOf(wordI);	//sum of all the term frequencies in fileOne			
			}
			
			fstreamTwo = new FileInputStream(fileTwo);
			inTwo = new DataInputStream(fstreamTwo);
			brTwo = new BufferedReader(new InputStreamReader(inTwo));
			while ((wordTwo = brTwo.readLine()) != null && wordTwo.indexOf("\t") != -1)
			{	
				wordJ = wordTwo.substring(wordTwo.indexOf("\t")+1);			
				wordTwo = wordTwo.substring(0,wordTwo.indexOf("\t"));
				
				j=j+Integer.valueOf(wordJ);	//sum of all the term frequencies in fileTwo
						
				if (union.containsKey(wordTwo))
				{
					intersection.add(wordTwo);
					Integer WI = (Integer)union.get((String)wordTwo);  
					Integer WJ = Integer.valueOf(wordJ);	
					numerator=numerator+ (WI*WJ); 
					//sum of dot products of term frequencies of 
					//terms that exist in fileOne AND fileTwo
				}
				else
				{
					union.put(wordTwo,Integer.valueOf(wordJ));
					//union.add(wordTwo);
				}
			}			
			
			denominator = i*j;
			
			brOne.close();
			inOne.close();
			fstreamOne.close();
			brTwo.close();
			inTwo.close();
			fstreamTwo.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("file you put do not found");
		}
		catch (IOException e)
		{			
			e.printStackTrace();
		}
		
		ret = (double)intersection.size()/union.size(); //intersection/union similarity
		//ret = (double)numerator/denominator; //cosine similarity measure
		return ret;
	}

	public static void main(String[] args)
	{
		Similarity s = new Similarity();
		
		double v = s.computeSimilarity("C:/Users/User/Desktop/textcategorization/code/../testing_corpus/testing_corpus/alt.atheism/53661","C:/Users/User/Desktop/textcategorization/code/../testing_corpus/testing_corpus/alt.atheism/53661" );
		System.out.println(v);		
	}

}
