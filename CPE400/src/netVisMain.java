import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.NumberFormatter;


public class netVisMain extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private static final int DEFAULT_BASE = 0;

	//###GUI VARIABLES###
	public JFrame mainWindow;
		protected JPanel layer1grid;
			protected JTextArea visualizerSection;
			
	public settingsManager settings;
	public baseOptionDialogWindow baseSettings;
	
	//MenuBar variables
	protected JMenuBar menuBar;
		protected JMenu fileMenu;
			protected JMenuItem aboutBtn;
			protected JMenuItem newBtn;
                protected JMenu viewMenu;
                        protected JCheckBoxMenuItem showViewerBtn;

			
	//Settings Bar variables
	protected JPanel foundation;
		protected JPanel centerTextArea;
			protected JFormattedTextField numNodesWanted;
			protected JFormattedTextField numDegreeWanted;
			protected JTextField srcNodes;
			protected JTextField destNodes;
			protected JTextField linksToBreak;
			protected JTextField packetsToSend;
		protected JPanel southBtnArea;
			protected JButton startNew;
			protected JPanel placeHold1;
			protected JPanel placeHold2;
			protected JButton resetBtn;
			protected JButton visualize;
			
	//Base Setting Dialog variables
	protected JPanel basePanel;
		protected JFormattedTextField numNodesField;
		protected JFormattedTextField numDegreeField;
		
	//Base Setting number variables
	protected int numNodes = 0;
	protected int numDegree = 0;
        
        // NetViewer object variable
        protected NetViewer netViewer;
	
	public static void main(String[] args) {
		new netVisMain();
	}
	
	//default constructor for the window
	public netVisMain()
	{
		System.gc();
		//###mainWindow initializers
		mainWindow = new JFrame("Net_Visualizer_CPE400");
		mainWindow.setSize(1200, 200);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setMinimumSize(new Dimension(1200, 200));
		//sets the window to whatever theme you have (i.e. Mac, Win Vista, 7, 8.1, 10, Linux)
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		/////////////////////////////////////////////////////////////////////////////////////
		
		//setting the window to the upper middle of the screen/////////////////////////////////////////////////////////////////////////
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		mainWindow.setLocation(dim.width/2-mainWindow.getSize().width/2, dim.height/8-mainWindow.getSize().height/2);
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		buildMenuBar();
		buildSettingsArea();
		
		mainWindow.getContentPane().add(menuBar, "North");
		mainWindow.getContentPane().add(foundation, "South");
		mainWindow.setVisible(true);
	}
	
        // A method to let netVisMain know that NetViewer has been closed
        // Called by NetViewer when it is destroyed
        public void netViewClosing()
        {
            netViewer = null;
            showViewerBtn.setState(false);
        }
        
	private void newBtnPressed()
	{
		int n = JOptionPane.showConfirmDialog(mainWindow, "Are you sure you would like to start a new visualization?",
				"New Visualization?", JOptionPane.YES_NO_OPTION);
		
		if(n == JOptionPane.YES_OPTION)
		{
                        if(netViewer != null)
                        {
                            netViewer.closeWindow();
                        }
                        
			resetBaseSettings();
			showBaseSettingsDialog();
                        
                        createVisualizer();
		}
	}
	
	private void buildSettingsArea()
	{
		//Title Creation
		TitledBorder first, second, third, fourth, fifth, sixth, seventh;
		Border f1, f2, f3, f4, f5, f6, f7;
		f1 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		f2 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		f3 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		f4 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		f5 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		f6 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		f7 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		first = BorderFactory.createTitledBorder(f1, "Number of Nodes");
		second = BorderFactory.createTitledBorder(f2, "Minimum Degree");
		third = BorderFactory.createTitledBorder(f3, "Source Nodes");
		fourth = BorderFactory.createTitledBorder(f4, "Destination Nodes");
		fifth = BorderFactory.createTitledBorder(f5, "Links to Break");
		sixth = BorderFactory.createTitledBorder(f6, "Settings");
		seventh = BorderFactory.createTitledBorder(f7, "Number of Packets");
		//////////////////////////
		
		//Accepting Numbers only/////////////////////
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setAllowsInvalid(false);
		formatter.setCommitsOnValidEdit(false);
		/////////////////////////////////////////////
		
		foundation = new JPanel();
		foundation.setLayout(new BorderLayout());
		foundation.setPreferredSize(new Dimension(400, 120));
			centerTextArea = new JPanel();
			centerTextArea.setLayout(new GridLayout(1, 6));
				numNodesWanted = new JFormattedTextField(formatter);
				numDegreeWanted = new JFormattedTextField(formatter);
				srcNodes = new JTextField();
				destNodes = new JTextField();
				linksToBreak = new JTextField();
				packetsToSend = new JTextField();
				numNodesWanted.setValue(0);
				numDegreeWanted.setValue(0);
				numNodesWanted.setHorizontalAlignment(SwingConstants.CENTER);
				numDegreeWanted.setHorizontalAlignment(SwingConstants.CENTER);
				srcNodes.setHorizontalAlignment(SwingConstants.CENTER);
				destNodes.setHorizontalAlignment(SwingConstants.CENTER);
				linksToBreak.setHorizontalAlignment(SwingConstants.CENTER);
				packetsToSend.setHorizontalAlignment(SwingConstants.CENTER);
				numNodesWanted.setOpaque(false);
				numDegreeWanted.setOpaque(false);
				srcNodes.setOpaque(false);
				destNodes.setOpaque(false);
				linksToBreak.setOpaque(false);
				packetsToSend.setOpaque(false);
				numNodesWanted.setBorder(first);
				numDegreeWanted.setBorder(second);
				srcNodes.setBorder(third);
				destNodes.setBorder(fourth);
				linksToBreak.setBorder(fifth);
				packetsToSend.setBorder(seventh);
			centerTextArea.add(numNodesWanted, "Center");
			centerTextArea.add(numDegreeWanted, "Center");
			centerTextArea.add(srcNodes, "Center");
			centerTextArea.add(destNodes, "Center");
			centerTextArea.add(linksToBreak, "Center");
			centerTextArea.add(packetsToSend, "Center");
			centerTextArea.setBorder(sixth);
		foundation.add(centerTextArea, "Center");
			southBtnArea = new JPanel();
			southBtnArea.setLayout(new GridLayout(1, 5));
				startNew = new JButton("New");
				startNew.addActionListener(new ActionHandler());
				placeHold1 = new JPanel();
				placeHold2 = new JPanel();
				resetBtn = new JButton("Reset Values to Default");
				resetBtn.addActionListener(new ActionHandler());
				visualize = new JButton("Visualize");
				visualize.addActionListener(new ActionHandler());
			southBtnArea.add(startNew, "Center");
			southBtnArea.add(placeHold1, "Center");
			southBtnArea.add(placeHold2, "Center");
			southBtnArea.add(resetBtn, "Center");
			southBtnArea.add(visualize, "Center");
		foundation.add(southBtnArea, "South");
			
	}
	
	private void buildMenuBar()
	{
		//###MenuBar initializers
		menuBar = new JMenuBar();
			fileMenu = new JMenu("File");
				newBtn = new JMenuItem("New");
				newBtn.addActionListener(new ActionHandler());
				newBtn.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
				aboutBtn = new JMenuItem("About");
				aboutBtn.addActionListener(new ActionHandler());
				aboutBtn.setAccelerator(KeyStroke.getKeyStroke('T', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
			fileMenu.add(newBtn);
			fileMenu.add(aboutBtn);
                        viewMenu = new JMenu("View");
                                showViewerBtn = new JCheckBoxMenuItem("Show Viewer");
                                showViewerBtn.addActionListener(new ActionHandler());
                                showViewerBtn.setAccelerator(KeyStroke.getKeyStroke('V', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
                        viewMenu.add(showViewerBtn);
		menuBar.add(fileMenu);
                menuBar.add(viewMenu);
	}

	private void buildDialogPanel()
	{
		
		//Accepting Numbers only/////////////////////
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setAllowsInvalid(false);
		formatter.setCommitsOnValidEdit(false);
		/////////////////////////////////////////////
		
		//Title Creation
		TitledBorder second, third;
		Border f2, f3;
		f2 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		f3 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		second = BorderFactory.createTitledBorder(f2, "Number of Nodes");
		third = BorderFactory.createTitledBorder(f3, "Minimum Degree");
		//////////////////////////
		basePanel = new JPanel();
		basePanel.setPreferredSize(new Dimension(300, 75));
		basePanel.setLayout(new GridLayout(1, 2));
			numNodesField = new JFormattedTextField(formatter);
			numNodesField.setOpaque(false);
			numNodesField.setBorder(second);
			numNodesField.setValue(numNodes);
			numNodesField.setHorizontalAlignment(SwingConstants.CENTER);
			numDegreeField = new JFormattedTextField(formatter);
			numDegreeField.setOpaque(false);
			numDegreeField.setBorder(third);
			numDegreeField.setValue(numDegree);
			numDegreeField.setHorizontalAlignment(SwingConstants.CENTER);	
		basePanel.add(numNodesField, "Center");
		basePanel.add(numDegreeField, "Center");
	}
	
	private void resetBaseSettings()
	{
		numNodes = DEFAULT_BASE;
		numDegree = DEFAULT_BASE;
                
                numNodesWanted.setText(Integer.toString(numNodes));
                numDegreeWanted.setText(Integer.toString(numDegree));
                

	}
	
	private void getBaseSettingNums()
	{
		String nodes = numNodesField.getText();
		String degree = numDegreeField.getText();
		nodes = nodes.replaceAll(",", "");
		degree = degree.replaceAll(",", "");
		numNodes = Integer.parseInt(nodes);
		numDegree = Integer.parseInt(degree);
                numNodesWanted.setText(nodes);
                numDegreeWanted.setText(degree);
	}
        
        private void getWantedSettingNums()
        {
            String nodes = numNodesWanted.getText();
            String degree = numDegreeWanted.getText();
            nodes = nodes.replaceAll(",", "");
            degree = degree.replaceAll(",", "");
            numNodes = Integer.parseInt(nodes);
            numDegree = Integer.parseInt(degree);
        }
	
	private void showBaseSettingsDialog()
	{
		buildDialogPanel();
		int result = JOptionPane.showConfirmDialog(null, basePanel, "New Visualizer", JOptionPane.OK_CANCEL_OPTION);
		if(result == JOptionPane.YES_OPTION)
		{
			getBaseSettingNums();
		}
	}
        
        private void createVisualizer()
        {
            getWantedSettingNums();
            netViewer = new NetViewer(this);
            new Thread(netViewer).start();
            
            showViewerBtn.setState(true);
        }
        
	private class ActionHandler implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
                    // File->About
                    if(e.getSource() == aboutBtn)
                    {
                            JOptionPane.showMessageDialog(mainWindow, "NetVisulizer was created "
                                            + "for the CPE400 class at UNR" + "\n\n" + "Created By: " + "\n" 
                                            + "Mitchell Reyes" + "\n" + "Pattaphol Jirasessakul" + "\n" + "Zachary Waller");
                    }

                    // File->New
                    // New
                    if(e.getSource() == newBtn || e.getSource() == startNew)
                    {
                            newBtnPressed();
                    }
                    
                    // Reset Values to Default
                    if(e.getSource() == resetBtn)
                    {
                        resetBaseSettings();
                    }

                    // Visualize
                    if(e.getSource() == visualize)
                    {
                        if(netViewer == null)
                        {
                            createVisualizer();
                        }
                    }

                    // View->Show Viewer
                    if(e.getSource() == showViewerBtn)
                    {
                        boolean checked = showViewerBtn.getState();

                        if(netViewer == null)
                        {
                            createVisualizer();
                        }
                        else if(checked)
                        {
                            netViewer.showWindow();
                        }
                        else
                        {
                            netViewer.hideWindow();
                        }
                    }
		}
	}
	
}
