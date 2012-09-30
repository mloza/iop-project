package gui;

import FaceDetecting.FaceDetector;

import javax.swing.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Application {
	public static String CURRENT_DIRECTORY;
	
	private CameraView cameraView;
	private Camera cam;
	private FrameObservable faceDetector;
	
	public static void main(String args[]) {
		try {
			new Application();
		} catch (Exception e) {
			Logger.logException(e);
			JOptionPane.showMessageDialog(null, "Unable to start application, check log file for more informations.",
					"Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	public Application() throws Exception {
		CURRENT_DIRECTORY = getCurrentDirectory();
		
		try {
			Logger.create(CURRENT_DIRECTORY + "/InteligentEye.log");
		} catch(IOException e) {
			JOptionPane.showMessageDialog(null, "Unable to create log file.", "Warning!", JOptionPane.WARNING_MESSAGE);
		}
		Logger.log("InteligentEye application started from \"" + CURRENT_DIRECTORY + "\"");
		
		createCamera();
		createFaceDetector();
		createApplicationView();
		
		startCapturingTask();
	}
	
	private String getCurrentDirectory() throws UnsupportedEncodingException {
		String currentDir = null;
		String undecodedPath = Application.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String decodedPath = URLDecoder.decode(undecodedPath, "UTF-8");
		
		if(decodedPath.endsWith("/")) {
			// If application was lunched not as JAR
			String withoutLastSlash = decodedPath.substring(0, decodedPath.length()-1);
			currentDir = withoutLastSlash.substring(0, withoutLastSlash.lastIndexOf("/"));
		} else {
			// If lunched as JAR
			currentDir = decodedPath.substring(0, decodedPath.lastIndexOf("/"));
		}

		return currentDir;
	}
	
	private void createCamera() throws Exception {
		cam = new Camera(2);
		Logger.log("Access to video device gained.");
	}
	
	private void createFaceDetector() {
		faceDetector = new FaceDetector(cam, cam.imageWidth, cam.imageHeight);
	}
	
	private void createApplicationView() {
		cameraView = new CameraView(faceDetector);
		cameraView.createView();
	}
	
	private void startCapturingTask() throws Exception {
		FrameCaptureTask fct = new FrameCaptureTask(cam);
		new Thread(fct).start();
	}
}
