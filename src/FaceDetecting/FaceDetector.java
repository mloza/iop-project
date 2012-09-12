package FaceDetecting;

import com.googlecode.javacv.cpp.opencv_objdetect;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.intersectConvexConvex;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;


public class FaceDetector {
    private static final String CASCADE_FILE = "haarcascade_frontalface_alt.xml";

    public static List<Integer[]> detect(IplImage originalImage){

        List<Integer[]> facesList = new ArrayList<Integer[]>();

        IplImage grayImage = IplImage.create(originalImage.width(), originalImage.height(), IPL_DEPTH_8U, 1);

        cvCvtColor(originalImage, grayImage, CV_BGR2GRAY);

        CvMemStorage storage = CvMemStorage.create();
        opencv_objdetect.CvHaarClassifierCascade cascade = new opencv_objdetect.CvHaarClassifierCascade(cvLoad(CASCADE_FILE));

        CvSeq faces = cvHaarDetectObjects(grayImage, cascade, storage, 1.1, 1, 0);

        Integer[] koordynaty = null;
        for (int i = 0; i < faces.total(); i++) {
            CvRect r = new CvRect(cvGetSeqElem(faces, i));
            koordynaty = new Integer[3];
            koordynaty[0] = r.x();
            koordynaty[1] = r.y();
            koordynaty[2] = r.height();
            koordynaty[3] = r.width();
            facesList.add(koordynaty);
        }
        return facesList;


    }
}