
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Stack;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.json.JSONObject;
import org.minueto.MinuetoTool;

public class GameScreen extends JPanel implements Serializable
{
	private JFrame mainFrame;
	private Integer width;
	private Integer height;
	
	private JLayeredPane boardGame_Layers;

	private Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
	
	private JLabel roundImage_TopLayer;
	private JLabel mapImage_BottomLayer;
	private JLabel informationCardImage_TopLayer;
	private JLabel deckOfTransportationCountersImage_TopLayer;
	private JLabel elfBoot1Image_TopLayer;
	private JLabel elfBoot2Image_TopLayer;
	
	private JPanel backgroundPanel_ForMap = new JPanel();
	private JPanel backgroundPanel_ForRound = new JPanel();
	private JPanel backgroundPanel_ForObstacle = new JPanel();
	private JPanel backgroundPanel_ForCards = new JPanel();
	private JPanel backgroundPanel_ForTransportationCounters = new JPanel();
	private JPanel backgroundPanel_ForInformationCard = new JPanel();
	private JPanel backgroundPanel_ForDeckOfTransportationCounters = new JPanel();
	private JPanel backgroundPanel_ForLeaderboard = new JPanel();

	// TODO: add a second one for the second boot and initialize it properly
	private JPanel panelForElfBoot = new JPanel();
	private JPanel[] panelForPlayerTransportationCounters = new JPanel[5];
	private JPanel[] panelForPlayerCards = new JPanel[8];
	private JPanel[] panelForFaceUpTransportationCounters = new JPanel[5];
	private JPanel panelForDeckOfTransportationCounters = new JPanel();
	private JPanel panelForObstacle = new JPanel();

	private JPanel panelForTownOfBeata = new JPanel();
	private JPanel panelForElfBoot1_TownOfBeata = new JPanel();
	private JPanel panelForElfBoot2_TownOfBeata = new JPanel();
	private JPanel panelForTownOfElvenhold = new JPanel();
	private JPanel panelForElfBoot1_TownOfElvenhold = new JPanel();
	private JPanel panelForElfBoot2_TownOfElvenhold = new JPanel();
	private Boolean elfBoot1Selected = false; // this is for our rudimentary implementation of moving the elf boot
	private Boolean elfBoot2Selected = false; // this is for our rudimentary implementation of moving the second elf boot
	private JPanel currentPanelOfElfBoot1 = panelForElfBoot1_TownOfElvenhold; // starting position
	private JPanel currentPanelOfElfBoot2 = panelForElfBoot2_TownOfElvenhold;

	private JTable leaderboard = new JTable();
	
	private Deck transportationCountersToDraw;
	//private String filepathToRepo = "/Users/nicktriantos/Desktop/f2021-hexanome-12"; // change this depending on whose machine we are using
	// private String filepathToRepo = "/Users/charlescouture/eclipse-workspace/COMP361";
	private String filepathToRepo = ".";
	private boolean isOurTurn;

	// TODO: change this on the other computer
	private String otherPlayerIP = "10.122.175.220"; // Nick's IP address

	
	// alternate constructor for networking demo
	GameScreen (JFrame frame, boolean isTurn)
	{
		// layout is necessary for JLayeredPane to be added to the JPanel
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		// Get dimensions of the full screen
		mainFrame = frame;
		width = mainFrame.getWidth();
		height = mainFrame.getHeight();

		// Set Bounds for entire Board Game screen and do the initialization of the structure for the UI
		boardGame_Layers = new JLayeredPane();
		boardGame_Layers.setBounds(0,0,width,height);
		initialization();

		// Add the images to their corresponding JPanel
		addImages();
		intializeListenerToDraw();
		
		// Add the JPanels to the main JLayeredPane with their corresponding layer
		addPanelToScreen();
		
		// Add the entire structure of the UI to the panel
		this.add(boardGame_Layers);

		// doing this for the demo
		isOurTurn = isTurn;
	}

