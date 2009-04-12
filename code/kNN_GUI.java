/**
 * @(#)kNN_GUI.java
 *
 *
 * @author 
 * @version 1.00 2009/4/11
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;       

public class kNN_GUI extends JFrame implements ActionListener{
	
	JTextField jtfClassify, jtfFile;
    JLabel jlbClassify, jlbFile;
    JLabel jlbk, jlbdf,jlbidf;
    JTextField jtfk, jtfdf,jtfidf;
    JPanel panel1, panel2, mainPanel;
    Container container;
    String filename = "";
    File file;
    
    kNN kc;
    
    JButton jbnOpen, jbnSearch,jbnTrain;
    //JButton[] button;
    
	public kNN_GUI() {
		super("KNN text classification");

        kc = new kNN();

        container = getContentPane();
        container.setLayout(new FlowLayout());

        jlbFile = new JLabel("File:");
        container.add(jlbFile);
        
         
        jtfFile = new JTextField("", 65);
        jtfFile.setEditable(false);
        container.add(jtfFile);

        jbnOpen = new JButton("Open");
        container.add(jbnOpen);

		jlbk = new JLabel("K:");
        container.add(jlbk);

        jtfk = new JTextField("", 20);
        jtfk.setEditable(true);
        jtfk.setText("3");
        container.add(jtfk);
        
        jlbdf = new JLabel("DF:");
        container.add(jlbdf);

        jtfdf = new JTextField("", 20);
        jtfdf.setEditable(true);
        jtfdf.setText("0");
        container.add(jtfdf);
        
        jlbidf = new JLabel("IDF:");
        container.add(jlbidf);

        jtfidf = new JTextField("", 20);
        jtfidf.setEditable(true);
        jtfidf.setText("9999");
        container.add(jtfidf);
        
        jbnTrain = new JButton("Train");
        container.add(jbnTrain);
        
		jlbClassify = new JLabel("Classifed as:");
        container.add(jlbClassify);

        jtfClassify = new JTextField("", 57);
        jtfClassify.setEditable(false);
        container.add(jtfClassify);
        
        jbnSearch = new JButton("Search");
        container.add(jbnSearch);

        mainPanel = new JPanel(); 
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        jbnOpen.addActionListener(this);
        jbnSearch.addActionListener(this);
        jbnTrain.addActionListener(this);

        this.setSize(850, 130); 
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

            try {
            	if(!jtfFile.getText().equals(""))
            	{
            		filename = jtfFile.getText();
                	jtfClassify.setText(kc.classify(filename,new Integer(jtfk.getText())));
            	}
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }  
        } else if (e.getSource() == jbnTrain) {

            try {
                kc.setThreshHolds(new Float(jtfdf.getText()),new Float(jtfidf.getText()));
                kc.train1();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
            
        } 
        
    }

    public static void main(String[] args) {
        kNN_GUI app = new kNN_GUI();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
