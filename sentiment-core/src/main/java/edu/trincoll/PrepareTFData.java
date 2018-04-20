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
 * Class to prepare training data fitting in the Tensorlow/Neural networks
 * format
 * 
 * @author Anastasija Mensikova
 * @version 1.0 Feb. 2018
 * @see BufferedReader
 * @see PrintWriter
 */

public class PrepareTFData {

  private static String netflixInputTrain = "../sentiment-examples/src/main/resources/train/sentiment/new-sentiment-train";
  private static String outputFileNamePos = "../../datasets/twitter-train-pos";
  private static String outputFileNameNeg = "../../datasets/twitter-train-neg";

  /**
   * Default constructor
   */
  public PrepareTFData() {
  }

  /**
   * Method to prepare Netflix data for Deep Learning
   * 
   * @throws IOException
   */
  public void parseNetflixTrain() throws IOException {
    Path inputFileName = Paths.get(netflixInputTrain);
    Path outputFilePos = Paths.get(outputFileNamePos);
    Path outputFileNeg = Paths.get(outputFileNameNeg);
    BufferedReader reader = Files.newBufferedReader(inputFileName,
        Charset.forName("UTF-8"));
    String line;
    int i = 0;
    PrintWriter fileWriterPos = new PrintWriter(
        Files.newBufferedWriter(outputFilePos, Charset.forName("UTF-8")));
    PrintWriter fileWriterNeg = new PrintWriter(
        Files.newBufferedWriter(outputFileNeg, Charset.forName("UTF-8")));
    while ((line = reader.readLine()) != null) {
      String[] delims = line.split(" ", 2);
      String label = delims[0];
      if (label.equals("positive")) {
        fileWriterPos.write(delims[1] + "\n");
      } else if (label.equals("negative")) {
        fileWriterNeg.write(delims[1] + "\n");
      }
      i++;
    }
    fileWriterPos.close();
    fileWriterNeg.close();
  }

  /**
   * Method to prepare Comedy data for Deep Learning
   * 
   * @throws IOException
   */
  public void parseComedyTrain() throws IOException {
    Path inputFileName = Paths
        .get("../sentiment-examples/src/main/resources/train/comedy_train");
    Path outputFilePos = Paths.get("../../datasets/comedy-train-fun");
    Path outputFileNeg = Paths.get("../../datasets/comedy-train-notfun");
    BufferedReader reader = Files.newBufferedReader(inputFileName,
        Charset.forName("UTF-8"));
    String line;
    int i = 0;
    PrintWriter fileWriterPos = new PrintWriter(
        Files.newBufferedWriter(outputFilePos, Charset.forName("UTF-8")));
    PrintWriter fileWriterNeg = new PrintWriter(
        Files.newBufferedWriter(outputFileNeg, Charset.forName("UTF-8")));
    while ((line = reader.readLine()) != null) {
      String[] delims = line.split(" ", 2);
      String label = delims[0];
      if (label.equals("funny")) {
        fileWriterPos.write(delims[1] + "\n");
      } else if (label.equals("not_funny")) {
        fileWriterNeg.write(delims[1] + "\n");
      }
      i++;
    }
    fileWriterPos.close();
    fileWriterNeg.close();
  }

  public static void main(String[] args) throws IOException {
    PrepareTFData run = new PrepareTFData();
    // run.parseNetflixTrain();
    // run.parseComedyTrain();
  }

}
