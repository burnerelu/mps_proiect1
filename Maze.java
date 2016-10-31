import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/*
 * Aceasta clasa defineste functionalitatea labirintului
 * 
 * in cazul asta harta este de 800x800 si zidurile au o grosime de 20 deci
 * labirintul va fi de 40x40 pentru a fi o matrice mica usoara de parcurs
 * 
 */
public class Maze {

	int zid[][]; // matrice 0 liber 1 zid

	private static final int MAZE_SIZE = Board.RESOLUTION / Board.DRAW_SIZE;
	

	public Maze() {
		zid = new int[Board.RESOLUTION][Board.RESOLUTION];

		this.generate(zid);
	}

	/*
	 * Alegem Random puncte pe harta si incerca asezarea de forme random facem
	 * mai multe iteratii si incercam sa umplem labirintul pastram loc pentru
	 * jucatori
	 */
	private void generate(int[][] zid2) {

		Random rand = new Random();
		int x, y, forma;
		
		// contur
		for (int i = 0; i < MAZE_SIZE; i++) {
			zid[i][0] = 1;
			zid[i][MAZE_SIZE - 1] = 1;
		}	
		
		for (int i = 0; i < MAZE_SIZE; i++) {
			zid[0][i] = 1;
			zid[MAZE_SIZE - 1][i] = 1;
		}

		System.out.println("TEST");
		for (int i = 0; i < 10 * MAZE_SIZE; i++) {
			
			x = rand.nextInt(MAZE_SIZE - 6) + 2;
			y = rand.nextInt(MAZE_SIZE - 6) + 2;
			forma = rand.nextInt(3);
			System.out.println(forma);

			switch (forma) {
			case 0: // zid orizontal ---
				
				tryputWall(x - 1, y, x + 1, y);
				break;
			case 1: // zid vertical |

				tryputWall(x, y - 1, x, y + 1);

				break;
			case 2: // patrat 3x3

				tryputWall(x - 1, y - 1, x + 1, y + 1);

				break;

			default:
				break;
			}

		
		}

	}

	/* verifica zona libera apoi 
	 * pune 1 in matricea definita de x_start y_start respectiva x_end y_end
	 */
	private void tryputWall(int x_start, int y_start, int x_end, int y_end) {

		for (int i = x_start - 1; i < x_end + 2; i++) {
			for (int j = y_start - 1; j < y_end + 2; j++) {
				if (zid[i][j] == 1) { // daca e zid nu putem pune return
					return;
				}
			}
		}

		for (int i = x_start; i < x_end + 1; i++) {
			for (int j = y_start; j < y_end + 1; j++) {
				zid[i][j] = 1;
			}
		}
	}

	

	// daca pozitia dorita a jucatorului interesecteaza atunci returneaza 1
	// altfel 0
	public int intersectie(Pacman pac, int reqx, int reqy) {

		//TODO aici se pierde precizie si nu se intersecteaza cum trebuie
		//TODO nu am putut sa ii dau de caaaaaaap
		int x = (pac.x + Board.DRAW_SIZE / 2 + Pacman.SPEED * reqx) / Board.DRAW_SIZE;
		int y = (pac.y + Board.DRAW_SIZE / 2 + Pacman.SPEED * reqy) / Board.DRAW_SIZE;
		
		System.out.println(x + " " + y);
		//daca unde vrei sa mergi e liber
		return zid[x][y] ;
			
	}
	
	/* 
	 * Returneaza o locatie de spawn 
	 */
	public Point respawnLocation(){
		
		
		Random rand = new Random();
		//incepem cautarea pentru un loc de spawn dintr=o zona random de mijloc
		int i = rand.nextInt(MAZE_SIZE - MAZE_SIZE / 4) + MAZE_SIZE / 8;
		int j = rand.nextInt(MAZE_SIZE - MAZE_SIZE / 4) + MAZE_SIZE / 8;
		
		for (; i < MAZE_SIZE; i++) {
			for (; j < MAZE_SIZE; j++) {
				if(zid[i][j] == 0){
					return new Point( i * Board.DRAW_SIZE, j * Board.DRAW_SIZE);
				}
			}
		}
		
		//in caz ca nu gasim un loc disponibil facem respawn in stanga sus
		return new Point(Board.DRAW_SIZE, Board.DRAW_SIZE);
	}

	// deseneaza labirintul
	public void draw(Graphics2D g2d) {

		g2d.setColor(Color.BLUE);
		g2d.setStroke(new BasicStroke(5));

		// deseneaza contur
		g2d.drawLine(0, 0, Board.WIDTH, 0);
		g2d.drawLine(0, 0, 0, Board.HEIGHT);
		g2d.drawLine(0, Board.HEIGHT, Board.WIDTH, Board.HEIGHT);
		g2d.drawLine(Board.WIDTH, 0, Board.WIDTH, Board.HEIGHT);

		for (int i = 0; i < MAZE_SIZE; i++) {
			for (int j = 0; j < MAZE_SIZE; j++) {

				if (zid[i][j] == 1) {
					g2d.fillRect(i * Board.DRAW_SIZE, j * Board.DRAW_SIZE, Board.DRAW_SIZE - 2, Board.DRAW_SIZE - 2);
				}
			}

		}

	}
}
