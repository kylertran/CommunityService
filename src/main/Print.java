package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Print extends JFrame
{
	Image icon = Toolkit.getDefaultToolkit().getImage("src\\resources\\icon.png"); 
	static DefaultTableModel model = new DefaultTableModel();
	private static JTable table = new JTable();
	private static String firstName, lastName, stuID, grade, hours, numOfAwards;
	private static String awards = "";
	private static int commaCount = 0, commaAt;
	
	private static JComboBox studentsList = new JComboBox();
	
	private static JLabel studentFirstName = new JLabel("First Name: ");
	private static JLabel studentLastName = new JLabel("Last Name: ");
	private static JLabel studentID = new JLabel("Student ID: ");
	private static JLabel studentGrade = new JLabel("Grade: ");
	private static JLabel studentHours = new JLabel("Total Hours: ");
	private static JLabel studentNumAwards = new JLabel("# of Awards: ");
	private static JLabel studentAwards = new JLabel("Awards: ");
	
	private static JTextArea awardsTextArea = new JTextArea(7, 18);
	
	private static JButton saveToFile = new JButton("Save to file");
	private static JFileChooser fileChooser = new JFileChooser();
	
	public static void main(String[] args)
	{
		new Print();
	}
	
	public Print()
	{
		//Setup GUI
		JFrame frame = new JFrame("Print"); 
		setTitle("Print");	
		getContentPane().setBackground(Color.WHITE);
		setIconImage(icon);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		//Run setup methods
		createTable();
		addStudentsList();
		createComponents();
		
		setVisible(true);
		setSize(217, 320);
		
		studentsList.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if(studentsList.getSelectedIndex() != 0)
		    	{
		    		updateInfo();	
		    	}
		    	else
		    	{
		    		resetInfo();
		    	}
		    }
		});
		saveToFile.addActionListener(new ActionListener() //Save student info in a text file
		{
			public void actionPerformed(ActionEvent e)
			{
				if(studentsList.getSelectedIndex() != 0)
				{
					fileChooser.setDialogTitle("Save student to file");   
					fileChooser.setSelectedFile(new File((String) table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 0) + " - Community Service.txt"));
					
					int userSelection = fileChooser.showSaveDialog(frame);
					 
					if (userSelection == JFileChooser.APPROVE_OPTION) {
					    File fileToSave = fileChooser.getSelectedFile();
					    System.out.println("Saved file: " + fileToSave.getAbsolutePath());
					   
					    String data = (String) "First Name: " + table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 0) + "\n"
					    		+ (String) "Last Name:" + table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 1) + "\n"
					    		+ (String) "Student ID:" + table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 2) + "\n"
					    		+ (String) "Grade:" + table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 3) + "\n"
					    		+ (String) "Total Hours:" + table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 4) + "\n"
					    		+ (String) "# of Awards:" + table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 5) + "\n"
					    		+ "Awards: " + awardsTextArea.getText();
					    
					    FileOutputStream out;
						try
						{
							out = new FileOutputStream(fileToSave.getAbsolutePath());
						    try
							{
								out.write(data.getBytes());
							} catch (IOException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						    try
							{
								out.close();
							} catch (IOException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (FileNotFoundException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					     
					}
				}
		    	else
		    	{
		    		JOptionPane.showMessageDialog(frame, "Please select a student", "Error", JOptionPane.ERROR_MESSAGE); //Display error message
		    	}
			}
		});
	}
	
	//Update the GUI with the students info
	public void updateInfo()
	{
		studentFirstName.setText((String) "First Name: " + table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 0));
		studentLastName.setText((String) "Last Name:" + table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 1));
		studentID.setText((String) "Student ID:" + table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 2));
		studentGrade.setText((String) "Grade:" + table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 3));
		studentHours.setText((String) "Total Hours:" + table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 4));
		studentNumAwards.setText((String) "# of Awards:" + table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 5));
		
		awards = "";
    	Scanner sc = null;
        File file = new File("src\\student_awards\\" + table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 0) 
        		+ ((String) (table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 2))).trim() + ".txt"); 
		try
		{
			sc = new Scanner(file);
		} catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    	 
        int lineNumber = 1;
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            awards = awards += line;

        }
		
		if(Integer.valueOf(((String) table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 5)).trim()) != 0)
		{
			awardsTextArea.setText(awards);	
		}
		else
		{
			awardsTextArea.setText((String) "None");
		}
	}
	
	//Reset info
	public void resetInfo()
	{
		studentFirstName.setText("First Name: ");
		studentLastName.setText("Last Name:");
		studentID.setText("Student ID:");
		studentGrade.setText("Grade:");
		studentHours.setText("Total Hours:");
		studentNumAwards.setText("# of Awards:");
		studentAwards.setText("Awards:");
		awardsTextArea.setText("");
		awards = "";
	}
	
	//Add components
	public void createComponents()
	{	
		JPanel selectPanel = new JPanel(new GridBagLayout());
		JPanel infoPanel = new JPanel(new GridBagLayout());
		infoPanel.setLayout(new GridBagLayout());
		JPanel buttonPanel = new JPanel(new GridLayout(1,1));
		selectPanel.setBackground(Color.white);
		infoPanel.setBackground(Color.white);

		GridBagConstraints myGrid = new GridBagConstraints();
		myGrid.anchor = GridBagConstraints.WEST;
		
		selectPanel.add(studentsList);
		
		infoPanel.add(studentFirstName, myGrid);
		myGrid.gridy = 1;
		studentFirstName.setFont(new Font("Courier New", Font.BOLD, 16));
		infoPanel.add(studentLastName, myGrid);
		myGrid.gridy = 2;
		studentLastName.setFont(new Font("Courier New", Font.BOLD, 16));
		infoPanel.add(studentID, myGrid);
		myGrid.gridy = 3;
		studentID.setFont(new Font("Courier New", Font.BOLD, 16));
		infoPanel.add(studentHours, myGrid);
		myGrid.gridy = 4;
		studentHours.setFont(new Font("Courier New", Font.BOLD, 16));
		infoPanel.add(studentNumAwards, myGrid);
		myGrid.gridy = 5;
		studentNumAwards.setFont(new Font("Courier New", Font.BOLD, 16));
		infoPanel.add(studentAwards, myGrid);
		myGrid.gridy = 6;
		studentAwards.setFont(new Font("Courier New", Font.BOLD, 16));
		myGrid.gridy = 7;
		infoPanel.add(awardsTextArea, myGrid);
		
		awardsTextArea.setEditable(false);
		awardsTextArea.setLineWrap(true);
		awardsTextArea.setBackground(new Color(200,200,200));
		awardsTextArea.setBorder(BorderFactory.createBevelBorder(1));
		
		buttonPanel.add(saveToFile, BorderLayout.CENTER);
		
		add(selectPanel, BorderLayout.NORTH);
		add(infoPanel, BorderLayout.WEST);
		add(buttonPanel, BorderLayout.PAGE_END);
	}
	
	//Make list of students
	public static void addStudentsList()
	{
		studentsList.addItem("Select Student");
		for(int i = 0; i < table.getRowCount(); i++)
		{
			studentsList.addItem(table.getModel().getValueAt(i, 0) + "" + table.getModel().getValueAt(i, 1));
		}
	}
	
	//Create table to better utilize data
	public static void createTable()
	{
    	model.setRowCount(0);
    	model.setColumnCount(0);
		table = new JTable(model);
		model.addColumn("First Name");
		model.addColumn("Last Name");
		model.addColumn("Student ID");
		model.addColumn("Grade");
		model.addColumn("Hours");
		model.addColumn("# of Awards");
		Scanner sc = null;
	    File file = new File("src\\resources\\Data.txt"); 
		try
		{
			sc = new Scanner(file);
		} catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    	 
        int lineNumber = 1;
        while(sc.hasNextLine()){
        	commaAt = 0;
        	commaCount = 0;
            String line = sc.nextLine();
            for(int i = 0; i < line.length(); i++)
            {
            	if(line.charAt(i) == ',')
            	{
            		if(commaCount == 0)
            		{
                		firstName = line.substring(0, i);
                		commaAt = i + 1;
                		commaCount++;
            		}
            		else if(commaCount == 1)
            		{
            			lastName = line.substring(commaAt, i);
            			commaAt = i + 1;
            			commaCount++;
            		}
            		else if(commaCount == 2)
            		{
            			stuID = line.substring(commaAt, i);
            			commaAt = i + 1;
            			commaCount++;
            		}
            		else if(commaCount == 3)
            		{
            			grade = line.substring(commaAt, i);
            			commaAt = i + 1;
            			commaCount++;
            		}
            		else if(commaCount == 4)
            		{
            			hours = line.substring(commaAt, i);
            			commaAt = i + 1;
            			commaCount++;
            		}
            	}
            	numOfAwards = line.substring(commaAt);
            }
            if(!line.isEmpty())
            {
	            model.addRow(new Object[]{firstName, lastName, stuID, grade, hours, numOfAwards});
	            lineNumber++;
            }
        }
	}
}