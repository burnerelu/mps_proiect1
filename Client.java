import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.util.ArrayList;

/* 
 * Orice modificare asupra codului trebuie comentata
 * Clientul are un id, labirintul, un vector cu pacmans pe care il updateaza permanent 
 * cu informatii de la server, 
 */
public class Client {
	
	
	/*
	 * TODO 6 asta va disparea pentru ca informatile legate de playerul
	 * local si despre ceilalti playeri vr veni de la server
	 */
	public static void initPlayers(ArrayList<Pacman> jucatori, Maze maze, int ds, int devil_s) {

		// (int id, Color culoare, int x, int y, int stare, int scor_initial, int draw_size)

		// jucator verde
		Point p0 = maze.respawnLocation();
		jucatori.add(new Pacman(0, "Alex", Color.orange, p0.x, p0.y, 0, 0, ds, devil_s));

		// jucator rosu
		Point p1 = maze.respawnLocation();
		jucatori.add(new Pacman(1, "Andreea", Color.red, p1.x, p1.y, 0, 0, ds, devil_s));

		// jucator turcoaz
		Point p2 = maze.respawnLocation();
		jucatori.add(new Pacman(2, "Paul",  Color.cyan, p2.x, p2.y, 0, 0, ds, devil_s));
		
		// jucator rosu
		Point p3 = maze.respawnLocation();
		jucatori.add(new Pacman(3, "Gabriela", Color.blue, p3.x, p3.y, 0, 0, ds, devil_s));

		// jucator turcoaz
		Point p4 = maze.respawnLocation();
		jucatori.add(new Pacman(4, "Vlad",  Color.yellow, p4.x, p4.y, 0, 0, ds, devil_s));

		// jucator rosu
		Point p5 = maze.respawnLocation();
		jucatori.add(new Pacman(5, "Daniel", Color.green, p5.x, p5.y, 0, 0, ds, devil_s));

		// jucator turcoaz
		Point p6 = maze.respawnLocation();
		jucatori.add(new Pacman(6, "Mihaela",  Color.pink, p6.x, p6.y, 0, 0, ds, devil_s));
		
		
	}
	
	public static void main(String[] args) {

		
		// conectare la server
		// TODO 2 Paul
		
		// primeste labirint, culoare si alte informatii
		// TODO 1 Paul
		int resolution = 800;
		int draw_size = 20;
		int devil_time = 1000;	//cat timp ramane devil
		int devil_speed = 1;
		
		
		//initializare joc
		Maze my_m = new Maze(resolution, draw_size);
		ArrayList<Pacman> pacs = new ArrayList<Pacman>();
		Client.initPlayers(pacs, my_m, draw_size, devil_speed);

		Game my_g = new Game(resolution, my_m, pacs, devil_time);
		Window my_w = new Window(resolution + 100, resolution + 50, my_g);
		
		
		// porneste jocul
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				my_w.setVisible(true);
			}
		});
	}

	

}
