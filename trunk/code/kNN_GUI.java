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
    JPanel panel1, panel2, mainPanel;
    Container container;
    String disp = "";
    String filename = "";
    File file = null;
    int success = -1;
    private java.util.List<String> results;
    kNN kc;
    JButton jbnOpen, jbnSearch;
    JButton[] button;
    
	public kNN_GUI() {
		super("KNN text classification");

        kc = new kNN();
        kc.train();

        container = getContentPane();
        container.setLayout(new FlowLayout());

        jlbFile = new JLabel("File:");
        container.add(jlbFile);
        
         
        jtfFile = new JTextField("", 60);
        jtfFile.setEditable(false);
        container.add(jtfFile);

        jbnOpen = new JButton("Open");
        container.add(jbnOpen);

        jlbClassify = new JLabel("Classifed as:");
        container.add(jlbClassify);

        jtfClassify = new JTextField("", 57);
        jtfClassify.setEditable(false);
        container.add(jtfClassify);

        jbnSearch = new JButton("Search");
        container.add(jbnSearch);

        mainPanel = new JPanel(); 
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));


        panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        panel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel1.setAlignmentY(Component.CENTER_ALIGNMENT);

        panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel2.setAlignmentY(Component.CENTER_ALIGNMENT);

        mainPanel.add(panel1);
        mainPanel.add(panel2);
        container.add(mainPanel);

        jbnOpen.addActionListener(this);
        jbnSearch.addActionListener(this);

        this.setSize(850, 100); 
        // pack();
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
                filename = jtfFile.getText();
               
                jtfClassify.setText(kc.classify(filename));
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
