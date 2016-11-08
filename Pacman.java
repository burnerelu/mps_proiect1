import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

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
	int devil; // bun 0 sau rau 1
	int devil_count;//cat timp este devil
	int scor;	//scorul fiecarui jucatoru

	
	public static final int SPEED = 2;	//factor de viteza
	public static final int DEVIL_SPEED = 2;	//factor de viteza

	/*
	 * Constructor jucator
	 */
	public Pacman(int id, Color culoare, int x, int y, int devil, int scor) {
		super();

		this.id = id;
		this.culoare = culoare;
		this.x = x;
		this.y = y;
		this.devil = devil;
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
			if(devil == 0){
				x += SPEED * addX;
				y += SPEED * addY;
			}else{
				x += DEVIL_SPEED * addX;
				y += DEVIL_SPEED * addY;
			}
		}

	}

	/*
	 * returneaza distanta acestui pacman catre ceilalti
	 */
	public double dist_to(Pacman pac){
		
		if(pac.id == this.id){
			return -1;
		}
		
		double deltax = Math.abs(this.x - pac.x);
		double deltay = Math.abs(this.y - pac.y);
		
		return Math.sqrt(deltax * deltax + deltay * deltay);
		
	}
	
	/*
	 * Desenarea jucatorului
	 * TODO daca este devil trebuie desenat separat
	 */
	public void draw(Graphics2D g2d) {
		
		g2d.setColor(culoare);
		
		if(devil == 0){	
			g2d.fillOval(x, y, Board.DRAW_SIZE, Board.DRAW_SIZE);		
		}
		
		else{
			
			if(addX == 0 && addY == 0){
				g2d.fillArc(x,  y, Board.DRAW_SIZE, Board.DRAW_SIZE, 30, 300);//dreapta
			}
			
			if(addX == 1 && addY == 0){
				g2d.fillArc(x,  y, Board.DRAW_SIZE, Board.DRAW_SIZE, 30, 300);//dreapta
			}
			
			if(addX == -1 && addY == 0){
				g2d.fillArc(x,  y, Board.DRAW_SIZE, Board.DRAW_SIZE, 210, 300);//stanga
			}
			
			if(addX == 0 && addY == -1){
				g2d.fillArc(x,  y, Board.DRAW_SIZE, Board.DRAW_SIZE, 120, 300);//jos
			}
			
			if(addX == 0 && addY == 1){
				g2d.fillArc(x,  y, Board.DRAW_SIZE, Board.DRAW_SIZE, 300, 300);//sus
			}
		}
		
		g2d.setColor(Color.white);
		// centrare si afisare scor
		g2d.drawString(Integer.toString(scor), x + Board.DRAW_SIZE / 3, y + 3 * Board.DRAW_SIZE / 4);
	}

	public void req_directie(int reqX, int reqY) {
		this.reqX = reqX;
		this.reqY = reqY;
	}

	public void set_postion(Point respawnLocation) {
		this.x = respawnLocation.x;
		this.y = respawnLocation.y;
		this.addX = 0;
		this.addY = 0;
		this.reqX = 0;
		this.reqY = 0;
		
	}

}