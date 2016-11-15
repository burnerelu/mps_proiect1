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
	
	int resolution;
	int draw_size;
	int maze_size;
	int marja_de_eroare;
	ArrayList<Wall> walls = new ArrayList<Wall>();

	/*
	 * constructor Labirint
	 */
	public Maze(int r, int ds) {
		
		zid = new int[r][r];
		
		this.draw_size = ds;
		this.resolution = r;
		this.maze_size = resolution / draw_size;
		this.marja_de_eroare = 1;
		
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
		for (int i = 0; i < maze_size; i++) {
			zid[i][0] = 1;
			zid[i][maze_size - 1] = 1;
			zid[0][i] = 1;
			zid[maze_size - 1][i] = 1;
		}	
		
		//generare forme
		for (int i = 0; i < 10 * maze_size; i++) {
			
			x = rand.nextInt(maze_size - 6) + 2;
			y = rand.nextInt(maze_size - 6) + 2;
			forma = rand.nextInt(3);

			switch (forma) {
			case 0: // zid orizontal ---

				if(tryputWall(x - 1, y, x + 1, y)) {

					Wall wall = new Wall(x, y, 0);
					walls.add(wall);
				}
				break;
			case 1: // zid vertical |

				if(tryputWall(x, y - 1, x, y + 1)) {
					Wall wall = new Wall(x, y, 1);
					walls.add(wall);
				}

				break;
			case 2: // patrat 3x3

				if(tryputWall(x - 1, y - 1, x + 1, y + 1)) {
					Wall wall = new Wall(x, y, 2);
					walls.add(wall);
				}

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
	public boolean tryputWall(int x_start, int y_start, int x_end, int y_end) {

		for (int i = x_start - 1; i < x_end + 2; i++) {
			for (int j = y_start - 1; j < y_end + 2; j++) {
				if (zid[i][j] == 1) { // daca e zid nu putem pune return
					return false;
				}
			}
		}

		for (int i = x_start; i < x_end + 1; i++) {
			for (int j = y_start; j < y_end + 1; j++) {
				zid[i][j] = 1;
			}
		}
		return true;
	}

	/* 
	 * Daca pozitia dorita a jucatorului interesecteaza atunci returneaza 1 altfel 0
	 * intersectia jucatorului cu labirintul se face considerand ca jucatorul are forma patrata
	 */
	public int intersectie(int x, int y, int reqx, int reqy) {

		int xstanga = (x + draw_size - marja_de_eroare + Pacman.SPEED * reqx) / draw_size;
		int xdreapta = (x + marja_de_eroare + Pacman.SPEED * reqx) / draw_size;
				
		int ysus = (y + draw_size - marja_de_eroare + Pacman.SPEED * reqy) / draw_size;
		int yjos = (y + marja_de_eroare + Pacman.SPEED * reqy) / draw_size;
		
		int xcentru = (x + draw_size / 2 ) / draw_size;
		int ycentru = (y + draw_size / 2 ) / draw_size;
		
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
		int i = rand.nextInt(maze_size / 2) + maze_size / 8;
		int j = rand.nextInt(maze_size / 2) + maze_size / 8;
		
		for (; i < maze_size; i++) {
			for (; j < maze_size; j++) {
				
				if(intersectie(i * draw_size, j * draw_size, 0, 0) == 0){	//daca nu intersecteaza
					
					return new Point( i * draw_size, j * draw_size);
				}
			}
		}
		
		System.out.println("NU AM GASIT LOC DE SPAWN");
		//in caz ca nu gasim un loc disponibil facem respawn in stanga sus
		return new Point(draw_size, draw_size);
	}

	/* 
	 * Deseneaza labirintul
	 */
	public void draw(Graphics2D g2d) {

		
		g2d.setColor(new Color(10,10,200));
		g2d.setStroke(new BasicStroke(5));

		// deseneaza contur
		g2d.drawLine(0, 0, Window.WIDTH, 0);
		g2d.drawLine(0, 0, 0, Window.HEIGHT);
		g2d.drawLine(0, Window.HEIGHT, Window.WIDTH, Window.HEIGHT);
		g2d.drawLine(Window.WIDTH, 0, Window.WIDTH, Window.HEIGHT);

		for (int i = 0; i < maze_size; i++) {
			for (int j = 0; j < maze_size; j++) {

				if (zid[i][j] == 1) {
					g2d.fillRoundRect(i * draw_size, j * draw_size, draw_size, draw_size,10,10);
				}
			}

		}

	}
}
