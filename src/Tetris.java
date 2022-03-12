
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Tetris extends JFrame {

	private static final long serialVersionUID = 1L;

	JLabel statusbar;
    Board board;
    Application myApp;
    StatusPane statuspane;
    public Tetris(Application app) {
    	myApp = app;
        setSize(600, 600);
        setTitle("Tetris");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initializeComponents();
        setVisible(true);
   }

   private void initializeComponents() {
		JMenuBar menubar = new JMenuBar();
		JMenu menu;
		JMenuItem menuitem;
		
		this.setJMenuBar(menubar);
		menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_G);
		menuitem = new JMenuItem("New Game");
		menuitem.setMnemonic(KeyEvent.VK_N);
		menuitem.addActionListener(new MenuHandler(this));
		menu.add(menuitem);
		menu.addSeparator();
		menuitem = new JMenuItem("Exit");
		menuitem.setMnemonic(KeyEvent.VK_X);
		menuitem.addActionListener(new MenuHandler(this));
		menu.add(menuitem);
		menubar.add(menu);
		
		statuspane = new StatusPane(myApp);
		statusbar = new JLabel("Choose New Game from the menu Game to start game.");
		board = new Board(myApp, this);
		this.add(statuspane, BorderLayout.EAST);
		this.add(board);
		this.add(statusbar, BorderLayout.SOUTH);
	}

   public JLabel getStatusBar() {
       return statusbar;
   }
   
   public class MenuHandler implements ActionListener
   {
	   Tetris ter;
	   public MenuHandler(Tetris ter)
	   {
		   this.ter = ter;
	   }
	   public void actionPerformed(ActionEvent e) {
		   String s = e.getActionCommand();
		   if(s == "New Game")
		   {
			   board.start();
		   }
		   else if(s == "Exit")
		   {
			   ter.dispose();
		   }
	   }
   }
	
}
