package edu.trincoll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PrepareTestData {

  private static String inputFile = "../../datasets/new_sentiment/new-sentiment-test";
  private static String outputFolder = "../sentiment-examples/src/main/resources/test/new-sentiment-test";
  private static String outputFile = "../../datasets/new_sentiment/new-sentiment-test-unlabeled";

  private static final String labels = "../../datasets/new_sentiment/new-sent-test-labels";
  private static final int num = 149356;

  public PrepareTestData() {
  }

  public void saveLabels() throws IOException {
    Path inputFileName = Paths.get(inputFile);
    Path outputFileName = Paths.get(labels);
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

  public void removeLabels() throws IOException {
    Path inputFileName = Paths.get(inputFile);

    BufferedReader reader = Files.newBufferedReader(inputFileName,
        Charset.forName("UTF-8"));

    String line;
    int i = 0;
    while ((line = reader.readLine()) != null) {
      String content = line.substring(line.indexOf(" ") + 1);
      String name = Integer.toString(i);
      Path output = Paths.get(outputFolder, "/" + name + ".sent");
      PrintWriter fileWriter = new PrintWriter(
          Files.newBufferedWriter(output, Charset.forName("UTF-8")));
      fileWriter.write(content);
      fileWriter.close();
      i++;
    }
    reader.close();
  }

  public static void main(String[] args) throws IOException {
    PrepareTestData prepare = new PrepareTestData();
    prepare.removeLabels();
  }

}
