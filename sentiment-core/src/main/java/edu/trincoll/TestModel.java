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

import org.apache.commons.io.FileUtils;

/**
 * A class to help divide the given training dataset into train and test data
 * (80:20)
 * 
 * @author Anastasija Mensikova
 * @version 2.0 Nov. 2017
 * @see BufferedReader
 * @see PrintWriter
 */
public class TestModel {

  private static String inputFile;
  private static String outputFile;

  private static final String labels = "../sentiment-examples/src/main/resources/hybrid/hybrid-v3-test20-labels";
  private static final int num = 4449;

  private double trueRel;
  private double trueNotRel;
  private double falseRel;
  private double falseNotRel;

  /**
   * Constructor to instantiate input and output file names
   * 
   * @param input
   *          input file name
   * @param output
   *          output file name
   */
  public TestModel(String input, String output) {
    this.inputFile = input;
    this.outputFile = output;
  }

  /**
   * Constructor to instantiate input file name
   * 
   * @param input
   *          input file name
   */
  public TestModel(String input) {
    this.inputFile = input;
  }

  /**
   * Method to save the given labels
   * 
   * @throws IOException
   */
  public void saveLabels() throws IOException {
    Path inputFileName = Paths.get(inputFile);
    Path outputFileName = Paths.get(outputFile);

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
   * Method to remove labels
   * 
   * @throws IOException
   */
  public void removeLabels() throws IOException {
    Path inputFileName = Paths.get(inputFile);
    Path outputFileName = Paths.get(outputFile);

    BufferedReader reader = Files.newBufferedReader(inputFileName,
        Charset.forName("UTF-8"));

    String line;
    int i = 0;
    while ((line = reader.readLine()) != null) {
      String content = line.substring(line.indexOf(" ") + 1);
      String name = Integer.toString(i);
      Path output = Paths.get(outputFile, "/" + name + ".sent");
      PrintWriter fileWriter = new PrintWriter(
          Files.newBufferedWriter(output, Charset.forName("UTF-8")));
      fileWriter.write(content);
      fileWriter.close();
      i++;

    }
  }

  /**
   * Method to compare two different sets of labels
   * 
   * @throws IOException
   */
  public void compareLabels() throws IOException {
    File input1 = new File(inputFile);
    File input2 = new File(labels);
    for (File file1 : input1.listFiles()) {
      String id = file1.getName();
      if (id.equals(".DS_Store") || id.equals("..out") || id.equals(".out"))
        continue;
      id = id.replace(".out", "");
      int idint = Integer.valueOf(id);
      String out1 = FileUtils.readFileToString(file1);
      File file2 = new File(labels + "/" + idint);
      String out2 = FileUtils.readFileToString(file2);
      if (out1.indexOf("NOT_FUNNY") > -1 && out2.indexOf("NOT_FUNNY") > -1) {
        trueNotRel++;
      } else if (out1.indexOf("NOT_FUNNY") <= -1
          && out2.indexOf("NOT_FUNNY") <= -1) {
        trueRel++;
      } else if (out1.indexOf("NOT_FUNNY") > -1 // not funny output
          && out2.indexOf("NOT_FUNNY") <= -1) { // funny actually
        falseNotRel++;
      } else if (out1.indexOf("NOT_FUNNY") <= -1
          && out2.indexOf("NOT_FUNNY") > -1) {
        falseRel++;
      }
    }

  }

  /**
   * Method to return the true relevant rate
   * 
   * @return true relevant
   */
  public double getTrueRel() {
    return trueRel;
  }

  /**
   * Method to change the true relevant rate
   * 
   * @param trueRel
   *          the new true relevant
   */
  public void setTrueRel(int trueRel) {
    this.trueRel = trueRel;
  }

  /**
   * Method to return the false relevant rate
   * 
   * @return false relevant
   */
  public double getFalseRel() {
    return falseRel;
  }

  /**
   * Method to change the true relevant rate
   * 
   * @param falseRel
   *          the new false relevant
   */
  public void setFalseRel(int falseRel) {
    this.falseRel = falseRel;
  }

  /*
   * ../sentiment-examples/src/main/resources/hybrid/hybrid-test20-out
   */
  public static void main(String[] args) throws IOException {

    String fileName = args[0];
    String outputName = args[1];

    TestModel test = new TestModel(fileName, outputName);
    test.saveLabels();
  }

}
