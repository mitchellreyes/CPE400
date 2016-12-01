import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


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
			private JPanel CENTER_PANEL;
				private JTextField nodesShown;
				private JTextField degreeShown;
				private JTextField sourceNodes;
				private JTextField destNodes;
				private JTextField linksToBreak;
			
				
	
	//default constructor
	public settingsManager(int numNodes, int numDegree, JRadioButtonMenuItem settingsSelect)
	{
		System.gc();
		//###Main Settings Window initializers
		settingsWindow = new JFrame("Settings");
		settingsWindow.setSize(500, 400);
		settingsWindow.setMinimumSize(new Dimension(500, 400));
		//setting the window to the middle of the screen/////////////////////////////////////////////////////////////////////////
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		settingsWindow.setLocation(dim.width/2-settingsWindow.getSize().width/2, dim.height/2-settingsWindow.getSize().height/2);
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Title Creation
		TitledBorder first, second, third, fourth, fifth;
		Border f1, f2, f3, f4, f5;
		f1 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		f2 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		f3 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		f4 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		f5 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		first = BorderFactory.createTitledBorder(f1, "Number of Nodes:");
		second = BorderFactory.createTitledBorder(f2, "Minimum Degree:");
		third = BorderFactory.createTitledBorder(f3, "Source Nodes:");
		fourth = BorderFactory.createTitledBorder(f4, "Destination Nodes:");
		fifth = BorderFactory.createTitledBorder(f5, "Links to Break:");
		//////////////////////////
		
		mainGrid = new JPanel();
		mainGrid.setLayout(new BorderLayout());
			CENTER_PANEL = new JPanel();
			CENTER_PANEL.setLayout(new GridLayout(5, 1));
				nodesShown = new JTextField();
				degreeShown = new JTextField();
				nodesShown.setText("" + numNodes);
				degreeShown.setText("" + numDegree);
				nodesShown.setEditable(false);
				degreeShown.setEditable(false);
				nodesShown.setBorder(first);
				degreeShown.setBorder(second);
				sourceNodes = new JTextField();
				sourceNodes.setBorder(third);
				destNodes = new JTextField();
				destNodes.setBorder(fourth);
				linksToBreak = new JTextField();
				linksToBreak.setBorder(fifth);
			CENTER_PANEL.add(nodesShown, "Center");
			CENTER_PANEL.add(degreeShown, "Center");
			CENTER_PANEL.add(sourceNodes, "Center");
			CENTER_PANEL.add(destNodes, "Center");
			CENTER_PANEL.add(linksToBreak, "Center");
		mainGrid.add(CENTER_PANEL, "Center");
		
		settingsWindow.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
	    settingsWindow.getRootPane().getActionMap().put("Cancel", new AbstractAction(){
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent e)
	            {
	                settingsWindow.dispose();
	            }
	        });
		
		//do something when the settings window is closed
		settingsWindow.addWindowListener(new WindowAdapter(){
	        public void windowClosing(WindowEvent e){
	        	settingsSelect.setSelected(false);
	        	dispose();
	         }
	        });
	    settingsWindow.getContentPane().add(mainGrid, "Center");
		settingsWindow.setVisible(true);		
	}
}
	
	

