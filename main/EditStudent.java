package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class EditStudent extends JFrame
{
	Image icon = Toolkit.getDefaultToolkit().getImage("src\\resources\\icon.png");  
	static DefaultTableModel model = new DefaultTableModel();
	private static JTable table = new JTable();
	private static String firstName, lastName, stuID, grade, hours;
	private static int commaCount = 0, commaAt;
	
	private static JLabel nameLabel = new JLabel("Student");
	private static JLabel idLabel = new JLabel("Student ID");
	private static JLabel gradeLabel = new JLabel("Grade");
	private static JLabel hoursLabel = new JLabel("Total Hours");
	private static JLabel numOfAwardsText = new JLabel("# of Awards");
	private static JLabel awardsText = new JLabel("Awards");
	
	private static JTextField idField = new JTextField(10);
	private static JComboBox gradeField = new JComboBox();
	private static JTextField hoursField = new JTextField(10);
	private static SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0.0, 0.0, 100, 1.0);  
	private static JSpinner numOfAwardsField = new JSpinner(spinnerModel);
	private static JTextArea awardsTextArea = new JTextArea(4, 10);
	
	private static JComboBox studentsList = new JComboBox();
	
	private static JLabel saveEdit = new JLabel(new ImageIcon("src\\resources\\save-small.png"));
	private static JLabel deleteStudent = new JLabel(new ImageIcon("src\\resources\\delete-small.png"));
	
	private static String newLine;
	
	public static void main(String[] args)
	{
		new EditStudent();
	}
	
	public EditStudent()
	{
		//Reset GUI from last session
		resetFields();
		gradeField.addItem("Select Grade   ");
		gradeField.addItem("9");
		gradeField.addItem("10");
		gradeField.addItem("11");
		gradeField.addItem("12");
		
		//Setup GUI
		JFrame frame = new JFrame("Edit Student"); 
		setTitle("Edit Student");	
		getContentPane().setBackground(Color.WHITE);
		setIconImage(icon);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints myGrid = new GridBagConstraints();
		
		studentsList.removeAllItems();
		
		//Run setup methods
		createTable();
		addStudentsList();
		createComponents();
		
		setVisible(true);
		setSize(248, 268);
		
		//On "Save" button press
        saveEdit.addMouseListener(new MouseAdapter() 
        {
        	@Override
		    public void mouseClicked(MouseEvent e) 
		    {	
		    	if(studentsList.getSelectedIndex() != 0)
		    	{
			    	if(gradeField.getSelectedIndex() != 0)
				    {
			    		//Rename awards file if student ID changed
			    		if(idField.getText() != table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 2))
			    		{
			    			System.out.println(idField.getText() + " " + table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 2));
			    			File f1 = new File("src\\student_awards\\" + firstName + table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 2) + ".txt");
			    			File f2 = new File("src\\student_awards\\" + firstName + idField.getText() + ".txt");
			    			boolean c = f1.renameTo(f2);
			    		}
			    		
			    		int spaceAt = ((String) studentsList.getSelectedItem()).indexOf(' ');
			    		
				    	System.out.println(studentsList.getSelectedIndex());
				    	
				    	String numAwards = Integer.toString((int) spinnerModel.getValue());
				    	
				    	int studentLine = studentsList.getSelectedIndex() - 1;
				    	
				    	firstName = ((String) studentsList.getSelectedItem()).substring(0, spaceAt);
				    	lastName = ((String) studentsList.getSelectedItem()).substring(spaceAt);
				    	
				    	stuID = idField.getText();
				    	grade = (String) gradeField.getSelectedItem();
				    	hours = hoursField.getText();
				    	
				    	newLine = firstName + "," + lastName + ", " + stuID + ", " + grade + ", " + hours + ", " + numAwards;
				    	
				    	//Update Data.txt
				    	File a = new File("src\\resources\\Data.txt");
				    	 
					    try{	
					        List<String> lines = Files.readAllLines(a.toPath());
					        lines.set(studentLine, newLine);
					        Files.write(a.toPath(), lines);
					    } 
					    catch (IOException f){
					        f.printStackTrace();
					    }
					    
				        //Delete old awards file
				        File file = new File("src\\student_awards\\" + firstName + table.getModel().getValueAt(studentsList.getSelectedIndex() - 1, 2) + ".txt");
				        try
						{
							boolean result = Files.deleteIfExists(file.toPath());
						} catch (IOException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					    
				        //Update awards file
					    String awards = awardsTextArea.getText();
					    
					    File b = new File("src\\student_awards\\" + firstName + stuID + ".txt");
					    try
						{
							b.createNewFile();
							FileOutputStream oFile = new FileOutputStream(b, false); 
						} catch (IOException e2)
						{
							// TODO Auto-generated catch block
							e2.printStackTrace();
						} // if file already exists will do nothing
					    
					    System.out.println("src\\student_awards\\" + firstName + stuID + ".txt");
					    
					    //Clear file before writing
					    FileWriter fwOb = null;
						try
						{
							fwOb = new FileWriter("src\\student_awards\\" + firstName + stuID + ".txt", false);
						} catch (IOException e2)
						{
							// TODO Auto-generated catch block
							e2.printStackTrace();
						} 
					    PrintWriter pwOb = new PrintWriter(fwOb, false);
					    pwOb.flush();
					    pwOb.close();
					    try
						{
							fwOb.close();
						} catch (IOException e2)
						{
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					    
					    //Write in new awards
				        try 
				        {
					        FileWriter myWriter = new FileWriter("src\\student_awards\\" + firstName + stuID + ".txt");
					        myWriter.write(awards);
					        myWriter.close();
					        System.out.println("Successfully wrote to the file.");
				        } 
				        catch (IOException e1) 
				        {
				        System.out.println("An error occurred.");
				        e1.printStackTrace();
				        }
					   
				        
				    	CommunityService.forceReload();
				        setVisible(false);
				        dispose(); 
				    }
			    	else
			    	{
			    		JOptionPane.showMessageDialog(frame, "Please select a grade", "Error", JOptionPane.ERROR_MESSAGE);
			    		saveEdit.removeMouseListener(this);
			    	}
		    	}
		    	else
		    	{
		    		JOptionPane.showMessageDialog(frame, "Please select a student", "Error", JOptionPane.ERROR_MESSAGE);
		    		saveEdit.removeMouseListener(this);
		    	}
		    }

        });
        //On "Delete" button press
        deleteStudent.addMouseListener(new MouseAdapter() 
        {
        	@Override
		    public void mouseClicked(MouseEvent e) 
		    {	
		    	if(studentsList.getSelectedIndex() != 0)
		    	{
		    			//Remove line from Data.txt
				    	File a = new File("src\\resources\\Data.txt");
				    	{
					        try 
					        {	
					        	List<String> lines = Files.readAllLines(a.toPath());
					        	
					        	lines.set(studentsList.getSelectedIndex() - 1, "");
					        	for(int i = studentsList.getSelectedIndex() - 1; i < model.getRowCount() - 1; i++)
					        	{
					        		lines.set(i, lines.get(i+1));
					        	}
					        	lines.set(model.getRowCount() - 1, "");
					        	Files.write(a.toPath(), lines);
					        	
						    	CommunityService.forceReload();
						        setVisible(false);
						        dispose(); 
					        } 
					        catch (IOException f) 
					        {
					            f.printStackTrace();
					        }
				    	}
				    	//Delete personal awards file
				    	File b = new File("src\\student_awards\\" + firstName + stuID.trim() + ".txt");
				    	try
						{
							boolean result = Files.deleteIfExists(b.toPath());
						} catch (IOException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		    	}
		    	else
		    	{
		    		JOptionPane.showMessageDialog(frame, "Please select a student", "Error", JOptionPane.ERROR_MESSAGE);
		    		deleteStudent.removeMouseListener(this);
		    	}
			}
        });
	}
	
	//Reset data fields for next session
	public void resetFields()
	{
    	idField.setText("");
    	gradeField.removeAllItems();
    	hoursField.setText("");
    	firstName = "";
    	lastName = "";
    	stuID = "";
    	grade = "";
    	hours = "";
    	spinnerModel.setValue(0);
    	awardsTextArea.setText("");
	}
	
	//Get list of students from data
	public static void addStudentsList()
	{
		studentsList.addItem("Select Student");
		for(int i = 0; i < table.getRowCount(); i++)
		{
			studentsList.addItem(table.getModel().getValueAt(i, 0) + "" + table.getModel().getValueAt(i, 1));
		}
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
	public void addComponent(int gridy, int gridx, GridBagConstraints gbc, JComboBox text)
	{
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		getContentPane().add(text, gbc);
	}
	public void addComponent(int gridy, int gridx, GridBagConstraints gbc, JButton text)
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
		addComponent(0, 0, myGrid, nameLabel);
		addComponent(0, 1, myGrid, studentsList);
		addComponent(1, 0, myGrid, idLabel);
		addComponent(1, 1, myGrid, idField);
		addComponent(2, 0, myGrid, gradeLabel);
		addComponent(2, 1, myGrid, gradeField);
		addComponent(3, 0, myGrid, hoursLabel);
		addComponent(3, 1, myGrid, hoursField);
		
		Component mySpinnerEditor = numOfAwardsField.getEditor();
		JFormattedTextField jftf = ((JSpinner.DefaultEditor) mySpinnerEditor).getTextField();
		jftf.setColumns(8);
		
		addComponent(4, 0, myGrid, numOfAwardsText);
		addComponent(4, 1, myGrid, numOfAwardsField);
		
		awardsTextArea.setLineWrap(true);
		awardsTextArea.setBackground(new Color(200,200,200));
		awardsTextArea.setBorder(BorderFactory.createBevelBorder(1));
		
		addComponent(5, 0, myGrid, awardsText);
		addComponent(5, 1, myGrid, awardsTextArea);
		
		addComponent(6, 1, myGrid, saveEdit);
		addComponent(7, 1, myGrid, deleteStudent);
	}
	
	//Recreates table to interpret data
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
            			lastName = line.substring(commaAt, i) ;
            			commaAt = i + 1;
            			commaCount++;
            		}
            		else if(commaCount == 2)
            		{
            			stuID = line.substring(commaAt, i) ;
            			commaAt = i + 1;
            			commaCount++;
            		}
            		else if(commaCount == 3)
            		{
            			grade = line.substring(commaAt, i) ;
            			commaAt = i + 1;
            			commaCount++;
            		}
            	}
    			hours = line.substring(commaAt) ;
            }
            if(!line.isEmpty())
            {
	            model.addRow(new Object[]{firstName, lastName, stuID, grade, hours});
	            lineNumber++;
            }
        }
	}
}