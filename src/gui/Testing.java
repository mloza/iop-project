package gui;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


class FrameCapturingTask implements Runnable {
	Camera cam;
	
	public FrameCapturingTask(Camera cam) {
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
public class Testing implements FrameObserver{
	Camera cam;
	CanvasFrame canvasFrame = new CanvasFrame("Some Title");
	long currentMilis = 0;
	long oldMilis = 0;
	
	public static void main(String args[]) throws Exception {
		Testing main = new Testing(new Camera(0));
		main.test();
	}
	
	public Testing(Camera cam) throws Exception {
		this.cam = cam;
		this.cam.addListener(this);
		
		canvasFrame.setCanvasSize(400, 400);
	}
	
	void test() {
		FrameCapturingTask fct = new FrameCapturingTask(cam);
		new Thread(fct).start();
	}
	
	@Override
	public void update(IplImage frame) {
		oldMilis = currentMilis;
	    currentMilis = System.currentTimeMillis();
		System.out.println("Got frame! (it takes " + (currentMilis - oldMilis) + ")");
		
		canvasFrame.showImage(frame);
	}
}
