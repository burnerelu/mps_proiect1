import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.net.ConnectException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.awt.Color;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;


public class Client {
	static int Connected = 0;
    static String name = new String();
    static int attemp_conn = 0;
    static int leave_game = 0;
	static int state_ready = 0;
    public static void main(String args[]) throws Exception {
    	/*
    	   	*Connection window
    	*/
   
    	JFrame frame = new JFrame("Connect");
    	frame.setLocationRelativeTo(null);
    	frame.setSize(300,120);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
    	JPanel panel = new JPanel();
		frame.add(panel);
		JLabel userLabel = new JLabel("User");
		userLabel.setBounds(20, 20, 60, 40);
		panel.add(userLabel);		
		JTextField userText = new JTextField(15);
		userText.setBounds(100, 10, 160, 25);		
		panel.add(userText);
		JButton connectButton = new JButton("Connect");
		connectButton.setBounds(50, 80, 80, 25);
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
		      {
				/*
				 * TODO Check if user text is empty and display a warning
				 * TODO Name should only be alpha-numeric
				*/
				
				attemp_conn = 1;
				name = userText.getText();
				frame.setVisible(false);
				frame.dispose();
		      }

		});

		panel.add(connectButton);      
		//frame.setVisible(true);
		
		

		
		
		
		
        int rc;
        Socket sock = new Socket();
        while(Connected == 0) {
        	System.out.println(name);
        	if (attemp_conn == 1) {
        		try {
        			Connected = 1;
        			sock = new Socket("localhost", 24999);
        		}
        		catch (ConnectException e) {
        			Connected = 0;
        			System.out.println("Connection failed. Trying again in 5 seconds..");
        			Thread.sleep(5000);
        		}
        	}
        }
        /* Connected now */
        
        try {
        	
        	PrintStream pstream = new PrintStream(sock.getOutputStream());
            BufferedReader br =  new BufferedReader(new InputStreamReader(sock.getInputStream()));
            pstream.println(name);
            
            /*
             * TODO: Parse messages pregame from server
             * Server: nume:culoare:ready_status;nume:culoare:ready_status;
             * When all players are ready: start game, sleep5 (to decide each player's position and state)
             */
    		/*
        	 * Lobby window
        	 * TODO: Adapt each field to the middle of it's column
        	 * TODO: Decide what the leave status should say for the other players
        	 * TODO: Make it pretty?
        	 */
    		
    		/*
    		 * Fields
    		 */
        	JFrame lobby_frame = new JFrame();
        	lobby_frame.setLayout(null);
        	lobby_frame.setLocationRelativeTo(null);
        	lobby_frame.setSize(450,350);
        	lobby_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	JLabel nameLabel = new JLabel("Name");
    		nameLabel.setBounds(50, 10, 70, 10);
    		lobby_frame.add(nameLabel);		
    		JLabel colorLabel = new JLabel("Color");
    		colorLabel.setBounds(150, 10, 70, 10);
    		lobby_frame.add(colorLabel);		
    		JLabel readyLabel = new JLabel("Ready");
    		readyLabel.setBounds(250, 10, 70, 10);
    		lobby_frame.add(readyLabel);		
    		JLabel leaveLabel = new JLabel("Leave");
    		leaveLabel.setBounds(350, 10, 70, 10);
    		lobby_frame.add(leaveLabel);
    		
    		/*
    		 * Players
    		 */
    		List<String> playerNames = new ArrayList<String>();
    		/*
    		 * 	playerNames.add("Bot1");
    			playerNames.add("Bot2");
    			playerNames.add("Bot3");
    			playerNames.add("Bot4");
    		*/
    		JLabel player1Label = new JLabel(name);
    		player1Label.setBounds(50, 60, 70, 10);
    		lobby_frame.add(player1Label);
    		JLabel player2Label = new JLabel(playerNames.get(0));
    		player2Label.setBounds(50, 110, 70, 10);
    		lobby_frame.add(player2Label);
    		JLabel player3Label = new JLabel(playerNames.get(1));
    		player3Label.setBounds(50, 160, 70, 10);
    		lobby_frame.add(player3Label);
    		JLabel player4Label = new JLabel(playerNames.get(2));
    		player4Label.setBounds(50, 210, 70, 10);
    		lobby_frame.add(player4Label);
    		JLabel player5Label = new JLabel(playerNames.get(3));
    		player5Label.setBounds(50, 260, 70, 10);
    		lobby_frame.add(player5Label);
    		
    		List<String> colors = new ArrayList<String>();
    		/*
    			colors.add("Red");
    			colors.add("Blue");
    			colors.add("Green");
    			colors.add("Purple");
    			colors.add("Yellow");
    		*/
    		JLabel color1Label = new JLabel(colors.get(0));
    		JLabel color2Label = new JLabel(colors.get(1));
    		JLabel color3Label = new JLabel(colors.get(2));
    		JLabel color4Label = new JLabel(colors.get(3));
    		JLabel color5Label = new JLabel(colors.get(4));
    		color1Label.setBounds(150, 60, 70, 10);
    		color2Label.setBounds(150, 110, 70, 10);
    		color3Label.setBounds(150, 160, 70, 10);
    		color4Label.setBounds(150, 210, 70, 10);
    		color5Label.setBounds(150, 260, 70, 10);
    		lobby_frame.add(color1Label);
    		lobby_frame.add(color2Label);
    		lobby_frame.add(color3Label);
    		lobby_frame.add(color4Label);
    		lobby_frame.add(color5Label);
    		
    		/* Ready boxes
    		 * 
    		 */
    		
        	JCheckBox check = new JCheckBox("Ready");
    		check.setBounds(250, 60, 70, 10);
    		check.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				state_ready = 1;
    				/*
    				 * TODO Send ready state to server
    				 */
    				pstream.println(state_ready);
    			}
    		      
    		});
    		lobby_frame.add(check);
    		
    		List<String> playersReady = new ArrayList<String>();
    		/*
    			playersReady.add("Yes");
    			playersReady.add("Yes");
    			playersReady.add("Yes");
    			playersReady.add("Yes");
    		*/
    		JLabel player1ReadyLabel = new JLabel(playersReady.get(0));
    		JLabel player2ReadyLabel = new JLabel(playersReady.get(1));
    		JLabel player3ReadyLabel = new JLabel(playersReady.get(2));
    		JLabel player4ReadyLabel = new JLabel(playersReady.get(3));
    		player1ReadyLabel.setBounds(250, 110, 70, 10);
    		player2ReadyLabel.setBounds(250, 160, 70, 10);
    		player3ReadyLabel.setBounds(250, 210, 70, 10);
    		player4ReadyLabel.setBounds(250, 260, 70, 10);
    		lobby_frame.add(player1ReadyLabel);
    		lobby_frame.add(player2ReadyLabel);
    		lobby_frame.add(player3ReadyLabel);
    		lobby_frame.add(player4ReadyLabel);
    		
    		
    		JButton leaveButton = new JButton("Leave");
    		leaveButton.setBounds(350, 55, 70, 20);
    		leaveButton.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				leave_game = 1;
    			}
    		});
    		lobby_frame.add(leaveButton);
    		
    		lobby_frame.setVisible(true);
    		
    		
    		
            
    		
            //pstream.println("Howdy, server!");

            char[] inputChars = new char[1024];

            //System.out.println("Trying to read");
            //br.read(inputChars);
            //System.out.println(inputChars);
            
            // Some things to keep the loop up
            while(true && leave_game == 0) {
            	/*
            	 * TODO: send color, position, state, score
            	 * TODO: recv color, position, state
            	 */
                rc = br.read(inputChars);
                if(rc < 0) {
                    System.out.println("Socket disconnected");
                    break;
                }
                
            }

            sock.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }
}
