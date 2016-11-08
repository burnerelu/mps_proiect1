import java.awt.Color;
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;

/*
 * Orice modificare asupra codului trebuie comentata
 * 
 */
public class Board extends JFrame {

	public static final int DENSITY = 40;
	public static final int RESOLUTION = 800;
	public static final int DRAW_SIZE = RESOLUTION / DENSITY;
	public static final int DEVIL_TIME = 200;
	
    public Board() {
   
   	 add(new Game(RESOLUTION + 100, RESOLUTION));
       
       setTitle("Pacman ACS");
       setSize(RESOLUTION + 100, RESOLUTION + 22);
       setLocationRelativeTo(null);	//centrat
       setDefaultCloseOperation(EXIT_ON_CLOSE);
       setVisible(true);        
    }
    

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Board().setVisible(true);  
            }
        });
    }
}
