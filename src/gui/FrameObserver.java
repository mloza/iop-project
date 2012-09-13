package gui;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

/**
 * User: scroot
 * Date: 13.09.12
 * Time: 20:55
 */
public interface FrameObserver {
    void update(IplImage frame);
}