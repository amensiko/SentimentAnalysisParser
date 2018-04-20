/*
 * Trinity College Senior Project 2018
 */

package edu.trincoll;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Class to parse the dataset of YouTube video IDs and their labels.
 * 
 * @author Anastasija Mensikova
 * @version 1.0 Nov. 2017
 * @see BufferedReader
 * @see PrintWriter
 */
public class ComedyParser {

  private static String inputFileTrain = "../../datasets/comedy_comparisons/comedy_comparisons.train";
  private static String inputFileTest = "../../datasets/comedy_comparisons/comedy_comparisons.test";
  private static String trainLabeledIds = "../sentiment-examples/src/main/resources/train/comedy-train";
  private static String testLabeledIds = "../sentiment-examples/src/main/resources/test/comedy-test";
  private static String inputComedyTrain = "../../youtube/Archive/comedy_train_total";
  private static String inputComedyTest = "../../youtube/Archive/comedy_test_total";
  private static String outputFile;
  private PrintWriter outputStream;

  /**
   * Constructor to intialise the output stream
   * 
   * @param out
   *          name of the output file
   * @throws IOException
   */
  public ComedyParser(String out) throws IOException {
    this.outputFile = out;
    Path outputFileName = Paths.get(outputFile);
    Charset encoding = Charset.forName("UTF-8");
    outputStream = new PrintWriter(
        Files.newBufferedWriter(outputFileName, encoding));
  }

  /**
   * Default constructor
   */
  public ComedyParser() {
    String outFolder = "";
  }

  /**
   * Method to create a labeled file to use for MaxEntropy model training
   * 
   * @throws IOException
   */
  public void createTrainFile() throws IOException {
    Path inputFile = Paths.get(inputFileTrain);
    BufferedReader reader = Files.newBufferedReader(inputFile,
        Charset.forName("UTF-8"));
    Map<String, String> idMap = new HashMap<String, String>();
    String line;
    int i = 0;
    while ((line = reader.readLine()) != null) {
      String[] delims = line.split(",");
      String id1 = delims[0];
      String id2 = delims[1];
      String label = delims[2];
      if (!idMap.containsKey(id1)) {
        if (label.equals("left")) {
          idMap.put(id1, "funny");
        } else if (label.equals("right")) {
          idMap.put(id1, "not_funny");
        }
      }
      if (!idMap.containsKey(id2)) {
        if (label.equals("left")) {
          idMap.put(id2, "not_funny");
        } else if (label.equals("right")) {
          idMap.put(id2, "funny");
        }
      }
      String name = Integer.toString(i);
      i++;
    }
    for (Map.Entry<String, String> entry : idMap.entrySet()) {
      String videoId = entry.getKey();
      String label = entry.getValue();
      outputStream.write(label + " " + videoId + "\n");
    }
    outputStream.close();
  }

  /**
   * Method to put all the training information together
   * 
   * @throws IOException
   */
  public void buildTrainFile() throws IOException {
    Path inputFileName = Paths.get(inputComedyTest);
    Path labelFileName = Paths.get(testLabeledIds);
    BufferedReader readerUnlabeled = Files.newBufferedReader(inputFileName,
        Charset.forName("UTF-8"));
    BufferedReader readerLabeled = Files.newBufferedReader(labelFileName,
        Charset.forName("UTF-8"));
    Map<String, String> labelMap = new HashMap<String, String>();
    String labeled;
    int j = 0;
    while ((labeled = readerLabeled.readLine()) != null) {
      String[] element1 = labeled.split(" ");
      labelMap.put(element1[1], element1[0]);
      j++;
    }

    String line;
    int i = 0;
    String info = "";
    while ((line = readerUnlabeled.readLine()) != null) {
      String[] element = line.split(";");
      outputStream.write(labelMap.get(element[0]) + " ");
      for (int k = 1; k < element.length; k++) {
        outputStream.write(element[k] + "; ");
      }
      outputStream.write("\n");
    }
    outputStream.close();
  }

  public static void main(String[] args) throws IOException {
    ComedyParser parser = new ComedyParser(
        "../sentiment-examples/src/main/resources/train/comedy_test");
    parser.buildTrainFile();
  }

}
