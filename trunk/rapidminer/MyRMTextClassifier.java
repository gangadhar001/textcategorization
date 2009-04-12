import java.io.File;
import java.io.IOException;

import com.rapidminer.RapidMiner;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.Model;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorCreationException;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.tools.OperatorService;

public class MyRMTextClassifier {

	private OperatorChain wvtoolOperator;
	private Operator modelApplier;
	private Model model;

	public MyRMTextClassifier(File modelFile, File wordListFile)
			throws IOException, OperatorCreationException, OperatorException {

		RapidMiner.init(false, false, false, true);

		// Create the text input operator and set the path to the word list you stored using Rapid Miner
		// As there is only a single text, we use the SingleTextInput operator
		wvtoolOperator = (OperatorChain) OperatorService
				.createOperator("SingleTextInput");
		wvtoolOperator.setParameter("input_word_list", wordListFile
				.getAbsolutePath());
		wvtoolOperator.setParameter("default_content_language", "english");

		// Add additional processing steps.
		// Note the setup must be same as the one you used when creating the classification model
		wvtoolOperator.addOperator(OperatorService
				.createOperator("StringTokenizer"));
		wvtoolOperator.addOperator(OperatorService
				.createOperator("EnglishStopwordFilter"));
		wvtoolOperator.addOperator(OperatorService
				.createOperator("TokenLengthFilter"));
		wvtoolOperator.addOperator(OperatorService
				.createOperator("SnowballStemmer"));

		// Create the model applier
		modelApplier = OperatorService.createOperator("ModelApplier");

		// Load the model into a field of the class
		Operator modelLoader = OperatorService.createOperator("ModelLoader");
		modelLoader.setParameter("model_file", modelFile.getAbsolutePath());
		IOContainer container = modelLoader.apply(new IOContainer());
		model = container.get(Model.class);

	}

	public String apply(String text) throws OperatorException {

		// Set the text
		wvtoolOperator.setParameter("text", text);

		// Call the text input operator
		IOContainer container = wvtoolOperator.apply(new IOContainer(model));

		// Call the model applier (the model was added already before calling the text input)
		container = modelApplier.apply(container);

		// Obtain the example set from the io container. It contains only a single example with our text in it.
		ExampleSet eset = container.get(ExampleSet.class);
		Example e = eset.iterator().next();

		// Compare the predicted label with the positive label
		return eset.getAttributes().getPredictedLabel().getMapping().mapIndex((int) e.getPredictedLabel());

	}

	public static void main(String args[]) throws Exception {
		
		// Create a text classifier  
		MyRMTextClassifier tr = new MyRMTextClassifier(
				new File(
						"w-j48bin.mod"),
				new File(
						"w-j48words.list"));

		// Call the classifier with texts
		System.out.println("Test1:" + tr.apply("../testing_corpus/testing_corpus/comp.windows.x/67059"));
		System.out.println("Test2:" + tr.apply("I like to play baseball! :)"));

	}

}