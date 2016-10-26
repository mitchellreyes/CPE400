package netvisualizer.Backend;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.NumberFormatter;

/*Notes on what to do:
 * When the save/visualize button is pressed, send the variables to a file so that other people can use the variables
 * If cancel is pressed, reset the text fields to 0
 * 
 * 
 * 
 */


public class settingsManager extends JFrame{
	private static final long serialVersionUID = 1L;
	
	public JFrame settingsWindow;
		private JPanel mainGrid;
			private JPanel Upper;
				private JFormattedTextField one;
				private JFormattedTextField two;
				private JFormattedTextField three;
				private JFormattedTextField four;
				private JFormattedTextField five;
				private JPanel btnPanel;
					public JButton reset;
			private JPanel Middle;
			private JPanel Lower;
				private JPanel placeHold1;
					public JButton cancel;
				private JPanel placeHold2;
				private JPanel placeHold3;
					public JButton save;
					
	private File file;
	
	//main variables
	public int oneNum;
	public int twoNum;
	public int threeNum;
	public int fourNum;
	public int fiveNum;
	
	//default constructor
	public settingsManager()
	{
		System.gc();
		//###Main Settings Window initializers
		settingsWindow = new JFrame("Settings");
		settingsWindow.setSize(300, 900);
		settingsWindow.setMinimumSize(new Dimension(900, 400));
		//setting the window to the middle of the screen/////////////////////////////////////////////////////////////////////////
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		settingsWindow.setLocation(dim.width/2-settingsWindow.getSize().width/2, dim.height/2-settingsWindow.getSize().height/2);
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//###BORDER VARIABLES///////////////////////////////////////
		TitledBorder title1, title2, title3, title4, title5, title6;
		Border l1, l2, l3, l4, l5, l6;
		l1 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		l2 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		l3 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		l4 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		l5 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		l6 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);


		
		title1 = BorderFactory.createTitledBorder(l1, "Main Settings Area");
		title2 = BorderFactory.createTitledBorder(l2, "Number of Nodes");
		title3 = BorderFactory.createTitledBorder(l3, "Number of Source Nodes");
		title4 = BorderFactory.createTitledBorder(l4, "Number of Destination Nodes");
		title5 = BorderFactory.createTitledBorder(l5, "Minimum Degree");
		title6 = BorderFactory.createTitledBorder(l6, "Number of Packets to send");
		////////////////////////////////////////////////////////////
		
		//Accepting Numbers only/////////////////////
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setAllowsInvalid(false);
		formatter.setCommitsOnValidEdit(false);
		/////////////////////////////////////////////
		
