package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class CharacterPersonView extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CharacterPersonView frame = new CharacterPersonView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CharacterPersonView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnWidok = new JMenu("Widok");
		menuBar.add(mnWidok);
		
		
		
	    JCheckBox chckbxPodgldKamery = new JCheckBox("Podgląd kamery");
	    
		
		
	
		
		
		
		mnWidok.add(chckbxPodgldKamery);
		
		JMenu mnBazaDanych = new JMenu("Baza danych");
		menuBar.add(mnBazaDanych);
		
		JMenu mnPomoc = new JMenu("Pomoc");
		menuBar.add(mnPomoc);
		
		JSeparator separator = new JSeparator();
		mnPomoc.add(separator);
		
		JMenuItem mntmOAutorach = new JMenuItem("O autorach");
		mntmOAutorach.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame rem = new JFrame("O Autorach");
				
				rem.setSize(200, 200);
				rem.setVisible(true);
			}
		});
		mnPomoc.add(mntmOAutorach);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}
