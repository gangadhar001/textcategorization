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
		
		HashSet<String> union = new HashSet<String>();
		HashSet<String> intersection = new HashSet<String>();
		
		FileInputStream fstreamOne;
		DataInputStream inOne;
		BufferedReader brOne;
		String wordOne;
		FileInputStream fstreamTwo;
		DataInputStream inTwo;
		BufferedReader brTwo;
		String wordTwo;
		
		try
		{
			fstreamOne = new FileInputStream(fileOne);
			inOne = new DataInputStream(fstreamOne);
			brOne = new BufferedReader(new InputStreamReader(inOne));
			while ((wordOne = brOne.readLine()) != null)
			{
				wordOne = wordOne.substring(0,wordOne.indexOf("\t"));	
				union.add(wordOne);				
			}
			
			fstreamTwo = new FileInputStream(fileTwo);
			inTwo = new DataInputStream(fstreamTwo);
			brTwo = new BufferedReader(new InputStreamReader(inTwo));
			while ((wordTwo = brTwo.readLine()) != null && wordTwo.indexOf("\t") != -1)
			{				
				wordTwo = wordTwo.substring(0,wordTwo.indexOf("\t"));	
				if (union.contains(wordTwo))
				{
					intersection.add(wordTwo);
				}
				else
				{
					union.add(wordTwo);
				}
			}			
			
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
		System.out.println(intersection.size());
		System.out.println(union.size());
		ret = (double)intersection.size()/union.size();
		return ret;
	}

	public static void main(String[] args)
	{
		Similarity s = new Similarity();
		double v = s.computeSimilarity("output_1.txt","output_2.txt" );
		System.out.println(v);		
	}

}
