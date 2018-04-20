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
import org.memex.TestModel;

/**
 * Class to parse the results of running various models on test data NOTE: Has
 * been modified multiple times to fit the situation
 * 
 * @author Anastasija Mensikova
 * @version 5.0 Jan. 2018
 * @see BufferedReader
 * @see PrintWriter
 */

public class ModelPerformanceAnalyzer {

  private String correctLabels = "../../../SarcasmAnalysis/datasets/sarcasm_test_labels-v2";
  private String resultsFolder = "../sentiment-examples/src/main/resources/out/sarcasm-test-out-v2";
  private String outputFile = "../../../SarcasmAnalysis/datasets/sarc_roc_data-v2.tsv";
  private PrintWriter outputStream;
  private TprFpr[] tprFpr;
  private static final int SIZE = 64666;

  private double truePositive = 0;
  private double trueNegative = 0;
  private double falsePositive = 0;
  private double falseNegative = 0;

  /**
   * Inner private class to keep track of TPR and FPR
   * 
   * @author Anastasija Mensikova
   * @version 1.0 Nov. 2017
   */
  private class TprFpr {
    private double tpr = 0.0;
    private double fpr = 0.0;

    /**
     * Constructor for initialisation
     * 
     * @param fpr
     *          false positive rate
     * @param tpr
     *          true positive rate
     */
    public TprFpr(double fpr, double tpr) {
      this.tpr = tpr;
      this.fpr = fpr;
    }

    /**
     * Method to get the current TPR
     * 
     * @return tpr
     */
    public double getTpr() {
      return this.tpr;
    }

    /**
     * Method to get the current FPR
     * 
     * @return fpr
     */
    public double getFpr() {
      return this.fpr;
    }

  }

  /**
   * Constructor to intialise the output stream
   * 
   * @throws IOException
   */
  public ModelPerformanceAnalyzer() throws IOException {
    Path outputFileName = Paths.get(outputFile);
    Charset encoding = Charset.forName("UTF-8");
    outputStream = new PrintWriter(
        Files.newBufferedWriter(outputFileName, encoding));
  }

  /**
   * Method that prepares ROC data
   * 
   * @throws IOException
   */
  public void roc() throws IOException {
    tprFpr = new TprFpr[SIZE];
    outputStream.write("fpr\ttpr\n");
    double truePos = 0.0;
    double trueNeg = 0.0;
    double falsePos = 0.0;
    double falseNeg = 0.0;
    int sampleNum = 0;
    compareLabels();
    double totalTruePos = truePositive;
    double totalFalsePos = falsePositive;
    String result = ""; // TN, TP, FN, FP
    double tpr = 0.0, fpr = 0.0;
    Path labels = Paths.get(correctLabels);
    BufferedReader reader = Files.newBufferedReader(labels,
        Charset.forName("UTF-8"));
    int i = 0;
    String label;
    while ((label = reader.readLine()) != null) {
      File respectiveOutFile = new File(
          resultsFolder + "/" + String.valueOf(i) + ".out");
      String outputLabel = FileUtils.readFileToString(respectiveOutFile);
      if (label.equals("positive") && outputLabel.indexOf("positive") > -1) { // TP ==> TR
        result = "TP";
        truePos++;
        tpr = truePos / totalTruePos;
        fpr = falsePos / totalFalsePos;
      } else if (label.equals("negative")
          && outputLabel.indexOf("positive") > -1) { // FP ==> FR
        result = "FP";
        falsePos++;
        fpr = falsePos / totalFalsePos;
        tpr = truePos / totalTruePos;
      }
      tprFpr[sampleNum] = new TprFpr(fpr, tpr);
      System.out.println(
          tprFpr[sampleNum].getFpr() + "\t" + tprFpr[sampleNum].getTpr());
      outputStream.write(tprFpr[sampleNum].getFpr() + "\t"
          + tprFpr[sampleNum].getTpr() + "\n");
      sampleNum++;
      i++;
    }
    reader.close();
    outputStream.close();
  }

