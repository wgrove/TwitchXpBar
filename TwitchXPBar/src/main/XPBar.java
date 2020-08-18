package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class XPBar {

	private static final String propsPath = "./xpBar.properties";

	private JFrame frame;
	private XPBarFrame xpBarFrame;
	
	private StreamLabelWatcher watcher;
	
	private Properties props;
	private String streamLabelsDirectoryPath;

	// Default frame color is Black
	Color frameColor = Color.black;

	// Default key color is Green
	Color keyColor = Color.green;

	// Default text color
	Color textColor = Color.white;

	// Default XP Bar Color
	Color xpBarColor = Color.cyan;
	
	// Default XP+ color
	Color xpPlusColor = Color.cyan;
	
	// Default XP- Color
	Color xpMinusColor = Color.red;
	
	// Default LevelUp Color
	Color levelUpColor = Color.orange;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					XPBar window = new XPBar();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public XPBar() {
		readProps();
		initialize();
	}

	private void readProps() {

		props = new Properties();

		try {
			final FileInputStream in = new FileInputStream(propsPath);

			try {
				props.load(in);
			} catch (IOException e) {
				// Write a log saying the properties were not read correctly
				in.close();
			}

		} catch (FileNotFoundException e) {
			// TODO: Write log indicating the file was not found

			writeDefaultConfig();

		} catch (IOException e) {
			// TODO: Write log indicating the input stream was not closed properly
		}

	}

	private boolean isValidColorValues(Integer r, Integer g, Integer b) {

		return (r >= 0 && r <= 255) && (g >= 0 && g <= 255) && (b >= 0 && b <= 255);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		getWidgetColors();

		frame = new JFrame("XPBar Widget");
		frame.setBounds(100, 100, 300, 104);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(keyColor);
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setOpaque(false);
		panel.setBackground(keyColor);

		xpBarFrame = new XPBarFrame(frameColor, textColor, xpBarColor, xpPlusColor, xpMinusColor, levelUpColor);
		
		watcher = new StreamLabelWatcher(streamLabelsDirectoryPath);
		watcher.registerGui(xpBarFrame);
		
		frame.getContentPane().add(xpBarFrame, BorderLayout.CENTER);
	}

	private void writeDefaultConfig() {

		try {
			FileWriter writer = new FileWriter(propsPath);

			try {

				for(String prop : XPBarResources.xpBarProperties) {
					writer.write(prop+"\n");
				}

			} catch (IOException exception) {
				// TODO: Add log
			}

			writer.close();
		} catch (IOException e) {
			// TODO: Add Log
		}

	}

	private void getWidgetColors() {

		try {

			Color tempFrameColor = frameColor;
			Color tempKeyColor = keyColor;
			Color tempTextColor = textColor;
			Color tempXpBarColor = xpBarColor;
			Color tempXpPlusColor = xpPlusColor;
			Color tempXpMinusColor = xpMinusColor;
			Color tempLevelUpColor = levelUpColor;
			
			streamLabelsDirectoryPath = props.getProperty("StreamLabelsDirectoryPath");
			
			Integer r = Integer.parseInt(props.getProperty("FrameColorR"));
			Integer g = Integer.parseInt(props.getProperty("FrameColorG"));
			Integer b = Integer.parseInt(props.getProperty("FrameColorB"));

			if(isValidColorValues(r, g, b)) {
				tempFrameColor = new Color(r, g, b);
			}

			r = Integer.parseInt(props.getProperty("KeyColorR"));
			g = Integer.parseInt(props.getProperty("KeyColorG"));
			b = Integer.parseInt(props.getProperty("KeyColorB"));

			if(isValidColorValues(r, g, b)) {
				tempKeyColor = new Color(r, g, b);
			}

			r = Integer.parseInt(props.getProperty("TextColorR"));
			g = Integer.parseInt(props.getProperty("TextColorG"));
			b = Integer.parseInt(props.getProperty("TextColorB"));

			if(isValidColorValues(r, g, b)) {
				tempTextColor = new Color(r, g, b);
			}

			r = Integer.parseInt(props.getProperty("XPBarColorR"));
			g = Integer.parseInt(props.getProperty("XPBarColorG"));
			b = Integer.parseInt(props.getProperty("XPBarColorB"));

			if(isValidColorValues(r, g, b)) {
				tempXpBarColor = new Color(r, g, b);
			}
			
			r = Integer.parseInt(props.getProperty("XPPlusColorR"));
			g = Integer.parseInt(props.getProperty("XPPlusColorG"));
			b = Integer.parseInt(props.getProperty("XPPlusColorB"));

			if(isValidColorValues(r, g, b)) {
				tempXpPlusColor = new Color(r, g, b);
			}
			
			r = Integer.parseInt(props.getProperty("XPMinusColorR"));
			g = Integer.parseInt(props.getProperty("XPMinusColorG"));
			b = Integer.parseInt(props.getProperty("XPMinusColorB"));

			if(isValidColorValues(r, g, b)) {
				tempXpMinusColor = new Color(r, g, b);
			}
			
			r = Integer.parseInt(props.getProperty("LevelUpColorR"));
			g = Integer.parseInt(props.getProperty("LevelUpColorG"));
			b = Integer.parseInt(props.getProperty("LevelUpColorB"));

			if(isValidColorValues(r, g, b)) {
				tempLevelUpColor = new Color(r, g, b);
			}

			frameColor = tempFrameColor;
			textColor = tempTextColor;
			keyColor = tempKeyColor;
			xpBarColor = tempXpBarColor;
			xpPlusColor = tempXpPlusColor;
			xpMinusColor = tempXpMinusColor;
			levelUpColor = tempLevelUpColor;

		} catch (NumberFormatException exception) {
			//TODO: Add log saying that RGB values must be a positive integer between 0 and 255 inclusively;
		} catch (NullPointerException exception) {
			//TODO: Add log saying the config is missing a property, and that defaults will be used.
			// Recommend deleting old properties file to rewrite another.
			// Also recommend potentially backing up old properties file.
		}
	}

}
