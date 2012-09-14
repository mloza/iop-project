package FaceDetecting;

import java.util.Arrays;
import com.googlecode.javacv.cpp.opencv_core;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

import com.googlecode.javacv.cpp.opencv_core;

public class Tester {

    public static void main(String[] args) {
        opencv_core.IplImage originalImage = cvLoadImage("zdjecie-grupowe.jpg", 1);

        int i = 0;
        for(Integer[] faceCoords : FaceDetector.detect(originalImage)) {
        	i++;
        	System.out.println(Arrays.toString(faceCoords));
        }
        System.out.println("Znalazłem " + i + " twarzy.");
    }
}
