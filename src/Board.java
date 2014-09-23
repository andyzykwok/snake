import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel implements ActionListener {
    
	private static final long serialVersionUID = 1L;
	
	boolean playing, bugfix;
	boolean left, right, up, down = false;

	int width = 300, height = 300;
	int pixelSize = 10;
	int totalpixels = 900;
	int maxpixelNum = 30;
	int x[] = new int[totalpixels];
	int y[] = new int[totalpixels];

	int randomNum;
	int bodyNum;
	int xFood, yFood;
	int count = 0;
	int delay = 70;
	int xloseHead;

	Timer timer;

	ImageIcon headpic = new ImageIcon(this.getClass().getResource("head.gif"));
	ImageIcon foodpic = new ImageIcon(this.getClass().getResource("food.png"));
	ImageIcon bodypic = new ImageIcon(this.getClass().getResource("dot.png"));
	Image food, head, body;

	Font f = new Font("Helvetica", Font.BOLD, 14);
	Font o = new Font("Helvetica", Font.PLAIN, 10);
	Font n = new Font("Helvetica", Font.BOLD, 70);
	Font t = new Font("Helvetica", Font.PLAIN, 12);
	String scoretext, introtext1, introtext2, introtext3, titletext, nametext,
			instructiontext, ggtext1, ggtext2;

	public Board() {
		addKeyListener(new KAdapter()); //adds input

		setBackground(Color.black);

		head = headpic.getImage();
		body = bodypic.getImage();
		food = foodpic.getImage();

		// delaying the movement
		timer = new Timer(delay, this);
		timer.start();

		setFocusable(true); 
	}

	public void paint(Graphics g) {
		super.paint(g); //runs paint

		if ((left == false) && (right == false) && (up == false)
				&& (down == false)) 
			intro(g);
		
		// drawing the image
		if (playing) {
			for (int i = 1; i < bodyNum; i++)
				g.drawImage(body, x[i], y[i], this); // drawing the body
			
			g.drawImage(head, x[0], y[0], this); // drawing the head
			
			bugfix = true;
			
			g.drawImage(food, xFood, yFood, this); // drawing the food

			g.setColor(Color.white);
			g.drawLine(0, height + 10, 999, height + 10);

			g.setFont(o);
			g.drawString("Score: " + count * 10, 240, 325);

			g.drawString("SPACE - Restart         Q - Quit", 20, 325);
		}

		if (!playing) {
			gameover(g);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (playing) {
			crashTest();
			checkFood();
			tail();
			direction();
		}
		repaint(); // refreshes the image
	}

	// title
	public void intro(Graphics g) {
		//start values
		bodyNum = 3;
		x[0] = 160;
		y[0] = 160;

		// place food
		xFood = 70;
		yFood = 250;
		playing = true;
		count = 0;

		g.setColor(Color.white);
		FontMetrics centre = this.getFontMetrics(n);
		g.setFont(n);
		titletext = "snake";
		g.drawString(titletext, (316 - centre.stringWidth(titletext)) / 2, 150);

		g.setFont(t);
		instructiontext = "Instructions";
		g.drawString(instructiontext, 5, 260);

		introtext1 = "Feed the snake without colliding with the walls or itself.";
		introtext2 = "Use the arrow keys to control the snake.";
		introtext3 = "Press any arrow key to begin.";

		g.setFont(o);
		g.drawString(introtext1, 5, 275);
		g.drawString(introtext2, 5, 289);
		g.drawString(introtext3, 5, 303);

		nametext = "by: andy kwok";
		g.setFont(t);
		g.drawString(nametext, 173, 169);
	}

	public void checkFood() {

		if ((x[0] == xFood) && (y[0] == yFood)) {
			bodyNum = bodyNum + 2;
			positionFood(); // set new food location
			count++;
		}
	}

	public void crashTest() {
		// collision with window
		if ( (x[0] < 0) || (x[0] > width) || (y[0] < 0) || (y[0] > height) )
			playing = false;

		// collision with itself
		for (int i = bodyNum; i > 0; i--) {
			if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i]))
				playing = false;
		}

	}

	public void positionFood() {
		randomNum = (int) (Math.random() * maxpixelNum);
		xFood = ((randomNum * pixelSize));
		randomNum = (int) (Math.random() * maxpixelNum);
		yFood = ((randomNum * pixelSize));
	}

	public void tail() {
		// previous bodyNum (the tail)
		for (int i = bodyNum; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
	}

	public void direction() {
		if (left) {
			x[0] = x[0] - pixelSize;
			bugfix = true;
		}

		if (right) {
			x[0] = x[0] + pixelSize;
			bugfix = true;
		}

		if (up) {
			y[0] = y[0] - pixelSize;
			bugfix = true;
		}

		if (down) {
			y[0] = y[0] + pixelSize;
			bugfix = true;
		}
	}

	public class KAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();

			if ((key == KeyEvent.VK_LEFT) && (!right) && (bugfix == true)) {
				left = true;
				up = false;
				down = false;
				bugfix = false;
			}

			if ((key == KeyEvent.VK_RIGHT) && (!left)  && (bugfix == true)) {
				right = true;
				up = false;
				down = false;
				bugfix = false;
			}

			if ((key == KeyEvent.VK_UP) && (!down) && (bugfix == true)) {
				up = true;
				right = false;
				left = false;
				bugfix = false;
			}

			if ((key == KeyEvent.VK_DOWN) && (!up) && (bugfix == true)) {
				down = true;
				right = false;
				left = false;
				bugfix = false;
			}

			if (key == KeyEvent.VK_SPACE) {
				left = false;
				right = false;
				up = false;
				down = false;
			}

			if ((key == KeyEvent.VK_Q) || (key == KeyEvent.VK_ESCAPE)) {
				System.exit(0);
			}
		}
	}

	// what happens when the player loses
	public void gameover(Graphics g) {
		FontMetrics centre = this.getFontMetrics(f);
		g.setColor(Color.white);
		g.setFont(f);
		scoretext = "Your score is " + count * 10 + ".";
		ggtext1 = "Press SPACE to play again!";
		ggtext2 = "Press Q or ESC to quit.";
		g.drawString("Game Over!",
				(316 - centre.stringWidth("Game Over!")) / 2, 110);
		g.drawString(scoretext, (316 - centre.stringWidth(scoretext)) / 2, 130);
		g.drawString(ggtext1, (316 - centre.stringWidth(ggtext1)) / 2, 210);
		g.drawString(ggtext2, (316 - centre.stringWidth(ggtext2)) / 2, 230);

		xloseHead = 123;
		for (int i = 0; i < 5; i++) {
			g.drawImage(head, xloseHead, 160, this);
			xloseHead = xloseHead + 15;
		}
	}
}
