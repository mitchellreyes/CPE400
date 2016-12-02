import java.awt.*;

import java.awt.event.*;
import java.text.NumberFormat;
import java.util.Random;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;


public class netVisMain extends JFrame{
	private static final long serialVersionUID = 1L;
	private final int MAX_NODES = 16;
	private final int MIN_NODES = 2;
	private final int MIN_DEGREE = 1;
	//###GUI VARIABLES###
	public JFrame mainWindow;
		protected JPanel layer1grid;
			//protected JTextArea visualizerSection;
			protected GraphicsArea graphicsSection;
				
	//MenuBar variables
	protected JMenuBar menuBar;
		protected JMenu fileMenu;
			protected JMenuItem aboutBtn;
			protected JMenuItem newBtn;

			
	//Settings Bar variables
	protected JPanel foundation;
		protected JPanel centerTextArea;
			protected JTextField numNodesWanted;
			protected JTextField numDegreeWanted;
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
		
	//Base Setting number variables
	protected int numNodes = 2;
	protected int numDegree = 1;
	protected int numLinksToBreak = 0;
	
	//Graph variables
	protected graph netGraph;
		protected vertex verticies[];
	
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
		createNewGraphDialog();
	}
	
	
	public graph getGraph()
	{
		return netGraph;
	}
	public void packetDelivered(vertex source, vertex dest)
    {
        Random random = new Random();
        int index = random.nextInt(dest.getNeighborCount() + 1);
        vertex neighbor;
        
        if(!dest.getNeighbor(index).getVertexOne().equals(dest))
        {
            neighbor = dest.getNeighbor(index).getVertexOne();
        }
        else
        {
            neighbor = dest.getNeighbor(index).getVertexTwo();
        }
        
        graphicsSection.sendPacket(dest, neighbor);
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
			//TODO repaint canvas to blank
			graphicsSection.stopVisualization();
			netGraph = null;
			createNewGraphDialog();
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
			//TODO repaint canvas to original graph
		}
	}
	//END of Button Pressed Functions
	
	
	//Support Functions
	private void resetAllSettings()
	{
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
		visualize.setEnabled(false);
		return false;
	}
	
	private void enableAllFields()
	{
		srcNodes.setEditable(true);
		srcNodes.requestFocus();
		destNodes.setEditable(true);
		linksToBreak.setEditable(true);
		packetsToSend.setEditable(true);
		startNew.setEnabled(true);
	}
	
	private void disableAllFields()
	{
		srcNodes.setEditable(false);
		destNodes.setEditable(false);
		linksToBreak.setEditable(false);
		packetsToSend.setEditable(false);
		visualize.setEnabled(false);		
	}
	
	private void addListeners()
	{			
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
	
	public void initializeGraph()
	{
		System.out.println("Initializing Graph");
		if(numDegree > numNodes)
		{
			JOptionPane.showMessageDialog(mainWindow, "The number of degrees cannot be higher than the number of nodes!", 
					"ERROR", JOptionPane.WARNING_MESSAGE);
			numDegree = numNodes - 1;
			createNewGraphDialog();
		}
		else{
			netGraph = new graph();
			verticies = new vertex[numNodes];
			for(int i = 0; i < verticies.length; i++)
			{
				verticies[i] = new vertex("Node" + i, numNodes, i);
				netGraph.addVertex(verticies[i], true);
			}
			addConnections();
			//System.out.println(netGraph.getEdges());
		}
	}
	
	public void addConnections()
	{
		//Generate a random number between the degree given, D, and number of verticies - 1, R1
		int neighborIndex, randWeightNum, randDegreeAmt;
		Random rand = new Random();
		boolean positiveOffset = true;
		
		System.out.println("TEST");
		
		//For each vertex in X1
		for(int vert = 0; vert < verticies.length; vert++)
		{
			// Generates a random number of degrees that can go up to three std dev's away from average
			randDegreeAmt = numDegree;
			
			// Decide whether to increment or decrement degree, 50/50 chance
			if(rand.nextFloat() < 0.50f)
			{
				positiveOffset = false;
			}
			
			// Offset desired degree
			float[] stdDevs = { 0.341f, 0.136f, 0.021f };
			for(int dev = 0; dev < 3; dev++)
			{
				randDegreeAmt = offset(randDegreeAmt, stdDevs[dev], positiveOffset);
			}
			
			// Clip randDegree between 1 and numNodes - 1
			if(randDegreeAmt <= 0) randDegreeAmt = 1;
			if(randDegreeAmt >= numNodes) randDegreeAmt = numNodes - 1;
			
			verticies[vert].setDesiredDegree(randDegreeAmt);
			
			// Try to generate desired number of connections
			int iterations = 0;
			neighborIndex = vert + 1;
			while(verticies[vert].getDegree() < randDegreeAmt && iterations < 100)
			{
				//Generate a random index number for neighbor
				//randIndexNum = randNum(0, verticies.length - 1);
				neighborIndex = (neighborIndex + 1) % numNodes;
				
				// Skip node if it already has its desired degree or, if desired degree isn't set, skip if it already has numDegree connections
				// if(vertex desired degree set AND has less connections than desired)
				if(
					verticies[neighborIndex].getDesiredDegree() != -1
					&& verticies[neighborIndex].getDegree() >= verticies[neighborIndex].getDesiredDegree()
					)
				{
					iterations++;
					break;
				}
				// if(vertex desired degree NOT set AND vertex already has numDegree connections
				else if(
					verticies[neighborIndex].getDesiredDegree() == -1
					&& verticies[neighborIndex].getDegree() >= numDegree
					)
				{
					iterations++;
					break;
				}
				
				//Generate a random number for the weight of the edges, R2		
				randWeightNum = randNum(1, 9);
				
				// Add edge
				netGraph.addEdge(verticies[vert], verticies[neighborIndex], randWeightNum);
				
				iterations++;
			}
		}	
		
	}
	
	// Takes an input, has a probability to increment or decrement input
	private int offset(int input, float probability, boolean positive)
	{
		Random rand = new Random();
		float randNum = rand.nextFloat();
		
		if(randNum <= probability)
		{
			if(positive)
			{
				return input + 1;
			}
			else
			{
				return input - 1;
			}
		}
		
		return input;
	}
	
	public int randNum(int min, int max)
	{
		Random rand = new Random();
			return rand.nextInt((max - min) + 1) + min;
	}
	
	public void breakLinks()
	{
		numLinksToBreak = Integer.parseInt(linksToBreak.getText());
		Object[] edgeArray = netGraph.getEdges().toArray();
		int randNum = randNum(0, numNodes - 1);
		
		for(int i = 0; i < numLinksToBreak; i++)
		{
			edge edgeRef = (edge) edgeArray[randNum];
			// Get random edges until unbroken one is found
			while (edgeRef.isBroken()) 
			{
				randNum = randNum(0, numNodes - 1);
				edgeRef = (edge) edgeArray[randNum];
			}
			
			edgeRef.setBroken(true);
			graphicsSection.breakEdge(edgeRef);
		}
	}
	
	public void setSourceNodes()
	{
		String srcNodesWanted = "5";//srcNodes.getText();
		srcNodesWanted = srcNodesWanted.trim();

		//if its just one integer "#"
			processInt(srcNodesWanted);
			graphicsSection.sendPacket(verticies[0], verticies[1]);
		//if its formatted as "#-#"
		/*if(srcNodesWanted.contains("-")){
			splitNums = srcNodesWanted.split("-");
			for(int i = 0; i < splitNums.length; i++){
				System.out.println(splitNums[i]);
			}
		}
		if(srcNodesWanted.contains(",")){
			splitNums = srcNodesWanted.split(",");
			for(int i = 0; i < splitNums.length; i++){verticies.length
				splitNums[i] = splitNums[i].trim();
				System.out.println(splitNums[i]);
			}
		}*/
		//if its formatted as "#, #, #"
		//else non format
			//throw error
	}
	
	private void processInt(String s){
		String vertexLabel;
		for(int i = 0; i < graphicsSection.vertices.size(); i++){
			vertexLabel = graphicsSection.vertices.get(i).getLabel();
			vertexLabel = vertexLabel.replaceAll("Node", "");
			vertexLabel = vertexLabel.trim();
			if(vertexLabel.equals(s)){
				graphicsSection.vertices.get(i).setColor(Color.BLUE);
				//graphicsSection.vertices.get(i).setLabel(new String("[SRC] " + graphicsSection.vertices.get(i).getLabel()));
			}
		}
	}
	
	public boolean isInteger(String s) {
	    return isInteger(s,10);
	}

	public boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
				JOptionPane.showMessageDialog(mainWindow, "Source Nodes cannot be negative!", 
						"ERROR", JOptionPane.WARNING_MESSAGE);
	            return false;
	            //else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
	
	//End of Support Functions
	
	//BUILDING THE GUI FUNCTIONS
	
	private void createNewGraphDialog()
	{
		JPanel westFoundation = new JPanel();
		westFoundation.setLayout(new BorderLayout());
			JButton plus = new JButton("+");
			numNodesWanted = new JTextField(3);
				numNodesWanted.setEditable(false);
				numNodesWanted.setText("" + numNodes);
				numNodesWanted.setHorizontalAlignment(JTextField.CENTER);
			JButton minus = new JButton("-");
			plus.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(numNodes < MAX_NODES){
						numNodes++;
						numNodesWanted.setText("" + numNodes);
					}
				}
				
			});
			minus.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if(numNodes > MIN_NODES){
						numNodes--;
						numNodesWanted.setText("" + numNodes);
					}
					//TODO add this to the new code
					if(numNodes <= numDegree)
					{
						numDegree = numNodes - 1;
						numDegreeWanted.setText("" + numDegree);
					}
				}
				
			});
		westFoundation.add(new JLabel("Number of Nodes: "), "North");
		westFoundation.add(minus, "West");
		westFoundation.add(numNodesWanted, "Center");
		westFoundation.add(plus, "East");
		
		JPanel eastFoundation = new JPanel();
		eastFoundation.setLayout(new BorderLayout());
			JButton plus2 = new JButton("+");
			numDegreeWanted = new JTextField(3);
				numDegreeWanted.setEditable(false);
				numDegreeWanted.setText("" + numDegree);
				numDegreeWanted.setHorizontalAlignment(JTextField.CENTER);
			JButton minus2 = new JButton("-");
			plus2.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if(numDegree < numNodes - 1)
					{
						numDegree++;
						numDegreeWanted.setText("" + numDegree);
					}
				}
				
			});
			minus2.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if(numDegree > MIN_DEGREE){
						numDegree--;
						numDegreeWanted.setText("" + numDegree);
					}
				}
				
			});
		eastFoundation.add(new JLabel("Average degree: "), "North");
		eastFoundation.add(minus2, "West");
		eastFoundation.add(numDegreeWanted, "Center");
		eastFoundation.add(plus2, "East");
		
		JPanel myPanel = new JPanel(new BorderLayout());
		myPanel.add(westFoundation, "North");
	    myPanel.add(Box.createHorizontalStrut(25));
		myPanel.add(eastFoundation, "South");
		
		int result = JOptionPane.showConfirmDialog(null, myPanel, "Create New Graph", JOptionPane.OK_CANCEL_OPTION);
		
		if(result == JOptionPane.OK_OPTION)
		{
			initializeGraph();
			graphicsSection.beginVisualization();
			//TODO REMOVE THIS!!
			//setSourceNodes();
			enableAllFields();
		}
		
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
		
		menuBar.add(fileMenu);
	}
	
	private void buildSettingsArea()
	{
		//Title Creation
		TitledBorder third, fourth, fifth, sixth, seventh;
		Border f3, f4, f5, f6, f7;
		f3 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		f4 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		f5 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		f6 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		f7 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
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
				srcNodes = new JTextField();
				destNodes = new JTextField();
				linksToBreak = new JTextField();
				packetsToSend = new JTextField("10");
				srcNodes.setHorizontalAlignment(SwingConstants.CENTER);
				destNodes.setHorizontalAlignment(SwingConstants.CENTER);
				linksToBreak.setHorizontalAlignment(SwingConstants.CENTER);
				packetsToSend.setHorizontalAlignment(SwingConstants.CENTER);
				srcNodes.setBorder(third);
				destNodes.setBorder(fourth);
				linksToBreak.setBorder(fifth);
				packetsToSend.setBorder(seventh);
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
			if(e.getSource() == aboutBtn)
			{
				JOptionPane.showMessageDialog(mainWindow, "NetVisulizer was created "
						+ "for the CPE400 class at UNR" + "\n\n" + "Created By: " + "\n" 
						+ "Mitchell Reyes" + "\n" + "Pattaphol Jirasessakul" + "\n" + "Zachary Waller");
			}
			
			if(e.getSource() == resetBtn)
			{
				resetBtnPressed();
			}
			
			if(e.getSource() == startNew || e.getSource() == newBtn)
			{
				newBtnPressed();
			}
			
			if(e.getSource() == visualize){
				graphicsSection.stopVisualization();
				graphicsSection.beginVisualization();
				breakLinks();
				setSourceNodes();
			}
			
		}
	}
}
