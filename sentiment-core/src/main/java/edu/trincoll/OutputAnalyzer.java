/*
 * Trinity College Senior Project 2018
 */

package edu.trincoll;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;

/**
 * Class to analyse all the outputs of MaxEntropy models
 * 
 * @author Anastasija Mensikova
 * @version 2.0 Nov. 2018
 * @see BufferedReader
 * @see PrintWriter
 */

public class OutputAnalyzer {

  private static int positiveFunny = 0;
  private static int positiveNotFunny = 0;
  private static int negativeFunny = 0;
  private static int negativeNotFunny = 0;

  public static int fun = 0;
  public static int not_fun = 0;

  public static int angryFunny = 0;
  public static int sadFunny = 0;
  public static int neutralFunny = 0;
  public static int likeFunny = 0;
  public static int loveFunny = 0;
  public static int angryNotFunny = 0;
  public static int sadNotFunny = 0;
  public static int neutralNotFunny = 0;
  public static int likeNotFunny = 0;
  public static int loveNotFunny = 0;

  /**
   * Default constructor
   */
  public OutputAnalyzer() {
  }

  /**
   * Method to parse Comedy+Binary Sentiment output files and analyse them
   * 
   * @param pathName1
   *          first file
   * @param pathName2
   *          second file
   * @throws IOException
   */
  public void parse(String pathName1, String pathName2) throws IOException {
    File input1 = new File(pathName1);
    File input2 = new File(pathName2);
    boolean funny = false;
    for (File file1 : input1.listFiles()) {
      String id = file1.getName();
      if (id.equals(".DS_Store") || id.equals("..out") || id.equals(".out"))
        continue;
      String out1 = FileUtils.readFileToString(file1);
      if (out1.indexOf("NOT_FUNNY") > -1) {
        funny = false;
        not_fun++;
      } else {
        funny = true;
        fun++;
      }
      File file2 = new File(pathName2 + "/" + id);
      String out2 = FileUtils.readFileToString(file2);
      if ((out2.indexOf("positive") != -1) && funny == true) {
        positiveFunny++;
      } else if ((out2.indexOf("positive") != -1) && funny == false) {
        positiveNotFunny++;
      } else if ((out2.indexOf("negative") != -1) && funny == true) {
        negativeFunny++;
      } else if ((out2.indexOf("negative") != -1) && funny == false) {
        negativeNotFunny++;
      }
    }
  }

  /**
   * Method to parse Comedy+Categorical Sentiment output files and analyse them
   * 
   * @param pathName1
   *          first file
   * @param pathName2
   *          second file
   * @throws IOException
   */
  public void parseCateg(String pathName1, String pathName2)
      throws IOException {
    File input1 = new File(pathName1);
    File input2 = new File(pathName2);
    boolean funny = false;
    for (File file1 : input1.listFiles()) {
      String id = file1.getName();
      if (id.equals(".DS_Store") || id.equals("..out") || id.equals(".out"))
        continue;
      String out1 = FileUtils.readFileToString(file1);
      if (out1.indexOf("NOT_FUNNY") > -1) {
        funny = false;
      } else {
        funny = true;
      }
      File file2 = new File(pathName2 + "/" + id);
      String out2 = FileUtils.readFileToString(file2);
      if ((out2.indexOf("angry") != -1) && funny == true) {
        angryFunny++;
      } else if ((out2.indexOf("angry") != -1) && funny == false) {
        angryNotFunny++;
      } else if ((out2.indexOf("sad") != -1) && funny == true) {
        sadFunny++;
      } else if ((out2.indexOf("sad") != -1) && funny == false) {
        sadNotFunny++;
      } else if ((out2.indexOf("neutral") != -1) && funny == true) {
        neutralFunny++;
      } else if ((out2.indexOf("neutral") != -1) && funny == false) {
        neutralNotFunny++;
      } else if ((out2.indexOf("like") != -1) && funny == true) {
        likeFunny++;
      } else if ((out2.indexOf("like") != -1) && funny == false) {
        likeNotFunny++;
      } else if ((out2.indexOf("love") != -1) && funny == true) {
        loveFunny++;
      } else if ((out2.indexOf("sad") != -1) && funny == false) {
        loveNotFunny++;
      }
    }

  }

  public static void main(String[] args) throws IOException {
    OutputAnalyzer analyzer = new OutputAnalyzer();
  }

}
