package edu.trincoll;

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

public class SentimentCSVParser {
  
  private final String inputFileName = "../../datasets/SentimentAnalysisDataset.csv";
  private final String outputFileName = "../../datasets/new_sentiment/new-sentiment-dataset";
  private PrintWriter outputStream;
  
  public SentimentCSVParser() throws IOException {
    Path outputFileName = Paths.get(this.outputFileName);
    Charset encoding = Charset.forName("UTF-8");
    outputStream = new PrintWriter(
        Files.newBufferedWriter(outputFileName, encoding));
  }
  
  public void read() throws IOException {
    Path inputFile = Paths.get(this.inputFileName);
    CSVReader csvReader = new CSVReader(new FileReader(inputFileName));
    
    String[] nextLine = csvReader.readNext(); //column headers
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
  
  private int getHeaderLocation(String[] headers, String columnName) {
    return Arrays.asList(headers).indexOf(columnName);
  }
  
  public static void main(String[] args) throws IOException {
    SentimentCSVParser parser = new SentimentCSVParser();
    parser.read();
  }
  
}
