package gui;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

interface FrameObserver {
	void update(IplImage frame);
}

public interface FrameObservable {
	void addListener(FrameObserver observer);
	void removeListener(FrameObserver observer);
	void notifyListeners();
}
