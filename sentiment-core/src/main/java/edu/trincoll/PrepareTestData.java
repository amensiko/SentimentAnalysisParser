/*
 * Trinity College Senior Project 2018
 */

package edu.trincoll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class to prepare respective data for testing
 * 
 * @author Anastasija Mensikova
 * @version 2.0 Nov. 2017
 * @see BufferedReader
 * @see PrintWriter
 */

public class PrepareTestData {

  private static String inputFile = "../../datasets/new_sentiment/new-sentiment-test";
  private static String outputFolder = "../sentiment-examples/src/main/resources/test/new-sentiment-test";
  private static String outputFile = "../../datasets/new_sentiment/new-sentiment-test-unlabeled";
  private static final String labels = "../../datasets/new_sentiment/new-sent-test-labels";

  private static final String comedyTestLabels = "../sentiment-examples/src/main/resources/test/comedy_test_final_labels";
  private static final String comedyInput = "../sentiment-examples/src/main/resources/test/comedy_test_total";
  private static String comedyOutputFolder = "../sentiment-examples/src/main/resources/test/comedy-videos-test";

  private static final String netflixTestLabels = "../sentiment-examples/src/main/resources/test/netflix-test-new-labels";
  private static final String netflixInput = "../sentiment-examples/src/main/resources/test/netflix-test-new";
  private static String outputFolderNetflix = "../sentiment-examples/src/main/resources/test/new-netflix-test";

  /**
   * Default constructor
   */
  public PrepareTestData() {
  }

  /**
   * Method to save given labels
   * 
   * @throws IOException
   */
  public void saveLabels() throws IOException {
    Path inputFileName = Paths.get(netflixInput);
    Path outputFileName = Paths.get(netflixTestLabels);
    BufferedReader reader = Files.newBufferedReader(inputFileName,
        Charset.forName("UTF-8"));
    String line;
    int i = 0;
    PrintWriter fileWriter = new PrintWriter(
        Files.newBufferedWriter(outputFileName, Charset.forName("UTF-8")));
    while ((line = reader.readLine()) != null) {
      String[] delims = line.split(" ");
      String label = delims[0];
      String name = Integer.toString(i);
      fileWriter.write(label + "\n");
      i++;
    }
    fileWriter.close();
  }

  /**
   * Method to remove the labels from the data
   * 
   * @throws IOException
   */
  public void removeLabels() throws IOException {
    Path inputFileName = Paths.get(netflixInput);

    BufferedReader reader = Files.newBufferedReader(inputFileName,
        Charset.forName("UTF-8"));

    String line;
    int i = 0;
    while ((line = reader.readLine()) != null) {
      String[] delims = line.split(" ");
      String label = delims[0];
      String name = Integer.toString(i);
      Path output = Paths.get(outputFolderNetflix + "/" + name + ".sent");
      PrintWriter fileWriter = new PrintWriter(
          Files.newBufferedWriter(output, Charset.forName("UTF-8")));
      fileWriter.write(label);
      fileWriter.close();
      i++;
    }
    reader.close();
  }

  public static void main(String[] args) throws IOException {
    PrepareTestData prepare = new PrepareTestData();
    prepare.saveLabels();
  }

}
