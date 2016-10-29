import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/*
 * Aceasta clasa defineste functionalitatea labirintului
 * 
 * in cazul asta harta este de 400x400 si zidurile au o grosime de 20 deci
 * labirintul va fi de 20x20 pentru a fi o matrice mica usoara de parcurs
 * 
 * TODO dar ar trebui sa-l il facem functional si daca Board.WIDTH se schimba 
 */
public class Maze {

	
	int zid[][]; //matrice 0 liber 1 zid
	
	private static final int MAZE_WIDTH = Board.WIDTH / 20;
	private static final int MAZE_HEIGHT = Board.HEIGHT / 20;
	
	public Maze(){
		zid = new int[MAZE_WIDTH][MAZE_HEIGHT];
		
		this.generate(zid);
	}
	
	// TODO ceva mai avansat aici + comentarii	cum ati facut
	private void generate(int[][] zid2) {
 
		
		zid[10][10] = 1;
		zid[10][11] = 1;
		zid[10][12] = 1;
		
		zid[11][10] = 1;
		zid[11][11] = 1;
		zid[11][12] = 1;
		
		zid[12][10] = 1;
		zid[12][11] = 1;
		zid[12][12] = 1;
				
				
	}

	//daca pozitia curenta a jucatorului interesecteaza atunci returneaza 1 altfel 0
	public int intersectie(Pacman pac){

		
		// TODO aveti grija la OUT OF BOUNDS ca pac.x este [0,400] si zid.x este [0,20]
		return 0;
		
	}

	//deseneaza labirintul
	public void draw(Graphics2D g2d){
		
		g2d.setColor(Color.BLUE);
		g2d.setStroke(new BasicStroke(5));
		
		//deseneaza contur
		g2d.drawLine(0, 0, Board.WIDTH, 0);
		g2d.drawLine(0, 0, 0, Board.HEIGHT);
		g2d.drawLine(0, Board.HEIGHT, Board.WIDTH, Board.HEIGHT);
		g2d.drawLine(Board.WIDTH, 0, Board.WIDTH, Board.HEIGHT);
		
		
		for (int i = 0; i < MAZE_WIDTH; i++) {
			for (int j = 0; j < MAZE_WIDTH; j++) {
				
				if(zid[i][j] == 1){
					g2d.fillRoundRect(i * MAZE_WIDTH, j * MAZE_HEIGHT, MAZE_WIDTH, MAZE_HEIGHT, 10, 10);
				}
			}
			
		}
		
		
	}
}
