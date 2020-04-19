package main;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class CommunityService extends JFrame
{
	//Initialize elements
	Image icon = Toolkit.getDefaultToolkit().getImage("src\\resources\\icon.png");  
	private static JButton addStudent = new JButton(new ImageIcon("src\\resources\\add.png"));
	private static JButton editStudent = new JButton(new ImageIcon("src\\resources\\edit.png"));
	private static JButton printStudent = new JButton(new ImageIcon("src\\resources\\print.png"));
	private static JTable table = new JTable();
	private static String firstName, lastName, stuID, grade, hours, awards;
	private static int commaCount = 0, commaAt;
	static DefaultTableModel model = new DefaultTableModel();
	
	static FileInputStream fis;
	
	//Run program
	public static void main(String[] args)
	{
		new CommunityService();
	}

	public CommunityService()
	{	
		//Setup GUI
        createTable();
		setTitle("Community Service | Kyler Tran");	
		setIconImage(icon);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//1 X 3 button grid
		JPanel buttonPanel = new JPanel(new GridLayout(1,3));
		
		//Organize buttons to grid
		addStudent.setBackground(Color.WHITE);
		addStudent.setOpaque(true);
		editStudent.setBackground(Color.WHITE);
		editStudent.setOpaque(true);
		printStudent.setBackground(Color.WHITE);
		printStudent.setOpaque(true);
		buttonPanel.add(addStudent, BorderLayout.WEST);
		buttonPanel.add(editStudent, BorderLayout.CENTER);
		buttonPanel.add(printStudent, BorderLayout.EAST);
		
		//Add scrollable table and button panel to GUI
		add(new JScrollPane(table));
		add(buttonPanel, BorderLayout.SOUTH);
		
        setVisible(true);
        setSize(428, 375);
        setResizable(false);
        
        //On 'Add' button click
        addStudent.addMouseListener(new MouseAdapter() 
        {
		    @Override
		    public void mouseClicked(MouseEvent e) 
		    {
		        new AddStudent();
		    }

        });
        
        //On 'Edit' button click
        editStudent.addMouseListener(new MouseAdapter() 
        {
		    @Override
		    public void mouseClicked(MouseEvent e) 
		    {
		        new EditStudent();
		    }

        });
        
        //On 'Print' button click
        printStudent.addMouseListener(new MouseAdapter() 
        {
		    @Override
		    public void mouseClicked(MouseEvent e) 
		    {
		        new Print();
		    }

        });
	}
	
	//Force refresh table
	public static void forceReload()
	{
    	model.fireTableDataChanged();
    	createTable();
	}
	
	//Create or reset table
	public static void createTable()
	{
		model.setRowCount(0);
		model.setColumnCount(0);

		table = new JTable(model)
		{
			public boolean isCellEditable(int rowIndex, int colIndex) 
			{
				return false; //Disallow the editing of any cell
			}
		};

		//Disable resizing and reordering
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
		//Add column titles
		model.addColumn("First Name");
		model.addColumn("Last Name");
		model.addColumn("Student ID");
		model.addColumn("Grade");
		model.addColumn("Hours");
		model.addColumn("Awards");
		
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(50);
		table.getColumnModel().getColumn(2).setPreferredWidth(40);
		table.getColumnModel().getColumn(3).setPreferredWidth(5);
		table.getColumnModel().getColumn(4).setPreferredWidth(5);
		table.getColumnModel().getColumn(5).setPreferredWidth(20);
		
		//Scan and insert data from text file
		Scanner sc = null;
	    File file = new File("src\\resources\\Data.txt"); 
		
		try
		{
			sc = new Scanner(file);
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
            	awards = line.substring(commaAt);
            }
            if(!line.isEmpty())
            {
	            model.addRow(new Object[]{firstName, lastName, stuID, grade, hours, awards});
	            lineNumber++;
            }
        }
        //Add extra blank rows
        model.setRowCount(25);
	}
}