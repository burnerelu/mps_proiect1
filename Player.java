import javax.swing.JFrame;
import javax.swing.JLabel;

public class Player {

	String name = "";
	String color = "";
	String ready_state = "";
	int[] name_position;
	int[] ready_position;
	int[] leave_position;
	
	public Player() {
		this.name = "";
		this.color = "";
		this.ready_state = "";
	}
	
	public void set_position(int[] n_pos, int[] r_pos, int[] l_pos) {
		name_position = new int[4];
		ready_position = new int[4];
		leave_position = new int[4];
		name_position = n_pos.clone();
		ready_position = r_pos.clone();
		leave_position = l_pos.clone();
	}
	
	public void set_name(String p_name) {
		name = p_name;
	}
	
	public void set_color(String c_color) {
		color = c_color;
	}
	
	public void set_state(String ready_s) {
		ready_state = ready_s;
	}
	
	public int get_position(String color) {
		switch(color) {
		case "Red": return 0;
		case "Green": return 1;
		case "Blue": return 2;
		case "Yellow": return 3;
		case "Purple": return 4;
		default: return -1;
		}
	}
	
	public void add_to_frame(JFrame frame) {
		JLabel r_label = new JLabel();
		JLabel n_label = new JLabel();
		JLabel l_label = new JLabel();
		n_label.setBounds(name_position[0], name_position[1],
		          name_position[2], name_position[3]);
		r_label.setBounds(ready_position[0], ready_position[1],
		          ready_position[2], ready_position[3]);
		l_label.setBounds(leave_position[0], leave_position[1],
		          leave_position[2], leave_position[3]);
	
		frame.remove(n_label);
		
		n_label.setText(name);
		n_label.setBounds(name_position[0], name_position[1],
				          name_position[2], name_position[3]);
		frame.add(n_label);
			
		frame.remove(r_label);
		r_label.setText(ready_state);
		r_label.setBounds(ready_position[0], ready_position[1],
				          ready_position[2], ready_position[3]);
		frame.add(r_label);
		
	}
}
