import javax.swing.*;

import java.awt.*;

import java.awt.event.*;

import java.awt.image.*;

/**
 * The BigTwoGUI class implements the CardGameUI interface. It is used to build a GUI for
 * the Big Two card game and handle all user actions. Below is a detailed description for the
 * BigTwoGUI class.
 * 
 * @author Bryan Melvison
 */

public class BigTwoGUI {
	
	private BigTwo game;
	
	private boolean[] selected;
	
	private int activePlayer;
	
	private JFrame frame;
	
	private JPanel bigTwoPanel;
	
	private JButton playButton;
	
	private JButton passButton;
	
	private JTextArea msgArea;
	
	private JTextArea chatArea;
	
	private JTextField chatInput;
	
	private BigTwoClient bigTwoClient;
	
	// Stores an array of images  for the character
	private Image[] anya;
	
	// Stores the images of the entire cards
	private Image[][] cards;
	
	//The back image of the card
	private Image back;
	
	/**
	 * a constructor for creating a BigTwoGUI. The parameter
	 * game is a reference to a Big Two card game associates with this GUI.
	 * 
	 * @param game is a reference to a Big Two card game associates with this GUI.
	 */
	
	public BigTwoGUI(BigTwo game) {
		this.game = game;
		
		selected = new boolean[13];
		setActivePlayer(game.getCurrentPlayerIdx());
		
		//Creating a new frame
		JFrame frame = new JFrame("Big Two"); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setMinimumSize(new Dimension(1400, 1050));

		//Setting the anyas (players)
		anya = new Image[4];
		anya[0] = new ImageIcon("Anya0.jpg").getImage();
		anya[0] = anya[0].getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		
		anya[1] = new ImageIcon("Anya1.jpg").getImage();
		anya[1] = anya[1].getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		
		anya[2] = new ImageIcon("Anya2.png").getImage();
		anya[2] = anya[2].getScaledInstance(60, 60, Image.SCALE_SMOOTH);
	
		anya[3] = new ImageIcon("Anya3.png").getImage();
		anya[3] = anya[3].getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		
		//Creating the cards and the back of the card
		
		cards = new Image[4][13];
		String suit[] = {"d", "c", "h", "s"};
		String rank[] = {"a", "2", "3", "4", "5", "6", "7", "8", "9", "t", "j", "q", "k"};
		for(int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				//i is suit j is rank
				cards[i][j] = new ImageIcon(rank[j]+suit[i]+".gif").getImage();
				cards[i][j] = cards[i][j].getScaledInstance(100, 100, Image.SCALE_SMOOTH);
			}
		}
		
		back = new ImageIcon("cardback.png").getImage();
		back = back.getScaledInstance(100, 100, Image.SCALE_SMOOTH);

		
		//Setting the bigtwopanel
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setLayout(new BoxLayout(bigTwoPanel, BoxLayout.Y_AXIS));
		bigTwoPanel.addMouseListener(new BigTwoPanel());
		
		
		//Creating main menu
		JMenuBar menu = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		JMenu messageMenu = new JMenu("Message");
		
		//Components of the menu
		JMenuItem connect = new JMenuItem("Connect");
		connect.addActionListener(new ConnectMenuItemListener());
		
		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(new QuitMenuItemListener());
		
		JMenuItem clear = new JMenuItem("Dummy Button");
		clear.addActionListener(new DummyMenuItemListener());
		
		//Adding the components into the menu
		gameMenu.add(connect);
		gameMenu.add(quit);
		messageMenu.add(clear);
		
		menu.add(gameMenu);
		menu.add(messageMenu);
		
		//Creating the bottom panel which includes play, pass and chat input area
		JPanel panel_bottom = new JPanel();
		panel_bottom.setBackground(Color.lightGray);
		Font font = new Font("Comic Sans MS", Font.ROMAN_BASELINE, 14);
		
		//Play Button
		this.playButton = new JButton("Play");
		this.playButton.setFont(font);
		this.playButton.addActionListener(new PlayButtonListener());
		
		//Pass Button 
		this.passButton = new JButton("Pass");	
		this.passButton.setFont(font);
		this.passButton.addActionListener(new PassButtonListener());
		
		//Chat Input Area
		JLabel chatlabel = new JLabel("Message: ");
		this.chatInput = new JTextField(20);
		this.chatInput.addKeyListener(new ChatKeyListener());
		
