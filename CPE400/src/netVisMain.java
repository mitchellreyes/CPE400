import java.awt.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;


public class netVisMain extends JFrame{
	private static final long serialVersionUID = 1L;

	//###GUI VARIABLES###
	public JFrame mainWindow;
		protected JPanel layer1grid;
			protected JTextArea visualizerSection;
			protected JPanel NORTHWEST;
				protected JTextField one;
				protected JTextField two;
				protected JTextField three;
				protected JTextField four;
			protected JTextArea SOUTHEAST;
			protected JTextArea SOUTHWEST;
			
	public settingsManager settings;
	
	//MenuBar variables
	protected JMenuBar menuBar;
		protected JMenu fileMenu;
			protected JMenuItem aboutBtn;
			protected JMenuItem settingsBtn;
	
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
				
		//###layer1grid initializers
		layer1grid = new JPanel();
		layer1grid.setLayout(new BorderLayout());
			visualizerSection = new JTextArea("[Placeholder area for visualizer]");
			visualizerSection.setEditable(false);
		layer1grid.add(visualizerSection, "Center");

		//###MenuBar initializers
		menuBar = new JMenuBar();
			fileMenu = new JMenu("File");
				aboutBtn = new JMenuItem("About");
				aboutBtn.addActionListener(new ActionHandler());
				aboutBtn.setAccelerator(KeyStroke.getKeyStroke('T', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
				settingsBtn = new JMenuItem("Settings");
				settingsBtn.addActionListener(new ActionHandler());
				settingsBtn.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
			fileMenu.add(settingsBtn);
			fileMenu.add(aboutBtn);
		
		menuBar.add(fileMenu);
		
		mainWindow.getContentPane().add(layer1grid, "Center");
		mainWindow.getContentPane().add(menuBar, "North");
		mainWindow.setVisible(true);
	}
	
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
			
			if(e.getSource() == settingsBtn)
			{
				settings = new settingsManager();
			}
		}
	}
	
}
