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
 * Class to parse the Netflix data to make it suitable for Neural Networks
 * 
 * @author Anastasija Mensikova
 * @version 2.0 Oct. 2018
 * @see BufferedReader
 * @see PrintWriter
 */

public class NetflixParser {

  private static String initialFolder = "../../datasets/netflix/";
  private String outputFileTrain = "../../datasets/netflix/netflix-train-new";
  private String outputFileTest = "../../datasets/netflix/netflix-test-new";
  private PrintWriter outputStream;

  /**
   * Constructor to intialise the output stream
   * 
   * @throws IOException
   */
  public NetflixParser() throws IOException {
    Path outputFileName = Paths.get(outputFileTrain);
    Charset encoding = Charset.forName("UTF-8");
    outputStream = new PrintWriter(
        Files.newBufferedWriter(outputFileName, encoding));
  }

  /**
   * Method to parse Netflix data and put it in respectable Neural Network
   * folders for further analysis
   * 
   * @throws IOException
   */
  public void parse() throws IOException {
    File input1 = new File(initialFolder + "train/neg");
    for (File file1 : input1.listFiles()) {
      String negativeText = FileUtils.readFileToString(file1);
      outputStream.write("negative " + negativeText + "\n");
    }
    File input2 = new File(initialFolder + "train/pos");
    for (File file2 : input2.listFiles()) {
      String positiveText = FileUtils.readFileToString(file2);
      outputStream.write("positive " + positiveText + "\n");
    }
    outputStream.close();
  }

  public static void main(String[] args) throws IOException {
    NetflixParser parser = new NetflixParser();
    parser.parse();
  }

}