	GameScreen (JFrame frame)
	{
		// layout is necessary for JLayeredPane to be added to the JPanel
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		// Get dimensions of the full screen
		mainFrame = frame;
		width = mainFrame.getWidth();
		height = mainFrame.getHeight();

		// Set Bounds for entire Board Game screen and do the initialization of the structure for the UI
		boardGame_Layers = new JLayeredPane();
		boardGame_Layers.setBounds(0,0,width,height);
		initialization();

		// Add the images to their corresponding JPanel
		addImages();
		intializeListenerToDraw();

		// Add the JPanels to the main JLayeredPane with their corresponding layer
		addPanelToScreen();

		// Add the entire structure of the UI to the panel
		this.add(boardGame_Layers);
	}



	public void update(JPanel panel)
	{
		panel.repaint();
		panel.revalidate();
	}




	public void initialization()
	{
		initializeMapImage();
		initializeRoundCardImage(1);
		initializeTransportationCountersAndObstacle();
		initializeBackgroundPanels();
		initializeCards();
		initializeInformationCardImage();
		initializeFaceUpTransportationCounters();
		initializeDeckOfTransportationCounters();
		initializeDeckOfTransportationCountersImage();
		initializeLeaderboard();
		initializeTransportationCounters();
		initializeLeaderboard();
		initializeElfBootImage();
		initializeTownOfBeata();
		initializeTownOfElvenhold();
	}
	
	public void initializeBackgroundPanels()
	{
		// Set Bounds for background Player Transportation Counter zone
		backgroundPanel_ForTransportationCounters.setBounds(width*0/1440, height*565/900, width*983/1440, height*70/900);
		backgroundPanel_ForTransportationCounters.setBackground(Color.DARK_GRAY);
		
		// Set Bounds for background Obstacle zone
		backgroundPanel_ForObstacle.setBackground(Color.RED);
		backgroundPanel_ForObstacle.setBounds(width*984/1440, height*565/900, width*80/1440, height*70/900);
		
		// Set Bounds for background Image zone
		backgroundPanel_ForMap.setBounds(width*0/1440, height*0/900, width*1064/1440, height*564/900);
		backgroundPanel_ForMap.setBackground(Color.BLUE);
		
		// Set Bounds for background Round zone
		backgroundPanel_ForRound.setBounds(width*932/1440, height*34/900, width*86/1440, height*130/900);
		backgroundPanel_ForRound.setOpaque(false);
		
		// Set Bounds for background Cards zone
		backgroundPanel_ForCards.setBounds(width*0/1440, height*566/900, width*1064/1440, height*333/900);
		backgroundPanel_ForCards.setBackground(Color.DARK_GRAY);
		
		// Set Bounds for background Information zone
		backgroundPanel_ForInformationCard.setBounds(width*1065/1440, height*565/900, width*375/1440, height*335/900);
		backgroundPanel_ForInformationCard.setBackground(Color.DARK_GRAY);
		
		// Set Bounds for background Face Up Transportation Counter zone
		backgroundPanel_ForDeckOfTransportationCounters.setBounds(width*1065/1440, height*275/900, width*375/1440, height*289/900);
		backgroundPanel_ForDeckOfTransportationCounters.setBackground(Color.DARK_GRAY);
		
		backgroundPanel_ForLeaderboard.setBounds(width*1065/1440, height*0/900, width*375/1440, height*274/900);
		backgroundPanel_ForLeaderboard.setBackground(Color.DARK_GRAY);
	}
	
	public void initializeDeckOfTransportationCounters()
	{
		Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
		
		JPanel panel1 = new JPanel();
		panel1.setBounds(width*1170/1440, height*290/900, width*70/1440, height*60/900);
		panel1.setOpaque(false);
		panel1.setBorder(whiteLine);
		panelForDeckOfTransportationCounters = panel1;
	}
	
	public void initializeFaceUpTransportationCounters()
	{
		Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
		JPanel panel2 = new JPanel();
		panel2.setBounds(width*1280/1440, height*290/900, width*70/1440, height*60/900);
		panel2.setOpaque(false);
		panel2.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[0] = panel2;
		
		JPanel panel3 = new JPanel();
		panel3.setBounds(width*1170/1440, height*390/900, width*70/1440, height*60/900);
		panel3.setOpaque(false);
		panel3.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[1] = panel3;
		
		JPanel panel4 = new JPanel();
		panel4.setBounds(width*1280/1440, height*390/900, width*70/1440, height*60/900);
		panel4.setOpaque(false);
		panel4.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[2] = panel4;
		
		JPanel panel5 = new JPanel();
		panel5.setBounds(width*1170/1440, height*490/900, width*70/1440, height*60/900);
		panel5.setOpaque(false);
		panel5.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[3] = panel5;
		
		JPanel panel6 = new JPanel();
		panel6.setBounds(width*1280/1440, height*490/900, width*70/1440, height*60/900);
		panel6.setOpaque(false);
		panel6.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[4] = panel6;
	}
	
