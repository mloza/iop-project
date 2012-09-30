package gui;
import common.Person;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


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
        if(person.getWindow() == null) {
            person.setWindow(1);
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        CharacterPersonView frame = new CharacterPersonView(person);
	                    person.setFrameWindow(frame);
                        frame.setVisible(true);
                        frame.setResizable(false);
                        frame.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosing(WindowEvent e) {
                                person.setWindow(null);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
	}

	/**
	 * Create the frame.
	 */
	public CharacterPersonView(Person person) {
       // person.setWindow(this);
		System.out.println(person);
		setTitle("Uwaga, pojawiła się podejrzana osoba!");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		ImageIcon img;
		if(person.getPicture() != null) {
			img = new ImageIcon(person.getPicture().getBufferedImage());
		}
		else {
			img = new ImageIcon();
		}

		lblNewLabel_1.setIcon(img);
		panel.add(lblNewLabel_1);
	}

}
