/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FaceRec;


import com.googlecode.javacpp.FloatPointer;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.PointerPointer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_legacy.*;

/**
 *
 * @author Bartek
 */
public class test {
    int nTrainFaces = 0;
    int nEigens = 0;
    IplImage[]  trainingFaceImgArr;
    CvMat[] personNumTruthMat;
    IplImage pAvgTrainImg;
    IplImage[] eigenVectArr;
    CvMat[] eigenValMat;
    CvMat projectedTrainFaceMat;
    
    void learn()
    {
         final String trainFileName = "train.txt";
         //trainingFaceImgArr = loadFaceImgArray(trainFileName); generuje blad

         nTrainFaces = trainingFaceImgArr.length;
         System.out.println(nTrainFaces);
         if(nTrainFaces < 2)
             System.out.println("Za malo twarzy treningowych!");
         doPCA();
         projectedTrainFaceMat =  cvCreateMat(nTrainFaces, nEigens, CV_32FC1);
         final FloatPointer floatPointer = new FloatPointer(nEigens);
         for(int i=0; i<nTrainFaces; i++)
         {
             cvEigenDecomposite(trainingFaceImgArr[i], nEigens, new PointerPointer(eigenVectArr), 0,null, pAvgTrainImg, floatPointer);

         }
         storeTrainingData();
    }
    static void recognize()
    {
        System.out.println("ROZPOZNAWANIE");
        
    }
    void doPCA()
    {
        
    }
    void storeTrainingData()
    {
        
    }
    int loadTrainingData(CvMat[] pTrainPersonNumMat)
    {
        return 0;
    }
    int findNearestNeighbor(float[] projectedTestFace)
    {
        return 0;
    }
    int loadFaceImgArray(String traintxt)
    {
        return 0;
    }
    static void printUsage()
    {
        System.out.println("Usage: eigenface <command>\n"
                + "Valid commands are:\n"
                + "train\n"
                + "test\n");
    }
    public static void main(String[] args)
    {
        test nowy = new test();
        if(args.length == 0)
        {
            
            printUsage();
            
        } else if(!args[0].equals("train"))
            nowy.learn();
        else if(!args[0].equals("test")) 
            recognize();
        else
        {
            System.out.println("Komenda nieznana: "+args[0]);
            printUsage();
        }
    }
}
