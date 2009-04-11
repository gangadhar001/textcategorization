import java.io.*;
import java.io.File;
import java.util.*;

public class TermSelection
{
	public float DFThresh = 0;
	public float IDFThresh = 9999;
	public File MasterFile;
	public String MASTERFILENAME = "database";

	public TermSelection()
	{
	}


	public void filterTerms(ArrayList trainingDocumentVector, Hashtable HT)
	{
		ArrayList filtered;
		ArrayList trainoutputDocumentVector = new ArrayList();
		File consolidatedDoc = new File(MASTERFILENAME);
		String filename;
		String category;
		File[] categorizedDoc = new File[10];
		for (int i = 0; i < trainingDocumentVector.size(); i++)
		{
			filename = (String)trainingDocumentVector.get(i);
			File file = new File(filename);
			category = (String)HT.get(filename);

			//append document into certain category
			categorizedDoc = appendDocCat(file, category, categorizedDoc);
			
		}
		

		for (int j = 0; j < 10; j++)
		{
			
			//TODO: run stemmer on the 10 files
			// run stemmer on categorizedDoc[j]

			//filter key terms based on df frequency on each stemmered file
			//returns a list of filtered keyterms(unwanted)
			File stemmered_file = new File(categorizedDoc[j].getName() + "_output");
			filtered = filterDF(stemmered_file);

			//append document(consolidated category) into single consolidated file
			consolidatedDoc = appendDoc(categorizedDoc[j], consolidatedDoc);
		}

		//filter key terms of the single consolidated file
		//returns a list of filtered keyterms(unwanted)
		filtered = filterIDF();

		//filter keyterms from original files
		filterOriginal(filtered, trainoutputDocumentVector);
	}

	public ArrayList filterDF(File file)
	{
		return (new ArrayList());
	}

	public ArrayList filterIDF()
	{
		return (new ArrayList());
	}

	public void filterOriginal(ArrayList filtered, ArrayList trainoutputDocumentVector)
	{
	}

	public File appendDoc(File file, File consolidatedFile)
	{
		return (new File("stub"));
	}

	public File[] appendDocCat(File file, String category, File[] categorizedFile)
	{
		return (new File[10]);
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