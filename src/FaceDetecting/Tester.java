package FaceDetecting;

<<<<<<< HEAD
import java.util.Arrays;

import com.googlecode.javacv.cpp.opencv_core;

=======
>>>>>>> 298f84042ca96444895cc1e26af5418d47a97887
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

import com.googlecode.javacv.cpp.opencv_core;

public class Tester {

    public static void main(String[] args) {
        opencv_core.IplImage originalImage = cvLoadImage("zdjecie-grupowe.jpg", 1);
<<<<<<< HEAD
        int i = 0;
        for(Integer[] faceCoords : FaceDetector.detect(originalImage)) {
        	i++;
        	System.out.println(Arrays.toString(faceCoords));
        }
        System.out.println("Znalazłem " + i + " twarzy.");
=======
        FaceDetector.detect(originalImage);
        
        
>>>>>>> 298f84042ca96444895cc1e26af5418d47a97887
    }
}
