import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/*
 * Orice modificare asupra codului trebuie comentata
 * Aceasta clasa defineste functionalitatea labirintului
 * 
 * maze-ul are rolulurile :
 * 
 *  a genera random labirintul
 * 	a returna intersectia intre un pacman si labirint
 *  de a returna o pozitie de spawn disponibila tinand cont de locatia lui devil
 *  se deseneaza 
 *  
 *  TODO un bonus simplu ar fi sa facem labirintul sa se miste,
 *  ar trebui alterata doar matricea zid
 *  
 */
public class Maze {

	int zid[][]; // matrice 0 liber 1 zid

	private static final int MAZE_SIZE = Board.RESOLUTION / Board.DRAW_SIZE;
	private static final int MARJA_DE_EROARE = 1;	//marja de eroare pentru coliziuni

	/*
	 * constructor Labirint
	 */
	public Maze() {
		
		zid = new int[Board.RESOLUTION][Board.RESOLUTION];
		this.generate(zid);
	}

	/*
	 * Alegem Random puncte pe harta si incerca asezarea de forme random facem
	 * mai multe iteratii si incercam sa umplem labirintul, pastram loc intre forma
	 * ca jucatorii sa aiba loc sa treaca
	 */
	private void generate(int[][] zid2) {

		Random rand = new Random();
		int x, y, forma;
		
		// generarea contur
		for (int i = 0; i < MAZE_SIZE; i++) {
			zid[i][0] = 1;
			zid[i][MAZE_SIZE - 1] = 1;
			zid[0][i] = 1;
			zid[MAZE_SIZE - 1][i] = 1;
		}	
		
		//generare forme
		for (int i = 0; i < 10 * MAZE_SIZE; i++) {
			
			x = rand.nextInt(MAZE_SIZE - 6) + 2;
			y = rand.nextInt(MAZE_SIZE - 6) + 2;
			forma = rand.nextInt(3);

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

	/* 
	 * Verifica zona libera apoi 
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

	/* 
	 * Daca pozitia dorita a jucatorului interesecteaza atunci returneaza 1 altfel 0
	 * intersectia jucatorului cu labirintul se face considerand ca jucatorul are forma patrata
	 */
	public int intersectie(int x, int y, int reqx, int reqy) {

		int xstanga = (x + Board.DRAW_SIZE - MARJA_DE_EROARE + Pacman.SPEED * reqx) / Board.DRAW_SIZE;
		int xdreapta = (x + MARJA_DE_EROARE + Pacman.SPEED * reqx) / Board.DRAW_SIZE;
				
		int ysus = (y + Board.DRAW_SIZE - MARJA_DE_EROARE + Pacman.SPEED * reqy) / Board.DRAW_SIZE;
		int yjos = (y + MARJA_DE_EROARE + Pacman.SPEED * reqy) / Board.DRAW_SIZE;
		
		int xcentru = (x + Board.DRAW_SIZE / 2 ) / Board.DRAW_SIZE;
		int ycentru = (y + Board.DRAW_SIZE / 2 ) / Board.DRAW_SIZE;
		
		//daca e zid in vreun colt trebuie sa returnam 1 intersectie
		if (zid[xstanga][yjos] == 1 || zid[xstanga][ysus] == 1 || zid[xdreapta][yjos] == 1 
				|| zid[xdreapta][ysus] == 1 || zid[xcentru][ycentru] == 1 ){
			return 1;
		}else{
			return 0;
		}
		
	}
	
	/* 
	 * Returneaza o locatie de spawn 
	 */
	public Point respawnLocation(){
		
		
		Random rand = new Random();
		//incepem cautarea pentru un loc de spawn dintr=o zona random de mijloc
		//TODO aici i si j sa inceapa de la 0 si sa nu generam peste alt jucator
		int i = rand.nextInt(MAZE_SIZE / 2) + MAZE_SIZE / 8;
		int j = rand.nextInt(MAZE_SIZE / 2) + MAZE_SIZE / 8;
		
		for (; i < MAZE_SIZE; i++) {
			for (; j < MAZE_SIZE; j++) {
				System.out.println(i + " " + j);
				if(intersectie(i * Board.DRAW_SIZE, j * Board.DRAW_SIZE, 0, 0) == 0){	//daca nu intersecteaza
					System.out.println(i * Board.DRAW_SIZE + " " + j * Board.DRAW_SIZE);
					return new Point( i * Board.DRAW_SIZE, j * Board.DRAW_SIZE);
				}
			}
		}
		
		System.out.println("NU AM GASIT LOC DE SPAWN");
		//in caz ca nu gasim un loc disponibil facem respawn in stanga sus
		return new Point(Board.DRAW_SIZE, Board.DRAW_SIZE);
	}

	/* 
	 * Deseneaza labirintul
	 */
	public void draw(Graphics2D g2d) {

		
		g2d.setColor(new Color(10,10,200));
		g2d.setStroke(new BasicStroke(5));

		// deseneaza contur
		g2d.drawLine(0, 0, Board.WIDTH, 0);
		g2d.drawLine(0, 0, 0, Board.HEIGHT);
		g2d.drawLine(0, Board.HEIGHT, Board.WIDTH, Board.HEIGHT);
		g2d.drawLine(Board.WIDTH, 0, Board.WIDTH, Board.HEIGHT);

		for (int i = 0; i < MAZE_SIZE; i++) {
			for (int j = 0; j < MAZE_SIZE; j++) {

				if (zid[i][j] == 1) {
					g2d.fillRoundRect(i * Board.DRAW_SIZE, j * Board.DRAW_SIZE, Board.DRAW_SIZE, Board.DRAW_SIZE,10,10);
				}
			}

		}

	}
}
