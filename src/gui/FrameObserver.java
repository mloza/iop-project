package gui;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

public interface FrameObserver {
	void update(IplImage frame);
}
