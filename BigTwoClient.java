import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

/**
 * The BigTwoClient class implements the NetworkGame interface. It is used to model a Big
 * Two game client that is responsible for establishing a connection and communicating with
 * the Big Two game server. Below is a detailed description for the BigTwoClient class.
 * @author Bryan Melvison
 *
 */

public class BigTwoClient implements NetworkGame {

	private BigTwo game;
	
	private BigTwoGUI gui;
	
	private Socket sock;
	
	private ObjectOutputStream oos;
	
	private int playerID;
	
	private String playerName;
	
	private String serverIP;
	
	private int serverPort;
	
	//For connection of the socket
	private boolean flag;

	//For handling QUIT
	private boolean anotherflag;
	
	/**
	 * a constructor for creating a Big Two client. The first parameter 
	 * is a reference to a BigTwo object associated with this client
	 * and the second parameter is a reference to a BigTwoGUI object 
	 * associated the BigTwo object.
	 * @param game BigTwo object associated with the client
	 * @param gui Reference to a BigTwoGUI object
	 */
	
	public BigTwoClient(BigTwo game, BigTwoGUI gui) {
		this.game = game;
		this.gui = gui;
		
		//Asking for the name of the player
		String namePlayer = JOptionPane.showInputDialog(null, "Valid Name: ");
		
		while(namePlayer == null || namePlayer.isEmpty()) {
			namePlayer = JOptionPane.showInputDialog(null, "Valid Name Pls: ");
		}
			
		setPlayerName(namePlayer);
		
		//Hard-coding the server ip and port (localized)
		this.setServerIP("127.0.0.1");
		this.setServerPort(2396);
		this.connect();
	}
	
	/**
	 * a method getting the anotherflag variable
	 * @return a t/f value indicating whether a player has quit the game.
	 */
	
	public boolean getanotherflag() {
		return this.anotherflag;
	}
	
	/**
	 * A method for getting the conenction status of the socket
	 * @return a t/f value indicating the connection status
	 */
	
	public boolean checkConnection() {
		return flag;
	}
	
	/**
	 * a method for getting the playerID (i.e., index) of the local player.
	 * @return the playerID of the local player.
	 */
	
	@Override
	public int getPlayerID() {
		return this.playerID;
	}
	
	/**
	 *  method for setting the playerID (i.e., index) of
	 *  the local player. This method should be called from the parseMessage() method when a
	 *  message of the type PLAYER_LIST is received from the game server. 
	 *  @param the playerID of the local player.
	 */
	
