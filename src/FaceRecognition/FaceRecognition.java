//     _____  _____  _____                                                                            
//    / ____|/ ____|/ ____|                                                                           
//   | |  __| |  __| |                                                                                
//   | | |_ | | |_ | |                                                                                
//   | |__| | |__| | |____                                                                            
//    \_____|\_____|\_____|           _       _                _                                  _   
//                                   | |     | |              | |                                | |  
//     __ _  ___ _ __   ___ _ __ __ _| |   __| | _____   _____| | ___  _ __  _ __ ___   ___ _ __ | |_ 
//    / _` |/ _ \ '_ \ / _ \ '__/ _` | |  / _` |/ _ \ \ / / _ \ |/ _ \| '_ \| '_ ` _ \ / _ \ '_ \| __|
//   | (_| |  __/ | | |  __/ | | (_| | | | (_| |  __/\ V /  __/ | (_) | |_) | | | | | |  __/ | | | |_ 
//    \__, |\___|_| |_|\___|_|  \__,_|_|  \__,_|\___| \_/ \___|_|\___/| .__/|_| |_| |_|\___|_| |_|\__|
//     __/ |                                                          | |                             
//    |___/     __      _                _                            |_|                             
//      __     / _|    (_)              | |                                                           
//    ( _ )   | |_ _ __ _  ___ _ __   __| |___                                                        
//    / _ \/\ |  _| '__| |/ _ \ '_ \ / _` / __|                                                       
//   | (_>  < | | | |  | |  __/ | | | (_| \__ \                                                       
//    \___/\/ |_| |_|  |_|\___|_| |_|\__,_|___/                                                       
//                                                                                                    
//     Gaweł and Grzebinoga Company & Obszyński
//
//     Wymaga zainstalowanego i dolaczonego OpenCV 2.4.2 oraz dolaczanej biblioteki javacv
 

package FaceRecognition;


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

public class FaceRecognition {
    int nTrainFaces = 0;
    int nEigens = 0;
    IplImage[]  trainingFaceImgArr;
    CvMat personNumTruthMat;
    IplImage pAvgTrainImg;
    int nPersons;
    final List<String> personNames = new ArrayList<String>();
    IplImage[] eigenVectArr;
    //CvMat[] eigenValMat;
    CvMat eigenValMat; //to nie moze byc tablica, jesli bedzie wplywac na inne metody to dajcie znac
    CvMat projectedTrainFaceMat;
    
    void learn()
    {
         final String trainFileName = "train.txt";
         trainingFaceImgArr = loadFaceImgArray(trainFileName); 

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
    
    //PCA wymaga poprawek ale zeby to sprawdzic musza byc wszystkie metody zrobione
    //PCA to glowna metoda algorytmu eigenface. To ona 'rozbija' twarz na wektory 
    //i normalizuje dzieki czemu mozna je potem porownywac
    //i szukac najmniejszego 'distance'
    //znajac zycie pewnie nie bedzie dzialac ;)
    private void doPCA() {
    int i;
    CvTermCriteria calcLimit;
    CvSize faceImgSize = new CvSize();

    // set the number of eigenvalues to use
    nEigens = nTrainFaces - 1;

    // allocate the eigenvector images
    faceImgSize.width(trainingFaceImgArr[0].width());
    faceImgSize.height(trainingFaceImgArr[0].height());
    eigenVectArr = new IplImage[nEigens];


    for (i = 0; i < nEigens; i++) {
      eigenVectArr[i] = cvCreateImage(
              faceImgSize, // size
              IPL_DEPTH_32F, // depth
              1); // channels
    }

    // allocate the eigenvalue array
    eigenValMat = cvCreateMat(
            1, // rows
            nEigens, // cols
            CV_32FC1); // type, 32-bit float, 1 channel

    // allocate the averaged image
    pAvgTrainImg = cvCreateImage(
            faceImgSize, // size
            IPL_DEPTH_32F, // depth
            1); // channels

    // set the PCA termination criterion
    calcLimit = cvTermCriteria(
            CV_TERMCRIT_ITER, // type
            nEigens, // max_iter
            1); // epsilon

  
    // compute average image, eigenvalues, and eigenvectors
    cvCalcEigenObjects(
            nTrainFaces, // nObjects
            new PointerPointer(trainingFaceImgArr), // input
            new PointerPointer(eigenVectArr), // output
            CV_EIGOBJ_NO_CALLBACK, // ioFlags
            0, // ioBufSize
            null, // userData
            calcLimit,
            pAvgTrainImg, // avg
            eigenValMat.data_fl()); // eigVals

    //normalizacja czyli odjecie avarage face
    cvNormalize(
            eigenValMat, // src (CvArr)
            eigenValMat, // dst (CvArr)
            1, // a
            0, // b
            CV_L1, // norm_type
            null); // mask
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
    private IplImage[] loadFaceImgArray(String filename)
    {
        IplImage[] faceImgArr;
        BufferedReader imgListFile;
        String imgFilename;
        int iFace = 0;
        int nFaces = 0;
        int i;
        
        try 
        {
            imgListFile = new BufferedReader(new FileReader(filename));
            
            while(true)
            {
                final String line = imgListFile.readLine();
                if(line == null || line.isEmpty())
                    break;
                nFaces++;
            }
            System.out.println("loadingImgArray-> liczba plikow: " + nFaces);
            imgListFile = new BufferedReader(new FileReader(filename));
            
            faceImgArr = new IplImage[nFaces];
            personNumTruthMat = cvCreateMat(
                    1, //rows
                    nFaces, //cols
                    CV_32SC1 //type 32 bit unsigned 1 ch
                    );
            for(int j1 = 0; j1< nFaces;j1++)
            {
                String personName;
                String sPersonName;
                int personNumber;
                
                //reading person number
                final String line = imgListFile.readLine();
                if(line.isEmpty())
                {
                    break;
                }
                final String[] tokens = line.split(" ");
                personNumber = Integer.parseInt(tokens[0]);
                personName = tokens[1];
                imgFilename = tokens[2];
                sPersonName = personName;
                System.out.println("Got: "+iFace+" "+personNumber+" "+personName+" "+imgFilename);
                //check if a new person is beaing loaded
                if(personNumber > nPersons)
                {
                    //allocate memory
                    personNames.add(sPersonName);
                    nPersons = personNumber;
                    System.out.println("Added new person"+sPersonName+" ->nPersons = " + nPersons + " ["+personNames.size()+"] ");
                }
                
                //keep the data
                personNumTruthMat.put(
                        0, //i
                        iFace, //j
                        personNumber); //v
                //load the face img
                faceImgArr[iFace] = cvLoadImage(
                        imgFilename, //filename
                        CV_LOAD_IMAGE_GRAYSCALE //is color
                        );
                if(faceImgArr[iFace] == null)
                {
                    throw new RuntimeException("Can't load image from "+imgFilename);
                }
            }
            
            imgListFile.close();
        } catch(IOException ex)
        {
            throw new RuntimeException(ex);
        }
        System.out.println("Data loaded from"+filename+"': ("+nFaces+" images of "+nPersons+" people).");
        
        return faceImgArr;
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
        FaceRecognition nowy = new FaceRecognition();
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
