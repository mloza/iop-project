package gui;


import FaceDetecting.FaceDetector;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


class FrameCaptureTask implements Runnable {
	Camera cam;
	
	public FrameCaptureTask(Camera cam) {
		this.cam = cam;
	}
	
	@Override
	public void run() {
		try {
			cam.startCapturing();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

// Przyjmijmy na razie, że to jest klasa, z której odpalana jest aplikacja. 
public class Main implements FrameObserver {
	//CanvasFrame canvasFrame = new CanvasFrame("InteligentEye Test CanvasFrame");
	CameraView cameraView;
	Camera cam;
	FrameObservable faceDetector;
	
	public static void main(String args[]) throws Exception {
		Camera c = new Camera(0);
		Main main = new Main(c, new FaceDetector(c, c.imageWidth, c.imageHeight));
		main.test();
		
		//CharacterPersonView.wlacz();
	}
	
	public Main(Camera cam, FrameObservable observable) throws Exception {
		this.cam = cam;
		this.faceDetector = observable;
		
		cameraView = new CameraView(faceDetector);
		cameraView.createView();
		
		System.out.println("CanvasFrame width = " + cam.imageWidth + ", height = " + cam.imageHeight);
		//canvasFrame.setCanvasSize(cam.imageWidth, cam.imageHeight);
		
		faceDetector.addListener(this);
	}
	
	void test() {
		FrameCaptureTask fct = new FrameCaptureTask(cam);
		new Thread(fct).start();
	}
	
	long old;
	long curr;
	
	@Override
	public void update(IplImage frame) {
		old = curr;
		curr = System.currentTimeMillis();
		//canvasFrame.showImage(frame);
		System.out.println("Frame time equals " + (curr - old) + " ms");
	}
}
