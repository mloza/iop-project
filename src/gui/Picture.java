package gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import ppm.*;
import javax.swing.*;

import ppm.PPMReader;

public class Picture extends JPanel {
	public void paintComponent(Graphics g) {
		BufferedImage obrazek = null;
		try {
			obrazek = PPMReader.readFile("learnFaces/00041_930831_fa.ppm");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncompatibleFormat e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenImageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		g.drawImage(obrazek, 0, 0, 200,300, this); // rysowanie ze skalowaniem do rozmiarów 200x300
	}

}
