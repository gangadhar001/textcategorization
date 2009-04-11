import java.io.*;
import java.io.File;
import java.util.*;

public class TermSelection
{
	public float DFThresh;
	public float IDFThresh;
	public ArrayList consolidatedDocuments = new ArrayList();

	public TermSelection()
	{
	}


	public void filterTerms(ArrayList trainoutputDocumentVector, Hashtable HT)
	{
		ArrayList filtered;
		String filename;
		String category;

		for (int i = 0; i < trainoutputDocumentVector.size(); i++)
		{
			filename = (String)trainoutputDocumentVector.get(i);
			File file = new File(filename);
			category = (String)HT.get(filename);

			//insert document into certain category
			insertDoc(file, category);
			
		}

		for (int j = 0; j < 10; j++)
		{
			filename = (String)consolidatedDocuments.get(j);
			File file = new File(filename);

			//filter key terms based on df frequency
			//returns a list of filtered keyterms(unwanted)
			filtered = filterDF(file);

			//filter keyterms from original files
			filterOriginal(filtered);

			//insert document(consolidated category) into single consolidated file
			insertDoc(file);

		}

		//filter key terms of the single consolidated file
		//returns a list of filtered keyterms(unwanted)
		filtered = filterIDF();

		//filter keyterms from original files
		filterOriginal(filtered);
	}

	public ArrayList filterDF(File file)
	{
		return (new ArrayList());
	}

	public ArrayList filterIDF()
	{
		return (new ArrayList());
	}

	public void filterOriginal(ArrayList filtered)
	{
	}

	public void insertDoc(File file)
	{
	}

	public void insertDoc(File file, String category)
	{
		/*int catIndex;
		
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
		}*/
	}
}