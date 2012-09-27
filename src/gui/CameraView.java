package gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class CameraView implements FrameObserver {
	FrameObservable frameProvider;
	
	JFrame viewFrame;
	CanvasFrame canvasFrame;
	
	public CameraView(FrameObservable frameProvider) {
		this.frameProvider = frameProvider;
		this.frameProvider.addListener(this);
	}
	
	public void createView() {
		viewFrame = new JFrame();
		viewFrame.setTitle("IntelligentEye - Camera View");
		viewFrame.setLayout(new BorderLayout());
		viewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		viewFrame.setSize(frameProvider.getFrameWidth(), frameProvider.getFrameHeight());
		
		// Create canvasFrame, add its ContentPane to this window and hide canvasFrame window.
		canvasFrame = new CanvasFrame("InteligentEye Test CanvasFrame");
		canvasFrame.setCanvasSize(frameProvider.getFrameWidth(), frameProvider.getFrameHeight());
		canvasFrame.setVisible(false);
		viewFrame.add(canvasFrame.getContentPane(), BorderLayout.CENTER);
		viewFrame.setVisible(true);
	}

	@Override
	public void update(IplImage frame) {
		canvasFrame.showImage(frame);
	}
}