  /**
   * Method to calculate label ratios, compared to the actual labels
   * 
   * @throws IOException
   */
  public void compareLabels() throws IOException {
    File input1 = new File(resultsFolder);
    File input2 = new File(correctLabels);
    Path labels = Paths.get(correctLabels);
    BufferedReader reader = Files.newBufferedReader(labels,
        Charset.forName("UTF-8"));
    int i = 0;
    String label;
    while ((label = reader.readLine()) != null) {
      File respectiveOutFile = new File(
          resultsFolder + "/" + String.valueOf(i) + ".out");
      String outputLabel = FileUtils.readFileToString(respectiveOutFile);
      if (label.equals("positive") && outputLabel.indexOf("positive") > -1) {
        truePositive++;
      } else if (label.equals("positive")
          && outputLabel.indexOf("negative") > -1) {
        falseNegative++;
      } else if (label.equals("negative")
          && outputLabel.indexOf("negative") > -1) {
        trueNegative++;
      } else if (label.equals("negative")
          && outputLabel.indexOf("positive") > -1) {
        falsePositive++;
      }
      i++;
    }
    reader.close();

  }

  /**
   * Method that prepares ROC data for the comedy model
   * 
   * @throws IOException
   */
  public void rocComedy() throws IOException {
    tprFpr = new TprFpr[SIZE];
    outputStream.write("fpr\ttpr\n");
    double truePos = 0.0;
    double trueNeg = 0.0;
    double falsePos = 0.0;
    double falseNeg = 0.0;
    int sampleNum = 0;
    compareLabelsComedy();
    double totalTruePos = truePositive;
    double totalFalsePos = falsePositive;
    String result = ""; // TN, TP, FN, FP
    double tpr = 0.0, fpr = 0.0;
    Path labels = Paths.get(correctLabels);
    BufferedReader reader = Files.newBufferedReader(labels,
        Charset.forName("UTF-8"));
    int i = 0;
    String label;
    while ((label = reader.readLine()) != null) {
      File respectiveOutFile = new File(
          resultsFolder + "/" + String.valueOf(i) + ".out");
      String outputLabel = FileUtils.readFileToString(respectiveOutFile);
      if (label.equals("funny") && outputLabel.indexOf("funny") > -1) {
        result = "TP";
        truePos++;
        tpr = truePos / totalTruePos;
        fpr = falsePos / totalFalsePos;
      } else if (label.equals("not_funny")
          && outputLabel.indexOf("funny") > -1) { // FP ==> FR
        result = "FP";
        falsePos++;
        fpr = falsePos / totalFalsePos;
        tpr = truePos / totalTruePos;
      }
      tprFpr[sampleNum] = new TprFpr(fpr, tpr);
      System.out.println(
          tprFpr[sampleNum].getFpr() + "\t" + tprFpr[sampleNum].getTpr());
      outputStream.write(tprFpr[sampleNum].getFpr() + "\t"
          + tprFpr[sampleNum].getTpr() + "\n");
      sampleNum++;
      i++;
    }
    reader.close();
    outputStream.close();
  }

  /**
   * Method to calculate comedy model label ratios, compared to the actual
   * labels
   * 
   * @throws IOException
   */
  public void compareLabelsComedy() throws IOException {
    File input1 = new File(resultsFolder);
    File input2 = new File(correctLabels);
    Path labels = Paths.get(correctLabels);
    BufferedReader reader = Files.newBufferedReader(labels,
        Charset.forName("UTF-8"));
    int i = 0;
    String label;
    while ((label = reader.readLine()) != null) {
      File respectiveOutFile = new File(
          resultsFolder + "/" + String.valueOf(i) + ".out");
      String outputLabel = FileUtils.readFileToString(respectiveOutFile);
      if (label.equals("funny") && outputLabel.indexOf("funny") > -1) {
        truePositive++;
      } else if (label.equals("funny")
          && outputLabel.indexOf("not_funny") > -1) {
        falseNegative++;
      } else if (label.equals("not_funny")
          && outputLabel.indexOf("not_funny") > -1) {
        trueNegative++;
      } else if (label.equals("not_funny")
          && outputLabel.indexOf("funny") > -1) {
        falsePositive++;
      }
      i++;
    }
    reader.close();

  }

  public static void main(String[] args) throws IOException {
    ModelPerformanceAnalyzer analyzer = new ModelPerformanceAnalyzer();
    analyzer.compareLabelsComedy();
    analyzer.rocComedy();
  }

}
