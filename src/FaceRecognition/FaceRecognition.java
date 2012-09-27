package FaceRecognition;


import FaceDetecting.FrameObservableWithCoords;
import FaceDetecting.FrameObserverWithCoords;
import com.googlecode.javacpp.FloatPointer;
import com.googlecode.javacpp.Pointer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_LOAD_IMAGE_GRAYSCALE;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_legacy.*;

public class FaceRecognition implements FrameObserverWithCoords {
    int nTrainFaces = 0;
    int nEigens = 0;
    IplImage[] trainingFaceImgArr;
    CvMat personNumTruthMat;
    IplImage pAvgTrainImg;
    IplImage testFaceImg;
    IplImage[] testFaceImgArr;
    int nPersons;
    final List<String> personNames = new ArrayList<String>();
    IplImage[] eigenVectArr;
    //CvMat[] eigenValMat;
    CvMat eigenValMat; //to nie moze byc tablica, jesli bedzie wplywac na inne metody to dajcie znac
    CvMat projectedTrainFaceMat;

    public FaceRecognition() {

    }

    public FaceRecognition(FrameObservableWithCoords observable) {
        observable.addListener(this);
        learn();
    }

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
            cvEigenDecomposite(trainingFaceImgArr[i], nEigens, eigenVectArr, 0,null, pAvgTrainImg, floatPointer);

        }
        storeTrainingData();
    }


    public void recognize(IplImage face) {
        CvMat trainPersonNumMat;
        float[] projectedTestFace;
        double timeFaceRecognizeStart;
        double tallyFaceRecognizeTime;
        float confidence = 0.0f;
        String answer;
        int nCorrect = 0;
        int nWrong = 0;
        testFaceImg = face;
        System.out.println("Twarz do badania przyjęta.");
        trainPersonNumMat = loadTrainingData();
        personNumTruthMat = cvCreateMat(
                1, //rows
                1, //cols
                CV_32SC1 //type 32 bit unsigned 1 ch
        );
        if(trainPersonNumMat == null)
        {
            return;
        }
        //PCA
        projectedTestFace = new float[nEigens];
        timeFaceRecognizeStart = (double) cvGetTickCount();

        int iNearest;
        int nearest;
        int truth;

        cvEigenDecomposite(testFaceImg,
                nEigens,
                eigenVectArr,
                0,
                null,
                pAvgTrainImg,
                projectedTestFace);
        final FloatPointer pConfidence = new FloatPointer(confidence);
        iNearest = findNearestNeighbor(projectedTestFace, new FloatPointer(pConfidence));
        confidence = pConfidence.get();
        truth = personNumTruthMat.data_i().get(0);
        nearest = trainPersonNumMat.data_i().get(iNearest);

        if (nearest == truth)
        {
            answer = "Correct";
            nCorrect++;
        } else
        {
            answer = "WRONG!";
            nWrong++;
        }
        System.out.println("nearest = " + nearest + ", Truth = " + truth + " (" + answer + "). Confidence = " + confidence);

        tallyFaceRecognizeTime = (double) cvGetTickCount() - timeFaceRecognizeStart;
        if (nCorrect + nWrong > 0) {
            System.out.println("TOTAL ACCURACY: " + (nCorrect * 100 / (nCorrect + nWrong)) + "% out of " + (nCorrect + nWrong) + " tests.");
            System.out.println("TOTAL TIME: " + (tallyFaceRecognizeTime / (cvGetTickFrequency() * 1000.0 * (nCorrect + nWrong))) + " ms average.");
        }
    }

    //PCA wymaga poprawek ale zeby to sprawdzic musza byc wszystkie metody zrobione
    //PCA to glowna metoda algorytmu eigenface. To ona 'rozbija' twarz na wektory 
    //i normalizuje dzieki czemu mozna je potem porownywac
    //i szukac najmniejszego 'distance'
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
        // System.out.println(calcLimit);
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
        // compute average image, eigenvalues, and eigenvectors TU JEST PROBLEM
        cvCalcEigenObjects(
                nTrainFaces, // nObjects
                trainingFaceImgArr, // input
                eigenVectArr, // output
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

        System.out.println("PCA done.");
    }

    void storeTrainingData()
    {
        CvFileStorage fileStorage;
        int i;

        // create a file-storage interface
        fileStorage = cvOpenFileStorage(
                "data/facedata.xml", // filename
                null, // memstorage
                CV_STORAGE_WRITE, // flags
                null); // encoding

        // Store the person names. Added by Shervin.
        cvWriteInt(
                fileStorage, // fs
                "nPersons", // name
                nPersons); // value

        for (i = 0; i < nPersons; i++) {
            String varname = "personName_" + (i + 1);
            cvWriteString(
                    fileStorage, // fs
                    varname, // name
                    personNames.get(i), // string
                    0); // quote
        }

        // store all the data
        cvWriteInt(
                fileStorage, // fs
                "nEigens", // name
                nEigens); // value

        cvWriteInt(
                fileStorage, // fs
                "nTrainFaces", // name
                nTrainFaces); // value

        cvWrite(
                fileStorage, // fs
                "trainPersonNumMat", // name
                personNumTruthMat); // value

        cvWrite(
                fileStorage, // fs
                "eigenValMat", // name
                eigenValMat); // value

        cvWrite(
                fileStorage, // fs
                "projectedTrainFaceMat", // name
                projectedTrainFaceMat);

        cvWrite(fileStorage, // fs
                "avgTrainImg", // name
                pAvgTrainImg); // value

        for (i = 0; i < nEigens; i++) {
            String varname = "eigenVect_" + i;
            cvWrite(
                    fileStorage, // fs
                    varname, // name
                    eigenVectArr[i]); // value
        }
        cvReleaseFileStorage(fileStorage);
    }

    CvMat loadTrainingData()
    {
        CvMat pTrainPersonNumMat = null; // the person numbers during training
        CvFileStorage fileStorage;
        int i;

        // create a file-storage interface
        fileStorage = cvOpenFileStorage(
                "data/facedata.xml", // filename
                null, // memstorage
                CV_STORAGE_READ, // flags
                null); // encoding


        // Load the person names.
        personNames.clear();        // Make sure it starts as empty.
        nPersons = cvReadIntByName(
                fileStorage, // fs
                null, // map
                "nPersons", // name
                0); // default_value

        // Load each person's name.
        for (i = 0; i < nPersons; i++) {
            String sPersonName;
            String varname = "personName_" + (i + 1);
            sPersonName = cvReadStringByName(
                    fileStorage, // fs
                    null, // map
                    varname,
                    "");
            personNames.add(sPersonName);
        }

        // Load the data
        nEigens = cvReadIntByName(
                fileStorage, // fs
                null, // map
                "nEigens",
                0); // default_value
        nTrainFaces = cvReadIntByName(
                fileStorage,
                null, // map
                "nTrainFaces",
                0); // default_value
        Pointer pointer = cvReadByName(
                fileStorage, // fs
                null, // map
                "trainPersonNumMat"); // name
        pTrainPersonNumMat = new CvMat(pointer);

        pointer = cvReadByName(
                fileStorage, // fs
                null, // map
                "eigenValMat"); // name
        eigenValMat = new CvMat(pointer);

        pointer = cvReadByName(
                fileStorage, // fs
                null, // map
                "projectedTrainFaceMat"); // name
        projectedTrainFaceMat = new CvMat(pointer);

        pointer = cvReadByName(
                fileStorage,
                null, // map
                "avgTrainImg");
        pAvgTrainImg = new IplImage(pointer);
        eigenVectArr = new IplImage[nEigens];
        for (i = 0; i < nEigens; i++)
        {
            String varname = "eigenVect_" + i;
            pointer = cvReadByName(
                    fileStorage,
                    null, // map
                    varname);
            eigenVectArr[i] = new IplImage(pointer);
        }

        // release the file-storage interface
        cvReleaseFileStorage(fileStorage);
        return pTrainPersonNumMat;

    }
    private int findNearestNeighbor(float projectedTestFace[], FloatPointer pConfidencePointer) {
        double leastDistSq = Double.MAX_VALUE;
        int i = 0;
        int iTrain = 0;
        int iNearest = 0;

        System.out.println("................");
        System.out.println("find nearest neighbor from " + nTrainFaces + " training faces");
        for (iTrain = 0; iTrain < nTrainFaces; iTrain++) {
            //LOGGER.info("considering training face " + (iTrain + 1));
            double distSq = 0;

            for (i = 0; i < nEigens; i++) {
                //LOGGER.debug("  projected test face distance from eigenface " + (i + 1) + " is " + projectedTestFace[i]);

                float projectedTrainFaceDistance = (float) projectedTrainFaceMat.get(iTrain, i);
                float d_i = projectedTestFace[i] - projectedTrainFaceDistance;
                distSq += d_i * d_i; // / eigenValMat.data_fl().get(i);  // Mahalanobis distance (might give better results than Eucalidean distance)
            }

            if (distSq < leastDistSq) {
                leastDistSq = distSq;
                iNearest = iTrain;
                System.out.println("  training face " + (iTrain + 1) + " is the new best match, least squared distance: " + leastDistSq);
            }
        }

        // Return the confidence level based on the Euclidean distance,
        // so that similar images should give a confidence between 0.5 to 1.0,
        // and very different images should give a confidence between 0.0 to 0.5.
        float pConfidence = (float) (1.0f - Math.sqrt(leastDistSq / (float) (nTrainFaces * nEigens)) / 255.0f);
        pConfidencePointer.put(pConfidence);

        System.out.println("training face " + (iNearest + 1) + " is the final best match, confidence " + pConfidence);
        return iNearest;
    }

    private IplImage[] loadFaceImgArray(String filename) {
        IplImage[] faceImgArr;
        BufferedReader imgListFile;
        String imgFilename;
        int iFace = 0;
        int nFaces = 0;
        int i;

        try {
            imgListFile = new BufferedReader(new FileReader(filename));

            while (true) {
                final String line = imgListFile.readLine();
                if (line == null || line.isEmpty())
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
            //initialize for debugging
            for (int j1 = 0; j1 < nFaces; j1++) {
                personNumTruthMat.put(0, j1, 0);
            }
            personNames.clear();
            nPersons = 0;
            for (iFace = 0; iFace < nFaces; iFace++) {
                String personName;
                String sPersonName;
                int personNumber;

                //reading person number
                final String line = imgListFile.readLine();
                if (line.isEmpty()) {
                    break;
                }
                final String[] tokens = line.split(" ");
                personNumber = Integer.parseInt(tokens[0]);
                personName = tokens[1];
                imgFilename = tokens[2];
                sPersonName = personName;
                System.out.println("Got: " + iFace + " " + personNumber + " " + personName + " " + imgFilename);
                //check if a new person is beaing loaded
                if (personNumber > nPersons) {
                    //allocate memory
                    personNames.add(sPersonName);
                    nPersons = personNumber;
                    System.out.println("Added new person" + sPersonName + " ->nPersons = " + nPersons + " [" + personNames.size() + "] ");
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

                if (faceImgArr[iFace] == null) {
                    throw new RuntimeException("Can't load image from " + imgFilename);
                }
            }

            imgListFile.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("Data loaded from" + filename + "': (" + nFaces + " images of " + nPersons + " people).");

        return faceImgArr;
    }

    public static void main(String[] args) {
        FaceRecognition nowy = new FaceRecognition();
        if (args.length != 0) {
            nowy.learn();
        }


    }

    @Override
    public void update(IplImage frame, List<Integer[]> coords) {
        System.out.println("Przekazuje do rozpoznania");
        IplImage[] faces = new IplImage[coords.size()];
        IplImage tmp;
        int j = 0;
        for(Integer[] i: coords) {
            cvSetImageROI(frame, cvRect(i[0], i[1], i[2], i[3]));
            tmp = cvCreateImage(cvGetSize(frame), frame.depth(), frame.nChannels());
            cvCopy(frame, tmp, null);
            faces[j] = tmp;
            tmp = cvCreateImage(new CvSize(100, 100), frame.depth(), frame.nChannels());
            cvResize(faces[j], tmp);
            faces[j] = tmp;
            tmp = IplImage.create(100, 100, IPL_DEPTH_8U, 1);
            cvCvtColor(faces[j], tmp, CV_BGR2GRAY);
            faces[j] = tmp;
            cvResetImageROI(frame);
            cvSaveImage("next"+j+".png", tmp);
            recognize(faces[j]);
            j++;
        }
    }
}