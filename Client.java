import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.*;
import java.net.ConnectException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;



public class Client {
	public static Pacman get_pacman(Player p, int x, int y) {
		/*
		int id;	//identificatorul jucatorului
		String nume;	//identificatorul jucatorului
		Color culoare;	//culoare unica fiecarui jucator
		int x, y; // locatia curenta a jucatorului
		int devil; // bun 0 sau rau 1
		int scor;	//scorul fiecarui jucatoru
		int draw_size;
		int devil_speed; 
		 */
		 
		 /* Fixed all grey players
		 	Added break to each case
		 	Thanks Andreea
		 */
		Color c;
		int id;
		String pac_name = p.name;
		switch (p.color) {
		case "Red": c = Color.RED;
					id = 1;
					break;
		case "Green": c = Color.GREEN;
					id = 2;
					break;
		case "Blue": c = Color.BLUE;
					id = 3;
					break;
		case "Purple": c = Color.PINK;
					id = 4;
					break;
		case "Yellow": c = Color.YELLOW;
					id = 5;
					break;
		default: c = Color.GRAY;
					id = -1;
		}
		
		Pacman pac = new Pacman(id, pac_name, c, x, y, 0, 0, 20, 1);
		
		return pac;
	}
	public static void sendMessage(PrintStream ps, String message, int code) {
	        //System.out.println("Sending " + message);
	        ps.println("" + code + "<" + message + ">\n");
	}
	 
	public static boolean all_ready(Player p1, Player[] players) { 
		int ready_count = 0;
		if (p1.ready_state == "Ready") 
			ready_count++;
		for(int i = 0; i < players.length; i++) {
			if (players[i].ready_state == "Ready") 
				ready_count++;
		}
		if (ready_count == 1)
			return true;
		else
			return false;
	}
	
	public static String encode_map(int[][] map) {
		String separator = "";
	    StringBuffer result = new StringBuffer();

	    for (int i = 0; i < map.length; i++) {
	        for(int j = 0; j < map[i].length; j++){
	            result.append(map[i][j]);
	            result.append(separator);
	        }
	        result.setLength(result.length() - separator.length());
	        result.append("\n");
	    }
	    return result.toString();
	}
	
	public static int[][] decode_map(String map) {
		String[] lines = map.split("\n");
		int[][] new_map = new int[800][800];
		for (int i = 0; i < lines.length; i++) {
			System.arraycopy(lines[i], 0, new_map[i], 0, 800);
		}
		return new_map;
	}
	
