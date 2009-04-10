/**
 * @(#)FeatureExtraction.java
 *
 *
 * @author 
 * @version 1.00 2009/4/10
 */

import java.io.*;
import java.util.*;

public class FeatureExtraction {

    public FeatureExtraction() {
    }
    
    public void extract(String fileOne)
	{	
		ArrayList<String> terms = new ArrayList<String>();
		ArrayList<String> freq = new ArrayList<String>();
		
		FileInputStream fstreamOne;
		DataInputStream inOne;
		BufferedReader brOne;
		String wordOne;
		String wordI;
		
		try
		{
			fstreamOne = new FileInputStream(fileOne);
			inOne = new DataInputStream(fstreamOne);
			brOne = new BufferedReader(new InputStreamReader(inOne));
			
			while ((wordOne = brOne.readLine()) != null)
			{
				wordI = wordOne.substring(wordOne.indexOf("\t")+1);
				wordOne = wordOne.substring(0,wordOne.indexOf("\t"));
				if(Integer.valueOf(wordI)>5)
				{
					terms.add(wordOne);	
					freq.add(wordI);		
				}	
			}
				
			brOne.close();
			inOne.close();
			fstreamOne.close();
			
			File f = new File(fileOne);
			if(f.delete())
			{
				if(f.createNewFile())
				{
					FileWriter fstream = new FileWriter(fileOne);
		        	BufferedWriter out = new BufferedWriter(fstream);
		    	
		    		for(int i=0;i<terms.size();i++)
		    			out.write(terms.get(i)+"\t"+freq.get(i)+"\n");
		    		
		    		out.close();
		    		fstream.close();
				}
				else
					System.out.println(fileOne);
			}
			else
				System.out.println(fileOne);
			f = null;
		}
		catch(Exception e){}
	}
}