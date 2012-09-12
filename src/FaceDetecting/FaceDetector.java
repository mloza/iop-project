package FaceDetecting;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * User: scroot
 * Date: 11.09.12
 * Time: 19:13
 */
public class FaceDetector {
    IplImage[] trainingFaceImgArr;
    public void learn(final String fileName) {
        int i;

        trainingFaceImgArr = loadFaceImgArray(fileName);
    }

    /**
     * Odczyt nazw i nazw plików ludzi z pliku tekstowego i ładowanie listy tych obrazków
     * @param fileName nazwa pliku
     * @return Tablica obrazów twarzy
     */
    private IplImage[] loadFaceImgArray(String fileName) {
        IplImage faceImgArr;
        BufferedReader imgListFile;
        String imgFilename;
        int iFace = 0, nFaces = 0, i = 0;

        try {
            // Otwieramy plik wejściowy z listą obrazków
            imgListFile = new BufferedReader(new FileReader(fileName));
            String line;

            //liczenie twarzy, wczytuję plik po linijce
            while(true) {
                line = imgListFile.readLine();
                if(line == null || line.isEmpty()) {
                    break;
                }
                nFaces++;
            }
            Utilities.log("Wczytałem "+ nFaces+" lini z pliku.");



        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

        return new IplImage[0];  //To change body of created methods use File | Settings | File Templates.
    }

    public void Update(IplImage image) {

    }

}
