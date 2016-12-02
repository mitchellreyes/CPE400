import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.NumberFormatter;

public class baseOptionDialogWindow extends JFrame {
	private static final long serialVersionUID = 1L;
		
	private JFrame optionPane;
		private JPanel mainPanel;
			private JPanel center;
				private JFormattedTextField numNodesField;
				private JFormattedTextField numDegreeField;
			private JPanel south;
				private JButton save;
				private JPanel XXX;
				private JButton cancel;
	
	public baseOptionDialogWindow()
	{
		optionPane = new JFrame("New Visualizer");
		optionPane.setSize(400, 200);
		optionPane.setMinimumSize(new Dimension(400, 200));
		//optionPane.setUndecorated(true);
		optionPane.setResizable(false);
		//setting the window to the middle of the screen/////////////////////////////////////////////////////////////////////////
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		optionPane.setLocation(dim.width/2-optionPane.getSize().width/2, dim.height/2-optionPane.getSize().height/2);
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		TitledBorder first, second, third;
		Border f1, f2, f3;
		f1 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		f2 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		f3 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		first = BorderFactory.createTitledBorder(f1, "New Visualizer");
		second = BorderFactory.createTitledBorder(f2, "Number of Nodes");
		third = BorderFactory.createTitledBorder(f3, "Minimum Degree");

		//Accepting Numbers only/////////////////////
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setAllowsInvalid(false);
		formatter.setCommitsOnValidEdit(false);
		/////////////////////////////////////////////
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
			center = new JPanel();
			center.setLayout(new GridLayout(1, 2));
				numNodesField = new JFormattedTextField(formatter);
				numNodesField.setOpaque(false);
				numNodesField.setBorder(second);
				numNodesField.setHorizontalAlignment(SwingConstants.CENTER);
				numDegreeField = new JFormattedTextField(formatter);
				numDegreeField.setOpaque(false);
				numDegreeField.setBorder(third);
				numDegreeField.setHorizontalAlignment(SwingConstants.CENTER);
			center.add(numNodesField, "Center");
			center.add(numDegreeField, "Center");
			
			south = new JPanel();
			south.setLayout(new GridLayout(1, 3));
				save = new JButton("Visualize");
				XXX = new JPanel();
				cancel = new JButton("Cancel");
			south.add(cancel, "Center");
			south.add(XXX, "Center");
			south.add(save, "Center");
		mainPanel.add(center, "Center");
		mainPanel.add(south, "South");
		mainPanel.setBorder(first);
			
		optionPane.getContentPane().add(mainPanel, "Center");
		optionPane.addWindowListener(new WindowAdapter(){
	        public void windowClosing(WindowEvent e){
	        	dispose();
	         }
	        });
		optionPane.setVisible(true);
	}
}
