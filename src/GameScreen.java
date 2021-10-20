import java.awt.Color;
import java.awt.Image;

import javax.swing.*;
import javax.swing.border.Border;

import org.minueto.MinuetoTool;

public class GameScreen extends JPanel
{
	private JFrame mainFrame;
	private int width;
	private int height;
	
	private JLayeredPane boardGame_Layers = new JLayeredPane();
	
	private JLabel roundImage_Layer1;
	private JLabel mapImage_Layer2;
	
	private JPanel panelForMap = new JPanel();
	private JPanel panelForRound = new JPanel();
	private JPanel panelForTransportationCounters = new JPanel();
	private JPanel[] playerTransportationCounters = new JPanel[5];
	
	GameScreen (JFrame frame)
	{
		// Get dimensions of the full screen
		mainFrame = frame;
		width = mainFrame.getWidth();
		height = mainFrame.getHeight();
		
		// Set Bounds for entire Board Game screen and do the initialization
		boardGame_Layers.setBounds(0,0,width,height);	
		initializeMapImage();
		initializeRounCardImage(1);
		initializeTransportationCounters();

		// Add the map image to its corresponding JPanel
		panelForMap.setBounds(0, 0, 1064, 564);
		panelForMap.setBackground(Color.BLUE);
		panelForMap.add(mapImage_Layer2);		
		
		// Add the Round image to its corresponding JPanel
		panelForRound.setBounds(932, 39, 86, 94);
		panelForRound.setBackground(Color.WHITE);
		panelForRound.add(roundImage_Layer1);
		
		// Set Bound for Transportation Counter zone
		panelForTransportationCounters.setBounds(0, 565, 983, 70);
		panelForTransportationCounters.setBackground(Color.BLUE);
		
		// Add the JPanels to the main Panel
		boardGame_Layers.add(panelForRound, 1); 
		addTransportationCounters();
		boardGame_Layers.add(panelForTransportationCounters, 6);
		boardGame_Layers.add(panelForMap, 2); 
		
		// Add Board Game with Round on top of it to the main screen
		mainFrame.add(boardGame_Layers);
		
		JPanel panel3 = new JPanel();
		panel3.setBackground(Color.GREEN);
		panel3.setBounds(984, 565, 80, 70);
		mainFrame.add(panel3);
	}
	
	public void addTransportationCounters()
	{
		for (JPanel panel : playerTransportationCounters)
		{
			boardGame_Layers.add(panel, 1);
		}
	}
	
	public void initializeTransportationCounters()
	{
		int xCoordinate = 5;
		
		for (int i = 0; i < 5; i++)
		{
			JPanel panelForTransportationCounters = new JPanel();
			panelForTransportationCounters.setBackground(Color.WHITE);
			Border blackline = BorderFactory.createLineBorder(Color.black);
			panelForTransportationCounters.setBorder(blackline);
			panelForTransportationCounters.setBounds(xCoordinate, 565, 80, 70);
			
			playerTransportationCounters[i] = panelForTransportationCounters;
			xCoordinate += 200;
		}
	}
	
	public void initializeMapImage()
	{
		ImageIcon mapImage = new ImageIcon(getClass().getResource("map.png"));
		Image map = mapImage.getImage();
		Image mapResized = map.getScaledInstance(1064, 564,  java.awt.Image.SCALE_SMOOTH);
		mapImage = new ImageIcon(mapResized);
		mapImage_Layer2 = new JLabel(mapImage);
	}
	
	public void initializeRounCardImage(int round)
	{
		String image = "R" + String.valueOf(round) + ".png";
		ImageIcon roundImage = new ImageIcon(getClass().getResource(image));
		Image Round = roundImage.getImage();
		Image RoundResized = Round.getScaledInstance(90, 90,  java.awt.Image.SCALE_SMOOTH);
		roundImage = new ImageIcon(RoundResized);
		roundImage_Layer1 = new JLabel(roundImage);
	}
	
	public static void main(String[] args) 
	{
		JFrame game_screen = new JFrame("GameScreen");
		game_screen.setSize(MinuetoTool.getDisplayWidth() - 100, MinuetoTool.getDisplayHeight() - 100);
		
		game_screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		game_screen.add(new GameScreen(game_screen));
		game_screen.setVisible(true);
		
	}

}