	public void initializeLeaderboard()
	{	
		String[][] players = 
		{
				{"1", "0"},
				{"2", "0"},
				{"3", "0"},
				{"4", "0"},
				{"5", "0"},
				{"6", "0"},
		};
		String[] titles = {"PLAYERS", "POINTS"};
		
		leaderboard = new JTable (players, titles);
		leaderboard.setRowHeight(height*40/900);
		
		backgroundPanel_ForLeaderboard.setLayout(new BorderLayout());
		backgroundPanel_ForLeaderboard.add(leaderboard.getTableHeader(), BorderLayout.PAGE_START);
		backgroundPanel_ForLeaderboard.add(leaderboard, BorderLayout.CENTER);
		
		backgroundPanel_ForLeaderboard.add(leaderboard);
	}
	
	public void initializeCards()
	{
		int xCoordinate = width*2/1440;

		for (int i = 0; i < 8; i++)
		{
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			panel.setBorder(whiteLine);
			panel.setBounds(xCoordinate, height*637/900, width*130/1440, height*260/900);
			panelForPlayerCards[i] = panel;
			xCoordinate += width*133/1440;
		}
	}
	
	public void initializeTransportationCountersAndObstacle()
	{
		int xCoordinate = width*10/1440;
		
		// Transportation Counters
		for (int i = 0; i < 5; i++)
		{
			JPanel panel= new JPanel();
			panel.setOpaque(false);
			panel.setBorder(whiteLine);
			panel.setBounds(xCoordinate, height*570/900, width*70/1440, height*60/900);
			panelForPlayerTransportationCounters[i] = panel;
			xCoordinate += width*200/1440;
		}
		
		// Obstacle
		panelForObstacle.setOpaque(false);
		panelForObstacle.setBorder(whiteLine);
		panelForObstacle.setBounds(width*989/1440, height*570/900, width*70/1440, height*60/900);
	}


