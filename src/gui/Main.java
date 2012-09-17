package gui;

import java.util.Arrays;
import java.util.List;

import FaceDetecting.FaceDetector;
import FaceDetecting.FrameObservableWithCoords;
import FaceDetecting.FrameObserverWithCoords;

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
public class Main implements FrameObserverWithCoords {
	CanvasFrame canvasFrame = new CanvasFrame("InteligentEye Test CameraView");
	Camera cam;
	FrameObservableWithCoords faceDetector;
	
	public static void main(String args[]) throws Exception {
		Camera c = new Camera(0);
		Main main = new Main(c, new FaceDetector(c, c.imageWidth, c.imageHeight));
		main.test();
		
		CharacterPersonView.wlacz();
	}
	
	public Main(Camera cam, FrameObservableWithCoords observable) throws Exception {
		this.cam = cam;
		this.faceDetector = observable;
		
		
		System.out.println("CanvasFrame width = " + cam.imageWidth + ", height = " + cam.imageHeight);
		canvasFrame.setCanvasSize(cam.imageWidth, cam.imageHeight);
		
		faceDetector.addListener(this);
	}
	
	void test() {
		FrameCaptureTask fct = new FrameCaptureTask(cam);
		new Thread(fct).start();
	}
	
	long old;
	long curr;
	
	@Override
	public void update(IplImage frame, List<Integer[]> coords) {
		old = curr;
		curr = System.currentTimeMillis();
		canvasFrame.showImage(frame);
		System.out.println("Frame time equals " + (curr - old) + " ms");
		System.out.print("Faces coordinates: ");
		for(Integer[] face : coords) {
			System.out.println(Arrays.toString(face));
		}
	}
}
