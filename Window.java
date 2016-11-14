import java.awt.Color;
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;

/*
 * Orice modificare asupra codului trebuie comentata
 * Window porneste jocul
 */
public class Window extends JFrame {

	public Window(int w, int h, Game g) {

		add(g);
		setTitle("Pacman ACS");
		setSize(w, h);
		setLocationRelativeTo(null); // centrat
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

}
