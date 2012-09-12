package FaceDetecting;

import com.googlecode.javacv.cpp.opencv_core;

import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

public class Tester {

    public static void main(String[] args) {
        opencv_core.IplImage originalImage = cvLoadImage("zdjecie-grupowe.jpg", 1);
        FaceDetector.detect(originalImage);
    }
}
