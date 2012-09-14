package FaceDetecting;

/**
 * Interface used to specify which of observers needs to obtain coordinates
 * 
 * @author M.Loza
 */

public interface FrameObservableWithCoords {
	void addListener(FrameObserverWithCoords observer);

	void removeListener(FrameObserverWithCoords observer);

	void notifyListeners();
}
