/*
 * Trinity College Senior Project 2018
 */

package edu.trincoll;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import com.opencsv.CSVReader;

/**
 * Class to parse data for sentiment analysis that is stored in a specific CSV
 * format (for Twitter model)
 * 
 * @author Anastasija Mensikova
 * @version 1.0 Nov. 2017
 * @see BufferedReader
 * @see PrintWriter
 */

public class SentimentCSVParser {

  private final String inputFileName = "../../datasets/SentimentAnalysisDataset.csv";
  private final String outputFileName = "../../datasets/new_sentiment/new-sentiment-dataset";
  private PrintWriter outputStream;

  /**
   * Constructor to intialise the output stream
   * 
   * @throws IOException
   */
  public SentimentCSVParser() throws IOException {
    Path outputFileName = Paths.get(this.outputFileName);
    Charset encoding = Charset.forName("UTF-8");
    outputStream = new PrintWriter(
        Files.newBufferedWriter(outputFileName, encoding));
  }

  /**
   * Method to read the CSV files
   * 
   * @throws IOException
   */
  public void read() throws IOException {
    Path inputFile = Paths.get(this.inputFileName);
    CSVReader csvReader = new CSVReader(new FileReader(inputFileName));

    String[] nextLine = csvReader.readNext(); // column headers
    int sentimentScorePosition = getHeaderLocation(nextLine, "Sentiment");
    int textPosition = getHeaderLocation(nextLine, "SentimentText");

    while ((nextLine = csvReader.readNext()) != null) {
      String sentiment = "";
      String textOrig = nextLine[textPosition];
      String textNew = textOrig.trim().replaceAll(" +", " ");
      if (nextLine[sentimentScorePosition].equals("0")) {
        sentiment = "negative";
      } else {
        sentiment = "positive";
      }
      outputStream.write(sentiment + " " + textNew + "\n");
    }
    outputStream.close();
    csvReader.close();
  }

  /**
   * Helper method to help identify the column number in the CSV
   * 
   * @param headers
   *          array of headers in a file
   * @param columnName
   *          the name of the column searched for
   * @return column name
   */
  private int getHeaderLocation(String[] headers, String columnName) {
    return Arrays.asList(headers).indexOf(columnName);
  }

  public static void main(String[] args) throws IOException {
    SentimentCSVParser parser = new SentimentCSVParser();
    parser.read();
  }

}