		//###Main Grid Initializers
		mainGrid = new JPanel();
		mainGrid.setLayout(new GridLayout(3, 1));
			Upper = new JPanel();
			Upper.setLayout(new GridLayout(3, 2));
			Upper.setBorder(title1);
				one = new JFormattedTextField(formatter);
				one.setValue(new Integer(0));
				two = new JFormattedTextField(formatter);
				two.setValue(new Integer(0));
				three = new JFormattedTextField(formatter);
				three.setValue(new Integer(0));
				four = new JFormattedTextField(formatter);
				four.setValue(new Integer(0));
				five = new JFormattedTextField(formatter);
				five.setValue(new Integer(0));
				btnPanel = new JPanel();
				btnPanel.setLayout(new BorderLayout());
					reset = new JButton("Reset Values to Default");
					//!!!! ENABLE THE RESET BUTTON ONLY WHEN THE VALUES ARE NOT DEFAULT
					reset.setEnabled(false);
					reset.addActionListener(new ActionHandler());
					btnPanel.add(reset, "South");
					one.setOpaque(false);
					two.setOpaque(false);
					three.setOpaque(false);
					four.setOpaque(false);
					five.setOpaque(false);
					one.setBorder(title2);
					two.setBorder(title3);
					three.setBorder(title4);
					four.setBorder(title5);
					five.setBorder(title6);
				Upper.add(one, "West");
				Upper.add(two, "East");
				Upper.add(three, "South");
				Upper.add(four, "South");
				Upper.add(five, "South");
				Upper.add(btnPanel, "South");
			Middle = new JPanel();
			Lower = new JPanel();
			Lower.setLayout(new GridLayout(1, 3));
				save = new JButton("Visualize");
				save.addActionListener(new ActionHandler());
				cancel = new JButton("Cancel");
				cancel.addActionListener(new ActionHandler());
				placeHold1 = new JPanel();
				placeHold2 = new JPanel();
				placeHold3 = new JPanel();
				placeHold1.setLayout(new BorderLayout());
					placeHold1.add(cancel, "South");
				placeHold3.setLayout(new BorderLayout());
					placeHold3.add(save, "South");
				Lower.add(placeHold1, "East");
				Lower.add(placeHold2, "East");
				Lower.add(placeHold3, "East");
		mainGrid.add(Upper, "North");
		mainGrid.add(Middle, "South");
		mainGrid.add(Lower, "South");
		
		
		settingsWindow.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
	    settingsWindow.getRootPane().getActionMap().put("Cancel", new AbstractAction(){
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent e)
	            {
	                settingsWindow.dispose();
	            }
	        });
		settingsWindow.getContentPane().add(mainGrid, "Center");
		settingsWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		settingsWindow.setVisible(true);
		
	}
	
	
	/*
	 * reset all variables to their initial values
	 * set the numbers in the textfields to the initial values 
	 */
	private void resetValues()
	{
		//TODO
	}
	
	private void showResetDialog()
	{
		int n = JOptionPane.showConfirmDialog(settingsWindow, "Are you sure you would like to reset values to default?",
				"Reset Values to Default", JOptionPane.YES_NO_OPTION);
		
		if(n == JOptionPane.YES_OPTION)
		{
			//resetValues();
		}
		
	}
	
	/*fn: getVars()
	 * purpose: gets the text from the textboxs and parses them into integers
	 */
	public void getVars()
	{
		String a, b, c, d, e;
		a = one.getText();
		a = a.replaceAll(",", "");
		b = two.getText();
		b = b.replaceAll(",", "");
		c = three.getText();
		c = c.replaceAll(",", "");
		d = four.getText();
		d = d.replaceAll(",", "");
		e = five.getText();
		e = e.replaceAll(",", "");

		oneNum = Integer.parseInt(a);
		twoNum = Integer.parseInt(b);
		threeNum = Integer.parseInt(c);
		fourNum = Integer.parseInt(d);
		fiveNum = Integer.parseInt(e);
		
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
		System.out.println(d);
		System.out.println(e);

	}
	
	/*
	 *Writes all variables from this class to a file in the temp directory
	 *i.e. Nodes: X
	 *		Degree: Y
	 *		Src: Z
	 *
	 * Hint: use new File(System.getProperty("java.io.tmpdir") + "[some name here]"); to put a file into the temp folder
	 */
	private void writeVarsToFile()
	{
		//TODO
		try(BufferedWriter br = new BufferedWriter(new FileWriter(file)))
		{
			//br.write();
		}
		catch(FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "/*file we want to write*/ cannot be found");
		}
		catch(IOException e)
		{
			JOptionPane.showMessageDialog(null, "Error has occured while writing to file.");
		}
	}

	/*
	 * Reads all the variables from the file in the temp folder and places them into the variables
	 * 
	 */
	private void readVarsFromFile()
	{
		//TODO
		try(BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			while(br.ready())
			{
				//br.readLine();
			}
		}
		catch(FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "Cannot find /*file we want to read from*/");
		}
		catch(IOException e)
		{
			JOptionPane.showMessageDialog(null, "An error has occured while reading from file");
		}
	}
	

	
	private class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == save)
			{
				//writeVarsToFile();
				getVars();
				settingsWindow.dispose();
			}
			
			if(e.getSource() == reset)
			{
				showResetDialog();
			}
			
			if(e.getSource() == cancel)
			{
				settingsWindow.dispose();
			}
			
		}
	}

}
