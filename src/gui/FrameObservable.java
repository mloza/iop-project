package gui;

public interface FrameObservable {
	void addListener(FrameObserver observer);
	void removeListener(FrameObserver observer);
	void notifyListeners();
}
