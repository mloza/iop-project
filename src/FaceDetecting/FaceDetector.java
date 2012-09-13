package FaceDetecting;

import com.googlecode.javacv.cpp.opencv_objdetect;
import gui.FrameObservable;
import gui.FrameObserver;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;


public class FaceDetector implements FrameObserver, FrameObservableWithCoords, FrameObservable {
    private List<FrameObserverWithCoords> observers = new ArrayList<FrameObserverWithCoords>();
    private List<FrameObserver> observersWithoutCoords = new ArrayList<FrameObserver>();
    private IplImage frame;

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
            koordynaty = new Integer[4];
            koordynaty[0] = r.x();
            koordynaty[1] = r.y();
            koordynaty[2] = r.height();
            koordynaty[3] = r.width();
            facesList.add(koordynaty);
        }
        return facesList;
    }

    public FaceDetector(FrameObservable observable) {
        observable.addListener(this);
    }

    @Override
    public void update(IplImage frame) {
        List<Integer[]> coords = FaceDetector.detect(frame);
        this.notifyListeners(frame, coords);
        this.frame = frame;
        addRectangles(coords);
        this.notifyListeners();
    }

    private void addRectangles(List<Integer[]> coords) {
        for(Integer[] i: coords) {
            cvRectangle(this.frame, cvPoint(i[0], i[1]),
                    cvPoint(i[0] + i[2], i[1] + i[3]), CvScalar.YELLOW, 1, CV_AA, 0);
        }
    }

    @Override
    public void addListener(FrameObserverWithCoords observer) {
        observers.add(observer);
    }

    @Override
    public void removeListener(FrameObserverWithCoords observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyListeners() {
        for(FrameObserver i: observersWithoutCoords) {
            i.update(this.frame);
        }
    }

    public void notifyListeners(IplImage frame, List<Integer[]> coords) {
        for(FrameObserverWithCoords i: observers) {
            i.update(frame, coords);
        }
    }

    @Override
    public void addListener(FrameObserver observer) {
        observersWithoutCoords.add(observer);
    }

    @Override
    public void removeListener(FrameObserver observer) {
        observersWithoutCoords.remove(observer);
    }
}