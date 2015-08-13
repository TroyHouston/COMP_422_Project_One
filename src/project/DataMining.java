package project;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.regex.Pattern;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

/**
 * Created by troyr on 8/7/2015.
 */
public class DataMining {

    // Input files (number of classes/number of things in a class)
    // Merge, add class labels, and return tabular data.

    // Display this in the UI!

    // Train a C4.5 Decision Tree..

    // Display in UI

    // UI
    //
    // Could do this as Buttons. Or Tabs?
    // ________________________________
    // | Tabular Data | Decision Tree |
    // ________________________________
    //                |
    // Left hand menu
    // Choose which files to use.
    //
    // Below? - Results - Confusion Matrix - Percentages.
    //
    // Needs to support Comparisons.
    //
    // Try a setting for using cross validation.

    /**
     * Probably can replace this with a temp stringBuilder now.
     */

    private J48 tree;
    public String results;

    public DataMining() {



    }

    public String getGraph() {
        try {
            return tree.graph();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void trainJ48Classifier() {

        csvToArff("output\\trainingData.csv", "output\\trainingData.arff");
        csvToArff("output\\testData.csv", "output\\testData.arff");

        DataSource testData = null;
        DataSource trainingData = null;
        String[] options = new String[1];
        // Unpruned tree
        options[0] = "-U";
        try {
            testData = new DataSource("output\\testData.arff");
            trainingData = new DataSource("output\\trainingData.arff");
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

            tree = new J48();         // new instance of tree
            tree.setOptions(options);     // set the options
            tree.buildClassifier(train);   // build classifier

            // evaluate classifier and print some statistics
            Evaluation eval = new Evaluation(train);
            eval.evaluateModel(tree, test);
            results = eval.toSummaryString("\nResults\n======\n", false);
            System.out.println(eval.toSummaryString("\nResults\n======\n", false));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void csvToArff(String csv, String arff) {
        // load CSV
        CSVLoader loader = new CSVLoader();
        ArffSaver saver = new ArffSaver();
        try {
            loader.setSource(new File(csv));
            Instances data = null;
            data = loader.getDataSet();


            // save ARFF
            saver.setInstances(data);
            saver.setFile(new File(arff));
            saver.writeBatch();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Load all the data into and internal data array.
     * Write all data to an external file.
     *
     * @param filesToLoad - Which files of the six should be loaded. 0 = No, 1 = Yes.
     */
    public void loadData(int filesToLoad[]) {

        ArrayList<BufferedReader> readers = new ArrayList<BufferedReader>();
        PrintWriter allWriter;
        PrintWriter testWriter;
        PrintWriter trainingWriter;

        try {
            createReader(filesToLoad[0], "fac", readers);
            createReader(filesToLoad[1], "fou", readers);
            createReader(filesToLoad[2], "kar", readers);
            createReader(filesToLoad[3], "mor", readers);
            createReader(filesToLoad[4], "pix", readers);
            createReader(filesToLoad[5], "zer", readers);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Create blank file
        File allData = new File("output\\allData.csv");
        File testData = new File("output\\testData.csv");
        File trainingData = new File("output\\trainingData.csv");
        try {
            allWriter = new PrintWriter(allData);
            testWriter = new PrintWriter(testData);
            trainingWriter = new PrintWriter(trainingData);

            // Create data table
            StringBuilder tempLine = new StringBuilder();
            String currentLine = "";
            String lineSplit[];
            String csv = ",";
            int index = 1;

            currentLine = getAttributeLine(filesToLoad);

            allWriter.append(currentLine);
            testWriter.append(currentLine);
            trainingWriter.append(currentLine);
            allWriter.append("\n");
            testWriter.append("\n");
            trainingWriter.append("\n");

            for (int i = 0; i < 2000; i++) {
                // Get current class
                String currentClass = String.valueOf(i / 200);
                tempLine = new StringBuilder();
                tempLine.append(currentClass);
                tempLine.append(csv);
                index = 0;

                // Add data to internal data table
                for (BufferedReader r : readers) {
                    index++;
                    currentLine = readers.get(index - 1).readLine();
                    lineSplit = currentLine.trim().split("\\s+");
                    for (String n: lineSplit) {
                        tempLine.append(n);
                        tempLine.append(csv);
                    }
                }
                currentLine = tempLine.toString();
                currentLine = currentLine.substring(0, currentLine.length()-1);

                // Write to external tabular data set

                allWriter.append(currentLine);
                if (i % 2 == 0)
                    testWriter.append(currentLine);
                else if (i % 2 == 1)
                    trainingWriter.append(currentLine);

                allWriter.append("\n");
                if (i % 2 == 0)
                    testWriter.append("\n");
                else if (i % 2 == 1)
                    trainingWriter.append("\n");
            }


            // Close readers.
            for (BufferedReader br : readers) {
                br.close();
            }
            allWriter.close();
            testWriter.close();
            trainingWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File Not Found: Failed to create data file.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Exception: Failed to read & write.");
        }


    }

    /**
     * Create the first line of the respective csv data files.
     * i.e Containing the headings.
     *
     * @param files - Which datasets are being used.
     * @return
     */
    private String getAttributeLine(int[] files) {
        String csv = ",";
        StringBuilder line = new StringBuilder();
        String finalLine;

        line.append("Class");
        line.append(csv);

        if (files[0] == 1) {
            for (int i = 1; i <= 216; i++) {
                line.append("fac" + i);
                line.append(csv);
            }
        }

        if (files[1] == 1) {
            for (int i = 1; i <= 76; i++) {
                line.append("fou" + i);
                line.append(csv);
            }
        }

        if (files[2] == 1) {
            for (int i = 1; i <= 64; i++) {
                line.append("kar" + i);
                line.append(csv);
            }
        }

        if (files[3] == 1) {
            for (int i = 1; i <= 6; i++) {
                line.append("mor" + i);
                line.append(csv);
            }
        }

        if (files[4] == 1) {
            for (int i = 1; i <= 240; i++) {
                line.append("pix" + i);
                line.append(csv);
            }
        }

        if (files[5] == 1) {
            for (int i = 1; i <= 47; i++) {
                line.append("zer" + i);
                line.append(csv);
            }
        }
        finalLine = line.toString();
        finalLine = finalLine.substring(0, finalLine.length()-1);
        return  finalLine;
    }

    /**
     * If this file should be loaded. Create buffered reader and add to global array of file readers.
     */
    private void createReader(int bool, String file, ArrayList<BufferedReader> readers) throws FileNotFoundException {
        if (bool == 1)
            readers.add(new BufferedReader(new FileReader("assets\\mfeat-digits\\mfeat-" + file)));
    }



}
