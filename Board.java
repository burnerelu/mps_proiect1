import java.awt.Color;
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Board extends JFrame {

	public static final int WIDTH = 400;
	public static final int HEIGHT = 400;
	
    public Board() {
    /* VARIABILE de test */

   	
   	 ArrayList<Pacman> jucatori = new ArrayList<Pacman>();
   	 						//id, culoare, directie, x, y, scor_initial)
   	 jucatori.add(new Pacman(0, Color.green, 1, 0, 0, 1, 8));
   	 jucatori.add(new Pacman(1, Color.cyan, 1, 10, 110, 0, 0));
   	 jucatori.add(new Pacman(2, Color.gray, 3, 300, 10, 0, 0));
   	 jucatori.add(new Pacman(3, Color.blue, 2, 150, 10, 0, 0));
   	 jucatori.add(new Pacman(4, Color.red, 4, 100, 100, 0, 0));
   	 jucatori.add(new Pacman(5, Color.yellow, 0, 150, 150, 0, 0));
   	 System.out.println(" il controlezi pe cel verde");
   	 
   	/* */
   	 add(new Game(jucatori, WIDTH, HEIGHT));
       
       setTitle("Pacman ACS");
       setSize(WIDTH, HEIGHT + 22);
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
