package FaceDetecting;

/**
 * User: scroot
 * Date: 11.09.12
 * Time: 19:14
 */
public class Tester {

    public static void main(String[] args) {
        FaceDetector fd = new FaceDetector();
        fd.learn("learnFile.txt");
    }
}
