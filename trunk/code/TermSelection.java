import java.io.*;
import java.io.File;
import java.util.*;

public class TermSelection
{
	public static String newline = System.getProperty("line.separator");
	
	public float DFThresh = 100;
	public float IDFThresh = 100;

	public void setDFThresh(float in)
	{
		DFThresh = in;
	}
	public void setIDFThresh(float in)
	{
		IDFThresh = in;
	}

	public TermSelection()
	{
	}


	public void filterTerms(ArrayList trainingDocumentVector, Hashtable HT)
	throws Exception
	{
		ArrayList filtered = new ArrayList();
		ArrayList trainoutputDocumentVector = new ArrayList();
		String filename;
		String category;
		File[] categorizedDoc = new File[10];
		File consolidatedDoc;

		//Pre concatenated files
		categorizedDoc[0] = new File("alt.atheism.txt");
		categorizedDoc[1] = new File("comp.windows.x.txt");
		categorizedDoc[2] = new File("misc.forsale.txt");
		categorizedDoc[3] = new File("rec.autos.txt");
		categorizedDoc[4] = new File("rec.motorcycles.txt");
		categorizedDoc[5] = new File("rec.sport.baseball.txt");
		categorizedDoc[6] = new File("sci.electronics.txt");
		categorizedDoc[7] = new File("sci.med.txt");
		categorizedDoc[8] = new File("talk.politics.misc.txt");
		categorizedDoc[9] = new File("talk.religion.misc.txt");

		consolidatedDoc = new File("all.txt");


		String dir = "../training_corpus/training_corpus/concats/";

		for (int i = 0; i < trainingDocumentVector.size(); i++)
		{
			filename = (String)trainingDocumentVector.get(i);
			File file = new File(filename);
			category = (String)HT.get(filename);

			//append document into certain category
			//categorizedDoc = appendDocCat(file, category, categorizedDoc);
		}
		

		for (int j = 0; j < 10; j++)
		{
			String exec = "";
			//TODO: run stemmer on the 10 files
			// run stemmer on categorizedDoc[j]
			
			Process p1 = Runtime.getRuntime().exec("java StopListStemmer \"\" " + dir + categorizedDoc[j].getName());
			p1.waitFor();
        	
        	
			//filter key terms based on df frequency on each stemmered file
			//returns a list of filtered keyterms(unwanted)
			File stemmered_file = new File("output_" + categorizedDoc[j].getName());
			filtered = filterDF(filtered, stemmered_file);

			//append document(consolidated category) into single consolidated file
			//consolidatedDoc = appendDoc(categorizedDoc[j], consolidatedDoc);
		}
		
		
		Process p2 = Runtime.getRuntime().exec("java StopListStemmer \"\" " + dir + "all.txt");
		p2.waitFor();

		//filter key terms of the single consolidated file
		//returns a list of filtered keyterms(unwanted)
		File all_File = new File("output_all.txt");
		filtered = filterIDF(filtered, all_File);

		//filter keyterms from original files
		//filterOriginal(filtered, trainoutputDocumentVector);
		addStopList(filtered);

		//remove created stemmered files
		for (int k = 0; k < 10; k++)
		{
			File stemmered_file = new File("output_" + categorizedDoc[k].getName());
			stemmered_file.delete();
		}

		all_File.delete();
	}

	public ArrayList filterDF(ArrayList filtered, File file)
	throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader(file));
		ArrayList file2string = new ArrayList();
		
		String input = null;
		while ((input = br.readLine()) != null)
			file2string.add(input);
		for (int i = 0; i < file2string.size(); i++)
		{
			StringTokenizer tk = new StringTokenizer((String)file2string.get(i));
			String text = tk.nextToken();
			int freq = Integer.parseInt(tk.nextToken());
			if (freq < DFThresh)
				filtered.add(text+" ");
		}
		br.close();
		return filtered;
	}

	public ArrayList filterIDF(ArrayList filtered, File file)
	throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader(file));
		ArrayList file2string = new ArrayList();
		//FileInputStream in = new FileInputStream(file);
		String input = null;
		while ((input = br.readLine()) != null)
			file2string.add(input);
		for (int i = 0; i < file2string.size(); i++)
		{
			StringTokenizer tk = new StringTokenizer((String)file2string.get(i));
			String text = tk.nextToken();
			int freq = Integer.parseInt(tk.nextToken());
			if (freq > IDFThresh)
				filtered.add(text+" ");
		}
		br.close();
		return filtered;
	}

	/*public void filterOriginal(ArrayList filtered, ArrayList trainoutputDocumentVector)
	{
	}*/
	
	public void addStopList(ArrayList filtered)
	throws Exception
	{
		FileInputStream fstream = new FileInputStream("stoplist.bak");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
		ArrayList file2string = new ArrayList();
		
		String input = null;
		if(br.ready())
		{
			while ((input = br.readLine()) != null)
				file2string.add(input);
		}
		br.close();
        in.close();
        fstream.close();
		
		FileOutputStream fstream2 = new FileOutputStream("stoplist.txt");
        DataOutputStream out = new DataOutputStream(fstream2);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
		
		for (int i = 0; i < file2string.size(); i++)
			bw.write((String)file2string.get(i)+newline);
			
		for (int i = 0; i < filtered.size(); i++)
			bw.write((String)filtered.get(i)+newline);
		
		bw.close();
		out.close();
		fstream2.close();
	}
	/*
	public File appendDoc(File file, File consolidatedFile)
	{
		return (new File("stub"));
	}*/
	/*
	public File[] appendDocCat(File file, String category, File[] categorizedFile)
	{
		return (new File[10]);
		int catIndex;
		
		switch (category)
		{
			case "alt.atheism":
				catIndex = 0;
				break;
			case "comp.windows.x":
				catIndex = 1;
				break;
			case "misc.forsale":
				catIndex = 2;
				break;
			case "rec.autos":
				catIndex = 3;
				break;
			case "rec.motorcycles":
				catIndex = 4;
				break;
			case "rec.sport.baseball":
				catIndex = 5;
				break;
			case "sci.electronics":
				catIndex = 6;
				break;
			case "sci.med":
				catIndex = 7;
				break;
			case "talk.politics.misc":
				catIndex = 8;
				break;
			case "talk.religion.misc":
				catIndex = 9;
				break;
			default:
				break;
		}
	}*/
}