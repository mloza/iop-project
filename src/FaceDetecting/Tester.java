package FaceDetecting;

import java.util.Arrays;
import com.googlecode.javacv.cpp.opencv_core;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

import com.googlecode.javacv.cpp.opencv_core;
import gui.FrameObservable;
import gui.FrameObserver;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;

public class Tester implements FrameObservable, FrameObserver {

    FrameObserver listener;
    opencv_core.IplImage frame;

    public static void main(String[] args) {
        Tester t = new Tester();
        FaceDetector f = new FaceDetector(t);
        f.addListener(t);
        t.notifyListeners();
    }

    Tester() {
        System.out.println("Ładuję obrazek");
        frame = cvLoadImage("testFaces/group_photo_2007.jpg", 1);
    }

    @Override
    public void update(opencv_core.IplImage frame) {
        System.out.println("odbieram obrobiony obrazek");
        System.out.println(frame);
        cvSaveImage("testFaces/group_photo_2007_face.jpg", frame);
    }

    @Override
    public void addListener(FrameObserver observer) {
        listener = observer;
    }

    @Override
    public void removeListener(FrameObserver observer) {
        // Do nothing
    }

    @Override
    public void notifyListeners() {
        System.out.println("powiadamiam słuchaczy");
        listener.update(frame);
    }
}
