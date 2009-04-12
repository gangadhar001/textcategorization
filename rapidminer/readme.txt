To compile:
javac -cp rapidminer.jar MyRMTextClassifier.java 

To run:

// Moved the plugins into the lib folder so that the later command can be used
java -cp rapidminer.jar:plugins/rapidminer-text-4.4.jar:. -Drapidminer.operators.additional=lib/plugins/operators.xml -Drapidminer.home=. MyRMTextClassifier

Or:
java -cp rapidminer.jar:. -Drapidminer.home=. MyRMTextClassifier