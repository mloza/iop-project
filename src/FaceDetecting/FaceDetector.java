package FaceDetecting;

import static com.googlecode.javacv.cpp.opencv_core.CV_AA;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;
import gui.FrameObservable;
import gui.FrameObserver;

import java.util.ArrayList;
import java.util.List;

import FaceDetecting.FrameObservableWithCoords;
import FaceDetecting.FrameObserverWithCoords;

import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_objdetect;

/**
 * Class implements interfaces FrameObserver, FrameObservableWithCoords,
 * FrameObservable. It uses observer design pattern to communicate. Uses face
 * recognition to set coordinates of faces to photo. There are two lists of
 * observers, one with coordinates, second without
 * 
 * @author A.Czarnota, M.Gruszka, M.Loza
 * @see FaceDetecting.FrameObesrver
 * @see FaceDetecting.FrameObservableWithCoords
 * @see FaceDetecting.FrameObservable
 */

public class FaceDetector implements FrameObserver, FrameObservableWithCoords,
		FrameObservable {

	/**
	 * List of observers which needs coordinates of faces
	 */
	private List<FrameObserverWithCoords> observersWithCoords = new ArrayList<FrameObserverWithCoords>();
	/**
	 * List of observers
	 */
	private List<FrameObserver> observers = new ArrayList<FrameObserver>();
	/**
	 * image captured from camera
	 */
	private IplImage frame;
	/**
	 * xml file with info about face recognition
	 */
	private static final String CASCADE_FILE = "haarcascade_frontalface_alt.xml";

	/**
	 * Receives frame and uses face recognition algorithms to set coordinates of
	 * faces
	 * 
	 * @param originalImage
	 * @return list of integer arrays, with coordinates and size of faces
	 */
	public static List<Integer[]> detect(IplImage originalImage) {

		List<Integer[]> facesList = new ArrayList<Integer[]>();

		IplImage grayImage = IplImage.create(originalImage.width(),
				originalImage.height(), IPL_DEPTH_8U, 1);

		cvCvtColor(originalImage, grayImage, CV_BGR2GRAY);

		CvMemStorage storage = CvMemStorage.create();
		opencv_objdetect.CvHaarClassifierCascade cascade = new opencv_objdetect.CvHaarClassifierCascade(
				cvLoad(CASCADE_FILE));

		CvSeq faces = cvHaarDetectObjects(grayImage, cascade, storage, 1.1, 1,
				0);

		Integer[] coordinates = null;
		for (int i = 0; i < faces.total(); i++) {
			CvRect r = new CvRect(cvGetSeqElem(faces, i));
			coordinates = new Integer[4];
			coordinates[0] = r.x();
			coordinates[1] = r.y();
			coordinates[2] = r.height();
			coordinates[3] = r.width();
			facesList.add(coordinates);
		}
		return facesList;
	}

	/**
	 * Adds listener to observable list
	 * 
	 * @param observable
	 */
	public FaceDetector(FrameObservable observable) {
		observable.addListener(this);
	}

	/**
	 * @Override method from FrameObserver
	 * @param
	 * @see gui.FrameObserver
	 */
	public void update(IplImage frame) {
		List<Integer[]> coords = FaceDetector.detect(frame);
		this.notifyListeners(frame, coords);
		this.frame = frame;
		addRectangles(coords);
		this.notifyListeners();
	}

	/**
	 * Adds rectangles surrounding faces
	 * 
	 * @param coords
	 */
	private void addRectangles(List<Integer[]> coords) {
		for (Integer[] i : coords) {
			cvRectangle(this.frame, cvPoint(i[0], i[1]),
					cvPoint(i[0] + i[2], i[1] + i[3]), CvScalar.YELLOW, 1,
					CV_AA, 0);
		}
	}

	/**
	 * Adds listeners to observersWithCoords list
	 * 
	 * @Override method from FrameObservableWithCoords
	 * @see FaceDetector.FrameObservableWithCoords
	 */
	public void addListener(FrameObserverWithCoords observer) {
		observersWithCoords.add(observer);
	}

	/**
	 * Removes listeners from observersWithCoords list
	 * 
	 * @Override method from FrameObservableWithCoords
	 * @see FaceDetector.FrameObservableWithCoords
	 */
	public void removeListener(FrameObserverWithCoords observer) {
		observersWithCoords.remove(observer);
	}

	/**
	 * Notifies all listeners
	 * 
	 * @Override method from FrameObservableWithCoords and FrameObservable
	 * @see FaceDetector.FrameObservableWithCoords
	 * @see gui.FrameObservable
	 */
	public void notifyListeners() {
		for (FrameObserver i : observers) {
			i.update(this.frame);
		}
	}

	/**
	 * Notifies listeners from observersWithCoords list
	 * 
	 * @param frame
	 * @param coords
	 */
	public void notifyListeners(IplImage frame, List<Integer[]> coords) {
		for (FrameObserverWithCoords i : observersWithCoords) {
			i.update(frame, coords);
		}
	}

	/**
	 * Adds listeners to list of observers
	 * 
	 * @param
	 * @Override method from FrameObservable
	 * @see gui.FrameObservable
	 */
	public void addListener(FrameObserver observer) {
		observers.add(observer);
	}

	/**
	 * Removes listeners from list of observers
	 * 
	 * @param
	 * @Override method from FrameObservable
	 * @see gui.FrameObservable
	 */
	public void removeListener(FrameObserver observer) {
		observers.remove(observer);
	}
}