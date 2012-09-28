package FaceDetecting;

import com.googlecode.javacv.cpp.opencv_core;
import gui.FrameObservable;
import gui.FrameObserver;

import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;

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
     * @param args
     */
    public static void main(String[] args) {
        Tester t = new Tester();
        FaceDetector f = new FaceDetector(t, t.frame.width(), t.frame.height());
        Recognizer recognizer = new Recognizer(f);

        f.addListener(t);
        t.notifyListeners();
    }

    /**
     * Loads image to process
     */
    Tester() {
        System.out.println("Ładuję obrazek");
        frame = cvLoadImage("tst1/4.jpg", 1);
        //opencv_core.IplImage tmp = cvCreateImage(new opencv_core.CvSize(100, 100), frame.depth(), frame.nChannels());
        //cvResize(frame, tmp);
        //frame = tmp;
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
