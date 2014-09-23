import javax.swing.JFrame;

public class Snake extends JFrame{

	private static final long serialVersionUID = 1L;
	
	Board b = new Board ();
	
    public Snake() {
        add(b);
        setTitle("Snake");
        setResizable(false);
        setSize(316, 365);
        setLocationRelativeTo(null); //pops up in middle            
        setVisible(true);
    }

    public static void main(String[] args) {
        new Snake(); //run
    }
}