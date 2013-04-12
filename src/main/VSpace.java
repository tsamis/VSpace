package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import processing.core.PApplet;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

/**
 * This Class is just a holder for the frame and an entry point to the application.
 * 
 */
public class VSpace {

	private JFrame frame;

	/**
	 * Entry point of the application. 
	 */
	public static void main(String[] args) {
		/* First we set the look and feel */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		/* We then run the application. */
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VSpace window = new VSpace();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public VSpace() {
		initialize();
		
	}

	/**
	 * Initialize a BZEclipse Instance and run the processing sketch.
	 */
	private void initialize() {
		final PApplet bz = new BZEclipse();
		
		
		PApplet.runSketch(new String[] {"main.BZEclipse" },bz);
		//toolBar.add(btnNewButton);
		//frame.getContentPane().setLayout(groupLayout);
	}
}
