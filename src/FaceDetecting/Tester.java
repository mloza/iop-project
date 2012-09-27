package FaceDetecting;

import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import gui.FrameObservable;
import gui.FrameObserver;

import com.googlecode.javacv.cpp.opencv_core;

/**
 * Testing class, creates listeners and sends sample image. Implements
 * FrameObservable and FrameObserver interfaces
 * 
 * @author A.Czarnota, M.Gruszka, M.Loza
 * @see gui.FrameObservable
 * @see gui.FrameObserver
 */
public class Tester implements FrameObservable, FrameObserver {
	/**
	 * listener
	 */
	FrameObserver listener;
	/**
	 * image to recognize faces
	 */
	opencv_core.IplImage frame;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Tester t = new Tester();
		FaceDetector f = new FaceDetector(t, 1802, 1202);
		f.addListener(t);
		t.notifyListeners();
	}

	Tester() {
		System.out.println("Ładuję obrazek");
		frame = cvLoadImage("testFaces/group_photo_2007.jpg", 1);
	}

	/**
	 * Saves image with rectangles around faces
	 * 
	 * @Override
	 */
	public void update(opencv_core.IplImage frame) {
		System.out.println("odbieram obrobiony obrazek");
		System.out.println(frame);
		cvSaveImage("testFaces/group_photo_2007_face.jpg", frame);
	}

	/**
	 * @Override
	 */
	public void addListener(FrameObserver observer) {
		listener = observer;
	}

	/**
	 * @Override because it had to be
	 */
	public void removeListener(FrameObserver observer) {
		// Do nothing
	}

	/**
	 * Notifies all listeners about new frame
	 * 
	 * @Override
	 */
	public void notifyListeners() {
		System.out.println("powiadamiam słuchaczy");
		listener.update(frame);
	}

	@Override
	public int getFrameWidth() {
		// WYMAGANE TYLKO GDY OBRAZ WYŚWIETLAMY
		return 0;
	}

	@Override
	public int getFrameHeight() {
		// WYMAGANE TYLKO GDY OBRAZ WYŚWIETLAMY
		return 0;
	}
}
