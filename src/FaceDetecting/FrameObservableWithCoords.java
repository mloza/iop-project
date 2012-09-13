package FaceDetecting;

/**
 * User: scroot
 * Date: 13.09.12
 * Time: 21:06
 */
public interface FrameObservableWithCoords {
    void addListener(FrameObserverWithCoords observer);
    void removeListener(FrameObserverWithCoords observer);
    void notifyListeners();
}
