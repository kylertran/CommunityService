package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class AddStudent extends JFrame
{
	//Initialize elements
	Image icon = Toolkit.getDefaultToolkit().getImage("src\\resources\\icon.png");  
	private static Font myFont = new Font("Arial", Font.BOLD, 19);
	private static JLabel firstNameText = new JLabel("First Name");
	private static JLabel lastNameText = new JLabel("Last Name");
	private static JLabel studentIDText = new JLabel("Student ID");
	private static JLabel gradeText = new JLabel("Grade");
	private static JLabel hoursText = new JLabel("Total Hours");
	private static JLabel numOfAwardsText = new JLabel("# of Awards");
	private static JLabel awardsText = new JLabel("Awards");
	
	private static JTextField firstNameField = new JTextField(10);
	private static JTextField lastNameField = new JTextField(10);
	private static JTextField studentIDField = new JTextField(10);
	private static JComboBox gradeField = new JComboBox();
	private static JTextField hoursField = new JTextField(10);
	private static SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0.0, 0.0, 100, 1.0);  
	private static JSpinner numOfAwardsField = new JSpinner(spinnerModel);
	private static JTextArea awardsTextArea = new JTextArea(4, 10);
	
	private static JLabel addButton = new JLabel(new ImageIcon("src\\resources\\add-small.png"));
	
	private static String firstName, lastName, stuID, grade, hours, numOfAwards, awards;
	
	private static FileWriter writer = null;
	
	//Run program
	public static void main(String[] args)
	{
		new AddStudent();
	}
	
	public AddStudent()
	{
		resetFields();
		
		gradeField.addItem("Select Grade   ");
		gradeField.addItem("9");
		gradeField.addItem("10");
		gradeField.addItem("11");
		gradeField.addItem("12");
		
		//Setup GUI
		JFrame frame = new JFrame("Add Student"); 
		setTitle("Add Student");	
		setIconImage(icon);
		getContentPane().setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		getContentPane().setLayout(new GridBagLayout());
		
		createComponents();
		
		setVisible(true);
	    setSize(250, 275);	
	    
	    //On 'Add' button press
        addButton.addMouseListener(new MouseAdapter() 
        {
        	@Override
		    public void mouseClicked(MouseEvent e) 
		    {	       
		    	if(gradeField.getSelectedIndex() != 0) //If a grade is selection
		    	{
			    	writeToFile();
			    	CommunityService.forceReload();
			        setVisible(false);
			        dispose();
		    	}
		    	else //Give error message
		    	{
		    		JOptionPane.showMessageDialog(frame, "Please select a grade", "Error", JOptionPane.ERROR_MESSAGE);
		    		addButton.removeMouseListener(this);
		    	}
		    }
	    });
	}
	
	//Shortcuts for adding various components
	public void addComponent(int gridy, int gridx, GridBagConstraints gbc, JTextField text)
	{
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		getContentPane().add(text, gbc);
	}
	public void addComponent(int gridy, int gridx, GridBagConstraints gbc, JLabel text)
	{
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		text.setFont(new Font("Courier New", Font.BOLD, 15));
		getContentPane().add(text, gbc);
	}
	public void addComponent(int gridy, int gridx, GridBagConstraints gbc, JButton text)
	{
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		getContentPane().add(text, gbc);
	}
	public void addComponent(int gridy, int gridx, GridBagConstraints gbc, JComboBox text)
	{
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		getContentPane().add(text, gbc);
	}
	public void addComponent(int gridy, int gridx, GridBagConstraints gbc, JSpinner text)
	{
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		getContentPane().add(text, gbc);
	}
	public void addComponent(int gridy, int gridx, GridBagConstraints gbc, JTextArea text)
	{
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		getContentPane().add(text, gbc);
	}
	
	//Add components
	public void createComponents()
	{
		GridBagConstraints myGrid = new GridBagConstraints();
		myGrid.anchor = GridBagConstraints.WEST;
		addComponent(1, 1, myGrid, firstNameText);
		addComponent(1, 2, myGrid, firstNameField);
		addComponent(2, 1, myGrid, lastNameText);
		addComponent(2, 2, myGrid, lastNameField);
		addComponent(3, 1, myGrid, studentIDText);
		addComponent(3, 2, myGrid, studentIDField);
		addComponent(4, 1, myGrid, gradeText);
		addComponent(4, 2, myGrid, gradeField);
		addComponent(5, 1, myGrid, hoursText);
		addComponent(5, 2, myGrid, hoursField);
		addComponent(6, 1, myGrid, numOfAwardsText);
		
		Component mySpinnerEditor = numOfAwardsField.getEditor();
		JFormattedTextField jftf = ((JSpinner.DefaultEditor) mySpinnerEditor).getTextField();
		jftf.setColumns(8);
		
		addComponent(6, 2, myGrid, numOfAwardsField);
		addComponent(7, 1, myGrid, awardsText);
		
		awardsTextArea.setLineWrap(true);
		awardsTextArea.setBackground(new Color(200,200,200));
		awardsTextArea.setBorder(BorderFactory.createBevelBorder(1));
		
		addComponent(7, 2, myGrid, awardsTextArea);
		addComponent(8, 2, myGrid, addButton);
	}
	
	//Add student to data file
	public void writeToFile()
	{
    	firstName = firstNameField.getText();
    	lastName = lastNameField.getText();
    	stuID = studentIDField.getText();
    	grade = (String) gradeField.getSelectedItem();
    	hours = hoursField.getText();
    	numOfAwards = Integer.toString((int) spinnerModel.getValue());
    	awards = awardsTextArea.getText();
	    {
	        try {	       	
	            FileWriter writer = new FileWriter("src\\resources\\Data.txt", true);
	            writer.write("\n" + firstName + ", " + lastName + ", " + stuID + ", " 
	            		+ grade + ", " + hours + ", " + numOfAwards);
	            writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        try {
	        	PrintWriter writer1 = new PrintWriter("src\\student_awards\\" + firstName + stuID + ".txt", "UTF-8");
	            FileWriter writer = new FileWriter("src\\student_awards\\" + firstName + stuID + ".txt", true);
	            writer.write(awards);
	            writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	//Reset data fields for next session
	public void resetFields()
	{
		firstNameField.setText("");
    	lastNameField.setText("");
    	studentIDField.setText("");
    	gradeField.removeAllItems();
    	hoursField.setText("");
    	firstName = "";
    	lastName = "";
    	stuID = "";
    	grade = "";
    	hours = "";
    	spinnerModel.setValue(0);
    	awardsTextArea.setText("");
    	addButton.removeMouseListener(null);
	}
}