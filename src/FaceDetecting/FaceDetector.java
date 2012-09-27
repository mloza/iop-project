package FaceDetecting;

import static com.googlecode.javacv.cpp.opencv_core.CV_AA;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvClearMemStorage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_AREA;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvResize;
import static com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;
import gui.FrameObservable;
import gui.FrameObserver;

import java.util.ArrayList;
import java.util.List;

import FaceDetecting.FrameObservableWithCoords;
import FaceDetecting.FrameObserverWithCoords;

import com.googlecode.javacpp.Loader;
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
	
	private int imageWidth;
	private int imageHeight;
	/**
	 * xml file with info about face recognition
	 */
	private static final String CASCADE_FILE = "haarcascade_frontalface_alt.xml";
	private opencv_objdetect.CvHaarClassifierCascade classifier;
	private IplImage grayImage;
	private IplImage smallImage;
	private CvMemStorage storage;
	
	private static final int REDUCTION = 3; 
	
	void init(int imageWidth, int imageHeight) {
		// Preload the opencv_objdetect module to work around a known bug.
        Loader.load(opencv_objdetect.class);
        
		classifier = new opencv_objdetect.CvHaarClassifierCascade(cvLoad(CASCADE_FILE));
		grayImage = IplImage.create(imageWidth, imageHeight, IPL_DEPTH_8U, 1);
		smallImage = IplImage.create(imageWidth/REDUCTION, imageHeight/REDUCTION, IPL_DEPTH_8U, 1);
		storage = CvMemStorage.create();
	}
	
	/**
	 * Receives frame and uses face recognition algorithms to set coordinates of
	 * faces
	 * 
	 * @param originalImage
	 * @return list of integer arrays, with coordinates and size of faces
	 */
	public List<Integer[]> detect(IplImage originalImage) {

		List<Integer[]> facesList = new ArrayList<Integer[]>();
		
		cvClearMemStorage(storage);
		cvCvtColor(originalImage, grayImage, CV_BGR2GRAY);
		cvResize(grayImage, smallImage, CV_INTER_AREA);
		// Search faces on image <SCALE_FACTOR> times smaller ...
		CvSeq faces = cvHaarDetectObjects(smallImage, classifier, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);

		Integer[] coordinates = null;
		for (int i = 0; i < faces.total(); i++) {
			CvRect r = new CvRect(cvGetSeqElem(faces, i));
			coordinates = new Integer[4];
			// ... so coordinates needs multiplying by SCALE_FACTOR
			coordinates[0] = r.x() * REDUCTION;
			coordinates[1] = r.y() * REDUCTION;
			coordinates[2] = r.height() * REDUCTION;
			coordinates[3] = r.width() * REDUCTION;
			facesList.add(coordinates);
		}
		return facesList;
	}

	/**
	 * Adds listener to observable list
	 * 
	 * @param observable
	 */
	public FaceDetector(FrameObservable observable, int frameWidth, int frameHeight) {
		imageWidth = frameWidth;
		imageHeight = frameHeight;
		init(frameWidth, frameHeight);
		observable.addListener(this);
	}

	/**
	 * @Override method from FrameObserver
	 * @param
	 * @see gui.FrameObserver
	 */
	public void update(IplImage frame) {
		List<Integer[]> coords = detect(frame);
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

	@Override
	public int getFrameWidth() {
		return imageWidth;
	}

	@Override
	public int getFrameHeight() {
		return imageHeight;
	}
}