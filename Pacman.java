import java.awt.Color;
import java.awt.Graphics2D;

/* 
 * Clasa ce reprezinta pacman
 * 
 * 
 */
public class Pacman {

	int id;
	Color culoare;
	int directie;// sens trigonometric : 1 dreapta, 2 sus, 3 stanga, 4 jos
	int x, y; // locatie
	int stare; // bun sau rau
	int scor;
	private final int SIZE = 20;
	private final int text_shift = 5;
	final int SPEED = 2;

	// constructor
	public Pacman(int id, Color culoare, int directie, int x, int y, int stare, int scor) {
		super();

		this.id = id;
		this.culoare = culoare;
		this.directie = directie;
		this.x = x;
		this.y = y;
		this.stare = stare;
		this.scor = scor;
	}

	// se misca pe directie daca nu intersecteaza labirintul atfel ramane pe loc
	public void update(Maze lab) {

		// TODO de verificat intersectia cu labirintul
		if (lab.intersectie(this) == 0) {

			switch (directie) {
			case 1: // dreapta
				x += SPEED;
				break;
			case 2: // sus
				y -= SPEED;
				break;
			case 3: // stanga
				x -= SPEED;
				break;
			case 4: // jos
				y += SPEED;
				break;

			default:
				break;
			}
		}

	}

	// afisare jucatori
	public void draw(Graphics2D g2d) {
		g2d.setColor(culoare);
		g2d.fillOval(x, y, SIZE, SIZE);
		g2d.setColor(Color.white);
		// centrare si afisare scor
		g2d.drawString(Integer.toString(scor), x + SIZE / 3, y + 3 * SIZE / 4);
	}

	public void set_directie(int dir) {
		this.directie = dir;
	}

}