/**
 * To compile:
 * javac -cp ../rapidminer/rapidminer.jar:. DT_GUI.java
 * To run:
 * java -cp ../rapidminer/rapidminer.jar:. -Drapidminer.home=../rapidminer/ DT_GUI
 */

/**
 * @(#)DT_GUI.java
 *
 *
 * @author 
 * @version 1.00 2009/4/12
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;       

public class DT_GUI extends JFrame implements ActionListener{
	
	JTextField jtfClassify, jtfFile;
    JLabel jlbClassify, jlbFile, jlbText;
    JPanel mainPanel, topPanel, centerPanel, bottomPanel;
    JTextArea jtaText;
    String toprocess = "", theresult = "";
    File file;
    static MyRMTextClassifier theclassifier;
    
    JButton jbnOpen, jbnSearch;
    
    //stub: DT instance
    
	public DT_GUI() {
		super("DT Text Classification");
		
		    mainPanel = new JPanel(new BorderLayout());
        topPanel = new JPanel(new BorderLayout());
        
        jlbFile = new JLabel("File:");
        topPanel.add(jlbFile, BorderLayout.WEST);
        
        jtfFile = new JTextField("");
        //jtfFile.setEditable(false);
        topPanel.add(jtfFile, BorderLayout.CENTER);

        jbnOpen = new JButton("Open");
        topPanel.add(jbnOpen, BorderLayout.EAST);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        centerPanel = new JPanel(new BorderLayout(5,5));
        jlbText = new JLabel("Text:");
        centerPanel.add(jlbText, BorderLayout.NORTH);
        
        jtaText = new JTextArea();
        centerPanel.add(jtaText, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        bottomPanel = new JPanel(new BorderLayout());
        
		jlbClassify = new JLabel("Classifed as:");
        bottomPanel.add(jlbClassify, BorderLayout.WEST);

        jtfClassify = new JTextField("", 57);
        jtfClassify.setEditable(false);
        bottomPanel.add(jtfClassify, BorderLayout.CENTER);
        
        jbnSearch = new JButton("Classify");
        bottomPanel.add(jbnSearch, BorderLayout.SOUTH);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        setContentPane(mainPanel);
        jbnOpen.addActionListener(this);
        jbnSearch.addActionListener(this);

        this.setSize(600, 400); 
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == jbnOpen) {

            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();

                //This is where a real application would open the file.
                jtfFile.setText(file.getAbsolutePath());
               
            }
        } else if (e.getSource() == jbnSearch) {

            try 
            {
            	if(!jtfFile.getText().equals(""))
            	{
            		toprocess = jtfFile.getText();
                	//stub : classify method
            	} else {
            	  // else process the text in the textbox
            	  toprocess = jtaText.getText();
            	}
            	theresult = theclassifier.apply(toprocess);
            	jtfClassify.setText(theresult);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }  
        } 
        
    }

    public static void main(String[] args) {
        DT_GUI app = new DT_GUI();
        try {
        theclassifier = new MyRMTextClassifier(
    				new File(
    						"../rapidminer/w-j48bin.mod"),
    				new File(
    						"../rapidminer/w-j48words.list"));
    		} catch (Exception ex) {
    		  JOptionPane.showMessageDialog(app, ex.getMessage());
    		}
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}