/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class OptionPane extends JDialog implements ChangeListener, ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	Application myApp;
	private JSlider speed;
	private JButton button;
	private JLabel label;
	private JRadioButton radio;
	private ButtonGroup group = new ButtonGroup();
	private JPanel panel;
	@SuppressWarnings("unused")
	private int preSpeed = 3;
	private int difficulty = 1;
	public OptionPane(Application app)
	{
		myApp = app;
		initializeComponents();
		setModal(true);
		setBounds(450, 200,400,200);
	}
	
	private void initializeComponents() {
		// Label shows current speed and Slider to config the game speed
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Game speed"));
		label = new JLabel("Current speed: 3");
		panel.add(label, BorderLayout.NORTH);
		speed = new JSlider(1, 5, 3);
		speed.addChangeListener(this);
		speed.setPreferredSize(new Dimension(250, 50));
		panel.add(speed, BorderLayout.CENTER);
		this.add(panel, BorderLayout.NORTH);
		panel.setVisible(false);
		
		// Radio Button to set the difficulty in game
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Difficulty"));
		radio = new JRadioButton("Normal");
		radio.addActionListener(this);
		radio.setSelected(true);
		group.add(radio);
		panel.add(radio);
		radio = new JRadioButton("Hard");
		radio.addActionListener(this);
		group.add(radio);
		panel.add(radio);
		radio = new JRadioButton("Extreme");
		radio.addActionListener(this);
		group.add(radio);
		panel.add(radio);
		this.add(panel, BorderLayout.CENTER);
		
		// Button OK and Cancel
		panel = new JPanel();
		button = new JButton("OK");
		button.setPreferredSize(new Dimension(80, 30));
		button.addActionListener(this);
		panel.add(button, BorderLayout.WEST);
		button = new JButton("Cancel");
		button.setPreferredSize(new Dimension(80, 30));
		button.addActionListener(this);
		panel.add(button, BorderLayout.EAST);
		this.add(panel, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		String com = e.getActionCommand();
		if(com == "OK")
		{
			myApp.tetris.board.setSpeed(speed.getValue());
			switch(difficulty)
			{
				case 1:
					myApp.tetris.board.setSpeed(3);
					break;
				case 2:
					myApp.tetris.board.setSpeed(4);
					break;
				case 3:
					myApp.tetris.board.setSpeed(5);
					break;
			}
			preSpeed = speed.getValue();
			myApp.tetris.board.grabFocus();
			this.setVisible(false);
		}
		else if(com == "Normal")
		{
			difficulty = 1;
		}
		else if(com == "Hard")
		{
			difficulty = 2;
		}
		else if(com == "Extreme")
		{
			difficulty = 3;
		}
		else if(com == "Cancel")
		{
			myApp.tetris.board.grabFocus();
			this.setVisible(false);
		}
	}


	public void stateChanged(ChangeEvent e) {
		if(e.getSource().getClass().toString().endsWith("JSlider"))
		{
			label.setText("Current speed: " + speed.getValue());
		}
	}
}

