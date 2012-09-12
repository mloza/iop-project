package gui;

import java.util.*;

import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class Camera implements FrameObservable {
	FrameGrabber grabber;
	volatile boolean grabberOn;
	IplImage currentFrame;
	
	private List<FrameObserver> listeners = new ArrayList<FrameObserver>();
	
	public Camera(int cameraIdx) throws Exception {
		grabber = new OpenCVFrameGrabber(cameraIdx);
	}
	
	public void startCapturing() throws Exception {
		grabber.start();
		grabberOn = true;
		
		while(grabberOn) {
			currentFrame = grabber.grab(); // grabber.grab() nie zwraca dwukrotnie tej samej klatki tylko czeka na nową (blokuje się).
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
}
