
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel implements ActionListener {

	int width, height;
	ArrayList<Pacman> jucatori;
	Maze labirint;
	int stare; //0 cauta server 1 lobby 2 joc
	
	public final int RIGHT = 1;
	public final int UP = 2;
	public final int LEFT = 3;
	public final int DOWN = 4;
	
	private Timer timer;
	
	public Game() {
		super();
	}
	
	public Game(ArrayList<Pacman> jucatori, int w, int h) {
		super();

		//adaugam un obiect ce se ocupa cu controlul jocului (asculta tastatura)
		addKeyListener(new Controller());
		setFocusable(true);
		setDoubleBuffered(true);
		
		//timer pentru repaint
		this.timer = new Timer(40, this);
        this.timer.start();
        
        this.jucatori = jucatori;
		this.labirint = new Maze();
		this.width = w;
		this.height = h;
		
	}
	
	@Override
	public void addNotify() {
        super.addNotify();
    }

	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //TODO deseneaza : fereastra de search_server apoi lobby apoi joc 
        
        Graphics2D g2d = (Graphics2D) g;
        //deseneaza fundal
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, this.width, this.height);

        
        //deseneaza labirint
        labirint.draw(g2d);
        
       
        //deseneaza jucatori
        for (Pacman pacman : jucatori) {
        	pacman.update(labirint);
			pacman.draw(g2d);
		}
        
        g2d.dispose();	//elibereaza resurse
        
    }
	
	class Controller extends KeyAdapter{

		
		@Override
	    public void keyPressed(KeyEvent e) {
			
			
	        int key = e.getKeyCode();

	       //TODO cazuri de control pentru lobby 
	        //(facem asta dupa ce facem conectarea intre calculatoare)
	        
	        
	        /* TODO trebuie controlata schimbarea de directie in felul urmator :
	         * 
	         * exemplu : daca merge in dreapta -> si da de un perete => se opreste
	         * daca merge in dreapta si peretele e sub el si dam comanda sa o ia in jos 
	         * NU trebuie sa se opreasca ci sa retina comanda si sa o ia in jos cand poate
	         * 
	         * (sa nu ramana lipit de pereti cand nu trebuie)
	         */
	        
	        switch (key) {
			case KeyEvent.VK_RIGHT:
				
				jucatori.get(0).set_directie(RIGHT) ; 
				break;

			case KeyEvent.VK_UP:
				jucatori.get(0).set_directie(UP);
				break;

			case KeyEvent.VK_LEFT:
				jucatori.get(0).set_directie(LEFT);
				break;

			case KeyEvent.VK_DOWN:
				jucatori.get(0).set_directie(DOWN);
				break;
				
			default:
				break;
			}
	    }
		
		@Override
	     public void keyReleased(KeyEvent e) {

			
	         int key = e.getKeyCode();
	         if (key == Event.LEFT || key == Event.RIGHT
	                    || key == Event.UP || key == Event.DOWN) {
	        	 
	             //TODO daca e nevoie putem folosi si asta
	          }
	     }
	}	 
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		repaint();
		
	}
	

}
