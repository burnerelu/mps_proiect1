import java.awt.Color;
import java.awt.Graphics2D;

/* 
 * Orice modificare asupra codului trebuie comentata
 * Clasa ce reprezinta pacman
 * 
 * 
 */
public class Pacman {

	int id;	//identificatorul jucatorului
	Color culoare;	//culoare unica fiecarui jucator
	int x, y; // locatia curenta a jucatorului
	int addX, addY; // increment pentru locatie
	int reqX, reqY; // variabile pentru a cere o noua directie
	int stare; // bun 0 sau rau 1
	int scor;	//scorul fiecarui jucatoru

	private final int text_shift = 5;
	public static final int SPEED = 2;	//factor de viteza

	/*
	 * Constructor jucator
	 */
	public Pacman(int id, Color culoare, int x, int y, int stare, int scor) {
		super();

		this.id = id;
		this.culoare = culoare;
		this.x = x;
		this.y = y;
		this.stare = stare;
		this.scor = scor;
	}

	/*
	 * updateaza pozitia jucatorului tinand cont de intersectia cu labirintul
	 * se misca pe directia initiala daca nu intersecteaza labirintul, atfel ramane pe loc
	 */
	public void update(Maze maze) {

		//schimba directia doar daca poti, altfel addX addY vechi
		if(maze.intersectie(this.x, this.y, reqX, reqY ) == 0){
			addX = reqX;
			addY = reqY;
		}
		
		//adauga addX addY doar daca poti
		if(maze.intersectie(this.x, this.y, addX, addY ) == 0){
			x += SPEED * addX;
			y += SPEED * addY;
		}

	}

	/*
	 * Desenarea jucatorului
	 * TODO daca este devil trebuie desenat separat
	 */
	public void draw(Graphics2D g2d) {
		
		g2d.setColor(culoare);
		g2d.fillOval(x, y, Board.DRAW_SIZE, Board.DRAW_SIZE);
		g2d.setColor(Color.white);
		// centrare si afisare scor
		g2d.drawString(Integer.toString(scor), x + Board.DRAW_SIZE / 3, y + 3 * Board.DRAW_SIZE / 4);
	}

	public void req_directie(int reqX, int reqY) {
		this.reqX = reqX;
		this.reqY = reqY;
	}

}