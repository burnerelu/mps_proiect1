
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
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.omg.CORBA.Current;

/*
 * Orice modificare asupra codului trebuie comentata
 * Clasa Game se va ocupa 
 */
public class Game extends JPanel implements ActionListener {

	int width, height;
	ArrayList<Pacman> jucatori;
	Maze maze;
	int stare; // 0 cauta server 1 lobby 2 joc

	public final int RIGHT = 1;
	public final int UP = 2;
	public final int LEFT = 3;
	public final int DOWN = 4;
	private static final int GAME_SPEED = 30; // cu cat mai mic cu atat mai
												// repede

	private Timer timer;

	int devil; // cine e devil actual

	public Game() {
		super();
	}

	/*
	 * Constructor joc Initilizeaza
	 * 
	 * TODO initializarea trebuie facuta in functie de datele primite in urma
	 * ecranului LOBBY
	 */
	public Game(int w, int h) {
		super();

		addKeyListener(new Controller()); // adaugam un obiect ce se ocupa cu
											// controlul jocului (asculta
											// tastatura)
		setFocusable(true);
		setDoubleBuffered(true);

		// timer pentru repaint
		this.timer = new Timer(GAME_SPEED, this);
		this.timer.start();

		this.devil = 1; // TODO = -1
		this.maze = new Maze();

		this.width = w;
		this.height = h;

		initPlayers();

	}

	/*
	 * Metoda ce initializeaza jucatorii TODO trebuie legata cu informatiile
	 * primite de la ecranul de LOBBY
	 */
	private void initPlayers() {

		jucatori = new ArrayList<Pacman>();
		// (int id, Color culoare, int x, int y, int stare, int scor)

		// jucator verde
		Point p1 = maze.respawnLocation();
		jucatori.add(new Pacman(0, "Alex", Color.green, p1.x, p1.y, 0, 0));

		// jucator rosu
		Point p2 = maze.respawnLocation();
		jucatori.add(new Pacman(1, "Andreea", Color.red, p2.x, p2.y, 0, 0));

		// jucator turcoaz
		Point p3 = maze.respawnLocation();
		jucatori.add(new Pacman(2, "Mihai",  Color.cyan, p3.x, p3.y, 0, 0));

	}

	
	/*
	 * Dupa un timp alegem un alt devil care va fi cel mai apropiat jucaotr daca
	 * nu a fost niciun devil ales alegem unul random
	 */
	public void choose_next_devil() {

		if (devil == -1) {
			devil = new Random().nextInt(jucatori.size());
			jucatori.get(devil).devil = 1;
			jucatori.get(devil).devil_count = Board.DEVIL_TIME;

		} else { // daca cineva este deja devil

			double dist;
			double min_dist = 9000;
			int closest = devil;
			Pacman devil_pac = jucatori.get(devil), curent;

			/*
			 * daca nu este inca timpul sa alegem un alt devil decrementam
			 * contorul lui si iesim din functie
			 */
			if (devil_pac.devil_count != 0) {
				devil_pac.devil_count--;
				return;

			}

			/*
			 * Daca contorul lui a ajuns la 0 atunci aflam care este cel mai
			 * apropiat jucator si il facem devil iar pe devil il facem bun
			 */
			for (int i = 0; i < jucatori.size(); i++) {
				curent = jucatori.get(i);
				dist = devil_pac.dist_to(curent);
				if (curent.id != devil_pac.id && dist < min_dist) {
					min_dist = dist;
					closest = i;
				}
			}

			jucatori.get(devil).devil = 0;

			devil = closest;

			jucatori.get(devil).devil = 1;
			jucatori.get(devil).devil_count = Board.DEVIL_TIME;

		}
	}

	private void score_update(ArrayList<Pacman> jucatori) {

		if (devil == -1) { // daca devil inca nu a fost ales
			return;
		}

		Pacman devil_pac = jucatori.get(devil);
		Pacman curent;

		// verificam dinstanta intre devil si ceilalti jucatori
		for (int i = 0; i < jucatori.size(); i++) {

			curent = jucatori.get(i);

			// daca distanta este suficient de mica
			if (curent.id != devil_pac.id && devil_pac.dist_to(curent) < Board.DRAW_SIZE) {

				// facem respan si micsoaram respectiv marim scorul
				curent.set_postion(maze.respawnLocation());
				if(curent.scor > 0){
					curent.scor--;
				}
				devil_pac.scor++;
			}
		}

	}

	class Controller extends KeyAdapter {

		/*
		 * (non-Javadoc)
		 * 
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

			// I J K L
			case KeyEvent.VK_L:
				jucatori.get(2).req_directie(1, 0);
				break;
			case KeyEvent.VK_I:
				jucatori.get(2).req_directie(0, -1);
				break;
			case KeyEvent.VK_J:
				jucatori.get(2).req_directie(-1, 0);
				break;
			case KeyEvent.VK_K:
				jucatori.get(2).req_directie(0, 1);
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics) Se ocupa de
	 * desenarea elementelor pe tabla de joc
	 * 
	 * TODO trebuie sa desenam si ecranul de lobby
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int i = 0;
		Graphics2D g2d = (Graphics2D) g;
		// deseneaza fundal
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, this.width, this.height);

		// deseneaza labirint
		maze.draw(g2d);

		// deseneaza jucatori + scoruri
		for (Pacman pacman : jucatori) {
			pacman.draw(g2d);
			i++;
			g2d.drawString(pacman.nume + " " + pacman.scor, Board.RESOLUTION + 10, i * Board.DRAW_SIZE);
		}

		g2d.dispose(); // elibereaza resurse
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		score_update(jucatori);
		choose_next_devil();
		for (Pacman pacman : jucatori) {
			pacman.update(maze);
		}

		repaint();

	}

}
