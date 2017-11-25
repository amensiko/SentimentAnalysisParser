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

/*
 * Class to parse the dataset of youtube video IDs and their labels
 */
public class ComedyParser {

  private static String inputFileTrain = "../../datasets/comedy_comparisons/comedy_comparisons.train";
  private static String inputFileTest = "../../datasets/comedy_comparisons/comedy_comparisons.test";
  private static String outputFile;
  private PrintWriter outputStream;

  public ComedyParser(String out) throws IOException {
    this.outputFile = out;
    Path outputFileName = Paths.get(outputFile);
    Charset encoding = Charset.forName("UTF-8");
    outputStream = new PrintWriter(
        Files.newBufferedWriter(outputFileName, encoding));
  }

  public ComedyParser() {
    String outFolder = "";
  }

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

  public void createTestFiles() {

  }

  public static void main(String[] args) throws IOException {
    ComedyParser parser = new ComedyParser(
        "../sentiment-examples/src/main/resources/train/comedy-train-labels");
    parser.createTrainFile();
    //ComedyParser parser = new ComedyParser();
  }

}
