package project;

import ij.ImagePlus;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.ArrayList;

import static ij.IJ.openImage;

/**
 * Created by troyr on 15-Aug-15.
 */
public class FaceDetection {

    private String csv = ",";
    private int currentClass = 1;
    private static BufferedImage image;
    private static WritableRaster r;
    private int width = 19;
    private int height = 19;
    private String results;
    private String matrix;
    private ThresholdCurve tc;
    private Instances resultTC;


    public FaceDetection() {


        // Create tabular data sets for testing and training
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // Write feature headers to csv file.

        // Read in a picture: DO

        // Design 8+ features to read in.
        // Methods to extract each feature.
        // Write to test/train csv files

        // REPEAT

        // Train a simple Bayesian classifier
        // Test classifier on both training and test data

        // Choose appropriate evaluation criteria

        // Produce and display ROC curve


        // UI Requirements:

        // Tick boxes for what features to use
        // Display ROC curve button.
        // ROC curve box - RHS
        // Results - LHS





    }

    public void trainBayesClassifier() {

        ConverterUtils.DataSource testData = null;
        ConverterUtils.DataSource trainingData = null;
        String[] options = new String[1];
        Classifier cModel;

        try {
            testData = new ConverterUtils.DataSource("output/faces/testData.csv");
            trainingData = new ConverterUtils.DataSource("output/faces/trainingData.csv");
            Instances test = testData.getDataSet();
            Instances train = trainingData.getDataSet();
            test.setClassIndex(0);
            train.setClassIndex(0);

            NumericToNominal convert = new NumericToNominal();
            String[] fOptions = new String[2];
            fOptions[0] = "-R";
            fOptions[1] = "first";  //range of variables to change

            convert.setOptions(fOptions);
            convert.setInputFormat(train);

            train = Filter.useFilter(train, convert);
            test = Filter.useFilter(test, convert);

            cModel = (Classifier)new NaiveBayes();
            cModel.buildClassifier(train);

            // evaluate classifier and print some statistics
            Evaluation eval = new Evaluation(train);
            eval.evaluateModel(cModel, train);
            results = eval.toSummaryString("\nTRAIN Results\n======\n", false);
            eval.evaluateModel(cModel, test);
            results += eval.toSummaryString("\nTEST Results\n======\n", false);
            matrix = eval.toMatrixString("Confusion Matrix");

            tc = new ThresholdCurve();
            int classIndex = 0;
            resultTC = tc.getCurve(eval.predictions(), classIndex);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public ThresholdCurve getROC() {
        return tc;
    }

    public Instances getROCResults() {
        return resultTC;
    }

    public String getResults() {
        return results;
    }

    public String getMatrix() {
        return matrix;
    }

    public void loadData(int[] featuresToUse) {
        PrintWriter testWriter;
        PrintWriter trainingWriter;
        File testData = new File("output/faces/testData.csv");
        File trainingData = new File("output/faces/trainingData.csv");

        ArrayList<File> faceFiles;

        try {
            testWriter = new PrintWriter(testData);
            trainingWriter = new PrintWriter(trainingData);

            // Create data table
            StringBuilder tempLine = new StringBuilder();
            String currentLine = "";
            String tempFeature;
            String lineSplit[];
            int index = 1;

            currentLine = getAttributeLine(featuresToUse);
            currentLine = currentLine.substring(0, currentLine.length()-1);

            testWriter.append(currentLine);
            trainingWriter.append(currentLine);
            testWriter.append("\n");
            trainingWriter.append("\n");

            // train faces
            faceFiles = listFilesForFolder(new File("assets/face/train/face"));
            readFiles(featuresToUse, trainingWriter, faceFiles, 1);
            // train non faces
            faceFiles = listFilesForFolder(new File("assets/face/train/non-face"));
            readFiles(featuresToUse, trainingWriter, faceFiles, 0);

            // test faces
            faceFiles = listFilesForFolder(new File("assets/face/test/face"));
            readFiles(featuresToUse, testWriter, faceFiles, 1);

            // test non faces
            faceFiles = listFilesForFolder(new File("assets/face/test/non-face"));
            readFiles(featuresToUse, testWriter, faceFiles, 0);

            testWriter.close();
            trainingWriter.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFiles(int[] featuresToUse, PrintWriter writer, ArrayList<File> faceFiles, int classID) {
        for (int i = 0; i < faceFiles.size(); i++) {
            //image = ImageIO.read(testFaces.get(i));

            ImagePlus ip =  openImage(faceFiles.get(i).getPath());
            image = ip.getBufferedImage();

            width = image.getWidth();
            height = image.getHeight();
            r = image.getRaster();

            // Class
            writer.append(classID + csv);
            // Features
            writer.append(getFeatures(featuresToUse));
            // End line
            writer.append("\n");
        }
    }

    private String getAttributeLine(int[] attributes) {

        StringBuilder sb = new StringBuilder();

        sb.append("ClassID" + csv);

        for (int i = 0; i < 8; i++) {
            if ( attributes[i] == 1)
                sb.append("Feature" + (i +1) + csv);
        }
        //sb.append(attributesNames);

        return sb.toString();
    }

    private String getFeatures(int[] featuresToUse) {
        StringBuilder sb =  new StringBuilder();

        if (featuresToUse[0] == 1)
            sb.append(globalMean() + csv);
        if (featuresToUse[1] == 1)
            sb.append(globalStD() + csv);
        if (featuresToUse[2] == 1)
            sb.append(noseMean() + csv);
        if (featuresToUse[3] == 1)
            sb.append(noseStD() + csv);
        if (featuresToUse[4] == 1)
            sb.append(eyesMean() + csv);
        if (featuresToUse[5] == 1)
            sb.append(eyesStD() + csv);
        if (featuresToUse[6] == 1)
            sb.append(globalMoment() + csv);
        if (featuresToUse[7] == 1)
            sb.append(noseMoment() + csv);

        return sb.toString().substring(0, sb.length() - 1);
    }

    private int globalMoment() {

        int x = 0;
        int y = 0;
        int w = width;
        int h = height;

        return getMoment(x, y, w, h);
    }

    private int globalMean() {

        int x = 0;
        int y = 0;
        int w = width;
        int h = height;

        return getMean(x, y, w, h);
    }

    private int globalStD() {

        int x = 0;
        int y = 0;
        int w = width;
        int h = height;

        return getStD(x, y, w, h);
    }

    private int noseMean() {

        int x = 7;
        int y = 0;
        int w = 6;
        int h = 13;

        return getMean(x, y, w, h);
    }

    private int noseStD() {

        int x = 7;
        int y = 0;
        int w = 6;
        int h = 13;

        return getStD(x, y, w, h);
    }

    private int noseMoment() {

        int x = 7;
        int y = 0;
        int w = 6;
        int h = 13;

        return getMoment(x, y, w, h);
    }

    private int eyesMean() {

        int x = 0;
        int y = 0;
        int w = width;
        int h = 8;

        return getMean(x, y, w, h);
    }

    private int eyesStD() {

        int x = 0;
        int y = 0;
        int w = width;
        int h = 8;

        return getStD(x, y, w, h);
    }

    private int getMean(int x, int y, int w, int h) {
        int[] pixels = new int[w*h];
        r.getPixels(x, y, w, h, pixels);

        int total = 0;

        for (int i : pixels) {
            total += i;
        }

        return (total / pixels.length);
    }

    private int getMoment(int x, int y, int w, int h) {
        int[] pixels = new int[w*h];
        r.getPixels(x, y, w, h, pixels);

        int total = 0;
        int index = 0;


        for (int i : pixels) {
            total += i * index;
            index++;
        }

        return (total / pixels.length);
    }

    private int getStD(int x, int y, int w, int h) {
        int std = 0;
        int mean = noseMean();

        int[] pixels = new int[w*h];
        r.getPixels(x, y, w, h, pixels);

        int total = 0;

        for (int i = 0; i < pixels.length; i++) {
            pixels[i] += Math.pow(pixels[i] - mean, 2);
            total += pixels[i];
        }

        std = (int) Math.sqrt(total/ pixels.length);

        return std;
    }


    private ArrayList<File> listFilesForFolder(final File folder) {

        ArrayList<File> files = new ArrayList<File>();

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                continue;
            } else {
                files.add(fileEntry);
            }
        }

        return files;
    }

}
