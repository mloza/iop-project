package gui;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.SpringLayout;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import common.Person;

import java.awt.GridLayout;


public class CharacterPersonView extends JFrame {

	private JPanel contentPane;
	private final JPanel panel_1 = new JPanel();
	private final JLabel lblNewLabel = new JLabel();
	private final JLabel lblMatch = new JLabel();
	private final JLabel lblNewLabel_1 = new JLabel("Picture");

	/**
	 * Launch the application.
	 */
	public static void createWindow(final Person person) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CharacterPersonView frame = new CharacterPersonView(person);
					frame.setVisible(true);
					frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CharacterPersonView(Person person) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 634, 179);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		panel_1.setBounds(124, 23, 486, 100);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		lblNewLabel.setText("Imię i nazwisko:" + person.getFirstname() + " " + person.getLastname());
		lblNewLabel.setBounds(0, 23, 474, 22);
		panel_1.add(lblNewLabel);
		lblMatch.setBounds(0, 57, 474, 22);
		lblMatch.setText("Współczynnik podobieństwa: " + person.getMatchCoefficient());
		panel_1.add(lblMatch);
		
		JPanel panel = new JPanel();
		panel.setBounds(12, 23, 100, 100);
		contentPane.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		// obraz do sprawdzenia
		ImageIcon img = new ImageIcon(person.getPicture().getBufferedImage());
		lblNewLabel_1.setIcon(img);
		panel.add(lblNewLabel_1);
	}

}
