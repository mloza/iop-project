package gui;

public class FrameCaptureTask implements Runnable {
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