	public void initializeTownOfBeata()
	{
		// Town panel
		panelForTownOfBeata.setBounds(width*940/1440, height*392/900, width*74/1440, height*37/900);
		panelForTownOfBeata.setOpaque(false);
		panelForTownOfBeata.setBorder(whiteLine);

		panelForTownOfBeata.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e) {
				if (elfBoot1Selected && isOurTurn)
				{
					currentPanelOfElfBoot1.removeAll();
					update(currentPanelOfElfBoot1);

					panelForElfBoot1_TownOfBeata.add(elfBoot1Image_TopLayer);
					update(panelForElfBoot1_TownOfBeata);

					currentPanelOfElfBoot1 = panelForElfBoot1_TownOfBeata;
					elfBoot1Selected = false;

					isOurTurn = !isOurTurn;

					try
					{
						sendGameState(currentPanelOfElfBoot1);
					}

					catch (IOException problem)
					{
						// do nothing. we are screwed
						System.out.println("We ran into an IOException when trying to send the game state over to the other player.");
						problem.printStackTrace();
					}
				}
				else if (elfBoot2Selected && isOurTurn)
				{
					currentPanelOfElfBoot2.removeAll();
					update(currentPanelOfElfBoot2);

					panelForElfBoot2_TownOfBeata.add(elfBoot2Image_TopLayer);
					update(panelForElfBoot2_TownOfBeata);

					currentPanelOfElfBoot2 = panelForElfBoot2_TownOfBeata;
					elfBoot2Selected = false;

					isOurTurn = !isOurTurn;

					try
					{
						sendGameState(currentPanelOfElfBoot2);
					}

					catch (IOException problem)
					{
						// do nothing. we are screwed
						System.out.println("We ran into an IOException when trying to send the game state over to the other player.");
						problem.printStackTrace();
					}

				}

			}
		});

		// Boot panel for boot 1
		panelForElfBoot1_TownOfBeata.setBounds(width*935/1440, height*430/900, width*24/1440, height*24/900);
		panelForElfBoot1_TownOfBeata.setOpaque(false);
		panelForElfBoot1_TownOfBeata.setBorder(whiteLine);

		// Boot panel for boot 2
		// TODO: write the code for this, play with the positioning a bit
	}

	private void initializeTownOfElvenhold()
	{
		// Town panel
		panelForTownOfElvenhold.setBounds(width*750/1440, height*275/900, width*115/1440, height*70/900);
		panelForTownOfElvenhold.setOpaque(false);
		panelForTownOfElvenhold.setBorder(whiteLine);

		panelForTownOfElvenhold.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (elfBoot1Selected && isOurTurn)
				{
					currentPanelOfElfBoot1.removeAll();
					update(currentPanelOfElfBoot1);

					panelForElfBoot1_TownOfElvenhold.add(elfBoot1Image_TopLayer);
					update(panelForElfBoot1_TownOfElvenhold);

					currentPanelOfElfBoot1 = panelForElfBoot1_TownOfElvenhold;
					elfBoot1Selected = false;

					isOurTurn = !isOurTurn;
					try
					{
						sendGameState(currentPanelOfElfBoot1);
					}

					catch (IOException problem)
					{
						// do nothing. we are screwed
						System.out.println("We ran into an IOException when trying to send the game state over to the other player.");
						problem.printStackTrace();
					}
				}

				else if (elfBoot2Selected && isOurTurn)
				{
					currentPanelOfElfBoot2.removeAll();
					update(currentPanelOfElfBoot2);

					panelForElfBoot2_TownOfBeata.add(elfBoot2Image_TopLayer);
					update(panelForElfBoot2_TownOfBeata);

					currentPanelOfElfBoot2 = panelForElfBoot2_TownOfBeata;
					elfBoot2Selected = false;

					isOurTurn = !isOurTurn;
					try
					{
						sendGameState(currentPanelOfElfBoot2);
					}

					catch (IOException problem)
					{
						// do nothing. we are screwed
						System.out.println("We ran into an IOException when trying to send the game state over to the other player.");
						problem.printStackTrace();
					}


				}
			}
		});

		// Boot panel
		panelForElfBoot1_TownOfElvenhold.setBounds(width*750/1440, height*346/900, width*24/1440, height*24/900);
		panelForElfBoot1_TownOfElvenhold.setOpaque(false);
		panelForElfBoot1_TownOfElvenhold.setBorder(whiteLine);

		// Boot panel for second boot
		// TODO: write the code to intialize this, play around with the dimensions
	}
	
	public void initializeMapImage()
	{
		ImageIcon mapImage = new ImageIcon(filepathToRepo + "/assets/sprites/map.png");
		Image map = mapImage.getImage();
		Image mapResized = map.getScaledInstance(width*1064/1440, height*564/900,  java.awt.Image.SCALE_SMOOTH);
		mapImage = new ImageIcon(mapResized);
		mapImage_BottomLayer = new JLabel(mapImage);
	}
	
	public void initializeRoundCardImage(int round)
	{
		String image = filepathToRepo + "R" + String.valueOf(round) + ".png";
		ImageIcon roundImage = new ImageIcon(filepathToRepo + "/assets/sprites/R1.png");
		Image Round = roundImage.getImage();
		Image RoundResized = Round.getScaledInstance(width*90/1440, height*130/900,  java.awt.Image.SCALE_SMOOTH);
		roundImage = new ImageIcon(RoundResized);
		roundImage_TopLayer = new JLabel(roundImage);
	}
	
	public void initializeInformationCardImage()
	{
		ImageIcon gridImage = new ImageIcon(filepathToRepo + "/assets/sprites/grid.png");
		Image grid = gridImage.getImage();
		Image gridResized = grid.getScaledInstance(width*360/1440, height*325/900,  java.awt.Image.SCALE_SMOOTH);
		gridImage = new ImageIcon(gridResized);
		informationCardImage_TopLayer = new JLabel(gridImage);
	}
	
	public void initializeDeckOfTransportationCountersImage()
	{
		ImageIcon gridImage = new ImageIcon(filepathToRepo + "/assets/sprites/M08.png");
		Image grid = gridImage.getImage();
		Image gridResized = grid.getScaledInstance(width*67/1440, height*52/900,  java.awt.Image.SCALE_SMOOTH);
		gridImage = new ImageIcon(gridResized);
		deckOfTransportationCountersImage_TopLayer = new JLabel(gridImage);
	}

	/**
	 * editing this method to initialize EXACTLY two elf boots for the networking demo
	 * we will have to change it later
	 */
	public void initializeElfBootImage() // for demo
	{

		// we will represent the ElfBoot as a JLabel with a ClickAdapter
		// initializing (black) boot 1 here
		ImageIcon blackBootIcon = new ImageIcon(filepathToRepo + "/assets/boppels-and-boots/boppel-black.png");
		Image blackBootImage = blackBootIcon.getImage();
		Image blackBootResized = blackBootImage.getScaledInstance(width*15/1440, height*15/900,  java.awt.Image.SCALE_SMOOTH);
		blackBootIcon = new ImageIcon(blackBootResized);
		elfBoot1Image_TopLayer = new JLabel(blackBootIcon);

		elfBoot1Image_TopLayer.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				// this toggles the elfBootSelected to determine if it is possible to move a boot
				elfBoot1Selected = true;
			}
		});

		// now we will initialize the second boot (blue)

		ImageIcon blueBootIcon = new ImageIcon(filepathToRepo + "/assets/boppels-and-boots/boppel-blue.png");
		Image blueBootImage = blueBootIcon.getImage();
		Image blueBootResized = blueBootImage.getScaledInstance(width*15/1440, height*15/900,  java.awt.Image.SCALE_SMOOTH);
		blueBootIcon = new ImageIcon (blueBootResized);
		elfBoot2Image_TopLayer = new JLabel(blueBootIcon);

		elfBoot2Image_TopLayer.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				// this toggles the elfBootSelected to determine if it is possible to move a boot
				elfBoot2Selected = true;
			}
		});




	}
	
	public void addImages()
	{
		backgroundPanel_ForMap.add(mapImage_BottomLayer);		
		backgroundPanel_ForRound.add(roundImage_TopLayer);
		backgroundPanel_ForInformationCard.add(informationCardImage_TopLayer);
		panelForDeckOfTransportationCounters.add(deckOfTransportationCountersImage_TopLayer);
		panelForElfBoot1_TownOfElvenhold.add(elfBoot1Image_TopLayer);
		panelForElfBoot2_TownOfElvenhold.add(elfBoot2Image_TopLayer);
	}
	
	public void addPanelToScreen()
	{
		boardGame_Layers.add(backgroundPanel_ForRound, 0);
		boardGame_Layers.add(panelForDeckOfTransportationCounters,0);
		addFaceUpTransportationCounters();
		addTransportationCountersAndObstacle();
		addCards();
		boardGame_Layers.add(backgroundPanel_ForTransportationCounters, -1);
		boardGame_Layers.add(backgroundPanel_ForMap, -1); 
		boardGame_Layers.add(backgroundPanel_ForObstacle,-1);
		boardGame_Layers.add(backgroundPanel_ForCards, -1);
		boardGame_Layers.add(backgroundPanel_ForInformationCard, -1);
		boardGame_Layers.add(backgroundPanel_ForDeckOfTransportationCounters, -1);

		boardGame_Layers.add(backgroundPanel_ForLeaderboard,-1);
		boardGame_Layers.add(panelForElfBoot1_TownOfBeata, 0);
		boardGame_Layers.add(panelForElfBoot2_TownOfBeata, 0);
		boardGame_Layers.add(panelForTownOfBeata,0);
		boardGame_Layers.add(panelForTownOfElvenhold,0);
		boardGame_Layers.add(panelForElfBoot1_TownOfElvenhold,0);
		boardGame_Layers.add(panelForElfBoot2_TownOfElvenhold, 0);
	}
	
	public void addFaceUpTransportationCounters()
	{	
		for (JPanel panel : panelForFaceUpTransportationCounters)
		{
			boardGame_Layers.add(panel, 0);
		}
	}
	
	public void addCards()
	{	
		for (JPanel panel : panelForPlayerCards)
		{
			boardGame_Layers.add(panel, 0);
		}
	}
	
	public void addTransportationCountersAndObstacle()
	{
		// Transportation Counters
		for (JPanel panel : panelForPlayerTransportationCounters)
		{
			panel.setOpaque(false);
			boardGame_Layers.add(panel, 0);
		}
		
		// Obstacle
		boardGame_Layers.add(panelForObstacle,0);
	}

	public void initializeTransportationCounters()
	{
		// we have 8 of each counter in the game
		Stack<TransportationCounter> toAddToDeck = new Stack<TransportationCounter>();

		for (int i = 1; i <= 8; i++)
		{
			toAddToDeck.push(new TransportationCounter(TransportationCounter.CounterType.DRAGON, width, height));
			toAddToDeck.push(new TransportationCounter(TransportationCounter.CounterType.GIANTPIG, width, height));
			toAddToDeck.push(new TransportationCounter(TransportationCounter.CounterType.UNICORN, width, height));
			toAddToDeck.push(new TransportationCounter(TransportationCounter.CounterType.TROLLWAGON, width, height));
			toAddToDeck.push(new TransportationCounter(TransportationCounter.CounterType.ELFCYCLE, width, height));
			toAddToDeck.push(new TransportationCounter(TransportationCounter.CounterType.MAGICCLOUD, width, height));
		}

		transportationCountersToDraw = new Deck(toAddToDeck);
	}
	
	public void intializeListenerToDraw()
	{
		// here we will add a MouseListener to deckOfTransportationCountersImage_TopLayer

		deckOfTransportationCountersImage_TopLayer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TransportationCounter drawn = transportationCountersToDraw.draw(); // draw a counter
				for (int i = 0; i < 5; i++)
				{
					if (panelForPlayerTransportationCounters[i].getComponentCount() == 0) // if spot is empty, put card there
					{
						panelForPlayerTransportationCounters[i].add(drawn.getDisplay());
						mainFrame.repaint();
						mainFrame.revalidate();
						break;
					}
					// if we don't find an open spot, we do nothing. we can't put the counter anywhere.
				}
			}
		});
	}

	public void moveElfBoot1(JPanel newCurrentPanel)
	{
		// this is what we will use to update the game state based on the information sent over the network

		currentPanelOfElfBoot1.remove(elfBoot1Image_TopLayer);
		update(currentPanelOfElfBoot1);

		// now switch the current panel

		currentPanelOfElfBoot1 = newCurrentPanel;
		currentPanelOfElfBoot1.add(elfBoot1Image_TopLayer);
		update(currentPanelOfElfBoot1);
	}

	public void setCurrentPanelOfElfBoot2(JPanel currentPanel)
	{
		currentPanelOfElfBoot2 = currentPanel;
	}

	/*
	public void setOurTurn()
	{

	}

	 */

	public static void main(String[] args) 
	{
		JFrame game_screen = new JFrame("GameScreen");
		
		game_screen.setSize(MinuetoTool.getDisplayWidth(), MinuetoTool.getDisplayHeight());
		game_screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		game_screen.add(new GameScreen(game_screen, true));
		game_screen.setVisible(true);
	}

	public void sendGameState(JPanel elfBootLocation) throws IOException
	{
		System.out.println("Sending the game state...");
		Socket connection = new Socket(otherPlayerIP, 4444); // start up the connection
		System.out.println("Outwards socket is up and running...");

		// send the Elf boot's new location
		JPanel toSend = elfBootLocation;
		OutputStream out = connection.getOutputStream();
		ObjectOutputStream payload = new ObjectOutputStream(out);
		payload.writeObject(toSend);
		System.out.println("Done writing the elf boot current location into payload...");
		payload.flush();
		System.out.println("Payload has been flushed.");
		connection.close();
		System.out.print("Done. connection has been closed.");


	}

	public void listen(int port) throws IOException
	{
		ServerSocket listener = new ServerSocket(port);
	}

	public boolean isItOurTurn()
	{return isOurTurn;}




	// only using this for the demo. See NetworkDemoPlayer2 loop
	public void reverseTurn()
	{
		isOurTurn = !isOurTurn;
	}

}