	static int Connected = 0;
    static String name = new String();
    static int attemp_conn = 0;
    static int leave_game = 0;
	static int state_ready = 0;
	static int game_started = 0;
    public static void main(String args[]) throws Exception {
    	/*
    	   	*Connection window
    	*/
    	Maze maze = new Maze(800, 20);
    	ArrayList<Pacman> pacs = new ArrayList<Pacman>();
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
				name = userText.getText().replaceAll("\n", "");
				frame.setVisible(false);
				frame.dispose();
		      }

		});

		panel.add(connectButton);      
		frame.setVisible(true);
		
		
        int rc;
        Socket sock = new Socket();
        while(Connected == 0) {
        	//System.err.println(attemp_conn);
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
        	System.out.println("connected");
        	PrintStream pstream = new PrintStream(sock.getOutputStream());
            BufferedReader br =  new BufferedReader(new InputStreamReader(sock.getInputStream()));
            sendMessage(pstream, name, 200);
            char[] inputChars = new char[1024];
            String message;
           
    		/*
        	 * Lobby window
        	 * TODO: Adapt each field to the middle of it's column
        	 * TODO: Decide what the leave status should say for the other players
        	 * TODO: Make it pretty?
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
    		JLabel redc = new JLabel("Red");
    		redc.setBounds(150, 60, 70, 10);
    		lobby_frame.add(redc);
    		JLabel greenc = new JLabel("Green");
    		greenc.setBounds(150, 110, 70, 10);
    		lobby_frame.add(greenc);
    		JLabel bluec = new JLabel("Blue");
    		bluec.setBounds(150, 160, 70, 10);
    		lobby_frame.add(bluec);
    		JLabel yellowc = new JLabel("Yellow");
    		yellowc.setBounds(150, 210, 70, 10);
    		lobby_frame.add(yellowc);
    		JLabel purplec = new JLabel("Purple");
    		purplec.setBounds(150, 260, 70, 10);
    		lobby_frame.add(purplec);
		
    		lobby_frame.setVisible(true);
    		
    		int[][] n = new int[][]{{50,60,70,10},{50, 110, 70, 10},{50, 160, 70, 10},{50, 210, 70, 10},{50, 260, 70, 10}};
    		int[][] r = new int[][]{{250,60,70,10},{250, 110, 70, 10},{250, 160, 70, 10},{250, 210, 70, 10},{250, 260, 70, 10}};
    		int[][] l = new int[][]{{350,60,70,10},{350, 110, 70, 10},{350, 160, 70, 10},{350, 210, 70, 10},{350, 260, 70, 10}};
    		int red = 0;
    		int gr = 1;
    		int bl = 2;
    		int yl = 3;
    		int pp = 4;
    		
    		Player me = new Player();
    		me.set_name(name);
    		
    		ArrayList<Player> players = new ArrayList<Player>();
    		

    		while(game_started == 0 && leave_game == 0) {
    			lobby_frame.revalidate();
    			lobby_frame.repaint();
    			
    			message = br.readLine();
    			
    			String[] code_message = message.split("<");
    			int code = Integer.parseInt(code_message[0]);
       			if (code == 103) {
       				
       				message = code_message[1].split(">")[0];
       			
       			
       				String[] players_info = message.split(";");
       				String[] my_info = players_info[0].split(":");
       				if (!me.name.equals(my_info[0])) {
       					me.set_name(my_info[0]);
       				}
       				if (!me.color.equals(my_info[1])) {
       					me.set_color(my_info[1]);
       				}
       				String ready = "";
       				if (Integer.parseInt(my_info[2]) == 1) {
       					ready = "Ready"; 
       				}
       				else {
       					ready = "Not ready";
       				}
       				int my_position = me.get_position(me.color);
       				if (!ready.equals("Ready")) {
       					JCheckBox check = new JCheckBox("Ready");
       					check.setBounds(r[my_position][0], r[my_position][1], r[my_position][2], r[my_position][3]);
       					check.addActionListener(new ActionListener() {
       						public void actionPerformed(ActionEvent e) {
       							state_ready = 1;
       							sendMessage(pstream, "ready", 201); 
       						}     		      
       					});
            		
       					lobby_frame.add(check);
       				}
       				JButton leaveButton = new JButton("Leave");
       				leaveButton.setBounds(l[my_position][0], l[my_position][1], l[my_position][2], l[my_position][3]);
       				leaveButton.addActionListener(new ActionListener() {
       					public void actionPerformed(ActionEvent e) {
       						leave_game = 1;
       						lobby_frame.setVisible(false);
       						lobby_frame.dispose();
       					}
       				});
       				lobby_frame.add(leaveButton);
       				JLabel name_label = new JLabel(me.name);
       				name_label.setBounds(n[my_position][0], n[my_position][1], n[my_position][2], n[my_position][3]);
       				lobby_frame.remove(name_label);
       				lobby_frame.add(name_label);
    			
       				for (int i = 1; i < players_info.length; i++) {
       					int player_exists = 0;
       					String[] info = players_info[i].split(":");
       					Player player = new Player();
       					player.set_name(info[0]);
       					player.set_color(info[1]);
       					int position = player.get_position(player.color);
       					player.set_position(n[position], r[position], l[position]);
       					if (Integer.parseInt(info[2]) == 1) {
       						ready = "Ready";
       					}
       					else {
       						ready = "Not ready";
       					}
       					player.set_state(ready);
       					int j;
       					players.add(player);
       					player_exists = 0;
       					for(j = 0; j < players.size() - 1; j++) {
       						if (players.get(j).name.equals(player.name)) {	
       							players.get(j).set_state(player.ready_state);
       							player.add_to_frame(lobby_frame);
       							player_exists = 1;
       							break;
       						}    						
       					}
    					if (player_exists == 1) {
    						players.remove(players.get(players.size() - 1));
    					}
    					else {
    						player.add_to_frame(lobby_frame);
    					}
       				}
       			}
       			else if( code == 110 || code == 120) {
       				
    				if(code == 110) {
    					if (me.color.equals("Blue")) {
    						
    						String map_to_send = "";
    						for (int i = 0; i < maze.walls.size(); i++) {
    							map_to_send += String.valueOf(maze.walls.get(i).x) + " ";
    							map_to_send += String.valueOf(maze.walls.get(i).y) + " ";
    							map_to_send += String.valueOf(maze.walls.get(i).forma) + " ";
    							map_to_send += ";";
    						}
    						
    						
    						sendMessage(pstream, map_to_send, 202);
    					}
    					else {
    						
    						for (int i = 0; i < 800; i++) {
    							for(int j = 0; j < 800; j++) {
    								maze.zid[i][j] = 0;
    							}
    						}
    						for (int i = 0; i < 40; i++) {
								maze.zid[i][0] = 1;
								maze.zid[i][40 - 1] = 1;
								maze.zid[0][i] = 1;
								maze.zid[40 - 1][i] = 1;
							}	
							
							String map_to_use = br.readLine();
							map_to_use = map_to_use.split("<")[1].split(">")[0];
							String[] walls = map_to_use.split(";");
							for (int i = 0; i < walls.length; i++) {
								String[] things = walls[i].split(" ");
								int x = Integer.parseInt(things[0]);
								int y = Integer.parseInt(things[1]);
								int forma = Integer.parseInt(things[2]);
								if (x < 2 || y < 2)
									continue;
								
								boolean aux;
								switch (forma) {
								case 0: aux = maze.tryputWall(x - 1, y, x + 1, y);;
										break;
								case 1: aux = maze.tryputWall(x, y - 1, x, y + 1);
										break;
								case 2: aux = maze.tryputWall(x - 1, y - 1, x + 1, y + 1);
										break;
								}
							}
						}
					}
					sendMessage(pstream, "ready", 201); 
					TimeUnit.SECONDS.sleep(5);
    				lobby_frame.setVisible(false);
    				lobby_frame.dispose();
    				
    				

    				Point point = maze.respawnLocation();
    				pacs.add(get_pacman(me, point.x, point.y));
    				for (int i = 0; i < players.size(); i++) {
    					point = maze.respawnLocation();
    					pacs.add(get_pacman(players.get(i), point.x, point.y));
    				}
    				Game my_g = new Game(800, maze, pacs, 1000);
    				Window my_w = new Window(900, 850, my_g);
    				EventQueue.invokeLater(new Runnable() {

    					@Override
    					public void run() {
    						my_w.setVisible(true);
    					}
    				});
    				break;
    			}
    		}
    		
    		
            
            // Some things to keep the loop up
            while(true) {
            	
            	String message_to_send = "" + me.color + ":" + pacs.get(0).x + ":" + pacs.get(0).y + ":" +
            							pacs.get(0).devil + ":" + pacs.get(0).scor;
            	sendMessage(pstream, message_to_send, 401);
            	
                message = br.readLine();
                
                //System.out.println(message);
                String[] code_message = message.split("<");
                int code = Integer.parseInt(code_message[0]);
                message = code_message[1].split(">")[0];
                if (code == 301) {
                	try {
                		String[] player_infos = message.split(";");
                		String[] my_info = player_infos[0].split(":");
                			//pacs.get(0).x = Integer.parseInt(my_info[1]);
                			//pacs.get(0).y = Integer.parseInt(my_info[2]);
                			pacs.get(0).devil = Integer.parseInt(my_info[3]);
                			pacs.get(0).scor = Integer.parseInt(my_info[4]);
                			for (int i = 1; i < player_infos.length; i++) {
                				String[] players_i = player_infos[i].split(":");
                				for (int k = 1; k < pacs.size(); k++) {
                					Color player_color;
                					switch(players_i[0]) {
                					case "Red": player_color = Color.RED;
                								break;
                					case "Green": player_color = Color.GREEN;
                								break;
                					case "Blue": player_color = Color.BLUE;
                								break;
                					case "Purple": player_color = Color.PINK;
                								break;
                					case "Yellow": player_color = Color.YELLOW;
                								break;
                					default: player_color = Color.GRAY;
                								break;
                					}
                					if (player_color.equals(pacs.get(k).culoare)) {
                						pacs.get(k).x = Integer.parseInt(players_i[1]);
                						pacs.get(k).y = Integer.parseInt(players_i[2]);
                						pacs.get(k).devil = Integer.parseInt(players_i[3]);
                						pacs.get(k).scor = Integer.parseInt(players_i[4]);
                						
                					}
                				}
                			}
                	}
                		catch (IndexOutOfBoundsException e) {
                			continue;
                		}
                }
              	 if(pacs.get(0).scor == 10) {
              		 break;
              	 }
                
                
            }
           
            sock.close();
        } catch (IOException e) {
           // System.out.println(e);
        }

    }

}
