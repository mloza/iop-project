package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.BoxLayout;

public class CharacterPersonView extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void wlacz() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CharacterPersonView frame = new CharacterPersonView();
					frame.setResizable(false);
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
		setTitle("IntelligentEye - Character Person View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 528, 409);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JPanel panel1 = new JPanel();
		sl_contentPane.putConstraint(SpringLayout.NORTH, panel1, 20, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, panel1, 36, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, panel1, 236, SpringLayout.WEST, contentPane);
		
		Dimension dd = new Dimension(200,300);
		panel1.setPreferredSize(dd);
		panel1.setLayout(new BorderLayout());
		Picture obraz = new Picture();
		obraz.setSize(dd);
		panel1.add(obraz, BorderLayout.CENTER);
		
		
		contentPane.add(panel1);
		
		JPanel panel2 = new JPanel();
		sl_contentPane.putConstraint(SpringLayout.NORTH, panel2, 20, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, panel2, 32, SpringLayout.EAST, panel1);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, panel2, 0, SpringLayout.SOUTH, panel1);
		sl_contentPane.putConstraint(SpringLayout.EAST, panel2, -48, SpringLayout.EAST, contentPane);
		panel2.setPreferredSize(dd);
		panel2.setLayout(new BorderLayout());
		Picture obraz2 = new Picture();
		obraz.setSize(dd);
		panel2.add(obraz2, BorderLayout.CENTER);
		
		contentPane.add(panel2);
		
		
		JPanel dane = new JPanel();
		sl_contentPane.putConstraint(SpringLayout.NORTH, dane, 301, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, panel1, -10, SpringLayout.NORTH, dane);
		sl_contentPane.putConstraint(SpringLayout.WEST, dane, 10, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, dane, -10, SpringLayout.SOUTH, contentPane);
		panel1.setLayout(new BorderLayout(0, 0));
		sl_contentPane.putConstraint(SpringLayout.EAST, dane, 508, SpringLayout.WEST, contentPane);
		contentPane.add(dane);
		dane.setLayout(new BoxLayout(dane, BoxLayout.Y_AXIS));
		
		JLabel lblImiINazwisko = new JLabel("Imię i nazwisko: Xxxx Yyyyyy");
		dane.add(lblImiINazwisko);
		
		JLabel lblWiek = new JLabel("Wiek: 36");
		dane.add(lblWiek);
		
		JLabel lblPochodzenie = new JLabel("Pochodzenie: Zzzz");
		dane.add(lblPochodzenie);
	}
	
}
