package FaceDetecting;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import java.util.List;

/**
 * User: scroot
 * Date: 13.09.12
 * Time: 21:02
 */
public interface FrameObserverWithCoords {
    void update(IplImage frame, List<Integer[]> coords);
}
