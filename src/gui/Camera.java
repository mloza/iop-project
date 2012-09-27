package gui;

import java.util.*;

import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class Camera implements FrameObservable {
	public int imageWidth;
	public int imageHeight;
	
	FrameGrabber grabber;
	volatile boolean grabberOn;
	IplImage currentFrame;
	
	private List<FrameObserver> listeners = new ArrayList<FrameObserver>();
	
	public Camera(int cameraIdx) throws Exception {
		grabber = new OpenCVFrameGrabber(cameraIdx);
		setupCameraProperties();
	}
	
	private void setupCameraProperties() throws Exception {
		// To get camera properties we need only one frame, after that we turn off grabbing.
		grabber.start();
			IplImage frame = grabber.grab();
			imageWidth = frame.width();
			imageHeight = frame.height();
		grabber.stop();
	}
	
	public void startCapturing() throws Exception {
		grabber.start();
		grabberOn = true;
		
		while(grabberOn) {
			// grabber.grab() gets the frame and waits for the next one. It's blocking operation.
			currentFrame = grabber.grab();
			notifyListeners();
		}
	}
	
	public void stopCapturing() throws Exception {
		grabber.stop();
		grabberOn = false;
	}
	
	@Override
	public void addListener(FrameObserver observer) {
		listeners.add(observer);
	}

	@Override
	public void removeListener(FrameObserver observer) {
		listeners.remove(observer);
	}

	@Override
	public void notifyListeners() {
		for(FrameObserver observer : listeners) {
			observer.update(currentFrame);
		}
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
