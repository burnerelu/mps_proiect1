
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * Orice modificare asupra codului trebuie comentata
 * Clasa Game se va ocupa 
 */
public class Game extends JPanel implements ActionListener {

	int width, height;
	ArrayList<Pacman> jucatori;
	Maze labirint;
	int stare; // 0 cauta server 1 lobby 2 joc

	public final int RIGHT = 1;
	public final int UP = 2;
	public final int LEFT = 3;
	public final int DOWN = 4;
	private static final int GAME_SPEED = 30; //cu cat mai mic cu atat mai repede
	private Timer timer;

	public Game() {
		super();
	}

	/*
	 * Constructor joc
	 * Initilizeaza 
	 * 
	 * TODO initializarea trebuie facuta in functie de datele primite in urma ecranului 
	 * LOBBY
	 */
	public Game(int w, int h) {
		super();
 
		addKeyListener(new Controller());		// adaugam un obiect ce se ocupa cu controlul jocului (asculta tastatura)
		setFocusable(true);
		setDoubleBuffered(true);

		// timer pentru repaint
		this.timer = new Timer(GAME_SPEED, this);
		this.timer.start();

		this.jucatori = jucatori;

		this.labirint = new Maze();
		initPlayers();
		this.width = w;
		this.height = h;

	}

	/*
	 * Metoda ce initializeaza jucatorii
	 * TODO trebuie legata cu informatiile primite de la ecranul de LOBBY
	 */
	private void initPlayers() {
		
		jucatori = new ArrayList<Pacman>();
		// (int id, Color culoare, int x, int y, int stare, int scor)
		
		
		//jucator verde
		Point p1 = labirint.respawnLocation();
		jucatori.add(new Pacman(0, Color.green, p1.x, p1.y, 1, 8));
		
		//jucator rosu
		Point p2 = labirint.respawnLocation();
		jucatori.add(new Pacman(1, Color.cyan, p2.x, p2.y, 1, 0));
		
		Point p3 = labirint.respawnLocation();
		jucatori.add(new Pacman(2, Color.gray, p3.x, p3.y, 0, 0));
		
		Point p4 = labirint.respawnLocation();
		jucatori.add(new Pacman(3, Color.blue, p4.x, p4.y, 0, 0));
		
		Point p5 = labirint.respawnLocation();
		jucatori.add(new Pacman(4, Color.red, p5.x, p5.y, 0, 0));
		
		Point p6 = labirint.respawnLocation();
		jucatori.add(new Pacman(5, Color.yellow, p6.x, p6.y, 0, 0));

	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * Se ocupa de desenarea elementelor pe tabla de joc
	 * 
	 * TODO trebuie sa desenam si ecranul de lobby
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		// deseneaza fundal
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, this.width, this.height);

		// deseneaza labirint
		labirint.draw(g2d);

		// TODO labirint.setNextDevil(); unde facem asta ?
		
		// deseneaza jucatori
		for (Pacman pacman : jucatori) {
			pacman.update(labirint);
			pacman.draw(g2d);
		}

		g2d.dispose(); // elibereaza resurse
	}

	class Controller extends KeyAdapter {


				
					 
		/*
		 * 			 (non-Javadoc)
		 * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
		 * 
		 * Controlul jocului
		 */
		@Override
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();

			switch (key) {
			case KeyEvent.VK_RIGHT:

				jucatori.get(0).req_directie(1, 0);
				break;

			case KeyEvent.VK_UP:
				jucatori.get(0).req_directie(0, -1);
				break;

			case KeyEvent.VK_LEFT:
				jucatori.get(0).req_directie(-1, 0);
				break;

			case KeyEvent.VK_DOWN:
				jucatori.get(0).req_directie(0, 1);
				break;
				
			// W A S D
			case KeyEvent.VK_D:
				jucatori.get(1).req_directie(1, 0);
				break;
			case KeyEvent.VK_W:
				jucatori.get(1).req_directie(0, -1);
				break;
			case KeyEvent.VK_A:
				jucatori.get(1).req_directie(-1, 0);
				break;
			case KeyEvent.VK_S:
				jucatori.get(1).req_directie(0, 1);
				break;
				

			default:
				break;
			}
		}

		// TODO daca e nevoie putem folosi si asta
		@Override
		public void keyReleased(KeyEvent e) {

			int key = e.getKeyCode();
			if (key == Event.LEFT || key == Event.RIGHT || key == Event.UP || key == Event.DOWN) {

			}
			
			
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		repaint();

	}

}