		//Adding pass, play, and chat input area
		panel_bottom.add(Box.createHorizontalStrut(40));
		panel_bottom.add(this.playButton);
		panel_bottom.add(Box.createHorizontalStrut(10));
		panel_bottom.add(this.passButton);
		panel_bottom.add(Box.createHorizontalStrut(10));
		panel_bottom.add(chatlabel);
		panel_bottom.add(this.chatInput);
		
		//Chat Area
		
		msgArea = new JTextArea(100, 45);
	    msgArea.setLineWrap(true);
	    msgArea.setEditable(false);
	    msgArea.setFont(font);

	    JScrollPane scroller_msgarea = new JScrollPane(msgArea);
	    scroller_msgarea.setVerticalScrollBarPolicy(
	             ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    scroller_msgarea.setHorizontalScrollBarPolicy(
	             ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	    scroller_msgarea.setPreferredSize(new Dimension(350, 175));
	    scroller_msgarea.setMaximumSize(new Dimension(450,450));
	    scroller_msgarea.setMinimumSize(new Dimension(100,100));
	    
	    chatArea = new JTextArea(100,55);
	    chatArea.setLineWrap(true);
	    chatArea.setEditable(false);
	    chatArea.setFont(font);
	    
	    JScrollPane scroller_chatarea = new JScrollPane(chatArea);
	    scroller_chatarea.setVerticalScrollBarPolicy(
	             ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    scroller_chatarea.setHorizontalScrollBarPolicy(
	             ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	    scroller_chatarea.setPreferredSize(new Dimension(350,175));
	    scroller_chatarea.setMaximumSize(new Dimension(450,450));
	    scroller_chatarea.setMinimumSize(new Dimension(100,100));
	    
		JPanel chatpanel = new JPanel();
		chatpanel.setLayout(new BoxLayout(chatpanel, BoxLayout.Y_AXIS));

		chatpanel.add(scroller_msgarea);
		chatpanel.add(scroller_chatarea);
		chatpanel.setPreferredSize(new Dimension(315, 375));
	    chatpanel.setMaximumSize(new Dimension(400,400));
	    chatpanel.setMinimumSize(new Dimension(225,225));
	    
	    
		//Creating the frame
	    frame.setJMenuBar(menu);
	    frame.add(bigTwoPanel, BorderLayout.CENTER);
		frame.add(panel_bottom, BorderLayout.SOUTH);
		frame.add(chatpanel, BorderLayout.EAST);
		frame.setVisible(true);
			
	}
	
	/**
	 * a method for setting the index of the active player
	 * 
	 * @param activePlayer The index of the active player.
	 */
	
	public void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
	}
	
	/**
	 *  a method for repainting the GUI. 
	 */
	
	public void repaint() {
		resetSelected();
		bigTwoPanel.removeAll();
		bigTwoPanel.repaint();
		
		if(game.endOfGame() != true) {
			resetSelected();
			if(game.getCurrentPlayerIdx() == activePlayer) {
				promptActivePlayer();
			}
			else {
				printMsg(game.getPlayerList().get(game.getCurrentPlayerIdx()).getName() + "'s turn:");
			}
		}
		
		//Handle enabling and disabling of buttons depending on who's active
		if(game.getCurrentPlayerIdx() == activePlayer && game.getBigTwoClient().getanotherflag() == false) {
			enable();
		}
		else {
			disable();
		}		
	}
	
	/**
	 * a method for clearing the message area of the GUI. 
	 */
	
	public void clearMsgArea() {
		msgArea.setText("");
	}
	
	/**
	 * a method for resetting the GUI. You should (i) reset the list of selected cards; 
	 * (ii) clear the message area; and (iii) enable user interactions.
	 */
	
	public void reset() {
		this.resetSelected();
		clearMsgArea();
		chatArea.setText("");
		enable();
	}
	
	/**
	 * a method for enabling user interactions with the GUI. 
	 * You should (i) enable the “Play” button and “Pass” button (i.e., making them clickable); 
	 * and (ii) enable the BigTwoPanel for selection of cards through mouse clicks.
	 */
	
	public void enable() {
		this.playButton.setEnabled(true);
		this.passButton.setEnabled(true);
//		this.chatInput.setEnabled(true);
		this.bigTwoPanel.setEnabled(true);
	}
	
	/**
	 * a method for disabling user interactions with the GUI. 
	 * You should (i) disable the “Play” button and “Pass” button 
	 * (i.e., making them not clickable); 
	 * and (ii) disable the BigTwoPanel for selection of cards through mouse clicks.
	 */
	
	public void disable() {
		this.playButton.setEnabled(false);
		this.passButton.setEnabled(false);
//		this.chatInput.setEnabled(false);
		this.bigTwoPanel.setEnabled(false);
	}
	
	/**
	 * a method for prompting the active player to select cards
	 * and make his/her move. A message should be displayed in the message area showing it
	 * is the active player’s turn.
	 */
	
	public void promptActivePlayer() {
		this.printMsg(game.getPlayerList().get(activePlayer).getName() + "'s turn: ");
		resetSelected();
	}
	
	//Returns an array of indices of the cards selected through the UI.
	private int[] getSelected(){
		int[] cardIdx = null;
		int count = 0;
		for (int j = 0; j < selected.length; j++) {
			if (selected[j]) {
				count++;
			}
		}

		if (count != 0) {
			cardIdx = new int[count];
			count = 0;
			for (int j = 0; j < selected.length; j++) {
				if (selected[j]) {
					cardIdx[count] = j;
					count++;
				}
			}
		}
		return cardIdx;
	}
	
	// Reset the cards selected through the UI.
	private void resetSelected(){
		for (int j = 0; j < this.selected.length; j++) {
			this.selected[j] = false;
		}
	}
	
	private int getPlayerId() {
		return game.getBigTwoClient().getPlayerID();
	}
	/**
	 * – an inner class that extends the JPanel class and implements the
	 * MouseListener interface. Overrides the paintComponent() method inherited from the
	 * JPanel class to draw the card game table. Implements the mouseReleased() method
	 * from the MouseListener interface to handle mouse click events. 
	 * 
	 * @author Bryan Melvison
	 */
	
	public class BigTwoPanel extends JPanel implements MouseListener{
		
		
		public void paintComponent(Graphics g) {
			
			//Setting the background of the panel
			super.paintComponent(g);
			Graphics g2D = (Graphics2D) g;
			this.setBackground(new Color(0, 163, 108));
			Font changefont = new Font("Comic Sans MS", Font.ROMAN_BASELINE, 14);
			g2D.setFont(changefont);
			
			//create the entire layout of the entire big two panel
			g.drawImage(anya[0], 30, 35, bigTwoPanel);
			g.drawImage(anya[0], 30, 175, bigTwoPanel);
			g.drawImage(anya[0], 30, 315, bigTwoPanel);
			g.drawImage(anya[0], 30, 455, bigTwoPanel);
		
			g.drawLine(1500, 135, 0, 135);
			g.drawLine(1500, 275, 0, 275);
			g.drawLine(1500, 415, 0, 415);
			g.drawLine(1500, 555, 0, 555);

			//Drawing all the cards held by the players when it is their turn and when it is not
			if(activePlayer == 0) {
				g2D.setColor(Color.WHITE);
				g.drawString("You", 30, 25);
				for(int i = 0; i < game.getPlayerList().get(0).getNumOfCards(); i++) {
					if (selected[i] == false) {
						g.drawImage(cards[game.getPlayerList().get(0).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(0).getCardsInHand().getCard(i).getRank()], 155 + (40 * i), 25, bigTwoPanel);
					}
					else {
						g.drawImage(cards[game.getPlayerList().get(0).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(0).getCardsInHand().getCard(i).getRank()], 155 + (40 * i), 15, bigTwoPanel);
					}
		    	}
				
			}
			
			else {
				g2D.setColor(Color.BLACK);
				g.drawString(game.getPlayerList().get(0).getName(), 30, 25);
				
				for(int i = 0; i < game.getPlayerList().get(0).getNumOfCards(); i++) {
					g.drawImage(back, 150 + (40 * i), 25, bigTwoPanel);
				}
			}
			
			if(activePlayer == 1) {
				g2D.setColor(Color.WHITE);
				g.drawString("You", 30, 165);
				
				for(int i = 0; i < game.getPlayerList().get(1).getNumOfCards(); i++) {
		    		if (selected[i] == false) {
		    			g.drawImage(cards[game.getPlayerList().get(1).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(1).getCardsInHand().getCard(i).getRank()], 155 + (40 * i), 165, bigTwoPanel);
		    		}
		    		else {
		    			g.drawImage(cards[game.getPlayerList().get(1).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(1).getCardsInHand().getCard(i).getRank()], 155 + (40 * i), 155, bigTwoPanel);
		    		}
		    	}
			}
			
			else {
				g2D.setColor(Color.BLACK);
				g.drawString(game.getPlayerList().get(1).getName(), 30, 165);
				
				for(int i = 0; i < game.getPlayerList().get(1).getNumOfCards(); i++) {
					g.drawImage(back, 150 + (40 * i), 165, bigTwoPanel);
				}
			}
			
			if(activePlayer == 2) {
				g2D.setColor(Color.WHITE);
				g.drawString("You", 30, 305);
				for(int i = 0; i < game.getPlayerList().get(2).getNumOfCards(); i++) {
		    		if (selected[i] == false) {
		    			g.drawImage(cards[game.getPlayerList().get(2).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(2).getCardsInHand().getCard(i).getRank()], 155 + (40 * i), 305, bigTwoPanel);
		    		}
		    		else {
		    			g.drawImage(cards[game.getPlayerList().get(2).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(2).getCardsInHand().getCard(i).getRank()], 155 + (40 * i), 295, bigTwoPanel);
		    		}
		    	}
			}
			
			else {
				g2D.setColor(Color.BLACK);
				g.drawString(game.getPlayerList().get(2).getName(), 30, 305);
				for(int i = 0; i < game.getPlayerList().get(2).getNumOfCards(); i++) {
					g.drawImage(back, 150 + (40 * i), 305, bigTwoPanel);
				}
			}
			
			if(activePlayer == 3) {
				g2D.setColor(Color.WHITE);
				g.drawString("You", 30, 445);
				
				for(int i = 0; i < game.getPlayerList().get(3).getNumOfCards(); i++) {
					if (selected[i] == false) {
						g.drawImage(cards[game.getPlayerList().get(3).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(3).getCardsInHand().getCard(i).getRank()], 155 + (40 * i), 445, bigTwoPanel);	
					}
					else {
						g.drawImage(cards[game.getPlayerList().get(3).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(3).getCardsInHand().getCard(i).getRank()], 155 + (40 * i), 435, bigTwoPanel);	
					}
		    	}
			}
			
			else {
				g2D.setColor(Color.BLACK);
				g.drawString(game.getPlayerList().get(3).getName(), 30, 445);
				
				for(int i = 0; i < game.getPlayerList().get(3).getNumOfCards(); i++) {
					g.drawImage(back, 150 + (40 * i), 445, bigTwoPanel);
				}
			}
			
			// Setting up the table
			g.setColor(Color.black);
			g.drawString("Table", 30, 575);
			if (game.getHandsOnTable().size() != 0) {
				
				Hand lasthand = game.getHandsOnTable().get(game.getHandsOnTable().size()-1);
				g.drawString("Played by:" + game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getPlayer().getName(), 30, 590 );
				
				for(int i = 0; i < lasthand.size(); i++) {
					g.drawImage(cards[lasthand.getCard(i).getSuit()][lasthand.getCard(i).getRank()], 30 + (40 * i), 610, bigTwoPanel);
				}
			}
			
			this.repaint();
			
		}
		
		/**
		 * Dummy method, nothing to be overridden
		 * 
		 * @param e the action of the mouse being clicked within the frame
		 */
		
		public void mouseClicked(MouseEvent e) {
			
		}
		
		/**
		 * Dummy method, nothing to be overridden
		 * 
		 * @param e the action of the mouse being clicked within the frame
		 */
		
		public void mousePressed(MouseEvent e) {

		}
		
		/**
		 * Indicating when the mouse is released upon being clicked
		 * 
		 * @param e the action of the mouse being clicked within the frame
		 */
		
		public void mouseReleased(MouseEvent e) {
			
			// A flag to indicate whether a card has been selected, true if it is and have the entire graphics be painted afterwards
			boolean flag = false;
					
			//Observation: Only the last card is seen visible with the entire image
			//So we do 2 checkings, first is from the first card onto the second to the last card, second is only the last card
			
			// The case for the last card:
			int lastcardidx = game.getPlayerList().get(game.getBigTwoGUI().activePlayer).getNumOfCards() - 1;
			if (e.getX() >= 155 + 40 * lastcardidx && e.getX() <= 255+ 40 * lastcardidx) {
				if (selected[lastcardidx] == false && e.getY() >= 25 + 140 * game.getBigTwoGUI().activePlayer && e.getY() <= 125+ 140 * game.getBigTwoGUI().activePlayer) {
					selected[lastcardidx] = true;
					flag = true;
				}
				else if (selected[lastcardidx] == true && e.getY() >= 15 + 140 * game.getBigTwoGUI().activePlayer && e.getY() <= 115+ 140 * game.getBigTwoGUI().activePlayer) {
					selected[lastcardidx] =false;
					flag = true;
				}
			}
			
			//Second Case: first card - second to last card
			for (int i = 0; i < game.getPlayerList().get(game.getBigTwoGUI().activePlayer).getNumOfCards(); i++) {
				if (flag == false) {
					//First case: Check the card that is 4/10 visible:
					if(e.getX() >= 155 + i * 40 && e.getX() <= 195 + i * 40) {
						//if pressed card isn't selected already
						if(selected[i] == false && e.getY() >= 25 + game.getBigTwoGUI().activePlayer * 140 && e.getY() <= 125 + game.getBigTwoGUI().activePlayer *140) {
							selected[i] = true;
							flag = true;
						}
						// if it is
						else if(selected[i] == true && e.getY() >= 15 + game.getBigTwoGUI().activePlayer * 140 && e.getY() <= 115 + game.getBigTwoGUI().activePlayer *140) {
							selected[i] = false;
							flag = true;
						}
					}
					
					//Second case: The overlapping part of the cards
					//The lower part of the card that's overlapped
					else if(e.getX() >= 195 + i * 40 && e.getX() <= 255 + i* 40 && e.getY() >= 115 + game.getBigTwoGUI().activePlayer * 140 && e.getY() <= 125 + game.getBigTwoGUI().activePlayer *140) {
						if (selected[i] == false && selected[i+1] == true) {
							selected[i] = true;
							flag = true;
						}
					}
					//The upper part of the card that's overlapped
					else if(e.getX() >= 195 + i * 40 && e.getX() <= 255 + i* 40 && e.getY() >= 15 + game.getBigTwoGUI().activePlayer * 140 && e.getY() <= 25 + game.getBigTwoGUI().activePlayer *140) {
						if (selected[i + 1] == false && selected[i] == true) {
							selected[i] = false;
							flag = true;
						}
					}
					
				}
			}
			//Repaint the panel
			repaint();
		}
		
		/**
		 * Dummy method, nothing to be overridden
		 * 
		 * @param e the action of the mouse being clicked within the frame
		 */
		
		public void mouseEntered(MouseEvent e) {
			
		}
		
		/**
		 * Dummy method, nothing to be overridden
		 * 
		 * @param e the action of the mouse being clicked within the frame
		 */
		
		public void mouseExited(MouseEvent e) {
			
		}
		
	}
	
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface
	 * to handle button-click events for the “Play” button. When the “Play” button is clicked,
	 * you should call the makeMove() method of your BigTwo object to make a move.
	 * 
	 * @author Bryan Melvison
	 */
	
	public class PlayButtonListener implements ActionListener{
		
		/**
		 * Implements the actionPerformed() method from the ActionListener interface
		 * to handle button-click events for the “Play” button. When the “Play” button is clicked,
		 * you should call the makeMove() method of your BigTwo object to make a move.
		 * 
		 * @param e the action performed (clickable buttons)
		 */

		public void actionPerformed(ActionEvent e) {
			if(getSelected() == null) {
				printMsg("No card selected");
				promptActivePlayer();
			}
			
			else {
				game.makeMove(activePlayer, getSelected());
			}
			
		}
	}
	
	/**
	 * an inner class that implements the ActionListener
	 * interface. Implements the actionPerformed() method from the ActionListener interface
	 * to handle button-click events for the “Pass” button. When the “Pass” button is clicked,
	 * you should call the makeMove() method of your BigTwo object to make a move.
	 * 
	 * @author Bryan Melvison
	 */
	
	public class PassButtonListener implements ActionListener{
		
		/**
		 * handle button-click events for the “Pass” button. When the “Pass” button is clicked,
		 * you should call the makeMove() method of your BigTwo object to make a move.
		 * 
		 * @param e the actionevent when the "Pass" button is clicked
		 */
		
		public void actionPerformed(ActionEvent e) {
			game.makeMove(activePlayer, null);
		}
		
	}
	
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface
	 * to handle menu-item-click events for the “Connect” menu item. When the “Connect”
	 * menu item is selected, you should connect to server.
	 * 
	 * @author Bryan Melvison
	 */
	
	public class ConnectMenuItemListener implements ActionListener{
		
		/**
		 * to handle menu-item-click events for the “Connect” menu item. When the “Connect”
		 * menu item is selected, you should connect to the server
		 * 
		 * @param e to handle menu-item-click events for the “Connect” menu item
		 */
		
		public void actionPerformed(ActionEvent e) {
			if (game.getBigTwoClient().checkConnection() != true) {
				game.getBigTwoClient().connect();
			}
			else {
				printMsg("Already Connected.");
			}
		}
		
	}
	
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface
	 * to handle menu-item-click events for the “Quit” menu item. When the “Quit” menu
	 * item is selected, you should terminate your application. (You may use System.exit()
	 * to terminate your application.)
	 * 
	 * @author Bryan Melvison
	 */
	
	public class QuitMenuItemListener implements ActionListener{
		
		/**
		 * Implements the actionPerformed() method from the ActionListener interface
		 * to handle menu-item-click events for the “Quit” menu item. When the “Quit” menu
		 * item is selected, you should terminate your application.
		 * 
		 * @param e handle menu-item-click events for the “Quit” menu item.
		 */
		
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
		
	}
	
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface
	 * to handle menu-item-click events for the “Dummy Menu” menu item. When the “Dummy Menu” menu
	 * item is selected, you should print out "ANYA FORGER LES GO" 100 times. (Just a joke)
	 * 
	 * @author Bryan Melvison
	 */
	
	public class DummyMenuItemListener implements ActionListener{
		
		/**
		 * Implements the actionPerformed() method from the ActionListener interface
		 * to handle menu-item-click events for the “Dummy Menu” menu item. When the “Dummy Menu” menu
		 * item is selected, you should print out "ANYA FORGER LES GO" 100 times. (Just a joke)
		 * 
		 * @param e handle menu-item-click events for the “Dummy Menu” menu item.
		 */
		
		public void actionPerformed(ActionEvent e) {
			int i = 0;
			while(i < 100) {
				chatArea.append("ANYA FORGER LES GO \n");
				i++;
			}
		}
		
	}
	
	/**
	 * an inner class that implements the KeyListener interface. 
	 * Implements the keyPressed() method from the KeyListener interface
	 * to handle key pressed events for the chat input item. When the “Enter” button
	 * is pressed, you should have the message appended to the chat area. 
	 * 
	 * @author Bryan Melvison
	 */
	
	public class ChatKeyListener implements KeyListener{
		
		/**
		 * Dummy method
		 * 
		 * @param e KeyEvent
		 */
		
		public void keyTyped(KeyEvent e) {
			
		}
		
		/**
		 * Implements the keyPressed() method from the KeyListener interface
		 * to handle key pressed events for the chat input item. When the “Enter” button
		 * is pressed, you should have the message appended to the chat area. 
		 * 
		 * @param e KeyEvent
		 */
		
		/**
		 *
		 */
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				game.getBigTwoClient().sendMessage(new CardGameMessage(CardGameMessage.MSG,-1, chatInput.getText()));
				chatInput.setText("");
			}
		}
		
		/**
		 * Dummy method
		 * 
		 * @param e KeyEvent
		 */
		
		public void keyReleased(KeyEvent e) {
		}
		
	}
	
	/**
	 * a method for printing the specified string to the message area of the GUI
	 * 
	 * @param msg the message to be printed out.
	 */
	
	public void printMsg(String msg) {
		msgArea.append(msg + "\n");
	}
	
	/**
	 * a method for printing the specified string to the chat area of the GUI
	 * 
	 * @param msg the message to be printed out.
	 */
	
	public void printChatArea(String msg) {
		chatArea.append(msg + "\n");
	}
	
}

