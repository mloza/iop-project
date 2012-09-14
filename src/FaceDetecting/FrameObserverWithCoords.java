package FaceDetecting;

import java.util.List;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

/**
 * Interface used to notify all listeners which need coordinates of faces
 * 
 * @author M.Loza
 */
public interface FrameObserverWithCoords {
	void update(IplImage frame, List<Integer[]> coords);
}
