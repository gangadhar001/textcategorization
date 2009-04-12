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
    JPanel mainPanel;
    JTextArea jtaText;
    Container container;
    String filename = "";
    File file;
    
    JButton jbnOpen, jbnSearch;
    
    //stub: DT instance
    
	public DT_GUI() {
		super("DT Text Classification");

        container = getContentPane();
        container.setLayout(new FlowLayout());

        jlbFile = new JLabel("File:");
        container.add(jlbFile);
        
        jtfFile = new JTextField("", 65);
        jtfFile.setEditable(false);
        container.add(jtfFile);

        jbnOpen = new JButton("Open");
        container.add(jbnOpen);
        
        jlbText = new JLabel("Text:");
        container.add(jlbText);
        
        jtaText = new JTextArea(35,70);
        container.add(jtaText);
        
		jlbClassify = new JLabel("Classifed as:");
        container.add(jlbClassify);

        jtfClassify = new JTextField("", 57);
        jtfClassify.setEditable(false);
        container.add(jtfClassify);
        
        jbnSearch = new JButton("Classify");
        container.add(jbnSearch);

        mainPanel = new JPanel(); 
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        jbnOpen.addActionListener(this);
        jbnSearch.addActionListener(this);

        this.setSize(850, 700); 
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
            		filename = jtfFile.getText();
                	//stub : classify method
            	}
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }  
        } 
        
    }

    public static void main(String[] args) {
        DT_GUI app = new DT_GUI();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}