	@Override
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
		
	}
	
	/**
	 * a method for getting the name of the local player.
	 * @return the name of the local player
	 */	
	
	@Override
	public String getPlayerName() {
		return this.playerName;
	}

	/**
	 * a method for setting the name of the local player. 
	 * @param the name of the local player.
	 */
	
	@Override
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * a method for getting the IP address of the game server.
	 * @return the IP address of the game server.
	 */
	
	@Override
	public String getServerIP() {
		return this.serverIP;
	}
	
	/**
	 * a method for setting the IP address of the game server.
	 * @param the IP address of the game server.
	 */
	
	@Override
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
		
	}
	
	/**
	 * a method for getting the TCP port of the game server.
	 * @return the TCP port of the game server.
	 */
	
	@Override
	public int getServerPort() {
		return this.serverPort;
	}
	
	/**
	 * a method for setting the TCP port of the game server.
	 * @param the TCP port of the game server.
	 */
	
	@Override
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
		
	}
	
	/**
	 * a method for making a socket connection with the game server.
	 * Upon successful connection, you should (i) create an ObjectOutputStream for sending
	 * messages to the game server; (ii) create a new thread for receiving messages from the
	 * game server.
	 */
	
	@Override
	public void connect() {
		try {

			this.sock = new Socket(this.getServerIP(),this.getServerPort());
			this.oos = new ObjectOutputStream(sock.getOutputStream());
			Thread thread = new Thread(new ServerHandler());
			thread.start();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}	
	}
	
	/**
	 * a method for parsing the messages received from the game server. 
	 * This method should be called from the thread responsible for receiving 
	 * messages from the game server. Based on the message type, different actions 
	 * will be carried out (please refer to the general behavior of the client
	 * described in the previous section).
	 * @param the messages received from the game server.
	 */
	
	@Override
	public void parseMessage(GameMessage message) {
		
		// Value is 0 (PLAYER_LIST)
		if (message.getType() == CardGameMessage.PLAYER_LIST ) {
			flag = true;
			setPlayerID(message.getPlayerID());
			String names [] = (String []) message.getData();
			for (int i = 0; i < names.length; i++) {
				game.getPlayerList().get(i).setName(names[i]);	
			}
			sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, this.getPlayerName()));
		}
		
		// Value is 1 (JOIN)
		else if(message.getType() == CardGameMessage.JOIN) {
			String name = (String) message.getData();
			game.getPlayerList().get(message.getPlayerID()).setName(name);	
			if (this.getPlayerID() == message.getPlayerID()) {
				sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			}
			this.gui.printMsg(game.getPlayerList().get(message.getPlayerID()).getName() + " joins the game");
			this.gui.repaint();
		}
		
		// Value is 2 (FULL)
		else if(message.getType() == CardGameMessage.FULL) {
			flag = false;
			this.gui.printMsg("Server is full, cannot join game.");
		}
		
		// Value is 3 (QUIT)
		else if(message.getType() == CardGameMessage.QUIT) {
			this.gui.printMsg(game.getPlayerList().get(message.getPlayerID()).getName() + " left the game.");
			this.gui.disable();
			game.getPlayerList().get(message.getPlayerID()).setName("");
			this.gui.repaint();
			sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
		}
		
		// Value is 4 (READY)
		else if(message.getType() == CardGameMessage.READY) {
			anotherflag = true;
			this.gui.printMsg(game.getPlayerList().get(message.getPlayerID()).getName() + " is ready.");
		}

		// Value is 5 (START)
		else if(message.getType() == CardGameMessage.START) {
			anotherflag = false;
			BigTwoDeck btd = (BigTwoDeck) message.getData();
			game.start(btd);
			this.gui.repaint();
		}	
		
		// Value is 6 (MOVE)
		else if(message.getType() == CardGameMessage.MOVE) {
			int [] moveidx = (int[]) message.getData();
			game.checkMove(message.getPlayerID(), moveidx);

		}		
		
		// Value is 7 (MSG)
		else if(message.getType() == CardGameMessage.MSG) {
			String msg = (String) message.getData();
			gui.printChatArea(msg);
		}
	}
	
	/**
	 * a method for sending the specified message to the game server. 
	 * This method should be called whenever the client wants to
	 * communicate with the game server or other clients. 
	 * @param message to be sent to the game server
	 */
	
	@Override
	public synchronized void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * an inner class that implements the Runnable interface. You should implement the run() method 
	 * from the Runnable interface and create a thread
	 * with an instance of this class as its job in the connect() method from the NetworkGame
	 * interface for receiving messages from the game server. Upon receiving a message, the
	 * parseMessage() method from the NetworkGame interface should be called to parse the
	 * messages accordingly.
	 * @author Bryan Melvison
	 */
	
	class ServerHandler implements Runnable{
		
		/**
		 * create a thread with an instance of this class as its job in the connect() method from the NetworkGame
		 * interface for receiving messages from the game server. Upon receiving a message, the
		 * parseMessage() method from the NetworkGame interface should be called to parse the
		 * messages accordingly.
		 */
		
		@Override
		public void run() {
			ObjectInputStream ois;
			CardGameMessage cgm;
			try {
				ois = new ObjectInputStream(sock.getInputStream());
				//Check for connectivity of the sock
				while(sock.isClosed() == false) {
					cgm = (CardGameMessage) ois.readObject();
					if (cgm != null) {
						parseMessage(cgm);
					}
					else {
						break;
					}
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
	}

}
