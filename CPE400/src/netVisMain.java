import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import java.text.NumberFormat;

import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;


public class netVisMain extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private static final int DEFAULT_BASE = 0;

	//###GUI VARIABLES###
	public JFrame mainWindow;
		protected JPanel layer1grid;
			//protected JTextArea visualizerSection;
                        protected GraphicsArea graphicsSection;
			
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
        
        //Graph variables
	protected graph netGraph;
	
	public static void main(String[] args) {
		new netVisMain();
	}
	
	//default constructor for the window
	public netVisMain()
	{
		System.gc();
		//###mainWindow initializers
		mainWindow = new JFrame("Net_Visualizer_CPE400");
		mainWindow.setSize(1200, 800);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setMinimumSize(new Dimension(1200, 800));
		//sets the window to whatever theme you have (i.e. Mac, Win Vista, 7, 8.1, 10, Linux)
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		/////////////////////////////////////////////////////////////////////////////////////
		
		//setting the window to the middle of the screen/////////////////////////////////////////////////////////////////////////
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		mainWindow.setLocation(dim.width/2-mainWindow.getSize().width/2, dim.height/2-mainWindow.getSize().height/2);
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
                graphicsSection = new GraphicsArea(this);
                
		buildMenuBar();
		buildSettingsArea();
		
                mainWindow.getContentPane().add(graphicsSection, "Center");
		mainWindow.getContentPane().add(menuBar, "North");
		mainWindow.getContentPane().add(foundation, "South");
		mainWindow.setVisible(true);
	}
        
        /*
	 * @function: newBtnPressed()
	 * This function is called when the 'New' button is pressed on the JFrame. It opens a dialog
	 * that confirms that you will want to clear the graph on the screen and start a new one.
	 */
	private void newBtnPressed()
	{
		int n = JOptionPane.showConfirmDialog(mainWindow, "Are you sure you would like to start a new visualization?",
				"New Visualization?", JOptionPane.YES_NO_OPTION);
		
		if(n == JOptionPane.YES_OPTION)
		{
			resetAllSettings();
			graphicsSection.stopVisualization();
                        netGraph = null;
		}
	}
	
        /*
	 * @function: resetBtnPressed()
	 * This function is called when the 'Reset Values to Default' button is pressed on the main JFrame.
	 * If opens a dialog that confirms that you will want to reset the settings to default.
	 */
	private void resetBtnPressed()
	{
		int n = JOptionPane.showConfirmDialog(mainWindow, "Are you sure you would like to reset values to defualt?",
				"New Visualization?", JOptionPane.YES_NO_OPTION);
		
		if(n == JOptionPane.YES_OPTION)
		{
			resetToDefault();
			//repaint canvas to original graph
		}
	}
	//END of Button Pressed Functions
        
	//Support Functions
	private void resetAllSettings()
	{
		numNodesWanted.setValue(DEFAULT_BASE);
		numDegreeWanted.setValue(DEFAULT_BASE);
		numNodesWanted.setEditable(true);
		numDegreeWanted.setEditable(true);
		numNodesWanted.requestFocus();
		srcNodes.setText("");
		destNodes.setText("");
		linksToBreak.setText("");
		packetsToSend.setText("");
		disableAllFields();
	}
	
	private void resetToDefault()
	{
		srcNodes.setText("");
		destNodes.setText("");
		linksToBreak.setText("");
		packetsToSend.setText("10");
	}
	
	private boolean checkVisualizationSettings()
	{
		if(!srcNodes.getText().equals("") && !destNodes.getText().equals("") && !linksToBreak.getText().equals("") && !packetsToSend.getText().equals(""))
		{
			visualize.setEnabled(true);
			return true;
		}
		return false;
	}
	
	private void checkValues()
	{
		if(!numNodesWanted.getText().equals("0") && !numDegreeWanted.getText().equals("0"))
		{
			//check if degree = numNodes - 1
			//paint graph
			
			//keep the nodes and degrees locked
			numNodesWanted.setEditable(false);
			numDegreeWanted.setEditable(false);
			
			//enable all other text fields
			enableAllFields();
			//initializeGraph();
			//enable visualize button
		}
	}
	
	private void enableAllFields()
	{
		srcNodes.setEditable(true);
		destNodes.setEditable(true);
		linksToBreak.setEditable(true);
		packetsToSend.setEditable(true);
		startNew.setEnabled(true);
		//visualize.setEnabled(true);
	}
	
	private void disableAllFields()
	{
		srcNodes.setEditable(false);
		destNodes.setEditable(false);
		linksToBreak.setEditable(false);
		packetsToSend.setEditable(false);
		//startNew.setEnabled(false);
		visualize.setEnabled(false);		
	}
	
	private void addListeners()
	{
		numNodesWanted.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                checkValues();
            }
        });
				
		numDegreeWanted.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
            	checkValues();
            }
        });
						
		packetsToSend.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if(checkVisualizationSettings());
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if(checkVisualizationSettings());
			}
			
		});
		
		linksToBreak.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if(checkVisualizationSettings());
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if(checkVisualizationSettings());
			}
			
		});
		srcNodes.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if(checkVisualizationSettings());
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if(checkVisualizationSettings());
			}
			
		});
		destNodes.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if(checkVisualizationSettings());
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if(checkVisualizationSettings());
			}
			
		});
	
	}
	
	public void getSettings()
	{
		String one = numNodesWanted.getText();
		String two = numDegreeWanted.getText();
		one = one.replaceAll(",", "");
		two = two.replaceAll(",", "");
		
		numNodes = Integer.parseInt(one);
		numDegree = Integer.parseInt(two);
	}
        
	public void initializeGraph()
	{
		netGraph = new graph();
		//Build Graph
			//Get the number of nodes desired
			//Get the number of degree desired
			getSettings();
			//Create vertex
			//addConnections();
			//System.out.println(one.getNeighbor(0));
			//Add vertex to the graph
				//netGraph.addVertex(vertex1, true);
				//netGraph.addVertex(vertex2, true);
				//...
				//netGraph.addEdge(vertex1, vertex2, weight);
		//Visualize the graph
                
                /* TEST CODE FOR GRAPHICS */
                for(int i = 1; i <= numNodes; i++)
                {
                    netGraph.addVertex(new vertex("" + i), false);
                }
                netGraph.addEdge
                    (
                        netGraph.getVertex("1"),
                        netGraph.getVertex("2"),
                        1
                    );
                netGraph.addEdge
                    (
                        netGraph.getVertex("2"),
                        netGraph.getVertex("4"),
                        4
                    );
                netGraph.addEdge
                    (
                        netGraph.getVertex("4"),
                        netGraph.getVertex("8"),
                        10
                    );
                /* END TEST CODE */
	}
        
        // Gets netGraph - used by GraphicsArea
        public graph getGraph()
        {
            return netGraph;
        }
	
	public void addConnections()
	{
		//Know the degree given D
		//Have the list of known verticies X1
		//Have a duplicate list of known verticies X2
		//Generate a random number between the degree given, D, and number of verticies - 1, R1
		//For each vertex in X1
			//Generate a random number for the weight of the edges, R2
			//while X1.degree does not equal R1 && X1.degree < R1
				//Generate a random index number for X2
				//if(addEdge(X1, X2 , R2));
			
	}
	//End of Support Functions
	
	//BUILDING THE GUI FUNCTIONS
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
		
		menuBar.add(fileMenu);
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
				packetsToSend = new JTextField("10");
				numNodesWanted.setValue(0);
				numDegreeWanted.setValue(0);
				numNodesWanted.setHorizontalAlignment(SwingConstants.CENTER);
				numDegreeWanted.setHorizontalAlignment(SwingConstants.CENTER);
				srcNodes.setHorizontalAlignment(SwingConstants.CENTER);
				destNodes.setHorizontalAlignment(SwingConstants.CENTER);
				linksToBreak.setHorizontalAlignment(SwingConstants.CENTER);
				packetsToSend.setHorizontalAlignment(SwingConstants.CENTER);
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
		
                addListeners();
                disableAllFields();
	}
        //END OF BUILDING GUI FUNCTIONS
        
	private class ActionHandler implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
                    // File->New
                    // New
                    if(e.getSource() == newBtn || e.getSource() == startNew)
                    {
                            newBtnPressed();
                    }
                    
                    // File->About
                    if(e.getSource() == aboutBtn)
                    {
                            JOptionPane.showMessageDialog(mainWindow, "NetVisulizer was created "
                                            + "for the CPE400 class at UNR" + "\n\n" + "Created By: " + "\n" 
                                            + "Mitchell Reyes" + "\n" + "Pattaphol Jirasessakul" + "\n" + "Zachary Waller");
                    }

                    // Reset Values to Default
                    if(e.getSource() == resetBtn)
                    {
                        resetBtnPressed();
                    }

                    // Visualize
                    if(e.getSource() == visualize)
                    {
                        initializeGraph();
                        graphicsSection.beginVisualization();
                    }

		}
	}
}
